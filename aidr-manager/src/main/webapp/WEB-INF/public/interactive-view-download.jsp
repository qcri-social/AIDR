<jsp:include page="/WEB-INF/secure/header.jsp"/>
<title>AIDR - Interactive View/Download</title>
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
	
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/aidrpublic/interactive-view-download/Application.js"></script>

  <script type="text/javascript">
      CRISIS_ID = ${crisisId};
      COLLECTION_ID = ${collectionId};
      CRISIS_CODE = "${code}";
      COLLECTION_COUNT = "${count}";
      CRISIS_NAME = "${crisisName}";
      USER_NAME = "${userName}";
      TYPE = "${collectionType}";
      COLLECTION_TYPES = ${collectionTypes};
  </script>

</body>
</html>