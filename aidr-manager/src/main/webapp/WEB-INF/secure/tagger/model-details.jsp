<jsp:include page="../header.jsp"/>
<title>AIDR - Classifier details</title>
</head>
<body class="mainbody">
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/taggui/model-details/Application.js"></script>

  <script type="text/javascript">
      CRISIS_ID = ${crisisId};
      MODEL_ID = ${modelId};
      MODEL_FAMILY_ID = ${modelFamilyId};
      CRISIS_CODE = "${code}";
      MODEL_NAME = "${modelName}";
      MODEL_AUC = ${modelAuc};
      CRISIS_NAME = "${crisisName}";
      USER_ID = ${userId};
      ATTRIBUTE_ID = ${attributeId};
      ITEM_SINGULAR = "${item_singular}";
      ITEM_PLURAL = "${item_plural}";

  </script>

</body>
</html>