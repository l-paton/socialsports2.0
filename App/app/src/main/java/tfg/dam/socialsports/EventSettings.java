package tfg.dam.socialsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import tfg.dam.socialsports.fragments.eventsettings.EventSettingsParticipants;
import tfg.dam.socialsports.fragments.eventsettings.EventSettingsRequests;
import tfg.dam.socialsports.fragments.eventsettings.EventSettingsSettings;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventSettings extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private BottomNavigationView navigationView;
    private EventSettingsSettings eventSettingsSettings;
    private EventSettingsParticipants eventSettingsParticipants;
    private EventSettingsRequests eventSettingsRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_settings);
        tabLayout = findViewById(R.id.tabsEventSettings);
        navigationView = findViewById(R.id.navigationEventSettings);
        toolbar = findViewById(R.id.toolbarEventSettings);
        toolbar.setTitle(Funcionalidades.eventSeleccionado.getSport()+" - "+Funcionalidades.eventSeleccionado.getAddress());
        eventSettingsSettings = new EventSettingsSettings();
        eventSettingsParticipants = new EventSettingsParticipants();
        eventSettingsRequests = new EventSettingsRequests();

        if (Funcionalidades.eventSeleccionado.getOrganizer().getId() != LoginActivity.user.getId()) {
            if (!Funcionalidades.eresSolicitante(Funcionalidades.eventSeleccionado)
                    && !Funcionalidades.eresParticipante(Funcionalidades.eventSeleccionado)) {
                toolbar.inflateMenu(R.menu.event_subscribe_menu);
                navigationView.inflateMenu(R.menu.event_subscribe_menu);
            }
            else {
                toolbar.inflateMenu(R.menu.event_unsubscribe_menu);
                navigationView.inflateMenu(R.menu.event_unsubscribe_menu);
            }
        }
        else {
            toolbar.inflateMenu(R.menu.event_organizer_menu);
            navigationView.inflateMenu(R.menu.event_organizer_menu);
        }
        Funcionalidades.showSelectedFragment(R.id.containerEventSettings,getSupportFragmentManager(),eventSettingsSettings);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_event_settings_sttings))) {
                    Funcionalidades.showSelectedFragment(R.id.containerEventSettings, getSupportFragmentManager(), eventSettingsSettings);
                }
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_event_settings_participants))) {
                    eventSettingsParticipants = new EventSettingsParticipants();
                    Funcionalidades.showSelectedFragment(R.id.containerEventSettings, getSupportFragmentManager(), eventSettingsParticipants);
                }
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_event_settings_requests))) {
                    eventSettingsRequests = new EventSettingsRequests();
                    Funcionalidades.showSelectedFragment(R.id.containerEventSettings, getSupportFragmentManager(), eventSettingsRequests);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                realizarAccionSeleccionada(item);
                return true;
            }
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                realizarAccionSeleccionada(menuItem);
                return true;
            }
        });
    }

    private void realizarAccionSeleccionada(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemMenuEventSave:
                Funcionalidades.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),toolbar);
                guardarCambiosEvento();
                break;
            case R.id.itemMenuEventDelete:
                Funcionalidades.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),toolbar);
                eliminarEvento();
                finish();
                break;
            case R.id.itemMenuEventFinalize:
                Funcionalidades.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),toolbar);
                Funcionalidades.eventSeleccionado.setFinish(true);
                Funcionalidades.finishEvent(Funcionalidades.eventSeleccionado.getId());
                finish();
                Funcionalidades.mostrarMensaje("Evento Finalizado",this);
                break;
            case R.id.itemMenuSubscribe:
                Funcionalidades.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),toolbar);
                mandarSolicitud();
                break;
            case R.id.itemMenuUnsubscribe:
                Funcionalidades.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),toolbar);
                //eliminarSolicitud();
                break;
        }
    }

    private void guardarCambiosEvento() {
        if (eventSettingsSettings.getFechaEvento() != null) {
            if (Funcionalidades.eventSeleccionado.getStartDate() == null ||
                    eventSettingsSettings.getFechaEvento().compareTo(Funcionalidades.eventSeleccionado.getStartDate()) != 0) {
                Funcionalidades.eventSeleccionado.setStartDate(eventSettingsSettings.getFechaEvento());
                Funcionalidades.editStartDate(Funcionalidades.eventSeleccionado.getId(),eventSettingsSettings.getFechaEvento());
            }
        }

        if (!eventSettingsSettings.getHoraEvento().equals(Funcionalidades.eventSeleccionado.getTime())) {
            Funcionalidades.eventSeleccionado.setTime(eventSettingsSettings.getHoraEvento());
            Funcionalidades.editEventTime(Funcionalidades.eventSeleccionado.getId(), eventSettingsSettings.getHoraEvento());
        }

        if (!eventSettingsSettings.getDireccion().equals(Funcionalidades.eventSeleccionado.getAddress())) {
            Funcionalidades.eventSeleccionado.setAddress(eventSettingsSettings.getDireccion());
            Funcionalidades.actualizarDireccionEvento(Funcionalidades.eventSeleccionado.getId(),eventSettingsSettings.getDireccion());
        }

        if (eventSettingsSettings.getNumParticipantes() != Funcionalidades.eventSeleccionado.getMaxParticipants()) {
            Funcionalidades.eventSeleccionado.setMaxParticipants(eventSettingsSettings.getNumParticipantes());
            Funcionalidades.actualizarMaxParticipantesEvento(Funcionalidades.eventSeleccionado.getId(),eventSettingsSettings.getNumParticipantes());
        }

        if (eventSettingsSettings.getElOrganizadorEsParticipante() !=
                Funcionalidades.eventSeleccionado.getParticipants().contains(Funcionalidades.eventSeleccionado.getOrganizer())) {
            if (eventSettingsSettings.getElOrganizadorEsParticipante())
                Funcionalidades.insertarParticipante(Funcionalidades.eventSeleccionado,Funcionalidades.eventSeleccionado.getOrganizer());
            else
                Funcionalidades.eliminarParticipante(Funcionalidades.eventSeleccionado,Funcionalidades.eventSeleccionado.getOrganizer());
        }

        if (!eventSettingsSettings.getComentarios().equals(Funcionalidades.eventSeleccionado.getComments())) {
            Funcionalidades.eventSeleccionado.setComments(eventSettingsSettings.getComentarios());
            Funcionalidades.actualizarComentariosEvento(Funcionalidades.eventSeleccionado.getId(),eventSettingsSettings.getComentarios());
        }

        if (eventSettingsSettings.getEdadMinima() != Funcionalidades.eventSeleccionado.getRequirement().getMinAge()) {
            Funcionalidades.eventSeleccionado.getRequirement().setMinAge(eventSettingsSettings.getEdadMinima());
            Funcionalidades.actualizarEdadMinEvento(Funcionalidades.eventSeleccionado.getId(),eventSettingsSettings.getEdadMinima());
        }

        if (eventSettingsSettings.getEdadMaxima() != Funcionalidades.eventSeleccionado.getRequirement().getMaxAge()) {
            Funcionalidades.eventSeleccionado.getRequirement().setMaxAge(eventSettingsSettings.getEdadMaxima());
            Funcionalidades.actualizarEdadMaxEvento(Funcionalidades.eventSeleccionado.getId(),eventSettingsSettings.getEdadMaxima());
        }

        if (eventSettingsSettings.getGenero() != Funcionalidades.eventSeleccionado.getRequirement().getGender()) {
            Funcionalidades.eventSeleccionado.getRequirement().setGender(eventSettingsSettings.getGenero());
            Funcionalidades.actualizarGeneroEvento(Funcionalidades.eventSeleccionado.getId(),eventSettingsSettings.getGenero());
        }

        if (eventSettingsSettings.getReputacion() != Funcionalidades.eventSeleccionado.getRequirement().getReputation()) {
            Funcionalidades.eventSeleccionado.getRequirement().setReputation(eventSettingsSettings.getReputacion());
            Funcionalidades.actualizarReputacionEvento(Funcionalidades.eventSeleccionado.getId(),eventSettingsSettings.getReputacion());
        }

        Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_guardado_correcto), this);
    }

    private void mandarSolicitud() {
        Funcionalidades.insertarSolicitante(Funcionalidades.eventSeleccionado);
        Funcionalidades.mostrarMensaje(getResources().getString(R.string.messaje_request_sent),getApplicationContext());
        toolbar.inflateMenu(R.menu.event_unsubscribe_menu);
        tabLayout.getTabAt(2).select();
    }

    /*private void eliminarSolicitud() {
        if (Funcionalidades.eresSolicitante(Funcionalidades.eventoSeleccionado)) {
            Funcionalidades.eliminarSolicitante(Funcionalidades.eventoSeleccionado, LoginActivity.usuario);
            tabLayout.getTabAt(2).select();
        }
        else if (Funcionalidades.eresParticipante(Funcionalidades.eventoSeleccionado)) {
            Funcionalidades.eliminarParticipante(Funcionalidades.eventoSeleccionado, LoginActivity.usuario);
            tabLayout.getTabAt(1).select();
        }
        toolbar.inflateMenu(R.menu.event_subscribe_menu);

        Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_request_removed),this);
    }*/

    private void eliminarEvento() {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();
        service.eliminarEvento("Bearer " + LoginActivity.token, Funcionalidades.eventSeleccionado.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_event_removed),getApplicationContext());
                    //MainActivity.listaEventos.remove(Funcionalidades.eventoSeleccionado);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
