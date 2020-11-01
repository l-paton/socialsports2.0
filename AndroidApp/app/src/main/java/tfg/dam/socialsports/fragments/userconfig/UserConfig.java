package tfg.dam.socialsports.fragments.userconfig;


import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import tfg.dam.socialsports.APIService;
import tfg.dam.socialsports.Clases.User;
import tfg.dam.socialsports.Funcionalidades;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;
import tfg.dam.socialsports.RETROFIT;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserConfig extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private BottomNavigationView navigationView;
    private UserConfigSettings userConfigSettings;
    private UserConfigFriends userConfigFriends;
    private UserConfigBanned userConfigBanned;

    public UserConfig() {
        userConfigSettings = new UserConfigSettings();
        userConfigFriends = new UserConfigFriends();
        userConfigBanned = new UserConfigBanned();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_config, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabLayout = getActivity().findViewById(R.id.tabsUserConfig);
        toolbar = getActivity().findViewById(R.id.toolBarUserConfig);
        toolbar.inflateMenu(R.menu.user_menu);
        navigationView = getActivity().findViewById(R.id.navigationUserConfig);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Funcionalidades.esconderTeclado(getActivity(),getContext(),toolbar);
                realizarAccionSeleccionada(menuItem);
                return true;
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Funcionalidades.esconderTeclado(getActivity(),getContext(),toolbar);
                realizarAccionSeleccionada(item);
                return true;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_configuration))) {
                    Funcionalidades.showSelectedFragment(R.id.containerUserConfig, getActivity().getSupportFragmentManager(), userConfigSettings);
                }
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_amigos))) {
                    Funcionalidades.showSelectedFragment(R.id.containerUserConfig, getActivity().getSupportFragmentManager(), userConfigFriends);
                }
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_bloqueados))) {
                    Funcionalidades.showSelectedFragment(R.id.containerUserConfig, getActivity().getSupportFragmentManager(), userConfigBanned);
                }
                Funcionalidades.esconderTeclado(getActivity(),getContext(),getView());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Funcionalidades.esconderTeclado(getActivity(),getContext(),getView());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Funcionalidades.esconderTeclado(getActivity(),getContext(),getView());
            }
        });

        Funcionalidades.showSelectedFragment(R.id.containerUserConfig, getActivity().getSupportFragmentManager(), userConfigSettings);
    }

    private void realizarAccionSeleccionada(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemUserMenuSave:
                guardarCambios();
                break;
            case R.id.itemUserMenuLogout:
                logout();
                break;
            case R.id.itemUserMenuDelete:
                eliminarCuentaDelUsuario();
                break;
        }
    }

    private void guardarCambios() {
        String passwordNew = userConfigSettings.getNewpass();
        String passwordNewRepeat = userConfigSettings.getRepeatpass();
        if (!passwordNew.equals(passwordNewRepeat)) {
            Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_passwords_diferentes),getContext());
            return;
        }

        String email = LoginActivity.user.getEmail();
        String nombreNew = userConfigSettings.getNombre().toUpperCase();
        String nombreOld = LoginActivity.user.getFirstName();
        if (!nombreNew.equals(nombreOld)) {
            actualizarNombreUsuarioBBDD(email,nombreNew);
        }

        String apellidosNew = userConfigSettings.getApellido().toUpperCase();
        String apellidosOld = LoginActivity.user.getLastName();
        if (!apellidosNew.equals(apellidosOld)) {
            actualizarApellidosUsuarioBBDD(email,apellidosNew);
        }

        String direccionNew = userConfigSettings.getDireccion();
        String direccionOld = LoginActivity.user.getAddress();
        if (!direccionNew.equals(direccionOld)) {
            actualizarDireccionUsuarioBBDD(direccionNew);
        }

        String generoNew = userConfigSettings.getGenero();
        String generoOld = LoginActivity.user.getGender();
        if (!generoNew.isEmpty() && !generoNew.equals(generoOld)) {
            actualizarGeneroUsuarioBBDD(generoNew);
        }

        if (!passwordNew.isEmpty()) {
            actualizarPasswordUsuarioBBDD(email,passwordNew);
        }

        Date fechaNew = userConfigSettings.getBirthdate();
        Date fechaOld = LoginActivity.user.getBirthday();
        if (fechaNew != null && fechaNew != fechaOld) {
            actualizarFechaNacimientoUsuarioBBDD(email,Funcionalidades.dateToString2(fechaNew));
        }

        if(userConfigSettings.getUri() != null){
            try {
                String type = getFileExtension(userConfigSettings.getUri());
                actualizarImagen(getBytes(userConfigSettings.getInputStream()) , type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_finalizados),getContext()); //He cambiado el menssaje para que sea más genérico falle o no falle el guardado.
    }

    private void logout() {
        getActivity().finish();
    }

    private void eliminarCuentaDelUsuario() {
        eliminararUsuarioBBDD(LoginActivity.user);
    }

//------------- FUNCIONES PARA CONECTAR CON LA BBDD DEL SERVIDOR -------------------------------------------------------------------------------------

    public void actualizarNombreUsuarioBBDD(String email,String nombre) {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.editFirstName("Bearer " + LoginActivity.token, nombre).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    try {
                        LoginActivity.user.setFirstName(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void actualizarApellidosUsuarioBBDD(String email,String apellidos) {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.editLastName("Bearer " + LoginActivity.token, apellidos).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    try {
                        LoginActivity.user.setLastName(response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void actualizarDireccionUsuarioBBDD(String direccion) {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.editAddress("Bearer " + LoginActivity.token, direccion).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    try {
                        LoginActivity.user.setAddress(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void actualizarGeneroUsuarioBBDD(String genero) {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.editGender("Bearer " + LoginActivity.token, genero).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    try {
                        LoginActivity.user.setGender(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void actualizarFechaNacimientoUsuarioBBDD(String email,String fecha) {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.putFechaNacimiento("Bearer " + LoginActivity.token, email, fecha).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    try {
                        String f = response.body().string();
                        LoginActivity.user.setBirthday(Funcionalidades.StringToDate(f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void actualizarPasswordUsuarioBBDD(String email,String password) {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.putPassword("Bearer " + LoginActivity.token, email, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    try {
                        LoginActivity.user.setPassword(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void eliminararUsuarioBBDD(User user) {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        service.borrarUsuario("Bearer " + LoginActivity.token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 204) {
                    Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_usuario_eliminado),getContext());
                    logout();
                }else{
                    Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_usuario_no_eliminado),getContext());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void actualizarImagen(byte[] bytes , String type) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),bytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file" , LoginActivity.user.getEmail().replace(".","") + "." + type ,requestFile);
        RETROFIT retrofit = new RETROFIT();

        retrofit.getAPIService().subirImagen("Bearer " + LoginActivity.token, body, requestFile, LoginActivity.user.getEmail()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        LoginActivity.user.setPicture(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    //------------- FUNCIONES PARA LAS IMAGENES -------------------------------------------------------------------------------------



    private byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
