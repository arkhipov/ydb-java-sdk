package com.yandex.ydb.core.auth;

/**
 * @author Sergey Polovko
 */
public class NopAuthProvider implements AuthProvider {

    public static final NopAuthProvider INSTANCE = new NopAuthProvider();

    private NopAuthProvider() {
    }

    @Override
    public String getToken() {
        return "";
    }

    @Override
    public void close() {
    }
}
