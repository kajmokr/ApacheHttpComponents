package exercise;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by ohjunho on 16. 9. 27.
 */
public class Login {

    public static void main(String[] args) throws Exception {

        Login Login = new Login();

        Cookie cookie = Login.login("admin","admin");
        //cookie내용 보이기

    }

    private Cookie login(String id, String password){
        Cookie cookie = null;
        try {
            BasicCookieStore cookieStore = new BasicCookieStore();
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .build();


            HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI("http://61.40.220.6:9763/publisher/site/blocks/user/login/ajax/login.jag"))
                    .addParameter("action", "login")
                    .addParameter("username", id)
                    .addParameter("password", password)
                    .build();
            CloseableHttpResponse response2 = httpclient.execute(login);
            try {
                HttpEntity entity = response2.getEntity();

                System.out.println("Login form get: " + response2.getStatusLine());
                EntityUtils.consume(entity);

                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
//                    for (int i = 0; i < cookies.size(); i++) {
//                        System.out.println("- " + cookies.get(i).toString());
//                    }
                    System.out.println("- " + cookies.get(0).toString());
                    cookie = cookies.get(0);

                }
            } finally {
                response2.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        return cookie;
    }
} 
