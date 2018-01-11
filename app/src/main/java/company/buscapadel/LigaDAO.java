package company.buscapadel;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by fuste on 9/01/18.
 */

public class LigaDAO {


    public void getLiga (final int id, final ServerCallBack callBack, final boolean firstTime){

        String url = "https://quiet-lowlands-92391.herokuapp.com/api/ligas/";
        //String url = "http://10.0.2.2:3000/api/ligas/";
        //String url = "http://192.168.1.117:3000/api/ligas/";
        url = url + String.valueOf(id);

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (firstTime && error instanceof TimeoutError) {
                    // note : may cause recursive invoke if always timeout.
                    getLiga(id,callBack, false);
                }
                else {
                    callBack.onError();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    public void getLigas (final ServerCallBack callBack, final boolean firstTime){

        String url = "https://quiet-lowlands-92391.herokuapp.com/api/ligas";
        //String url = "http://10.0.2.2:3000/api/ligas";
        //String url = "http://192.168.1.117:3000/api/ligas";

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (firstTime && error instanceof TimeoutError) {
                    // note : may cause recursive invoke if always timeout.
                    getLigas(callBack, false);
                }
                else {
                    callBack.onError();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    public void postLiga (final String nombre, final int numJug,
                             final ServerCallBack callBack, final boolean firstTime) {
        String url = "https://quiet-lowlands-92391.herokuapp.com/api/ligas";
        //String url = "http://10.0.2.2:3000/api/ligas";
        //String url = "http://192.168.1.117:3000/api/ligas";

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nombre", nombre);
            jsonBody.put("numJugadores", numJug);
        } catch (org.json.JSONException e) {
            Log.d("Error", e.toString());
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(jsonObject);
                        callBack.onSuccess(jsonArray);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (firstTime && volleyError instanceof TimeoutError) {
                            // note : may cause recursive invoke if always timeout.
                            postLiga(nombre,numJug,callBack, false);
                        }
                        else {
                            callBack.onError();
                        }
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);


    }
}
