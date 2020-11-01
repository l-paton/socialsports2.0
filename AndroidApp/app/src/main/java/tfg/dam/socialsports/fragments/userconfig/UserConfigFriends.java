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
        opcionesAmigos = new String[]{getResources().getString(R.string.opcion_eliminar_amigo)
                ,getResources().getString(R.string.opcion_bloqueo_permanente)};
        menuOpciones = new AlertDialog.Builder(getContext());
        menuOpciones.setItems(opcionesAmigos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        eliminarAmigo();
                        break;
                    case 1:
                        bloqueaoPermanebte();
                        break;
                }
                mostrarListaAmigos(LoginActivity.user.getListaAmigos());
            }
        });
        listViewAmigos = getActivity().findViewById(R.id.listUserConfigFriends);
        obtenerListaAmigos();
    }

    @Override
    public void onResume() {
        super.onResume();
        obtenerListaAmigos();
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

    private void eliminarAmigo() {

        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();
        service.deleteFriend("Bearer " + LoginActivity.token,
                LoginActivity.user.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 204){
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

    private void bloqueaoPermanebte() {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();
        service.bloquearUsuario("Bearer " + LoginActivity.token,
                LoginActivity.user.getEmail(),
                userSeleccionado.getEmail()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 204){
                    LoginActivity.user.getListaAmigos().remove(userSeleccionado);
                    LoginActivity.user.getListaBloqueados().add(userSeleccionado);
                    mostrarListaAmigos(LoginActivity.user.getListaAmigos());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void obtenerListaAmigos(){
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();
        service.listaAmigos("Bearer " + LoginActivity.token).enqueue(new Callback<ArrayList<User>>() {
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
