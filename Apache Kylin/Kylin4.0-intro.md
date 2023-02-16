# Kylin 4.0 学习笔记

<!-- TOC -->

- [Kylin 4.0 学习笔记](#kylin-40-学习笔记)
  - [log](#log)
  - [Cube 优化篇](#cube-优化篇)
    - [背景](#背景)
    - [优化原理 一句话catch-up](#优化原理-一句话catch-up)
    - [Cube 优化原理](#cube-优化原理)
    - [聚合组（Aggregation Group）](#聚合组aggregation-group)
    - [联合维度（Joint Dimension）](#联合维度joint-dimension)
    - [层级维度（Hierarchy Dimension）](#层级维度hierarchy-dimension)
    - [必要维度（Mandatory Dimension）](#必要维度mandatory-dimension)
    - [其他案例](#其他案例)
  - [精华 catchup](#精华-catchup)
  - [书籍](#书籍)
  - [网页](#网页)
    - [新架构优点](#新架构优点)
  - [源码阅读](#源码阅读)
    - [Kylin 4.0 的构建引擎](#kylin-40-的构建引擎)
    - [Kylin 4.0 的查询引擎](#kylin-40-的查询引擎)
    - [全局字典](#全局字典)
  - [TopN 度量](#topn-度量)
  - [性能优化](#性能优化)
  - [测试](#测试)
  - [kylin 集群搭建](#kylin-集群搭建)
    - [文件获取](#文件获取)

<!-- /TOC -->

- 个人篇
- 出差篇
- Kylin 大概流程/优势/缺点
- 整理出想问的点。

个人觉得kylin 的点就在于把分析师查询时间从100% 降到60%。也就是报表不能覆盖提前跑出的场景。通过cube 提前整理。也就是可能分析师50种想查，预跑出来30种。但是30种可能不好每一个都提前跑报表，不属于必看项。提前跑报表太繁杂，有改动不方便。
1. 查询的可预知性
2. 查询的覆盖程度高（分析师70% 的被加速
3. 跑成PLSQL 比较复杂，可维护性较低



聚合组/联合维度/层级维度/必须维度

1027 看完 《Kylin权威指南》

- [x] 明天看完书，看邮件，想想怎么搞体现自己 的价值
- [x] 字典到底是干嘛的
- [x] segment 是啥
- [ ] debug https://cwiki.apache.org/confluence/display/KYLIN/Development+Guide+for+Kylin+4
- [ ] 总结Cube 原理/目前Kylin 亮点/问题/

农信出差：

- [ ] 业务场景调研？kyligence 的价值体现
- [ ] kylin 4.0 源码 研发角度：从代码层面对接评估 SQL引擎接入，在Quark上创建Job读取Kylin创建的cube文件，完成聚合查询的demo对接。
- [ ] 集群部署review，argo 方案熟悉（各种表创建 尤其范围分区）
- [ ] Kyligence 搞了个KyBot 貌似很有用
- [ ] TOPN 算法我们估计也没有，说白了就是定制化开发，来一条sql 针对性优化一波
- [ ] 相同维度不重复构建

邮件信息：

kyli 并发单节点 仅50。
哪些场景 非常需要 cube 命中？？？
风暴：
1. 分析师急需，但是查询有弹性，报表不一定涉及or涉及成本比较高
2. 。。。
3. 。。。

研发想从农信拿最真实的场景。我这边最好能提供分析能力？！ 怎么去满足这个场景？
优先在 z j n x 上线，搜集一些数据。
数据至少包含：
 a. 成功走kylin和不走kylin的case比例
 b. 成功走kylin的case的业务特点

研发&中台On现场时的计划：
1. 方案部署，根据 固定报表场景 完成Cube创建（尽量参考 Kyligence 或 rubik 的已有经验）
2. 在Kyligence上 单独验证 适合kyligence的固定报表场景，收集数据
3. 拿所有业务（固定报表+灵活查询）验证 前司OLAP数据库+Kyligence 方案的 功能 和 性能，收集数据
 （当前 Gateway超时转发的规则下，可能会有意料外的收获）
4. 对于其中有问题或可以改进的地方，分析问题，及时解决或调整
5. 评估我们MBO在该场景的可行性和性能，Kyligence是否有绝对领先优势

对项目现场的需求：
1. 准备测试集群环境（至少包含前司OLAP数据库，可在上面安装开源Kylin或Kyligence）
2. 收集信息： 当前kyligence在zjnx的使用反馈
 3.1 基本信息： 业务语句、业务表数据量、cube信息、业务数量、创建cube的时长、 cube创建触发时机 等
 3.2 性能： 查询延时、并发
 3.3 kyligence使用过程中是否有问题，尤其是客户视角下的 kyligence不适合的场景（附加： 该场景 前司OLAP数据库是否能满足）
3. 收集信息：当前rubik 在 zjnx的使用现状和问题
4. 向测试集群上迁移 运行在 前司OLAP上的场景业务 及 真实数据
 2.1 需要现场一起评估场景， 个人预估 至少需要同时包含 适合kyligence的固定报表场景 和 灵活查询的场景
 2.2 定义好场景后，反馈 场景使用的所有表的 表结构（尤其关注 范围分区表）、数据量等信息
5. 收集该场景在 前司OLAP数据库单独执行 的执行时间（后续做对比）。
6. 其他现场觉得值得提供的信息
（期望这周完成，具体时间安排看现场安排）

## log

1103

- MV 操作熟悉
[[]]

1104 

和研发测试 tpch 

Cube 构建



## Cube 优化篇

发现Kyligence 的Cube 优化大体上也只有以下几个功能

Kyligence 高级设置：

聚合组（Aggregation Group）、联合维度（Joint Dimension）、层级维度（Hierarchy Dimension）和必要维度（Mandatory Dimension）

![picture 8](../../../images/422975bd22427cccdf2c9b98c93be3ec89ac16618ca5c09e86ceac2c6e69584d.png)  


### 背景

众所周知，Apache Kylin 的主要工作就是为源数据构建 N 个维度的 Cube，实现聚合的预计算。理论上而言，构建 N 个维度的 Cube 会生成 2N 个 Cuboid， 如图 1 所示，构建一个 4 个维度（A，B，C, D）的 Cube，需要生成 16 个Cuboid。

如果普通查询动不动选择10个维度，那就是1024 个cube。。。计算量非常夸张，同时因为不一定cube就贴合业务场景cube 命中率🎯感人。

假设一个场景，一共16 个维度，如下：

![picture 9](../../../images/bf6019c82bd7e20959802556255a8771d43df27f0fe703d127a1cc002d788a2b.png)  


### 优化原理 一句话catch-up

说白了优化原理分为业务层优化和框架层优化。

业务层优化 就是 Kyligence 页面上建cube 要走的流程。四板斧：

聚合组（Aggregation Group）：价格 和 地区 不会一起查。就各自在同一张宽表建cube

联合维度（Joint Dimension）：时间和城市总是一起查不会出现不一起查的情况

层级维度（Hierarchy Dimension）：国家/省份/城市 一起查，不会出现 国家/城市 这种跨越的

必要维度（Mandatory Dimension）：总是会带上 时间字段

框架层优化：老 Cube 对HBase 的一些优化比如查的比较多的放rowkey 前面，segment 合并、维度字段尽量不要decimal。。。
### Cube 优化原理

Cube 的生成：

为了深入理解Cube，首先要先了解Cuboid生成树。如图3所示，在Cube中，所有的Cuboid组成一个树形结构，根节点是全维度的Base Cuboid，再依次逐层聚合掉每个维度生成子Cuboid，直到出现0个维度时结束。图3中绿色部分就是一条完整的Cuboid生成路径。预计算的过程实际就是按照这个流程构建所有的Cuboid。

![picture 22](../../../images/c16d547229266c294cffa788cd084cc76f1cab8d4c96f7f378867b795d3b78b5.png)  

###  聚合组（Aggregation Group）

用户根据自己关注的维度组合，可以划分出自己关注的组合大类，这些大类在 Apache Kylin 里面被称为聚合组。例如图 1 中展示的 Cube，如果用户仅仅关注维度 AB 组合和维度 CD 组合，那么该 Cube 则可以被分化成两个聚合组，分别是聚合组 AB 和聚合组 CD。如图 2 所示，生成的 Cuboid 数目从 16 个缩减成了 8 个。

![picture 10](../../../images/11ce15a5a023d6fb5ac721076acfd6e2bd1c6004cf20d81ac655867accd2dcfe.png)  


说白了就是聚合后只会有

select A, count(number) from xxx group by A;

select A,B, count(number) from xxx group by A,B;

select C,D, count(number) from xxx group by C,D;

不会出现（A，B） 组和 （C，D）组交叉的查询场景。

🌈：Kyligence 的优化地方：

用户关心的聚合组之间可能包含相同的维度，例如聚合组 ABC 和聚合组 BCD 都包含维度 B 和维度 C。这些聚合组之间会衍生出相同的 Cuboid，例如聚合组 ABC 会产生 Cuboid BC，聚合组 BCD 也会产生 Cuboid BC。这些 Cuboid不会被重复生成，一份 Cuboid 为这些聚合组所共有，如图 3 所示。

![picture 11](../../../images/a202a5cb4c9088bb3e075eac489bdc65c273c182865658ae9844a8df5096a019.png)  

有了聚合组用户就可以粗粒度地对 Cuboid 进行筛选，获取自己想要的维度组合。

案例：

假设创建一个交易数据的 Cube，它包含了以下一些维度：

1. A 顾客 ID buyer_id 
2. B 交易日期 cal_dt、
3. C 付款的方式 pay_type
4. D 买家所在的城市 city

有时候，分析师需要通过分组聚合 D city、B cal_dt 和 C pay_type 来获知不同消费方式在不同城市的应用情况；有时候，分析师需要通过聚合 D city 、B cal_dt 和 A buyer_id，来查看顾客在不同城市的消费行为。在上述的实例中，推荐建立两个聚合组，包含的维度和方式如图：

![picture 12](../../../images/45c7665cc0d0b24bd7121788e85f4acba0e617420ed7b4aa0e7c21d231d65ab2.png)  

聚合组 1： [cal_dt, city, pay_type]

聚合组 2： [cal_dt, city, buyer_id]

在不考虑其他干扰因素的情况下，这样的聚合组将节省不必要的 3 个 Cuboid: [pay_type, buyer_id]、[city, pay_type, buyer_id] 和 [cal_dt, pay_type, buyer_id] 等，节省了存储资源和构建的执行时间。

```SQL
Case 1:
SELECT cal_dt, city, pay_type, count(*) FROM table GROUP BY cal_dt, city, pay_type;
则将从 Cuboid [cal_dt, city, pay_type] 中获取数据。
Case2:
SELECT cal_dt, city, buy_id, count(*) FROM table GROUP BY cal_dt, city, buyer_id;
 则将从 Cuboid [cal_dt, city, pay_type] 中获取数据。
Case3 如果有一条不常用的查询:
SELECT pay_type, buyer_id, count(*) FROM table GROUP BY pay_type, buyer_id;
则没有现成的完全匹配的 Cuboid。
此时，Apache Kylin 会通过在线计算的方式，从现有的 Cuboid 中计算出最终结果
```

### 联合维度（Joint Dimension）

用户有时并不关心维度之间各种细节的组合方式，例如用户的查询语句中仅仅会出现 group by A, B, C，而不会出现 group by A, B 或者 group by C 等等这些细化的维度组合。这一类问题就是联合维度所解决的问题。例如将维度 A、B 和 C 定义为联合维度，Apache Kylin 就仅仅会构建 Cuboid ABC，而 Cuboid AB、BC、A 等等Cuboid 都不会被生成。最终的 Cube 结果如图 2 所示，Cuboid 数目从 16 减少到 4。

![picture 13](../../../images/de0ef1b36e338cd996bf54159787240488ffe624aa02f4b510361825a8a961ae.png)  

案例：

假设创建一个交易数据的Cube，它具有很多普通的维度，像是交易日期 cal_dt，交易的城市 city，顾客性别 sex_id 和支付类型 pay_type 等。分析师常用的分析方法为通过按照交易时间、交易地点和顾客性别来聚合，获取不同城市男女顾客间不同的消费偏好，例如同时聚合交易日期 cal_dt、交易的城市 city 和顾客性别 sex_id来分组。在上述的实例中，推荐在已有的聚合组中建立一组联合维度，包含的维度和组合方式如图：

![picture 14](../../../images/70b67d0c34bf3bb41c13500bf3353cb09973d0cb682b60d776aee1c29a632e64.png)  

聚合组：[cal_dt, city, sex_id，pay_type]

联合维度： [cal_dt, city, sex_id]

```SQL
Case 1：
SELECT cal_dt, city, sex_id, count(*) FROM table GROUP BY cal_dt, city, sex_id;
则它将从Cuboid [cal_dt, city, sex_id]中获取数据
Case2如果有一条不常用的查询：
SELECT cal_dt, city, count(*) FROM table GROUP BY cal_dt, city;
则没有现成的完全匹配的 Cuboid，Apache Kylin 会通过在线计算的方式，从现有的 Cuboid 中计算出最终结果。
```

### 层级维度（Hierarchy Dimension）

用户选择的维度中常常会出现具有层级关系的维度。例如对于国家（country）、省份（province）和城市（city）这三个维度，从上而下来说国家／省份／城市之间分别是一对多的关系。也就是说，用户对于这三个维度的查询可以归类为以下三类:

1. group by country
2. group by country, province（等同于group by province）
3. group by country, province, city（等同于 group by country, city 或者group by city）

以图所示的 Cube 为例，假设维度 A 代表国家，维度 B 代表省份，维度 C 代表城市，那么ABC 三个维度可以被设置为层级维度，生成的Cube 如图7所示。

![picture 15](../../../images/39c065b3edca2ec3f766c10075f422cdbd27bdcf5f0eff8e64a9598a0b763681.png)  

例如，Cuboid [A,C,D]=Cuboid[A, B, C, D]，Cuboid[B, D]=Cuboid[A, B, D]，因而 Cuboid[A, C, D] 和 Cuboid[B, D] 就不必重复存储。

图8展示了 Kylin 按照前文的方法将冗余的Cuboid 剪枝从而形成图 2 的 Cube 结构，Cuboid 数目从 16 减小到 8。

![picture 16](../../../images/3530687ff513b94f949b894dcdbe055d272ad76bea851ba856f19bd2b57581bd.png)  

例子：

假设一个交易数据的 Cube，它具有很多普通的维度，像是交易的城市 city，交易的省 province，交易的国家 country， 和支付类型 pay_type等。分析师可以通过按照交易城市、交易省份、交易国家和支付类型来聚合，获取不同层级的地理位置消费者的支付偏好。在上述的实例中，建议在已有的聚合组中建立一组层级维度（国家country／省province／城市city），包含的维度和组合方式如图9：

![picture 17](../../../images/dcd90538b2553daf59cbfe92253bcbaa587dcf2eab0e70e233b44407b6001119.png)  

聚合组：[country, province, city，pay_type]

层级维度： [country, province, city]

```SQL
Case 1 当分析师想从城市维度获取消费偏好时：
SELECT city, pay_type, count(*) FROM table GROUP BY city, pay_type;
则它将从 Cuboid [country, province, city, pay_type] 中获取数据。
Case 2 当分析师想从省级维度获取消费偏好时：
SELECT province, pay_type, count(*) FROM table GROUP BY province,pay_type;
则它将从Cuboid [country, province, pay_type] 中获取数据。
Case 3 当分析师想从国家维度获取消费偏好时：
SELECT country, pay_type, count(*) FROM table GROUP BY country, pay_type;
则它将从Cuboid [country, pay_type] 中获取数据。
Case 4 如果分析师想获取不同粒度地理维度的聚合结果时：
无一例外都可以由图 3 中的 cuboid 提供数据 。
例如，SELECT country, city, count(*) FROM table GROUP BY country, city 则它将从 Cuboid [country, province, city] 中获取数据。
```

### 必要维度（Mandatory Dimension）

用户有时会对某一个或几个维度特别感兴趣，所有的查询请求中都存在group by这个维度，那么这个维度就被称为必要维度，只有包含此维度的Cuboid会被生成（如图10）。

![picture 18](../../../images/f8c79485caabea49369f45c605bfb1903c7481f5a266f80ce9d6a2ab588b047d.png)  

以图 1中的Cube为例，假设维度A是必要维度，那么生成的Cube则如图3所示，维度数目从16变为9。

![picture 19](../../../images/3a75ac3cbb953123a820682ec7457ab8b7fb72ed3105113af27ca7b083a6f5be.png)  

例子：

假设一个交易数据的Cube，它具有很多普通的维度，像是交易时间order_dt，交易的地点location，交易的商品product和支付类型pay_type等。其中，交易时间就是一个被高频作为分组条件（group by）的维度。 如果将交易时间order_dt设置为必要维度，包含的维度和组合方式如图4：

![picture 20](../../../images/4da4e833fba999df8e4136a88bd5bd0c4f3059c1a51fe0e6e195d1925a1244d3.png)  

### 其他案例

基本就是上面的四板斧。加上一些其他的

案例1 – 提升Cube查询效率

数据：

- 9个维度，其中1个维度基数是千万级，1个维度基数是百万级，其他维度基数是10w以内
- 单月原始数据6亿条

优化方案：

- 数据清理：将时间戳字段转换成日期，降低维度的基数
- 调整聚合组：不会同时在查询中出现的维度分别包含在不同聚合组（如崩溃时间、上传时间等）
- 设置必须维度：把某些超低基数维度设为必须维度

优化成果：

- 查询性能：提升5倍
- 构建时间：缩短30%
- Cube大小：减小74%

案例二 – 提升Cube构建效率

背景：某金融企业使用Apache Kylin作为报表分析引擎，发现Cube膨胀率多大、构建时间过长，希望对这一情况进行改善。

硬件：20台高配置PC服务器

数据：事实表有100多万条记录，度量是某些列的平均值

优化方案：

维度精简：去除查询中不会出现的维度
调整聚合组：设置多个聚合组，每个聚合组内设置多组联合维度

![picture 21](../../../images/5e5786fd984fd785fd7e655b5609d0dd132615cd6f7110ad03133b60ad0dd8ae.png)  




## 精华 catchup

 Kylin 原理

![picture 7](../../../images/d01c9045ca37e7b8757e9fb34d562469f0e3b88d32eb3fa9084623fd76bd7284.png)  

1）创建临时的Hive平表（从Hive读取数据）。 2）计算各维度的不同值，并收集各Cuboid的统计数据。 3）创建并保存字典。 4）保存Cuboid统计信息。 5）创建HTable。 6）计算Cube（一轮或若干轮MapReduce）。7）将Cube的计算结果转成HFile。 8）加载HFile到HBase。 9）更新Cube元数据。 10）垃圾回收。

以上步骤中，前5步是为计算Cube而做的准备工作，例如遍历维度值 来创建字典，对数据做统计和估算以创建HTable等；第6）步是真正的Cube 计算，取决于所使用的Cube算法，它可能是一轮MapReduce任务，也可能 是N（在没有优化的情况下，N可以被视作是维度数）轮迭代的 MapReduce。由于Cube运算的中间结果是以SequenceFile的格式存储在 HDFS上的，所以为了导入到HBase中，还需要第7）步将这些结果转换成 HFile（HBase文件存储格式）。第8）步通过使用HBase BulkLoad工具，将 HFile导入进HBase集群，这一步完成之后，HTable就可以查询到数据了。 第9）步更新Cube的数据，将此次构建的Segment的状态从“NEW”更新 为“READY”，表示已经可供查询了。最后一步，清理构建过程中生成的临 时文件等垃圾，释放集群资源。

Cube 理论：
给定一个数据模型，我们可以对其上的所有维度进行组合。对于N个 维度来说，组合的所有可能性共有2N 种。对于每一种维度的组合，将度 量做聚合运算，然后将运算的结果保存为一个物化视图，称为Cuboid。所 有维度组合的Cuboid作为一个整体，被称为Cube。所以简单来说，一个 Cube就是许多按维度聚合的物化视图的集合。

Cube（或Data Cube），即数据立方体，是一种常用于数据分析与索引 的技术；它可以对原始数据建立多维度索引。通过Cube对数据进行分析， 可以大大加快数据的查询效率。 
Cuboid在Kylin中特指在某一种维度组合下所计算的数据。
Cube Segment是指针对源数据中的某一个片段，计算出来的Cube数 据。通常数据仓库中的数据数量会随着时间的增长而增长，而Cube Segment也是按时间顺序来构建的


优化1： 分组剪枝，比方说10 个维度就是 1024 种，但是如果分为两组的话就是 2 的5次方 + 2 的5次方 = 64 种，大大减小了不必要的空间
优化2:  增量构建，避免每次全量跑 cube 耗时太久，指定增量字段
优化3 ： 待确认：雪花模型 和 星型模型 是否都支持？ （可以跑批或者用视图将星型模型转化为 雪花 也就是跑成大宽表）


Kylin 技术壁垒：

1. By-layer逐层算法：一个N维的Cube，是由1个N维子立方体、N个（N-1）维子立方体、N*(N-1)/2个(N-2)维子立方体、……N个1维子立方体和1个0维子立方体构成，总共有 2^N个子立方体。在逐层算法中，按照维度数逐层减少来计算，每个层级的计算（除了第一层，由原始数据聚合而来），是基于上一层级的计算结果来计算的。例如：group by [A,B]的结果，可以基于group by [A,B,C]的结果，通过去掉C后聚合得来的，这样可以减少重复计算，当0维Cuboid计算出来的时候，整个Cube的计算也就完成了。如下图所示：、
此过程为Kylin构建的核心，切换Spark引擎后，默认只采用By-layer逐层算法，不再自动选择（By-layer逐层算法、快速算法）。Spark在实现By-layer逐层算法的过程中，从最底层的Cuboid一层一层地向上计算，**直到计算出最顶层的Cuboi** （相当于执行了一个不带group by的查询），将各层的结果数据缓存到内存中，跳过每次数据的读取过程，直接依赖上层的缓存数据，大大提高了执行效率。Spark执行过程具体内容如下。


Job个数为By-layer算法树的层数，Spark将每层结果数据的输出，作为一个Job。

![picture 6](../../../images/894657f5ce28009b5f5acc4d5e870781210624714e84a9bfa12b7466298d5a61.png)  


https://tech.meituan.com/2020/11/19/apache-kylin-practice-in-meituan.html


1. 字典计算：Kylin通过计算Hive表出现的维度值，创建维度字典，将维度值映射成编码，并保存保存统计信息，节约HBase存储资源。每一种维度组合，称为一个Cuboid。理论上来说，一个N维的Cube，便有2^N种维度组合。

2. 剪枝算法

3. 复杂 cube 的命中




## 书籍

Kylin 学习


Kylin 主要想解决数据分析过慢的问题。

发现：
1. 查询一般是统计结果，各种sum/count 聚合函数计算后的统计值，原始记录很少直接查（select *）
2. 聚合是按  维度  进行的，由于分析场景（比方说就是按照时间和类型）有限，所以维度聚合组合 也是有限的

所以说白了就是 预计算 可能出现的结果，看命中率

除了 “大规模并行处理” 和 “列存储” 的第三种方案： 预计算

本质上是MOLAP（Multidimensional Online Analytical Processing）Cube，也就是多维立方体分析。

概念：

维度（Dimension）和度量 （Measure）这两个概念
维度就是观察数据的角度，一般是离散值（时间/地区/商品
度量就是被聚合的统计值，也是聚合运算的结果，它一般是连续的 值 （销售额/价格/总件数。。。

分析人员往往要结合若干个维度来审查 度量值，以便在其中找到变化规律。在一个SQL查询中，Group By的属性 通常就是维度，而所计算的值则是度量。

Select a b sum(c) count(d) from x group by a b 
这里 ab 是维度、cd 是度量


通过比较和测算度 量，分析师可以对数据进行评估，比如今年的销售额相比去年有多大的 增长，增长的速度是否达到预期，不同商品类别的增长比例是否合理等

有了维度和度量，就可以把所有涉及到的字段都分类了：
Cube 理论：
给定一个数据模型，我们可以对其上的所有维度进行组合。对于N个 维度来说，组合的所有可能性共有2N 种。对于每一种维度的组合，将度 量做聚合运算，然后将运算的结果保存为一个物化视图，称为Cuboid。所 有维度组合的Cuboid作为一个整体，被称为Cube。所以简单来说，一个 Cube就是许多按维度聚合的物化视图的集合。

Cube（或Data Cube），即数据立方体，是一种常用于数据分析与索引 的技术；它可以对原始数据建立多维度索引。通过Cube对数据进行分析， 可以大大加快数据的查询效率。 
Cuboid在Kylin中特指在某一种维度组合下所计算的数据。
Cube Segment是指针对源数据中的某一个片段，计算出来的Cube数 据。通常数据仓库中的数据数量会随着时间的增长而增长，而Cube Segment也是按时间顺序来构建的


优化1： 分组剪枝，比方说10 个维度就是 1024 种，但是如果分为两组的话就是 2 的5次方 + 2 的5次方 = 64 种，大大减小了不必要的空间
优化2:  增量构建，避免每次全量跑 cube 耗时太久，指定增量字段
优化3 ： 待确认：雪花模型 和 星型模型 是否都支持？ （可以跑批或者用视图将星型模型转化为 雪花 也就是跑成大宽表）


OLAP的概念，在实际应用中存在广义和狭义两种不同的理解方式。 广义上的理解与字面上的意思相同，泛指一切不会对数据进行更新的分 析处理。但更多的情况下OLAP被理解为其狭义上的含义，即与多维分析 相关，基于立方体（Cube）计算而进行的分析。


纬度的基数：

维度的基数（Cardinality）指的是该维度在数据集中出现的不同值的 个数；例如“国家”是一个维度，如果有200个不同的值，那么此维度的基数 就是200。通常一个维度的基数会从几十到几万个不等，个别维度如“用 户ID”的基数会超过百万甚至千万。如果一个Cube 中有好几个超高基数维度，那么这个Cube膨胀的概率就会很高 （只能count distinct 看


聚合组的概念：
Kylin默认会把所有维度都放在同一个聚合组中；如果维度数较多（例 如>10），那么建议用户根据查询的习惯和模式，单击“New Aggregation Group+”，将维度分为多个聚合组。通过使用多个聚合组，可以大大降低 Cube中的Cuboid数量。下面来举例说明，如果一个Cube有（M+N）个维度， 那么默认它会有2m+n 个Cuboid；如果把这些维度分为两个不相交的聚合 组，那么Cuboid的数量将被减少为2m +2 n 。


Mandatory维度指的是那些总是会出现在Where条件或Group By语句 里的维度；通过将某个维度指定为Mandatory，Kylin就可以不用预计算那 些不包含此维度的Cuboid，从而减少计算量。

Hierarchy是一组有层级关系的维度，例如“国家”“省”“市”，这里 的“国家”是高级别的维度，“省”“市”依次是低级别的维度。用户会按高级 别维度进行查询，也会按低级别维度进行查询，但在查询低级别维度时， 往往都会带上高级别维度的条件，而不会孤立地审视低级别维度的数 据。例如，用户会单击“国家”作为维度来查询汇总数据，也可能单击“国 家”＋“省”，或者“国家”＋“省”＋“市”来查询，但是不会跨越国家直接 Group By“省”或“市”。


Joint是将多个维度组合成一个维度，其通常适用于如下两种情形。 ·总是会在一起查询的维度。 ·基数很低的维度

Kylin以Key-Value的方式将Cube存储到HBase中。HBase的key，也就 是Rowkey，是由各维度的值拼接而成的；为了更高效地存储这些值， Kylin会对它们进行编码和压缩
每个维度均可以选择合适的编码 （Encoding）方式，默认采用的是字典（Dictionary）编码技术；除了字典以 外，还有整数（Int）和固定长度（Fixed Length）的编码。 字典编码是将此维度下的所有值构建成一个从string到int的映射表； Kylin会将字典序列化保存，在Cube中存储int值，从而大大减小存储的大 小。另外，字典是保持顺序的，即如果字符串A比字符串B大的话，那么A 编码后的int值也会比B编码后的值大；这样可以使得在HBase中进行比较 查询的时候，依然使用编码后的值，而无需解码。


流程：
1）创建临时的Hive平表（从Hive读取数据）。 2）计算各维度的不同值，并收集各Cuboid的统计数据。 3）创建并保存字典。 4）保存Cuboid统计信息。 5）创建HTable。 6）计算Cube（一轮或若干轮MapReduce）。7）将Cube的计算结果转成HFile。 8）加载HFile到HBase。 9）更新Cube元数据。 10）垃圾回收。

以上步骤中，前5步是为计算Cube而做的准备工作，例如遍历维度值 来创建字典，对数据做统计和估算以创建HTable等；第6）步是真正的Cube 计算，取决于所使用的Cube算法，它可能是一轮MapReduce任务，也可能 是N（在没有优化的情况下，N可以被视作是维度数）轮迭代的 MapReduce。由于Cube运算的中间结果是以SequenceFile的格式存储在 HDFS上的，所以为了导入到HBase中，还需要第7）步将这些结果转换成 HFile（HBase文件存储格式）。第8）步通过使用HBase BulkLoad工具，将 HFile导入进HBase集群，这一步完成之后，HTable就可以查询到数据了。 第9）步更新Cube的数据，将此次构建的Segment的状态从“NEW”更新 为“READY”，表示已经可供查询了。最后一步，清理构建过程中生成的临 时文件等垃圾，释放集群资源。


全量和增量构建。
增量可以选取时间字段


HBase 优点1: 支持流式构建 cube

HBase 缺点1: 各维度在Rowkeys中的顺序，对于查询的性能会产生较明显的影响。 在这里用户可以根据查询的模式和习惯，通过拖曳的方式调整各个维度 在Rowkeys上的顺序（如图2-17所示）。
HBase 缺点2:  Segment 合并（不知道parquet 怎么办）合并的时候，Kylin将直接以当初各个Segment构建时生成的Cuboid文 件作为输入内容，而不需要从Hive加载原始数据。后续的步骤跟构建时 基本一致。直到新的HTable加载完成后，Kylin才会卸载旧的HTable，从而 确保在整个合并过程中，Cube都是可以查询的 （我们将Cube划分为多个Segment，每个Segment用起始时间和结束时 间来标志。Segment代表一段时间内源数据的预计算结果。在大部分情况 下（例外情况见第4章“流式构建”），一个Segment的起始时间等于它之前 那个Segment的结束时间，同理，它的结束时间等于它后面那个Segment的 起始时间。同一个Cube下不同的Segment除了背后的源数据不同之外，其 他如结构定义、构建过程、优化方法、存储方式等都完全相同。） 
看起来一个segment 应该是一颗LSM 树一个表吧
整体来说，增量构建的 Cube上的查询会比全量构建的做更多的运行时聚合，而这些运行时聚合 都发生在单点的查询引擎之上，因此通常来说增量构建的Cube上的查询 会比全量构建的Cube上的查询要慢一些。


Cube 优化手段 

1. Cuboid 剪枝
2. 编码
3. 纬度分组等等
4. 纬度分片（Hbase）以HBase存储引擎为例，不同的Region代表不同的 Cuboid分片，在读取Cuboid数据的时候，HBase会为每个Region开启一个 Coprocessor实例来处理查询引擎的请求。查询引擎将查询条件和分组条 件作为请求参数的一部分发送到Coprocessor中，Coprocessor就能够在返 回结果之前先对当前分片的数据做一定的预聚合
5. 调整 rowkey 顺序（Hbase
6. 降低度量精度


top-n 
Select customer, sum(price) from sales group by seller_id order by sum(price) desc 

Kylin 的理论基础是 Cube 理论，每一种维度组合称之为 Cuboid，所有 Cuboid 的集合是 Cube。其中由所有维度组成的 Cuboid 称为 Base Cuboid，图中（time，item，location，supplier）即为 Base Cuboid，所有的 Cuboid 都可以基于 Base Cuboid 计算出来。Cuboid 我们可以理解为就是一张预计算过后的大宽表，在查询时，Kylin 会自动选择满足条件的最合适的 Cuboid，比如上图的查询就会去找Cuboid（time，item，location），相比于从用户的原始表进行计算，从 Cuboid 取数据进行计算能极大的降低扫描的数据量和计算量。




## 网页

新架构：

http://kylin.apache.org/cn_blog/2021/07/29/kylin-on-parquet-new-architecture-share/

https://kylin.apache.org/blog/2021/07/02/Apache-Kylin4-A-new-storage-and-compute-architecture/

Kylin 的核心思想是预计算，将数据按照指定的维度和指标，预先计算出所有可能的查询结果，利用空间换时间来加速查询模式固定的 OLAP 查询。

Kylin 的理论基础是 Cube 理论，每一种维度组合称之为 Cuboid，所有 Cuboid 的集合是 Cube。其中由所有维度组成的 Cuboid 称为 Base Cuboid，图中（time，item，location，supplier）即为 Base Cuboid，所有的 Cuboid 都可以基于 Base Cuboid **计算出来**。Cuboid 我们可以理解为就是一张预计算过后的大宽表，在查询时，Kylin 会自动选择满足条件的最合适的 Cuboid，比如上图的查询就会去找Cuboid（time，item，location），相比于从用户的原始表进行计算，从 Cuboid 取数据进行计算能极大的降低扫描的数据量和计算量。


例子：


用户有一张商品访问表（stock），其中 Item 商品，user_id 表示商品被哪个用户访问过，用户希望分析商品的 PV。用户定义了一个 Cube，维度是 item，度量是COUNT(user_id)，用户如果想分析商品的 PV，会发出如下的 SQL：
SELECT item，COUNT(user_id) FROM stock GROUP BY item;
这条 SQL 发给 Kylin 后，Kylin 不能直接的用它原始的语义去查我们的 Cube 数据，这是因为的数据经过预计算后，每个 item 的 key 只会存在一行数据，原始表中相同 item key 的行已经被提前聚合掉了，生成了一列新的 measure 列，存放每个 item key 有多少 user_id 访问，所以 rewrite 的 SQL 会类似这样：
SELECT item，SUM(M_C) FROM stockGROUP BY item;

为什么这里还会有一步 SUM/ GROUP BY 的操作，而不是直接取出数据直接返回就 OK 了呢？因为可能查询击中的 Cuboid 不止 item 一个维度，即击中的不是最精确的 Cuboid，所以还需从这些维度中再聚合一次，但是部分聚合的数据量相比起用户原始表中的数据，还是减少了非常多的数据量和计算。并且如果查询精确的命中Cuboid，我们是可以直接跳过 Agg/GROUP BY 的流程，如下图：

![picture 1](../../../images/ad996296504122507448bf7412972a59ed706cc75a32761c70c2e734ce099708.png)  


上图是无预计算的场景，全部需要现场计算，Agg 和 Join 因为都会牵涉到 shuffle 操作，故当数据量很大的时候，性能就会比较差，同时也会占用更多的资源，这也会影响查询的并发。

![picture 2](../../../images/f9028989ef747d5be942dbe614e38a70fa4837926a8a9fcb82692e41da9b7e15.png)  


原 HBase 架构

![picture 3](../../../images/5896cfb17f5ebdd1f9159de141fb98cab33936f8f276d8c89d8d9aa64662afe8.png)  

在目前开源版本的实现中，构建完的数据是存储在 HBase 中的，在上面小节中，我们得到了一个能够查询 Cube 数据的逻辑执行计划，Calcite 框架会根据这个逻辑执行计划生成对应的物理执行计划，最终每个算子都会通过代码生成生成自己算子的可执行代码，这个过程是一个迭代器模型，数据从最底层的 TableScan 算子向上游算子流动，整个过程就像火山喷发一样，故又名 Volcano Iterator Mode。而这个 TableScan 生成的代码会从 HBase 中取出 Cube 数据，当数据返回到 Kylin 的 Query Server 端之后，再被上层的算子一层层消费。

缺点：

1. 计算都在查询节点
2. 计算逻辑都交给 Calcite 不方便调试
3. HBase 不是真正列存
4. HBase 运维比较难

![picture 4](../../../images/19df8c2f2b9153860898d168f80acbc1b9835c3e229ca0b423a2ce6418091206.png)  


复杂 Query 不好处理：

这套方案对于简单的 SQL 并没有什么大问题，因为在精确匹配 Cuboid 的情况下，从 HBase 取回数据后，在 Kylin Query Server 端并不会做太多计算，但当一些比较复杂的查询，例如一句查询 join 了两个子查询，每个子查询都命中了各自的 cube，并在最外层做一些比较复杂的 Aggregate 操作，比如 COUNT DISTINCT 等，在这种情况下，Kylin Query Server 端不仅要从 HBase拉回大量的数据，并且还要在 Kylin Query Server 端计算 Join/Aggregate 等非常耗时耗资源的操作，当数据量变大，Kylin 的Query Server 端就可能会 OOM，解决的方式是提高 Query Server 端的内存，但这是个垂直扩容的过程，这就成了一个单点瓶颈，而大数据方案中存在单点瓶颈，是一个非常严重的问题，可能直接导致公司在架构选型的时候一键 pass 掉这个方案。

### 新架构优点

说白了就是利用新spark 的优势。。。把 Calcite 大部分工作推给 spark DataFrame 去做，就可以调试了。。。

![picture 5](../../../images/ee0afe0bcceed1d1cf4bb69b5a98eb27a57994727200f5e1b0c782d0fb173650.png)  


其实整体来说，新的设计非常简洁：**使用 visitor 模式**遍历之前生成的能够查询 Cube 数据的逻辑执行计划树，执行计划树的节点代表一个算子，里面其实无非就是保存了一些信息，比如要扫哪个表，要 filter/project 哪些列等等。将原来树上的每一个算子都翻译成一个 Spark 对于 Dataframe 的一个操作，每个上游节点都问自己的下游节点它处理完之后的一个 DF，一直到最下游的TableScan节点，由它生成初始的 DF，可以简单理解成 cuboidDF= spark.read.parquet(path)，得到初始的 DF之后，向它的上游返回，上游节点再对这个下游的 DF apply 上自己的操作，再返回给自己的上游，最后最上层节点对这个 DF 进行 collect 就触发了整个计算流程。这套框架的思想很简单，不过中间 Calcite 和 Spark 的 gap 的坑比我们想象的要多一些，比如数据类型/两边支持函/行为定义不一致等等。后期我们也有打算替换 Calcite 为 Catalyst，整套的架构会更加精致自然。



## 源码阅读

TODO：

- 全局字典 https://cwiki.apache.org/confluence/display/KYLIN/Global+Dictionary+on+Spark+CN

官网下来 Kylin4.0 ，对照着稍微看看


入口类：org.apache.kylin.engine.spark.job.CubeBuildJob#main
org.apache.kylin.engine.spark.job.CubeBuildJob#doExecute

查询入口：org.apache.kylin.query.QueryCli

TopN介绍
通过对Kylin 3.x的TopN实现原理的介绍，我们知道Kylin 3及之前版本的TopN使用了Space-Saving的算法，并在此之上做了优化，代码实现可以查看org.apache.kylin.measure.topn.TopNCounter 。

Kylin 4.0继续使用了Space-Saving的算法，并在Kylin 3.x的TopNCounter的基础上做了优化，不过同样的当前TopN也是存在误差的，这些在后面会有详细介绍。



看起来虽然run 失败，但是应该是 hadoop 相关的没加进来

1. 创建平表

统计数据初始化：

1.1 Call CuboidStatistics#statistics 生产一个 ForestSpanningTree 

1.2 Save cuboid statistics

1.3 Trigger cube planner phase one and save optimized cuboid set into CubeInstance

// choose source

// build cuboids from flat table



4.0 parquet 格式：
http://kylin.apache.org/cn/docs/


针对以上问题，社区提出了对使用 Apache Parquet + Spark 来代替 HBase 的提议，理由如下：
1. Parquet 是一种开源并且已经成熟稳定的列式存储格式；
2. Parquet 对云更加友好，可以兼容各种文件系统，包括 HDFS、S3、Azure Blob store、Ali OSS 等；
3. Parquet 可以很好地与 Hadoop、Hive、Spark、Impala 等集成；
4. Parquet 支持自定义索引。


预计算结果在 Kylin4.0 中如何存储
在 Kylin4.x 中，预计算结果以 Parquet 格式存储在文件系统中，文件存储结构对于 I/O 优化很重要，提前对存储目录结构进行设计，就能够在查询时通过目录或者文件名过滤数据文件，避免不必要的扫描。
Kylin4 对 cube 进行构建得到的预计算结果的 Parquet 文件在文件系统中存储的目录结构如下：
- cube_name
- SegmentA
- Cuboid-111
- part-0000-XXX.snappy.parquet
- part-0001-XXX.snappy.parquet
- …
- Cuboid-222
- part-0000-XXX.snappy.parquet
- part-0001-XXX.snappy.parquet
- …
- SegmentB
- Cuboid-111
- part-0000-XXX.snappy.parquet
- part-0001-XXX.snappy.parquet
- …
- Cuboid-222
- part-0000-XXX.snappy.parquet
- part-0001-XXX.snappy.parquet
- …


### Kylin 4.0 的构建引擎

在 Kylin4 中，Spark Engine 是唯一的构建引擎，与之前版本中的构建引擎相比，存在如下特点：

1、Kylin4 的构建简化了很多步骤。比如在 Cube Build Job 中， kylin4 只需要资源探测和 cubing 两个步骤，就可以完成构建；
2、由于 Parquet 会对存储的数据进行编码，所以在 kylin4 中不再需要维度字典和对维度列编码的过程；
3、Kylin4 对全局字典做了全新的实现，更多细节请参考：Kylin4 全局字典 ；
4、Kylin4 会根据集群资源、构建任务情况等对 Spark 进行自动调参；
5、Kylin4 提高了构建速度。

用户可以通过 kylin.build.spark-conf 开头的配置项手动修改构建相关的 Spark 配置，经过用户手动修改的 Spark 配置项不会再参与自动调参。


### Kylin 4.0 的查询引擎

Kylin4 的查询引擎 Sparder(SparderContext) 是由 spark application 后端实现的新型分布式查询引擎，相比于原来的查询引擎，Sparder 的优势体现在以下几点：
- 分布式的查询引擎，有效避免单点故障；
- 与构建所使用的计算引擎统一为 Spark；
- 对于复杂查询的性能有很大提高；
- 可以从 Spark 的新功能及其生态中获益。

在 Kylin4 中，Sparder 是作为一个 long-running 的 spark application 存在的。 Sparder 会根据 kylin.query.spark-conf 开头的配置项中配置的 Spark 参数来获取 Yarn 资源，如果配置的资源参数过大，可能会影响构建任务甚至无法成功启动 Sparder，如果 Sparder 没有成功启动，则所有查询任务都会失败，用户可以在 kylin WebUI 的 System 页面中检查 Sparder 状态。

默认情况下，用于查询的 spark 参数会设置的比较小，在生产环境中，大家可以适当把这些参数调大一些，以提升查询性能。


### 全局字典

主要就是怕增量构建 parquet 的话 如果字典不统一一是性能不好二是结果可能出错

https://cwiki.apache.org/confluence/display/KYLIN/Global+Dictionary+on+Spark+CN


在OLAP数据分析领域，去重计数(Count distinct)是非常常见的需求，而根据去重结果的要求又分为近似去重和精确去重。

在大规模数据集下，要想做到精确去重还要保证查询快速响应还是很有挑战性的。我们知道精确去重经常用到的处理方式就是**位图法(Bit map)**。对于整型数据，我们可以将统计信息保存在Bit map中，但是实际处理的数据中除了整型还会有String等其他类型，如果想做到精确去重首先就需要构建一个字典来为这些数据进行统一映射,然后再通过Bit map法进行统计。

我们都知道Kylin通过预计算技术来加速大数据分析。在增量构建Cube的时候，为了避免不同的segment单独构建字典导致最终去重结果出错，一个Cube中所有的Segment会使用同一个字典，也就是全局字典(Global Dictionary)。

基于Spark的全局字典
Kylin 4.0构建全局字典的方式基于Spark进行分布式的编码处理，减小了单机节点的压力，构建字典数量能够突破整型最大数量的限制。

全局字典的结构
每一次构建任务会生成一份新的全局字典
每次新的构建任务的字典按版本号进行保存, 旧的全局字典会被逐渐删除
一份字典包括一份meta数据文件和多个字典文件, 每个字典文件称之为桶(Bucket)
每个桶分为两个映射( Map<Object, Long>), 两者合并为完整的映射关系

![picture 24](../../../images/42764d2e5e915508d71ec25d2a3969ba57ea2ef6f9db9c46862546b298cd219a.png)  


BucketDictionary的概念与转化



Kylin引入了桶这一概念，可以理解为在处理数据的时候，将数据分到若干个桶(即多个分区)中进行并行处理。 第一次构建字典的时候会对每个桶内的值从1开始编码，在所有桶的编码完成之后再根据每个桶的offset值进行整体字典值的分配。在代码中两次编码是通过两个HashMap进行存储的，其中一个存储桶内相对的字典值，另一个存储所有桶之间绝对的字典值。

下图所示的是编号为1的桶多次构建任务中，桶内字典的传递，每一次构建都会为桶创建一个新的版本(即v1, v2, v3等)，加入版本控制的原因后面会有解释。Curr(current)和Prev(Previous)是一个桶内的两个HashMap，分别存储着当前桶内字典的相对(Relative)编码值和之前已经构建的所有字典值的绝对(Absolute)编码值。

![picture 25](../../../images/0a543c56eb4bfe4e478db734d7668d69c3367bd7ce67e0c064936cc18147ad89.png)  




构建步骤
通过 Spark 创建平表并获取需精确去重列的 distinct 值
根据确定去重后的字面值数量来确认分片数, 并且根据需求判断是否需要扩容
将数据分配(repartition)到多个分片(Partition)中，分别进行编码, 存储到各自的字典文件中
为当前构建任务分配版本号
保存字典文件和 metadata数据(桶数量和桶的 offset 值)
根据条件判断需要删除旧版本

初次构建
计算桶的大小
取需要构建字典的数量处理单个桶阈值和桶数量默认值的最大值。
创建桶并分配数据进行编码
生成meta文件记录桶的offsets
以下是相关配置项及其默认值。

kylin.dictionary.globalV2-min-hash-partitions=10
kylin.dictionary.globalV2-threshold-bucket-size=500000

![picture 26](../../../images/6b3b021a231c668fe65efef81dded24648c40f39b570768692b64dddc445fadb.png)  

非初次构建
根据字典数量确定桶是否需要扩容
已编码的字典值对扩容后的桶进行重新分配
读取之前最新版本的字典数据，并分配到各个桶中
将新的值分配到桶中
前一次构建的字典值不会改变

![picture 27](../../../images/2721cf17f8e492261d391b143ae0dd846c168c056b0763311e2af1f15e84627b.png)  

## TopN 度量

https://cwiki.apache.org/confluence/display/KYLIN/Kylin+4.0+TopN+Introduction+CN

背景：

我们先从一个典型的TopN应用场景入手，在电商平台做数据分析的时候我们想要获取可能会经常需要查看销售额靠前100的卖家是哪些，SQL查询示例如下：

SELECT kylin_sales.part_dt, seller_id

FROM kylin_sales

GROUP BY

kylin_sales.part_dt, kylin_sales.seller_id

ORDER BY SUM(kylin_sales.price) desc LIMIT 100;

在大数据量的场景下，想要求TopN的数据，如果先group by后再计算出所有的sum(price)，然后再对sum(price)进行排序，这里总的计算开销非常大的。


![picture 28](../../../images/c974b37bd7032fef52f55f9960f108834a8bfa844f2b3700d34a22ab6ec2dda0.png)  

TopN介绍
通过对Kylin 3.x的TopN实现原理的介绍，我们知道Kylin 3及之前版本的TopN使用了Space-Saving的算法，并在此之上做了优化，代码实现可以查看org.apache.kylin.measure.topn.TopNCounter 。

Kylin 4.0继续使用了Space-Saving的算法，并在Kylin 3.x的TopNCounter的基础上做了优化，不过同样的当前TopN也是存在误差的，这些在后面会有详细介绍。

## 性能优化

https://cwiki.apache.org/confluence/display/KYLIN/How+to+improve+cube+building+and+query+performance


## 测试

```SQL
-- 普通表
-- kylin 表元数据 load 成功
drop table if exists tzh_holo_common1;
create table tzh_holo_common1 (id int, value string, tdate date) 
STORED AS HOLODESK WITH TABLESIZE 10MB
tblproperties('cache'='ram');


insert into tzh_holo_common1
WITH
tb1 AS (SELECT 1 as id, 'a333' as value, '1950-10-1' as tdate FROM system.dual)
tb2 AS (SELECT 2 as id, 'ssss' as value, '1990-10-1' as tdate FROM system.dual)
SELECT * FROM tb1 UNION ALL SELECT * FROM tb2;

select * from tzh_holo_common1;

-- dan
DROP TABLE IF EXISTS tzh_holo_singlepartition_comment;
create table tzh_holo_singlepartition_comment (name string, id int, value string) 
COMMENT 'this is a comment test'
PARTITIONED BY (p string)
STORED AS HOLODESK WITH TABLESIZE 10MB
tblproperties('cache'='ram');

-- 单值分区表 
-- kylin 表元数据读取失败

DROP TABLE IF EXISTS tzh_holo_singlepartition;
create table tzh_holo_singlepartition (name string, id int, value string) 
PARTITIONED BY (p string)
STORED AS HOLODESK WITH TABLESIZE 10MB
tblproperties('cache'='ram');

ALTER TABLE tzh_holo_singlepartition ADD PARTITION (p='p1');
ALTER TABLE tzh_holo_singlepartition ADD PARTITION (p='p2');
SHOW PARTITIONS tzh_holo_singlepartition;

set hive.exec.dynamic.partition=true;
set stargate.dynamic.partition.enabled=true;
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.crud.dynamic.partition=true;



insert  into table tzh_holo_singlepartition partition(p) 
WITH
Tb1 AS (SELECT 'a113' as name,1 as id, 'a333' as value, 'p1' as p FROM system.dual)
tb2 AS (SELECT 'a114' as name,2 as id, 'ssss' as value, 'p2' as p FROM system.dual)
SELECT * FROM tb1 UNION ALL SELECT * FROM tb2;

select * from tzh_holo_singlepartition; 

-- 纬度表
-- kylin 表元数据读取失败

DROP TABLE IF EXISTS tzh_holo_dimension;
create table tzh_holo_dimension (name string, id int, value string) 
PARTITIONED BY (p string)
STORED AS HOLODESK WITH TABLESIZE 10MB
tblproperties('cache'='ram');

ALTER TABLE tzh_holo_dimension ADD PARTITION (p='p1');
ALTER TABLE tzh_holo_dimension ADD PARTITION (p='p2');
SHOW PARTITIONS tzh_holo_dimension;

set hive.exec.dynamic.partition=true;
set stargate.dynamic.partition.enabled=true;
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.crud.dynamic.partition=true;



insert  into table tzh_holo_dimension partition(p) 
WITH
Tb1 AS (SELECT 'a113' as name,1 as id, 'a333' as value, 'p1' as p FROM system.dual)
tb2 AS (SELECT 'a114' as name,2 as id, 'ssss' as value, 'p2' as p FROM system.dual)
SELECT * FROM tb1 UNION ALL SELECT * FROM tb2;

select * from tzh_holo_dimension; 

-- 范围分区

DROP TABLE IF EXISTS tzh_holo_range_partition_test;
CREATE TABLE tzh_holo_range_partition_test (name STRING,value int)
PARTITIONED BY RANGE (reg_date DATE) (
 PARTITION before2010 VALUES LESS THAN ('2010-12-31'),
 PARTITION before2012 VALUES LESS THAN ('2012-12-31'),
 PARTITION beforemax VALUES LESS THAN (MAXVALUE)
) STORED AS HOLODESK WITH TABLESIZE 10MB
tblproperties('cache'='ram');

SHOW PARTITIONS tzh_holo_range_partition_test;



DROP TABLE IF EXISTS tzh_org_range_partition_table;
create table tzh_org_range_partition_table (name string,value int,reg_date DATE) STORED AS HOLODESK WITH TABLESIZE 10MB
tblproperties('cache'='ram');
insert into table tzh_org_range_partition_table values("1",1,"2010-12-30");
insert into table tzh_org_range_partition_table values("2",1,"2012-12-30");
insert into table tzh_org_range_partition_table values("3",1,"2014-12-30");
insert into table tzh_org_range_partition_table values("3",null,"2014-12-30");


set hive.exec.dynamic.partition=true;
set stargate.dynamic.partition.enabled=true;
set hive.exec.dynamic.partition.mode=nonstrict;

-- 动态插入
INSERT into TABLE tzh_holo_range_partition_test
PARTITION (reg_date)
SELECT * FROM tzh_org_range_partition_table;

select * from tzh_holo_range_partition_test;
```

单值分区测试通过✅

![picture 10](../../../images/281703cd7e9f0090dbb02d1f6846455a24c62ac74b099dde2e4434f62c921ea5.png)  




待会儿把 tpch 的删了然后重跑一下



```SQL
use TPCH_前司OLAP数据库_10;

drop view v_lineitem;

create view if not exists v_lineitem as
select
    lineitem.*,

    year(l_shipdate) as l_shipyear,
    case when l_commitdate < l_receiptdate then 1 else 0 end as l_receiptdelayed,
    case when l_shipdate < l_commitdate then 0 else 1 end as l_shipdelayed,
    
    l_extendedprice * (1 - l_discount) as l_saleprice,
    l_extendedprice * (1 - l_discount) * l_tax as l_taxprice,
    ps_supplycost * l_quantity as l_supplycost
from
    lineitem
    inner join partsupp on l_partkey=ps_partkey and l_suppkey=ps_suppkey
;


drop view v_orders;

create view if not exists v_orders as
select
    orders.*,
    year(o_orderdate) as o_orderyear
from
    orders
;

drop view v_partsupp;
create view if not exists v_partsupp as
select
    partsupp.*,
    ps_supplycost * ps_availqty as ps_partvalue
from
    partsupp
;


-- 表
drop table if exists tzh_partsupp;
create table if not exists tzh_partsupp (
    `ps_partkey` bigint DEFAULT NULL,
    `ps_suppkey` int DEFAULT NULL, 
    `ps_availqty` int DEFAULT NULL,
    `ps_supplycost` decimal(10,2) DEFAULT NULL,
    `ps_comment` string DEFAULT NULL,
    `ps_partvalue` decimal(10,2) DEFAULT NULL
    ) 
STORED AS HOLODESK WITH TABLESIZE 1GB
tblproperties('cache'='ram');

insert into tzh_partsupp select 
partsupp.*,
    ps_supplycost * ps_availqty as ps_partvalue
from
    partsupp
;


-- v_orders
drop table if exists tzh_orders;
create table if not exists tzh_orders (
    `o_orderkey` bigint DEFAULT NULL,
    `o_custkey` bigint DEFAULT NULL, 
    `o_orderstatus` string DEFAULT NULL,
    `o_totalprice` decimal(10,2) DEFAULT NULL,
    `o_orderdate` date DEFAULT NULL,
    `o_orderpriority` string DEFAULT NULL,
    `o_clerk` string DEFAULT NULL,
    `o_shippriority` int DEFAULT NULL,
    `o_comment` string DEFAULT NULL,
    `o_orderyear` int DEFAULT NULL
    ) 
STORED AS HOLODESK WITH TABLESIZE 1GB
tblproperties('cache'='ram');

insert into tzh_orders select 
    orders.*,
    year(o_orderdate) as o_orderyear
from
    orders
;

drop table if exists tzh_lineitem;
create table if not exists tzh_lineitem (
    `l_orderkey` bigint DEFAULT NULL,                                 
 `l_partkey` bigint DEFAULT NULL,                      
 `l_suppkey` bigint DEFAULT NULL,                      
 `l_linenumber` int DEFAULT NULL,                      
 `l_quantity` int DEFAULT NULL,                        
 `l_extendedprice` decimal(10,2) DEFAULT NULL,               
 `l_discount` decimal(10,2) DEFAULT NULL,                 
 `l_tax` decimal(10,2) DEFAULT NULL,                      
 `l_returnflag` string DEFAULT NULL,                      
 `l_linestatus` string DEFAULT NULL,                      
 `l_shipdate` date DEFAULT NULL,                       
 `l_commitdate` date DEFAULT NULL,                     
 `l_receiptdate` date DEFAULT NULL,                       
 `l_shipinstruct` string DEFAULT NULL,                    
 `l_shipmode` string DEFAULT NULL,                     
 `l_comment` string DEFAULT NULL,
 `l_shipyear` int DEFAULT NULL,
  `l_receiptdelayed` int DEFAULT NULL,
  `l_shipdelayed` int DEFAULT NULL,
`l_saleprice` decimal(10,2) DEFAULT NULL,
`l_taxprice` decimal(10,2) DEFAULT NULL,
`l_supplycost` int DEFAULT NULL
    ) 
STORED AS HOLODESK WITH TABLESIZE 1GB
tblproperties('cache'='ram');

insert into tzh_lineitem 
select
    lineitem.*,

    year(l_shipdate) as l_shipyear,
    case when l_commitdate < l_receiptdate then 1 else 0 end as l_receiptdelayed,
    case when l_shipdate < l_commitdate then 0 else 1 end as l_shipdelayed,
    
    l_extendedprice * (1 - l_discount) as l_saleprice,
    l_extendedprice * (1 - l_discount) * l_tax as l_taxprice,
    ps_supplycost * l_quantity as l_supplycost
from
    lineitem
    inner join partsupp on l_partkey=ps_partkey and l_suppkey=ps_suppkey
;





use TPCH_前司OLAP数据库_10;
-- 表
drop table if exists v_partsupp;
create table if not exists v_partsupp (
    `ps_partkey` bigint DEFAULT NULL,
    `ps_suppkey` int DEFAULT NULL, 
    `ps_availqty` int DEFAULT NULL,
    `ps_supplycost` decimal(10,2) DEFAULT NULL,
    `ps_comment` string DEFAULT NULL,
    `ps_partvalue` decimal(10,2) DEFAULT NULL
    ) 
STORED AS HOLODESK WITH TABLESIZE 1GB
tblproperties('cache'='ram');

insert into v_partsupp select 
partsupp.*,
    ps_supplycost * ps_availqty as ps_partvalue
from
    partsupp
;


-- v_orders
drop table if exists v_orders;
create table if not exists v_orders (
    `o_orderkey` bigint DEFAULT NULL,
    `o_custkey` bigint DEFAULT NULL, 
    `o_orderstatus` string DEFAULT NULL,
    `o_totalprice` decimal(10,2) DEFAULT NULL,
    `o_orderdate` date DEFAULT NULL,
    `o_orderpriority` string DEFAULT NULL,
    `o_clerk` string DEFAULT NULL,
    `o_shippriority` int DEFAULT NULL,
    `o_comment` string DEFAULT NULL,
    `o_orderyear` int DEFAULT NULL
    ) 
STORED AS HOLODESK WITH TABLESIZE 1GB
tblproperties('cache'='ram');

insert into v_orders select 
    orders.*,
    year(o_orderdate) as o_orderyear
from
    orders
;

drop table if exists v_lineitem;
create table if not exists v_lineitem (
    `l_orderkey` bigint DEFAULT NULL,                                 
 `l_partkey` bigint DEFAULT NULL,                      
 `l_suppkey` bigint DEFAULT NULL,                      
 `l_linenumber` int DEFAULT NULL,                      
 `l_quantity` int DEFAULT NULL,                        
 `l_extendedprice` decimal(10,2) DEFAULT NULL,               
 `l_discount` decimal(10,2) DEFAULT NULL,                 
 `l_tax` decimal(10,2) DEFAULT NULL,                      
 `l_returnflag` string DEFAULT NULL,                      
 `l_linestatus` string DEFAULT NULL,                      
 `l_shipdate` date DEFAULT NULL,                       
 `l_commitdate` date DEFAULT NULL,                     
 `l_receiptdate` date DEFAULT NULL,                       
 `l_shipinstruct` string DEFAULT NULL,                    
 `l_shipmode` string DEFAULT NULL,                     
 `l_comment` string DEFAULT NULL,
 `l_shipyear` int DEFAULT NULL,
  `l_receiptdelayed` int DEFAULT NULL,
  `l_shipdelayed` int DEFAULT NULL,
`l_saleprice` decimal(10,2) DEFAULT NULL,
`l_taxprice` decimal(10,2) DEFAULT NULL,
`l_supplycost` int DEFAULT NULL
    ) 
STORED AS HOLODESK WITH TABLESIZE 1GB
tblproperties('cache'='ram');

insert into v_lineitem 
select
    lineitem.*,

    year(l_shipdate) as l_shipyear,
    case when l_commitdate < l_receiptdate then 1 else 0 end as l_receiptdelayed,
    case when l_shipdate < l_commitdate then 0 else 1 end as l_shipdelayed,
    
    l_extendedprice * (1 - l_discount) as l_saleprice,
    l_extendedprice * (1 - l_discount) * l_tax as l_taxprice,
    ps_supplycost * l_quantity as l_supplycost
from
    lineitem
    inner join partsupp on l_partkey=ps_partkey and l_suppkey=ps_suppkey
;
```


views 张泽修改后

```SQL
use TPCH_前司OLAP数据库_10;
-- 表
drop view if exists v_partsupp;
drop table if exists v_partsupp;
create table if not exists v_partsupp (
    `ps_partkey` bigint DEFAULT NULL,
    `ps_suppkey` int DEFAULT NULL, 
    `ps_availqty` int DEFAULT NULL,
    `ps_supplycost` double DEFAULT NULL,
    `ps_comment` string DEFAULT NULL,
    `ps_partvalue` double DEFAULT NULL
    ) 
STORED AS HOLODESK WITH TABLESIZE 1GB
tblproperties('cache'='ram');

insert into v_partsupp select 
partsupp.*,
    ps_supplycost * ps_availqty as ps_partvalue
from
    partsupp
;


-- v_orders
drop view if exists v_orders;
drop table if exists v_orders;
create table if not exists v_orders (
    `o_orderkey` bigint DEFAULT NULL,
    `o_custkey` bigint DEFAULT NULL, 
    `o_orderstatus` string DEFAULT NULL,
    `o_totalprice` double DEFAULT NULL,
    `o_orderdate` date DEFAULT NULL,
    `o_orderpriority` string DEFAULT NULL,
    `o_clerk` string DEFAULT NULL,
    `o_shippriority` int DEFAULT NULL,
    `o_comment` string DEFAULT NULL,
    `o_orderyear` int DEFAULT NULL
    ) 
STORED AS HOLODESK WITH TABLESIZE 1GB
tblproperties('cache'='ram');   

insert into v_orders select 
    orders.*,
    year(o_orderdate) as o_orderyear
from
    orders
;

drop view if exists v_lineitem;
drop table if exists v_lineitem;
create table if not exists v_lineitem (
 `l_orderkey` bigint DEFAULT NULL,                                 
 `l_partkey` bigint DEFAULT NULL,                      
 `l_suppkey` bigint DEFAULT NULL,                      
 `l_linenumber` int DEFAULT NULL,                      
 `l_quantity` int DEFAULT NULL,                        
 `l_extendedprice` double DEFAULT NULL,               
 `l_discount` double DEFAULT NULL,                 
 `l_tax` double DEFAULT NULL,                      
 `l_returnflag` string DEFAULT NULL,                      
 `l_linestatus` string DEFAULT NULL,                      
 `l_shipdate` date DEFAULT NULL,                       
 `l_commitdate` date DEFAULT NULL,                     
 `l_receiptdate` date DEFAULT NULL,                       
 `l_shipinstruct` string DEFAULT NULL,                    
 `l_shipmode` string DEFAULT NULL,                     
 `l_comment` string DEFAULT NULL,
 `l_shipyear` int DEFAULT NULL,
 `l_receiptdelayed` int DEFAULT NULL,
 `l_shipdelayed` int DEFAULT NULL,
 `l_saleprice` double DEFAULT NULL,
 `l_taxprice` double DEFAULT NULL,
 `l_supplycost` int DEFAULT NULL
    ) 
STORED AS HOLODESK WITH TABLESIZE 1GB
tblproperties('cache'='ram');

insert into v_lineitem 
select
    lineitem.*,

    year(l_shipdate) as l_shipyear,
    case when l_commitdate < l_receiptdate then 1 else 0 end as l_receiptdelayed,
    case when l_shipdate < l_commitdate then 0 else 1 end as l_shipdelayed,
    
    l_extendedprice * (1 - l_discount) as l_saleprice,
    l_extendedprice * (1 - l_discount) * l_tax as l_taxprice,
    ps_supplycost * l_quantity as l_supplycost
from
    lineitem
    inner join partsupp on l_partkey=ps_partkey and l_suppkey=ps_suppkey
;

```


Kylin tpch 使用方式

```bash
export KYLIN_HOME=/var/log/zz/kylin/apache-kylin-4.0.0-bin-spark2
source /var/log/zz/kylin/apache-kylin-4.0.0-bin-spark2/kylin-env.sh
kinit hive
sh setup-kylin-model.sh >> out.txt 2>&1 &
tail -f out.txt
```

查看cube

hdfs dfs -du -h /kylin/kylin_metadata/tpch/parquet/customer_cube

tpch 数据集建Cube测试

```SQL
SELECT
`CUSTOMER`.`C_CUSTKEY` as `CUSTOMER_C_CUSTKEY`
,`CUSTOMER`.`C_PHONE` as `CUSTOMER_C_PHONE`
,`CUSTOMER`.`C_ACCTBAL` as `CUSTOMER_C_ACCTBAL`
,`V_ORDERS`.`O_ORDERKEY` as `V_ORDERS_O_ORDERKEY`
,`V_ORDERS`.`O_COMMENT` as `V_ORDERS_O_COMMENT`
,`V_ORDERS`.`O_CUSTKEY` as `V_ORDERS_O_CUSTKEY`
 FROM `TPCH_前司OLAP数据库_10`.`CUSTOMER` as `CUSTOMER`
LEFT JOIN `TPCH_前司OLAP数据库_10`.`V_ORDERS` as `V_ORDERS`
ON `CUSTOMER`.`C_CUSTKEY` = `V_ORDERS`.`O_CUSTKEY`
WHERE 1=1


SELECT
CUSTOMER.C_CUSTKEY as CUSTOMER_C_CUSTKEY
,CUSTOMER.C_NATIONKEY as CUSTOMER_C_NATIONKEY,
-- ,V_ORDERS.O_ORDERSTATUS as V_ORDERS_O_ORDERSTATUS
-- ,V_ORDERS.O_CUSTKEY as V_ORDERS_O_CUSTKEY
-- ,CUSTOMER.C_PHONE as CUSTOMER_C_PHONE
-- ,CUSTOMER.C_ACCTBAL as CUSTOMER_C_ACCTBAL
-- ,CUSTOMER.C_ADDRESS as CUSTOMER_C_ADDRESS
COUNT(*)

 FROM TPCH_前司OLAP数据库_10.CUSTOMER as CUSTOMER
LEFT JOIN TPCH_前司OLAP数据库_10.V_ORDERS as V_ORDERS
ON CUSTOMER.C_CUSTKEY = V_ORDERS.O_CUSTKEY

GROUP BY CUSTOMER_C_CUSTKEY, CUSTOMER_C_NATIONKEY;


--- 列表格。 kylin 建cube 时间/sql 


SELECT
LINEITEM.L_RETURNFLAG as LINEITEM_L_RETURNFLAG
,LINEITEM.L_SHIPMODE as LINEITEM_L_SHIPMODE
,COUNT(*)
 FROM TPCH_前司OLAP数据库_10.LINEITEM as LINEITEM

 GROUP BY LINEITEM_L_RETURNFLAG,LINEITEM_L_SHIPMODE;

SELECT
COUNT(*)
 FROM TPCH_前司OLAP数据库_10.LINEITEM as LINEITEM;


 
WHERE 1=1
```


![picture 11](../../../images/c078d13266b85ceff7eb210d72e094c6d2965d783b1e036be2e11a615b6f194a.png)  



1105 tpch 自建表测试

```SQL
SELECT
LINEITEM.L_RETURNFLAG as LINEITEM_L_RETURNFLAG
,LINEITEM.L_SHIPMODE as LINEITEM_L_SHIPMODE
,COUNT(*)
 FROM TPCH_前司OLAP数据库_10.LINEITEM as LINEITEM

LEFT JOIN TPCH_前司OLAP数据库_10.ORDERS as ORDERS
ON LINEITEM.ORDERKEY = ORDERS.ORDERKEY
LEFT JOIN TPCH_前司OLAP数据库_10.PARTSUPP as PARTSUPP
ON LINEITEM.PARTKEY = PARTSUPP.PARTKEY
LEFT JOIN TPCH_前司OLAP数据库_10.SUPPLIER as SUPPLIER
ON LINEITEM.SUPPKEY = SUPPLIER.SUPPKEY

LEFT JOIN TPCH_前司OLAP数据库_10.NATION as NATION
ON SUPPLIER.NATIONKEY = NATION.NATIONKEY

LEFT JOIN TPCH_前司OLAP数据库_10.PART as PART
ON LINEITEM.PARTKEY = PART.PARTKEY
```


cube json 构建失败 （问题，貌似不支持  a left join b 然后 b left join c 。建cube 会报 rowkey xx 和 xx 不匹配

```json
{
  "name": "tzh_complex_cube",
  "model_name": "tzh_complex_tpch",
  "description": "",
  "dimensions": [
    {
      "name": "L_SHIPDATE",
      "table": "LINEITEM",
      "derived": null,
      "column": "L_SHIPDATE"
    },
    {
      "name": "L_COMMITDATE",
      "table": "LINEITEM",
      "derived": null,
      "column": "L_COMMITDATE"
    },
    {
      "name": "L_RECEIPTDATE",
      "table": "LINEITEM",
      "derived": null,
      "column": "L_RECEIPTDATE"
    },
    {
      "name": "O_ORDERPRIORITY",
      "table": "ORDERS",
      "derived": [
        "O_ORDERPRIORITY"
      ],
      "column": null
    },
    {
      "name": "PS_SUPPLYCOST",
      "table": "PARTSUPP",
      "derived": [
        "PS_SUPPLYCOST"
      ],
      "column": null
    },
    {
      "name": "S_NATIONKEY",
      "table": "SUPPLIER",
      "derived": [
        "S_NATIONKEY"
      ],
      "column": null
    },
    {
      "name": "P_BRAND",
      "table": "PART",
      "derived": [
        "P_BRAND"
      ],
      "column": null
    },
    {
      "name": "P_TYPE",
      "table": "PART",
      "derived": [
        "P_TYPE"
      ],
      "column": null
    },
    {
      "name": "N_REGIONKEY",
      "table": "NATION",
      "derived": [
        "N_REGIONKEY"
      ],
      "column": null
    }
  ],
  "measures": [
    {
      "name": "_COUNT_",
      "function": {
        "expression": "COUNT",
        "returntype": "bigint",
        "parameter": {
          "type": "constant",
          "value": "1",
          "next_parameter": null
        },
        "configuration": {}
      }
    },
    {
      "name": "SUM_quantity",
      "function": {
        "expression": "SUM",
        "returntype": "bigint",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_QUANTITY",
          "next_parameter": null
        },
        "configuration": null
      },
      "showDim": false
    },
    {
      "name": "MAX_quantity",
      "function": {
        "expression": "MAX",
        "returntype": "integer",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_QUANTITY",
          "next_parameter": null
        },
        "configuration": null
      },
      "showDim": false
    },
    {
      "name": "SUM_ext_price",
      "function": {
        "expression": "SUM",
        "returntype": "decimal(10,2)",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_EXTENDEDPRICE",
          "next_parameter": null
        },
        "configuration": null
      },
      "showDim": false
    },
    {
      "name": "COUNT_shipmode",
      "function": {
        "expression": "COUNT",
        "returntype": "bigint",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_SHIPMODE",
          "next_parameter": null
        },
        "configuration": null
      },
      "showDim": false
    },
    {
      "name": "COUNT_DISTINCT_line_status",
      "function": {
        "expression": "COUNT_DISTINCT",
        "returntype": "hllc(10)",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_LINESTATUS",
          "next_parameter": null
        },
        "configuration": null
      },
      "showDim": false
    },
    {
      "name": "MIN_tax",
      "function": {
        "expression": "MIN",
        "returntype": "decimal(10,2)",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_TAX",
          "next_parameter": null
        },
        "configuration": null
      },
      "showDim": false
    },
    {
      "name": "top_10_discount",
      "function": {
        "expression": "TOP_N",
        "returntype": "topn(10)",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_DISCOUNT",
          "next_parameter": {
            "type": "column",
            "value": "SUPPLIER.S_NATIONKEY",
            "next_parameter": null
          }
        },
        "configuration": {
          "topn.encoding.SUPPLIER.S_NATIONKEY": "dict",
          "topn.encoding_version.SUPPLIER.S_NATIONKEY": "1"
        }
      },
      "showDim": false
    }
  ],
  "dictionaries": [],
  "rowkey": {
    "rowkey_columns": [
      {
        "column": "LINEITEM.L_SHIPDATE",
        "encoding": "dict",
        "isShardBy": "false",
        "encoding_version": 1
      },
      {
        "column": "LINEITEM.L_COMMITDATE",
        "encoding": "dict",
        "isShardBy": "false",
        "encoding_version": 1
      },
      {
        "column": "LINEITEM.L_RECEIPTDATE",
        "encoding": "dict",
        "isShardBy": "false",
        "encoding_version": 1
      },
      {
        "column": "LINEITEM.L_ORDERKEY",
        "encoding": "dict",
        "isShardBy": "false",
        "encoding_version": 1
      },
      {
        "column": "LINEITEM.L_PARTKEY",
        "encoding": "dict",
        "isShardBy": "false",
        "encoding_version": 1
      },
      {
        "column": "LINEITEM.L_SUPPKEY",
        "encoding": "dict",
        "isShardBy": "false",
        "encoding_version": 1
      },
      {
        "column": "SUPPLIER.S_NATIONKEY",
        "encoding": "dict",
        "isShardBy": "false",
        "encoding_version": 1
      }
    ]
  },
  "aggregation_groups": [
    {
      "includes": [
        "LINEITEM.L_SHIPDATE",
        "LINEITEM.L_COMMITDATE",
        "LINEITEM.L_RECEIPTDATE",
        "LINEITEM.L_ORDERKEY",
        "LINEITEM.L_PARTKEY",
        "LINEITEM.L_SUPPKEY",
        "SUPPLIER.S_NATIONKEY"
      ],
      "select_rule": {
        "hierarchy_dims": [],
        "mandatory_dims": [],
        "joint_dims": []
      }
    }
  ],
  "mandatory_dimension_set_list": [],
  "partition_date_start": 0,
  "notify_list": [],
  "hbase_mapping": {
    "column_family": [
      {
        "name": "F1",
        "columns": [
          {
            "qualifier": "M",
            "measure_refs": [
              "_COUNT_",
              "SUM_quantity",
              "MAX_quantity",
              "SUM_ext_price",
              "COUNT_shipmode",
              "MIN_tax",
              "top_10_discount"
            ]
          }
        ]
      },
      {
        "name": "F2",
        "columns": [
          {
            "qualifier": "M",
            "measure_refs": [
              "COUNT_DISTINCT_line_status"
            ]
          }
        ]
      }
    ]
  },
  "volatile_range": "0",
  "retention_range": "0",
  "status_need_notify": [
    "ERROR",
    "DISCARDED",
    "SUCCEED"
  ],
  "auto_merge_time_ranges": [
    604800000,
    2419200000
  ],
  "engine_type": "6",
  "storage_type": 2,
  "override_kylin_properties": {}
}
```


cube json 构建成功 

```json
{
  "uuid": "5f4f340d-e290-eb75-8ba6-24982f71f3d3",
  "last_modified": 1636083335020,
  "version": "4.0.0.0",
  "name": "tzh_complex_cube",
  "is_draft": false,
  "model_name": "tzh_complex_tpch",
  "description": "",
  "null_string": null,
  "dimensions": [
    {
      "name": "L_SHIPDATE",
      "table": "LINEITEM",
      "column": "L_SHIPDATE",
      "derived": null
    },
    {
      "name": "L_COMMITDATE",
      "table": "LINEITEM",
      "column": "L_COMMITDATE",
      "derived": null
    },
    {
      "name": "L_RECEIPTDATE",
      "table": "LINEITEM",
      "column": "L_RECEIPTDATE",
      "derived": null
    },
    {
      "name": "O_ORDERPRIORITY",
      "table": "ORDERS",
      "column": null,
      "derived": [
        "O_ORDERPRIORITY"
      ]
    },
    {
      "name": "PS_SUPPLYCOST",
      "table": "PARTSUPP",
      "column": null,
      "derived": [
        "PS_SUPPLYCOST"
      ]
    },
    {
      "name": "S_NATIONKEY",
      "table": "SUPPLIER",
      "column": null,
      "derived": [
        "S_NATIONKEY"
      ]
    },
    {
      "name": "P_BRAND",
      "table": "PART",
      "column": null,
      "derived": [
        "P_BRAND"
      ]
    },
    {
      "name": "P_TYPE",
      "table": "PART",
      "column": null,
      "derived": [
        "P_TYPE"
      ]
    }
  ],
  "measures": [
    {
      "name": "_COUNT_",
      "function": {
        "expression": "COUNT",
        "parameter": {
          "type": "constant",
          "value": "1"
        },
        "returntype": "bigint"
      }
    },
    {
      "name": "SUM_QUANTITY",
      "function": {
        "expression": "SUM",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_QUANTITY"
        },
        "returntype": "bigint"
      }
    },
    {
      "name": "MAX_QUANTITY",
      "function": {
        "expression": "MAX",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_QUANTITY"
        },
        "returntype": "integer"
      }
    },
    {
      "name": "SUM_EXT_PRICE",
      "function": {
        "expression": "SUM",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_EXTENDEDPRICE"
        },
        "returntype": "decimal(10,2)"
      }
    },
    {
      "name": "COUNT_SHIPMODE",
      "function": {
        "expression": "COUNT",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_SHIPMODE"
        },
        "returntype": "bigint"
      }
    },
    {
      "name": "COUNT_DISTINCT_LINE_STATUS",
      "function": {
        "expression": "COUNT_DISTINCT",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_LINESTATUS"
        },
        "returntype": "hllc(10)"
      }
    },
    {
      "name": "MIN_TAX",
      "function": {
        "expression": "MIN",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_TAX"
        },
        "returntype": "decimal(10,2)"
      }
    },
    {
      "name": "TOP_10_DISCOUNT",
      "function": {
        "expression": "TOP_N",
        "parameter": {
          "type": "column",
          "value": "LINEITEM.L_DISCOUNT",
          "next_parameter": {
            "type": "column",
            "value": "SUPPLIER.S_NATIONKEY"
          }
        },
        "returntype": "topn(10,4)",
        "configuration": {
          "topn.encoding.SUPPLIER.S_NATIONKEY": "dict",
          "topn.encoding_version.SUPPLIER.S_NATIONKEY": "1"
        }
      }
    }
  ],
  "dictionaries": [],
  "rowkey": {
    "rowkey_columns": [
      {
        "column": "LINEITEM.L_SHIPDATE",
        "encoding": "date",
        "isShardBy": false
      },
      {
        "column": "LINEITEM.L_COMMITDATE",
        "encoding": "date",
        "isShardBy": false
      },
      {
        "column": "LINEITEM.L_RECEIPTDATE",
        "encoding": "date",
        "isShardBy": false
      },
      {
        "column": "LINEITEM.L_PARTKEY",
        "encoding": "dict",
        "isShardBy": false
      },
      {
        "column": "LINEITEM.L_ORDERKEY",
        "encoding": "dict",
        "isShardBy": false
      },
      {
        "column": "LINEITEM.L_SUPPKEY",
        "encoding": "dict",
        "isShardBy": false
      }
    ]
  },
  "hbase_mapping": {
    "column_family": [
      {
        "name": "F1",
        "columns": [
          {
            "qualifier": "M",
            "measure_refs": [
              "_COUNT_",
              "SUM_QUANTITY",
              "MAX_QUANTITY",
              "SUM_EXT_PRICE",
              "COUNT_SHIPMODE",
              "MIN_TAX",
              "TOP_10_DISCOUNT"
            ]
          }
        ]
      },
      {
        "name": "F2",
        "columns": [
          {
            "qualifier": "M",
            "measure_refs": [
              "COUNT_DISTINCT_LINE_STATUS"
            ]
          }
        ]
      }
    ]
  },
  "aggregation_groups": [
    {
      "includes": [
        "LINEITEM.L_COMMITDATE",
        "LINEITEM.L_RECEIPTDATE",
        "LINEITEM.L_ORDERKEY",
        "LINEITEM.L_PARTKEY",
        "LINEITEM.L_SUPPKEY",
        "LINEITEM.L_SHIPDATE"
      ],
      "select_rule": {
        "hierarchy_dims": [],
        "mandatory_dims": [],
        "joint_dims": [],
        "dim_cap": 6
      }
    }
  ],
  "signature": "XiLqx1UL5xE0dGfN/0hNAg==",
  "notify_list": [],
  "status_need_notify": [
    "ERROR",
    "DISCARDED",
    "SUCCEED"
  ],
  "partition_date_start": 0,
  "partition_date_end": 3153600000000,
  "auto_merge_time_ranges": [
    604800000,
    2419200000
  ],
  "volatile_range": 0,
  "retention_range": 0,
  "engine_type": 6,
  "storage_type": 4,
  "override_kylin_properties": {},
  "cuboid_black_list": [],
  "parent_forward": 3,
  "mandatory_dimension_set_list": [],
  "snapshot_table_desc_list": []
}
```

构建失败：Caused by: java.lang.IllegalStateException: Failed to build lookup table PARTSUPP snapshot for Dup key found, key= PS_PARTKEY

原因：      最后，总结出来，出现这个问题的原因就是：事实表（列：id）与维度表(列：fact_id) 进行关联时，进行快照的时候，纬度表的这个key 不唯一，好像可以关闭快照。
通过 select a, count(*) from table group by a 来看。
https://issues.apache.org/jira/browse/KYLIN-5067?page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel&focusedCommentId=17404407#comment-17404407

勾选这里可以关闭快照

![picture 1](../../../images/1d95e280e39514707cd731def9c5141a38477c0466f40e9d7e40a69bcb3fd07f.png)  

snapshot 取消勾选后，聚合组就会少了那个join 的值 （不然聚合组会多一个不存在的维度，其实就是snapshot 事实表join 的那个维度表的 key，不可以重复
（ 不对，是纬度表join 的列不支持重复

```SQL
select count(*) from TPCH_前司OLAP数据库_10.LINEITEM LINEITEM
LEFT JOIN TPCH_前司OLAP数据库_10.SUPPLIER as SUPPLIER
ON LINEITEM.l_suppkey = SUPPLIER.s_suppkey
;

select 
L_SHIPDATE
,L_SHIPMODE
,count(*) 
,sum(LINEITEM.L_QUANTITY)
,count(LINEITEM.L_QUANTITY)

from TPCH_前司OLAP数据库_10.LINEITEM LINEITEM
group by L_SHIPDATE,L_SHIPMODE;

-- 添加 sum(l_tax) 此时未被cube 包含
-- No realization found for OLAPContext
select 
L_SHIPDATE
,L_SHIPMODE
,count(*) 
,sum(LINEITEM.L_QUANTITY)
,count(LINEITEM.L_QUANTITY)
,sum(LINEITEM.l_tax)

from TPCH_前司OLAP数据库_10.LINEITEM LINEITEM
group by L_SHIPDATE,L_SHIPMODE;


-- 度量增减
select 
L_SHIPDATE
,L_SHIPMODE
,count(*) 
,sum(LINEITEM.L_QUANTITY)
-- ,count(LINEITEM.L_QUANTITY)
-- ,sum(LINEITEM.l_tax)

from TPCH_前司OLAP数据库_10.LINEITEM LINEITEM
group by L_SHIPDATE,L_SHIPMODE;


-- 纬度增减
select 
L_SHIPDATE
-- ,L_SHIPMODE
,count(*) 
,sum(LINEITEM.L_QUANTITY)
-- ,count(LINEITEM.L_QUANTITY)
-- ,sum(LINEITEM.l_tax)

from TPCH_前司OLAP数据库_10.LINEITEM LINEITEM
group by L_SHIPDATE;
-- ,L_SHIPMODE;


SELECT
LINEITEM.L_SHIPDATE as LINEITEM_L_SHIPDATE
,LINEITEM.L_SHIPMODE as LINEITEM_L_SHIPMODE
,LINEITEM.L_QUANTITY as LINEITEM_L_QUANTITY
,LINEITEM.L_TAX as LINEITEM_L_TAX
,LINEITEM.L_DISCOUNT as LINEITEM_L_DISCOUNT
 FROM TPCH_前司OLAP数据库_10.LINEITEM as LINEITEM
LEFT JOIN TPCH_前司OLAP数据库_10.SUPPLIER as SUPPLIER
ON LINEITEM.L_SUPPKEY = SUPPLIER.S_SUPPKEY
LEFT JOIN TPCH_前司OLAP数据库_10.ORDERS as ORDERS
ON LINEITEM.L_ORDERKEY = ORDERS.O_ORDERKEY
LEFT JOIN TPCH_前司OLAP数据库_10.PART as PART
ON LINEITEM.L_PARTKEY = PART.P_PARTKEY
WHERE 1=1
```


如果一个cube 的维度没涉及到纬度表，查询的时候如果包含join纬度表就会报错：
select count(*) from TPCH_前司OLAP数据库_10.LINEITEM LINEITEM
LEFT JOIN TPCH_前司OLAP数据库_10.SUPPLIER as SUPPLIER
ON LINEITEM.l_suppkey = SUPPLIER.s_suppkey
;

From line 2, column 11 to line 2, column 33: Object 'SUPPLIER' not found within 'TPCH_前司OLAP数据库_10' 


```SQL
SELECT

LINEITEM.L_SHIPDATE
,LINEITEM.L_COMMITDATE
,SUPPLIER.S_NATIONKEY
,ORDERS.O_SHIPPRIORITY
,SUM(LINEITEM.L_QUANTITY)
-- ,COUNT(*)
-- LINEITEM.L_SHIPDATE as LINEITEM_L_SHIPDATE
-- ,LINEITEM.L_SHIPMODE as LINEITEM_L_SHIPMODE
-- ,LINEITEM.L_QUANTITY as LINEITEM_L_QUANTITY
-- ,LINEITEM.L_TAX as LINEITEM_L_TAX
-- ,LINEITEM.L_DISCOUNT as LINEITEM_L_DISCOUNT
 FROM TPCH_前司OLAP数据库_10.LINEITEM as LINEITEM
LEFT JOIN TPCH_前司OLAP数据库_10.SUPPLIER as SUPPLIER
ON LINEITEM.L_SUPPKEY = SUPPLIER.S_SUPPKEY
LEFT JOIN TPCH_前司OLAP数据库_10.ORDERS as ORDERS
ON LINEITEM.L_ORDERKEY = ORDERS.O_ORDERKEY

taLINEITEM.L_SHIPDATE
,LINEITEM.L_COMMITDATE
,SUPPLIER.S_NATIONKEY
,ORDERS.O_SHIPPRIORITY
;


SELECT
LINEITEM.L_SHIPDATE
,LINEITEM.L_COMMITDATE
,COUNT(*)
,SUM(LINEITEM.L_QUANTITY)
 FROM TPCH_前司OLAP数据库_10.LINEITEM as LINEITEM
GROUP BY
LINEITEM.L_SHIPDATE
,LINEITEM.L_COMMITDATE
;
```


## kylin 集群搭建

### 文件获取

 scp root@172.18.120.28:/var/log/zz/kylin/apache-kylin-3.1.2-bin-hbase1x.tar.gz /home/

 scp root@172.18.120.28:/var/log/zz/kylin/apache-kylin-4.0.0-bin-spark2.tar.gz /home/