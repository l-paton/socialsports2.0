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

public class UserConfigBanned extends Fragment {

    private ListView listViewBloqueados;
    private User userSeleccionado;
    private AlertDialog.Builder menuOpciones;
    private String[] opcionesBloqueados;

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
        opcionesBloqueados = new String[]{getResources().getString(R.string.opcion_solicitud_de_amistad)};
        menuOpciones = new AlertDialog.Builder(getContext());
        menuOpciones.setItems(opcionesBloqueados, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        agregarAmigo();
                        break;
                }
                mostrarListaBloqueados(LoginActivity.user.getListaBloqueados());
            }
        });
        listViewBloqueados = getActivity().findViewById(R.id.listUserConfigBanned);
    }

    private void mostrarListaBloqueados(final ArrayList<User> arrayList)
    {
        ListUsersAdapter adapter = new ListUsersAdapter(getContext(), R.layout.item_lista_usuarios,
                R.id.textItemUsuarioNombre, arrayList);
        listViewBloqueados.setAdapter(adapter);
        listViewBloqueados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userSeleccionado = LoginActivity.user.getListaBloqueados().get(position);
                menuOpciones.setTitle(userSeleccionado.getFirstName() +
                        " " + userSeleccionado.getLastName());
                menuOpciones.show();
            }
        });
    }

    private void agregarAmigo() {
        RETROFIT retrofit = new RETROFIT();
        retrofit.getAPIService().addFriend("Bearer " + LoginActivity.token,
                userSeleccionado.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            LoginActivity.user.getListaBloqueados().remove(userSeleccionado);
                            LoginActivity.user.getListaAmigos().add(userSeleccionado);
                            mostrarListaBloqueados(LoginActivity.user.getListaBloqueados());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}
