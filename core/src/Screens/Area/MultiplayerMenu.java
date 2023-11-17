package Screens.Area;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MultiplayerMenu extends MenuArea {

    protected boolean loginSuccess = false;

    protected boolean updateStatusMessage = false;

    protected Label status, statusTitle, serverMessage, userLabel, passwordLabel;

    protected String user, password;

    protected TextField userField, passwordField;

    public MultiplayerMenu() {
        title.setText("Mehrspieler");
        removeButton();
        addButton("Anmelden", "Zurueck", "Registrieren");
        buttonListener();
        addStatusLabel();
        if(!Client.connect) Client.connect();

    }

    @Override
    public void render(float delta) {

        if (Client.connect && Client.socket != null) {
            status.remove();
            status = new Label("ONLINE", buttonSkin, "status1");
            status.setSize(190, 40);
            status.setPosition(stage.getWidth() / 30f, stage.getHeight() - 85);
            stage.addActor(status);
        } else if (!Client.connect && Client.socket != null) {
            status.remove();
            status = new Label("OFFLINE", buttonSkin, "status2");
            status.setSize(190, 40);
            status.setPosition(stage.getWidth() / 30f, stage.getHeight() - 85);
            stage.addActor(status);
        }
        super.render(delta);
    }

    @Override
    protected void buttonListener() {
        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                OGRacerGame.getInstance().setScreen(new LoginMenu());
            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                if (Client.socket != null) Client.socket.disconnect();
                OGRacerGame.getInstance().setScreen(new MainMenu());

            }
        });

        buttonRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                OGRacerGame.getInstance().setScreen(new RegisterMenu());
            }
        });

    }


    private void addStatusLabel() {
        statusTitle = new Label("Server Status", buttonSkin);
        statusTitle.setSize(190, 40);
        statusTitle.setPosition(stage.getWidth() / 30f, stage.getHeight() - 65);

        status = new Label("OFFLINE", buttonSkin, "status2");
        status.setSize(190, 40);
        status.setPosition(stage.getWidth() / 30f, stage.getHeight() - 85);

        stage.addActor(statusTitle);
        stage.addActor(status);
    }

    protected void createInputField(int mode) {
        userField = new TextField("", buttonSkin);
        passwordField = new TextField("", buttonSkin);

        userField.setSize(190, 40);
        passwordField.setSize(190, 40);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);

        userLabel = new Label("Benutzername", buttonSkin);
        passwordLabel = new Label("Passwort", buttonSkin);
        userLabel.setSize(50, 30);
        passwordLabel.setSize(50, 30);

        if (mode == 0) {
            userField.setPosition(buttonLeft.getX() + 28, 200);
            passwordField.setPosition(buttonLeft.getX() + 28, 100);
            userLabel.setPosition(buttonLeft.getX() + 28, 250);
            passwordLabel.setPosition(buttonLeft.getX() + 28, 150);
        } else if(mode == 1) {
            userField.setPosition(buttonRight.getX() + 28, 200);
            passwordField.setPosition(buttonRight.getX() + 28, 100);
            userLabel.setPosition(buttonRight.getX() + 28, 250);
            passwordLabel.setPosition(buttonRight.getX() + 28, 150);
        }

        stage.addActor(userLabel);
        stage.addActor(passwordLabel);
        stage.addActor(userField);
        stage.addActor(passwordField);
    }

    protected void addServerMessage() {
        if (serverMessage != null) serverMessage.remove();
        serverMessage = new Label(Client.statusMessage, buttonSkin);
        serverMessage.setSize(190, 40);
        serverMessage.setPosition(stage.getWidth() / 30f, 10);
        stage.addActor(serverMessage);
    }
}
