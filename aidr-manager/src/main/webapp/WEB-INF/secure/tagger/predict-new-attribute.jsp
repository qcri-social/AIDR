<jsp:include page="../header.jsp"/>
<title>AIDR - Add new classifiers</title>
</head>
<body class="mainbody">
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/taggui/predict-new-attribute/Application.js"></script>

  <script type="text/javascript">
      CRISIS_ID = ${crisisId};
      CRISIS_CODE = "${code}";
      COLLECTION_NAME = "${name}";
  </script>

</body>
</html>