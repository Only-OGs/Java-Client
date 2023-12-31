package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Road.Car;
import Road.CustomSprite;
import Screens.MenuArea.MultiplayerMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadingScreen extends ScreenAdapter {

    private FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    private Stage stage = new Stage(viewport);

    private Timer.Task timerTask;

    private String userID;

    public LoadingScreen(String userID) {
        this.userID = userID;
        Gdx.input.setInputProcessor(stage);
        Constants.title.setText("Das Spiel startet gleich ...");
        Constants.title.setSize(Gdx.graphics.getWidth(), 100);
        Constants.title.setPosition(0, stage.getHeight() - 100);
        Constants.title.setAlignment(Align.center);
        stage.addActor(Constants.title);
        OGRacerGame.getInstance().setGameScreen(new GameScreen(true,userID));
        waitTimer();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        if (!Client.connect) OGRacerGame.getInstance().setScreen(new MultiplayerMenu());

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if(Client.startGame){
            Client.startGame = false;
            setPos();
        }

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        super.resize(width, height);
    }

    private void waitTimer() {

        timerTask = new Timer.Task() {
            @Override
            public void run() {
                OGRacerGame.getInstance().setScreen(OGRacerGame.getInstance().getGameScreen());
                Client.clientReady();
            }
        };

        // Starte den Timer mit einer Verzögerung von 10 Sekunden
        Timer.schedule(timerTask, 1);  // muss 1
    }

    void setPos(){
        ArrayList<Car> cars = new ArrayList<>();

        try {

            // Iteriere durch jedes JSON-Objekt im Array
            for (int i = 0; i < Client.jsonArrayStartPos.length(); i++) {
                JSONObject jsonObj = Client.jsonArrayStartPos.getJSONObject(i);

                // Greife auf die Werte der Schlüssel zu
                float offset = Float.parseFloat(jsonObj.getString("offset"));
                double pos = Double.parseDouble(jsonObj.getString("pos"));
                String id = jsonObj.getString("id");

                if(!(id.equals("null")) ){
                    if(id.equals(userID)){
                        OGRacerGame.getInstance().getGameScreen().setPlayerX(offset);
                        OGRacerGame.getInstance().getGameScreen().setCameraPosition(pos);
                    }else{
                        CustomSprite sprite = new CustomSprite(offset,pos);
                        cars.add(new Car(id,sprite));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OGRacerGame.getInstance().getGameScreen().setNewCars(cars.toArray(Car[]::new));
    }

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }
}
