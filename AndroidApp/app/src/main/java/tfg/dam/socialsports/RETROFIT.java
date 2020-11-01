package tfg.dam.socialsports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RETROFIT {

    private APIService service;

    public RETROFIT(){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl("http://"+ IP.getIp +":8080/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(APIService.class);
    }

    public APIService getAPIService(){
        return this.service;
    }
}
