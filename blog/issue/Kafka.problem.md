
# kafka问题总结

## kafka原理简述
kafka消息机制保证：一个topic的一条消息，只能被一个consumer group中的一个consumer消费，不同consumer group中的consumer间无相互关系。

&emsp;&emsp;一个topic可以注册很多consumer group，一个consumer group可以有很多consumer，同一个consumer group中consumer是相互竞争关系，只有一个consumer能够成功消费到消息。

&emsp;&emsp;一个topic实际是由多个partition组成的，单个parition内是保证消息有序，遇到瓶颈时，可以通过增加partition的数量来进行横向扩容。

&emsp;&emsp;consumer实际是订阅topic的partition，一个consumer group中的全部consumer共同订阅全部的partition。如果一个topic有6个partition，一个consumer group只有一个consumer，则consumer订阅全部的6个partiotion，获取到消息的概率100%；如果有两个consumer，则两个consumer各自订阅3个partition，获取到消息的概率二分之一；如果有6个consumer，则6个consumer各自订阅一个partition，获取到消息的概率是六分之一。

&emsp;&emsp;kafka的server对应一个个的broker，broker里面有多个partition，不同broker间的partition互为主备，优点：保证稳定性的同时，节省资源。比如：需要部署一个kafka集群：12个partition，一主两从模式。一个broker可以有4个partition。不需要9台机器（12 / 4 * （1+2）），只需要{{1,2,3,4}{2,3,4,5}{3,4,5,6}{5,6,7,8}{6,7,8,9}{7,8,9,10}{9,10,11,12}{10,11,12,1}{11,12,1,2}}

## 消息太快问题
&emsp;&emsp;业务中，系统交互分两步走，第一步创建一个流程，创建流程之后，根据流程id拉取节点的处理人。常规的处理逻辑是：分别调用两次接口，即可完成任务。因为这个场景是两个系统交互，为了系统解耦，借用kafka消息中间件，监听kafka推送的消息，获取处理人消息。创建流程成功之后，业务才会创建一个工单，工单不允许没有流程关联，否则就没有人处理这个工单。遇到问题：工单还没有创建成功（入库成功），处理人消息，kafka已经推送过来了。

#### 解决方案
1.先创建工单，再创建工作流，关联取处理人。如果工作流启动失败，撤销创建的工单。比如：回滚

2.接受kafka消息，检查工单是否创建，如果没有创建，本地自旋，等待一会儿。工单创建成功之后，再关联处理人。

## 消息太慢问题
&emsp;&emsp;业务中遇到，接口请求很久了，就是没有收到kafka消息，等了很久很久，30分钟以后，才接收到消息，导致实际服务用户不可用。

#### 解决方案
1.推拉结合方式：kafka推送消息+主动拉取消息。注意：消息互补，重复消息幂等性等问题。拉取失败，需要有补偿机制。

2.查找kafka消息缓慢的原因。
    
    1.是否是consumer假死，如果是，解决consumer逻辑问题。
    2.是否是没有产生消息，如果是，排查上游服务。
    3.是否是kafka服务问题，是否有消息堆积。如果是，请kafka同学查看具体原因。

## 重复消息问题
&emsp;&emsp;消费者出现重复消费，甚至出现持续消费但队列里的消息却未见减少的情况。

#### 问题原因
&emsp;&emsp;在consumer消费性能慢的情况下，consumer配置为手动提交，在指定时间内无法自动提交，如果触发rebalanced，数据重新发送到新的consumer上消费，就会出现数据重复消费问题，如果一直都在间隔时间内无法完成消费，就会出现重复消费但offset不变的死循环。

#### rebalanced现象原因
consumer和consumer group的关系是动态维护的，并不固定，当某个consumer卡住或者挂掉时，该consumer订阅的partition会被重新分配给该group下其它consumer，用于保证服务的可用性。

&emsp;&emsp;为维护consumer和group之间的关系，consumer会定期向服务端的coordinator(一个负责维持客户端与服务端关系的协调者)发送心跳heartbeat，当consumer因为某种原因如死机无法在session.timeout.ms配置的时间间隔内发送heartbeat时，coordinator会认为该consumer已死，它所订阅的partition会被重新分配给同一group的其它consumer，该过程叫：rebalanced。
    
    1.超过session.timeout.ms没有发送心跳就直接rebalance。
    2.max.poll.interval.ms，即最大的poll时间间隔。

&emsp;&emsp;consumer是通过拉取的方式向服务端拉取数据，当超过指定时间间隔max.poll.interval.ms没有向服务端发送poll()请求，而心跳heartbeat线程仍然在继续，会认为该consumer锁死，就会将该consumer退出group，并进行再分配。

poll方法与consumer挂勾，但并不是consumer消费完数据都会调用poll方法，除非你手动调用。需要结合max.poll.records一起考虑。

max.poll.records 每次poll的数据条数。

consumer实际一次poll一批数据，并将数据缓存在本地，consumer消费完records后，才会调用poll方法。

因此，consumer的消费时长 * max.poll.records > max.poll.interval.ms 才会真正出现rebalanced。

#### 解决方案
1.consumer配置自动提交，不要手动提交。

2.consumer消息消费逻辑简单，处理速度快。

&emsp;&emsp;consumer如果为了安全性，必须执行成功，才能手动提交。业务中复杂逻辑处理，可以交给线程池，异步去处理，保证收到消息即可。异步处理中，尽可能保证一定处理成功。比如：增加重试机制，或者增加任务队列，如果任务执行失败，提交给延迟队列，稍后处理。**（注意重试次数，和重试时间间隔）**

3.修改consumer中相关的配置：max.poll.interval.ms增大，max.poll.records减小

**2019-07-16**

