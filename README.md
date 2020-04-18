# fdr-manager

作者的开源项目 `flight-data-record`（简写为 `fdr`） 的管理器，提供了一个可以提供维护和查询的web ui界面。

[什么是fdr？它到底是干什么的？](https://github.com/DwArFeng/fdr)

## 界面

- 实体管理
  - 数据点管理
  - 过滤器管理
  - 触发器管理
- 数据支持
  - 过滤器支持
  - 触发器支持
  - 映射器支持
- 数据查询
  - 实时数据查询
  - 持久数据查询
  - 过滤数据查询
  - 触发数据查询

---

## 如何使用

1. 下载源码
   ```
   git clone git@github.com:DwArFeng/fdr-manager.git
   ```
   对于中国用户，可以使用gitee进行高速下载。
   ```
   git clone git@gitee.com:dwarfeng/fdr-manager.git
   ```
   
2. 项目打包

   进入项目根目录，执行maven命令
   ```
   mvn clean package
   ```
   该过程会下载node v12.16.1，中国用户的下载速度可能会很慢，请视情况翻墙或者提前准备文件。

3. 解压

   找到打包后的目标文件 
   ```
   fdr-manager-distribution/target/fdr-manager-distribution-1.0.0.a-SNAPSHOT-release.tar.gz
   ```
   将其解压至tomcat的根目录内即可。

4. 配置

   1. 确认tomcat的工作目录在tomcat的根目录下，否则需要将 conf/fdr-manager 文件夹移动到tomcat的工作目录中。
      
      *提示: 如果您使用windows的tomcat，那么工作目录可能是 bin/*
      
   2. 配置conf文件夹下的内容
   
      conf/dubbo/connection.properties
      ``` properties
      dubbo.zookeeper.address=zookeeper://192.168.154.100:2181
      dubbo.port=20030
      dubbo.qos.port=21030
      dubbo.host=192.168.154.1
      ```
      
      conf/fdr/exception.properties
      ``` properties
      # fdrmanager 工程中 subgrade 的异常代号偏移量。
      fdr_manager.exception_code_offset.subgrade=0
      ```
      

6. enjoy it

---

## 版本对应

|fdr-manager版本|fdr版本|
|:---|:---|
|1.0.0|1.6.3-1.6.x|
