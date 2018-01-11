package company.buscapadel;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fuste on 23/11/17.
 */

public class JugadoresDAO {

    public void getJugadores (final ServerCallBack callBack, final boolean firstTime){

        String url = "https://quiet-lowlands-92391.herokuapp.com/api/jugadores";
        //String url = "http://10.0.2.2:3000/api/jugadores";
        //String url = "http://192.168.1.117:3000/api/jugadores";

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
                    getJugadores(callBack, false);
                }
                else {
                    callBack.onError();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }

    public void getJugador (final int id, final ServerCallBack callBack, final boolean firstTime){

        String url = "https://quiet-lowlands-92391.herokuapp.com/api/jugadores/";
        //String url = "http://10.0.2.2:3000/api/jugadores/";
        //String url = "http://192.168.1.117:3000/api/jugadores/";
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
                    getJugador(id,callBack, false);
                }
                else {
                    callBack.onError();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    public void getJugadorRegistro (final String correo, final ServerCallBack callBack, final boolean firstTime){

        String url = "https://quiet-lowlands-92391.herokuapp.com/api/registro/";
        //String url = "http://10.0.2.2:3000/api/registro/";
        //String url = "http://192.168.1.117:3000/api/registro/";
        url = url + correo;

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ", error.getMessage());
                if (firstTime && error instanceof TimeoutError) {
                    // note : may cause recursive invoke if always timeout.
                    getJugadorRegistro(correo,callBack, false);
                }
                else {
                    callBack.onError();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    public void postJugador (final String correo, final String nombre, final String contrasena,
                             final ServerCallBack callBack, final boolean firstTime) {
        String url = "https://quiet-lowlands-92391.herokuapp.com/api/jugadores";
        //String url = "http://10.0.2.2:3000/api/jugadores";
        //String url = "http://192.168.1.117:3000/api/jugadores";

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("correo", correo);
            jsonBody.put("nombre", nombre);
            jsonBody.put("contrasena", contrasena);
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
                            postJugador(correo,nombre,contrasena,callBack, false);
                        }
                        else {
                            callBack.onError();
                        }
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);


    }

    public void actualizarNivel (final int id, final int nivel, final ServerCallBack callBack, final boolean firstTime){
        String url = "https://quiet-lowlands-92391.herokuapp.com/api/nivel/";
        //String url = "http://10.0.2.2:3000/api/nivel/";
        //String url = "http://192.168.1.117:3000/api/nivel/";
        url = url + id;

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nivel", nivel);
            jsonBody.put("id", id);
        } catch (org.json.JSONException e) {
            Log.d("Error", e.toString());
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
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
                            actualizarNivel(id,nivel,callBack, false);
                        }
                        else {
                            callBack.onError();
                        }
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);


    }

    public void actualizarJugador (final JSONObject jugador, final ServerCallBack callBack, final boolean firstTime){
        String url = "https://quiet-lowlands-92391.herokuapp.com/api/jugadores/";
        //String url = "http://10.0.2.2:3000/api/jugadores/";
        //String url = "http://192.168.1.117:3000/api/jugadores/";

        int id = 0;
        try {
            id = (int) jugador.get("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        url = url + id;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, jugador,
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
                            actualizarJugador(jugador,callBack, false);
                        }
                        else {
                            callBack.onError();
                        }
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);


    }

    public void actualizarJugadorLiga (final JSONObject jugador, final ServerCallBack callBack, final boolean firstTime){
        String url = "https://quiet-lowlands-92391.herokuapp.com/api/liga/";
        //String url = "http://10.0.2.2:3000/api/liga/";
        //String url = "http://192.168.1.117:3000/api/liga/";

        int id = 0;
        try {
            id = (int) jugador.get("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        url = url + id;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, jugador,
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
                            actualizarJugadorLiga(jugador,callBack, false);
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