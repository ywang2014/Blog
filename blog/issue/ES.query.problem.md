# ES查询问题总结

## 问题现象
1.使用ES查询时，小数据量查询没有什么问题，查询小数据量也没有什么问题，但是如果是大数据量，需要查询超过10000的数据时，es客户端会抛异常。比如Java的ES客户端抛出异常如下：

Result: {"error":{"root_cause":[{"type":"query_phase_execution_exception","reason":"Result window is too large, from + size must be less than or equal to: [10000] but was [10010]. See the scroll api for a more efficient way to request large data sets. This limit can be set by changing the [index.max_result_window] index level setting."}],"type":"search_phase_execution_exception","reason":"all shards failed","phase":"query","grouped":true,"failed_shards":[{"shard":0,"index":"ngcc_ticket_v1","node":"01Fl4q42R_uvznBY-HpKdA","reason":{"type":"query_phase_execution_exception","reason":"Result window is too large, from + size must be less than or equal to: [10000] but was [10010]. See the scroll api for a more efficient way to request large data sets. This limit can be set by changing the [index.max_result_window] index level setting."}}]},"status":500}, isSucceeded: false, response code: 500, error message: {"root_cause":[{"type":"query_phase_execution_except...

这个异常信息非常好，不仅给出了错误原因，还给出了解决方案。简单来说，就是查询窗口太大，超过了10000.

## 问题分析
10000是ES查询结果窗口最大值的默认值（max_result_window），查询超过1w的数据时，就会触发异常，导致查询失败。这个约束针对所有ES客户端的，Java、python、DSL等查询最大数据量都不能超过max_result_window限制，否则就会查询失败。

max_result_window是ES集群的一个可配置属性，支持用户修改的。因此解决上面问题的最简单方式，就是修改这个配置即可，比如：我们可以设置max_result_window=100w，这样就能解决查询超过1w数据的问题了。

这是合适的方式吗，最佳方式吗？

#### pagesize错误分析
pagesize错误很好理解，当用户一次查询数据量超过10000数据，会报错。即：pagesize > 10000时，肯定不能支持查询的。正常需求其实都会限制查询数量的，因为一次查询太多数据对用户来说完全没有意义，用户需要能够查询几百的数据就够了，数据太多了反而都是垃圾数据，用户没有利用。现在我们常常不是烦恼缺少数据，而是烦恼数据太多了，找不到目标数据。

查询大量的数据，直接放入内存，如果不加限制，容易导致内存溢出，out of memory。

因此程序通常会约束：max_pagesize <= 10000. 如果缺少这个约束，程序是有潜在危险的。数据量非常大的时候，如果没有限制用户的查询，一次查询太多数据到内存中，会撑爆内存的，直接导致服务崩溃，不可用。物理机器，或者说服务器的内存是有限的，但是磁盘、硬盘往往非常大，内存肯定装不下太多的查询数据。

#### pageindex错误分析
pageindex错误不太直观，一不小心就会踩坑。比如当用户查询第1001页，每页只有10个数据时，es查询也会失败，异常报错，这个报错就很奇怪了，查询的数据量明明只有10，没有超过10000，为什么报错呢。

因为查询的数据范围是[10001, 10010]，超过了ES的默认最大查询窗口10000，导致es查询失败。查询窗口的概念比较少见，ES和mysql实现机制不一样，mysql的limit非常大时，正常情况下不会报错抛异常（当然也可能查询太慢，遇到超时异常），只是不建议用户使用，用户还是可以查的，也支持查询，就是性能差而已，ES查询窗口太大时，server端直接不支持查询，返回错误。

这种错误，程序约束就不能是简简单单的max_pagesize <= 10000，需要使用(page x size) <= 10000 进行约束才行。

## 解决办法
#### from-size"浅"分页
修改max_result_window，支持大数据范围查询。

	1.PUT /_settings -d '{ "index" : { "max_result_window" : 1000000}}
	2.直接修改es集群配置

max_result_window足够大的时候，确实可以支持from-size查询非常大范围的数据，但是这种查询会查询出所有数据，然后剔除不要的部分，因此性能较差，ES不推荐。

#### scroll深分页
使用scroll api支持大数据范围查询。

scroll其实是维护了一份当前索引段的快照信息，每次执行scroll查询时，es会保存一份这个查询的快照，每次查询会记录一个读取的位置，保证下一次快速继续读取。scroll查询时会自动返回一个scroll_id，第二次查询需要通过scroll_id继续查询后面的数据，直到查询出全部数据为止。（注意，在某次scroll查询后，任何新索引进来的数据，都不会在这个快照中查询到。）

scrollId优化，类似于mysql查询，记录表id，查询时，增加where id > xxx这样的查询条件，进行优化。

一次性查询大量的数据（甚至是全部的数据），es推荐使用scroll模式。

*坑：scroll查询不支持from参数*

{"error":{"root_cause":[{"type":"action_request_validation_exception","reason":"Validation Failed: 1: using [from] is not allowed in a scroll context;"}],"type":"action_request_validation_exception","reason":"Validation Failed: 1: using [from] is not allowed in a scroll context;"},"status":400}

scroll查询，不支持from参数，from参数如果有，则只能是0，否则查询会失败，报错。

#### 对比scroll查询和from-size查询
1.from-size查询实现简单，性能有瓶颈。

2.scroll查询实现复杂，大范围（全量）数据查询性能有优势。

## Java解决方案

	// 通用查询接口
	public EsQueryResult queryByParams(SearchSourceBuilder searchSourceBuilder, Integer pageIndex, Integer pageSize) {
        log.info("ES query tickets start, pageIndex: {}, pageSize: {}", pageIndex, pageSize);
        int from = (pageIndex - 1) * pageSize;
        if (pageIndex * pageSize < Constant.MAX_RESULT_WINDOW) {
            searchSourceBuilder.from(from);
            searchSourceBuilder.size(pageSize);
            return queryByDirect(searchSourceBuilder);
        }
        return queryByScroll(searchSourceBuilder, from, pageSize);
    }

    // scroll 查询接口
    private EsQueryResult queryByScroll(SearchSourceBuilder searchSourceBuilder, int from, int size) {
        log.info("ES query tickets by scroll start, params: {}", searchSourceBuilder.toString());
        long maxSize = Constant.MAX_PAGE_SIZE;
        EsQueryResult esQueryResult = new EsQueryResult();
        try {
            JestResult result = jestClient.execute(new Search.Builder(searchSourceBuilder.toString())
                    .addIndex(esConfig.getTicketIndex())
                    .addType(esConfig.getTicketType())
                    .setParameter(Parameters.SIZE, maxSize)
                    .setParameter(Parameters.SCROLL, StringConstants.SCROLL_TIME)
                    .build());

            if (Objects.isNull(result) || result.getResponseCode() != 200) {
                log.error("ES query tickets by scroll error, result: {}", result);
                esQueryResult.setTotal(-1);
                return esQueryResult;
            }

            log.info("ES query tickets by scroll result: {}", result.getJsonString());
            long total = result.getJsonObject().getAsJsonObject("hits").get("total").getAsLong();
            esQueryResult.setTotal(total);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            JsonArray hits = result.getJsonObject().getAsJsonObject("hits").getAsJsonArray("hits");
            List<EsHitsObject> hitsList = gson.fromJson(hits, new TypeToken<List<EsHitsObject>>() {}.getType());
            String scrollId = result.getJsonObject().getAsJsonPrimitive("_scroll_id").getAsString();
            while (result.getResponseCode() == 200) {
                result = queryByScrollId(scrollId, maxSize);
                if (Objects.isNull(result) || result.getResponseCode() != 200) {
                    break;
                }
                hits = result.getJsonObject().getAsJsonObject("hits").getAsJsonArray("hits");
                List<EsHitsObject> list = gson.fromJson(hits, new TypeToken<List<EsHitsObject>>() {}.getType());
                if (list.isEmpty()) {
                    break;
                }
                hitsList.addAll(list);
                scrollId = result.getJsonObject().getAsJsonPrimitive("_scroll_id").getAsString();
            }
            List<TicketEsModel> list = hitsList.stream().map(EsHitsObject::get_source).collect(Collectors.toList());
            esQueryResult.setTicketList(CollectionUtil.pageList(list, from, size));
        } catch (Exception e) {
            log.error("ES Search document error.searchSourceBuilder {} , exception:",searchSourceBuilder, e);
            esQueryResult.setTotal(-1);
        }
        return esQueryResult;
    }

    // es普通查询
    private EsQueryResult queryByDirect(SearchSourceBuilder searchSourceBuilder) {
        log.info("ES query tickets directly start, params: {}", searchSourceBuilder.toString());
        EsQueryResult esQueryResult = new EsQueryResult();
        try {
            SearchResult result = jestClient.execute(new Search.Builder(searchSourceBuilder.toString())
                    .addIndex(esConfig.getTicketIndex())
                    .addType(esConfig.getTicketType())
                    .build());
            if (Objects.isNull(result) || result.getResponseCode() != 200) {
                log.error("ES query tickets directly error, result: {}", result);
                esQueryResult.setTotal(-1);
                return esQueryResult;
            }
            log.info("ES query tickets directly, result: {}", result.getJsonString());
            List<SearchResult.Hit<TicketEsModel, Void>> hits = result.getHits(TicketEsModel.class);
            esQueryResult.setTotal(result.getTotal());
            if (CollectionUtils.isEmpty(hits)) {
                return esQueryResult;
            }
            List<TicketEsModel> ticketEsModelList = hits.stream().map(x -> x.source).collect(Collectors.toList());
            esQueryResult.setTicketList(ticketEsModelList);
        } catch (Exception e) {
            log.error("ES Search document error.param {}, exception: ", searchSourceBuilder, e);
            esQueryResult.setTotal(-1);
        }
        return esQueryResult;
    }

## 总结
1.scroll 查询的高级优化，scroll-scan查询优化，特别是深分页的场景。比如需要查询区间[10100，10200]的数据场景。
	
	1.在scroll查询中，如果已经取到了超过10200的数据，但是scroll查询还有更多的数据没有查询，程序其实已经没有必要继续查询es了，因为已经完整的获取到了用户需要的数据，后续的while查询都是性能浪费。
		对应的程序优化：在while条件中增加数据size检查条件，减少不必要的es查询。
	2.在scroll查询中，如果每次查询1w的数据，则前10000的数据，可以直接丢弃，不需要保持在list中，因为查询的目标数据，肯定不在这个结果中，程序只需要维护查询的区间数量就行，减少查询过程中，大量数据放在内存中，对服务性能的影响。
		对应的程序优化：list.addAll()的时候，增加边界条件判断，仅把目标数据所在的区间，放在list中即可，然后进行分页。

2.ES查询字段类型问题，keyword支持精确查询，long/int等类型精确查询性能很差，因此id之类的字段，创建mapping的时候，建议字段类型为：keyword，或者多类型。number数据，尽量使用range查询，即使是精确查询，可以使用 <= x and >=x 查询实现。

**2020-01-09**