--CREATE DATABASE mrright_songhongoa;
create table mrright_songhongoa.dbo.staff(
	account varchar(20),
	name varchar(20),
	level int,
	post varchar(20),
	department varchar(20)
)
/*
	该表为淞虹职员表，
*/
drop table flow,staff;
insert into mrright_songhongoa.dbo.staff values
('zuofutao','左伏桃','5','总经理','no'),--
('zuofutao','左伏桃','5','党支部书记','no'),--
('guorunqing','郭润清','4','分管副总','测试认证部'),--
('guochenxiao','郭晨啸','1','普通员工','战略规划部'),--
('guorunqing','郭润清','4','分管副总','运营管理部'),--
('fanxiaoxu','樊晓旭','3','执行总监','战略规划部'),--
('tongbaofeng','童宝锋','3','执行总监','测试认证部'),--
('gefei','葛飞','1','普通员工','测试认证部'),--
('heyun','何鋆','1','普通员工','测试认证部'),
('xuzhendong','许振东','1','普通员工','测试认证部'),--
('qiwenqiang','戚文强','1','普通员工','测试认证部'),--
('jiangli','江力','1','运营负责人','测试认证部'),--
('zhangjunjie','张俊杰','1','普通员工','测试认证部'),--
('zengronglin','曾荣林','1','普通员工','测试认证部'),--
('wangyuxuan','王宇轩','1','普通员工','综合办公室'),--
('wangshuaiyu','王帅宇','1','普通员工','技术研发部'),--
('lihuafei','李华飞','1','普通员工','测试认证部'),--
('liduan','李端','1','普通员工','战略规划部'),--
('xieyilin','谢益林','1','普通员工','战略规划部'),--
('wujunxian','吴俊贤','1','普通员工','战略规划部'),--
('yixiaowei','仪孝伟','1','普通员工','战略规划部'),--
('chendong','陈栋','1','普通员工','运营管理部'),--
('nijingjing','倪晶晶','1','普通员工','运营管理部'),--
('langwei','郎威','1','普通员工','技术研发部'),--
('liujingqing','刘靖馨','1','普通员工','技术研发部'),--
('sunzhe','孙哲','1','法务','法务部'),
('gonglijun','龚黎军','1','IT负责人','运营管理部'),--
('hejiwei','何继伟','1','IT负责人-何继伟','运维管理部'),--
('huangyuqing','黄钰情','1','普通员工','技术研发部'),--
('yankaiwen','颜楷文','1','普通员工','技术研发部'),--
('lijing','李晶','1','普通员工','技术研发部'),--
('huoyanyan','霍燕燕','1','普通员工','战略规划部'),--
('liucaijuan','刘彩绢','1','普通员工','技术研发部'),--
('lingjie','凌洁','1','普通员工','技术研发部'),--
('xiaoxuan','肖轩','1','普通员工','技术研发部'),--
('chenbin','陈彬','1','普通员工','运营管理部'),--
('chenyiran','陈义然','1','运营负责人','运营管理部'),--
('chenyiran','陈义然','1','运营负责人-陈义然','运营管理部'),--
('zhouyi','周轶','1','普通员工','技术研发部'),--
('lihaiyong','李海勇','1','普通员工','技术研发部'),--
('liangzengzhi','梁增智','1','普通员工','技术研发部'),--
('huangjianqi','黄剑其','1','普通员工','技术研发部'),--
('yangtiandong','杨天栋','1','普通员工','技术研发部'),--
('linzhongpu','林中朴','1','普通员工','技术研发部'),--
('renmengjiao','任梦娇','2','部门总监','测试认证部'),--
('renmengjiao','任梦娇','1','党支部委员','测试认证部'),--
('caitianwei','蔡天威','1','普通员工','测试认证部'),--
('zhouming','周明','1','普通员工','技术研发部'),--
('zhouliu','周柳','1','普通员工','运营管理部'),--
('qiutian','邱天','1','普通员工','技术研发部'),--
('xuyangcheng','徐扬程','1','普通员工','技术研发部'),--
('fengjipeng','冯纪朋','1','部门总监','战略规划部'),--
('zhanglei','张雷','1','普通员工','综合办公室'),--
('liling','李玲','1','会计','综合办公室'),--
('qiuxiaoting','邱晓婷','1','会计','综合办公室'),--
('luqinyue','陆沁悦','1','出纳','综合办公室'),--
('shenggang','盛刚','1','采购负责人','综合办公室'),--
('xianglili','项丽莉','1','行政助理','综合办公室'),--
('zhangxun','张勋','1','合同保管员','综合办公室'),--
('linyu','林瑜','4','分管副总','技术研发部'),--
('zhaoyan','赵艳','4','分管副总','综合办公室'),--
('zuofutao','左伏桃','4','分管副总','战略规划部'),
('weijunsheng','魏俊生','3','执行总监','技术研发部'),
('dourui','窦瑞','3','执行总监','技术研发部'),
('zhaoyan','赵艳','3','执行总监','综合办公室'),
('hanhui','韩慧','3','执行总监','运营管理部'),
('hanhui','韩慧','2','部门总监','运营管理部'),
('ankang','安康','2','部门总监','技术研发部'),
('ankang','安康','1','课题负责人','技术研发部'),
('申请人','申请人部门总监','1','部门总监','申请人'),
('申请人','申请人执行总监','1','执行总监','申请人'),
('申请人','申请人分管副总','1','分管副总','申请人'),
('所属人','所属人部门总监','1','部门总监','所属人'),
('所属人','所属人执行总监','1','执行总监','所属人'),
('所属人','所属人分管副总','1','分管副总','所属人'),
('所属人','所属人','1','所属人','所属人'),
('申请人','申请人','1','申请人','申请人'),
('zhaoyan','赵艳','2','部门总监','综合办公室'),--
('领用人','领用人',1,'领用人','领用人'),
('jiangli','江力',1,'运营负责人','测试认证部'),
('liling','李玲',1,'会计-李玲','综合办公室'),
('qiuxiaoting','邱晓婷','1','会计1','综合办公室'),
('liling','李玲',1,'会计2','综合办公室')
