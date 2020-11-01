package tfg.dam.socialsports.fragments.newevent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;

public class NewEventInvite extends Fragment {

    private ArrayList<String> listaInvitarAmigos;
    private TextView textDescrip;
    private LinearLayout viewListaAmigos;
    private BottomNavigationView navigationView;

    public NewEventInvite() {
        listaInvitarAmigos = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_event_invite, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textDescrip = getActivity().findViewById(R.id.textInviteDescription);
        viewListaAmigos = getActivity().findViewById(R.id.viewListaAmigos);
        navigationView = getActivity().findViewById(R.id.navigationNewEventInvite);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.itemInviteAllMenu:
                        seleccionarTodosAmigos(true);
                        break;
                    case R.id.itemInviteNoneMenu:
                        seleccionarTodosAmigos(false);
                        break;
                }
                return true;
            }
        });
        for (int i = 0; i< LoginActivity.user.getListaAmigos().size(); i++) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setId(i);
            checkBox.setText(LoginActivity.user.getListaAmigos().get(i).getFirstName());
            checkBox.setHint(LoginActivity.user.getListaAmigos().get(i).getEmail());
            checkBox.setTextColor(getResources().getColor(R.color.colorElements));
            checkBox.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            checkBox.setButtonTintList(getResources().getColorStateList(R.color.colorElements));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        buttonView.setButtonTintList(getResources().getColorStateList(R.color.colorAccent));
                        if (!listaInvitarAmigos.contains(buttonView.getHint().toString()))
                            listaInvitarAmigos.add(buttonView.getHint().toString());
                    }
                    else {
                        buttonView.setButtonTintList(getResources().getColorStateList(R.color.colorElements));
                        if (listaInvitarAmigos.contains(buttonView.getHint().toString()))
                            listaInvitarAmigos.remove(buttonView.getHint().toString());
                    }
                }
            });
            viewListaAmigos.addView(checkBox);
        }
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

    public void seleccionarTodosAmigos(boolean selec) {
        for (int i = 0; i<LoginActivity.user.getListaAmigos().size(); i++) {
            CheckBox checkBox = getActivity().findViewById(i);
            checkBox.setChecked(selec);
            if (selec)
                checkBox.setButtonTintList(getResources().getColorStateList(R.color.colorAccent));
            else
                checkBox.setButtonTintList(getResources().getColorStateList(R.color.colorElements));
        }
    }

    //Devuelve la lista de los email pertenecientes a los amigos seleccionados para invitar al evento.
    public ArrayList<String> getListaInvitarAmigos() {
        return listaInvitarAmigos;
    }
}
