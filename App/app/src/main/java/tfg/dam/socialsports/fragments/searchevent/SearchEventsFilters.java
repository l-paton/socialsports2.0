package tfg.dam.socialsports.fragments.searchevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import tfg.dam.socialsports.Funcionalidades;
import tfg.dam.socialsports.R;

public class SearchEventsFilters extends Fragment {

    private DatePickerDialog dialogoCalendario;
    private Calendar newCalendar;
    private TimePickerDialog dialogoTime;
    private Date date;
    private int hora;
    private int minutos;
    private EditText editFecha;
    private EditText editHora;
    private EditText editDeporte;
    private EditText editLocalidad;
    private CheckBox checkReserva;
    private CheckBox checkReputation;
    private TextView textValorReputacion;
    private RatingBar ratingBarReputation;

    public SearchEventsFilters() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_events_filters, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editFecha = getActivity().findViewById(R.id.editFilterDate);
        editHora = getActivity().findViewById(R.id.editFilterTime);
        editDeporte = getActivity().findViewById(R.id.editFilterSport);
        editLocalidad = getActivity().findViewById(R.id.editFilterLocation);
        checkReserva = getActivity().findViewById(R.id.checkFilterReserved);
        checkReputation = getActivity().findViewById(R.id.checkFilterReputation);
        textValorReputacion = getActivity().findViewById(R.id.textFilterReputation);
        ratingBarReputation = getActivity().findViewById(R.id.ratingFilterReputation);
        ratingBarReputation.setEnabled(false);

        newCalendar = Calendar.getInstance();
        dialogoCalendario = new DatePickerDialog(getContext(), R.style.calenderDialogCustom, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);
                date = newCalendar.getTime();
                editFecha.setText(Funcionalidades.dateToString(date));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dialogoTime = new TimePickerDialog(getContext(), R.style.timerDialogCustom, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hora = hourOfDay;
                minutos = minute;
                mostrarHoraSeleccionada();
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true);

        editFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoCalendario.show();
                Funcionalidades.esconderTeclado(getActivity(),getContext(),v);
            }
        });
        editHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoTime.show();
                Funcionalidades.esconderTeclado(getActivity(),getContext(),v);
            }
        });
        editDeporte.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcionalidades.cambiarColoresTexto((EditText)v,getActivity().getApplication());
            }
        });
        editLocalidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcionalidades.cambiarColoresTexto((EditText)v,getActivity().getApplication());
            }
        });
        checkReserva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Funcionalidades.esconderTeclado(getActivity(),getContext(),getView());
            }
        });
        checkReputation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Funcionalidades.esconderTeclado(getActivity(),getContext(),getView());
                if (isChecked) {
                    ratingBarReputation.setEnabled(true);
                    textValorReputacion.setText(Float.toString(ratingBarReputation.getRating()));
                }
                else {
                    ratingBarReputation.setEnabled(false);
                    textValorReputacion.setText("");
                }
            }
        });
        ratingBarReputation.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Funcionalidades.esconderTeclado(getActivity(),getContext(),getView());
                textValorReputacion.setText(Float.toString(ratingBar.getRating()));
            }
        });
    }

    private void mostrarHoraSeleccionada() {
        String cadHora = Integer.toString(hora);
        String cadMin = Integer.toString(minutos);
        if (cadHora.length()==1)
            cadHora="0"+cadHora;
        if (cadMin.length()==1)
            cadMin="0"+cadMin;
        editHora.setText(cadHora+":"+cadMin);
    }

    public Date getFecha() {
        return this.date;
    }

    public String getEditHora() {
        return editHora.getText().toString();
    }

    public String getEditDeporte() {
        return editDeporte.getText().toString();
    }

    public String getEditLocalidad() {
        return editLocalidad.getText().toString();
    }

    public boolean getCheckReserva() {
        return checkReserva.isChecked();
    }

    public boolean getCheckReputation() {
        return checkReputation.isChecked();
    }

    public float getRatingBarReputation() {
        return ratingBarReputation.getRating();
    }
}
