<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.0/themes/base/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.0/jquery-ui.js"></script>

	<style>
		body { margin: 0; font-family: verdana;}
		h1 {text-align: center; margin: 0; padding: 15px;}
		h2 {margin: 0; padding: 20px 0px 5px 0px;}
		.header { top: 0px; height: 80px; border-bottom: 1px #ccc solid;background-color: #eaeaea; }
		.footer { clear: both;  overflow:hidden; border-top: 1px #ccc solid; padding: 10px 0px 0px 0px;}
		.footer .text {float: right; margin-right: 20px;}
		input[type="checkbox"] {display: inline !important; width: 20px; }
		input { display: block; width: 340px; margin: 3px 0px 20px 0px; padding: 5px;}
		label {font-weight: normal; font-size: 15px; display: block;}
		button {background-color: #ccc; color: #000; border: 1px #f4f4f4 solid; margin: 12px 0px; box-shadow: 0 3px 2px #AAA; margin-right: 5px; padding: 5px 10px; font-weight: bold;}
		#dialog {display: none;}
		.ui-dialog{ box-shadow: 0 20px 10px #AAA;}
		.error {border: 1px solid #FBB3B9; color: #f0051e; background: #FFECED; padding: 5px; margin-bottom: 10px; font-size: 12px;font-weight: bold; }
		.link {padding: 0px 30px 20px 30px; background-color: #f4f4f4; border: 1px solid #ccc; font-family: verdana; width: 400px; margin: 0 auto;  margin-top: 40px; margin-bottom: 40px;}
		.login {width: 380px; margin: 0 auto; border: 1px solid #ccc; padding: 30px; margin-top: 80px;}
		.login h2 {margin-top: 0px;}
		.success {border: 1px solid green; color: green; background: #90EE90; padding: 5px; margin-bottom: 10px; font-size: 12px;font-weight: bold;}
		table {width: 460px; margin: 0 auto; border: 1px solid #ccc; margin-bottom: 40px;border-collapse:collapse; }
		tr {margin: 0px; }
		th {text-align: left; border: 1px solid #ccc; padding: 5px;}
		td {border: 1px solid #ccc; padding: 5px; }
		</style>
</head>
<body>
<div class="header"><h1>AIDR-Cloudsourcing</h1></div>