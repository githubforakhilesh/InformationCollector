package com.example.informationcollector.SigletonClass;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class BaseUrl {
        public static final String BASE_URL = "https://www.experientialetc.com/hrapp/api/";
        private static Retrofit retrofit = null;

        public static Retrofit getClient() {
            if (retrofit == null) {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(getUnsafeOkHttpClient().build())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit;
        }

        public static OkHttpClient.Builder getUnsafeOkHttpClient() {
            long CONNECTION_TIMEOUT = 100;
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
                builder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
                builder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                builder.addInterceptor(logging);
                return builder;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
}
