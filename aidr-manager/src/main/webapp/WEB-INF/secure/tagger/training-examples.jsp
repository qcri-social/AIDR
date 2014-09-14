<jsp:include page="../header.jsp"/>
<title>AIDR - Human-tagged items</title>
</head>
<body class="mainbody">
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/taggui/training-examples/Application.js"></script>

  <script type="text/javascript">
      CRISIS_ID = ${crisisId};
      CRISIS_CODE = "${code}";
      CRISIS_NAME = "${crisisName}";
      MODEL_ID = ${modelId};
      MODEL_FAMILY_ID = ${modelFamilyId};
      NOMINAL_ATTRIBUTE_ID = ${nominalAttributeId};
      MODEL_NAME = "${modelName}";
      TYPE = "${collectionType}";
      COLLECTION_TYPES = ${collectionTypes};
  </script>

</body>
</html>