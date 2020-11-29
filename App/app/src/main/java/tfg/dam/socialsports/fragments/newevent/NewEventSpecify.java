package tfg.dam.socialsports.fragments.newevent;

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
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import tfg.dam.socialsports.Utils;
import tfg.dam.socialsports.R;

public class NewEventSpecify extends Fragment {

    private TextView textDescrip;
    private EditText editFecha;
    private EditText editHora;
    private EditText participantes;
    private EditText direccion;
    private CheckBox reserva;
    private EditText coste;
    private EditText precio;
    private CheckBox notParticipant;
    private EditText comentarios;
    private Date date = null;
    private Calendar newCalendar;
    private DatePickerDialog dialogoCalendario;
    private TimePickerDialog dialogoTime;
    private Time newTime = null;
    private int anio;
    private int mes;
    private int dia;
    private int hora;
    private int minutos;

    public NewEventSpecify() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_event_specify, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textDescrip = getActivity().findViewById(R.id.textSpecifyDescription);
        editFecha = getActivity().findViewById(R.id.editSpecifyDate);
        editHora = getActivity().findViewById(R.id.editSpecifyTime);
        participantes = getActivity().findViewById(R.id.editSpecifyParticipants);
        direccion = getActivity().findViewById(R.id.editSpecifyAddress);
        reserva = getActivity().findViewById(R.id.checkSpecifyReserved);
        coste = getActivity().findViewById(R.id.editSpecifyCost);
        precio = getActivity().findViewById(R.id.editSpecifyPrice);
        notParticipant = getActivity().findViewById(R.id.checkSpecifyNoParticipant);
        comentarios = getActivity().findViewById(R.id.editSpecifyComments);

        newCalendar = Calendar.getInstance();
        dialogoCalendario = new DatePickerDialog(getContext(), R.style.calenderDialogCustom, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth, 22, 0, 0);
                date = newCalendar.getTime();
                anio = year;
                mes = monthOfYear;
                dia = dayOfMonth;
                if ((fechaCorrecta(newCalendar.getTime()) && newTime == null) || getFechaEvento() != null) {
                    editFecha.setText(Utils.dateToString(date));
                }
                else {
                    Utils.mostrarMensaje("Fecha errónea.", getContext());
                    date = null;
                    editFecha.setText("");
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dialogoTime = new TimePickerDialog(getContext(), R.style.timerDialogCustom, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hora = hourOfDay;
                minutos = minute;
                newTime = new Time(0);
                if (date == null || (date != null && getFechaEvento() != null)) {
                    mostrarHoraSeleccionada();
                }
                else {
                    Utils.mostrarMensaje("Hora errónea.", getContext());
                    newTime = null;
                    editHora.setText("");
                }
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true);

        editFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoCalendario.show();
                Utils.esconderTeclado(getActivity(),getContext(),v);
            }
        });
        editHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoTime.show();
                Utils.esconderTeclado(getActivity(),getContext(),v);
            }
        });
        participantes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Utils.cambiarColoresTexto((EditText)v,getActivity().getApplication());
            }
        });
        direccion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Utils.cambiarColoresTexto((EditText)v,getActivity().getApplication());
            }
        });
        coste.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Utils.cambiarColoresTexto((EditText)v,getActivity().getApplication());
            }
        });
        precio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Utils.cambiarColoresTexto((EditText)v,getActivity().getApplication());
            }
        });
        comentarios.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Utils.cambiarColoresTexto((EditText)v,getActivity().getApplication());
            }
        });
        reserva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Utils.esconderTeclado(getActivity(),getContext(),getView());
                if (isChecked)
                    coste.setVisibility(View.VISIBLE);
                else
                    coste.setVisibility(View.INVISIBLE);
            }
        });
        notParticipant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Utils.esconderTeclado(getActivity(),getContext(),getView());
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

    private void mostrarHoraSeleccionada() {
        String cadHora = Integer.toString(hora);
        String cadMin = Integer.toString(minutos);
        if (cadHora.length()==1)
            cadHora="0"+cadHora;
        if (cadMin.length()==1)
            cadMin="0"+cadMin;
        editHora.setText(cadHora+":"+cadMin);
    }

    private boolean fechaCorrecta(Date fecha) {
        return fecha.after(new Date());
    }

    //Devuelve la fecha seleccionada, siempre que ésta sea una fecha futura.
    public Date getFechaEvento() {
        if (date!=null) {
            newCalendar.set(anio,mes,dia,0,0,0);
            if (newTime!=null) {
                    newCalendar.set(anio,mes,dia,hora,minutos);
            }
            date = newCalendar.getTime();
            if (date.after(new Date()))
                return date;
        }
        return null;
    }

    public String getHoraEvento() {
        return editHora.getText().toString().toUpperCase();
    }

    public int getNumParticipantes() {
        if (participantes.length()>0)
            return Integer.parseInt(participantes.getText().toString());
        return 0;
    }

    public String getDireccion() {
        return direccion.getText().toString().toUpperCase();
    }

    public float getCosteReserva() {
        if (reserva.isChecked()) {
            if (coste.length()>0)
                return Float.parseFloat(coste.getText().toString());
        }
        return 0;
    }

    public String getComentarios() {
        return comentarios.getText().toString().toUpperCase();
    }

    public boolean getElOrganizadorEsParticipante() {
        return (!notParticipant.isChecked());
    }
}
