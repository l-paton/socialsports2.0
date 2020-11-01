package tfg.dam.socialsports.fragments.newevent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import tfg.dam.socialsports.Funcionalidades;
import tfg.dam.socialsports.R;


public class NewEventDescription extends Fragment {

    private TextView textDescrip;
    private EditText editSport;
    private EditText editLocation;

    public NewEventDescription() {    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_event_description, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textDescrip = getActivity().findViewById(R.id.textDescriptionDescription);
        editSport = getActivity().findViewById(R.id.editDescriptionSport);
        editLocation = getActivity().findViewById(R.id.editDescriptionLocation);

        editSport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcionalidades.cambiarColoresTexto((EditText)v,getActivity().getApplication());
            }
        });
        editLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcionalidades.cambiarColoresTexto((EditText)v,getActivity().getApplication());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        textDescrip.setFocusable(true);
        textDescrip.setFocusableInTouchMode(true);
        textDescrip.requestFocus();
        textDescrip.setFocusable(false);
        textDescrip.setFocusableInTouchMode(false);
    }

    public String getDeporte() {
        return editSport.getText().toString().toUpperCase();
    }

    public String getLocalidad() {
        return editLocation.getText().toString().toUpperCase();
    }
}
