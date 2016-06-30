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
<!-- Google Tag Manager -->
	<noscript><iframe src="//www.googletagmanager.com/ns.html?id=GTM-TXPWN2"
	height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
	<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
	new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
	j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
	'//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
	})(window,document,'script','dataLayer','GTM-TXPWN2');</script>
	<!-- End Google Tag Manager -->
	
  <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/aidrpublic/home/Application.js"></script>
</body>
</html>