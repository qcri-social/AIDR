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
      TRAINING_EXAMPLE = "${trainingExamples}";
      MODEL_AUC = "${modelAuc}";
      RETRAINING_THRESHOLD = "${retrainingThreshold}";
      ITEM_SINGULAR = "${item_singular}";
      ITEM_PLURAL = "${item_plural}";
  </script>

</body>
</html>