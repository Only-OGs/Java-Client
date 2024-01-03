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

    public Leaderboard(Stage gameStage) {
        this.gameStage = gameStage;
        //testData();

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
                times.add(new Label(jsonObj.getString("time").replaceAll(";",":"),Constants.buttonSkin));
                System.out.println(times);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void show() {


        if(Client.jsonArrayLeaderboard != null && OGRacerGame.getInstance().isRunning){
            loadData();
        }

        // Spiel Stoppt
        OGRacerGame.getInstance().isRunning = false;

        // Fenster gezeichnet
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor((float) 255 / 255, (float) 6 / 255, (float) 193 / 255, 0.9f);
        renderer.rect(150, 100, gameStage.getWidth() - 300, gameStage.getHeight() - 200);
        renderer.end();

        // Spieler Felder gezeichnet
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(new Color(0, 0, 0, 0.8f));

        int counter = 0;
        for (int i = 200; i <= 620; i += 60){


            if (posis.size() > counter) {
                posis.get(counter).setBounds(260,gameStage.getHeight() - i-10,100,50);
                names.get(counter).setBounds(460,gameStage.getHeight() - i-10,100,50);
                times.get(counter).setBounds(710,gameStage.getHeight() - i-10,100,50);
                gameStage.addActor(posis.get(counter));
                gameStage.addActor(names.get(counter));
                gameStage.addActor(times.get(counter));

                counter++;
            }

            renderer.rect(255
                    , gameStage.getHeight() - i, 800, 30);
        }
        renderer.end();

        posiTitle.setColor(StyleGuide.white);
        posiTitle.setFontScale(1.3f);
        idNameTitle.setColor(StyleGuide.white);
        idNameTitle.setFontScale(1.3f);
        totalTimeTitle.setColor(StyleGuide.white);
        totalTimeTitle.setFontScale(1.3f);

        title.setBounds(gameStage.getWidth() / 2 - 200, gameStage.getHeight() - 120, 100, 50);
        posiTitle.setBounds(260, gameStage.getHeight() - 150, 100, 20);
        idNameTitle.setBounds(460, gameStage.getHeight() - 150, 100, 20);
        totalTimeTitle.setBounds(710, gameStage.getHeight() - 150, 100, 20);
        gameStage.addActor(title);
        gameStage.addActor(posiTitle);
        gameStage.addActor(idNameTitle);
        gameStage.addActor(totalTimeTitle);




    }
}
