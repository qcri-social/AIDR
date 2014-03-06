<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>AIDR - Login</title>

    <link rel="shortcut icon" type="image/ico" href="${pageContext.request.contextPath}/resources/img/favicon.ico" />

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css"/>

</head>
<script type="text/javascript">
    setTimeout("submitform()",3000);
    function submitform(){
        document.forms["login"].submit();
    }
</script>
<body class="mainbody index">

<div class="headerWrapper">
    <div class="header"><img src="${pageContext.request.contextPath}/resources/img/AIDR/aidr_logo_240x90.png"></div>
</div>

<div class="mainWraper">
    <div class="main">
        <div>
            <p align="center">Welcome! We are redirecting you to Twitter to sign-in. Please note that <strong>during the testing phase we will clean-up data and restart frequently<strong></p>
            <div>
                <form action="signin/twitter" method="POST" id="login">
                    <input type="image" src="${pageContext.request.contextPath}/resources/img/tweeterSignin.png">
                </form>

            </div>
            <br/><br/>
        </div>
    </div>
</div>

<div class="site-footer">
    <div class="footer">
        <div class="right">
            <a href="http://www.qcri.qa/">A project by
                <img align="middle" src="${pageContext.request.contextPath}/resources/img/qcri-gray-horiz.png"/></a>
        </div>
        <div class="left">
            <a href="http://aidr.qcri.org/tos/">Terms of Service</a>
        </div>
    </div>
</div>

</body>
</html>