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
import tfg.dam.socialsports.retrofit.APIService;
import tfg.dam.socialsports.retrofit.RetrofitConnection;

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
        toolbar.setTitle(Utils.eventSeleccionado.getSport()+" - "+ Utils.eventSeleccionado.getAddress());
        eventSettingsSettings = new EventSettingsSettings();
        eventSettingsParticipants = new EventSettingsParticipants();
        eventSettingsRequests = new EventSettingsRequests();

        if (Utils.eventSeleccionado.getOrganizer().getId() != LoginActivity.user.getId()) {
            if (!Utils.eresSolicitante(Utils.eventSeleccionado)
                    && !Utils.eresParticipante(Utils.eventSeleccionado)) {
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
        Utils.showSelectedFragment(R.id.containerEventSettings,getSupportFragmentManager(),eventSettingsSettings);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_event_settings_sttings))) {
                    Utils.showSelectedFragment(R.id.containerEventSettings, getSupportFragmentManager(), eventSettingsSettings);
                }
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_event_settings_participants))) {
                    eventSettingsParticipants = new EventSettingsParticipants();
                    Utils.showSelectedFragment(R.id.containerEventSettings, getSupportFragmentManager(), eventSettingsParticipants);
                }
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_event_settings_requests))) {
                    eventSettingsRequests = new EventSettingsRequests();
                    Utils.showSelectedFragment(R.id.containerEventSettings, getSupportFragmentManager(), eventSettingsRequests);
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
                Utils.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),toolbar);
                saveChanges();
                break;
            case R.id.itemMenuEventDelete:
                Utils.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),toolbar);
                eliminarEvento();
                finish();
                break;
            case R.id.itemMenuEventFinalize:
                Utils.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),toolbar);
                Utils.eventSeleccionado.setFinish(true);
                Utils.finishEvent(Utils.eventSeleccionado.getId());
                finish();
                Utils.mostrarMensaje("Evento Finalizado",this);
                break;
            case R.id.itemMenuSubscribe:
                Utils.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),toolbar);
                mandarSolicitud();
                break;
            case R.id.itemMenuUnsubscribe:
                Utils.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),toolbar);
                //eliminarSolicitud();
                break;
        }
    }

    private void saveChanges() {
        if (eventSettingsSettings.getFechaEvento() != null) {
            if (Utils.eventSeleccionado.getStartDate() == null ||
                    eventSettingsSettings.getFechaEvento().compareTo(Utils.eventSeleccionado.getStartDate()) != 0) {
                Utils.eventSeleccionado.setStartDate(eventSettingsSettings.getFechaEvento());
                Utils.editStartDate(Utils.eventSeleccionado.getId(),eventSettingsSettings.getFechaEvento());
            }
        }

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!eventSettingsSettings.getHoraEvento().equals(Utils.eventSeleccionado.getTime())) {
            Utils.eventSeleccionado.setTime(eventSettingsSettings.getHoraEvento());
            Utils.editEventTime(Utils.eventSeleccionado.getId(), eventSettingsSettings.getHoraEvento());
        }

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!eventSettingsSettings.getDireccion().equals(Utils.eventSeleccionado.getAddress())) {
            Utils.eventSeleccionado.setAddress(eventSettingsSettings.getDireccion());
            Utils.actualizarDireccionEvento(Utils.eventSeleccionado.getId(),eventSettingsSettings.getDireccion());
        }

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (eventSettingsSettings.getNumParticipantes() != Utils.eventSeleccionado.getMaxParticipants()) {
            Utils.eventSeleccionado.setMaxParticipants(eventSettingsSettings.getNumParticipantes());
            Utils.actualizarMaxParticipantesEvento(Utils.eventSeleccionado.getId(),eventSettingsSettings.getNumParticipantes());
        }
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (eventSettingsSettings.getElOrganizadorEsParticipante() !=
                Utils.eventSeleccionado.getParticipants().contains(Utils.eventSeleccionado.getOrganizer())) {
            if (eventSettingsSettings.getElOrganizadorEsParticipante())
                Utils.insertarParticipante(Utils.eventSeleccionado, Utils.eventSeleccionado.getOrganizer());
            else
                Utils.removeParticipant(Utils.eventSeleccionado, Utils.eventSeleccionado.getOrganizer());
        }
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!eventSettingsSettings.getComentarios().equals(Utils.eventSeleccionado.getComments())) {
            Utils.eventSeleccionado.setComments(eventSettingsSettings.getComentarios());
            Utils.actualizarComentariosEvento(Utils.eventSeleccionado.getId(),eventSettingsSettings.getComentarios());
        }
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (eventSettingsSettings.getEdadMinima() != Utils.eventSeleccionado.getRequirement().getMinAge()) {
            Utils.eventSeleccionado.getRequirement().setMinAge(eventSettingsSettings.getEdadMinima());
            Utils.actualizarEdadMinEvento(Utils.eventSeleccionado.getId(),eventSettingsSettings.getEdadMinima());
        }
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (eventSettingsSettings.getEdadMaxima() != Utils.eventSeleccionado.getRequirement().getMaxAge()) {
            Utils.eventSeleccionado.getRequirement().setMaxAge(eventSettingsSettings.getEdadMaxima());
            Utils.actualizarEdadMaxEvento(Utils.eventSeleccionado.getId(),eventSettingsSettings.getEdadMaxima());
        }
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (eventSettingsSettings.getGenero() != Utils.eventSeleccionado.getRequirement().getGender()) {
            Utils.eventSeleccionado.getRequirement().setGender(eventSettingsSettings.getGenero());
            Utils.actualizarGeneroEvento(Utils.eventSeleccionado.getId(),eventSettingsSettings.getGenero());
        }
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (eventSettingsSettings.getReputacion() != Utils.eventSeleccionado.getRequirement().getReputation()) {
            Utils.eventSeleccionado.getRequirement().setReputation(eventSettingsSettings.getReputacion());
            Utils.actualizarReputacionEvento(Utils.eventSeleccionado.getId(),eventSettingsSettings.getReputacion());
        }
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(eventSettingsSettings.getPrecio() != Utils.eventSeleccionado.getPrice()){
            Utils.eventSeleccionado.setPrice(eventSettingsSettings.getPrecio());
            Utils.actualizarPrecioEvento(Utils.eventSeleccionado.getId(), eventSettingsSettings.getPrecio());
        }

        Utils.mostrarMensaje(getResources().getString(R.string.mensaje_guardado_correcto), this);
    }

    private void mandarSolicitud() {
        Utils.insertarSolicitante(Utils.eventSeleccionado);
        Utils.mostrarMensaje(getResources().getString(R.string.messaje_request_sent),getApplicationContext());
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
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.deleteEvent("Bearer " + LoginActivity.token, Utils.eventSeleccionado.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Utils.mostrarMensaje(getResources().getString(R.string.mensaje_event_removed),getApplicationContext());
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
