package tfg.dam.socialsports.fragments.userconfig;

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

import tfg.dam.socialsports.retrofit.APIService;
import tfg.dam.socialsports.adapter.ListUsersAdapter;
import tfg.dam.socialsports.model.User;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;
import tfg.dam.socialsports.retrofit.RetrofitConnection;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConfigFriends extends Fragment {

    private ListView listViewAmigos;
    private User userSeleccionado;
    private AlertDialog.Builder menuOpciones;
    private String[] opcionesAmigos;

    public UserConfigFriends() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_config_friends, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        opcionesAmigos = new String[]{getResources().getString(R.string.opcion_eliminar_amigo)};
        menuOpciones = new AlertDialog.Builder(getContext());
        menuOpciones.setItems(opcionesAmigos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        deleteFriend();
                        break;
                }
                mostrarListaAmigos(LoginActivity.user.getListaAmigos());
            }
        });
        listViewAmigos = getActivity().findViewById(R.id.listUserConfigFriends);
        getFriendList();
    }

    @Override
    public void onResume() {
        super.onResume();
        getFriendList();
    }

    private void mostrarListaAmigos(final ArrayList<User> arrayList)
    {
        ListUsersAdapter adapter = new ListUsersAdapter(getContext(), R.layout.item_lista_usuarios,
                R.id.textItemUsuarioNombre, arrayList);
        listViewAmigos.setAdapter(adapter);
        listViewAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userSeleccionado = LoginActivity.user.getListaAmigos().get(position);
                menuOpciones.setTitle(userSeleccionado.getFirstName() +
                        " " + userSeleccionado.getLastName());
                menuOpciones.show();
            }
        });
    }

    private void deleteFriend() {

        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.deleteFriend("Bearer " + LoginActivity.token,
                userSeleccionado.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    LoginActivity.user.getListaAmigos().remove(userSeleccionado);
                    mostrarListaAmigos(LoginActivity.user.getListaAmigos());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getFriendList(){
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();
        service.friendList("Bearer " + LoginActivity.token).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {

                if(response.isSuccessful()){
                    LoginActivity.user.setListaAmigos(new ArrayList<User>());
                    for(User user : response.body()){
                        LoginActivity.user.getListaAmigos().add(user);
                    }
                    mostrarListaAmigos(LoginActivity.user.getListaAmigos());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
