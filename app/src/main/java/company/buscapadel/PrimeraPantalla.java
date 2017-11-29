package company.buscapadel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrimeraPantalla extends AppCompatActivity {

    private Button registroButton;
    private Button iniciarSesionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primera_pantalla);

        registroButton = (Button) findViewById(R.id.button9);
        iniciarSesionButton = (Button) findViewById(R.id.button10);
        final Intent iniciar = new Intent(this, iniciarSesion.class);
        final Intent registrar = new Intent(this, registro.class);

        registroButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(registrar);
            }
        });

        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(iniciar);
            }
        });
    }
}
