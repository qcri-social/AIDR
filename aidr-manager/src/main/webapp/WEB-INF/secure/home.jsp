<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<base href="${pageContext.request.contextPath}/" target="_blank"/>
    <link rel="shortcut icon" type="image/ico" href="resources/img/favicon.ico" />

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/extjs/resources/themes/ext-theme-crisp/resources/ext-theme-crisp-all.css"/>
    <link rel="stylesheet" type="text/css" href="resources/css/style.css"/>
    <link rel="stylesheet" href="resources/font-awesome-4.5.0/css/font-awesome.min.css">
    <script type="text/javascript" src="resources/extjs/ext-all.js"></script>
    <script type="text/javascript" src="resources/vendor/moment-with-langs.js"></script>
    <script type="text/javascript" src="resources/vendor/moment-lang-en.js"></script>
     <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
     <script type="text/javascript" src="${pageContext.request.contextPath}/resources/extjs/resources/charts/ext-charts.js"></script>
	<link type="text/css" href="${pageContext.request.contextPath}/resources/extjs/resources/charts/sencha-charts/build/crisp/resources/sencha-charts-all.css">
   <script type="text/javascript" src="${pageContext.request.contextPath}/resources/extjs/resources/themes/ext-theme-crisp/ext-theme-crisp.js"></script>
   <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/aidrfm/common/lang.js"></script>
    <script type="text/javascript">
        Ext.Loader.setConfig({
                enabled: true,
                disableCaching: false,
                paths: {
                    AIDRFM: 'resources/js/aidrfm',
                    TAGGUI: 'resources/js/taggui',
                    ADMIN: 'resources/js/administration',
                    AIDRPUBLIC: 'resources/js/aidrpublic'
                }
            }
        );

        var BASE_URL = '${pageContext.request.contextPath}';
    </script>
    <title>AIDR - My collections</title>
</head>
<body class="mainbody">
  <script type="text/javascript" src="resources/js/aidrfm/home/Application.js"></script>

  <script type="text/javascript">
      USER_NAME = "${userName}";
      COLLECTION_TYPES = ${collectionTypes};
  </script>

</body>
</html>