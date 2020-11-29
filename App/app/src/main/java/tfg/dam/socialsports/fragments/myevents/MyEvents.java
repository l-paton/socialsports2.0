package tfg.dam.socialsports.fragments.myevents;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import tfg.dam.socialsports.retrofit.APIService;
import tfg.dam.socialsports.adapter.ListEventsAdapter;
import tfg.dam.socialsports.model.Event;
import tfg.dam.socialsports.EventRate;
import tfg.dam.socialsports.EventSettings;
import tfg.dam.socialsports.Utils;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;
import tfg.dam.socialsports.retrofit.RetrofitConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyEvents extends Fragment {

    private TabLayout tabLayout;
    private ListView listViewEventos;
    private ArrayList<Event> listaDeEvents = new ArrayList<>();

    public MyEvents() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_events, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabLayout = getActivity().findViewById(R.id.tabsMyEvents);
        listViewEventos = getActivity().findViewById(R.id.myEventsListView);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getResources().getString(R.string.tab_my_events_pend)))
                    eventosPendientes();

                if (tab.getText().toString().equals(getResources().getString(R.string.tab_my_events_final)))
                    eventosFinalizados();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
        tabLayout.getTabAt(0).select();
    }

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).select();
    }

    public void eventosPendientes(){
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();

        service.myEventsJoined("Bearer " + LoginActivity.token).enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if(response.isSuccessful()){
                    ArrayList<Event> listaEvents = response.body();
                    if(listaEvents != null) mostrarListaEventos(listaEvents);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void eventosFinalizados(){
        RetrofitConnection retrofit = new RetrofitConnection();
        APIService service = retrofit.getAPIService();

        service.myEventsJoinedFinished("Bearer " + LoginActivity.token).enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if(response.isSuccessful()){
                    ArrayList<Event> listaEvents = response.body();
                    if(listaEvents != null) mostrarListaEventos(listaEvents);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void mostrarListaEventos(ArrayList<Event> arrayList)
    {
        if (arrayList != null && getContext() != null) {
            ListEventsAdapter adapter = new ListEventsAdapter(getContext(), R.layout.item_lista_eventos,
                    R.id.textItemEventoDeporte, arrayList);
            listaDeEvents = arrayList;
            listViewEventos.setAdapter(adapter);
            listViewEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Event evento = listaDeEvents.get(position);

                    Intent intent = null;
                    if (!evento.isFinish())
                        intent = new Intent(getContext(), EventSettings.class);
                    else
                        intent = new Intent(getContext(), EventRate.class);
                    Utils.eventSeleccionado = evento;
                    startActivity(intent);
                }
            });
        }
    }
}