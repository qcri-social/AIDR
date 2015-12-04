<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <link rel="shortcut icon" type="image/ico" href="${pageContext.request.contextPath}/resources/img/favicon.ico" />

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/build/packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/font-awesome-4.5.0/css/font-awesome.min.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/vendor/moment-with-langs.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/vendor/moment-lang-en.js"></script>
     <script type="text/javascript" src="${pageContext.request.contextPath}/resources/vendor/moment-lang-en.js"></script>
     <script type="text/javascript" src="${pageContext.request.contextPath}/resources/build/packages/sencha-charts/build/sencha-charts.js"></script>
	<link type="text/css" href="${pageContext.request.contextPath}/resources/build/packages/sencha-charts/build/crisp/resources/sencha-charts-all.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/build/packages/ext-charts/build/ext-charts.js"></script>
    <script type="text/javascript">
        Ext.Loader.setConfig({
                enabled: true,
                disableCaching: false,
                paths : {
                    AIDRFM: '${pageContext.request.contextPath}/resources/js/aidrfm',
                    TAGGUI: '${pageContext.request.contextPath}/resources/js/taggui',
                    ADMIN: '${pageContext.request.contextPath}/resources/js/administration',
                    AIDRPUBLIC: '${pageContext.request.contextPath}/resources/js/aidrpublic'
                }
            }
        );

        var BASE_URL = '<%=request.getContextPath() %>';
    </script>
