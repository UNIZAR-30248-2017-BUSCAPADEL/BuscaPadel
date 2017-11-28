package company.buscapadel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class iniciarSesion extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        final company.buscapadel.iniciarSesion local = this;
        username = (EditText) findViewById(R.id.editText8);
        password = (EditText) findViewById(R.id.editText9);
        button = (Button) findViewById(R.id.button8);

        button.setOnClickListener(new View.OnClickListener() {
            String passwordText = password.getText().toString();
            String usernameText = username.getText().toString();
            public void onClick(View view) {
                if (usernameText.isEmpty() || passwordText.isEmpty()) {
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
                else if (passwordText.length() < 8) { //usuario o contraseña invalida CAMBIAR condicion!!!
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
                else if (1 != 0) {//usuario y contraseña validos?? CAMBIAR condicion!!!

                }
                // if username y password son correctos
                //iniciar sesion
                //else
                //error de contraseña o de usuario
            }
        });
    }
}
