package tfg.dam.socialsports;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tfg.dam.socialsports.model.Event;
import tfg.dam.socialsports.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tfg.dam.socialsports.retrofit.APIService;
import tfg.dam.socialsports.retrofit.RetrofitConnection;

public class Utils extends AppCompatActivity {

    public static Event eventSeleccionado;

    public static void cambiarColoresTexto(EditText et, Application application){
        if (et.isFocused()) {
            et.setTextColor(application.getResources().getColor(R.color.colorAccent));
            et.getBackground().setTint(application.getResources().getColor(R.color.colorAccent));
        }
        else {
            et.setTextColor(application.getResources().getColor(R.color.colorElements));
            et.getBackground().setTint(application.getResources().getColor(R.color.colorElements));
        }
    }

    public static void cambiarColoresBoton(Button button, Application application){
        if (button.isFocused()) {
            button.setTextColor(application.getResources().getColor(R.color.colorAccent));
            button.setBackground(application.getResources().getDrawable(R.drawable.boton1_selected));
        }
        else {
            button.setTextColor(application.getResources().getColor(R.color.colorElements));
            button.setBackground(application.getResources().getDrawable(R.drawable.boton1));
        }
    }

    public static void cambiarColoresBotonSimple(Button button, Application application){
        if (button.isFocused())
            button.setTextColor(application.getResources().getColor(R.color.colorAccent));
        else
            button.setTextColor(application.getResources().getColor(R.color.colorElements));
    }

    public static void mostrarMensaje(String mensaje, Context context){
        Toast toast = Toast.makeText(context,mensaje,Toast.LENGTH_LONG);
        toast.show();
    }

    //Mostrar un fragment determinado en un elemento de tipo "R.id.container"(int) determindo.
    public static void showSelectedFragment(int container, FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction().replace(container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    public static void esconderTeclado(Activity a, Context c, View v) {
        InputMethodManager imm = (InputMethodManager)a.getSystemService(c.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void esconderTeclado(Object object, View v) {
        InputMethodManager imm = (InputMethodManager)object;
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static String dateToString(Date fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("E dd MMM yyyy");
        if (fecha!=null)
            return formato.format(fecha);
        return "";
    }

    public static String dateToString2(Date fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        if (fecha!=null)
            return formato.format(fecha);
        return "";
    }

    public static Date StringToDate(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        if (fecha!=null && !fecha.isEmpty()) {
            try {
                return formato.parse(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String dateToStringLargo(Date fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");
        if (fecha!=null)
            return formato.format(fecha);
        return "";
    }

    public static boolean eresSolicitante(Event ev) {
        for (User user: ev.getApplicants()) {
            if (user.getId() == LoginActivity.user.getId())
                return true;
        }
        return false;
    }

    public static boolean eresParticipante(Event ev) {
        for (User user : ev.getParticipants()) {
            if (user.getId() == LoginActivity.user.getId())
                return true;
        }
        return false;
    }

    public static boolean eresOrganizador(Event ev) {
        return LoginActivity.user.getId() == ev.getOrganizer().getId();
    }

    public static boolean soyYo(User user) {
        return LoginActivity.user.getId() == user.getId();
    }

    public static int calcularEdad(Date fecha) {
        if (fecha==null)
            return 0;
        return (int) (((new Date().getTime() - fecha.getTime()) / 86400000) / 365);
    }

    public static void finishEvent(Long idEvento) {
        RetrofitConnection retrofit = new RetrofitConnection();
        retrofit.getAPIService().finishEvent("Bearer " + LoginActivity.token, idEvento).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void addFriend(final User user) {
            RetrofitConnection retrofit = new RetrofitConnection();
            retrofit.getAPIService().sendFriendRequest("Bearer " + LoginActivity.token,
                    user.getId())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });


    }

    public static void editStartDate(Long idEvent, Date startDate) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.editStartDateEvent("Bearer " + LoginActivity.token, idEvent, dateToString2(startDate)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void editEventTime(Long id, String hora) {

        Log.e("HORA", hora);
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.editTimeEvent("Bearer " + LoginActivity.token, id, hora).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void actualizarDireccionEvento(Long idEvento, String direccion) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.editAddressEvent("Bearer " + LoginActivity.token, idEvento, direccion).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void actualizarMaxParticipantesEvento(Long idEvento, int maxParticipants) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.editMaxParticipantsEvent("Bearer " + LoginActivity.token, idEvento, maxParticipants).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void removeParticipant(final Event event, final User user) {

        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.removeParticipant("Bearer " + LoginActivity.token, event.getId(), user.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    for (User participant : event.getParticipants()) {
                        if (participant.getId()  == user.getId()) {
                            event.getParticipants().remove(participant);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void insertarParticipante(final Event event,final User user) {

        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.acceptApplicantRequest("Bearer " + LoginActivity.token, event.getId(), user.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if (!event.getParticipants().contains(user)) {
                        event.getParticipants().add(user);
                        event.getParticipants().remove(user);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void actualizarPrecioEvento(long id, float precio) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.actualizarPrecio("Bearer " + LoginActivity.token, id, precio).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void actualizarComentariosEvento(Long idEvento, String comment) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.editComment("Bearer " + LoginActivity.token, idEvento, comment).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void actualizarEdadMinEvento(Long idEvento, int edad) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.editMinAge("Bearer " + LoginActivity.token, idEvento, edad).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void actualizarEdadMaxEvento(Long idEvento, int edad) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.editMaxAge("Bearer " + LoginActivity.token, idEvento, edad).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void actualizarGeneroEvento(Long idEvento, String genero) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.editGender("Bearer " + LoginActivity.token, idEvento, genero).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void actualizarReputacionEvento(Long idEvento, float reputacion) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.actualizarReputacion("Bearer " + LoginActivity.token, idEvento, reputacion).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void insertarSolicitante(final Event event) {

        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.sendRequestToJoinEvent("Bearer " + LoginActivity.token, event.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    event.getApplicants().add(LoginActivity.user);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
