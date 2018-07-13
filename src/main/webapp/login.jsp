<%--
  Created by IntelliJ IDEA.
  User: 冰封承諾Andy
  Date: 2018/7/13
  Time: 10:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登陆</title>
</head>
<body>
    <form action="/manage/user/login.do" method="post">
        用户名：<input type="text" name="username"> <br>
        密码： <input type="password" name="password"> <br>
        <input type="submit" value="提交">
    </form>
</body>
</html>
