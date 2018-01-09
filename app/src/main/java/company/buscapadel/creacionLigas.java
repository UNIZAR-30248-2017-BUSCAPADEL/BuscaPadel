package company.buscapadel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

public class creacionLigas extends AppCompatActivity {

    private EditText nombreEditText;
    private EditText numJugadoresEditText;
    private ToggleButton toggleButton;
    private Button button;
    private String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_ligas);

        final company.buscapadel.creacionLigas local = this;
        nombreEditText = (EditText) findViewById(R.id.editText11);
        numJugadoresEditText = (EditText) findViewById(R.id.editText12);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        button = (Button) findViewById(R.id.button12);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tipo = "torneo";
                } else {
                    tipo = "liga";
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String nombre = nombreEditText.getText().toString();
                final int numJugadores = Integer.parseInt(numJugadoresEditText.getText().toString());
                if (nombre.equals("") || numJugadores == 0) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("No puede haber campos vacíos");
                    dlgAlert.setTitle("Error...");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                } else if (numJugadores < 4 || numJugadores % 2 != 0) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("La liga/torneo debe tener 4 ó más jugadores y ser un numero par");
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
                    //BASE DE DATOS
                    /*JugadoresDAO jugadoresDAO = new JugadoresDAO();
                    jugadoresDAO.getJugadorRegistro(correoText, new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONArray result) {
                            if (result.length()!=0){
                                try {
                                    JSONObject jsonObject = result.getJSONObject(0);
                                    String contrasena = (String) jsonObject.get("contrasena");
                                    int id = (int) jsonObject.get("id");
                                    if (contrasena.equals(passwordText)) {
                                        restoApp(correoText, id);
                                    } else {

                                        ((EditText) findViewById(R.id.editText8)).getText().clear();
                                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                                        dlgAlert.setMessage("El correo o la contraseña son incorrectos");
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
                                } catch (Exception e) {
                                    Log.d("Error:", e.toString());
                                }
                            }
                            else {
                                ((EditText) findViewById(R.id.editText8)).getText().clear();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                                dlgAlert.setMessage("El correo no ha sido registrado");
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
                    }, true);*/
                }
            }
        });
    }
}
