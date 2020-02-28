# deep-console

> 上传控制台，excel导入，数据管理系统

## Features 特征

- 开箱即用，使配置文件（如服务端口、数据库连接配置）分离出来外部单独文件，社区版可使用derby嵌入式数据库，更便捷的预览使用。

## Structure 结构说明

|key            |desc
|---------------|----
|common/        |公共模块，存放全局常量、异常处理、工具类等
|config/        |工程配置模块
|console/       |web控制台
|core/          |核心模块，启动监听器、权限认证等
|distribution/  |打包发布模块，存放启动脚本，Linux/Windows启动方式
|test/          |测试模块
|pom.xml        |maven主配置文件

+ /console/src/main/java/../ConsoleApplication  启动类

## Licenses 使用许可（下载该源代码资源即视为接受以下许可）

+ 您可以免费获得该源代码的原版拷贝。
+ 您可以自由地对该源代码进行分发、重构并运用于任何领域。
+ 作者对于使用该源代码造成的任何后果均无需负责。
+ 作者对该源代码具有版权。
