package org.astra_g.session_cookie_reader;

import java.util.HashMap;

public interface CookieParameterInterfaces {
    String url();

    String cookieName();

    HashMap<String, String> postParameter();

    CookieReaderInterfaces cookieCallback();
}