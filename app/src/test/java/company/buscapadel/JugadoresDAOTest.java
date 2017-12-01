package company.buscapadel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by ivansantamaria on 30/11/17.
 */

public class JugadoresDAOTest {

    JugadoresDAO jugadoresDAO = new JugadoresDAO();
    @Test
    public void getJugadorTest() {
        jugadoresDAO.getJugador(5, new ServerCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    JSONObject jsonObject = result.getJSONObject(0);
                    String correo = (String) jsonObject.get("correo");
                    assertEquals("amet@cursusluctusipsum.com", correo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }
}
