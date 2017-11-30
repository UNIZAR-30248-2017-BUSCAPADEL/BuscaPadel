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

import org.json.JSONArray;
import org.json.JSONObject;

public class iniciarSesion extends AppCompatActivity {

    private EditText correo;
    private EditText password;
    private Button button;

    private static final int ACTIVITY_MENU_PRINCIPAL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        final company.buscapadel.iniciarSesion local = this;
        correo = (EditText) findViewById(R.id.editText9);
        password = (EditText) findViewById(R.id.editText8);
        button = (Button) findViewById(R.id.button8);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String passwordText = password.getText().toString();
                final String correoText = correo.getText().toString();
                if (correoText.equals("") || passwordText.equals("")) {
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
                } else if (passwordText.length() < 8) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

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
                } else {
                    JugadoresDAO jugadoresDAO = new JugadoresDAO();
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
                    });
                }
            }
        });
    }

    private void restoApp(String correo, int id) {
        Intent i = new Intent(this, MenuPrincipal.class);
        i.putExtra("correo", correo);
        i.putExtra("id", id);
        startActivityForResult(i, ACTIVITY_MENU_PRINCIPAL);
    }
}
