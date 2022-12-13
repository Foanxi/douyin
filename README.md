# 简介
- 本项目是基于字节青训营的抖音项目进行的后端训练，为了训练大量后端技术的使用以及加深对SpringBoot和SpringCloud的理解，同时熟悉团队开发
- 本项目实现的是前后端分离，前端为字节提供的apk文件，后端为本团队自行开发的服务器
## 技术及工具
![image](https://user-images.githubusercontent.com/84964083/201473627-38dc33bf-3ceb-4355-a1ed-28ced41ce0d2.png)
## 目录结构
```shell
D:.
│  .gitignore
│  douyin.iml
│  mvnw
│  mvnw.cmd
│  pom.xml
│  README.md
│
│
├─sql
│      douyin.sql
│
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─douyin
│  │  │          │  DouyinApplication.java
│  │  │          │
│  │  │          ├─config
│  │  │          │      Interceptor.java
│  │  │          │      InterceptorConfiguration.java
│  │  │          │      NonStaticResourceHttpRequestConfig.java
│  │  │          │      SpringAmqpConfig.java
│  │  │          │
│  │  │          ├─controller
│  │  │          │      CommentController.java
│  │  │          │      FavouriteController.java
│  │  │          │      FeedController.java
│  │  │          │      FileController.java
│  │  │          │      PublishController.java
│  │  │          │      RelationController.java
│  │  │          │      UserController.java
│  │  │          │
│  │  │          ├─mapper
│  │  │          │      CommentMapper.java
│  │  │          │      FavouriteMapper.java
│  │  │          │      RelationMapper.java
│  │  │          │      UserMapper.java
│  │  │          │      VideoMapper.java
│  │  │          │
│  │  │          ├─model
│  │  │          │      CommentModel.java
│  │  │          │      UserModel.java
│  │  │          │      VideoModel.java
│  │  │          │
│  │  │          ├─pojo
│  │  │          │      Comment.java
│  │  │          │      Favourite.java
│  │  │          │      Relation.java
│  │  │          │      User.java
│  │  │          │      Video.java
│  │  │          │
│  │  │          ├─service
│  │  │          │  │  CommentService.java
│  │  │          │  │  FavouriteService.java
│  │  │          │  │  RelationService.java
│  │  │          │  │  UserService.java
│  │  │          │  │  VideoService.java
│  │  │          │  │
│  │  │          │  └─impl
│  │  │          │          CommentServiceImpl.java
│  │  │          │          FavouriteServiceImpl.java
│  │  │          │          RelationServiceImpl.java
│  │  │          │          UserServiceImpl.java
│  │  │          │          VideoServiceImpl.java
│  │  │          │
│  │  │          └─util
│  │  │                  CreateJson.java
│  │  │                  Entity2Model.java
│  │  │                  JwtHelper.java
│  │  │                  Md5.java
│  │  │                  SnowFlake.java
│  │  │                  SpringRabbitListener.java
│  │  │                  VideoProcessing.java
│  │  │
│  │  └─resources
│  │      │  application.yml
│  │      │  logback-spring.xml
│  │      │
│  │      ├─log
│  │      │      SpringbootLog.log
│  │      │
│  │      ├─META-INF
│  │      │      additional-spring-configuration-metadata.json
│  │      │
│  │      ├─public
│  │      │  │  app-release.apk
│  │      │  │
│  │      │  ├─picture
│  │      │  │  └─788388140522930176
│  │      │  │          19a30cde-2f3b-4ce0-8397-7b02c279f9e0.jpg
│  │      │  │
│  │      │  └─video
│  │      │      └─788388140522930176
│  │      │              19a30cde-2f3b-4ce0-8397-7b02c279f9e0.mp4
│  │      │
│  │      └─templates
│  └─test
│      └─java
│          └─com
│              └─douyin
│                      CommentTest.java
│                      DouyinApplicationTests.java
│                      Entity2ModelTest.java
│                      FeedTest.java
│                      logTest.java
│                      SpringAmqpTest.java
│
└─target
```
# 如何使用
1. 获取本项目
  1. 你可以选择在GitHub或华为代码仓库上克隆该项目
https://codehub-cn-south-1.devcloud.huaweicloud.com/douyin00001/douyin.git
https://github.com/Foanxi/douyin.git
  2. 或使用jar包
    - jar包下载链接如下
暂时无法在飞书文档外展示此内容
    - 数据库脚本文件如下
暂时无法在飞书文档外展示此内容
    - 除此之外由于您是在本地部署测试该项目，因此您还需要下载该项目的配置文件application.yml，并与jar包放置在同一文件路径下
暂时无法在飞书文档外展示此内容
2. 在您克隆或下载此处的sql文件以后，您需要使用SQL脚本文件建立对应的数据库
3. 启动该项目
  - 如果您使用克隆的方式，在您完成pom.xml文件的同步后，您需要先在application.yml文件中修改您的数据库相关配置，如用户名与密码，以及您的Resource文件夹所在的绝对路径，以及当前您本地的IP地址，然后在DouyinApplication.java类中启动服务
  - 如果您使用jar包的方式，则您需要首先修改application.yml文件中的相应配置，然后使用如下命令启动服务（除此之外，您也可以双击并在压缩工具中打开jar包中的文件 \douyin.jar\BOOT-INF\classes\application.yml中修改相关配置并保存）
java -jar  douyin.jar --spring.config.location=application.yml
4. 客户端的相关配置
  1. 首先您需要下载该软件安装包
暂时无法在飞书文档外展示此内容
  2. 然后在进入该应用后长按右下角的“我”进入IP配置界面，在此处您需要将您的IP地址修改为您对应的IP地址
