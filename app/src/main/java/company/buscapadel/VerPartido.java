package company.buscapadel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_partido);

        final company.buscapadel.VerPartido local = this;
        fecha = (TextView) findViewById(R.id.textView3);
        hora = (TextView) findViewById(R.id.textView4);
        lugar = (TextView) findViewById(R.id.textView5);
        nivel = (TextView) findViewById(R.id.textView6);
        numJugadoresText = (TextView) findViewById(R.id.textView7);
        fechaValor = (TextView) findViewById(R.id.textView13);
        horaValor = (TextView) findViewById(R.id.textView14);
        lugarValor = (TextView) findViewById(R.id.textView15);
        nivelValor = (TextView) findViewById(R.id.textView16);
        numJugadoresValor = (TextView) findViewById(R.id.textView17);
        unirseBoton = (Button) findViewById(R.id.button2);
        final String fechaText = fechaValor.getText().toString();
        final String horaText = horaValor.getText().toString();
        final String lugarText = lugarValor.getText().toString();

        final double nivelUsuario = 1.8; //coger nivel usuario de base de datos
        final int numJugadores = 3; //coger de BD
        final double nivelPartido = 2;

        nivelValor.setText(Double.toString(nivelUsuario));
        numJugadoresValor.setText(Integer.toString(numJugadores));

        unirseBoton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (nivelUsuario > nivelPartido + 0.5 || nivelUsuario < nivelPartido - 0.5) {
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
                else if (numJugadores == 4) {
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
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("Te has unido al partido en: " + lugarText + " Fecha: " + fechaText +
                            " Hora: " + horaText + " Nivel: " + nivelPartido);
                    dlgAlert.setTitle("Unido");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    //ACTUALIZAR BASE DE DATOS
//                    Intent i = new Intent(local, Modificar_Perfil_2.class);
//                    startActivityForResult(i, 0);
                }
            }
        });
    }
}
