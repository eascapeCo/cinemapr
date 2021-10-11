package com.eascapeco.cinemapr.bo.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 클래스명: <code>CookieUtils</code>
 *
 * <pre>
 *  cookie를 처리하는 유틸
 * </pre>
 *
 * @author JaeHan-Kim
 * @date 2019. 11. 19.
 *
 */
public class CookieUtils {

    /**
     * 쿠키 생성
     *
     * @param name
     * @param value
     * @param maxAge
     * @param response
     */
    public static void setCookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    /**
     * 쿠키 생성
     *
     * @param name
     * @param value
     * @param response
     */
    public static void setCookie(String name, String value, HttpServletResponse response) {
        setCookie(name, value, -1, response);
    }

    public static String getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String value = null;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (StringUtils.equals(name, c.getName())) {
                    value = c.getValue();
                    break;
                }
            }
        }

        return value;
    }

    /**
     * key에 해당되는 쿠키 삭제
     *
     * @param keyName
     * @param request
     * @param response
     */
    public static void expireCookie(String keyName, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookies[] = request.getCookies();

        if(cookies != null){
            for (Cookie thisCookie : cookies) {
                String _cName = thisCookie.getName();
                if (StringUtils.equals(_cName, keyName)) {
                    setCookie(_cName, "", 0, response);
                }
            }
        }
    }
}
