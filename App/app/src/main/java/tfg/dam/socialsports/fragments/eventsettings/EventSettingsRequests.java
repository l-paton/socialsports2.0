package tfg.dam.socialsports.fragments.eventsettings;

import android.content.DialogInterface;
import android.content.Intent;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tfg.dam.socialsports.APIService;
import tfg.dam.socialsports.Clases.ListUsersAdapter;
import tfg.dam.socialsports.Clases.User;
import tfg.dam.socialsports.EventSettings;
import tfg.dam.socialsports.Funcionalidades;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;
import tfg.dam.socialsports.RETROFIT;


public class EventSettingsRequests extends Fragment {

    private User selectedUser;
    private ListView listViewRequests;
    private ArrayList<User> listApplicants;
    private AlertDialog.Builder manuOptions;
    private String[] options;

    public EventSettingsRequests() {
        listApplicants = new ArrayList<>();
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
            options = new String[]{getResources().getString(R.string.opcion_aceptar_solicitud)
                    , getResources().getString(R.string.opcion_rechazar_solicitud)
                    , getResources().getString(R.string.opcion_solicitud_de_amistad)};
        }
        else {
            options = new String[]{getResources().getString(R.string.opcion_solicitud_de_amistad)};
        }
        manuOptions = new AlertDialog.Builder(getContext());
        if (Funcionalidades.eresOrganizador(Funcionalidades.eventSeleccionado)) {
            manuOptions.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            acceptApplicantRequest();
                            break;
                        case 1:
                            denyApplicantRequest();
                            break;
                        case 2:
                            addFriend();
                            break;
                    }
                    if (listApplicants != null)
                        showUserList(listApplicants);
                }
            });
        }
        else {
            manuOptions.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            addFriend();
                            break;
                    }
                }
            });
        }
        listViewRequests = getActivity().findViewById(R.id.listEventSettingsRequest);
        listApplicants = Funcionalidades.eventSeleccionado.getApplicants();
        if (listApplicants != null)
            showUserList(listApplicants);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listApplicants != null)
            showUserList(listApplicants);
    }

    private void showUserList(ArrayList<User> arrayList) {
        ListUsersAdapter adapter = new ListUsersAdapter(getContext(), R.layout.item_lista_usuarios,
                R.id.textItemUsuarioNombre, arrayList);
        listViewRequests.setAdapter(adapter);
        listViewRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = listApplicants.get(position);
                if (!Funcionalidades.soyYo(selectedUser)) {
                    manuOptions.setTitle(selectedUser.getFirstName() +
                            " " + selectedUser.getLastName());
                    manuOptions.show();
                }
            }
        });
    }

    private void acceptApplicantRequest() {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.acceptApplicantRequest("Bearer " + LoginActivity.token,
                Funcionalidades.eventSeleccionado.getId(),
                selectedUser.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Funcionalidades.eventSeleccionado.getParticipants().add(selectedUser);
                    Funcionalidades.eventSeleccionado.getApplicants().remove(selectedUser);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void denyApplicantRequest() {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();
        service.denyApplicantRequest("Bearer " + LoginActivity.token, Funcionalidades.eventSeleccionado.getId(), selectedUser.getId())
                .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    for(User u : Funcionalidades.eventSeleccionado.getApplicants()){
                        if(u.getId() == selectedUser.getId()){
                            Funcionalidades.eventSeleccionado.getApplicants().remove(u);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void addFriend() {
        Funcionalidades.addFriend(selectedUser);
    }
}
