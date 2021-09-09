# 0 一些规范以及参考
## 0.1 restful风格 [https://www.cnblogs.com/MTRD/p/12153561.html](https://www.cnblogs.com/MTRD/p/12153561.html)
## 0.2 url规范 [https://www.cnblogs.com/hduwbf/p/7300794.html](https://www.cnblogs.com/hduwbf/p/7300794.html)
## 0.3 sprint boot处理各种需求[https://spring.io/guides](https://spring.io/guides))
## 0.4 使用maven完成liquibase的changelog自动生成：
在对应的数据库中使用相应的sql建好需要的表, 进入对应的模块的根目录，也就是含有pom.xml的目录）执行如下命令：
```sh
mvn liquibase:generateChangeLog
```
# 1 总体数据库表
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

CREATE TABLE `self_check`  (
  `id` varchar(50)  COMMENT '主键',
  `name` varchar(50)  COMMENT '装备名称' unique,
  `showName` varchar(50)  COMMENT '显示名称, 供前端调用',
  `code` varchar(10)  COMMENT '装备编号',
  `hassErr` tinyint  COMMENT '该装备对应的显示名称是否有Err',
  `createTime` datetime  COMMENT '创建时间',
  `modifyTime` datetime  COMMENT '修改时间',
  `creator` varchar(50)  COMMENT '创建人',
  `sortKey` int  COMMENT '排序字段',
  `disabled` tinyint  COMMENT '废弃字段',
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

CREATE TABLE `history`  (
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

# 2 武器测试(weapon test pr)
## 2.0 任务管理 pipe_task
### 2.0.1 post /free/task/page 有一个page参数，为{}时获取所有任务
### 2.0.2 post /free/task 新建一个任务
### 2.0.3 put /free/task 更新一个任务
### 2.0.4 delete /free/task/{id} delete by id

## 2.1 测试项 pipe_test
### 2.1.1 get /free/task/allPipeTest 获取所有测试项

## 2.2 状态分析
### 2.2.1  自检表 
> 对空/水下防御 根据“传感器—指控设备—WQ”表做成表，从报文中获取对应的装备类型的自检状态，然后在该表中映射到前端显示
> 然后得到一个全局的[{"显示项": true/false, ...}, ]，做成接口给前端调用，所以就是一个四栏目的表 selfCheck： taskId, 装备名称, 显示名称, 显示状态
#### 2.2.1.0 get 

### 2.2.2 分析弹窗 测试结果以及对策表: L2Name, L1Name, res, 对策建议（全部初始化为true）

## 2.3 对策建议信息管理 更改2.2.2中的测试结果以及对策表的对策建议即可

## 2.4 回放 历史表 taskId, pipeTestId, res(string), 
### 2.4.1 重载update方法以此更新res
### 2.4.2 提供返回res的get方法

