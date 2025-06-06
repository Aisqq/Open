package com.me.utils;

import jakarta.servlet.http.Cookie;
public class CookieUtil {
    static public Cookie createJwtCookie(String jwtToken) {
        Cookie newCookie = new Cookie("jwtToken", jwtToken);
        newCookie.setHttpOnly(false);
        newCookie.setSecure(false);
        newCookie.setPath("/");
        newCookie.setMaxAge(3600);
        return newCookie;
    }

}
