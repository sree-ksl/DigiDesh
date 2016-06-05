package com.mercury.app.activity;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.auth.AuthScope;
import cz.msebera.android.httpclient.auth.UsernamePasswordCredentials;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.conn.SingleClientConnManager;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpParams;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class ConnectCustomerToFlow {

    public static void main(String[] args) throws Exception {

        SSLContext sslContext = SSLContext.getInstance("SSL");

        // set up a TrustManager that trusts everything
        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                System.out.println("getAcceptedIssuers =============");
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                                           String authType) {
                System.out.println("checkClientTrusted =============");
            }

            public void checkServerTrusted(X509Certificate[] certs,
                                           String authType) {
                System.out.println("checkServerTrusted =============");
            }

            public boolean isClientTrusted(X509Certificate[] arg0) {
                return false;
            }

            public boolean isServerTrusted(X509Certificate[] arg0) {
                return false;
            }
        } }, new SecureRandom());

        SSLSocketFactory sf = new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme httpsScheme = new Scheme("https", sf, 443);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(httpsScheme);

        HttpParams params = new BasicHttpParams();
        ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);

        DefaultHttpClient client = new DefaultHttpClient(cm, params);

        //Replace "<Exotel SID>" and "<Exotel Token>" with your SID and Token
        client.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials("changeterra", "a75b4d07876ce798e594fb428ed5bdd6c628c51b")
        );
        HttpPost post = new HttpPost("https://twilix.exotel.in/v1/Accounts/<Exotel SID>/Calls/connect");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        /*
	*Replace the text enclosed in < > with your desired values
	*The options for CallType are "trans" for transactional call and "promo" for promotional call
        */
        nameValuePairs.add(new BasicNameValuePair("From", "09591200619"));
        nameValuePairs.add(new BasicNameValuePair("To", "08039510969"));
        nameValuePairs.add(new BasicNameValuePair("Url", "http://my.exotel.in/exoml/start/53757"));
        nameValuePairs.add(new BasicNameValuePair("CallType", "trans"));
        nameValuePairs.add(new BasicNameValuePair("StatusCallback", "http://4911489a.ngrok.io"));
	/* Optional params
	nameValuePairs.add(new BasicNameValuePair("TimeLimit", "<time-in-seconds>"));
        nameValuePairs.add(new BasicNameValuePair("TimeOut", "<time-in-seconds>"));

	*/

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        ResponseHandler<String> responseHandler=new BasicResponseHandler();
        String response = client.execute(post, responseHandler);
        System.out.println(response);
    }
}
