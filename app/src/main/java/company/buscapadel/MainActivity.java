package company.buscapadel;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.app.AlertDialog;
//import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {

    private EditText lugar;
    private EditText fecha;
    private Spinner hora;
    private Button crearButton;
    private ComprobarDatos comprobarDatos = new ComprobarDatos();

    private int idSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        // Devuelve el id del usuario
        idSesion = intent.getIntExtra("id", 0);

        final company.buscapadel.MainActivity local = this;
        lugar = (EditText) findViewById(R.id.editText);
        fecha = (EditText) findViewById(R.id.editText3);
        String[] spin = {"9:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00",
                "18:00","19:00","20:00","21:00"};
        hora = (Spinner) findViewById(R.id.spinner);
        hora.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spin));
        crearButton = (Button) findViewById(R.id.button);

        crearButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                final String fechaText = fecha.getText().toString();
                final String horaText = hora.getSelectedItem().toString();
                final String lugarText = lugar.getText().toString();
                String[] horaParts = horaText.split(":");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
                Date dateSelected = null;
                int hora = Integer.parseInt(horaParts[0]);
                boolean esCorrecta = comprobarDatos.esFechaCorrecta(simpleDateFormat, fechaText);
                try {
                    dateSelected = simpleDateFormat.parse(fechaText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date today = Calendar.getInstance().getTime();

                int tipo = comprobarDatos.analizarDatos(fechaText, horaText, lugarText);

                if(tipo == 1)
                {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("Todos estos campos son obligatorios");
                    dlgAlert.setTitle("Error...");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                }
                else if (!esCorrecta){
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("El formato de fecha es incorrecto, usar yyyy-mm-dd");
                    dlgAlert.setTitle("Error...");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                }
                else if (tipo == 2) {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("La fecha debe ser posterior a hoy y no mas tarde de un mes");
                    dlgAlert.setTitle("Error...");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                } else if (tipo == 3) {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("La hora debe ser a partir de las 9 y hasta las 21");
                    dlgAlert.setTitle("Error...");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                }
                else {
                    JugadoresDAO jugadoresDAO = new JugadoresDAO();
                    jugadoresDAO.getJugador(idSesion, new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONArray result) {
                            try{
                                JSONObject jsonObject = result.getJSONObject(0);
                                int nivel = (int)jsonObject.get("nivel");
                                PartidosDAO partidos = new PartidosDAO();
                                partidos.postPartido(lugarText, fechaText, horaText, nivel, idSesion, new ServerCallBack() {
                                    @Override
                                    public void onSuccess(JSONArray result) {
                                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                                        dlgAlert.setMessage("Lugar: " + lugarText + " Fecha: " + fechaText +
                                                " Hora: " + horaText);
                                        dlgAlert.setTitle("Partido Creado");
                                        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        }   );
                                        dlgAlert.setCancelable(true);
                                        dlgAlert.create().show();
                                    }
                                    @Override
                                    public void onError() {
                                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                                        dlgAlert.setMessage("Problema con la base de datos, inténtelo más tarde");
                                        dlgAlert.setTitle("Error...");
                                        dlgAlert.setPositiveButton("OK", null);
                                        dlgAlert.setCancelable(true);
                                        dlgAlert.create().show();

                                        dlgAlert.setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                    }
                                }, true);
                            } catch (Exception e){
                                Log.d("Error: ", e.toString());
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                                dlgAlert.setMessage("Es necesario haber introducido el nivel " +
                                        "previamente para crear partidos");
                                dlgAlert.setTitle("Error...");
                                dlgAlert.setPositiveButton("OK", null);
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        }
                        @Override
                        public void onError() {
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                            dlgAlert.setMessage("Problema con la base de datos, inténtelo más tarde");
                            dlgAlert.setTitle("Error...");
                            dlgAlert.setPositiveButton("OK", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();

                            dlgAlert.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                        }
                    }, true);
                }
            }
        });


    }


}
