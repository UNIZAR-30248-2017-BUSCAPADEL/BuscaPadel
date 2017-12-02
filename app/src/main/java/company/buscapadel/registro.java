package company.buscapadel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class registro extends AppCompatActivity {

    private EditText correo;
    private EditText username;
    private EditText password;
    private EditText passwordRepetida;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        final company.buscapadel.registro local = this;
        correo = (EditText) findViewById(R.id.editText6);
        username = (EditText) findViewById(R.id.editText2);
        password = (EditText) findViewById(R.id.editText5);
        passwordRepetida = (EditText) findViewById(R.id.editText10);
        button = (Button) findViewById(R.id.button7);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                final String correoText = correo.getText().toString();
                final String passwordText = password.getText().toString();
                String passwordRepText = passwordRepetida.getText().toString();
                final String usernameText = username.getText().toString();
                if (usernameText.equals("") || passwordText.equals("") || correoText.equals("") || passwordRepText.equals("")) {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

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
                }
                else if (passwordText.length() < 8) {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("La contraseña debe tener 8 ó más caracteres");
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
                else if (!passwordText.equals(passwordRepText)) {

                    ((EditText) findViewById(R.id.editText5)).getText().clear();
                    ((EditText) findViewById(R.id.editText10)).getText().clear();
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("Las contraseñas deben coincidir");
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
                    final JugadoresDAO jugadoresDAO = new JugadoresDAO();
                    jugadoresDAO.getJugadorRegistro(correoText, new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONArray result) {
                            if (result.length() == 0){
                                //Crear usuario
                                jugadoresDAO.postJugador(correoText, usernameText, passwordText, new ServerCallBack() {
                                    @Override
                                    public void onSuccess(JSONArray result) {
                                        ((EditText) findViewById(R.id.editText2)).getText().clear();
                                        ((EditText) findViewById(R.id.editText5)).getText().clear();
                                        ((EditText) findViewById(R.id.editText6)).getText().clear();
                                        ((EditText) findViewById(R.id.editText10)).getText().clear();
                                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                                        dlgAlert.setMessage("Jugador creado correctamente");
                                        dlgAlert.setTitle("Éxito...");
                                        dlgAlert.setPositiveButton("OK", null);
                                        dlgAlert.setCancelable(true);
                                        dlgAlert.create().show();

                                        dlgAlert.setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
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
                            else {

                                ((EditText) findViewById(R.id.editText2)).getText().clear();
                                ((EditText) findViewById(R.id.editText5)).getText().clear();
                                ((EditText) findViewById(R.id.editText6)).getText().clear();
                                ((EditText) findViewById(R.id.editText10)).getText().clear();
                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                                dlgAlert.setMessage("El correo pertenece a un usuario ya existente");
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
                    }, true);
                }
            }
        });

    }
}
