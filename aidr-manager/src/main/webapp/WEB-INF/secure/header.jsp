<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <link rel="shortcut icon" type="image/ico" href="${pageContext.request.contextPath}/resources/img/favicon.ico" />

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/extjs/resources/css/ext-all.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/moment.min.js"></script>
    <script type="text/javascript">
        Ext.Loader.setConfig({
                enabled: true,
                disableCaching: false,
                paths : {
                    AIDRFM: '${pageContext.request.contextPath}/resources/js/aidrfm',
                    TAGGUI: '${pageContext.request.contextPath}/resources/js/taggui',
                    ADMIN: '${pageContext.request.contextPath}/resources/js/administration'
                }
            }
        );
        var BASE_URL = '<%=request.getContextPath() %>';
    </script>
