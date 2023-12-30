package Screens;

import OGRacerGame.OGRacerGame;
import Root.StyleGuide;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;

public class Leaderboard {

    private Stage gameStage;

    private ShapeRenderer renderer = new ShapeRenderer();

    private Label
            title = new Label("Leaderboard", Constants.buttonSkin, "title"),
            posiTitle = new Label("Position", Constants.buttonSkin),
            idNameTitle = new Label("Name", Constants.buttonSkin),
            totalTimeTitle = new Label("Gesamte Rundenzeit", Constants.buttonSkin);

    private ArrayList<Label> playerData = new ArrayList<>(8);


    public Leaderboard(Stage gameStage) {
        this.gameStage = gameStage;

    }

    public void show() {

        // Spiel stoppt
        OGRacerGame.getInstance().isRunning = false;

        // Rechteck wird gezeichnet
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor((float) 255 / 255, (float) 6 / 255, (float) 193 / 255, 0.8f);
        renderer.rect(150, 100, gameStage.getWidth() - 300, gameStage.getHeight() - 200);
        renderer.end();

        // Rechteck wird gezeichnet
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor((float) 186 / 255, (float) 46 / 255, (float) 151 / 255, 1);
        renderer.rect(150, 100, gameStage.getWidth() - 300, gameStage.getHeight() - 200);
        renderer.end();


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
