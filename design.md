# 设计文档

## 前言

- restful风格 [https://www.cnblogs.com/MTRD/p/12153561.html](https://www.cnblogs.com/MTRD/p/12153561.html)

- url规范 [https://www.cnblogs.com/hduwbf/p/7300794.html](https://www.cnblogs.com/hduwbf/p/7300794.html)

- sprint boot处理各种需求[https://spring.io/guides](https://spring.io/guides))

- 使用maven完成liquibase的changelog自动生成：

在对应的数据库中使用相应的sql建好需要的表, 进入对应的模块的根目录，也就是含有pom.xml的目录）执行如下命令：
```sh
mvn liquibase:generateChangeLog
# 进入到包含pom.xml的目标目录, 手动下载依赖
mvn dependency:get -DremoteRepositories=http://repo1.maven.org/maven2/ -DgroupId=org.reflections -DartifactId=Reflections -Dversion=0.11.7 
```
- 设计图地址：

火力兼容页面：[https://app.mockplus.cn/app/-voy0P0HScQ3/specs/design/Mgk3rpOTVW2](https://app.mockplus.cn/app/-voy0P0HScQ3/specs/design/Mgk3rpOTVW2)

WQ测试页面：[https://app.mockplus.cn/app/TTogpb0h7wTB/specs/design/Ny0HajcHvv3F](

## 1.系统整体架构图

![image-20211029171648447](https://nemo89.oss-cn-hangzhou.aliyuncs.com/doc/markdown/image-20211029171648447.png)

## 2 表设计

### 2.1 武器兼容测试数据库表

```sql
CREATE TABLE `pipe_task`  (
    `id` varchar(50)  COMMENT '主键',
    `name` varchar(50)  COMMENT '任务名称' unique,
    `code` varchar(10)  COMMENT '任务编号',
    `description` varchar(1000)  COMMENT '任务内容',
    `startTime` datetime  COMMENT '开始时间',
    `createTime` datetime(0)  COMMENT '创建时间',
    `modifyTime` datetime  COMMENT '修改时间',
    `endTime` datetime(0)  COMMENT '结束时间',
    `creator` varchar(50)  COMMENT '创建人',
    `disabled` tinyint  COMMENT '废弃标志',
    `status` tinyint  COMMENT '运行状态, 0: 未执行, 1: 执行中, 2: 执行完',
    PRIMARY KEY (`id`)
);


CREATE TABLE `pipe_test`  (
    `id` varchar(50)  COMMENT '主键',
    `name` varchar(50)  COMMENT '测试名称' unique,
    `code` varchar(10)  COMMENT '测试编号',
    `type` varchar(50)  COMMENT '测试类别(单武器通道测试, 自动防御系统测试)',
    `createTime` datetime  COMMENT '创建时间',
    `modifyTime` datetime  COMMENT '修改时间',
    `creator` varchar(50)  COMMENT '创建人',
    `sortKey` int  COMMENT '排序字段',
    `threshold` varchar(100) COMMENT '阈值, eg: [{"time": "5", ...}]',
    `disabled` tinyint  COMMENT '废弃字段',
    `status` tinyint  COMMENT '运行状态, 0: 未执行, 1: 执行中, 2: 执行完',
    `taskId` varchar(50)  COMMENT '外键-任务id',
    PRIMARY KEY (`id`),
    foreign key (`taskId`) references  pipe_task(`id`)
);

CREATE TABLE `pipe_self_check`  (
    `id` varchar(50)  COMMENT '主键',
    `name` varchar(50)  COMMENT '装备名称' unique,
    `showName` varchar(50)  COMMENT '显示名称, 供前端调用',
    `code` varchar(10)  COMMENT '装备编号',
    `hasErr` tinyint  COMMENT '该装备对应的显示名称是否有Err',
    `createTime` datetime  COMMENT '创建时间',
    `modifyTime` datetime  COMMENT '修改时间',
    `creator` varchar(50)  COMMENT '创建人',
    `sortKey` int  COMMENT '排序字段',
    `disable` tinyint  COMMENT '废弃字段',
    `taskId` varchar(50)  COMMENT '外键-任务id',
    PRIMARY KEY (`id`),
    foreign key (`taskId`) references  pipe_task(`id`)
);

CREATE TABLE `test_res_advice`  (
    `id` varchar(50)  COMMENT '主键',
    `l1Name` varchar(50)  COMMENT '测试项(1级)名称' unique,
    `l2Name` varchar(50)  COMMENT '测试项子项(2级)名称' unique,
    `l2HasErr` tinyint  COMMENT 'l2是否有Err',
    `advice` varchar(10000)  COMMENT '对策建议',
    `createTime` datetime  COMMENT '创建时间',
    `modifyTime` datetime  COMMENT '修改时间',
    `creator` varchar(50)  COMMENT '创建人',
    `sortKey` int  COMMENT '排序字段',
    `disabled` tinyint  COMMENT '废弃字段',
    `taskId` varchar(50)  COMMENT '外键-任务id',
    PRIMARY KEY (`id`),
    foreign key (`taskId`) references  pipe_task(`id`)
);

CREATE TABLE `pipe_history`  (
    `id` varchar(50)  COMMENT '主键',
    `res` blob(100000)  COMMENT '对策建议',
    `createTime` datetime  COMMENT '创建时间',
    `modifyTime` datetime  COMMENT '修改时间',
    `creator` varchar(50)  COMMENT '创建人',
    `sortKey` int  COMMENT '排序字段',
    `disabled` tinyint  COMMENT '废弃字段',
    `taskId` varchar(50)  COMMENT '外键-任务id',
    `pipeTestId` varchar(50)  COMMENT '外键-测试项id',
    PRIMARY KEY (`id`),
    foreign key (`taskId`) references  pipe_task(`id`),
    foreign key (`pipeTestId`) references  pipe_test(`id`)
);
```

测试数据生成

```sql
# 插入任务表项
INSERT INTO `weapon-test`.`pipe_task` (`id`, `name`, `code`, `description`, `createTime`, `creator`, `disabled`, `status`) VALUES ('1', 'task1', '1', 'task1', '2021-09-09 14:23:28', 'nash5', '0', '0');
INSERT INTO `weapon-test`.`pipe_task` (`id`, `name`, `code`, `description`, `createTime`, `creator`, `disabled`, `status`) VALUES ('2', 'task2', '2', 'task2', '2021-09-09 14:23:26', 'nash5', '0', '0');
INSERT INTO `weapon-test`.`pipe_task` (`id`, `name`, `code`, `description`, `createTime`, `creator`, `disabled`, `status`) VALUES ('4', 'task4', '4', 'task4', '2021-09-09 14:24:28', 'nash5', '0', '0');
INSERT INTO `weapon-test`.`pipe_task` (`id`, `name`, `code`, `description`, `createTime`, `creator`, `disabled`, `status`) VALUES ('5', 'task5', '5', 'task5', '2021-09-09 14:25:28', 'nash5', '0', '0');

# 插入自检表表项
INSERT INTO `weapon-test`.`pipe_self_check` (`id`, `name`, `showName`, `code`, `hasErr`, `creator`, `sortKey`, `disabled`, `taskId`) VALUES ('1', '声呐', '舰壳声呐', '1', '0', 'nash5', '0', '0', '1');

# 插入测试项
INSERT INTO `weapon-test`.`pipe_test` (`id`, `name`, `code`, `type`, `creator`, `threshold`, `disabled`, `status`, `taskId`) VALUES ('1', '航空弹道武器通道', '1', '1', 'nash5', '{\"v\": \"15km/h\"}', '0', '1', '1');
INSERT INTO `weapon-test`.`pipe_test` (`id`, `name`, `code`, `type`, `creator`, `threshold`, `disabled`, `status`, `taskId`) VALUES ('2', '反舰炮武器通道', '2', '1', 'nash5', '{\"v\": \"15km/h\"}', '0', '1', '1');
INSERT INTO `weapon-test`.`pipe_test` (`id`, `name`, `code`, `type`, `creator`, `threshold`, `disabled`, `status`, `taskId`) VALUES ('3', '信息流程测试', '3', '2', 'nash5', '{\"time\": \"5s\"}', '0', '1', '1');
```

### 2.2 火力兼容数据库表

```sql
CREATE TABLE `fire_conflict`  (
  `id` varchar(50)  COMMENT '主键',
  `code` varchar(10)  COMMENT '任务编号',
  `name` varchar(50)  COMMENT '兼容预判任务名称',
  `conflictType` varchar(255)  COMMENT '冲突类型(火力兼容，电磁兼容，水声兼容)[dic]',
  `conflictSource` varchar(255)  COMMENT '管控，预判[dic]',
  `taskId` varchar(50)  COMMENT '外键-任务',
  `conflictTime` datetime  COMMENT '冲突发生时间',
  `conflictObjects` varchar(500)  COMMENT '[{id: , name: }, ...]',
  `conflictSolution` varchar(500)  COMMENT '冲突解决',
  `createTime` datetime(0)  COMMENT '创建时间',
  `modifyTime` datetime  COMMENT '修改时间',
  `creator` varchar(50)  COMMENT '创建人',
  `disabled` tinyint  COMMENT '废弃标志',
  PRIMARY KEY (`id`)
);

CREATE TABLE `fire_priority`  (
  `id` varchar(50)  COMMENT '主键',
  `code` varchar(10)  COMMENT '规则序号',
  `conflictType` varchar(10)  COMMENT '冲突类型',
  `createTime` datetime  COMMENT '创建时间',
  `modifyTime` datetime  COMMENT '修改时间',
  `creator` varchar(50)  COMMENT '创建人',
  `disabled` tinyint  COMMENT '废弃字段',
  `ABetterThanB` tinyint  COMMENT 'A对B的优先级',
  `weaponAId` varchar(50)  COMMENT '外键-武器Aid',
  `weaponBId` varchar(50)  COMMENT '外键-武器Bid',
  PRIMARY KEY (`id`)
);

CREATE TABLE `fire_task`  (
  `id` varchar(50)  COMMENT '主键',
  `name` varchar(50)  COMMENT '兼容预判任务名称',
  `code` varchar(10)  COMMENT '任务编号',
  `startTime` datetime  COMMENT '开始时间',
  `createTime` datetime(0)  COMMENT '创建时间',
  `modifyTime` datetime  COMMENT '修改时间',
  `endTime` datetime(0)  COMMENT '结束时间',
  `creator` varchar(50)  COMMENT '创建人',
  `disabled` tinyint  COMMENT '废弃标志',
  `running` tinyint  COMMENT '运行标志'
  PRIMARY KEY (`id`)
);

CREATE TABLE `fire_threshold`  (
  `id` varchar(50)  COMMENT '主键',
  `name` varchar(50)  COMMENT '阈值名称',
  `code` varchar(10)  COMMENT '阈值序号',
  `type` varchar(10)  COMMENT '冲突类型',
  `createTime` datetime  COMMENT '创建时间',
  `modifyTime` datetime  COMMENT '修改时间',
  `creator` varchar(50)  COMMENT '创建人',
  `sortKey` int COMMENT '排序字段',
  `disabled` tinyint  COMMENT '废弃字段',
  `thresholdValue` varchar(255)  COMMENT '安全阈值',
  PRIMARY KEY (`id`)
);

CREATE TABLE `fire_weapon`  (
  `id` varchar(50)  COMMENT '主键',
  `name` varchar(50)  COMMENT '武器名称',
  `code` varchar(10)  COMMENT '武器编号',
  `type` varchar(10)  COMMENT '武器类型',
  `createTime` datetime  COMMENT '创建时间',
  `modifyTime` datetime  COMMENT '修改时间',
  `creator` varchar(50)  COMMENT '创建人',
  `sortKey` int  COMMENT '排序字段',
  `controlled` tinyint  COMMENT '管控标识',
  `selfCheck` tinyint  COMMENT '自检字段',
  `disabled` tinyint  COMMENT '废弃字段',
  PRIMARY KEY (`id`)
);
```



## 3 接口设计

### 3.1 武器通道测试

#### 任务管理 pipe_task

- post /free/task/page 有一个page参数，为{}时获取所有任务

- post /free/task 新建一个任务

- put /free/task 更新一个任务

-  delete /free/task/{id} delete by id

#### 测试项 pipe_test

get /free/task/allPipeTest 获取所有测试项

#### 自检表 

> 对空/水下防御 根据“传感器—指控设备—WQ”表做成表，从报文中获取对应的装备类型的自检状态，然后在该表中映射到前端显示
> 然后得到一个全局的[{"显示项": true/false, ...}, ]，做成接口给前端调用，所以就是一个四栏目的表 selfCheck： 
> taskId, 装备名称, 显示名称, 显示状态
- get /free/self-check/list-all 获取所有自检项

- put /free/self-check 更新一个自检状态

#### 分析弹窗 测试结果以及对策表: L2Name, L1Name, res, 对策建议（全部初始化为true）

#### 对策建议信息管理 更改2.2.2中的测试结果以及对策表的对策建议即可

#### 回放 历史表 taskId, pipeTestId, res(string), 

- 重载update方法以此更新res

- 提供返回res的get方法

### 3.2 火力兼容主要实现接口

#### 兼容预判任务表

- get /free/task/curTask: 获取当前正在运行的任务的状态
- post /free/task/curTask: 初始化预判任务的时候使用
- get /free/task/allTask：返回所有兼容预判任务
- patch /free/task/curTask/{runningStatus}: 更新当前任务执行状态，若所有任务都处于not running的状态，则选取最近执行的任务

#### 兼容预判武器表

- post /free/weapon：新建一个武器项
- delete /free/weapon/{id}：删除一个武器项,byId
- update /free/weapon：更新武器项
- get /free/weapon/{id}: 查询武器项，byId
- get /free/weapon/list-all: 查询所有武器项

#### 兼容预判阈值表

- post /free/threshold：新建一个阈值项
- delete /free/threshold/{id}：删除一个阈值项,byId
- update /free/threshold：更新阈值项
- get /free/threshold/{id}: 查询阈值项，byId
- get /free/threshold/list-all: 查询所有阈值项
- post /free/threshold/page: 分页查询

#### 兼容预判冲突表

- post /free/conflict：新建一个冲突项
- delete /free/conflict/{id}：删除一个冲突项,byId
- update /free/conflict：更新冲突项
- get /free/conflict/{id}: 查询冲突项，byId
- get /free/conflict/list-all: 查询所有冲突项
- get /free/conflict/type-id: 根据冲突类型及任务查询对应冲突

#### 兼容预判优先级表

patch /free/priority：将规则修改为A<B

get /free/priority/{type}：按照类型查找优先级规则, 有fire,sound,magnetic三种类型





### 4 .算法逻辑





