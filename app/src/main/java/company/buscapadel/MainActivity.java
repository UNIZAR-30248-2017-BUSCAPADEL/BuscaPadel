package company.buscapadel;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.app.AlertDialog;
//import android.support.v7.app.AlertDialog;
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

    private TextView title;
    private EditText lugar;
    private EditText fecha;
    private EditText hora;
    private Button crearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final company.buscapadel.MainActivity local = this;
        lugar = (EditText) findViewById(R.id.editText);
        fecha = (EditText) findViewById(R.id.editText3);
        hora = (EditText) findViewById(R.id.editText4);
        crearButton = (Button) findViewById(R.id.button);

        crearButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String fechaText = fecha.getText().toString();
                String horaText = hora.getText().toString();
                String lugarText = lugar.getText().toString();
                String[] fechaParts = fechaText.split("/");
                int fechaDay = Integer.parseInt(fechaParts[0]);
                int fechaMonth = Integer.parseInt(fechaParts[1]);
                int fechaYear = Integer.parseInt(fechaParts[2]);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                Date dateSelected = null;


                try {
                    dateSelected = simpleDateFormat.parse(fechaText);
                } catch (ParseException e) {
                    e.printStackTrace();
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
                } else if ()
                else {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("Lugar: " + lugarText + " Fecha: " + fechaText +
                            " Hora: " + horaText);
                    dlgAlert.setTitle("Partido Creado");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    //ACTUALIZAR BASE DE DATOS
//                    Intent i = new Intent(local, Modificar_Perfil_2.class);
//                    startActivityForResult(i, 0);

                    PartidosDAO partidos = new PartidosDAO();
                    partidos.postPartido(lugarText, fechaText, horaText, "1", "1", new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONArray result) {
                            int cont = 0;//parse
                        }


                    });


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
