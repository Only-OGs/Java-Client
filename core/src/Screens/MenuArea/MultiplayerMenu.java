package Screens.MenuArea;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Screens.Constants;
import Screens.MenuScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MultiplayerMenu extends MenuScreen {

    protected boolean loginSuccess = false;


    protected boolean statusOnOff = true;

    protected Label status, statusTitle, serverMessage, userLabel, passwordLabel;

    protected String user, password;

    protected TextField userField, passwordField;


    public MultiplayerMenu() {
        Constants.title.setText("Mehrspieler");
        removeButton();
        addButton("Anmelden", "Zurueck", "Registrieren");
        buttonListener();
        addConnectionStatuslabel();
        addServerMessageLabel();
        if(!Client.connect) Client.connect();

    }

    @Override
    public void render(float delta) {

        if (Client.connect && Client.socket != null && statusOnOff) {
            status.remove();
            status = new Label("ONLINE", Constants.buttonSkin, "status1");
            status.setSize(190, 40);
            status.setPosition( stage.getWidth() / 30f,  stage.getHeight() - 85);
            stage.addActor(status);
        } else if (!Client.connect && Client.socket != null && statusOnOff) {
            status.remove();
            status = new Label("OFFLINE", Constants.buttonSkin, "status2");
            status.setSize(190, 40);
            status.setPosition( stage.getWidth() / 30f,  stage.getHeight() - 85);
            stage.addActor(status);
        }
        super.render(delta);
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
    private void addConnectionStatuslabel() {
        statusTitle = new Label("Server Status", Constants.buttonSkin);
        statusTitle.setSize(190, 40);
        statusTitle.setPosition( stage.getWidth() / 30f,  stage.getHeight() - 65);

        status = new Label("OFFLINE", Constants.buttonSkin, "status2");
        status.setSize(190, 40);
        status.setPosition( stage.getWidth() / 30f,  stage.getHeight() - 85);

        stage.addActor(statusTitle);
        stage.addActor(status);
    }

    protected void createInputField(int mode) {
        userField = new TextField("", Constants.buttonSkin);
        passwordField = new TextField("", Constants.buttonSkin);

        userField.setSize(190, 40);

        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);

        userLabel = new Label("Benutzername",Constants. buttonSkin);
        passwordLabel = new Label("Passwort", Constants.buttonSkin);

         if (mode == 0) {
            userLabel.setBounds(buttonLeft.getX() + 28, buttonLeft.getY()-80,50,30);
            userField.setBounds(buttonLeft.getX() + 28, buttonLeft.getY()-120,190,40);

            passwordLabel.setBounds(buttonLeft.getX() + 28, buttonLeft.getY()-180,50,30);
            passwordField.setBounds(buttonLeft.getX() + 28, buttonLeft.getY()-220,190,40);

        } else if(mode == 1) {
            userLabel.setBounds(buttonRight.getX() + 28, buttonRight.getY()-80,50,30);
            userField.setBounds(buttonRight.getX() + 28, buttonRight.getY()-120,190,40);
            passwordLabel.setBounds(buttonRight.getX() + 28, buttonRight.getY()-180,50,30);
            passwordField.setBounds(buttonRight.getX() + 28, buttonRight.getY()-220,190,40);
        }


        stage.addActor(userLabel);
        stage.addActor(passwordLabel);
        stage.addActor(userField);
        stage.addActor(passwordField);
    }

    protected void addServerMessageLabel() {
        serverMessage = new Label("",Constants.buttonSkin);
        serverMessage.setBounds(stage.getWidth() / 30f, 10,190, 40);
        stage.addActor(serverMessage);
    }
}
