package Screens.MenuArea;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Screens.Constants;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.json.JSONException;

import java.util.Objects;

public class LoginMenu extends MultiplayerMenu {

    private int delay = 0;

    public LoginMenu() {
        Constants.title.setText("Anmelden");
        removeButton();
        addButton("Anmelden", "Zurueck", "");
        buttonListener();
        createInputField(0);
    }

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

        if ("login_failed".equals(Client.loginStatus)) {
            loginMessage();
        }

        if ("logout_success".equals(Client.logoutStatus) || "logout_failed".equals(Client.logoutStatus)) {
            logoutMessage();
        }

        if (!loginSuccess) {
            if (Objects.equals(Client.loginStatus, "login_success")) {
                loginSuccess = true;
                serverMessage.setText("");
                statusOnOff = false;
                OGRacerGame.getInstance().setScreen(new LobbyMenu(user));
            }
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

                user = userField.getText();
                password = passwordField.getText();

                if (user.length() > 3 && password.length() > 5 && Client.connect) {
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


