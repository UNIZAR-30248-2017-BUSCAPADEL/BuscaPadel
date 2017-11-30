package company.buscapadel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

public class VerPerfil extends AppCompatActivity {

    private TextView nombre;
    private TextView nivel;
    private EditText nivelNuevo;
    private Button introducirNivel;
    private Button misPartidos;

    private int idSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        Intent intent = getIntent();

        // Devuelve el id del usuario
        idSesion = intent.getIntExtra("id", 0);

        nombre = (TextView) findViewById(R.id.textView12);
        nivel = (TextView) findViewById(R.id.textView22);
        nivelNuevo = (EditText) findViewById(R.id.editText7);
        introducirNivel = (Button) findViewById(R.id.button11);
        misPartidos = (Button) findViewById(R.id.button12);
        final Intent partidosPropios = new Intent(this, partidosPropios.class);

        fillData();

        introducirNivel.setOnClickListener(new View.OnClickListener() {
            String nivelNuevoText = nivelNuevo.getText().toString();
            public void onClick(View view) {
                //actualizar nivel
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
