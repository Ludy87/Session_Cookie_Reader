package org.astra_g.session_cookie_reader;

import android.os.AsyncTask;
import android.util.Log;

import org.astra_g.session_cookie_reader.utilities.CookieUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class CookieReader extends AsyncTask<String, String, String> {

    private String _cookieName;
    private String _postParam;
    private String _url;
    private boolean _debug = false;
    private CookieReaderInterfaces callback;
    private String _contentType = CookieEnum.X_WWW_FROM_URLENCODED.toString();
    private int _timeout = 7000;
    private String TAG = CookieReader.class.getSimpleName();

    /**
     * @param parameterInterfaces Interface of CALLBACK, URL, POST PARAMETER, COOKIE NAME
     * @param contentType         custom Content-Type | default application/x-www-form-urlencoded
     * @param debug               default false | true enable debug logs
     */
    public CookieReader(CookieParameterInterfaces parameterInterfaces, String contentType, boolean debug) {
        callback = parameterInterfaces.cookieCallback();
        _url = parameterInterfaces.url();
        _postParam = CookieUtil.getQueryString(parameterInterfaces.postParameter());
        _cookieName = parameterInterfaces.cookieName();
        _contentType = contentType;
        _debug = debug;
        if (_debug) {
            Log.d(TAG + " - url", _url);
            Log.d(TAG + " - CookieName", _cookieName);
            Log.d(TAG + " - Content-Type", _contentType);
            if (_postParam != null)
                Log.d(TAG + " - postParameter", _postParam);
        }
    }

    /**
     * @param parameterInterfaces Interface of CALLBACK, URL, POST PARAMETER, COOKIE NAME
     * @param contentType         default application/x-www-form-urlencoded
     * @param debug               default false | true enable debug logs
     */
    public CookieReader(CookieParameterInterfaces parameterInterfaces, CookieEnum contentType, boolean debug) {
        this(parameterInterfaces, contentType.toString(), debug);
    }

    /**
     * @param parameterInterfaces Interface of CALLBACK, URL, POST PARAMETER, COOKIE NAME
     * @param contentType         default application/x-www-form-urlencoded
     */
    public CookieReader(CookieParameterInterfaces parameterInterfaces, CookieEnum contentType) {
        this(parameterInterfaces, contentType, false);
    }

    /**
     * @param parameterInterfaces Interface of CALLBACK, URL, POST PARAMETER, COOKIE NAME
     * @param debug               default false | true enable debug logs
     */
    public CookieReader(CookieParameterInterfaces parameterInterfaces, boolean debug) {
        this(parameterInterfaces, CookieEnum.X_WWW_FROM_URLENCODED, debug);
    }

    /**
     * @param parameterInterfaces Interface of CALLBACK, URL, POST PARAMETER, COOKIE NAME
     */
    public CookieReader(CookieParameterInterfaces parameterInterfaces) {
        this(parameterInterfaces, CookieEnum.X_WWW_FROM_URLENCODED, false);
    }

    /**
     * @param timeout default 7000 (7sec)
     * @return CookieReader
     */
    public CookieReader setTimeout(int timeout) {
        _timeout = timeout;
        if (_debug) {
            Log.d(TAG + " - TimeOut", String.valueOf(_timeout));
        }
        return this;
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
     * @param postParameter HashMap
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
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setReadTimeout(_timeout);
            con.setConnectTimeout(_timeout);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            if (_postParam != null)
                con.setFixedLengthStreamingMode(_postParam.getBytes().length);
            else
                Log.d(TAG + " - postParam", "Empty");
            con.setRequestProperty("Content-Type", _contentType);
            // Send
            PrintWriter out = new PrintWriter(con.getOutputStream());
            if (_postParam != null)
                out.print(_postParam);
            else
                Log.d(TAG + " - postParam", "Empty");
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
            }
        } catch (IOException e) {
            Log.e("TAG", "Exception");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null)
            if (callback != null) callback.onCookieCall(result);
            else
                Log.e(TAG, "CookieResult NULL");
    }
}