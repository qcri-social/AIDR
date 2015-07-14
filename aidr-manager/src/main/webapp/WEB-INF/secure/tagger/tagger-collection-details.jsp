<jsp:include page="../header.jsp"/>
<title>AIDR - Classifiers</title>
</head>
<body class="mainbody">
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/taggui/tagger-collection-details/Application.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/taggui/common/uiTemplate.js"></script>
  <script type="text/javascript">
      CRISIS_ID = ${crisisId};
      CRISIS_NAME = "${name}";
      CRISIS_TYPE_ID = ${crisisTypeId};
      CRISIS_CODE = "${code}";
      TYPE = "${collectionType}";
      IS_MICROMAPPER_ENABLED = ${isMicromapperEnabled};
      COLLECTION_TYPES = ${collectionTypes};
  </script>

</body>
</html>