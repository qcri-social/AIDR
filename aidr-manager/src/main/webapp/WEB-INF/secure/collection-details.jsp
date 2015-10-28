<jsp:include page="header.jsp"/>
<title>AIDR - Collection details</title>
</head>
<body class="mainbody">
    
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/aidrfm/collection-details/Application.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/aidrfm/common/lang.js"></script>

  <script type="text/javascript">
      COLLECTION_ID = ${id};
      COLLECTION_CODE = "${collectionCode}";
      USER_NAME = "${userName}";
      TYPE = "${collectionType}";
      COLLECTION_TYPES = ${collectionTypes};
  </script>

</body>
</html>