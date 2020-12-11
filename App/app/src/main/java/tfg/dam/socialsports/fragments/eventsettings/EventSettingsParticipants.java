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

    private User selectedUser;
    private ListView participantsListView;
    private ArrayList<User> participantsList;
    private AlertDialog.Builder optionsMenu;
    private String[] options;

    public EventSettingsParticipants() {
        participantsList = new ArrayList<>();
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
            options = new String[]{getResources().getString(R.string.opcion_eliminar_participante)
                    , getResources().getString(R.string.opcion_solicitud_de_amistad)};
        }
        else {
            options = new String[]{getResources().getString(R.string.opcion_solicitud_de_amistad)};
        }
        optionsMenu = new AlertDialog.Builder(getContext());
        if (Utils.eresOrganizador(Utils.eventSeleccionado)) {
            optionsMenu.setItems(options, new DialogInterface.OnClickListener() {
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
                    if (participantsList != null)
                        mostrarListaUsuarios(participantsList);
                }
            });
        }
        else {
            optionsMenu.setItems(options, new DialogInterface.OnClickListener() {
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
        participantsListView = getActivity().findViewById(R.id.listEventSettingsParticipants);
        participantsList = Utils.eventSeleccionado.getParticipants();
        if (participantsList != null)
            mostrarListaUsuarios(participantsList);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (participantsList != null)
            mostrarListaUsuarios(participantsList);
    }

    private void mostrarListaUsuarios(ArrayList<User> arrayList) {
        ListUsersAdapter adapter = new ListUsersAdapter(getContext(), R.layout.item_lista_usuarios,
                R.id.textItemUsuarioNombre, arrayList);
        participantsListView.setAdapter(adapter);
        participantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = participantsList.get(position);
                if (!Utils.soyYo(selectedUser)) {
                    optionsMenu.setTitle(selectedUser.getFirstName() +
                            " " + selectedUser.getLastName());
                    optionsMenu.show();
                }
            }
        });
    }

    private void eliminarParticipante() {
        Utils.removeParticipant(Utils.eventSeleccionado, selectedUser);
    }

    private void agregarAmigo() {
        Utils.addFriend(selectedUser);
    }
}
