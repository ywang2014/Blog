# Mybatis PageHelper分页遇到的坑
PageHelper是一个非常好用的Mybatis分页插件，使用简单方便，对sql无侵入。

#### 现象
频繁点击查询的时候，发现搜索的列表页有的时候能够正常查询到数据，有的时候不能正常查询到数据。

1.首先发现问题是偶发的。

2.一直点击不能复现问题，需要通过不同的查询操作，然后才能复现问题。

#### 解决过程
1.偶发问题，首先猜测缓存问题，是不是某些情况下，redis取值失败了等。

2.增加相关的日志，继续重现问题。发现不是redis缓存问题，redis取值，获取权限都是正常的。发现数据库取值有问题，有的时候是全量数据，有的时候不是全量的。猜测数据库有问题，但是不应该啊，咋这么奇怪的问题。

3.增加数据库日志到console，在logback-spring.xml中增加如下配置，打印出mybatis的执行日志。

	<springProfile name="local">
        <logger name="com.bytedance.ngcc.ticket.dao" level="DEBUG" />
    </springProfile>

4.通过日志发现了差异情况。结果不正常的时候，mysql查询竟然有分页逻辑，自动进行了分页。程序代码中明明没有使用分页逻辑的，但是程序执行时是有的，百思不得其解。问题只可能出现在page helper上面，因为程序中的分页逻辑，都是page helper处理的，没有在程序中写相关的业务逻辑。

5.google查了一下，发现早就有人出现这个case了，page helper有一个坑，莫名其妙的增加limit ？？。问题出现原因和pageHelper分页原理有关，PageHelper采用ThreadLocal来进行分页标识设置，通过Executor拦截器处理分页逻辑，执行分页逻辑之后，在finally中清理ThreadLocal中的分页标示。

#### 解决方法
1.PageHelper.clearPage() 方法，显式的清理hreadLocal。（Java有些版本没有此方法）

2.PageHelper.startPage()和SQL放在一起使用，也能避免ThreadLocal污染问题。

#### 总结
1.使用框架时，一定要熟悉框架实现原理，机制，存在哪些风险，问题等，否则乱用框架，容易踩坑。最麻烦的是找不到错误原因，无法解决问题。

2.使用ThreadLocal的时候，一定要小心，最好用完直接remove掉，不能等垃圾回收销毁，因为垃圾回收需要时间，线程id一致的时候，threadLocal会将线程当作一个线程，就会出现线程污染，而且问题不容易复现，感觉是随机问题，偶发现象。使用线程池的时候，更加容易发现此类问题。


**2019-11-17**
	