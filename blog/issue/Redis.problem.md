
# Redis问题总结

## Redis Connection refused问题

#### 现象
1.如果服务一直正常，突然发生这样的问题，请检查redis链接情况。

	redis-cli -h 127.0.0.1 -p 6080	（具体的ip和port）

	1.如果redis可以正常链接，说明redis server服务正常，请排查其他问题。

	2.如果redis不能正常链接，说明redis server服务异常，请换ip和port，特别是服务给出的psm、集群模式等，ip和port经常变更。
		建议：不要直接使用ip和port模式，请使用服务发现的方式，解决这个问题。

2.如果配置服务就遇到这个问题。

#### 解决办法
1.redis服务遇到问题，比如：服务挂了，服务切换了等问题。使用正确的redis即可，最后不要直接使用物理机器的ip port，建议使用：代理，或者注册中心进行服务发现，自动刷新等。减少服务遇到问题，需要重启等问题。

## RedisTemplate 问题
#### 1.key序列化问题

###### 现象

1.RedisTemplate直接set值，key在redis中存储的不是目标的key，而是被添加了一个乱码前缀。直接通过redis客户端查询key，就会发现查询不到，在程序中，通过RedisTemplate直接get这个key，是可以查询到正确的结果的。

2.在RedisTemplate中，利用redisTemplate.execute()方法，批量执行redis命令，比如：利用管道批量插入，同时又要设置过期时间，就不能直接使用redisTemplate.muliSet()。这种情况下，插入的key，在redis中存储的是目标的key，没有乱码前缀。与上面情况刚刚相反，直接通过redis客户端查询key，可以正常查询到，但是在程序中，通过RedisTemplate直接get这个key，查询不到任何结果。

3.redis集群模式，排查这个问题，非常坑。
	
	1.集群模式禁止了keys命令，因此无法使用正则匹配可能的key，只能按照正常的key时匹配。因此很难发现，key被加了前缀。
	2.集群使用consul服务发现方式，连接的，不能非常确定，已经连接到了目标物理机器。
	3.通过物理机器连接，在集群模式下，找key是通过hash策略，定位目标机器的，不管目标机器是否真正的存在这个可以。
	4.实际机器上，发现有些key有（管道插入的key），有些key没有（直接插入的key）。这个是最坑的。

###### 原因
spring redis自带的序列化方式的问题，使用RedisTemplate时，没有设置redisTemplate的key序列化方式，reidsTemplate就会使用默认的序列化，默认的方式有问题，就导致了这个问题。

###### 解决方法
1.调整RedisTemplate的key序列化方式，比如：redisTemplate.setKeySerializer(new StringRedisSerializer()); 设置redis key的序列化和反序列化方式为StringRedisSerializer。

2.问题定位和排查，不要直接使用集群模式，最好先使用单机模式，本地测试环境，支持使用keys一类的命令，方便发现定位问题。



**2019-07-24**

