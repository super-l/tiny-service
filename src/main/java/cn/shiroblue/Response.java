package cn.shiroblue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * <p>
 * ======================
 * by WhiteBlue
 * on 15/10/29
 */
public class Response {
    private static final Logger LOG = LoggerFactory.getLogger(Response.class);

    private HttpServletResponse response;
    private String body;

    //For wrapper
    protected Response() {
    }

    public Response(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Sets the status code for the
     *
     * @param statusCode the status code
     */
    public void status(int statusCode) {
        response.setStatus(statusCode);
    }

    /**
     * Sets the content type for the response
     *
     * @param contentType the content type
     */
    public void type(String contentType) {
        response.setContentType(contentType);
    }

    /**
     * Sets the body
     *
     * @param body the body
     */
    public void body(String body) {
        this.body = body;
    }

    /**
     * returns the body
     *
     * @return the body
     */
    public String body() {
        return this.body;
    }

    /**
     * @return the raw response object handed in by Jetty
     */
    public HttpServletResponse raw() {
        return response;
    }

    /**
     * 发送重定向
     *
     * @param location Where to redirect
     */
    public void redirect(String location) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Redirecting ({} {} to {}", "Found", HttpServletResponse.SC_FOUND, location);
        }
        try {
            response.sendRedirect(location);
        } catch (IOException ioException) {
            LOG.warn("Redirect failure", ioException);
        }
    }

    /**
     * 发送重定向(带状态码)
     *
     * @param location       Where to redirect permanently
     * @param httpStatusCode the http status code
     */
    public void redirect(String location, int httpStatusCode) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Redirecting ({} to {}", httpStatusCode, location);
        }
        response.setStatus(httpStatusCode);
        response.setHeader("Location", location);
        response.setHeader("Connection", "close");
        try {
            response.sendError(httpStatusCode);
        } catch (IOException e) {
            LOG.warn("Exception when trying to redirect permanently", e);
        }
    }

    /**
     * Adds/Sets a response header
     *
     * @param header the header
     * @param value  the value
     */
    public void header(String header, String value) {
        response.addHeader(header, value);
    }

    /**
     * 设置cookie
     *
     * @param name  name of the cookie
     * @param value value of the cookie
     */
    public void cookie(String name, String value) {
        cookie(name, value, -1, false);
    }

    /**
     * 设置cookie
     *
     * @param name   name of the cookie
     * @param value  value of the cookie
     * @param maxAge max age of the cookie in seconds (negative for the not persistent cookie,
     *               zero - deletes the cookie)
     */
    public void cookie(String name, String value, int maxAge) {
        cookie(name, value, maxAge, false);
    }

    /**
     * 设置cookie
     *
     * @param name    name of the cookie
     * @param value   value of the cookie
     * @param maxAge  max age of the cookie in seconds (negative for the not persistent cookie, zero - deletes the cookie)
     * @param secured if true : cookie will be secured
     */
    public void cookie(String name, String value, int maxAge, boolean secured) {
        cookie(name, value, maxAge, secured, false);
    }


    /**
     * 设置cookie
     *
     * @param name     name of the cookie
     * @param value    value of the cookie
     * @param maxAge   max age of the cookie in seconds (negative for the not persistent cookie, zero - deletes the cookie)
     * @param secured  if true : cookie will be secured
     * @param httpOnly if true: cookie will be marked as http only
     */
    public void cookie(String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        cookie("", name, value, maxAge, secured, httpOnly);
    }


    /**
     * 设置cookie
     *
     * @param path    path of the cookie
     * @param name    name of the cookie
     * @param value   value of the cookie
     * @param maxAge  max age of the cookie in seconds (negative for the not persistent cookie, zero - deletes the cookie)
     * @param secured if true : cookie will be secured
     */
    public void cookie(String path, String name, String value, int maxAge, boolean secured) {
        cookie(path, name, value, maxAge, secured, false);
    }

    /**
     * 设置cookie
     *
     * @param path     path of the cookie
     * @param name     name of the cookie
     * @param value    value of the cookie
     * @param maxAge   max age of the cookie in seconds (negative for the not persistent cookie, zero - deletes the cookie)
     * @param secured  if true : cookie will be secured
     * @param httpOnly if true: cookie will be marked as http only
     */
    public void cookie(String path, String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secured);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    /**
     * Removes the cookie.
     *
     * @param name name of the cookie
     */
    public void removeCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}