# 权限系统设计总结
&emsp;&emsp;任何系统都不能缺少权限控制，没有权限控制的系统就如同没有穿衣服的人一样可怕，在互联网中一直裸奔着。权限控制顾名思义就是对访问系统的用户进行权限约束，禁止用户作出越权的动作，破坏系统。比如：禁止非法用户访问系统的用户数据等，禁止合法用户篡改其他用户的数据等。
&emsp;&emsp;权限控制通常分为：页面菜单控制、功能（页面按钮）控制、数据权限控制。权限控制主要用于控制不同的用户拥有不同的权限（页面、功能、数据权限等），可以看见不同的页面，可以做不同的处理动作，可以看见不同的数据。

#### RBAC模型
最经典的权限模型，RBAC(Role-Based Access Control) 模型，基于角色的访问控制。
     
     **用户 --- n:n --- 角色 --- n:n --- 权限**

用户和角色之间，角色和权限之间，都是多对多的关系。系统可以有很多用户，一个用户可以有很多角色，一个角色可以授予很多用户，一个角色可以有很多权限，一个权限可以被授予很多角色。

#### 角色和权限关联

	1.创建权限     （权限应该是树形结构，森林模式，天然支持分类）
	2.创建角色
	3.用户创建
	4.角色勾选权限
	5.用户勾选角色

#### 用户和角色关联模式
1.基于用户的视角，用户可以选择多个角色。

2.基于角色的视角，角色上可以添加多个用户。

3.自动添加角色模式，减少管理员配置工作。
    
    1.创建用户组概念，用户组关联一个角色，同一个用户组的用户，都自动具备该角色。
    2.角色关联组织架构和职位，标准岗角色，同一个职位的员工，都自动具备该标准岗角色。
    3.用户组，标准岗都是虚拟的概念，方便权限配置工作。

#### 权限授予模式
1.用户申请权限，逐级审批通过后，授予权限，审批流模式。

2.管理员直接为用户配置权限，配置模式。

#### 系统权限隔离
系统隔离，权限系统中，可以创建很多系统，不同的系统，可以有不一样的权限配置和角色配置。

#### 风险控制
1.角色的权限数量控制

2.角色的用户数量控制

3.用户的角色数量控制

4.互斥权限控制，互斥角色控制

## 高级支持
#### 复制功能，提升用户体验。
1.角色复制、角色继承

2.系统复制

#### 操作记录日志功能
1.记录所有用户操作，跟踪权限的创建过程，用户的权限变化过程等。方便定位问题，追查权限错误的原因、责任。

2.一张表current_user_role_table维护正常的【用户】【角色】关联关系，任何新增、添加角色，添加用户组，标准岗都直接插入，记录操作人。

3.一张表history_user_role_table维护删除的【用户】【角色】关联关系，任何删除、减少角色，用户组移除，岗位变化都直接将current表的记录复制到history表，记录操作人，并删除current表的记录。
    
    优点：保证current表没有垃圾数据，插入查询效率高。history表作为历史变更记录，排查问题，定位原因。

#### 附身功能
用户A可以附身用户B，从而具备用户B的权限，方便排查定位用户因为权限不同导致的问题。

## 权限系统框架
Apache Shrio

Spring Security


**2020-01-19**

