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
        correo = (EditText) findViewById(R.id.editText8);
        password = (EditText) findViewById(R.id.editText9);
        button = (Button) findViewById(R.id.button8);

        button.setOnClickListener(new View.OnClickListener() {
            String passwordText = password.getText().toString();
            String correoText = correo.getText().toString();
            public void onClick(View view) {
                if (correoText.isEmpty() || passwordText.isEmpty()) {
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
                } else if (passwordText.length() < 8) { //usuario o contraseña invalida CAMBIAR condicion!!!
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
                            try {
                                JSONObject jsonObject = result.getJSONObject(0);
                                String contrasena = (String) jsonObject.get("contrasena");
                                if (contrasena.equals(passwordText)) {//usuario y contraseña validos?? CAMBIAR condicion!!!
                                   restoApp(correoText);
                                } else {
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
                    });
                }
            }
        });
    }

    private void restoApp(String correo) {
        Intent i = new Intent(this, MenuPrincipal.class);
        i.putExtra("correo", correo);
        startActivityForResult(i, ACTIVITY_MENU_PRINCIPAL);
    }
}
