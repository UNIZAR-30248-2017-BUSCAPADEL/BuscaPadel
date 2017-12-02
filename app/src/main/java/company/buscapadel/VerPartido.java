package company.buscapadel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VerPartido extends AppCompatActivity {

    private TextView fecha;
    private TextView hora;
    private TextView lugar;
    private TextView nivel;
    private TextView numJugadoresText;
    private TextView fechaValor;
    private TextView horaValor;
    private TextView lugarValor;
    private TextView nivelValor;
    private TextView numJugadoresValor;
    private Button unirseBoton;

    private int idPartido;
    private JSONArray partido;
    private String fechaText;
    private String horaText;
    private String lugarText;
    private int nivelText;
    private String numeroText;

    private int nivelUsuario;

    private int idSesion;

    final company.buscapadel.VerPartido local = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_partido);

        Intent intent = getIntent();

        // Devuelve el id del partido
        idPartido = intent.getIntExtra("idPartido",0);
        idSesion = intent.getIntExtra("id",0);
        getPartido();

        fecha = (TextView) findViewById(R.id.textView3);
        hora = (TextView) findViewById(R.id.textView4);
        lugar = (TextView) findViewById(R.id.textView5);
        nivel = (TextView) findViewById(R.id.textView6);
    }

    private void datosPartido() {
        int num = 0;
        try {
            JSONObject jsonObject = partido.getJSONObject(0);
            int id = (int) jsonObject.get("id");
            fechaText = (String) jsonObject.get("fecha");
            fechaText = fechaText.substring(0, 10);
            if (fechaText.contains("T")){
                fechaText = fechaText.substring(0,9);
            }
            horaText = (String) jsonObject.get("hora");
            lugarText = (String) jsonObject.get("lugar");
            nivelText = (int) jsonObject.get("nivel");
            int id1 = (int) jsonObject.get("fkIdJugador1");
            num++;
            int id2 = (int) jsonObject.get("fkIdJugador2");
            num++;
            int id3 = (int) jsonObject.get("fkIdJugador3");
            num++;
            int id4 = (int) jsonObject.get("fkIdJugador4");
            num++;

        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
        numeroText = String.valueOf(num);
    }

    private void getPartido() {

        PartidosDAO partidosDAO = new PartidosDAO();
        partidosDAO.getPartido(idPartido, new ServerCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                partido = result;
                datosPartido();
                fillData();
                fillonClick();
            }
        }, true);
    }

    private void fillonClick() {
        JugadoresDAO jugadoresDAO = new JugadoresDAO();
        jugadoresDAO.getJugador(idSesion, new ServerCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    JSONObject jsonObject = result.getJSONObject(0);
                    nivelUsuario = (int) jsonObject.get("nivel");
                } catch (Exception e) {
                    nivelUsuario = 0;
                }
            }
        }, true);
        unirseBoton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (nivelUsuario == 0){
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("Debes introducir tu nivel previamente para poder unirte a partidos");
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
                else if (nivelUsuario > nivelText + 0.5 || nivelUsuario < nivelText - 0.5) {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("Nivel de partido no adecuado para usuario");
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
                else if (Integer.valueOf(numeroText) == 4) {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("Partido completo");
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
                    JSONObject aux = null;
                    String name = null;
                    try{
                        aux = partido.getJSONObject(0);
                        String fecha = (String)aux.get("fecha");
                        fecha = fecha.substring(0,10);
                        if (fechaText.contains("T")){
                            aux.put("fecha",fecha.substring(0,9));
                        }
                        else {
                            aux.put("fecha",fecha);
                        }
                        name = "fkIdJugador2";
                        int id = (int)aux.get("fkIdJugador2");
                        name = "fkIdJugador3";
                        id = (int)aux.get("fkIdJugador3");
                        name = "fkIdJugador4";
                        id = (int)aux.get("fkIdJugador4");
                    } catch (Exception e){
                        try {
                            aux.put(name, idSesion);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    PartidosDAO partidosDAO = new PartidosDAO();
                    partidosDAO.updatePartido(idPartido, aux, new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONArray result) {
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                            dlgAlert.setMessage("Te has unido al partido en: " + lugarText + " Fecha: " + fechaText +
                                    " Hora: " + horaText + " Nivel: " + nivelText);
                            dlgAlert.setTitle("Unido");
                            dlgAlert.setPositiveButton("OK", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                            datosPartido();
                            fillData();
                        }
                    }, true);

                }
            }
        });
    }

    private void fillData() {
        numJugadoresText = (TextView) findViewById(R.id.textView7);
        fechaValor = (TextView) findViewById(R.id.textView13);
        horaValor = (TextView) findViewById(R.id.textView14);
        lugarValor = (TextView) findViewById(R.id.textView15);
        nivelValor = (TextView) findViewById(R.id.textView16);
        numJugadoresValor = (TextView) findViewById(R.id.textView17);
        unirseBoton = (Button) findViewById(R.id.button2);

        fechaValor.setText(fechaText);
        horaValor.setText(horaText);
        lugarValor.setText(lugarText);

        nivelValor.setText(Integer.toString(nivelText));
        numJugadoresValor.setText(numeroText);
    }
}
