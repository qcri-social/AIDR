<jsp:include page="../header.jsp"/>
<title>AIDR - Training examples</title>
</head>
<body class="mainbody">
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/taggui/training-data/Application.js"></script>

  <script type="text/javascript">
      CRISIS_ID = ${crisisId};
      CRISIS_CODE = "${code}";
      CRISIS_NAME = "${crisisName}";
      MODEL_ID = ${modelId};
      MODEL_FAMILY_ID = ${modelFamilyId};
      MODEL_NAME = "${modelName}";
      ATTRIBUTE_ID = "${attributeID}";
  </script>

</body>
</html>