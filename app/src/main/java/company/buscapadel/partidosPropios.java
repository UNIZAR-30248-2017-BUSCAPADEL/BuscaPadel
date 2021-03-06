package company.buscapadel;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.NotificationManager;
import android.support.v7.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class partidosPropios extends AppCompatActivity {

    private static final int ELIMINAR = 1;
    private String fecha;
    private String hora;
    private String lugar;
    private String numero;
    private JSONArray partidos;
    private ArrayList<String> horas = new ArrayList<>();
    private ArrayList<String> fechas = new ArrayList<>();
    private String anyoDatos;
    private String mesDatos;
    private String diaDatos;
    private String horaDatos;
    private String minutosDatos;

    private Button eliminarBoton;

    private static final int ACTIVITY_RESULTADO_PARTIDO = 0;

    private ListView listView;
    private static Bundle extras;
    private int idSesion;
    private partidosPropios local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidos_propios);

        local = this;

        Intent intent = getIntent();

        // Devuelve el id del usuario
        idSesion = intent.getIntExtra("id", 0);

        listView = (ListView) findViewById(R.id.list2);

        registerForContextMenu(listView);

        fillData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.getItemAtPosition(position);
                try{
                    int idPartido = (int)partidos.getJSONObject(position)
                            .get("id");
                    resultadoPartido(idPartido);
                } catch (Exception e){
                    Log.d("Error:", e.toString());
                }

            }
        });

        MyTimerTask myTimerTask = new MyTimerTask();
        Timer timer = new Timer();

        timer.schedule(myTimerTask, 150000, 150000);
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            //comprobar si hay algun partido en 12 horas
            getPartidos();
            if (comprobarDoceHoras(fechas, horas)) {
                generateNotification(getApplicationContext(), "Tienes un partido en 12 horas");
            }
        }
    }

    private void generateNotification(Context context, String message) {
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        PendingIntent contentIntent = PendingIntent
                .getActivity(context, 0, new Intent(context, partidosPropios.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        notification = builder.setContentIntent(contentIntent).setSmallIcon(R.mipmap.logo_app)
                .setContentTitle("BuscaPadel").setContentText(message).build();

        notificationManager.notify((int) when, notification);
    }

    public void getPartidos(){
        PartidosDAO partidosDAO = new PartidosDAO();
        partidosDAO.getPartidos(new ServerCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                JSONArray partidos = parseResult(result);
                for (int i=0; i<partidos.length(); i++){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = partidos.getJSONObject(i);
                        String hora = (String) jsonObject.get("hora");
                        String fecha = (String) jsonObject.get("fecha");
                        horas.add(hora);
                        fechas.add(fecha);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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


//    public void sendNotification(View view) {
//        android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.logo_app).setContentTitle("BuscaPadel")
//                .setContentText("Tienes un partido en menos de 12 horas");
//    }

    private boolean comprobarDoceHoras(ArrayList<String> fechas, ArrayList<String> horas) {
        String fecha = "";
        String hora = "";
        Date today = Calendar.getInstance().getTime();
        Date date = null;
        for (int i = 0; i < fechas.size(); i++) {
            fecha = fechas.get(i);
            hora = horas.get(i);
            date = cogerFechaHora(fecha, hora);
            long diff = date.getTime() - today.getTime();
            long diffHours = diff / (60 * 60 * 1000) % 24;
            if (diffHours >= 0 && diffHours <= 12) {
                return true;
            }
        }
        return false;
    }

    private Date cogerFechaHora(String fecha, String hora) {
        String anyoDatos = "";
        String mesDatos = "";
        String diaDatos = "";
        String horaDatos = "";
        String minutosDatos = "";
        String anyo = fecha.substring(0,4);
        String mes = fecha.substring(5,7);
        String dia = fecha.substring(8,10);
        String horas = hora.substring(0,2);
        String minutos = hora.substring(3,5);
        anyoDatos += anyo;
        mesDatos += mes;
        diaDatos += dia;
        horaDatos += horas;
        minutosDatos += minutos;
//        mesDatos.concat(fecha.substring(5,6));
//        diaDatos.concat(fecha.substring(8,9));
//        horaDatos.concat(hora.substring(0,1));
//        minutosDatos.concat(hora.substring(3,4));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(anyoDatos),
                Integer.parseInt(mesDatos) - 1, Integer.parseInt(diaDatos),
                        Integer.parseInt(horaDatos), Integer.parseInt(minutosDatos));
        return calendar.getTime();
    }

    private void resultadoPartido(int idPartido) {
        Intent i = new Intent(this, ResultadoPartido.class);
        i.putExtra("idPartido", idPartido);
        i.putExtra("id", idSesion);
        startActivityForResult(i, ACTIVITY_RESULTADO_PARTIDO);
    }

    private void fillData() {
        PartidosDAO partidosDAO = new PartidosDAO();
        partidosDAO.getPartidos(new ServerCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                JSONArray partidos = parseResult(result);
                showList(partidos);
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

    private JSONArray parseResult(JSONArray result) {
        JSONArray response = new JSONArray();
        for (int i = 0; i < result.length(); i++) {
            try {
                JSONObject jsonObject = result.getJSONObject(i);
                int id1 = (int) jsonObject.get("fkIdJugador1");
                if (id1 == idSesion){
                    response.put(jsonObject);
                }
            } catch (Exception e){
                Log.d("Error: ", e.toString());
            }
        }
        return response;
    }

    private void showList(JSONArray result) {
        if (result.length() == 0) {
            TextView empty = (TextView) findViewById(R.id.empty);
            empty.setVisibility(View.VISIBLE);
        }

        partidos = result;

        // Create an array to specify the fields we want to display in the list
        String[] from = new String[]{"fecha", "hora",
                "lugar", "id"};

        MatrixCursor partidoCursor = new MatrixCursor(
                new String[]{"_id", "fecha", "hora", "lugar", "id"});
        startManagingCursor(partidoCursor);

        for (int i = 0; i < result.length(); i++) {
            try {
                JSONObject jsonObject = result.getJSONObject(i);
                String fecha = (String) jsonObject.get("fecha");
                fecha = fecha.substring(0, 10);
                if (fecha.contains("T")){
                    fecha = fecha.substring(0,9);
                }
                String hora = (String) jsonObject.get("hora");
                hora = hora.substring(0,5);
                String lugar = (String) jsonObject.get("lugar");
                int id = (int) jsonObject.get("id");

                partidoCursor.addRow(new Object[]{i, fecha, hora,
                        lugar, id});
            } catch (Exception e) {
                Log.d("Error", e.toString());
            }
        }

        // and an array of the fields we want to bind those fields to
        int[] to = new int[]{R.id.fecha, R.id.hora,
                R.id.lugar,R.id.id,};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter partido =
                new SimpleCursorAdapter(this, R.layout.row_partidos_propios, partidoCursor,
                        from, to);
        listView.setAdapter(partido);
    }

    /**
     * Rellena el menú contextual tras mantener pulsado una nota. Las acciones son:
     * borrar nota, editar nota, enviar nota por mail y enviar nota por sms.
     * @param menu menu contextual
     * @param menuInfo información del menú
     * @param v vista para desplegar el menú con una pulsación larga
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, ELIMINAR, Menu.NONE, "Eliminar partido");
    }

    /**
     * Controla los eventos generados por el menú contextual.
     * @param item elemento seleccionado
     * @return devuelve false para que el menu contextual continua de forma normal,
     * true para consumir el item aquí
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case ELIMINAR:
                AdapterView.AdapterContextMenuInfo info =
                        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                View row = info.targetView;
                String id = ((TextView) findViewById(R.id.id)).getText().toString();
                eliminarPartido(Integer.parseInt(id));
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void eliminarPartido(final long idPar) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

        dlgAlert.setMessage("¿Seguro que desea eliminar este partido?");
        dlgAlert.setTitle("Confirmación");
        dlgAlert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                PartidosDAO partidosDAO = new PartidosDAO();
                partidosDAO.eliminarPartido((int)idPar, new ServerCallBack() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(local);

                        dlgAlert.setMessage("Partido eliminado correctamente");
                        dlgAlert.setTitle("Éxito");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        fillData();
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
        dlgAlert.setNegativeButton("No", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}
