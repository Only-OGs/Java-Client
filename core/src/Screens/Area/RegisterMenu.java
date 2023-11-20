package Screens.Area;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.json.JSONException;

public class RegisterMenu extends MultiplayerMenu {

    public RegisterMenu() {
        title.setText("Registrieren");
        removeButton();
        addButton("", "Zurueck", "Registrieren");
        buttonListener();
        createInputField(1);
    }

    @Override
    public void render(float delta) {
        if (updateStatusMessage) addServerMessage();

        super.render(delta);
    }

    @Override
    protected void buttonListener() {

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                Client.statusMessage = "";
                OGRacerGame.getInstance().setScreen(new MultiplayerMenu());
            }
        });

        buttonRight.addListener(new ClickListener() {
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
                        Client.sendRegisterData(user, password);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    updateStatusMessage = true;
                }
            }
        });
    }
}


