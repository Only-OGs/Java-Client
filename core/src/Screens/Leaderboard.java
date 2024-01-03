package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Root.StyleGuide;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Leaderboard {

    private Stage gameStage;

    private ArrayList<Label> posis = new ArrayList<>();
    private ArrayList<Label> names = new ArrayList<>();
    private ArrayList<Label> times = new ArrayList<>();

    private ShapeRenderer renderer = new ShapeRenderer();

    private Label
            title = new Label("Leaderboard", Constants.buttonSkin, "title"),
            posiTitle = new Label("Position", Constants.buttonSkin),
            idNameTitle = new Label("Name", Constants.buttonSkin),
            totalTimeTitle = new Label("Gesamte Rundenzeit", Constants.buttonSkin);

    private ArrayList<Label> playerData = new ArrayList<>(8);


    public Leaderboard(Stage gameStage) {
        this.gameStage = gameStage;
        testData();

    }

    private void testData(){

        posis.add(new Label("1",Constants.buttonSkin));
        names.add(new Label("donatboy",Constants.buttonSkin));
        times.add(new Label("01:10:45",Constants.buttonSkin));
        posis.add(new Label("2",Constants.buttonSkin));
        names.add(new Label("Olli",Constants.buttonSkin));
        times.add(new Label("01:23:45",Constants.buttonSkin));
    }

    private void loadData(){
        try {
            // Iteriere durch jedes JSON-Objekt im Array
            for (int i = 0; i < Client.jsonArrayLeaderboard.length(); i++) {
                JSONObject jsonObj = Client.jsonArrayLeaderboard.getJSONObject(i);

                // Greife auf die Werte der Schlüssel zu
                posis.add(new Label(jsonObj.getString("posi"),Constants.buttonSkin));
                names.add(new Label(jsonObj.getString("name"),Constants.buttonSkin));
                times.add(new Label(jsonObj.getString("time"),Constants.buttonSkin));
                gameStage.addActor(posis.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void show() {

        // Spiel stoppt
        OGRacerGame.getInstance().isRunning = false;

        loadData();

        // Fenster gezeichnet
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor((float) 255 / 255, (float) 6 / 255, (float) 193 / 255, 0.8f);
        renderer.rect(150, 100, gameStage.getWidth() - 300, gameStage.getHeight() - 200);
        renderer.end();

        // Fenster Rand gezeichnet
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor((float) 186 / 255, (float) 46 / 255, (float) 151 / 255, 1);
        renderer.rect(150, 100, gameStage.getWidth() - 300, gameStage.getHeight() - 200);
        renderer.end();


        // Spieler Felder gezeichnet
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(new Color(255, 255, 255, 0.3f));
        for (int i = 200; i <= 620; i += 60)


            renderer.rect(300, gameStage.getHeight() - i, 720, 30);
        renderer.end();

        posiTitle.setColor(StyleGuide.white);
        idNameTitle.setColor(StyleGuide.white);
        totalTimeTitle.setColor(StyleGuide.white);

        title.setBounds(gameStage.getWidth() / 2 - 200, gameStage.getHeight() - 120, 100, 50);
        posiTitle.setBounds(300, gameStage.getHeight() - 150, 100, 20);
        idNameTitle.setBounds(510, gameStage.getHeight() - 150, 100, 20);
        totalTimeTitle.setBounds(760, gameStage.getHeight() - 150, 100, 20);
        gameStage.addActor(title);
        gameStage.addActor(posiTitle);
        gameStage.addActor(idNameTitle);
        gameStage.addActor(totalTimeTitle);

    }
}
