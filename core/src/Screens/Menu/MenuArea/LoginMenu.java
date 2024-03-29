package Screens.Menu.MenuArea;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Helpers.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.json.JSONException;

public class LoginMenu extends MultiplayerMenu {

    private int delay = 0;

    public LoginMenu() {
        Constants.title.setText("Anmelden");
        removeButton();
        addButton("Anmelden", "Zurueck", "");
        buttonListener();
        createInputField(0);
    }

    // Zeigt die Logoutnachricht vom Server für eine bestimmte Zeit an.
    private void logoutMessage() {

        if (200 == delay) {
            serverMessage.setText("");
            Client.logoutMessage = "";
            Client.logoutStatus = "";
            delay++;
        } else if (delay < 200) {
            serverMessage.setText(Client.logoutMessage);
            delay++;
        }
    }

    // Zeigt die Login-Nachricht vom Server für eine bestimmte Zeit an.
    private void loginMessage() {

        if (200 == delay) {
            serverMessage.setText("");
            Client.loginMessage = "";
            Client.loginStatus = "";
            delay++;
        } else if (delay < 200) {
            serverMessage.setText(Client.loginMessage);
            delay++;
        }
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Constants.clickButton.play(0.2f);
            OGRacerGame.getInstance().setScreen(new MultiplayerMenu());
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Constants.clickButton.play(0.2f);
            checkData();
        }
        if ("login_failed".equals(Client.loginStatus)) {
            loginMessage();
        }

        if ("logout_success".equals(Client.logoutStatus) || "logout_failed".equals(Client.logoutStatus)) {
            logoutMessage();
        }

        if ("login_success".equals(Client.loginStatus)) {
            statusOnOff = false;
            OGRacerGame.getInstance().setScreen(new LobbyMenu(user));
        }
        super.render(delta);
    }

    // Überprüft, ob die Mindestlängen der Eingaben korrekt sind.
    private void checkData() {

        user = userField.getText();
        password = passwordField.getText();

        if (user.length() >= 3 && password.length() >= 6 && Client.connect) {
            delay = 0;
            userField.setText("");
            passwordField.setText("");

            try {
                Client.sendLoginData(user, password);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            Client.loginMessage = "";
        }
    }

    @Override
    protected void buttonListener() {
        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                checkData();
            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                Client.loginMessage = "";
                OGRacerGame.getInstance().setScreen(new MultiplayerMenu());
            }
        });
    }
}


