package tfg.dam.socialsports.fragments.userconfig;

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

import tfg.dam.socialsports.APIService;
import tfg.dam.socialsports.Clases.ListUsersAdapter;
import tfg.dam.socialsports.Clases.User;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;
import tfg.dam.socialsports.RETROFIT;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConfigBanned extends Fragment {

    private ListView listView;
    private User selectedUser;
    private AlertDialog.Builder menu;
    private String[] options;

    public UserConfigBanned() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_config_banned, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        options = new String[]{getResources().getString(R.string.opcion_aceptar_solicitud), getResources().getString(R.string.opcion_rechazar_solicitud)};
        menu = new AlertDialog.Builder(getContext());
        menu.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        acceptRequest();
                        break;
                    case 1:
                        denyRequest();
                        break;
                }
                showFriendRequests(LoginActivity.user.getListaBloqueados());
            }
        });
        listView = getActivity().findViewById(R.id.listUserConfigBanned);
        getRequestsList();
    }

    private void showFriendRequests(final ArrayList<User> arrayList)
    {
        ListUsersAdapter adapter = new ListUsersAdapter(getContext(), R.layout.item_lista_usuarios,
                R.id.textItemUsuarioNombre, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = LoginActivity.user.getListaBloqueados().get(position);
                menu.setTitle(selectedUser.getFirstName() +
                        " " + selectedUser.getLastName());
                menu.show();
            }
        });
    }

    private void acceptRequest() {
        RETROFIT retrofit = new RETROFIT();
        retrofit.getAPIService().acceptFriend("Bearer " + LoginActivity.token,
                selectedUser.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            LoginActivity.user.getListaAmigos().add(selectedUser);
                            LoginActivity.user.getListaBloqueados().remove(selectedUser);
                            showFriendRequests(LoginActivity.user.getListaBloqueados());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    public void denyRequest(){

        RETROFIT retrofit = new RETROFIT();
        retrofit.getAPIService().denyFriend("Bearer " + LoginActivity.token, selectedUser.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            LoginActivity.user.getListaBloqueados().remove(selectedUser);
                            showFriendRequests(LoginActivity.user.getListaBloqueados());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    private void getRequestsList(){

        Log.e("SE METE EN DENY", "EEEEEEEEEEEee");

        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.requestsReceived("Bearer " + LoginActivity.token).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {

                if(response.isSuccessful()){
                    LoginActivity.user.setListaBloqueados(new ArrayList<User>());
                    for(User user : response.body()){
                        LoginActivity.user.getListaBloqueados().add(user);
                    }
                    showFriendRequests(LoginActivity.user.getListaBloqueados());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
