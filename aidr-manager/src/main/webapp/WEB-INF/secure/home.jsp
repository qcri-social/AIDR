<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<base href="${pageContext.request.contextPath}/" target="_blank"/>
    <link rel="shortcut icon" type="image/ico" href="resources/img/AIDR/aidr_logo_30h.png" />

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
 	<!-- Google Tag Manager -->
	<noscript><iframe src="//www.googletagmanager.com/ns.html?id=GTM-TXPWN2"
	height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
	<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
	new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
	j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
	'//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
	})(window,document,'script','dataLayer','GTM-TXPWN2');</script>
	<!-- End Google Tag Manager -->
	
  <script type="text/javascript" src="resources/js/aidrfm/home/Application.js"></script>

  <script type="text/javascript">
      USER_NAME = "${userName}";
      COLLECTION_TYPES = ${collectionTypes};
	  SIGNED_IN_PROVIDER = "${signInProvider}";
  </script>

</body>
</html>