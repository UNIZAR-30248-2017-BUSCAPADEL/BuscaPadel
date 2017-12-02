package company.buscapadel;

/**
 * Created by fuste on 21/11/17.
 */

import org.json.JSONArray;

public interface ServerCallBack{
    void onSuccess(JSONArray result);
    void onError();
}
