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
 * Created by fuste on 8/01/18.
 */

public class ResultadosDAO {

    public void postResultado (final int idPartido, final int puntosE1S1, final int puntosE2S1, final int puntosE1S2,
                             final int puntosE2S2, final int puntosE1S3, final int puntosE2S3,
                             final ServerCallBack callBack, final boolean firstTime) {
        //String url = "https://quiet-lowlands-92391.herokuapp.com/api/resultados";
        String url = "http://10.0.2.2:3000/api/resultados";
        //String url = "http://192.168.1.117:3000/api/resultados";

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("fkIdPartido", idPartido);
            jsonBody.put("puntosEquipo1Set1", puntosE1S1);
            jsonBody.put("puntosEquipo1Set2", puntosE1S2);
            jsonBody.put("puntosEquipo1Set3", puntosE1S3);
            jsonBody.put("puntosEquipo2Set1", puntosE2S1);
            jsonBody.put("puntosEquipo2Set2", puntosE2S2);
            jsonBody.put("puntosEquipo2Set3", puntosE2S3);
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
                            postResultado(idPartido, puntosE1S1, puntosE1S2, puntosE1S3, puntosE2S1,
                                    puntosE2S2, puntosE2S3, callBack, false);
                        }
                        else {
                            callBack.onError();
                        }
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);


    }

    public void postResultado (final int idPartido, final int puntosE1S1, final int puntosE2S1, final int puntosE1S2,
                               final int puntosE2S2,
                               final ServerCallBack callBack, final boolean firstTime) {
        //String url = "https://quiet-lowlands-92391.herokuapp.com/api/resultados";
        String url = "http://10.0.2.2:3000/api/resultados";
        //String url = "http://192.168.1.117:3000/api/resultados";

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("fkIdPartido", idPartido);
            jsonBody.put("puntosEquipo1Set1", puntosE1S1);
            jsonBody.put("puntosEquipo1Set2", puntosE1S2);
            jsonBody.put("puntosEquipo2Set1", puntosE2S1);
            jsonBody.put("puntosEquipo2Set2", puntosE2S2);
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
                            postResultado(idPartido, puntosE1S1, puntosE1S2, puntosE2S1,
                                    puntosE2S2, callBack, false);
                        }
                        else {
                            callBack.onError();
                        }
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);


    }

    public void getResultado (final int idPartido, final ServerCallBack callBack, final boolean firstTime) {
        //String url = "https://quiet-lowlands-92391.herokuapp.com/api/resultados/";
        String url = "http://10.0.2.2:3000/api/resultados/";
        //String url = "http://192.168.1.117:3000/api/resultados/";
        url = url + String.valueOf(idPartido);

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
                    getResultado(idPartido,callBack, false);
                }
                else {
                    callBack.onError();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    public void getResultados (final ServerCallBack callBack, final boolean firstTime) {
        //String url = "https://quiet-lowlands-92391.herokuapp.com/api/resultados";
        String url = "http://10.0.2.2:3000/api/resultados";
        //String url = "http://192.168.1.117:3000/api/resultados";

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
                    getResultados(callBack, false);
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
