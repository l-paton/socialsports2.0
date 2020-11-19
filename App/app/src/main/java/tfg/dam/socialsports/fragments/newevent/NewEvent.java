package tfg.dam.socialsports.fragments.newevent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import tfg.dam.socialsports.APIService;
import tfg.dam.socialsports.Clases.Event;
import tfg.dam.socialsports.Clases.Requirement;
import tfg.dam.socialsports.Clases.User;
import tfg.dam.socialsports.Funcionalidades;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;
import tfg.dam.socialsports.RETROFIT;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewEvent extends Fragment {

    private NewEventDescription newEventDescription = new NewEventDescription();
    private NewEventSpecify newEventSpecify = new NewEventSpecify();
    private NewEventRequirements newEventRequirements = new NewEventRequirements();
    private NewEventInvite newEventInvite = new NewEventInvite();
    private TabLayout tabLayout;
    private Button createButton;
    private Button nextButton;
    private Button previousButton;
    private Event eventCreado = null;

    public NewEvent() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_event, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFriendList();
        createButton = getActivity().findViewById(R.id.createNewEventButton);
        nextButton = getActivity().findViewById(R.id.buttonNext);
        previousButton = getActivity().findViewById(R.id.buttonPrevious);
        tabLayout = getActivity().findViewById(R.id.tabs);
        Funcionalidades.showSelectedFragment(R.id.newEventContainer, getActivity().getSupportFragmentManager(), newEventSpecify);
        Funcionalidades.showSelectedFragment(R.id.newEventContainer, getActivity().getSupportFragmentManager(), newEventRequirements);
        Funcionalidades.showSelectedFragment(R.id.newEventContainer, getActivity().getSupportFragmentManager(), newEventInvite);

        createButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcionalidades.cambiarColoresBoton((Button)v,getActivity().getApplication());
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                Funcionalidades.esconderTeclado(getActivity(),getContext(),v);
                v.setFocusableInTouchMode(false);
                if (getParams())
                    crearEvento();
            }
        });

        nextButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcionalidades.cambiarColoresBotonSimple((Button) v,getActivity().getApplication());
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                Funcionalidades.esconderTeclado(getActivity(),getContext(),v);
                v.setFocusableInTouchMode(false);
                tabLayout.getTabAt(tabLayout.getSelectedTabPosition()+1).select();
            }
        });

        previousButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcionalidades.cambiarColoresBotonSimple((Button) v,getActivity().getApplication());
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                Funcionalidades.esconderTeclado(getActivity(),getContext(),v);
                v.setFocusableInTouchMode(false);
                tabLayout.getTabAt(tabLayout.getSelectedTabPosition()-1).select();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Funcionalidades.esconderTeclado(getActivity(),getContext(),getView());
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_description))) {
                    Funcionalidades.showSelectedFragment(R.id.newEventContainer,getActivity().getSupportFragmentManager(),newEventDescription);
                    activarBotonNext();
                }
                else if (tab.getText().toString().equals(getResources().getString(R.string.tab_specify))) {
                    Funcionalidades.showSelectedFragment(R.id.newEventContainer, getActivity().getSupportFragmentManager(), newEventSpecify);
                    activarBotonNextPrevious();
                }
                else if (tab.getText().toString().equals(getResources().getString(R.string.tab_requirements))) {
                    Funcionalidades.showSelectedFragment(R.id.newEventContainer, getActivity().getSupportFragmentManager(), newEventRequirements);
                    activarBotonNextPrevious();
                }
                else if (tab.getText().toString().equals(getResources().getString(R.string.tab_invite))) {
                    Funcionalidades.showSelectedFragment(R.id.newEventContainer, getActivity().getSupportFragmentManager(), newEventInvite);
                    activarBotonPrevious();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
        Funcionalidades.showSelectedFragment(R.id.newEventContainer, getActivity().getSupportFragmentManager(), newEventDescription);
        activarBotonNext();
    }

    private void activarBotonNext(){
        nextButton.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.GONE);
    }
    private void activarBotonPrevious(){
        nextButton.setVisibility(View.GONE);
        previousButton.setVisibility(View.VISIBLE);
    }
    private void activarBotonNextPrevious(){
        nextButton.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.VISIBLE);
    }

    private boolean getParams() {
        String sport,address,time,comments;
        Date startAt;
        int maxParticipants;
        float price;
        Requirement requirement = new Requirement(-1,-1,null,-1);

        sport = newEventDescription.getDeporte().toUpperCase();
        address = newEventDescription.getLocalidad().toUpperCase();

        if (sport.equals("") || address.equals("")) {
            Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_incomplete_data), getContext());
            return false;
        }

        startAt = newEventSpecify.getFechaEvento();
        time = newEventSpecify.getHoraEvento();
        maxParticipants = newEventSpecify.getNumParticipantes();
        price = newEventSpecify.getCosteReserva();
        comments = newEventSpecify.getComentarios();

        if(newEventRequirements.getEdadMinima() >= 0) requirement.setMinAge(newEventRequirements.getEdadMinima());
        if(newEventRequirements.getEdadMaxima() >= 0) requirement.setMaxAge(newEventRequirements.getEdadMaxima());
        if(newEventRequirements.getGenero() != null && !newEventRequirements.getGenero().equals("")) requirement.setGender(newEventRequirements.getGenero().toUpperCase());
        if(newEventRequirements.getReputacion() >= 0) requirement.setReputation(newEventRequirements.getReputacion());

        ArrayList<User> listaP = new ArrayList<>();
        /*if (newEventSpecify.getElOrganizadorEsParticipante())
            listaP.add(LoginActivity.user);*/

        for (User user : LoginActivity.user.getListaAmigos()) {
            if (newEventInvite.getListInviteFriends().contains(user.getId())) {
                listaP.add(user);
            }
        }

        eventCreado = new Event(sport, address, startAt, time, maxParticipants, price, comments, requirement);
        eventCreado.setParticipants(listaP);

        return true;
    }

    private void crearEvento() {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();
        Log.e("EVENTO: ", eventCreado.toString());
        service.createEvent("Bearer " + LoginActivity.token, eventCreado).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_evento_creado), getContext());
                    newEventDescription = new NewEventDescription();
                    newEventSpecify = new NewEventSpecify();
                    newEventRequirements = new NewEventRequirements();
                    newEventInvite = new NewEventInvite();
                    eventCreado = null;
                    Funcionalidades.showSelectedFragment(R.id.newEventContainer, getActivity().getSupportFragmentManager(), newEventSpecify);
                    Funcionalidades.showSelectedFragment(R.id.newEventContainer, getActivity().getSupportFragmentManager(), newEventRequirements);
                    Funcionalidades.showSelectedFragment(R.id.newEventContainer, getActivity().getSupportFragmentManager(), newEventInvite);
                    tabLayout.getTabAt(0).select();
                } else {
                    Funcionalidades.mostrarMensaje(getResources().getString(R.string.mensaje_error_evento_creado), getContext());
                    try {
                        Log.e("MENSAJE-ERROR:", response.errorBody().string());
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

    private void getFriendList() {
        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();
        service.friendList("Bearer " + LoginActivity.token).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {

                if (response.isSuccessful()) {
                    LoginActivity.user.setListaAmigos(new ArrayList<User>());
                    for (User user : response.body()) {
                        LoginActivity.user.getListaAmigos().add(user);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
