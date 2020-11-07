package tfg.dam.socialsports.fragments.searchevent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import tfg.dam.socialsports.APIService;
import tfg.dam.socialsports.Clases.Event;
import tfg.dam.socialsports.Clases.EventFilter;
import tfg.dam.socialsports.Funcionalidades;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;
import tfg.dam.socialsports.RETROFIT;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchEvent extends Fragment {

    private TabLayout tabLayout;
    private BottomNavigationView navigationView;
    private ArrayList<Event> listaEventosFiltrados;
    private SearchEventsFilters searchEventsFilters;
    private SearchEventsResults searchEventsResults;
    private static EventFilter filtro;

    public SearchEvent() {
        listaEventosFiltrados = new ArrayList<>();
        searchEventsFilters = new SearchEventsFilters();
        searchEventsResults = new SearchEventsResults();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_event, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabLayout = getActivity().findViewById(R.id.tabsSearchEvents);
        navigationView = getActivity().findViewById(R.id.navigationSearchEvent);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_search_events_filters))) {
                    navigationView.setVisibility(View.VISIBLE);
                    Funcionalidades.showSelectedFragment(R.id.contenedorSearchEvent, getActivity().getSupportFragmentManager(), searchEventsFilters);
                }
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_search_events_results))) {
                    navigationView.setVisibility(View.GONE);
                    Funcionalidades.showSelectedFragment(R.id.contenedorSearchEvent, getActivity().getSupportFragmentManager(), searchEventsResults);
                }
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

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.itemSearchMenuSearch:
                        Funcionalidades.esconderTeclado(getActivity(),getContext(),tabLayout);
                        buscarEventosFiltrados();
                        break;
                    case R.id.itemSearchMenuClean:
                        Funcionalidades.esconderTeclado(getActivity(),getContext(),tabLayout);
                        searchEventsFilters = new SearchEventsFilters();
                        Funcionalidades.showSelectedFragment(R.id.contenedorSearchEvent,getActivity().getSupportFragmentManager(),searchEventsFilters);
                        break;
                }
                return true;
            }
        });

        Funcionalidades.showSelectedFragment(R.id.contenedorSearchEvent,getActivity().getSupportFragmentManager(),searchEventsFilters);
    }

    private void buscarEventosFiltrados() {
        filtro = obtenerFiltros();

        RETROFIT retrofit = new RETROFIT();
        APIService service = retrofit.getAPIService();

        Log.e("FILTRO: " , filtro.toString());

        if(filtro.getStartAt() == null && filtro.getReputation() == -1.0 && filtro.getAddress() == null && filtro.getSport().equals("") && filtro.getTime().equals("")){
            service.listEvents("Bearer " + LoginActivity.token).enqueue(new Callback<ArrayList<Event>>() {
                @Override
                public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                    try{
                        ArrayList<Event> listaEvents = response.body();
                        searchEventsResults = new SearchEventsResults(listaEvents);
                        tabLayout.getTabAt(1).select();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Event>> call, Throwable t) {

                }
            });
        }else {
            service.buscarEventos("Bearer " + LoginActivity.token,
                    filtro.getSport(),
                    Funcionalidades.dateToString2(filtro.getStartAt()),
                    filtro.getTime()).enqueue(new Callback<ArrayList<Event>>() {
                @Override
                public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                    if (response.isSuccessful()) {

                        ArrayList<Event> listaEvents = response.body();

                        int edad = Funcionalidades.calcularEdad(LoginActivity.user.getBirthday());

                        for (Event event : listaEvents) {

                            //eventosNoCumploRequisitoEdadMaxima
                            if (event.getRequirement().getMaxAge() != 0 && edad > event.getRequirement().getMaxAge()) {
                                listaEvents.remove(event);
                                break;
                            }
                            //eventosNoCumploRequisitoEdadMinima
                            if (event.getRequirement().getMinAge() != 0 && edad < event.getRequirement().getMinAge()) {
                                listaEvents.remove(event);
                                break;
                            }
                            //eventosNoCumploRequisitoDeGenero
                            if (event.getRequirement().getGender() != null && !event.getRequirement().getGender().equals("")) {
                                if (!event.getRequirement().getGender().equals(LoginActivity.user.getGender())) {
                                    listaEvents.remove(event);
                                    break;
                                }
                            }
                            //eventosNoCumploRequisitoReputacion
                            /*if (LoginActivity.user.getUserScore() < event.getRequirement().getReputation() && event.getRequirement().getReputation() != -1) {
                                listaEvents.remove(event);
                                break;
                            }*/

                            if (event.getMaxParticipants() != 0 && event.getMaxParticipants() <= event.getParticipants().size()) {
                                listaEvents.remove(event);
                            }
                        }

                        listaEventosFiltrados = listaEvents;
                        searchEventsResults = new SearchEventsResults(listaEventosFiltrados);
                        tabLayout.getTabAt(1).select();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private EventFilter obtenerFiltros() {
        EventFilter filtro = new EventFilter();
        filtro.setSport(searchEventsFilters.getEditDeporte());
        filtro.setStartAt(searchEventsFilters.getFecha());
        filtro.setTime(searchEventsFilters.getEditHora());
        if (searchEventsFilters.getCheckReputation())
            filtro.setReputation(searchEventsFilters.getRatingBarReputation());
        else
            filtro.setReputation(-1);
        return filtro;
    }
}
