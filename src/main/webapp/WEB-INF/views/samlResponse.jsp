<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body onload='document.forms["saml-form"].submit()'>
<FORM name='saml-form' action='${URL}' method='POST'>
<INPUT type='hidden' name='SAMLResponse' value='${encodedXml}'/>
<INPUT type='hidden' value='submit' />
</FORM>
</body>
</html>