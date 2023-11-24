package Screens.MenuArea;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Screens.Constants;
import Screens.MenuScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class MultiplayerMenu extends MenuScreen {

    protected boolean loginSuccess = false;

    protected boolean updateStatusMessage = false;

    protected boolean statusOnOff = true;

    protected Label status, statusTitle, serverMessage, userLabel, passwordLabel;

    protected String user, password;

    protected TextField userField, passwordField;

    public MultiplayerMenu() {
        Constants.title.setText("Mehrspieler");
        removeButton();
        addButton("Anmelden", "Zurueck", "Registrieren");
        buttonListener();
        addStatusLabel();
        if(!Client.connect) Client.connect();

    }

    @Override
    public void render(float delta) {

        if (Client.connect && Client.socket != null && statusOnOff) {
            status.remove();
            status = new Label("ONLINE", Constants.buttonSkin, "status1");
            status.setSize(190, 40);
            status.setPosition( Constants.stage.getWidth() / 30f,  Constants.stage.getHeight() - 85);
            Constants.stage.addActor(status);
        } else if (!Client.connect && Client.socket != null && statusOnOff) {
            status.remove();
            status = new Label("OFFLINE", Constants.buttonSkin, "status2");
            status.setSize(190, 40);
            status.setPosition( Constants.stage.getWidth() / 30f,  Constants.stage.getHeight() - 85);
            Constants.stage.addActor(status);
        }
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    protected void buttonListener() {
        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                OGRacerGame.getInstance().setScreen(new LoginMenu());
            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                if (Client.socket != null) Client.socket.disconnect();
                OGRacerGame.getInstance().setScreen(new MainMenu());

            }
        });

        buttonRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                OGRacerGame.getInstance().setScreen(new RegisterMenu());
            }
        });

    }
    private void addStatusLabel() {
        statusTitle = new Label("Server Status", Constants.buttonSkin);
        statusTitle.setSize(190, 40);
        statusTitle.setPosition( Constants.stage.getWidth() / 30f,  Constants.stage.getHeight() - 65);

        status = new Label("OFFLINE", Constants.buttonSkin, "status2");
        status.setSize(190, 40);
        status.setPosition( Constants.stage.getWidth() / 30f,  Constants.stage.getHeight() - 85);

        Constants.stage.addActor(statusTitle);
        Constants.stage.addActor(status);
    }

    protected void createInputField(int mode) {
        userField = new TextField("", Constants.buttonSkin);
        passwordField = new TextField("", Constants.buttonSkin);

        userField.setSize(190, 40);
        passwordField.setSize(190, 40);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);

        userLabel = new Label("Benutzername",Constants. buttonSkin);
        passwordLabel = new Label("Passwort", Constants.buttonSkin);
        userLabel.setSize(50, 30);
        passwordLabel.setSize(50, 30);

        if (mode == 0) {
            userLabel.setPosition(buttonLeft.getX() + 28, buttonLeft.getY()-80);
            userField.setPosition(buttonLeft.getX() + 28, buttonLeft.getY()-120);
            passwordLabel.setPosition(buttonLeft.getX() + 28, buttonLeft.getY()-180);
            passwordField.setPosition(buttonLeft.getX() + 28, buttonLeft.getY()-220);
        } else if(mode == 1) {
            userLabel.setPosition(buttonRight.getX() + 28, buttonRight.getY()-80);
            userField.setPosition(buttonRight.getX() + 28, buttonRight.getY()-120);
            passwordLabel.setPosition(buttonRight.getX() + 28, buttonRight.getY()-180);
            passwordField.setPosition(buttonRight.getX() + 28, buttonRight.getY()-220);
        }

        Constants.stage.addActor(userLabel);
        Constants.stage.addActor(passwordLabel);
        Constants.stage.addActor(userField);
        Constants.stage.addActor(passwordField);
    }

    protected void addServerMessage() {
        if (serverMessage != null) serverMessage.remove();
        serverMessage = new Label(Client.statusMessage, Constants.buttonSkin);
        serverMessage.setSize(190, 40);
        serverMessage.setPosition( Constants.stage.getWidth() / 30f, 10);
        Constants.stage.addActor(serverMessage);
    }
}
