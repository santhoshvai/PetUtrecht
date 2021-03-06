package com.example.svaiyapu.petutrecht.data.Remote;

import com.example.svaiyapu.petutrecht.data.Model.RemoteResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;
import retrofit2.Call;

/**
 * Created by svaiyapu on 8/22/16.
 */
public class PetRetrofit {

    public static final String API_URL = "https://api.backendless.com/v1/data/";

    public interface PetResponse {
        @Headers({
                "application-id: 6B72376C-352B-753B-FFD8-91D5FA30AA00",
                "secret-key:  B60095CD-E517-230C-FFF5-171680B7C200",
                "application-type: REST",
                "Content-Type: application/json"
        })
        @GET("Pet?pageSize=20")
        Call<RemoteResponse> petResponse();
    }

    private static PetRetrofit INSTANCE = null;

    private static Retrofit sRetrofit = null;

    public static PetRetrofit getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PetRetrofit();
        }
        return INSTANCE;
    }

    private void buildRetrofitInstance() {
        if(sRetrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            sRetrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
    }

    public Call<RemoteResponse> getCallInstance() {
        buildRetrofitInstance();
        // Create an instance of our Pet backendless API interface.
        PetResponse petResponse = sRetrofit.create(PetResponse.class);
        // Create a call instance for looking up the json.
        Call<RemoteResponse> call = petResponse.petResponse();
        return call;
    }

    public static void main(String... args) throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our Pet backendless API interface.
        PetResponse petResponse = retrofit.create(PetResponse.class);

        // Create a call instance for looking up the json.
        Call<RemoteResponse> call = petResponse.petResponse();

        // Fetch and print a list of the contributors to the library.
        Response<RemoteResponse> response = call.execute();

        if(response.code() == 200) {
            RemoteResponse remoteResponse = response.body();
            System.out.println(remoteResponse.toString());
        } else {
            System.out.println(response.errorBody().string());
            //Log.e("ERRORBODY", response.errorBody().string());
        }

    }
}
