package rs.direktnoizbaste.dizb.settings;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.direktnoizbaste.dizb.app.AppConfig;

/**
 * Created by brajan on 10/5/2017.
 */

public class RetroCreator {
    private static final String API_URL = AppConfig.URL_BASE;

    public static Retrofit getService() {

        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
