
# Controller 类的方法修饰符问题
## 现象
业务增加metrics打点之后，发现一个controller接口报空指针，debug一看，竟然是注入的service为null。diff代码对比发现，没有啥异常情况啊，感觉代码都是正常的，首先猜测是不是加入的什么jar包冲突，导致不能正常注入。

现象1：把新加入的功能注释之后，服务正常了，加入相关的代码，就有异常。

现象2：通过调试发现，其他controller类可以正常工作，service注入正常，接口调用正常。

现象3：进一步验证，发现同一个类中，仅只有一个接口调用异常。其他接口调用的时候，service注入正常，但是出错接口调用时，service注册，就是异常。（说明：service在每次接口调用的时候，都是单独注册的，一次次注册的？）

## 原因分析
查看代码，发现调用异常的controller方法，是private修饰的，其他正常的接口都是public修饰的，说明这个就是问题的关键。

网上有关于controller方法，用private和public修饰符的讨论，多数结论是没有区别，不管是private修饰方法，还是public修饰方法，Spring都能正常转发和正常调用，因为容器是通过反射调用方法的，没有表现任何区别。大神回答："Java does not provide a mechanism for limiting the target of annotations based on access modifier."

在加入metrics打点之前，我们的服务表现出来的确实是一样的想象，都能够正常访问，没有啥区别。唯一的区别，可能就是IDEA的警告吧，发现private方法，没有被调用。

## 结论
Controller类的方法，如果用private修饰，在这个类中使用自定义注解的时候，这个controller类里面的所有注入类都注入不成功，都是null。猜测：基于代理的AOP只会作用于public的方法。

编程建议：Controller类中的接口，尽量使用public修饰符，减少踩坑。

**2019-07-18**

