package morris.com.voucher.graphql;

import com.apollographql.apollo.ApolloClient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by morris on 03/01/2019
 */

public class GraphQL {

    public static final String BASE_URL =

    //"http://192.168.43.250:2508/graphql/";
   "http://192.168.1.192:2508/graphql";
   //"http://173.82.51.162:2508/graphql";

    private static ApolloClient apolloClient;

    public static ApolloClient getApolloClient(){

//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
               // .addInterceptor(loggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();
        return apolloClient;
    }

}
