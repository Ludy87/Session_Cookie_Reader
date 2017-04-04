# CookieReader for Android

Cookie Reader of Session

[![Download](https://api.bintray.com/packages/ludy87/maven/SessionCookieReader/images/download.svg)](https://bintray.com/ludy87/maven/SessionCookieReader/_latestVersion)



-----
add to App *build.gradle*

    repositories {
        jcenter()
    }

    dependencies {
        compile 'org.astra_g:session_cookie_reader:1.3.+'
    }

-----

    setTimeout(int timeout) set of:
        - setReadTimeout
        - setConnectTimeout

    new CookieReader(this).setTimeout(7000);
-----

    /**
     *
     * @param parameterInterfaces Interface of CALLBACK, URL, POST PARAMETER, COOKIE NAME
     * @param contentType Enum | String
     * @param debug default false
     */
    CookieReader(CookieParameterInterfaces parameterInterfaces, ContentType);
    CookieReaderSSL(CookieParameterInterfaces parameterInterfaces, ContentType);

    /**
     *
     * @param parameterInterfaces Interface of CALLBACK, URL, POST PARAMETER, COOKIE NAME
     * @param contentType Enum or String
     * @param debug boolean
     */
    CookieReader(CookieParameterInterfaces, ContentType, Debug);
    CookieReaderSSL(CookieParameterInterfaces, ContentType, Debug);

    /**
     *
     * @param parameterInterfaces Interface of CALLBACK, URL, POST PARAMETER, COOKIE NAME
     * @param contentType default application/x-www-form-urlencoded
     * @param debug boolean
     */
    CookieReader(CookieParameterInterfaces, Debug);
    CookieReaderSSL(CookieParameterInterfaces, Debug);

    /**
     *
     * @param parameterInterfaces Interface of CALLBACK, URL, POST PARAMETER, COOKIE NAME
     * @param contentType default application/x-www-form-urlencoded
     * @param debug default false
     */
    CookieReader(CookieParameterInterfaces);
    CookieReaderSSL(CookieParameterInterfaces);

-----

    public class Test extends Activity implements CookieParameterInterfaces {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            CookieReader cookieReader = new CookieReader(this);
            cookieReader.execute();
        }

        @Override
        public String url() {
            return "https://mm.web.de/login";
        }

        @Override
        public String cookieName() {
            return "JSESSIONID";
        }

        @Override
        public HashMap<String, String> postParameter() {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("username", "USERNAME");
            param.put("password", "PASSWORD");
            param.put("login", "Login");
            return param;
        }

        @Override
        public CookieReaderInterfaces cookieCallback() {
            return new CookieReaderInterfaces() {
                @Override
                public void onCookieCall(String cookieValue) {
                    Log.d("Cookie value", cookieValue);
                }
            };
        }
    }

License
====================

    Copyright [2016-2017] [Ludy Astra-Germany]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.