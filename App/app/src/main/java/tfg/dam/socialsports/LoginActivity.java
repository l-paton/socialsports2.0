package tfg.dam.socialsports;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import tfg.dam.socialsports.Clases.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private ProgressBar loadingProgressBar;
    private RETROFIT retrofit;
    private APIService service;
    public static User user = null;
    public static String token = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        loadingProgressBar = findViewById(R.id.loading);
        retrofit = new RETROFIT();
        service = retrofit.getAPIService();

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcionalidades.cambiarColoresTexto((EditText) v, getApplication());
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcionalidades.cambiarColoresTexto((EditText) v, getApplication());
            }
        });

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                iniciarLogueo();
                return true;
            }
        });

        loginButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcionalidades.cambiarColoresBoton((Button) v, getApplication());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                v.setFocusableInTouchMode(false);
                Funcionalidades.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),v);
                iniciarLogueo();
            }
        });

        registerButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcionalidades.cambiarColoresBoton((Button) v, getApplication());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                v.setFocusableInTouchMode(false);
                Funcionalidades.esconderTeclado(getSystemService(INPUT_METHOD_SERVICE),v);
                iniciarRegistro();
            }
        });
    }

    private void iniciarLogueo() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        if (comprobarDatosLoginCorrectos(getResources().getString(R.string.action_sign_in_short))) {
            login(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }
    }

    private void iniciarRegistro() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        if (comprobarDatosLoginCorrectos(getResources().getString(R.string.action_register))) {
            signup(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }
    }

    private boolean comprobarDatosLoginCorrectos(String botonPulsado){
        if (emailEditText.getText().toString().equals("")) {
            Funcionalidades.mostrarMensaje(getResources().getString(R.string.usuario_incompleto)+" "+botonPulsado,this);
            return false;
        }
        else if (passwordEditText.getText().toString().equals("")) {
            Funcionalidades.mostrarMensaje(getResources().getString(R.string.password_incompleto)+" "+botonPulsado, this);
            return false;
        }
        return true;
    }

    private void cargarAplicacionUsuario(){
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        loadingProgressBar.setVisibility(View.GONE);
    }

    private void limpiarCajas() {
        emailEditText.setText("");
        passwordEditText.setText("");
    }

    public void login(String email, String password) {

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        Call<Object> login = service.signin(credentials);
        login.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if(response.code() == 200){
                    try {
                        JSONObject json = new JSONObject(new Gson().toJson(response.body()));
                        String user = json.getString("user");
                        String jsonToken = json.getString("accessToken");

                        LoginActivity.user = new Gson().fromJson(user, User.class);
                        token = new Gson().fromJson(jsonToken, String.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    user.inicializarValoresNulos();
                    cargarAplicacionUsuario();

                }else{
                    Funcionalidades.mostrarMensaje(getResources().getString(R.string.login_datos_incorrectos), getApplicationContext());
                    limpiarCajas();
                    loadingProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                loadingProgressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    public void signup(String email, String password) {

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        //NOMBRE, APELLIDOS, GENERO

        Call<User> registro = service.signup(credentials);

        registro.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int code = response.code();

                if (code == 201) {
                    String authorizationHeader = response.headers().get("Authorization");
                    token = authorizationHeader.substring("Bearer".length()).trim();
                    Funcionalidades.mostrarMensaje(getResources().getString(R.string.login_creado_nuevo_usuario), getApplicationContext());
                    user = response.body();
                    cargarAplicacionUsuario();
                } else if (code == 409) {
                    Funcionalidades.mostrarMensaje(getResources().getString(R.string.login_usuario_existe), getApplicationContext());
                    loadingProgressBar.setVisibility(View.GONE);
                    limpiarCajas();
                } else {
                    user = null;
                    loadingProgressBar.setVisibility(View.GONE);
                    Funcionalidades.mostrarMensaje(getResources().getString(R.string.login_error_nuevo_usuario), getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loadingProgressBar.setVisibility(View.GONE);
                Log.e("ONFAILURE", t.getMessage());
            }
        });
    }
}
