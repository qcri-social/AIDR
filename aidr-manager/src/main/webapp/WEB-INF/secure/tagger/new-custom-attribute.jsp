<jsp:include page="../header.jsp"/>
<title>AIDR - Create custom classifier</title>
</head>
<body class="mainbody">
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/taggui/new-custom-attribute/Application.js"></script>

  <script type="text/javascript">
      CRISIS_CODE = "${code}";
      CRISIS_ID = ${crisisId};
      CRISIS_NAME = "${crisisName}";
      ITEM_SINGULAR = "${item_singular}";
  </script>

</body>
</html>