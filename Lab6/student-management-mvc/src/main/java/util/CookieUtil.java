package util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    /**
     * Create and add cookie to response
     */
    public static void createCookie(HttpServletResponse response, String name, String value, int maxAge) {

        if (response == null || name == null) return;

        Cookie cookie = new Cookie(name, value == null ? "" : value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }

    /**
     * Get cookie value by name
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request == null || name == null) return null;

        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }

        return null;
    }

    /**
     * Check if cookie exists
     */
    public static boolean hasCookie(HttpServletRequest request, String name) {
        return getCookieValue(request, name) != null;
    }

    /**
     * Delete cookie by setting max age = 0
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        if (response == null || name == null) return;

        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }

    /**
     * Update cookie (simply re-create it)
     */
    public static void updateCookie(HttpServletResponse response, String name, String newValue, int maxAge) {
        createCookie(response, name, newValue, maxAge);
    }
}
