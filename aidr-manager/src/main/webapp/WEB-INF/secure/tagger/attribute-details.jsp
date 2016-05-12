<jsp:include page="../header.jsp"/>
<title>AIDR - Classifiers details</title>
</head>
<body class="mainbody">
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/taggui/attribute-details/Application.js"></script>

  <script type="text/javascript">
      ATTRIBUTE_ID = ${id};
      USER_ID = ${userId};
	  SIGNED_IN_PROVIDER = "${signInProvider}";
  </script>

</body>
</html>