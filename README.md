# CookieReader
Cookie Reader of Session

-----
    repositories {
        maven {
            url 'https://dl.bintray.com/ludy87/maven/'
        }
    }

    dependencies {
        compile 'org.astra_g:session_cookie_reader:1.2.3'
    }

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


----

    Copyright [2016] [Ludy]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.