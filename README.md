
#[教务管理系统](https://github.com/pengchengming/myCode/tree/master/PcmExamination)
-	架构：Spring+SpringMVC+Mybatis+shiro+mysql+maven
-	前端：HTML+CSS+JS +Jquery+ Bootstrap
-	功能：（1）管理员模块功能 ，管理员可对教师信息、学生信息、课程信息进行增删改查操作。<br>
          （2）教师模块功能，教师登陆后可以获取其教授的课程列表，并可以给已经选择该课程的同学打分<br>
          （3）学生模块功能，学生登录后，显示其已经选择的课程和已经修完的课程。显示所有课程: 这里可以选修课程；已选课程: 没给出成绩的课程可以进行退课操作。 已修课程: 显示已经修完，老师已经给成绩的课程。

#[公租房审批系统](https://github.com/pengchengming/myCode/tree/master/gzf)

-	架构：Spring+Spring+Spring Security+mysql+maven
-	前端：HTML+CSS+JS +JQuery+ Bootstrap
-	功能：（1）登记企业基本信息和申请列表。房管局：材料预审，资质审核，审核企业的认证管理<br>
     （2）房管局资质审核审批通过后系统将根据企业所属委办推送到对应委办（科委/金融办/商委）进行审批；人社局和人才办资质审核<br>
   （3）用户管理可为审批人用户修改用户信息以及重置密码等。若企业账户第一次登录本系统，需绑定邮箱

#[Pcm论坛](https://github.com/pengchengming/myCode/tree/master/PcmBlog)

- 架构：Spring+Spring MVC+Hibernate

- 数据库：mysql

- 前端HTML+CSS+JS Jquery QRcode kindeditor

- 功能：用户注册（发送邮件告知）； 登录； 浏览文章；留言（登录）； 写文章（登录）； 上传文件（登录）； 下载文件； 人脸检测（百度接口）； 文章分享（二维码）

- 说明：文章URL定位采用RESTful； 密码使用MD5加密； 文件保存uuid实际存储，但数据库有上传名字可以下载时保证上传文件名； 删除文章时实际不删除，改变字段不显示； 写文章用了kindeditor插件； 分享文章用了QRcode插件； 人脸检测用了百度接口；


