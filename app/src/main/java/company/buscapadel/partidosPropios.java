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

public class partidosPropios extends AppCompatActivity {

    private String fecha;
    private String hora;
    private String lugar;
    private String numero;
    private JSONArray partidos;

    private ListView listView;
    private static Bundle extras;
    private int idSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidos_propios);

        Intent intent = getIntent();

        // Devuelve el id del usuario
        idSesion = intent.getIntExtra("id", 0);

        listView = (ListView) findViewById(R.id.list);

        fillData();
    }

    private void fillData() {
        PartidosDAO partidosDAO = new PartidosDAO();
        partidosDAO.getPartidos(new ServerCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                JSONArray partidos = parseResult(result);
                showList(partidos);
            }
        });
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
        } else {

            // Create an array to specify the fields we want to display in the list
            String[] from = new String[]{"fecha", "hora",
                    "lugar"};

            MatrixCursor partidoCursor = new MatrixCursor(
                    new String[]{"_id", "fecha", "hora", "lugar"});
            startManagingCursor(partidoCursor);

            for (int i = 0; i < result.length(); i++) {
                try {
                    JSONObject jsonObject = result.getJSONObject(i);
                    String fecha = (String) jsonObject.get("fecha");
                    fecha = fecha.substring(0, 9);
                    String hora = (String) jsonObject.get("hora");
                    String lugar = (String) jsonObject.get("lugar");

                    partidoCursor.addRow(new Object[]{i, fecha, hora,
                            lugar});
                } catch (Exception e) {
                    Log.d("Error", e.toString());
                }
            }

            // and an array of the fields we want to bind those fields to
            int[] to = new int[]{R.id.fecha, R.id.hora,
                    R.id.lugar,};

            // Now create an array adapter and set it to display using our row
            SimpleCursorAdapter partido =
                    new SimpleCursorAdapter(this, R.layout.row_partidos_propios, partidoCursor,
                            from, to);
            listView.setAdapter(partido);
        }
    }
}
