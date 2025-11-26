package controller;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet("/change-password")
public class ChangePasswordController extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("views/change-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get current user session
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            resp.sendRedirect("login");
            return;
        }

        // Get form params
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        // Validate current password
        if (!BCrypt.checkpw(currentPassword, currentUser.getPassword())) {
            req.setAttribute("error", "Current password is incorrect.");
            req.getRequestDispatcher("views/change-password.jsp").forward(req, resp);
            return;
        }

        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            req.setAttribute("error", "New password must be at least 6 characters.");
            req.getRequestDispatcher("views/change-password.jsp").forward(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error", "New password and confirmation do not match.");
            req.getRequestDispatcher("views/change-password.jsp").forward(req, resp);
            return;
        }

        // Hash new password
        String newHashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Update DB
        boolean updated = userDAO.updatePassword(currentUser.getId(), newHashed);

        // Show success/error message
        if (!updated) {
            req.setAttribute("error", "Failed to update password.");
            req.getRequestDispatcher("views/change-password.jsp").forward(req, resp);
            return;
        }

        currentUser.setPassword(newHashed);

        req.setAttribute("success", "Password changed successfully!");
        req.getRequestDispatcher("views/change-password.jsp").forward(req, resp);
    }
}

