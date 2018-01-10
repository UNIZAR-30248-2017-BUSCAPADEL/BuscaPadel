package company.buscapadel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class anadirJugador extends AppCompatActivity {

    private EditText correoEditText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_jugador);

        final company.buscapadel.anadirJugador local = this;

        final Intent intent = getIntent();
        final ArrayList<String> correos = intent.getStringArrayListExtra("correos");

        correoEditText = (EditText) findViewById(R.id.editText13);
        button = (Button) findViewById(R.id.button13);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String correo = correoEditText.getText().toString();
                if (correo.equals("")) {
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
                } else if (correos.contains(correo)) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("El usuario ya ha sido añadido a la liga");
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
                    JugadoresDAO jugadoresDAO = new JugadoresDAO();
                    jugadoresDAO.getJugadorRegistro(correo, new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONArray result) {
                            if (result.length() != 0) {
                                try {
                                    JSONObject jsonObject = result.getJSONObject(0);
                                    int idLiga = (int) jsonObject.get("fkIdLiga");

                                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                                    dlgAlert.setMessage("El jugador ya pertenece a una liga, prueba con otro correo");
                                    dlgAlert.setTitle("Érror...");
                                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((EditText) findViewById(R.id.editText13)).getText().clear();
                                        }
                                    });
                                    dlgAlert.setCancelable(true);
                                    dlgAlert.create().show();
                                } catch (Exception e) {
                                    intent.putExtra("correo", correo);
                                    setResult(Activity.RESULT_OK, intent);
                                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                                    dlgAlert.setMessage("Jugador añadido correctamente");
                                    dlgAlert.setTitle("Éxito...");
                                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    dlgAlert.setCancelable(true);
                                    dlgAlert.create().show();
                                }
                            } else {
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                                dlgAlert.setMessage("El correo no está registrado");
                                dlgAlert.setTitle("Érror...");
                                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
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
