package company.buscapadel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PrimeraPantalla extends AppCompatActivity {

    private ImageButton registroButton;
    private ImageButton iniciarSesionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primera_pantalla);

        registroButton = (ImageButton) findViewById(R.id.imageButton2);
        iniciarSesionButton = (ImageButton) findViewById(R.id.imageButton);
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
