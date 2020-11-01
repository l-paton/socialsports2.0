package tfg.dam.socialsports;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialogo extends DialogFragment {

    AccionesDialogo ad;
    private String titulo;
    private String mensaje;

    private final static String TITULO_GUARDADO = "titulo";
    private final static String MENSAJE_GUARDADO = "mensaje";

    public Dialogo(){
    }

    public Dialogo(String titulo, String mensaje){
        this.titulo = titulo;
        this.mensaje = mensaje;
    }

    @Override
    public void onSaveInstanceState(Bundle estado) {
        super.onSaveInstanceState(estado);
        estado.putString(TITULO_GUARDADO,titulo);
        estado.putString(MENSAJE_GUARDADO,mensaje);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            titulo = savedInstanceState.getString(TITULO_GUARDADO);
            mensaje = savedInstanceState.getString(MENSAJE_GUARDADO);
        }
        AlertDialog.Builder alertaDialogo = new AlertDialog.Builder(getActivity());
        alertaDialogo.setMessage(mensaje);
        alertaDialogo.setTitle(titulo);
        alertaDialogo.setPositiveButton(getResources().getString(R.string.boton_aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.onDialogPositiveClick();
            }
        });
        alertaDialogo.setNegativeButton(getResources().getString(R.string.boton_cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.onDialogNegativeClick();
            }
        });
        return alertaDialogo.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        ad=(AccionesDialogo)context;
    }
}
