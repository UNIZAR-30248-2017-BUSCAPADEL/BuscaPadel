package company.buscapadel;

import android.content.Intent;
import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class ListarPartidos extends AppCompatActivity {

    private String fecha;
    private String hora;
    private String lugar;
    private String numero;
    private JSONArray partidos;

    private static final int ACTIVITY_VER_PARTIDO = 0;

    private ListView listView;
    private static Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_partidos);

        listView = (ListView) findViewById(R.id.list);

        fillData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.getItemAtPosition(position);
                try{
                    int idPartido = (int)partidos.getJSONObject(position)
                            .get("id");
                    verPartido(idPartido);
                } catch (Exception e){
                    Log.d("Error:", e.toString());
                }

            }
        });
    }

    /**
     * Rellena la lista de partidos con la información de la base de datos.
     */
    private void fillData() {
        PartidosDAO partidosDAO = new PartidosDAO();
        partidosDAO.getPartidos(new ServerCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                showList(result);
            }
        });
    }

    private void showList(JSONArray result) {
        // Si no existen profesores se muestra mensaje
        if (result.length() == 0) {
            TextView empty = (TextView) findViewById(R.id.empty);
            empty.setVisibility(View.VISIBLE);
        } else {
            partidos = result;
            // Create an array to specify the fields we want to display in the list
            String[] from = new String[]{"fecha", "hora",
                    "lugar", "numero", "id"};

            MatrixCursor partidoCursor = new MatrixCursor(
                    new String[]{"_id", "fecha", "hora", "lugar",
                            "numero", "id"});
            startManagingCursor(partidoCursor);

            for (int i = 0; i < result.length(); i++) {
                try {
                    JSONObject jsonObject = result.getJSONObject(i);
                    int id = (int) jsonObject.get("id") ;
                    String fecha = (String) jsonObject.get("fecha");
                    fecha = fecha.substring(0,9);
                    String hora = (String) jsonObject.get("hora");
                    String lugar = (String) jsonObject.get("lugar");
                    int id1 = (int) jsonObject.get("fkIdJugador1");
                    int id2 = (int) jsonObject.get("fkIdJugador2");
                    int id3 = (int) jsonObject.get("fkIdJugador3");
                    int id4 = (int) jsonObject.get("fkIdJugador4");

                    int num = 0;
                    if (id1 != 0) {
                        num++;
                    }
                    if (id2 != 0) {
                        num++;
                    }
                    if (id3 != 0) {
                        num++;
                    }
                    if (id4 != 0) {
                        num++;
                    }
                    String numero = String.valueOf(num);

                    partidoCursor.addRow(new Object[]{i, fecha, hora,
                            lugar, numero, id});

                } catch (Exception e) {
                    Log.d("Error", e.toString());
                }
            }
            // and an array of the fields we want to bind those fields to
            int[] to = new int[]{R.id.fecha, R.id.hora,
                    R.id.lugar, R.id.numero, R.id.id,};

            // Now create an array adapter and set it to display using our row
            SimpleCursorAdapter partido =
                    new SimpleCursorAdapter(this, R.layout.row_listar_partidos, partidoCursor,
                            from, to);
            listView.setAdapter(partido);
        }
    }

    private void verPartido(int idPartido) {
        Intent i = new Intent(this, VerPartido.class);
        i.putExtra("idPartido", idPartido);
        startActivityForResult(i, ACTIVITY_VER_PARTIDO);
    }
}
