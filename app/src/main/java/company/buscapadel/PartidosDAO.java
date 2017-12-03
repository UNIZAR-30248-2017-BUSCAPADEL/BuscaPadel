package company.buscapadel;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fuste on 21/11/17.
 */

public class PartidosDAO {

    public void getPartidos (final ServerCallBack callBack, final boolean firstTime) {
        //String url = "https://quiet-lowlands-92391.herokuapp.com/api/partidos";
        String url = "http://10.0.2.2:3000/api/partidos";
        //String url = "http://192.168.1.117:3000/api/partidos";

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
                    getPartidos(callBack, false);
                }
                else {
                    callBack.onError();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    public void postPartido (final String lugar, final String fecha, final String hora, final int nivel,
                             final int idJug, final ServerCallBack callBack, final boolean firstTime) {
        //String url = "https://quiet-lowlands-92391.herokuapp.com/api/partidos";
        String url = "http://10.0.2.2:3000/api/partidos";
        //String url = "http://192.168.1.117:3000/api/partidos";

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("lugar", lugar);
            jsonBody.put("fecha", fecha);
            jsonBody.put("hora", hora);
            jsonBody.put("nivel", nivel);
            jsonBody.put("fkIdJugador1", idJug);
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
                            postPartido(lugar, fecha, hora, nivel, idJug, callBack, false);
                        }
                        else {
                            callBack.onError();
                        }
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);


    }

    public void getPartido (final int id, final ServerCallBack callBack, final boolean firstTime) {
        //String url = "https://quiet-lowlands-92391.herokuapp.com/api/partidos";
        String url = "http://10.0.2.2:3000/api/partidos/";
        //String url = "http://192.168.1.117:3000/api/partidos/";
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
                    getPartido(id,callBack, false);
                }
                else {
                    callBack.onError();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    public void updatePartido (final int id, final JSONObject toUpdate, final ServerCallBack callBack, final boolean firstTime){
        //String url = "https://quiet-lowlands-92391.herokuapp.com/api/partidos";
        String url = "http://10.0.2.2:3000/api/partidos/";
        //String url = "http://192.168.1.117:3000/api/partidos/";
        url = url + String.valueOf(id);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, toUpdate,
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
                            updatePartido(id, toUpdate, callBack, false);
                        }
                        else {
                            callBack.onError();
                        }
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    public void eliminarPartido (final int id, final ServerCallBack callBack, final boolean firstTime){
        //String url = "https://quiet-lowlands-92391.herokuapp.com/api/partidos";
        String url = "http://10.0.2.2:3000/api/partidos/";
        //String url = "http://192.168.1.117:3000/api/partidos/";
        url = url + String.valueOf(id);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, url, null,
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
                            eliminarPartido(id, callBack, false);
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
