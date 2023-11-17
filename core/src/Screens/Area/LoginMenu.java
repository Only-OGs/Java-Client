package Screens.Area;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.json.JSONException;

import java.util.Objects;

public class LoginMenu extends MultiplayerMenu {


    public LoginMenu() {
        title.setText("Anmelden");
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
                OGRacerGame.getInstance().setScreen(new LobbyMenu(user));
            }
        }
        super.render(delta);
    }

    @Override
    protected void buttonListener() {
        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);

                user = userField.getText();
                password = passwordField.getText();

                if (user.length() > 3 && password.length() > 5 && Client.connect) {

                    userField.setText("");
                    passwordField.setText("");
                    Client.statusMessage = "";

                    try {
                        Client.sendLoginData(user, password);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    updateStatusMessage = true;
                }
            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                Client.statusMessage = "";
                OGRacerGame.getInstance().setScreen(new MultiplayerMenu());

            }
        });
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}


