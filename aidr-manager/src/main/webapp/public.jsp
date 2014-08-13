<jsp:include page="WEB-INF/secure/header.jsp"/>
<title>AIDR - Public collections</title>
</head>

<%@ page import = "java.util.ResourceBundle" %>
<%
    ResourceBundle resource = ResourceBundle.getBundle("system");
    String serverUrl = resource.getString("serverUrl");
%>

<script type="text/javascript">
    var SERVER_URL= "<%=serverUrl%>";
</script>

<body class="mainbody">
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/aidrpublic/home/Application.js"></script>
</body>
</html>