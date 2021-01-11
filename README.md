# SonghongAutomaticWorkFlow
# 淞泓智能汽车办公自动化系统流程测试工具

淞泓流程测试工具用于对淞泓系统的流程逻辑进行测试，主要功能包含一下：
+ 流程计算： 通过发起人和流程名称计算出流程审核通过时需要经过的每一个审核者。
+ 自动化流程审核：基于流程计算结果，自动登录每一个审核者的账号对指定流程标题的流程进行自动化审核，直到流程结束。
+ 手动流程：手动流程是指通过人工计算流程中每个节点的审核者，并将审核者序列输入后，对指定流程标题的流程进行自动化审核，直到结束。
+ 流程驳回审核：若在执行自动化审核前指定流程--reject 选项并提供有效驳回间隔，则在流程中会审核“不同意”值，直到结束。

## 环境配置：
1. 数据库： 安装sqlserver或mysql,mysql需要8以上；
2. geckroDriver: 火狐浏览器驱动，需要将其加到环境变量path变量中。macOS或Linux参考下面的命令配置
  cd 包含geckroDriver的目录
  sudo echo '# Firefox 驱动' >> /etc/profile
  sudo echo 'export GECKRO_DRIVER=`pwd` >> /etc/profile
  sudo echo 'export PATH=${GECKRO_DRIVER}:${PATH}' >> /etc/profile
  source /etc/profile
3. jre ： java运行环境
  在oracle官网下载jre或jdk并安装，并在控制台或终端输入“java”，若有大量文字输出，则表示安装成功；若出现例如“未找到”或“not found”则说明未成功安装；检查环境变量是否正确配置。
## 程序配置文件：
    首次运行程序时（可通过\[java -jar 程序包\]运行指定jar包）程序会在当前目录创建properties/properties.txt文件；该程序即程序运行时配置文件；首次使用配置文件时，需阅读理解配置文件中的注释行意义，完全理解后可将其中的‘#’开始的注释行删除，以方便配置。
    若配置文件错误或损坏时可直接删除properties目录并重新执行程序即可生成新的配置文件
## 数据库：
固定库名：mrright_songhongoa
数据表：flow
   |列名|数据类型(长度)|描述|
   |:----:|:----:|:----|
   |name|nvarchar(20)|流程名称，通过流程名称访问流程申明序列|
   |flow| nvarchar(300)|流程申明序列|
   
数据表：staff
   |列名|数据类型(长度)|描述|
   |:----:|:-----:|:------|
   |account|varchar(20)|用于账户|
   |name|varchar(20)|对应人员的真实姓名|
   |level|int|岗位级别，**非必过节点**中通过发起人该值的大小和流程申明中每一个节点比较，若该值比节点中人员的值大，则跳过该人员审核|
   |post|varchar(20)|岗位|
   |department|varchar(20)|部门|
## 流程申明：
  流程申明即向数据表flow中插入一条数据即可，名称则根据需要自由命名即可，但需要注意的是，虽然数据库中并未对该字段进行约束，插入时也尽量保持名称唯一，程序中通过名称取流程，并且以左边开始模糊匹配，若匹配多个，则取第一个；
  流程申明序列通过，（**全角逗号**）分割，例如下面的流程序列：
  
  执行总监，分管副总，综合办公室执行总监，综合办公室分管副总，总经理
  
  上面的流程中每个节点包含两段定义：前面为部门，后面为职位；例如“综合办公室分管副总”，它表示该节点需要department='综合办公室' and post='分管副总'（语义化sql条件）来审核；若申明中只包含职位而不包含部门时，则表示使用发起人岗位下的对应职位；例如“分管副总”表示该节点需要department=“流程发起者所在部门” and post = '分管副总'（语义化sqL条件）来审核；如果查询结果包含多个，则流程计算结果将显示多个同部门的人顺序审核
  
  【注意】：目前分上下级的职位并非可以任意定义，必须使用部门总监，执行总监和分管副总，其大小关系为：部门总监 < 执行总监 < 分管副总；在添加流程申明和员工信息时，都需要使用这些关键字流程才会被正确计算；否则除这些词的岗位以外，都会被程序认为是**必过节点**
  
  【建议】：为了确保流程脚本文件中始终为最新流程，请不要直接对数据表做更新操作，应当首先保持更新sql脚本文件为最新后，再删除原始库或表后使用脚本重新创建。
  
  当前最新流程会更新到项目sqls文件夹中的.sql文件中；可直接使用。
  
