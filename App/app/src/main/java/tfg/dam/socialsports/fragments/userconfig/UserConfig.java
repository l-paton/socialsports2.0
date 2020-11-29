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

import tfg.dam.socialsports.retrofit.APIService;
import tfg.dam.socialsports.model.User;
import tfg.dam.socialsports.Utils;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;
import tfg.dam.socialsports.retrofit.RetrofitConnection;
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
                Utils.esconderTeclado(getActivity(),getContext(),toolbar);
                realizarAccionSeleccionada(menuItem);
                return true;
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Utils.esconderTeclado(getActivity(),getContext(),toolbar);
                realizarAccionSeleccionada(item);
                return true;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_configuration))) {
                    Utils.showSelectedFragment(R.id.containerUserConfig, getActivity().getSupportFragmentManager(), userConfigSettings);
                }
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_amigos))) {
                    Utils.showSelectedFragment(R.id.containerUserConfig, getActivity().getSupportFragmentManager(), userConfigFriends);
                }
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_bloqueados))) {
                    Utils.showSelectedFragment(R.id.containerUserConfig, getActivity().getSupportFragmentManager(), userConfigBanned);
                }
                Utils.esconderTeclado(getActivity(),getContext(),getView());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Utils.esconderTeclado(getActivity(),getContext(),getView());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Utils.esconderTeclado(getActivity(),getContext(),getView());
            }
        });

        Utils.showSelectedFragment(R.id.containerUserConfig, getActivity().getSupportFragmentManager(), userConfigSettings);
    }

    private void realizarAccionSeleccionada(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemUserMenuSave:
                saveChanges();
                break;
            case R.id.itemUserMenuLogout:
                logout();
                break;
            case R.id.itemUserMenuDelete:
                deleteUserAccount();
                break;
        }
    }

    private void saveChanges() {
        String passwordNew = userConfigSettings.getNewpass();
        String passwordNewRepeat = userConfigSettings.getRepeatpass();

        if (!passwordNew.equals(passwordNewRepeat)) {
            Utils.mostrarMensaje(getResources().getString(R.string.mensaje_passwords_diferentes),getContext());
            return;
        }

        String newName = userConfigSettings.getNombre();
        String oldName = LoginActivity.user.getFirstName();
        if (!newName.equals(oldName)) {
            editFirstName(newName);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String newLastName = userConfigSettings.getApellido();
        String oldLastName = LoginActivity.user.getLastName();
        if (!newLastName.equals(oldLastName)) {
            editLastName(newLastName);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String newAddress = userConfigSettings.getDireccion();
        String oldAddress = LoginActivity.user.getAddress();
        if (!newAddress.equals(oldAddress)) {
            editAddress(newAddress);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String gender = userConfigSettings.getGenero();
        String oldGender = LoginActivity.user.getGender();
        if (!gender.isEmpty() && !gender.equals(oldGender)) {
            editGenre(gender);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!passwordNew.isEmpty()) {
            updatePassword(passwordNew);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Date newBirthday = userConfigSettings.getBirthdate();
        Date oldBirthday = LoginActivity.user.getBirthday();
        if (newBirthday != null && newBirthday != oldBirthday) {
            editBirthDay(Utils.dateToString2(newBirthday));
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(userConfigSettings.getUri() != null){
            try {
                String type = getFileExtension(userConfigSettings.getUri());
                updateImage(getBytes(userConfigSettings.getInputStream()) , type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Utils.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_finalizados),getContext());
    }

    private void logout() {
        getActivity().finish();
    }

    private void deleteUserAccount() {
        deleteUser(LoginActivity.user);
    }

    public void editFirstName(final String name) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();

        service.editFirstName("Bearer " + LoginActivity.token, name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    LoginActivity.user.setFirstName(name);
                }
                else Utils.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void editLastName(final String lastName) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();

        service.editLastName("Bearer " + LoginActivity.token, lastName).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    LoginActivity.user.setLastName(lastName);
                }
                else Utils.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void editAddress(final String address) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();

        service.editAddress("Bearer " + LoginActivity.token, address).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    LoginActivity.user.setAddress(address);
                }
                else{
                    Utils.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void editGenre(final String genre) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();

        service.editGender("Bearer " + LoginActivity.token, genre).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    LoginActivity.user.setGender(genre);
                }
                else Utils.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void editBirthDay(final String birthday) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();

        service.editBirthday("Bearer " + LoginActivity.token, birthday).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    LoginActivity.user.setBirthday(Utils.StringToDate(birthday));
                }
                else Utils.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void updatePassword(String password) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();

        service.putPassword("Bearer " + LoginActivity.token, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200) {
                    try {
                        LoginActivity.user.setPassword(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else Utils.mostrarMensaje(getResources().getString(R.string.mensaje_cambios_no_guardados), getContext());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void deleteUser(User user) {
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();

        service.borrarUsuario("Bearer " + LoginActivity.token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 204) {
                    Utils.mostrarMensaje(getResources().getString(R.string.mensaje_usuario_eliminado),getContext());
                    logout();
                }else{
                    Utils.mostrarMensaje(getResources().getString(R.string.mensaje_usuario_no_eliminado),getContext());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void updateImage(byte[] bytes , String type) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),bytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file" , LoginActivity.user.getId() + "." + type ,requestFile);
        RetrofitConnection retrofit = new RetrofitConnection();

        retrofit.getAPIService().uploadImage("Bearer " + LoginActivity.token, body, requestFile).enqueue(new Callback<ResponseBody>() {
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
