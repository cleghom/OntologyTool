# OntologyTool
Java实现中文本体自动构建工具：A program about how to build an ontology automatically itself.This program can translate chinese information into an standard ontology in OWL format
同期论文：[基于认知算法的中文本体自动构建工具研究与实现](http://xueshu.baidu.com/usercenter/paper/show?paperid=382be71dfa00235de627aa315d73c26d&site=xueshu_se&hitarticle=1)

---

# step1：下载相关需要额外配置的内容
>[下载地址](暂时为定义！)
>配置方法：将下载后的project_jars放置在OntologyTool路径下
>路径下内容如下：
- **project_jars**
 -- **HanLP**(文件夹)
 -- **htmlunit-2.15-bin**(文件夹)
 -- **Protege_3.5**(文件夹)
 -- `dom4j-1.6.1.jar`(文件)
 -- `forms-1.3.0.jar`(文件)
 -- `jsoup-1.7.2.jar`(文件)

# step2：参照.idea/libraries/* 添加jar包
>都在**project_jars/**下

# step3: 配置`src/hanlp.properties`和`bin/hanlp.properties`
>root地址修改为你的词典的绝对地址

# step4: 运行src/mainView/MainForm
>右键运行即可！