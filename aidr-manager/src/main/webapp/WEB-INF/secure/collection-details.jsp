<jsp:include page="header.jsp"/>
<title>AIDR - Collection details</title>
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
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/aidrfm/collection-details/Application.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/aidrfm/common/lang.js"></script>

  <script type="text/javascript">
      COLLECTION_ID = ${id};
      COLLECTION_CODE = "${collectionCode}";
      USER_NAME = "${userName}";
      TYPE = "${collectionType}";
      COLLECTION_TYPES = ${collectionTypes};
	  SIGNED_IN_PROVIDER = "${signInProvider}";
  </script>
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
</body>
</html>