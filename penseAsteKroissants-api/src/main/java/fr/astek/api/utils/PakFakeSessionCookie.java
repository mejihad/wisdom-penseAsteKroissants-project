package fr.astek.api.utils;

import org.wisdom.api.cookies.SessionCookie;
import org.wisdom.api.http.Context;
import org.wisdom.api.http.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmejdoub on 22/06/2015.
 */
public class PakFakeSessionCookie implements SessionCookie {
    private final Map<String, String> data = new HashMap<>();

    @Override
    public void init(Context context) {
        // Does nothing.
    }

    @Override
    public String getId() {
        return Integer.toString(this.hashCode());
    }

    @Override
    public Map<String, String> getData() {
        return data;
    }

    @Override
    public void save(Context context, Result result) {
        // Does nothing.
    }

    @Override
    public void put(String key, String value) {
        data.put(key, value);
    }

    @Override
    public String get(String key) {
        return data.get(key);
    }

    @Override
    public String remove(String key) {
        return data.remove(key);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }
}
