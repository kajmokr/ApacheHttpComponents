package exercise;

import com.sun.xml.internal.fastinfoset.util.CharArray;

import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by ohjunho on 16. 9. 27.
 */
public class API {

    private List<Cookie> cookies;

    private final static String SSL = "https://61.40.220.6:9443";
    private final static String NONE_SSL = "http://61.40.220.6:9763";

    public static void main(String[] args) throws Exception {

        API API = new API();

        API.login("admin", "admin");
        API.addAPI();
        API.getAllAPIs();
        API.getAnAPI("CalculatorTEST","1.0","admin");

//        String data = API.intToChar("Simple calculator API to perform addition, subtraction, multiplication and division.\r\n&#44228;&#49328;&#44592;");
//        System.out.println(data);
    }

    // Login
    private void login(String id, String password) {
        try {
            BasicCookieStore cookieStore = new BasicCookieStore();

            CloseableHttpClient httpclient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .build();

            HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI(NONE_SSL+"/publisher/site/blocks/user/login/ajax/login.jag"))
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
                cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                    //System.out.println("- " + cookies.get(0).toString());

                }
            } finally {
                response2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add API : 등록됨, 안에 들어갈 상세 내용 부족으로 정상적으로 만들어지진 않았음
    private void addAPI() throws Exception {

        HttpClient httpclient = createHttpClient_AcceptsUntrustedCerts();


        // cookie 로딩...
        HttpContext localContext = new BasicHttpContext();
        CookieStore cookieStore = new BasicCookieStore();
        cookieStore.addCookie(cookies.get(0));
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        HttpUriRequest addAPI = RequestBuilder.post()
                .setUri(new URI(SSL+"/publisher/site/blocks/item-add/ajax/add.jag")) // NONE_SSL 사용 가능(더 빠름)
                .addParameter("action", "addAPI")
                .addParameter("name", "CalculatorTEST")
                .addParameter("context", "/calctest")
                .addParameter("version", "1.0")
                .addParameter("visibility", "public") // <public|private|restricted>
                .addParameter("thumbUrl", "/registry/resource/_system/governance/apimgt/applicationdata/icons/admin/CalculatorAPI/1.0/icon")   // <URL of the image>
                .addParameter("description", "이게 맞을까요???")
                .addParameter("tags", "calculator,계산기,테스트")       // x,y,z
                .addParameter("resourceCount", "0")
                .addParameter("resourceMethod-0", "GET")
                .addParameter("resourceMethodAuthType-0", "Application")
                .addParameter("resourceMethodThrottlingTier-0", "Unlimited")
                .addParameter("swagger", "{}")  // ex -> {"paths" : {"/CheckPhoneNumber" : {"post" : {"x-auth-type" : "Application%20%26%20Application%20User", "x-scope" : "read_number", "x-throttling-tier" : "Unlimited", "responses" : {"200" : {}}}, "get" : {"x-auth-type" : "Application%20%26%20Application%20User", "x-throttling-tier" : "Unlimited", "responses" : {"200" : {}}, "parameters" : [{"name" : "PhoneNumber", "paramType" : "query", "required" : false, "type" : "string", "description" : "Phone Number", "in" : "query"}, {"name" : "LicenseKey", "paramType" : "query", "required" : false, "type" : "string", "description" : "License Key", "in" : "query"}]}}, "/" : {"put" : {"responses" : {"200" : {}}}, "get" : {"responses" : {"200" : {}}}}}, "swagger" : "2.0", "x-wso2-security" : {"apim" : {"x-wso2-scopes" : [{"description" : "", "name" : "read_number", "roles" : "admin", "key" : "read_number"}]}}, "info" : {"title" : "PhoneVerification", "version" : "1.0.0"}}
                .addParameter("endpoint_config","{\"production_endpoints\": {\"url\":\"https://localhost:9443/am/sample/calculator/v1/api\",\"config\":null},\"sandbox_endpoints\": {\"url\":\"https://localhost:9443/am/sample/calculator/v1/api\",\"config\":null},\"implementation_status\":\"managed\",\"endpoint_type\":\"http\"}")
                .addParameter("endpointType", "address")
                .addParameter("default_version_checked", "default_version")
                .addParameter("tiersCollection", "Unlimited")  // <Gold,Silver,Bronze,Unlimited>
                .addParameter("http_checked", "http")
                .addParameter("https_checked", "https")
                .addParameter("inSequence", "")
                .addParameter("outSequence", "")
                .addParameter("responseCache", "disabled")  // responseCache=enabled&cacheTimeout=300
                // 기타  bizOwner=<name>&bizOwnerMail=<e-mail address>&techOwner=<name>&techOwnerMail=<e-mail address>
                .build();

        CloseableHttpResponse response = (CloseableHttpResponse)httpclient.execute(addAPI, localContext);

        System.out.println("Executing request " + addAPI.getRequestLine());

        try {

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(EntityUtils.toString(response.getEntity()));

        } finally {
            response.close();
        }
    }


    // Update API


    // Get All APIs
    private void getAllAPIs() throws Exception {

        HttpClient httpclient = createHttpClient_AcceptsUntrustedCerts();


        // cookie 로딩...
        HttpContext localContext = new BasicHttpContext();
        CookieStore cookieStore = new BasicCookieStore();
        cookieStore.addCookie(cookies.get(0));
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        HttpUriRequest getAllAPIs = RequestBuilder.get()
            .setUri(new URI(SSL+"/publisher/site/blocks/listing/ajax/item-list.jag")) // NONE_SSL 사용 가능(더 빠름)
            .addParameter("action", "getAllAPIs")
            .build();

        CloseableHttpResponse response = (CloseableHttpResponse)httpclient.execute(getAllAPIs, localContext);

        System.out.println("Executing request " + getAllAPIs.getRequestLine());

        try {

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(EntityUtils.toString(response.getEntity()));

        } finally {
            response.close();
        }
    }

    // Get an API
    /*
    SELECT API_PROVIDER,API_NAME,API_VERSION FROM WSO2AM_DB.AM_API;
    쿼리 이용 하여 개별 API 내용 확보
     */
    private void getAnAPI(String name, String version, String provider) throws Exception {

        HttpClient httpclient = createHttpClient_AcceptsUntrustedCerts();


        // cookie 로딩...
        HttpContext localContext = new BasicHttpContext();
        CookieStore cookieStore = new BasicCookieStore();
        cookieStore.addCookie(cookies.get(0));
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        HttpUriRequest getAllAPIs = RequestBuilder.get()
                .setUri(new URI(SSL+"/publisher/site/blocks/listing/ajax/item-list.jag")) // NONE_SSL 사용 가능(더 빠름)
                .addParameter("action", "getAPI")
                .addParameter("name", name)
                .addParameter("version", version)
                .addParameter("provider", provider)
                .build();

        CloseableHttpResponse response = (CloseableHttpResponse)httpclient.execute(getAllAPIs, localContext);

        System.out.println("Executing request " + getAllAPIs.getRequestLine());

        try {

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(intToChar(EntityUtils.toString(response.getEntity())));

        } finally {
            response.close();
        }
    }

    // common class
    // SSL 적용/비적용 모두 처리 가능한 class
    private HttpClient createHttpClient_AcceptsUntrustedCerts() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        HttpClientBuilder b = HttpClientBuilder.create();

        // setup a Trust Strategy that allows all certificates.
        //
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();
        b.setSslcontext(sslContext);

        // don't check Hostnames, either.
        //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        // here's the special part:
        //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
        //      -- and create a Registry, to register it.
        //
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        // now, we create connection-manager using our Registry.
        //      -- allows multi-threaded use
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        b.setConnectionManager(connMgr);

        // finally, build the HttpClient;
        //      -- done!

        HttpClient client = b.build();
        return client;

    }

    // &#숫자; -> Char 변환
    public String intToChar(String src) {

        String result = "";

        Pattern p1 = Pattern.compile("&#(.*?);");
        Matcher matcher = p1.matcher(src);

        while(matcher.find()) {

            String tgt = String.valueOf((char) Integer.parseInt(matcher.group().replaceAll("&#", "").replaceAll(";", "")));

            result = matcher.replaceFirst(tgt);

            // 이동한 matcher의 시퀀스를 리셋
            matcher.reset();

            p1 = Pattern.compile("&#(.*?);");
            matcher = p1.matcher(result);
        }
        return result;
    }

}

