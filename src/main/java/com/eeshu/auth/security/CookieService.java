package com.eeshu.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CookieService {
    private final  String refreshTokenCookieName;
    private final boolean cookieHttpOnly;
    private final boolean cookieSecure;
    private final String cookieDomain;
    private final String sameSite;

    public CookieService(
          @Value("${security.jwt.access-token-cookie-name}") String refreshTokenCookieName,
          @Value("${security.jwt.cookie-http-only}")    boolean cookieHttpOnly,
          @Value("${security.jwt.cookie-secure}")  boolean cookieSecure,
          @Value("${security.jwt.cookie-domain}")  String cookieDomain,
          @Value("${security.jwt.cookie-same-site}")  String sameSite) {
        this.refreshTokenCookieName = refreshTokenCookieName;
        this.cookieHttpOnly = cookieHttpOnly;
        this.cookieSecure = cookieSecure;
        this.cookieDomain = cookieDomain;
        this.sameSite = sameSite;
    }
//create method to attach cookie to response
    public void attachRefreshCookie(HttpServletResponse response,String value,int maxAge){
        //create cookie
    var responseCookieBuilder =ResponseCookie.from(refreshTokenCookieName,value)
            .httpOnly(cookieHttpOnly)
            .secure(cookieSecure)
            .path("/")
            .maxAge(maxAge)
            .domain(cookieDomain)
            .sameSite(sameSite);

    if(cookieDomain!=null && !cookieDomain.isBlank()){
        responseCookieBuilder.domain(cookieDomain);
    }
        ResponseCookie responseCookie = responseCookieBuilder.build();
    response.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());

    }
    public void clearRefreshCookie(HttpServletResponse response){
        var responseCookieBuilder =ResponseCookie.from(refreshTokenCookieName, "")
                .httpOnly(cookieHttpOnly)
                .secure(cookieSecure)
                .path("/")
                .maxAge(0)

                .sameSite(sameSite);

        if(cookieDomain!=null && !cookieDomain.isBlank()){
            responseCookieBuilder.domain(cookieDomain);
        }
        ResponseCookie responseCookie = responseCookieBuilder.build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
    // MISSING HELPER: Request se cookie read karne ke liye
    public String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        return java.util.Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .map(jakarta.servlet.http.Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
    public void addNoStoreHeaders(HttpServletResponse response){
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }
}
