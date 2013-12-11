 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
 <head>
 </head>
 <body class="mainbody">

<p>Please choose your user name</p>

<form:form action="" method="post" modelAttribute="signUpForm">
<form:input path="userName"/>
<input type="submit" value="Complete Sign Up"/>
<form:errors path="*" />

</form:form>

</body>
</html>