package Road;

import Connection.Client;
import Helpers.Util;
import Road.Car;
import Road.CustomSprite;
import Road.RoadBuilder;
import Screens.GameScreen;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Multiplayerfunktions {

    public Multiplayerfunktions(GameScreen gameScreen) {this.gameScreen=gameScreen;this.userID=gameScreen.getUserID();}
    GameScreen gameScreen;
    private String userID;


    public void placeSprites() {
        ArrayList<CustomSprite> sprites = new ArrayList<>();
        try {
            // Iteriere durch jedes JSON-Objekt im Array
            for (int i = 0; i < Client.jsonArrayAssets.length(); i++) {
                JSONObject jsonObj = Client.jsonArrayAssets.getJSONObject(i);

                // Greife auf die Werte der Schlüssel zu
                float offset = Float.parseFloat(jsonObj.getString("side"));
                double pos = Double.parseDouble(jsonObj.getString("pos"));
                String texture = jsonObj.getString("model");
                CustomSprite cs = new CustomSprite(texture, offset, pos);
                sprites.add(cs);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RoadBuilder.addSprites(sprites.toArray(sprites.toArray(CustomSprite[]::new)));
    }
    public void startPosition() {
        try {
            // Iteriere durch jedes JSON-Objekt im Array
            for (int i = 0; i < Client.jsonArrayUpdatePos.length(); i++) {
                JSONObject jsonObj = Client.jsonArrayUpdatePos.getJSONObject(i);

                // Greife auf die Werte der Schlüssel zu
                float offset = Float.parseFloat(jsonObj.getString("offset"));
                double pos = Double.parseDouble(jsonObj.getString("pos"));
                String id = jsonObj.getString("id");
                if (id.equals(userID)) {
                    gameScreen.setCameraPosition(pos);
                    gameScreen.setPlayerX(offset);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param newCars
     */
    public void updateCars(Car [] newCars) {
        ArrayList<Car> cars = new ArrayList<>();
        try {
            // Iteriere durch jedes JSON-Objekt im Array
            for (int i = 0; i < Client.jsonArrayUpdatePos.length(); i++) {
                JSONObject jsonObj = Client.jsonArrayUpdatePos.getJSONObject(i);

                // Greife auf die Werte der Schlüssel zu
                float offset = Float.parseFloat(jsonObj.getString("offset"));
                double pos = Double.parseDouble(jsonObj.getString("pos"));
                String id = jsonObj.getString("id");
                int speed = jsonObj.getInt("speed");
                if (newCars==null) {
                    CustomSprite sprite = switch (jsonObj.getString("asset")) {
                        case "1" -> new CustomSprite("car01.png", offset, pos);
                        case "2" -> new CustomSprite("car02.png", offset, pos);
                        case "3" -> new CustomSprite("car03.png", offset, pos);
                        case "4" -> new CustomSprite("car04.png", offset, pos);
                        case "5" -> new CustomSprite("truck.png", offset, pos);
                        case "6" -> new CustomSprite("semi.png", offset, pos);
                        default -> new CustomSprite(offset, pos);
                    };
                    if(!userID.equals(id)){
                        cars.add(new Car(id, sprite));
                    }
                } else {
                    cars = new ArrayList<>(Arrays.asList(newCars));
                    if (!id.equals(userID)) {
                        cars.forEach(c -> {
                            if (c.getID().equals(id)) {
                                c.setZ(pos);
                                c.setOffset(offset);
                                c.setPercent((float) Util.percentRemaining((float) (pos % 200), 200f));
                                c.setSpeed(speed);
                            }
                        });
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gameScreen.setNewCars(cars.toArray(Car[]::new));
    }
}
