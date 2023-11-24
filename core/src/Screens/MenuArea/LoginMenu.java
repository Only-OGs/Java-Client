package Screens.MenuArea;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Screens.Constants;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.json.JSONException;

import java.util.Objects;

public class LoginMenu extends MultiplayerMenu {

    public LoginMenu() {
        Constants.title.setText("Anmelden");
        removeButton();
        addButton("Anmelden", "Zurueck", "");
        buttonListener();
        createInputField(0);
    }

    @Override
    public void render(float delta) {

        if (updateStatusMessage) addServerMessage();

        if (!loginSuccess) {
            if (Objects.equals(Client.status, "login_success")) {
                loginSuccess = true;
                Client.status = "";
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

                    userField.setText("");
                    passwordField.setText("");

                    try {
                        Client.sendLoginData(user, password);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Client.statusMessage = "";
                    updateStatusMessage = true;
                }
            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                Client.statusMessage = "";
                OGRacerGame.getInstance().setScreen(new MultiplayerMenu());
            }
        });
    }

    @Override
    public void dispose() {
        Constants.stage.dispose();
    }
}


