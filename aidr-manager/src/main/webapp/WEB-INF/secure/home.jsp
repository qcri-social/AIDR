<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<base href="${pageContext.request.contextPath}/" target="_blank"/>
    <link rel="shortcut icon" type="image/ico" href="resources/img/favicon.ico" />

    <link rel="stylesheet" type="text/css" href="resources/extjs/resources/css/ext-all.css"/>
    <link rel="stylesheet" type="text/css" href="resources/css/style.css"/>
    <script type="text/javascript" src="resources/extjs/ext-all.js"></script>
    <script type="text/javascript" src="resources/vendor/moment-with-langs.js"></script>
    <script type="text/javascript" src="resources/vendor/moment-lang-en.js"></script>
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