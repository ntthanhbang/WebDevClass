<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
  <title>Registration Result</title>
</head>
<body>
  <h2>Registration Result</h2>

  <%
    // Get form data
    String fullname = request.getParameter("fullname");
    String email = request.getParameter("email");
    String username = request.getParameter("username");
    String pwd = request.getParameter("pwd");
    String repwd = request.getParameter("repwd");

    // Server-side validation
    boolean hasError = false;
    String errorMessage = "";

    // Check if any field is empty
    if (fullname == null || fullname.isEmpty() ||
        email == null || email.isEmpty() ||
        username == null || username.isEmpty() ||
        pwd == null || pwd.isEmpty() ||
        repwd == null || repwd.isEmpty()) {
        hasError = true;
        errorMessage = "All fields are required!";
    }
    // Check if email format is valid (basic regex)
    else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
        hasError = true;
        errorMessage = "Invalid email format!";
    }
    // Check if passwords match
    else if (!pwd.equals(repwd)) {
        hasError = true;
        errorMessage = "Passwords do not match!";
    }

    if (hasError) {
  %>
      <script>
        alert("<%= errorMessage %>");
        window.history.back(); // Go back to the form
      </script>
  <%
    } else {
  %>
      <p><b>Full name:</b> <%= fullname %></p>
      <p><b>Email:</b> <%= email %></p>
      <p><b>Username:</b> <%= username %></p>
      <p><b>Password:</b> <%= pwd %></p>
      <p style="color:green;">✅ Registration Successful!</p>
  <%
    }
  %>

</body>
</html>
