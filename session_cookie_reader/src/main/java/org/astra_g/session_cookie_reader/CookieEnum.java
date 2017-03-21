package org.astra_g.session_cookie_reader;

public enum CookieEnum {

    // application/x-www-form-urlencoded
    X_WWW_FROM_URLENCODED("application/x-www-form-urlencoded", 0),
    // multipart/form-data
    FROM_DATA("multipart/form-data", 1);

    private String stringValue;
    private int intValue;
    CookieEnum(String toString, int value){
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
