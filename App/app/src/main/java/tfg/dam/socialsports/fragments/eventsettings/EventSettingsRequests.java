package tfg.dam.socialsports.fragments.eventsettings;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tfg.dam.socialsports.APIService;
import tfg.dam.socialsports.Clases.ListUsersAdapter;
import tfg.dam.socialsports.Clases.User;
import tfg.dam.socialsports.Funcionalidades;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;
import tfg.dam.socialsports.RETROFIT;


public class EventSettingsRequests extends Fragment {

    private User userSeleccionado;
    private ListView listViewSolicitudes;
    private ArrayList<User> listaSolicitantes;
    private AlertDialog.Builder menuOpciones;
    private String[] opciones;

    public EventSettingsRequests() {
        listaSolicitantes = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_settings_requests, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Funcionalidades.eresOrganizador(Funcionalidades.eventSeleccionado)) {
            opciones = new String[]{getResources().getString(R.string.opcion_aceptar_solicitud)
                    , getResources().getString(R.string.opcion_rechazar_solicitud)
                    , getResources().getString(R.string.opcion_bloqueo_de_evento)
                    , getResources().getString(R.string.opcion_bloqueo_permanente)
                    , getResources().getString(R.string.opcion_solicitud_de_amistad)};
        }
        else {
            opciones = new String[]{getResources().getString(R.string.opcion_solicitud_de_amistad)};
        }
        menuOpciones = new AlertDialog.Builder(getContext());
        if (Funcionalidades.eresOrganizador(Funcionalidades.eventSeleccionado)) {
            menuOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            aceptarSolicitud();
                            break;
                        case 1:
                            eliminarSolicitud();
                            break;
                        case 2:
                            bloquearSolicitud();
                            break;
                        case 3:
                            bloquearSolicitantePermanentemente();
                            break;
                        case 4:
                            agregarAmigo();
                            break;
                    }
                    if (listaSolicitantes != null)
                        mostrarListaUsuarios(listaSolicitantes);
                }
            });
        }
        else {
            menuOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            agregarAmigo();
                            break;
                    }
                }
            });
        }
        listViewSolicitudes = getActivity().findViewById(R.id.listEventSettingsRequest);
        listaSolicitantes = Funcionalidades.eventSeleccionado.getApplicants();
        if (listaSolicitantes != null)
            mostrarListaUsuarios(listaSolicitantes);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listaSolicitantes != null)
            mostrarListaUsuarios(listaSolicitantes);
    }

    private void mostrarListaUsuarios(ArrayList<User> arrayList) {
        ListUsersAdapter adapter = new ListUsersAdapter(getContext(), R.layout.item_lista_usuarios,
                R.id.textItemUsuarioNombre, arrayList);
        listViewSolicitudes.setAdapter(adapter);
        listViewSolicitudes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userSeleccionado = listaSolicitantes.get(position);
                if (!Funcionalidades.soyYo(userSeleccionado)) {
                    menuOpciones.setTitle(userSeleccionado.getFirstName() +
                            " " + userSeleccionado.getLastName());
                    menuOpciones.show();
                }
            }
        });
    }

    private void aceptarSolicitud() {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.insertarParticipante("Bearer " + LoginActivity.token,
                Funcionalidades.eventSeleccionado.getId(),
                userSeleccionado.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Funcionalidades.eventSeleccionado.getApplicants().remove((userSeleccionado));
                    Funcionalidades.eventSeleccionado.getParticipants().add(userSeleccionado);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void eliminarSolicitud() {
        Funcionalidades.eliminarSolicitante(Funcionalidades.eventSeleccionado, userSeleccionado);
    }

    private void bloquearSolicitud() {
        Funcionalidades.eliminarSolicitante(Funcionalidades.eventSeleccionado, userSeleccionado);
    }

    private void bloquearSolicitantePermanentemente() {
        Funcionalidades.eliminarSolicitante(Funcionalidades.eventSeleccionado, userSeleccionado);
        Funcionalidades.bloquearUsuarioPermanentemente(userSeleccionado);
        Funcionalidades.eliminarAmigo(userSeleccionado);
    }

    private void agregarAmigo() {
        //Funcionalidades.eliminarBloqueoPermanentemente(usuarioSeleccionado);
        Funcionalidades.insertarAmigo(userSeleccionado);
    }
}
