<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String color = request.getParameter("color");
    if (color == null || color.isEmpty()) {
        color = "white";
    }
%>

<!DOCTYPE html>
<html>
<head>
  <title>Background Color Changer</title>
</head>
<body style="background-color:<%= color %>;">
  <center>
    <h2>Choose a Background Color</h2>

    <form method="post">
      <input type="submit" name="color" value="Red">
      <input type="submit" name="color" value="Blue">
      <input type="submit" name="color" value="Green">
      <input type="submit" name="color" value="Pink">
      <input type="submit" name="color" value="Yellow">
    </form>

    <p>Current color: <b><%= color %></b></p>
  </center>
</body>
</html>
