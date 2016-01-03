package org.astra_g.session_cookie_reader;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class CookieReader extends AsyncTask<String, String, String> {

    private String _cookieName;
    private String _postParam;
    private String _url;
    private boolean _debug = false;
    private CookieReaderInterfaces callback;

    public CookieReader(CookieParameterInterfaces parameterInterfaces) {
        callback = parameterInterfaces.cookieCallback();
        _url = parameterInterfaces.url();
        _postParam = CookieUtil.getQueryString(parameterInterfaces.postParameter());
        _cookieName = parameterInterfaces.cookieName();
    }

    /**
     * @deprecated
     */
    public CookieReader() {
    }

    /**
     * @param url http://
     * @return CookieReader
     * @deprecated
     */
    public CookieReader setUrl(String url) {
        if (_debug)
            Log.d("url", url);
        _url = url;
        return this;
    }

    /**
     * @param cookieName JSESSIONID
     * @return CookieReader
     * @deprecated
     */
    public CookieReader setCookieName(String cookieName) {
        if (_debug)
            Log.d("cookieName", cookieName);
        _cookieName = cookieName;
        return this;
    }

    /**
     * @param postParameter HashMap<String, String>()
     * @return CookieReader
     * @deprecated
     */
    public CookieReader setPostParameter(HashMap<String, String> postParameter) {
        if (_debug)
            for (String s : postParameter.keySet()) {
                Log.d("postParameter", s + "=" + postParameter.get(s));
            }
        _postParam = CookieUtil.getQuery(postParameter);
        return this;
    }

    /**
     * @param callback CookieReaderInterfaces
     * @return CookieReader
     * @deprecated
     */
    public CookieReader setCookieCallback(CookieReaderInterfaces callback) {
        this.callback = callback;
        return this;
    }

    @Override
    protected String doInBackground(String... params) {
        URL url;
        try {
            url = new URL(_url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setReadTimeout(7000);
            con.setConnectTimeout(7000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setFixedLengthStreamingMode(_postParam.getBytes().length);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Send
            PrintWriter out = new PrintWriter(con.getOutputStream());
            out.print(_postParam);
            out.close();

            con.connect();
            String headers;
            for (int i = 1; (headers = con.getHeaderFieldKey(i)) != null; i++) {

                if (headers.equals("Set-Cookie")) {
                    String cookie = con.getHeaderField(i);
                    cookie = cookie.substring(0, cookie.indexOf(";"));
                    String cookieName = cookie.substring(0, cookie.indexOf("="));
                    String cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());

                    if (cookieName.equalsIgnoreCase(_cookieName)) {
                        return cookieValue;
                    }
                }
            }
            BufferedReader in = null;
            if (con.getResponseCode() != 200) {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                Log.d("TAG", "!=200: " + in.readLine());
                return in.readLine();
            } else {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                Log.d("TAG", "POST request send successful: " + in.readLine());
            };


        } catch (IOException e) {
            Log.e("TAG", "Exception");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (callback != null) callback.onCookieCall(result);
    }
}