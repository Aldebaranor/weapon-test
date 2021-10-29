# 0 一些规范以及参考
## 0.1 restful风格 [https://www.cnblogs.com/MTRD/p/12153561.html](https://www.cnblogs.com/MTRD/p/12153561.html)
## 0.2 url规范 [https://www.cnblogs.com/hduwbf/p/7300794.html](https://www.cnblogs.com/hduwbf/p/7300794.html)
## 0.3 sprint boot处理各种需求[https://spring.io/guides](https://spring.io/guides)
## 0.4 使用maven完成liquibase的changelog自动生成：
在对应的数据库中使用相应的sql建好需要的表, 进入对应的模块的根目录，也就是含有pom.xml的目录）执行如下命令：
```sh
mvn liquibase:generateChangeLog
```
# 0 总体数据库表
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

# 1 火力兼容(fire test)
## 火力兼容页面：[https://app.mockplus.cn/app/-voy0P0HScQ3/specs/design/Mgk3rpOTVW2](https://app.mockplus.cn/app/-voy0P0HScQ3/specs/design/Mgk3rpOTVW2)
## WQ测试页面：[https://app.mockplus.cn/app/TTogpb0h7wTB/specs/design/Ny0HajcHvv3F](https://app.mockplus.cn/app/TTogpb0h7wTB/specs/design/Ny0HajcHvv3F)

## 1.1 兼容预判任务表
### 1.1.1 实现
```sql
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
```
### 1.1.2 主要实现接口
#### 1.1.2.1 get /free/task/curTask: 获取当前正在运行的任务的状态
#### 1.1.2.2 post /free/task/curTask: 初始化预判任务的时候使用
#### 1.1.2.3 get /free/task/allTask：返回所有兼容预判任务
#### 1.1.2.4 patch /free/task/curTask/{runningStatus}: 更新当前任务执行状态，若所有任务都处于not running的状态，则选取最近执行的任务

## 1.2 兼容预判武器表

### 1.2.1 主要实现接口
#### 1.2.1.1 post /free/weapon：新建一个武器项
#### 1.2.1.2 delete /free/weapon/{id}：删除一个武器项,byId
#### 1.2.1.3 update /free/weapon：更新武器项
#### 1.2.1.4 get /free/weapon/{id}: 查询武器项，byId
#### 1.2.1.5 get /free/weapon/list-all: 查询所有武器项


## 1.3 兼容预判阈值表

### 1.3.1 主要实现接口
#### 1.3.1.1 post /free/threshold：新建一个阈值项
#### 1.3.1.2 delete /free/threshold/{id}：删除一个阈值项,byId
#### 1.3.1.3 update /free/threshold：更新阈值项
#### 1.3.1.4 get /free/threshold/{id}: 查询阈值项，byId
#### 1.3.1.5 get /free/threshold/list-all: 查询所有阈值项
#### 1.3.1.6 post /free/threshold/page: 分页查询

## 1.4 兼容预判冲突表

### 1.4.1 主要实现接口
#### 1.4.1.1 post /free/conflict：新建一个冲突项
#### 1.4.1.2 delete /free/conflict/{id}：删除一个冲突项,byId
#### 1.4.1.3 update /free/conflict：更新冲突项
#### 1.4.1.4 get /free/conflict/{id}: 查询冲突项，byId
#### 1.4.1.5 get /free/conflict/list-all: 查询所有冲突项
#### 1.4.1.6 get /free/conflict/type-id: 根据冲突类型及任务查询对应冲突

## 1.5 兼容预判优先级表

### 1.5.1 主要实现接口
#### 1.5.1.1 patch /free/priority：将规则修改为A<B
#### 1.5.1.2 get /free/priority/{type}：按照类型查找优先级规则, 有fire,sound,magnetic三种类型



