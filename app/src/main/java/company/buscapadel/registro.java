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

    private EditText username;
    private EditText password;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        final company.buscapadel.registro local = this;
        username = (EditText) findViewById(R.id.editText2);
        password = (EditText) findViewById(R.id.editText5);
        button = (Button) findViewById(R.id.button7);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String passwordText = password.getText().toString();
                String usernameText = username.getText().toString();
                if (usernameText.equals("") || passwordText.equals("")) {
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
                else {
                    JugadoresDAO jugadoresDAO = new JugadoresDAO();
                    jugadoresDAO.getJugadorRegistro(usernameText, new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONArray result) {
                            if (result.length() == 0){
                                //Crear usuario
                            }
                            else {

                                ((EditText) findViewById(R.id.editText2)).getText().clear();
                                ((EditText) findViewById(R.id.editText5)).getText().clear();
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
                    });
                }
                // if username es unico y password tiene 8 o mas caracteres
                //registro valido, registrar usuario
                //else if username no es unico
                //aviso de username no unico
                //else if password tiene menos de 8 caracteres
                //aviso de password menor de 8 caracteres
            }
        });

    }
}
