package org.astra_g.session_cookie_reader.utilities;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class CookieUtil {

    /**
     * Thanks Pascal
     *
     * @param params HashMap
     * @return String
     */
    public static String getQueryString(HashMap<String, String> params) {
        Uri.Builder builder = new Uri.Builder();
        for (Object o : params.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            builder.appendQueryParameter(pair.getKey().toString(), pair.getValue().toString());
        }
        return builder.build().getEncodedQuery();
    }
}