<jsp:include page="header.jsp"/>
<title>AIDR - Create new collections</title>
</head>
<body class="mainbody">
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/aidrfm/collection-create/Application.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/aidrfm/common/lang.js"></script>
  <script type="text/javascript">
      COLLECTION_TYPES = ${collectionTypes};
      USER_NAME = "${userName}";
      USER_ID = "${userId}";
	  SIGNED_IN_PROVIDER = "${signInProvider}";
  </script>
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>

</body>
</html>