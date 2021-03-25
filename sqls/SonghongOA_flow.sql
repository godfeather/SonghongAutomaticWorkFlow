create table mrright_songhongoa.dbo.flow(
	name nvarchar(20) primary key,
	flow nvarchar(300)
)
drop table flow;
insert into mrright_songhongoa.dbo.flow values
/*
办公相关
*/
('办公用品申领<300','行政助理'),
('办公用品申领>300','部门总监，执行总监，分管副总，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理，行政助理'),
('用车申请','综合办公室部门总监，综合办公室执行总监，综合办公室分管副总'),


/*
资产相关
*/
('入库','采购负责人，会计1，会计2，综合办公室部门总监，综合办公室执行总监'),
('归还=IT','综合办公室部门总监，综合办公室执行总监，领用人，IT负责人，行政助理'),
('归还!=IT','部门总监，执行总监，综合办公室部门总监，综合办公室执行总监，行政助理'),
-- 其中申请人原本为“领用人”，自动流程需要；
('领用','采购负责人，综合办公室部门总监，综合办公室执行总监，申请人，部门总监，执行总监，行政助理'),
('报废','采购负责人，会计1，会计2，综合办公室部门总监，综合办公室执行总监'),
('借用','部门总监，执行总监，所属人，行政助理，申请人，所属人'),
('报修','部门总监，执行总监，综合办公室部门总监，综合办公室执行总监'),
('采购申请','部门总监，执行总监，分管副总，采购负责人，课题负责人，会计1，会计2，综合办公室部门总监，综合办公室执行总监，总经理，采购负责人'),
('bom','部门总监，执行总监，分管副总，总经理'),
('成品','采购负责人，综合办公室部门总监，综合办公室执行总监，申请人，部门总监，执行总监，行政助理'),


/*
培训相关
*/
('培训=计划外','部门总监，执行总监，分管副总，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理，申请人，部门总监，执行总监，分管副总，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理，申请人，部门总监，执行总监'),
('培训=计划内','部门总监，执行总监，综合办公室部门总监，综合办公室执行总监，申请人，部门总监，执行总监，分管副总，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理，申请人，部门总监，执行总监'),


/*
人事相关
*/
('用人','部门总监，执行总监，分管副总，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理'),
('录用','？'),-- 由综合办公室总监发起，选择审核人即可
('离职','部门总监，执行总监，分管副总，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理'),
('岗位变动','？'),-- 由原部门总监发起
('请假单<3','部门总监，执行总监，综合办公室部门总监，综合办公室执行总监'),
-- 下面的流程在自动审核时“发起者”必须填写目标续签员工的名称，而不是实际发起者的名称，实际发起者永远都是张勋
('续签','部门总监，执行总监，分管副总，总经理，综合办公室部门总监，综合办公室执行总监，合同保管员'),
-- 下面的流程在自动审核时，“发起者”必须填离职员工的名称，而不是真实发起者的名称
('交接','部门总监，执行总监，分管副总，会计-李玲，合同保管员，行政助理，党支部委员，运营负责人-陈义然，IT负责人-何继伟，综合办公室部门总监，综合办公室执行总监'),
/*
完整的逻辑是这样的，省略了各种职位发起的case：
	申请人(3天内) >  部门经理 > 办公室经理
	部门副经理：部门经理 > 分管领导 > 办公室经理
	部门经理：分管领导 > 办公室经理
	分管领导：总经理 > 办公室经理
	总经理:  >办公室经理
	申请人(3天及以上) > 部门经理 > 分管领导 > 办公室经理 > 办公室分管领导 > 总经理
*/
('请假单>3','部门总监，执行总监，分管副总，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理'),
-- 下面的流程的驳回测试无法使用系统提供的驳回测试进行测试
('落户&项目','部门总监，执行总监，分管副总，合同保管员，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理，申请人，部门总监，执行总监，分管副总，合同保管员，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理'),



/*
业务相关
*/
('业务接待申请','部门总监，执行总监，分管副总，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理'),
('出差申请','部门总监，执行总监，分管副总，总经理，综合办公室部门总监，综合办公室执行总监'),
('普通章','部门总监，执行总监，分管副总，综合办公室部门总监，综合办公室执行总监'),
('外带章','部门总监，执行总监，分管副总，总经理，综合办公室部门总监，综合办公室执行总监'),
('合同=付款','课题负责人，部门总监，执行总监，法务，分管副总，总经理，综合办公室部门总监，综合办公室执行总监，合同保管员'),
('合同!=付款','部门总监，执行总监，法务，分管副总，总经理，综合办公室部门总监，综合办公室执行总监，合同保管员'),


/*
财务相关
*/
('报销','部门总监，执行总监，分管副总，会计1，会计2，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理，出纳，会计2'),
('收款申请','部门总监，执行总监，会计2，综合办公室部门总监，综合办公室执行总监，分管副总，总经理，出纳，会计2'),
('借款','部门总监，执行总监，分管副总，会计1，会计2，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理，申请人，出纳，会计1，会计2'),
('还款','部门总监，执行总监，分管副总，会计1，会计2，综合办公室部门总监，综合办公室执行总监，综合办公室分管副总，总经理，申请人，出纳，会计1，会计2'),
('付款申请','部门总监，执行总监，会计1，会计2，综合办公室部门总监，综合办公室执行总监，分管副总，总经理，出纳，会计2'),

/*
课题相关
*/
('课题预算申请','课题管理员，课题账目登记人，科研项目负责人'),
('课题预算变更','课题管理员，课题账目登记人，科研项目负责人'),
('课题申请','课题管理员，课题账目登记人，科研项目负责人'),
('党支部公章','党支部书记')
select *from flow;
