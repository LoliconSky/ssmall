<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-Hans">
<head>
    <meta charset="utf-8">
    <title>Test</title>
</head>
<body>
    <h2>Hello World!</h2>
    SpringMVC 文件上传测试：
    <form action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file">
        <input type="submit" value="上传">
    </form>
    <hr>
    富文本图片上传测试：
    <form action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file">
        <input type="submit" value="上传">
    </form>
</body>
</html>
