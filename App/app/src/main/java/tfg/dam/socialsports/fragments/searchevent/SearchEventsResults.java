package tfg.dam.socialsports.fragments.searchevent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import tfg.dam.socialsports.Clases.ListEventsAdapter;
import tfg.dam.socialsports.Clases.Event;
import tfg.dam.socialsports.EventSettings;
import tfg.dam.socialsports.Funcionalidades;
import tfg.dam.socialsports.R;

public class SearchEventsResults extends Fragment {

    private ListView listViewEventos;
    private ArrayList<Event> listaEventosFiltrados;

    public SearchEventsResults() {
        listaEventosFiltrados = new ArrayList<>();
    }

    public SearchEventsResults(ArrayList<Event> arrayList) {
        listaEventosFiltrados = arrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_events_results, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listViewEventos = getActivity().findViewById(R.id.listSearchEventsResults);
        if (listaEventosFiltrados != null)
            mostrarListaEventos(listaEventosFiltrados);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listaEventosFiltrados != null)
            mostrarListaEventos(listaEventosFiltrados);
    }

    private void mostrarListaEventos(final ArrayList<Event> arrayList)
    {
        ListEventsAdapter adapter = new ListEventsAdapter(getContext(), R.layout.item_lista_eventos,
                R.id.textItemEventoDeporte, arrayList);
        listViewEventos.setAdapter(adapter);
        listViewEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = listaEventosFiltrados.get(position);

                Intent intent = new Intent(getContext(), EventSettings.class);
                Funcionalidades.eventSeleccionado = event;
                startActivity(intent);

            }
        });
    }
}
