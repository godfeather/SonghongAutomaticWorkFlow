# SonghongAutomaticWorkFlow
# 淞泓智能汽车办公自动化系统流程测试工具

淞泓流程测试工具用于对淞泓系统的流程逻辑进行测试，主要功能包含一下：
+ 流程计算： 通过发起人和流程名称计算出流程审核通过时需要经过的每一个审核者。
+ 自动化流程审核：基于流程计算结果，自动登录每一个审核者的账号对指定流程标题的流程进行自动化审核，直到流程结束。
+ 手动流程：手动流程是指通过人工计算流程中每个节点的审核者，并将审核者序列输入后，对指定流程标题的流程进行自动化审核，直到结束。
+ 流程驳回审核：若在执行自动化审核前指定流程--reject 选项并提供有效驳回间隔，则在流程中会审核“不同意”值，直到结束。

### 环境配置：
1. 数据库： 安装sqlserver或mysql,mysql需要8以上；
2. geckroDriver: 火狐浏览器驱动，需要将其加到环境变量path变量中。macOS或Linux参考下面的命令配置
  cd 包含geckroDriver的目录
  sudo echo '# Firefox 驱动' >> /etc/profile
  sudo echo 'export GECKRO_DRIVER=`pwd` >> /etc/profile
  sudo echo 'export PATH=${GECKRO_DRIVER}:${PATH}' >> /etc/profile
  source /etc/profile
3. jre ： java运行环境
  在oracle官网下载jre或jdk并安装，并在控制台或终端输入“java”，若有大量文字输出，则表示安装成功；若出现例如“未找到”或“not found”则说明未成功安装；检查环境变量是否正确配置。

### 程序中定义的基础逻辑：
  1. 发起者不需要自己审核
  2. 除“部门总监”，“执行总监”，“分管副总”以外的其他节点都为**必过节点**
  3. **非必过节点**中连续节点同一人审核时会跳过其他节点并保留职位最大的一个节点审核
### 
