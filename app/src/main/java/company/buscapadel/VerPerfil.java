package company.buscapadel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VerPerfil extends AppCompatActivity {

    private TextView nombre;
    private TextView nivel;
    private EditText nivelNuevo;
    private Button introducirNivel;
    private Button misPartidos;

    private int idSesion;

    private VerPerfil local;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        local = this;

        Intent intent = getIntent();

        // Devuelve el id del usuario
        idSesion = intent.getIntExtra("id", 0);

        nivel = (TextView) findViewById(R.id.textView22);
        nivelNuevo = (EditText) findViewById(R.id.editText7);
        introducirNivel = (Button) findViewById(R.id.button11);
        misPartidos = (Button) findViewById(R.id.button12);
        final Intent partidosPropios = new Intent(this, partidosPropios.class);
        partidosPropios.putExtra("id", idSesion);

        fillData();

        introducirNivel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nivelNuevoText = nivelNuevo.getText().toString();
                JugadoresDAO jugadoresDAO = new JugadoresDAO();
                jugadoresDAO.actualizarNivel(idSesion, Integer.parseInt(nivelNuevoText), new ServerCallBack() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(VerPerfil.this);

                        dlgAlert.setMessage("Nivel modificado correctamente");
                        dlgAlert.setTitle("Éxito");
                        dlgAlert.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
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
        });

        misPartidos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(partidosPropios);
            }
        });
    }

    private void fillData() {
        JugadoresDAO jugadoresDAO = new JugadoresDAO();
        jugadoresDAO.getJugador(idSesion, new ServerCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                showUser(result);
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

    private void showUser(JSONArray result) {
        try {
            JSONObject jsonObject = result.getJSONObject(0);
            int nivelUsuario = (int)jsonObject.get("nivel");
            nivel.setText(String.valueOf(nivelUsuario));
        } catch (JSONException e) {
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
        } catch (Exception e) {
            nivel.setText("Sin nivel introducido");
        }
    }


}
