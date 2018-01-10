package company.buscapadel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class creacionLigas extends AppCompatActivity {

    private EditText nombreEditText;
    private Button button;
    private Button addPlayer;
    private Spinner spinner;
    private String tipo;

    private ArrayList<String> correosJugadores = new ArrayList<>();

    private int idLiga;

    final company.buscapadel.creacionLigas local = this;

    private static final int ACTIVITY_ANADIR_JUGADOR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_ligas);

        nombreEditText = (EditText) findViewById(R.id.editText11);
        button = (Button) findViewById(R.id.button3);
        addPlayer = (Button) findViewById(R.id.button14);
        String[] spin = {"4","6","8","10","12","14","16","18","20"};
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spin));

        final Intent i = new Intent(this, anadirJugador.class);

        addPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String spinn = spinner.getSelectedItem().toString();
                final int numJugadores = Integer.parseInt(spinn);
                if (numJugadores > correosJugadores.size()) {
                    if (correosJugadores.size()==0) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);
                        dlgAlert.setMessage("Si deseas pertenecer a la liga, necesitas añadir tu correo también");
                        dlgAlert.setTitle("Información...");
                        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                i.putStringArrayListExtra("correos", correosJugadores);
                                startActivityForResult(i, ACTIVITY_ANADIR_JUGADOR);
                            }
                        });
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    } else {
                        i.putStringArrayListExtra("correos", correosJugadores);
                        startActivityForResult(i, ACTIVITY_ANADIR_JUGADOR);
                    }
                } else {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);
                    dlgAlert.setMessage("Ya has añadido el número de jugadores indicado: " + numJugadores);
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
            }});

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String nombre = nombreEditText.getText().toString();
                final String spinn = spinner.getSelectedItem().toString();
                final int numJugadores = Integer.parseInt(spinn);
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

                    dlgAlert.setMessage("La liga debe tener 4 ó más jugadores y ser un numero par");
                    dlgAlert.setTitle("Error...");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                } else if (numJugadores > 20) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                    dlgAlert.setMessage("La liga debe tener 20 ó menos jugadores y ser un numero par");
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
                    if (correosJugadores.size() != numJugadores){
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(local);

                        dlgAlert.setMessage("Faltan por añadir " + (numJugadores - correosJugadores.size()) +
                                " jugadores");
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
                        final LigaDAO ligaDAO = new LigaDAO();
                        ligaDAO.getLigas(new ServerCallBack() {
                            @Override
                            public void onSuccess(JSONArray result) {
                                int i=0;
                                boolean encontrada = false;
                                while (i<result.length() && !encontrada){
                                    try {
                                        JSONObject jsonObject = result.getJSONObject(i);
                                        String nom = (String) jsonObject.get("nombre");
                                        if (nom.equals(nombre)){
                                            encontrada = true;
                                        }
                                        i++;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (!encontrada) {
                                    //Crear liga
                                    ligaDAO.postLiga(nombre, numJugadores, new ServerCallBack() {
                                        @Override
                                        public void onSuccess(JSONArray result) {
                                            ligaDAO.getLigas(new ServerCallBack() {
                                                @Override
                                                public void onSuccess(JSONArray result) {
                                                    int i = 0;
                                                    boolean encontrada = false;
                                                    while (i < result.length() && !encontrada) {
                                                        JSONObject jsonObject = null;
                                                        try {
                                                            jsonObject = result.getJSONObject(i);
                                                            String nombre2 = (String) jsonObject.get("nombre");
                                                            if (nombre2.equals(nombre)) {
                                                                encontrada = true;
                                                                idLiga = (int) jsonObject.get("id");
                                                            }
                                                            i++;
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                                                    dlgAlert.setMessage("Liga creada correctamente");
                                                    dlgAlert.setTitle("Éxito...");
                                                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            finish();
                                                        }
                                                    });
                                                    dlgAlert.setCancelable(true);
                                                    dlgAlert.create().show();
                                                    //Añadir jugadores
                                                    final JugadoresDAO jugadoresDAO = new JugadoresDAO();
                                                    for (i = 0; i < correosJugadores.size(); i++) {
                                                        String jug = correosJugadores.get(i);
                                                        jugadoresDAO.getJugadorRegistro(jug, new ServerCallBack() {
                                                            @Override
                                                            public void onSuccess(JSONArray result) {
                                                                try {
                                                                    JSONObject jsonObject = result.getJSONObject(0);
                                                                    jsonObject.put("fkIdLiga", idLiga);
                                                                    jugadoresDAO.actualizarJugadorLiga(jsonObject, new ServerCallBack() {
                                                                        @Override
                                                                        public void onSuccess(JSONArray result) {

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
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
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
                                } else {
                                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                                    dlgAlert.setMessage("El nombre de la liga ya existe");
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
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_ANADIR_JUGADOR){
            if (resultCode == RESULT_OK){
                String resultado = data.getExtras().getString("correo");
                correosJugadores.add(resultado);
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                dlgAlert.setMessage("Has añadido " + correosJugadores.size() + " jugador/es");
                dlgAlert.setTitle("Información...");
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
    }
}
