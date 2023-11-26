package Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataStream {

    public DataStream() throws JSONException {

        JSONArray playerInformation = new JSONArray();
        JSONObject cars1 = new JSONObject();
        JSONObject cars2 = new JSONObject();
        JSONObject cars3 = new JSONObject();
        JSONObject cars4 = new JSONObject();
        JSONObject cars5 = new JSONObject();
        JSONObject cars6 = new JSONObject();
        JSONObject cars7 = new JSONObject();
        JSONObject cars8 = new JSONObject();

        JSONObject car1Data = new JSONObject();
        JSONObject car2Data = new JSONObject();
        JSONObject car3Data = new JSONObject();
        JSONObject car4Data = new JSONObject();
        JSONObject car5Data = new JSONObject();
        JSONObject car6Data = new JSONObject();
        JSONObject car7Data = new JSONObject();
        JSONObject car8Data = new JSONObject();


        car1Data.put("userid","id1");
        car1Data.put("sprite","car.jpg");
        car1Data.put("x",42);

        cars1.put("car1",car1Data);


    }
}
