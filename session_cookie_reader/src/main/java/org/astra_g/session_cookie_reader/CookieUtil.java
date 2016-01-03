package org.astra_g.session_cookie_reader;

import android.net.Uri;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CookieUtil {

    /**
     * @deprecated
     * @param params HashMap
     * @return String
     */
    static String getQuery(HashMap<String,String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (String pair : params.keySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append((pair));
            result.append("=");
            result.append((params.get(pair)));
        }
        return result.toString();
    }

    /**
     * Thanks Pascal
     * @param params HashMap
     * @return String
     */
    static String getQueryString(HashMap<String, String> params) {
        Uri.Builder builder = new Uri.Builder();
        for (Object o : params.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            builder.appendQueryParameter(pair.getKey().toString(), pair.getValue().toString());
        }

        return builder.build().getEncodedQuery();
    }
}