package Screens.Area;

import OGRacerGame.OGRacerGame;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LobbyMenu extends MultiplayerMenu {

    private Label idLabel;

    private final String ID;

    private boolean test = false;

    private int count = 0;

    public LobbyMenu(String ID) {
        this.ID = ID;
        title.setText("Lobby");
        removeButton();
        addButton("Bereit","Abmelden","Bereit");
        buttonListener();
        updateStatusMessage = true;
        addIdLabel();
    }

    @Override
    public void render(float delta) {

        if(200 ==count ){
            updateStatusMessage = false;
            if (serverMessage != null) serverMessage.remove();
        }
        count++;
        if (updateStatusMessage) addServerMessage();
        super.render(delta);
    }

    public void addIdLabel() {
        idLabel = new Label("ID: " + ID, buttonSkin);
        idLabel.setSize(190, 40);
        idLabel.setPosition(stage.getWidth() / 1.3f, stage.getHeight() - 65);
        stage.addActor(idLabel);

    }

    @Override
    protected void buttonListener() {
        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);

            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                OGRacerGame.getInstance().setScreen(new MultiplayerMenu());

            }
        });

        buttonRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);

            }
        });
    }
}
