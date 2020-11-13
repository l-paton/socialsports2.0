package tfg.dam.socialsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;

import tfg.dam.socialsports.Clases.ListUsersAdapter;
import tfg.dam.socialsports.Clases.EventScore;
import tfg.dam.socialsports.Clases.UserScore;
import tfg.dam.socialsports.Clases.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRate extends AppCompatActivity {

    private User userSeleccionado;
    private ListView listViewParticipants;
    private RatingBar ratingBarOrganizer;
    private TextView textOrganizer;
    private TextView textParticipants;
    private View viewDivider;
    private Toolbar toolbar;
    private BottomNavigationView navigationView;
    private boolean haSidoPuntuado;
    public static ArrayList<UserScore> listaPuntuaciones;
    private ArrayList<User> listaParticipantes;
    private AlertDialog.Builder menuOpciones;
    private String[] opcionesOrganizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_rate);
        listaPuntuaciones = new ArrayList<>();
        listaParticipantes = new ArrayList<>();
        opcionesOrganizador = new String[]{getResources().getString(R.string.opcion_bloqueo_permanente)
                ,getResources().getString(R.string.opcion_solicitud_de_amistad)};
        listViewParticipants = findViewById(R.id.listRateParticipants);
        ratingBarOrganizer = findViewById(R.id.ratingRateOrganizer);
        textOrganizer = findViewById(R.id.textRateOrganizer);
        textParticipants = findViewById(R.id.textRateParticipants);
        viewDivider = findViewById(R.id.dividerEventRate);
        navigationView = findViewById(R.id.navigationEventRate);
        toolbar = findViewById(R.id.toolbarEventRate);
        toolbar.setTitle(Funcionalidades.eventSeleccionado.getSport()+" - "+Funcionalidades.eventSeleccionado.getAddress());
        if (Funcionalidades.eventSeleccionado.getOrganizer().getId() == LoginActivity.user.getId()) {
            ratingBarOrganizer.setVisibility(View.GONE);
            ratingBarOrganizer.setEnabled(false);
            textOrganizer.setVisibility(View.GONE);
            viewDivider.setVisibility(View.GONE);
        }
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.itemMenuRate) {
                    haSidoPuntuado = true;
                    deshabilitarPuntuar();
                    sendScores();
                }
                return true;
            }
        });

        haSidoPuntuado(Funcionalidades.eventSeleccionado.getId(),LoginActivity.user.getEmail());

        menuOpciones = new AlertDialog.Builder(this);
        menuOpciones.setItems(opcionesOrganizador, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        break;
                    case 1:
                        addFriend();
                        break;
                }
            }
        });
    }

    private void showParticipants(ArrayList<User> arrayList)
    {
        ListUsersAdapter adapter = new ListUsersAdapter(this, R.layout.item_lista_usuarios,
                R.id.textItemEventoDeporte, arrayList, haSidoPuntuado);
        listViewParticipants.setAdapter(adapter);
        if (haSidoPuntuado) {
            listViewParticipants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    userSeleccionado = listaParticipantes.get(position);
                    if (!Funcionalidades.soyYo(userSeleccionado)) {
                        menuOpciones.setTitle(userSeleccionado.getFirstName() +
                                " " + userSeleccionado.getLastName());
                        menuOpciones.show();
                    }
                }
            });
        }
    }

    private void sendScores() {
        if (ratingBarOrganizer.isEnabled()) {
            EventScore eventScore = new EventScore(LoginActivity.user.getId(),
                    Funcionalidades.eventSeleccionado.getId(), ratingBarOrganizer.getRating());
            enviarPuntuacionEvento(eventScore);
        }
        for (UserScore puntuacion: listaPuntuaciones) {
            rateParticipants(puntuacion);
        }
    }

    private void deshabilitarPuntuar() {
        navigationView.setVisibility(View.GONE);
        textOrganizer.setText(getResources().getString(R.string.organizer_has_been_rated));
        textParticipants.setText(getResources().getString(R.string.participants_has_been_rated));
    }

    private void addFriend() {
        Funcionalidades.addFriend(userSeleccionado);
    }

    public void haSidoPuntuado(Long idEvento, String email) {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.getHaSidoPuntuado("Bearer " + LoginActivity.token, idEvento, email).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()) {
                    haSidoPuntuado = response.body();
                    if (haSidoPuntuado)
                        deshabilitarPuntuar();
                }
                listaParticipantes = Funcionalidades.eventSeleccionado.getParticipants();
                if (listaParticipantes != null)
                    showParticipants(listaParticipantes);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Funcionalidades.mostrarMensaje("onFailure",getApplicationContext());
            }
        });
    }

    public void rateParticipants(UserScore puntuacion) {

        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.rateParticipant("Bearer " + LoginActivity.token, puntuacion.getIdRatedUser(), puntuacion.getIdEvent(), puntuacion.getScore()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void enviarPuntuacionEvento(EventScore puntuacion) {

        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.insertarPuntuacionEvento("Bearer " + LoginActivity.token, puntuacion).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 201){

                }else{
                    try {
                        Log.e("MENSAJE-ERROR:", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
