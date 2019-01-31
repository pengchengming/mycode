# PcmBlog

架构：Spring+Spring MVC+Hibernate  

数据库：mysql

前端HTML+CSS+JS  Jquery QRcode kindeditor

功能：用户注册（发送邮件告知）； 登录； 浏览文章；留言（登录）； 写文章（登录）； 上传文件（登录）； 下载文件； 人脸检测（百度接口）； 文章分享（二维码） 

说明：文章URL定位采用RESTful；
       密码使用MD5加密；
       文件保存uuid实际存储，但数据库有上传名字可以下载时保证上传文件名；
       删除文章时实际不删除，改变字段不显示；
       写文章用了kindeditor插件；
       分享文章用了QRcode插件；
       人脸检测用了百度接口；
