package company.buscapadel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ResultadoPartido extends AppCompatActivity {

    private int idSesion;
    private int idPartido;
    private int idResultado;

    private JSONArray partido;

    private String fechaText;
    private String horaText;
    private String lugarText;

    private TextView fecha;
    private TextView hora;
    private TextView lugar;

    private TextView textView11;
    private TextView textView21;
    private TextView textView12;
    private TextView textView22;
    private TextView textView13;
    private TextView textView23;

    private Spinner spinner11;
    private Spinner spinner21;
    private Spinner spinner12;
    private Spinner spinner22;
    private Spinner spinner13;
    private Spinner spinner23;


    private Button introducirResult;

    final company.buscapadel.ResultadoPartido local = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_partido);

        Intent intent = getIntent();

        // Devuelve el id del partido
        idPartido = intent.getIntExtra("idPartido",0);
        idSesion = intent.getIntExtra("id",0);

        fecha = (TextView) findViewById(R.id.textView13);
        hora = (TextView) findViewById(R.id.textView14);
        lugar = (TextView) findViewById(R.id.textView15);

        introducirResult = (Button) findViewById(R.id.button1);

        textView11 = (TextView) findViewById(R.id.textView23);
        textView21 = (TextView) findViewById(R.id.textView24);
        textView12 = (TextView) findViewById(R.id.textView25);
        textView22 = (TextView) findViewById(R.id.textView26);
        textView13 = (TextView) findViewById(R.id.textView27);
        textView23 = (TextView) findViewById(R.id.textView28);

        String[] result1 = {"0","1","2","3","4","5","6","7"};
        String[] result2 = {"-","0","1","2","3","4","5","6","7"};
        spinner11 = (Spinner) findViewById(R.id.spinner11);
        spinner11.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, result1));
        spinner21 = (Spinner) findViewById(R.id.spinner21);
        spinner21.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, result1));
        spinner12 = (Spinner) findViewById(R.id.spinner12);
        spinner12.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, result1));
        spinner22 = (Spinner) findViewById(R.id.spinner22);
        spinner22.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, result1));
        spinner13 = (Spinner) findViewById(R.id.spinner13);
        spinner13.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, result2));
        spinner23 = (Spinner) findViewById(R.id.spinner23);
        spinner23.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, result2));

        ResultadosDAO resultadosDAO = new ResultadosDAO();
        getResultados();
        getPartido();

        introducirResult.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String set11 = spinner11.getSelectedItem().toString();
                final String set21 = spinner21.getSelectedItem().toString();
                final String set12 = spinner12.getSelectedItem().toString();
                final String set22 = spinner22.getSelectedItem().toString();
                final String set13 = spinner13.getSelectedItem().toString();
                final String set23 = spinner23.getSelectedItem().toString();
                ResultadosDAO resultadosDAO = new ResultadosDAO();
                if (set13.equals("-") && set23.equals("-")){
                    resultadosDAO.postResultado(idPartido, Integer.parseInt(set11), Integer.parseInt(set21),
                            Integer.parseInt(set12), Integer.parseInt(set22), new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONArray result) {
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ResultadoPartido.this);

                            dlgAlert.setMessage("Resultado añadido correctamente");
                            dlgAlert.setTitle("Éxito");
                            dlgAlert.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            spinner11.setVisibility(View.GONE);
                                            spinner21.setVisibility(View.GONE);
                                            spinner12.setVisibility(View.GONE);
                                            spinner22.setVisibility(View.GONE);
                                            spinner13.setVisibility(View.GONE);
                                            spinner23.setVisibility(View.GONE);
                                            textView11.setText(set11);
                                            textView21.setText(set21);
                                            textView12.setText(set12);
                                            textView22.setText(set22);
                                            textView13.setText("-");
                                            textView23.setText("-");
                                            introducirResult.setVisibility(View.GONE);
                                            fillData();
                                        }
                                    });
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
                } else if (set13.equals("-") && !set23.equals("-") || !set13.equals("-") && set23.equals("-")){
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("Selecciona el resultado del tercer set, si no se ha jugado escoge dos -");
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
                    resultadosDAO.postResultado(idPartido, Integer.parseInt(set11), Integer.parseInt(set21),
                            Integer.parseInt(set12), Integer.parseInt(set22), Integer.parseInt(set13),
                            Integer.parseInt(set23), new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONArray result) {
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ResultadoPartido.this);

                            dlgAlert.setMessage("Resultado añadido correctamente");
                            dlgAlert.setTitle("Éxito");
                            dlgAlert.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            spinner11.setVisibility(View.GONE);
                                            spinner21.setVisibility(View.GONE);
                                            spinner12.setVisibility(View.GONE);
                                            spinner22.setVisibility(View.GONE);
                                            spinner13.setVisibility(View.GONE);
                                            spinner23.setVisibility(View.GONE);
                                            textView11.setText(set11);
                                            textView21.setText(set21);
                                            textView12.setText(set12);
                                            textView22.setText(set22);
                                            textView13.setText(set13);
                                            textView23.setText(set23);
                                            introducirResult.setVisibility(View.GONE);
                                            fillData();
                                        }
                                    });
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
                }
            }
        });


    }

    private void getResultados() {
        ResultadosDAO resultadosDAO = new ResultadosDAO();
        resultadosDAO.getResultados(new ServerCallBack(){

            @Override
            public void onSuccess(JSONArray result) {
                int i = 0;
                idResultado = 0;
                while (i<result.length() && idResultado == 0){
                    try {
                        JSONObject jsonObject = result.getJSONObject(i);
                        int idP = (int) jsonObject.get("fkIdPartido");
                        if (idP == idPartido){
                            idResultado = (int) jsonObject.get("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                compruebaResultado();
            }

            @Override
            public void onError() {

            }
        }, true);
    }

    private void compruebaResultado() {
        ResultadosDAO resultadosDAO = new ResultadosDAO();
        resultadosDAO.getResultado(idResultado, new ServerCallBack(){
            @Override
            public void onSuccess (JSONArray result){
                JSONObject jsonObject = null;
                int set11=1, set21=1, set12=1, set22=1, set13, set23;
                try {
                    jsonObject = result.getJSONObject(0);
                    spinner11.setVisibility(View.GONE);
                    spinner21.setVisibility(View.GONE);
                    spinner12.setVisibility(View.GONE);
                    spinner22.setVisibility(View.GONE);
                    spinner13.setVisibility(View.GONE);
                    spinner23.setVisibility(View.GONE);
                    introducirResult.setVisibility(View.GONE);
                    try {
                        set11 = (int) jsonObject.get("puntosEquipo1Set1");
                        set21 = (int) jsonObject.get("puntosEquipo2Set1");
                        set12 = (int) jsonObject.get("puntosEquipo1Set2");
                        set22 = (int) jsonObject.get("puntosEquipo2Set2");
                        set13 = (int) jsonObject.get("puntosEquipo1Set3");
                        set23 = (int) jsonObject.get("puntosEquipo2Set3");

                        textView11.setText(Integer.toString(set11));
                        textView21.setText(Integer.toString(set21));
                        textView12.setText(Integer.toString(set12));
                        textView22.setText(Integer.toString(set22));
                        textView13.setText(Integer.toString(set13));
                        textView23.setText(Integer.toString(set23));

                    } catch (Exception e) {
                        textView11.setText(Integer.toString(set11));
                        textView21.setText(Integer.toString(set21));
                        textView12.setText(Integer.toString(set12));
                        textView22.setText(Integer.toString(set22));
                        textView13.setText("-");
                        textView23.setText("-");
                    }
                } catch (JSONException e) {

                }

            }

            @Override
            public void onError (){
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

    private void getPartido() {

        PartidosDAO partidosDAO = new PartidosDAO();
        partidosDAO.getPartido(idPartido, new ServerCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                partido = result;
                datosPartido();
                fillData();
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

    private void fillData() {
        fecha.setText(fechaText);
        hora.setText(horaText);
        lugar.setText(lugarText);
    }

    private void datosPartido() {
        JSONObject jsonObject = null;
        try {
            jsonObject = partido.getJSONObject(0);

        int id = (int) jsonObject.get("id");
        fechaText = (String) jsonObject.get("fecha");
        fechaText = fechaText.substring(0, 10);
        if (fechaText.contains("T")){
            fechaText = fechaText.substring(0,9);
        }
        comprobarFecha();
        horaText = (String) jsonObject.get("hora");
        horaText = horaText.substring(0,5);
        lugarText = (String) jsonObject.get("lugar");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void comprobarFecha() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        Date dateSelected = null;
        try {
            dateSelected = simpleDateFormat.parse(fechaText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date today = Calendar.getInstance().getTime();

        if (dateSelected.after(today)){
            introducirResult.setVisibility(View.GONE);
            spinner11.setVisibility(View.GONE);
            spinner21.setVisibility(View.GONE);
            spinner12.setVisibility(View.GONE);
            spinner22.setVisibility(View.GONE);
            spinner13.setVisibility(View.GONE);
            spinner23.setVisibility(View.GONE);
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ResultadoPartido.this);

            dlgAlert.setMessage("No se puede añadir resultado porque la fecha es posterior a hoy");
            dlgAlert.setTitle("Error");
            dlgAlert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }
}
