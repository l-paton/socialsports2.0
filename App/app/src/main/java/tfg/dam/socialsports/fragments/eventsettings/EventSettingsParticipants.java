package tfg.dam.socialsports.fragments.eventsettings;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import tfg.dam.socialsports.adapter.ListUsersAdapter;
import tfg.dam.socialsports.model.User;
import tfg.dam.socialsports.Utils;
import tfg.dam.socialsports.R;


public class EventSettingsParticipants extends Fragment {

    private User userSeleccionado;
    private ListView listViewParticipantes;
    private ArrayList<User> listaParticipantes;
    private AlertDialog.Builder menuOpciones;
    private String[] opciones;

    public EventSettingsParticipants() {
        listaParticipantes = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_settings_participants, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Utils.eresOrganizador(Utils.eventSeleccionado)) {
            opciones = new String[]{getResources().getString(R.string.opcion_eliminar_participante)
                    , getResources().getString(R.string.opcion_solicitud_de_amistad)};
        }
        else {
            opciones = new String[]{getResources().getString(R.string.opcion_solicitud_de_amistad)};
        }
        menuOpciones = new AlertDialog.Builder(getContext());
        if (Utils.eresOrganizador(Utils.eventSeleccionado)) {
            menuOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            eliminarParticipante();
                            break;
                        case 2:
                            agregarAmigo();
                            break;
                    }
                    if (listaParticipantes != null)
                        mostrarListaUsuarios(listaParticipantes);
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
        listViewParticipantes = getActivity().findViewById(R.id.listEventSettingsParticipants);
        listaParticipantes = Utils.eventSeleccionado.getParticipants();
        if (listaParticipantes != null)
            mostrarListaUsuarios(listaParticipantes);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listaParticipantes != null)
            mostrarListaUsuarios(listaParticipantes);
    }

    private void mostrarListaUsuarios(ArrayList<User> arrayList) {
        ListUsersAdapter adapter = new ListUsersAdapter(getContext(), R.layout.item_lista_usuarios,
                R.id.textItemUsuarioNombre, arrayList);
        listViewParticipantes.setAdapter(adapter);
        listViewParticipantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userSeleccionado = listaParticipantes.get(position);
                if (!Utils.soyYo(userSeleccionado)) {
                    menuOpciones.setTitle(userSeleccionado.getFirstName() +
                            " " + userSeleccionado.getLastName());
                    menuOpciones.show();
                }
            }
        });
    }

    private void eliminarParticipante() {
        Utils.removeParticipant(Utils.eventSeleccionado, userSeleccionado);
    }

    private void agregarAmigo() {
        Utils.addFriend(userSeleccionado);
    }
}
