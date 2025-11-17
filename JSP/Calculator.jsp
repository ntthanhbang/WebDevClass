<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Calculator Result</title>
</head>
<body>
    <h2>Result</h2>

    <% 
        // Get parameters from the form
        String n1 = request.getParameter("num1");
        String n2 = request.getParameter("num2");
        String op = request.getParameter("op");
        double result = 0;
        boolean valid = true;

            double a = Double.parseDouble(n1);
            double b = Double.parseDouble(n2);

            if (op.equals("+")) {
                result = a + b;
            } else if (op.equals("-")) {
                result = a - b;
            } else if (op.equals("*")) {
                result = a * b;
            } else if (op.equals("/")) {
                if (b == 0) {
                    out.println("<p style='color:red;'>Error: Division by zero!</p>");
                    valid = false;
                } else {
                    result = a / b;
                }
            }
        

        if (valid) {
            out.println("<p>" + n1 + " " + op + " " + n2 + " = " + result + "</p>");
        }
    %>

    
</body>
</html>
