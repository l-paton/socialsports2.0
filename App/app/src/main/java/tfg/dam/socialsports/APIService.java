package tfg.dam.socialsports;

import java.util.ArrayList;
import java.util.HashMap;

import tfg.dam.socialsports.Clases.Event;
import tfg.dam.socialsports.Clases.EventScore;
import tfg.dam.socialsports.Clases.UserScore;
import tfg.dam.socialsports.Clases.User;
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

    @FormUrlEncoded
    @POST("auth/singup")
    Call<User> signup(@Body HashMap<String, String> credentials);

    @POST("auth/signin")
    Call<Object> signin(@Body HashMap<String, String> credentials);

    @POST("event/create")
    Call<ResponseBody> createEvent(@Header("Authorization") String authHeader, @Body Event event);

    /************************USUARIOS************************/

    @FormUrlEncoded
    @PUT("user/edit/firstname")
    Call<ResponseBody> editFirstName(@Header("Authorization") String authHeader, @Field("firstname") String firstname);

    @FormUrlEncoded
    @PUT("user/edit/lastname")
    Call<ResponseBody> editLastName(@Header("Authorization") String authHeader, @Field("lastname") String lastName);

    @FormUrlEncoded
    @PUT("edit/address")
    Call<ResponseBody> editAddress(@Header("Authorization") String authHeader, @Field("address") String address);

    @FormUrlEncoded
    @PUT("edit/gender")
    Call<ResponseBody> editGender(@Header("Authorization") String authHeader, @Field("gender") String gender);

    @FormUrlEncoded
    @PUT("perfil/nacimiento")
    Call<ResponseBody> putFechaNacimiento(@Header("Authorization") String authHeader, @Field("correo") String correo, @Field("fecha") String fecha);

    @FormUrlEncoded
    @PUT("perfil/password")
    Call<ResponseBody> putPassword(@Header("Authorization") String authHeader, @Field("correo") String correo, @Field("password") String password);

    @DELETE("user/delete")
    Call<ResponseBody> borrarUsuario(@Header("Authorization") String authHeader);

    @GET("user/friends")
    Call<ArrayList<User>> listaAmigos(@Header("Authorization") String authHeader);

    @GET("perfil/bloqueados/{correo}")
    Call<ArrayList<User>> listaBloqueados(@Header("Authorization") String authHeader, @Path("correo") String correo);

    @POST("add/friend/{id}")
    Call<ResponseBody> addFriend(@Header("Authorization") String authHeader, @Path("id") long id);

    @DELETE("delete/friend/{id}")
    Call<ResponseBody> deleteFriend(@Header("Authorization") String authHeader, @Path("id") long id);

    @POST("perfil/bloquearusuario/{correo}/{correoBloqueado}")
    Call<ResponseBody> bloquearUsuario(@Header("Authorization") String authHeader, @Path("correo") String correo, @Path("correoBloqueado") String correoBloqueado);

    @DELETE("perfil/quitarbloqueo/{correo}/{correoBloqueado}")
    Call<ResponseBody> quitarBloqueo(@Header("Authorization") String authHeader, @Path("correo") String correo, @Path("correoBloqueado") String correoBloqueado);


    /************************EVENTOS************************/

    @GET("event/list")
    Call<ArrayList<Event>> listEvents(@Header("Authorization") String authHeader);

    @GET("user/events/joined/notfinished")
    Call<ArrayList<Event>> myEventsJoined(@Header("Authorization") String authHeader);

    @GET("user/events/joined/finished")
    Call<ArrayList<Event>> myEventsJoinedFinished(@Header("Authorization") String authHeader);

    @GET("event/search")
    Call<ArrayList<Event>> buscarEventos(@Header("Authorization") String authHeader,
                                         @Query("sport") String sport, @Query("startDate") String startDate,
                                         @Query("time") String time /*,@Query("reputation") float reputacion*/);

    @FormUrlEncoded
    @PUT("event/edit/startdate")
    Call<ResponseBody> editStartDateEvent(@Header("Authorization") String authHeader, @Field("id") Long id, @Field("startDate") String startDate);

    @FormUrlEncoded
    @PUT("eventos/actualizar/hora")
    Call<ResponseBody> actualizarHoraEvento(@Header("Authorization") String authHeader, @Field("idEvento") Long idEvento, @Field("hora") String hora);

    @FormUrlEncoded
    @PUT("event/edit/address/{id}")
    Call<ResponseBody> editAddressEvent(@Header("Authorization") String authHeader, @Field("id") Long id, @Field("address") String address);

    @FormUrlEncoded
    @PUT("eventos/actualizar/maximoparticipantes")
    Call<ResponseBody> actualizarMaxParticipantesEvento(@Header("Authorization") String authHeader, @Field("idEvento") Long idEvento, @Field("maxParticipantes") int maxParticipantes);

    @FormUrlEncoded
    @PUT("eventos/actualizar/terminado")
    Call<ResponseBody> actualizarTerminarEvento(@Header("Authorization") String authHeader, @Field("idEvento") Long idEvento, @Field("terminado") boolean terminado);

    @FormUrlEncoded
    @PUT("eventos/actualizar/reserva")
    Call<ResponseBody> actualizarReserva(@Header("Authorization") String authHeader, @Field("idEvento") String idEvento, @Field("reserva") boolean reserva);

    @FormUrlEncoded
    @PUT("eventos/actualizar/coste")
    Call<ResponseBody> actualizarCoste(@Header("Authorization") String authHeader, @Field("idEvento") Long idEvento, @Field("coste") float coste);

    @FormUrlEncoded
    @PUT("eventos/actualizar/precio")
    Call<ResponseBody> actualizarPrecio(@Header("Authorization") String authHeader, @Field("idEvento") String idEvento, @Field("precio") float precio);

    @FormUrlEncoded
    @PUT("eventos/actualizar/comentarios")
    Call<ResponseBody> actualizarComentarios(@Header("Authorization") String authHeader, @Field("idEvento") Long idEvento, @Field("comentarios") String comentarios);

    @FormUrlEncoded
    @PUT("eventos/actualizar/edadminima")
    Call<ResponseBody> actualizarEdadMinima(@Header("Authorization") String authHeader, @Field("idEvento") Long idEvento, @Field("edad") int edad);

    @FormUrlEncoded
    @PUT("eventos/actualizar/edadmaxima")
    Call<ResponseBody> actualizarEdadMaxima(@Header("Authorization") String authHeader, @Field("idEvento") Long idEvento, @Field("edad") int edad);

    @FormUrlEncoded
    @PUT("eventos/actualizar/genero")
    Call<ResponseBody> actualizarGenero(@Header("Authorization") String authHeader, @Field("idEvento") Long idEvento, @Field("genero") String genero);

    @FormUrlEncoded
    @PUT("eventos/actualizar/reputacion")
    Call<ResponseBody> actualizarReputacion(@Header("Authorization") String authHeader, @Field("idEvento") Long idEvento, @Field("reputacion") float reputacion);

    @DELETE("eventos/eliminarparticipante/{idEvento}/{correo}")
    Call<ResponseBody> eliminarParticipante(@Header("Authorization") String authHeader, @Path("idEvento") Long idEvento, @Path("correo") String correo);

    @POST("event/accept/{idEvent}/{idUser}")
    Call<ResponseBody> insertarParticipante(@Header("Authorization") String authHeader, @Path("idEvent") long idEvent, @Path("idUser") long idUser);

    @POST("/join/{id}")
    Call<ResponseBody> insertarSolicitante(@Header("Authorization") String authHeader, @Path("id") Long idEvent);

    @DELETE("eventos/eliminarsolicitante/{idEvento}/{correo}")
    Call<ResponseBody> eliminarSolicitante(@Header("Authorization") String authHeader, @Path("idEvento") Long idEvento, @Path("correo") String correo);

    @PUT("eventos/bloquearsolicitud/{idEvento}/{correo}")
    Call<ResponseBody> bloquearSolicitud(@Header("Authorization") String authHeader, @Path("idEvento") String idEvento, @Path("correo") String correo);

    @DELETE("eventos/eliminar/{idEvento}")
    Call<ResponseBody> eliminarEvento(@Header("Authorization") String authHeader, @Path("idEvento") Long idEvento);

    @GET("perfil/puntuacionparticipante/{correo}")
    Call<Float> getReputacionParticipante(@Header("Authorization") String authHeader, @Path("correo") String correo);

    @GET("perfil/puntuacionorganizador/{correo}")
    Call<Float> getReputacionOrganizador(@Header("Authorization") String authHeader, @Path("correo") String correo);

    @GET("eventos/hasidopuntuado/{idevento}/{email}")
    Call<Boolean> getHaSidoPuntuado(@Header("Authorization") String authHeader, @Path("idevento") Long idevento, @Path("email") String email);

    @POST("perfil/insertarpuntuacion")
    Call<ResponseBody> insertarPuntuacionParticipante(@Header("Authorization") String authHeader, @Body UserScore puntuacion);

    @POST("eventos/insertarpuntuacion")
    Call<ResponseBody> insertarPuntuacionEvento(@Header("Authorization") String authHeader, @Body EventScore puntuacion);

    @Multipart
    @POST("imagenes/upload/{correo}")
    Call<ResponseBody> subirImagen(@Header("Authorization") String authHeader, @Part MultipartBody.Part filePart, @Part("filename") RequestBody name, @Path("correo") String correo);
}
