package tfg.dam.socialsports.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tfg.dam.socialsports.retrofit.APIService;

public class RetrofitConnection {

    private APIService service;
    public final String BASE_URL = "http://10.0.2.2:8080/api/";
    /*public final String BASE_URL = "https://socialsports2-0.herokuapp.com/api/";*/

    public RetrofitConnection(){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(APIService.class);
    }

    public APIService getAPIService(){
        return this.service;
    }
}
