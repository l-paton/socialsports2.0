package tfg.dam.socialsports.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

import tfg.dam.socialsports.EventRate;
import tfg.dam.socialsports.Utils;
import tfg.dam.socialsports.LoginActivity;
import tfg.dam.socialsports.R;
import tfg.dam.socialsports.model.User;
import tfg.dam.socialsports.model.UserScore;

public class ListUsersAdapter extends ArrayAdapter<User> {

    private int layoutFila;
    private Context context;
    private boolean bloquearPuntuar;

    public ListUsersAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull User[] objects) {
        super(context, resource, textViewResourceId, objects);
        layoutFila = resource;
        this.context = context;
        bloquearPuntuar = true;
    }

    public ListUsersAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<User> objects) {
        super(context, resource, textViewResourceId, objects);
        layoutFila = resource;
        this.context = context;
        bloquearPuntuar = true;
    }

    public ListUsersAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<User> objects, boolean bloquear) {
        super(context, resource, textViewResourceId, objects);
        layoutFila = resource;
        this.context = context;
        bloquearPuntuar = bloquear;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);
        if (LoginActivity.user.getId() != user.getId() || bloquearPuntuar) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View fila = inflater.inflate(layoutFila, parent, false);

            user = getItem(position);
            RatingBar ratingBar = fila.findViewById(R.id.ratingItemUsuarioReputation);
            ratingBar.setIsIndicator(bloquearPuntuar);

            if (bloquearPuntuar) {
                Log.e("REPUTACION: ", String.valueOf(user.getReputationParticipant()));
                ratingBar.setRating(user.getReputationParticipant());
            }else {
                ratingBar.setNextFocusDownId(EventRate.listaPuntuaciones.size());
                UserScore puntuacion = new UserScore(LoginActivity.user.getId(),
                        user.getId(), Utils.eventSeleccionado.getId(),0f);
                EventRate.listaPuntuaciones.add(puntuacion);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        EventRate.listaPuntuaciones.get(ratingBar.getNextFocusDownId()).setScore(rating);
                    }
                });
            }
            TextView textNombre = fila.findViewById(R.id.textItemUsuarioNombre);
            textNombre.setText(user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());
            TextView textGenero = fila.findViewById(R.id.textItemUsuarioGenero);
            TextView textEdad = fila.findViewById(R.id.textItemUsuarioEdad);
            ImageView image = fila.findViewById(R.id.imageItemUsuario);

            if (user.getGender() != null)
                textGenero.setText(user.getGender().toUpperCase());
            else
                textGenero.setText(context.getResources().getString(R.string.undefined_gender).toUpperCase());
            int edad = -1;
            if (user.getBirthday() != null)
                edad = Utils.calcularEdad(user.getBirthday());
            if (edad != -1)
                textEdad.setText(edad + " " + context.getResources().getString(R.string.years_old).toUpperCase());
            else{
                textEdad.setText(context.getResources().getString(R.string.unknow_age).toUpperCase());
            }

            if(user.getPicture() != null && !user.getPicture().equals("")){
                Glide.with(getContext())
                        .load(user.getPicture().replace("localhost", "192.168.1.177"))
                        .into(image);

            }

            return fila;
        }
        return new View(getContext());
    }

}
