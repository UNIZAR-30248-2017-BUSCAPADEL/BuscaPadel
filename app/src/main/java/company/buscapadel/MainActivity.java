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
                } else {
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
}
