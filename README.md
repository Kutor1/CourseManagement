# CourseManagement

G628Lab 二面实验考核，采用前后端分离开发模式，完成基本课程管理系统的基本功能实现。

## 技术栈
- Springboot
- Mybatisplus
- Apache Tika
- Lombok
- Vue3.js
- ElementPlus

## 特性
- 使用Mybatisplus简化数据层与服务层开发
- 使用Lombok简化抽象类开发
- 使用Apache Tika实现自动检测传输文件类型
- 使用ElementPlus组件库美化前端页面

## 功能
- 课程管理系统的基本功能：登录，注册，选课，退课，设置课程，查看课程详情等基本课程功能。
- 用户端：教师端和学生端。在课程功能进行区分。同时实现用户登录状态的控制，以管理用户多端登录的情况。
- 用户界面基本功能：用户密码与用户名。
- 课程管理系统异步更新：选退课与设置课程功能与数据库同步，前端与后端配合完成课程功能的同步更新显示。 
- 配有控制台功能：管理员通过控制台完成对用户信息的监控与修改。
- 拥有一个文件管理功能：能够上传文件与下载文件，同时能同步显示服务端文件信息。

## 安装
1. 克隆仓库：
```bash
git clone git@github.com:Kutor1/CourseManagement.git
```
2. 进入项目目录
```bash
cd CourseManagement
```

3. 根据前后端选择完成依赖构建
