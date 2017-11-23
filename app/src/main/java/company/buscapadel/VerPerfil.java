package company.buscapadel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

public class VerPerfil extends AppCompatActivity {

    private TextView nombre;
    private TextView nivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        nombre = (TextView) findViewById(R.id.textView9);
        nivel = (TextView) findViewById(R.id.textView10);
        //hora = (EditText) findViewById(R.id.textView11);

        fillData();
    }

    private void fillData() {
        JugadoresDAO jugadoresDAO = new JugadoresDAO();
        jugadoresDAO.getJugador(1, new ServerCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                showUser(result);
            }
        });
    }

    private void showUser(JSONArray result) {

    }


}
