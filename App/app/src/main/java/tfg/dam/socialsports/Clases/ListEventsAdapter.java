package tfg.dam.socialsports.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import tfg.dam.socialsports.Funcionalidades;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;

public class ListEventsAdapter extends ArrayAdapter<Event> {

    private int layoutFila;
    private Context context;

    public ListEventsAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull Event[] objects) {
        super(context, resource, textViewResourceId, objects);
        layoutFila = resource;
        this.context = context;
    }

    public ListEventsAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Event> objects) {
        super(context, resource, textViewResourceId, objects);
        layoutFila = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View raw = inflater.inflate(layoutFila, parent, false);

        TextView sport = raw.findViewById(R.id.textItemEventoDeporte);
        TextView startAt = raw.findViewById(R.id.textItemEventoFecha);
        TextView price = raw.findViewById(R.id.textItemEventoPrecio);
        TextView organizer = raw.findViewById(R.id.textItemEventoOrganizador);
        TextView estado = raw.findViewById(R.id.textItemEventoEstado);
        Event event = getItem(position);

        sport.setText(event.getSport().toUpperCase());
        if (event.getStartDate() != null)
            startAt.setText(Funcionalidades.dateToString(event.getStartDate()));
        else
            startAt.setText(context.getResources().getString(R.string.sin_fecha));
        if (!event.isFinish()) {
            if (event.getOrganizer().getId() == LoginActivity.user.getId())
                organizer.setText(context.getResources().getString(R.string.organizer)+" "+context.getResources().getString(R.string.me));
            else
                organizer.setText(context.getResources().getString(R.string.organizer)+" "+ event.getOrganizer().getFirstName());
            if (event.getMaxParticipants() > 0 && event.getMaxParticipants() <= event.getParticipants().size()) {
                estado.setText(context.getResources().getString(R.string.full));
                estado.setTextColor(context.getResources().getColor(R.color.full));
            } else {
                estado.setText(context.getResources().getString(R.string.vacancies));
                estado.setTextColor(context.getResources().getColor(R.color.vacancies));
            }
            price.setText(String.valueOf(event.getPrice()) + "€");
        }
        else {
            price.setVisibility(View.GONE);
            organizer.setVisibility(View.GONE);
            estado.setVisibility(View.GONE);
        }

        return raw;
    }
}
