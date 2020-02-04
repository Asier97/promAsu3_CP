package com.example.promasu3_cp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

//    private static final int SOLICITUD_PERMISO_WRITE_CALL_LOG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Verifica permisos para Android 6.0+
            int permissionCheck = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_CALL_LOG);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 225);
            }
            permissionCheck = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_CALL_LOG);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALL_LOG}, 225);
            }
        }

    }

    public void leer(View view) {
        // EN EL MANIFEST SE HA AÑADIDO EL PERMISO DE LECTURA DE LLAMADAS
        String[] TIPO_LLAMADA = {"", "entrante", "saliente", "perdida"};
        TextView salida = (TextView) findViewById(R.id.salida);
        Uri llamadas = Uri.parse("content://call_log/calls");

        Cursor c = getContentResolver().query(llamadas, null, null, null, null);

        while (c.moveToNext()) {
            salida.append("\n" + DateFormat.format("dd/MM/yy k:mm (",
                    c.getLong(c.getColumnIndex(CallLog.Calls.DATE)))
                    + c.getString(c.getColumnIndex(CallLog.Calls.DURATION)) + ") "
                    + c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)) + ", "
                    + TIPO_LLAMADA[Integer.parseInt(
                    c.getString(c.getColumnIndex(CallLog.Calls.TYPE)))]);
        }
    }

    public void escribir(View view) {
        // EN EL MANIFEST SE HA AÑADIDO EL PERMISO DE ESCRITURA DE LLAMADAS
        EditText num = findViewById(R.id.num);
        EditText dur = findViewById(R.id.dur);

        ContentValues valores = new ContentValues();
        valores.put(CallLog.Calls.DATE, new Date().getTime());
        valores.put(CallLog.Calls.NUMBER, num.getText().toString());
        valores.put(CallLog.Calls.DURATION, dur.getText().toString());
        valores.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
        // Suprimimos error de que puede que no se hayan dado permisos
        @SuppressLint("MissingPermission")
        Uri nuevoElemento = getContentResolver().insert(CallLog.Calls.CONTENT_URI,valores);
    }

//    private void borrarLlamada() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
//            //Acciones a realizar cuando se dispone de permisos
//            getContentResolver().delete(CallLog.Calls.CONTENT_URI,
//                    "number ='555555555'", null);
//
//            Snackbar.make(view, "LLamadas borradasd del registro.",
//                    Snackbar.LENGTH_SHORT).show();
//
//        } else {
//            //Solicitamos permisos
//            solicitarPermiso(Manifest.permission.WRITE_CALL_LOG, "Sin el" +
//                    " permiso ADMINISTRAR LLAMADASno puedo borrar" +
//                    " llamadas del registro.",
//                    SOLICITUD_PERMISO_WRITE_CALL_LOG, this);
//        }
//    }
//
//    private static void solicitarPermiso(final String permiso,
//                                        String justificacion,
//                                        final int requestCode,
//                                        final Activity actividad) {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,permiso)) {
//
//            // Informamos al usuario para qué y por qué se necesitan los permisos
//            new AlertDialog.Builder(actividad)
//                    .setTitle("Solicitud de permiso")
//                    .setMessage(justificacion)
//                    .setPositiveButton("OK",
//                            new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog,
//                                            int whichButton) {
//
//                            ActivityCompat.requestPermissions(actividad,
//                                    new String[]{permiso}, requestCode);
//                        }
//                    }).show();
//        } else {
//            // Muestra el cuadro de dialogo para la solicitud de los permisos
//            // y registra el permiso según respuesta del usuario
//            ActivityCompat.requestPermissions(actividad,
//                    new String[]{permiso}, requestCode);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String[] permissions,
//                                           int[] grantResults) {
//
//        if (requestCode == SOLICITUD_PERMISO_WRITE_CALL_LOG){
//            if (grantResults.length >= 1 &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                borrarLlamada();
//            } else {
//                Toast.makeText(this, "Sin el permiso, no puedo realizar" +
//                        " la acción",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

}