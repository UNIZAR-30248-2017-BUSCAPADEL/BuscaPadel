package company.buscapadel;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

/**
 * Created by fuste on 23/11/17.
 */

public class JugadoresDAO {

    public void getJugadores (final ServerCallBack callBack){

        //String url = "https://quiet-lowlands-92391.herokuapp.com/api/partidos";
        String url = "http://10.0.2.2:3000/api/jugadores";

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("yo", response.toString());
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("yo", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }

    public void getJugador (int id, final ServerCallBack callBack){

        //String url = "https://quiet-lowlands-92391.herokuapp.com/api/partidos";
        String url = "http://10.0.2.2:3000/api/jugadores/";
        url = url + String.valueOf(id);

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("yo", response.toString());
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("yo", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }
}