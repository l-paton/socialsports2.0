package tfg.dam.socialsports.fragments.userconfig;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import tfg.dam.socialsports.Funcionalidades;
import tfg.dam.socialsports.IP;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;

public class UserConfigSettings extends Fragment {

    private TextView textEmail;
    private RatingBar ratingBarUser;
    private RatingBar ratingBarOrganizer;
    private EditText editNombre;
    private EditText editApellido;
    private EditText editDireccion;
    private EditText editNacimiento;
    private EditText editNewpass;
    private EditText editRepeatpass;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private Date birthdate;
    private DatePickerDialog dialogoCalendario;
    private Calendar newCalendar;
    private ImageView image;
    private Uri uri;
    private InputStream is;

    public UserConfigSettings() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_config_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textEmail = getActivity().findViewById(R.id.textUserConfigEmail);
        ratingBarUser = getActivity().findViewById(R.id.ratingUserConfigReputation);
        ratingBarOrganizer = getActivity().findViewById(R.id.ratingUserConfigOrganizer);
        editNombre = getActivity().findViewById(R.id.editUserConfigNombre);
        editApellido = getActivity().findViewById(R.id.editUserConfigApellidos);
        editDireccion = getActivity().findViewById(R.id.editUserConfigDireccion);
        editNacimiento = getActivity().findViewById(R.id.editUserBirthday);
        editNewpass = getActivity().findViewById(R.id.editUserConfigNewpass);
        editRepeatpass = getActivity().findViewById(R.id.editUserConfigRepeatpass);
        radioMale = getActivity().findViewById(R.id.radioUserConfigMale);
        radioFemale = getActivity().findViewById(R.id.radioUserConfigFemale);
        image = getActivity().findViewById(R.id.imageUserConfig);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        newCalendar = Calendar.getInstance();
        dialogoCalendario = new DatePickerDialog(getContext(), R.style.calenderDialogCustom, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);
                birthdate = newCalendar.getTime();
                editNacimiento.setText(Funcionalidades.dateToString(birthdate));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        editNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoCalendario.show();
                Funcionalidades.esconderTeclado(getActivity(),getContext(),v);
            }
        });

        textEmail.setText(LoginActivity.user.getEmail());
        ratingBarUser.setRating(LoginActivity.user.getUserScore());
        ratingBarOrganizer.setRating(LoginActivity.user.getOrganizerScore());
        editNombre.setText(LoginActivity.user.getFirstName());
        editApellido.setText(LoginActivity.user.getLastName());
        editDireccion.setText(LoginActivity.user.getAddress());
        editNacimiento.setText(Funcionalidades.dateToString(LoginActivity.user.getBirthday()));

        if(LoginActivity.user.getPicture() != null && !LoginActivity.user.getPicture().equals("")){
            Glide.with(getContext())
                    .load(LoginActivity.user.getPicture().replace("localhost", IP.getIp))
                    .into(image);
        }

        if (LoginActivity.user.getGender().toUpperCase().equals("MALE"))
            radioMale.setChecked(true);
        else if (LoginActivity.user.getGender().toUpperCase().equals("FEMALE"))
            radioFemale.setChecked(true);
        birthdate = LoginActivity.user.getBirthday();
    }

    public String getNombre() {
        return editNombre.getText().toString();
    }

    public String getApellido() {
        return editApellido.getText().toString();
    }

    public String getDireccion() {
        return editDireccion.getText().toString();
    }

    public String getNewpass() {
        return editNewpass.getText().toString();
    }

    public String getRepeatpass() {
        return editRepeatpass.getText().toString();
    }

    public String getGenero() {
        if (radioMale.isChecked())
            return "Hombre";
        else if (radioFemale.isChecked())
            return "Mujer";
        return "Otro";
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public Uri getUri(){ return uri; }

    public InputStream getInputStream(){ return is; }

    public void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK && requestCode == 100){
            try {
                uri = data.getData();
                is = getContext().getContentResolver().openInputStream(data.getData());
                image.setImageURI(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
