package tfg.dam.socialsports.retrofit;

import java.util.ArrayList;
import java.util.HashMap;

import tfg.dam.socialsports.model.Event;
import tfg.dam.socialsports.model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @POST("auth/signup")
    Call<User> signup(@Body HashMap<String, String> credentials);

    @POST("auth/signin")
    Call<Object> signin(@Body HashMap<String, String> credentials);

    @POST("event/create")
    Call<ResponseBody> createEvent(@Header("Authorization") String authHeader, @Body Event event);

    /************************USUARIOS************************/

    @FormUrlEncoded
    @PUT("user/edit/firstname")
    Call<ResponseBody> editFirstName(@Header("Authorization") String authHeader, @Field("firstName") String firstName);

    @FormUrlEncoded
    @PUT("user/edit/lastname")
    Call<ResponseBody> editLastName(@Header("Authorization") String authHeader, @Field("lastName") String lastName);

    @FormUrlEncoded
    @PUT("user/edit/address")
    Call<ResponseBody> editAddress(@Header("Authorization") String authHeader, @Field("address") String address);

    @FormUrlEncoded
    @PUT("user/edit/gender")
    Call<ResponseBody> editGender(@Header("Authorization") String authHeader, @Field("gender") String gender);

    @FormUrlEncoded
    @PUT("user/edit/birthday")
    Call<ResponseBody> editBirthday(@Header("Authorization") String authHeader, @Field("birthday") String birthday);

    @FormUrlEncoded
    @PUT("user/edit/password")
    Call<ResponseBody> putPassword(@Header("Authorization") String authHeader, @Field("password") String password);

    @DELETE("user/delete")
    Call<ResponseBody> borrarUsuario(@Header("Authorization") String authHeader);

    @GET("user/email")
    Call<ResponseBody> getMyEmail(@Header("Authorization") String authHeader);

    /** FRIENDS **/

    @GET("friend/list")
    Call<ArrayList<User>> friendList(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("friend/sendrequest")
    Call<ResponseBody> sendFriendRequest(@Header("Authorization") String authHeader, @Field("id") long id);

    @FormUrlEncoded
    @POST("friend/accept")
    Call<ResponseBody> acceptFriend(@Header("Authorization") String authHeader, @Field("id") long id);

    @FormUrlEncoded
    @POST("friend/deny")
    Call<ResponseBody> denyFriend(@Header("Authorization") String authHeader, @Field("id") long id);

    @DELETE("friend/delete/{id}")
    Call<ResponseBody> deleteFriend(@Header("Authorization") String authHeader, @Path("id") long id);

    @GET("friend/requests/received")
    Call<ArrayList<User>> requestsReceived(@Header("Authorization") String authHeader);

    /************************EVENTOS************************/

    @GET("event/list")
    Call<ArrayList<Event>> listEvents(@Header("Authorization") String authHeader);

    @GET("user/events/joined/notfinished")
    Call<ArrayList<Event>> myEventsJoined(@Header("Authorization") String authHeader);

    @GET("user/events/joined/finished")
    Call<ArrayList<Event>> myEventsJoinedFinished(@Header("Authorization") String authHeader);

    @GET("event/search")
    Call<ArrayList<Event>> searchEvents(@Header("Authorization") String authHeader,
                                        @Query("sport") String sport, @Query("startDate") String startDate,
                                        @Query("time") String time, @Query("address") String address, @Query("reputation") float reputation);

    @FormUrlEncoded
    @PUT("event/edit/address")
    Call<ResponseBody> editAddressEvent(@Header("Authorization") String authHeader, @Field("id") Long id, @Field("address") String address);

    @FormUrlEncoded
    @PUT("event/edit/startdate")
    Call<ResponseBody> editStartDateEvent(@Header("Authorization") String authHeader, @Field("id") Long id, @Field("startDate") String startDate);

    @FormUrlEncoded
    @PUT("event/edit/time")
    Call<ResponseBody> editTimeEvent(@Header("Authorization") String authHeader, @Field("id") Long id, @Field("time") String time);

    @FormUrlEncoded
    @PUT("event/edit/maxparticipants")
    Call<ResponseBody> editMaxParticipantsEvent(@Header("Authorization") String authHeader, @Field("id") Long id, @Field("maxParticipants") int maxParticipants);

    @FormUrlEncoded
    @PUT("event/finish")
    Call<ResponseBody> finishEvent(@Header("Authorization") String authHeader, @Field("id") Long id);

    /**TODO**/
    @FormUrlEncoded
    @PUT("eventos/actualizar/coste")
    Call<ResponseBody> actualizarCoste(@Header("Authorization") String authHeader, @Field("idEvento") Long idEvento, @Field("coste") float coste);

    /**TODO**/
    @FormUrlEncoded
    @PUT("eventos/actualizar/precio")
    Call<ResponseBody> actualizarPrecio(@Header("Authorization") String authHeader, @Field("idEvento") String idEvento, @Field("precio") float precio);

    @FormUrlEncoded
    @PUT("event/edit/comment")
    Call<ResponseBody> editComment(@Header("Authorization") String authHeader, @Field("id") Long id, @Field("comment") String comment);

    @FormUrlEncoded
    @PUT("event/edit/minage")
    Call<ResponseBody> editMinAge(@Header("Authorization") String authHeader, @Field("id") Long id, @Field("minAge") int minAge);

    @FormUrlEncoded
    @PUT("event/edit/maxage")
    Call<ResponseBody> editMaxAge(@Header("Authorization") String authHeader, @Field("id") Long id, @Field("maxAge") int maxAge);

    /**TODO**/
    @FormUrlEncoded
    @PUT("event/edit/gender")
    Call<ResponseBody> editGender(@Header("Authorization") String authHeader, @Field("id") Long id, @Field("gender") String gender);

    /**TODO**/
    @FormUrlEncoded
    @PUT("eventos/actualizar/reputacion")
    Call<ResponseBody> actualizarReputacion(@Header("Authorization") String authHeader, @Field("idEvento") Long idEvento, @Field("reputacion") float reputacion);

    @DELETE("event/removeparticipant/{idEvent}/{idUser}")
    Call<ResponseBody> removeParticipant(@Header("Authorization") String authHeader, @Path("idEvent") Long idEvent, @Path("idUser") Long idUser);

    @FormUrlEncoded
    @POST("event/accept")
    Call<ResponseBody> acceptApplicantRequest(@Header("Authorization") String authHeader, @Field("idEvent") long idEvent, @Field("idUser") long idUser);

    @FormUrlEncoded
    @POST("event/deny")
    Call<ResponseBody> denyApplicantRequest(@Header("Authorization") String authHeader, @Field("idEvent") Long idEvent, @Field("idUser") Long idUser);

    @FormUrlEncoded
    @POST("event/join")
    Call<ResponseBody> sendRequestToJoinEvent(@Header("Authorization") String authHeader, @Field("id") Long id);

    /**TODO**/
    @GET("eventos/hasidopuntuado/{idevento}/{email}")
    Call<Boolean> getHaSidoPuntuado(@Header("Authorization") String authHeader, @Path("idevento") Long idevento, @Path("email") String email);

    @DELETE("event/delete/{id}")
    Call<ResponseBody> deleteEvent(@Header("Authorization") String authHeader, @Path("id") Long id);

    @FormUrlEncoded
    @POST("rate/participant")
    Call<ResponseBody> rateParticipant(@Header("Authorization") String authHeader, @Field("idParticipant") long idParticipant, @Field("idEvent") long idEvent, @Field("score") float score);

    @FormUrlEncoded
    @POST("rate/organizer")
    Call<ResponseBody> rateOrganizer(@Header("Authorization") String authHeader, @Field("idOrganizer") long idOrganizer, @Field("idEvent") long idEvent, @Field("score") float score);

    @Multipart
    @POST("images/upload")
    Call<ResponseBody> uploadImage(@Header("Authorization") String authHeader, @Part MultipartBody.Part filePart, @Part("file") RequestBody name);
}
