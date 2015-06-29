package fr.astek.api.utils;

import org.wisdom.api.cookies.FlashCookie;
import org.wisdom.api.http.Context;
import org.wisdom.api.http.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmejdoub on 22/06/2015.
 */
public class PakFakeFlashCookie implements FlashCookie {
    private final Map<String, String> data = new HashMap<>();

    @Override
    public void init(Context context) {
        //Unused
    }

    @Override
    public void save(Context context, Result result) {
        //Unused
    }

    @Override
    public void put(String key, String value) {
        data.put(key, value);
    }

    @Override
    public void put(String key, Object value) {
        data.put(key, value.toString());
    }

    @Override
    public void error(String value) {
        data.put(FLASH_ERROR, value);
    }

    @Override
    public void success(String value) {
        data.put(FLASH_SUCCESS, value);
    }

    @Override
    public void discard(String key) {
        data.remove(key);
    }

    @Override
    public void discard() {
        data.clear();
    }

    @Override
    public void keep(String key) {
        //Unused
    }

    @Override
    public void keep() {
        //Unused
    }

    @Override
    public String get(String key) {
        return data.get(key);
    }

    @Override
    public boolean remove(String key) {
        return data.remove(key) != null;
    }

    @Override
    public void clearCurrentFlashCookieData() {
        data.clear();
    }

    @Override
    public boolean contains(String key) {
        return data.containsKey(key);
    }

    @Override
    public Map<String, String> getCurrentFlashCookieData() {
        return data;
    }

    @Override
    public Map<String, String> getOutgoingFlashCookieData() {
        return data;
    }
}