## 工具使用：
  该工具由java编写，基于selenium web自动化测试框架实现；通过命令行方式执行；
  
  在配置文件中配置数据库和登录页地址后，即可开始使用。
  
  执行格式为： java -jar “程序包.jar” [参数列表...];
  
  在命令行输入 java -jar “程序包.jar” -h '' 查看帮助信息
  
  因为main函数入口的参数仅支持键值对，所以参数列表最后单独的一个值将会被忽略，所以如果只传入-h程序是无法接收到该参数的，所以单个参数可以在后面任意添加字符将其传入。
  
  使用-h可以列出程序的可用选项，也可以不传入任何参数通过程序提示传入必要的参数；
  
  【注意】：参数区分大小写
  #### 下面是必要参数的说明：
  
  1. -m 该参数指定程序已何种方式运行，有效值有calc,auto,manual
      
  #### calc 计算流程：
    通过-m选项指定calc后，并提供-r选项指定发起人，-n选项指定流程名称后，计算出需审核序列：
    
    例如控制台输入下列语句的输出结果：
    
    java -jar release.jar -m calc -n 业务 -r 陈彬
|序号|流程名称（Process）|审核者（Auditer）|
|:----:|:----:|:----:|
|1|执行总监|韩慧|
|2|分管副总|郭润清|
|3|综合办公室分管副总|赵艳|
|4|总经理|左伏桃|

【警告】： **若输出内容开头包含“未找到行！”时，本次计算结果不可信，需根据输出的sql语句检查数据库对应流程和对应人员。**
#### auto 自动执行审核：
  通过-m选项指定auto后，并提供-r选项指定发起人，-n选项指定流程名称，-t选项指定流程标题后；程序会首先根据发起人和流程标题获取流程审核者列表；再将流程审核者列表和流程标题交给自动审核模块；自动审核模块会按照流程审核者列表的顺序开始登录各自的账号完成审核操作。
  审核完毕后可检查刚才发起的流程是否已审核通过。
  
  若流程审核过程中因任何问题导致预期人员的流程列表中并未出现指定标题的流程时，则程序会提示并暂停；此时需确认被测系统的指定流程是否与预期不符，若被测系统OK，则需检查程序的流程申明；处理完毕后需要继续审核下一节点则输入“done”，若想终止则输入“quit”退出程序。
  
  若执行程序时指定了-R 选项则表示执行驳回测试，该选项用于指定驳回间隔数；在实际流程审核者列表中第一位是发起人（下标0），流程始终是从下标1开始顺序审核；而如果指定驳回测试，并包含有效的驳回间隔（-1 < interval < 审核者列表长度)时流程审核时会包含“不同意”的审核，否则流程节点均审核为“同意”；当流程审核“不同意”时， 程序会立即登录审核者列表中下标为0（发起者）的用户账户进行重新提交操作，操作完成后，将流程审核者指针置为0，此时流程会重新回到第一个审核者开始审核；若驳回间隔设置为0，则表示没有间隔，每个节点都会执行一次驳回。
  
  驳回测试是非常耗时间的，例如1个12个审核节点的流程驳回时将间隔设为0后，则该流程将审核至少(12\*12)/2=72次；12个节点的流程在目前系统中算中等长度的流程
  
 #### 自动审核中容易遭遇的断点：
  遭遇程序
 1. 登录：
    + 用户名或密码错误： 登录操作失败的原因包括登录账户的用户名或密码不正确，在系统中仅提供使用全局密码进行自动登录操作，若所有用户密码不相同，则该操作无法成功（）。
    + 网络原因： 若因网络原因导致长时间未进入首页时，程序也会因此暂停；此时建议更换网络环境，或增大loginTryingCount的次数（配置文件中）
    遭遇登录问题后，检查问题原因并修复后，需使用目标用户的账
### 程序中定义的流程基础逻辑：
    语义定义：
      流程申明：flow表中的flow字段
    
      必过节点：指无论如何都需要由指定人员审核的节点，节点所属岗位不分上下级关系，因此不参与流程基础逻辑，即流程申明中包含则最终流程生成后就一定存在；例如“申请人”，“会计”，“课题负责人”等
      
      非必过节点：审核节点中岗位有明确等级的为“非必过节点”，(如总经理 > 分管副总 > 执行总监，像这样能比较职位大小的为非必过节点)，非必过节点会因为下面的逻辑导致最终生成的流程序列中跳过不必要的审核；
  1. **非必过节点**中发起者不需要自己审核
  2. **非必过节点**中连续节点同一人审核时会跳过前面的节点并保留其中的最后一个节点
  3. **非必过节点**岗位的上级发起流程不需要**非必过节点**岗位的下级审核
  4. 非必过节点的某个岗位下有多个人时，其中一人发起流程需要其他人审核
