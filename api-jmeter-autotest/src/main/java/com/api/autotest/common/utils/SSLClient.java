package com.api.autotest.common.utils;

/**
 * Created by fanseasn on 2020/11/30.
 */
/*
 @author Season
 @DESCRIPTION 
 @create 2020/11/30
*/
import org.apache.http.client.CookieStore;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
//用于进行Https请求的HttpClient
public class SSLClient extends DefaultHttpClient{
    public SSLClient() throws Exception{
        super();
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = this.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
    }

    public static CloseableHttpClient createSSLClientDefault(CookieStore cookieStore) throws Exception {
//        SSLContext sslContext;
        try {
            SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
                    SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
                    NoopHostnameVerifier.INSTANCE);
            return HttpClients.custom().setSSLSocketFactory(scsf).build();
//            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//                //信任所有
//                @Override
//                public boolean isTrusted(X509Certificate[] xcs, String string){
//                    return true;
//                }
//            }).build();
//
//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
//            if(cookieStore == null){
//                return HttpClients.custom().setSSLSocketFactory(sslsf).build();
//            }else{
//                return HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCookieStore(cookieStore).build();
//            }
        }
        catch (Exception ex) {
            throw new Exception("Https 创建请求失败"+ex.getMessage());
        }
        //return HttpClients.createDefault();
    }
}