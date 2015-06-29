package fr.astek.api.utils;

import com.google.common.collect.Maps;
import org.wisdom.api.cookies.Cookie;
import org.wisdom.api.cookies.Cookies;

import java.util.Map;

/**
 * Created by jmejdoub on 22/06/2015.
 */
public class PakFakeCookies  implements Cookies {
    private Map<String, Cookie> cookies = Maps.newTreeMap();

    /**
     * Adds a cookie. Except the value, others cookie's attributes are meaningless.
     *
     * @param name  the name, must not be {@literal null}
     * @param value the value
     * @return the current cookies holder
     */
    public PakFakeCookies add(String name, String value) {
        Cookie cookie = new Cookie(name, value, null, null, 3600, null, false, false);
        cookies.put(name, cookie);
        return this;
    }

    /**
     * Adds a cookie.
     *
     * @param cookie the cookie, must not be {@literal null}
     * @return the current cookies holder
     */
    public PakFakeCookies add(Cookie cookie) {
        cookies.put(cookie.name(), cookie);
        return this;
    }


    /**
     * @param name Name of the cookie to retrieve
     * @return the cookie that is associated with the given name, or null if there is no such cookie
     */
    @Override
    public Cookie get(String name) {
        return cookies.get(name);
    }
}
