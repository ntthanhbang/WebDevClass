package controller;

import java.io.IOException;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Get remember token BEFORE invalidating session
        String token = null;
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        
        // Invalidate session
        if (session != null) {
            session.invalidate();
        }
        
        // Delete remember token from database and cookie
        if (token != null) {
            UserDAO userDAO = new UserDAO();
            userDAO.deleteRememberToken(token);
            
            // Delete cookie
            Cookie deleteCookie = new Cookie("remember_token", "");
            deleteCookie.setMaxAge(0);
            deleteCookie.setPath("/");
            response.addCookie(deleteCookie);
        }
        
        // Redirect to login with message
        request.getSession().setAttribute("message", "You have been logged out successfully");
        response.sendRedirect("login");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
