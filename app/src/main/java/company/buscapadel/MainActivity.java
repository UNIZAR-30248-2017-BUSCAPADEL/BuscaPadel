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
import android.widget.Button;
import android.widget.EditText;
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
    private EditText hora;
    private Button crearButton;

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
        hora = (EditText) findViewById(R.id.editText4);
        crearButton = (Button) findViewById(R.id.button);

        crearButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                final String fechaText = fecha.getText().toString();
                final String horaText = hora.getText().toString();
                final String lugarText = lugar.getText().toString();
                String[] horaParts = horaText.split(":");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
                Date dateSelected = null;
                int hora = Integer.parseInt(horaParts[0]);
                int minutos = Integer.parseInt(horaParts[1]);
                boolean fechaIncorrecta = false;
                try {
                    dateSelected = simpleDateFormat.parse(fechaText);
                } catch (ParseException e) {
                    e.printStackTrace();
                    fechaIncorrecta = true;
                }
                Date today = Calendar.getInstance().getTime();

                if(fechaText.equals("") || horaText.equals("") || lugarText.equals(""))
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
                else if (fechaIncorrecta){
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
                else if (dateSelected.before(today) || superaElMes(today, dateSelected)) {
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
                } else if (hora < 9 || hora >= 21) {
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
                                        dlgAlert.setPositiveButton("OK", null);
                                        dlgAlert.setCancelable(true);
                                        dlgAlert.create().show();
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
                    }, true);
                }
            }
        });


    }

    public boolean superaElMes(Date time1, Date time2) {
        int daysApart = (int)((time2.getTime() - time1.getTime()) / (1000*60*60*24l));
        if (abs(daysApart) >= 30)
            return true;
        else
            return false;
    }
}
