package Screens.MenuArea;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Screens.Constants;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.json.JSONException;

public class RegisterMenu extends MultiplayerMenu {

    private int delay = 0;

    public RegisterMenu() {
        Constants.title.setText("Registrieren");
        removeButton();
        addButton("", "Zurueck", "Registrieren");
        buttonListener();
        createInputField(1);
    }

    @Override
    public void render(float delta) {

        if (200 == delay) {
            serverMessage.setText("");
            delay++;
        }else if( delay < 200){
            serverMessage.setText(Client.statusMessage);
            delay++;
        }
        super.render(delta);
    }

    @Override
    protected void buttonListener() {

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                Client.statusMessage = "";
                OGRacerGame.getInstance().setScreen(new MultiplayerMenu());
            }
        });

        buttonRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                user = userField.getText();
                password = passwordField.getText();

                if (user.length() > 3 && password.length() > 5 && Client.connect) {
                    delay = 0;
                    userField.setText("");
                    passwordField.setText("");
                    Client.statusMessage = "";

                    try {
                        Client.sendRegisterData(user, password);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}


