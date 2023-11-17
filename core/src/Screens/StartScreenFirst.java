package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.json.JSONException;

import java.util.Objects;

public class StartScreenFirst extends ScreenAdapter implements IInputHandler {

    private boolean loginSucess = false;
    private boolean connectionCheck = false;
    private boolean updateStatusMessage = false;
    private Viewport viewport;
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Slider slider;
    private TextButton buttonLeft, buttonMiddle, buttonRight;
    private Label title;
    private Label userLabel;
    private Label passwordLabel;
    private Label status;
    private Label statusTitle;
    private Label idLabel;
    private Label serverMessage;
    private String user;
    private String password;
    private Skin buttonSkin;
    private Sound clickSound;
    private Music music;
    private TextField userField, passwordField;

    public StartScreenFirst() {
        addSkin();
        addViewport();
        addBackground();
        addLabel();
        addButton("Einzelspieler", "Einstellungen", "Mehrspieler");
        mainMenu();
        addMusic();
        addSlider();
    }

    private void addBackground() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture("background/BackgroundOGRacerGame.png");
    }

    private void addButton(String btn1, String btn2, String btn3) {

        buttonLeft = new TextButton(btn1, buttonSkin);
        buttonLeft.setSize(250, 60);
        buttonLeft.setPosition((stage.getWidth() - buttonLeft.getWidth()) / 2 - buttonLeft.getWidth() - 50, stage.getHeight() / 2 - (buttonLeft.getHeight()) + 25);

        buttonRight = new TextButton(btn3, buttonSkin);
        buttonRight.setSize(250, 60);
        buttonRight.setPosition((stage.getWidth() - buttonRight.getWidth()) / 2 + buttonRight.getWidth() + 50, stage.getHeight() / 2 - (buttonRight.getHeight()) + 25);

        buttonMiddle = new TextButton(btn2, buttonSkin);
        buttonMiddle.setSize(250, 60);
        buttonMiddle.setPosition((stage.getWidth() - buttonMiddle.getWidth()) / 2, stage.getHeight() / 2 - (buttonMiddle.getHeight()));
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

    private void addLabel() {
        title = new Label("Hauptmenu", buttonSkin, "title");
        title.setSize(Gdx.graphics.getWidth(), 100);
        title.setPosition(0, stage.getHeight() - 100);
        title.setAlignment(Align.center);
        stage.addActor(title);
    }

    private void addServerMessage() {
        if (serverMessage != null) serverMessage.remove();
        serverMessage = new Label(Client.statusMessage, buttonSkin);
        serverMessage.setSize(190, 40);
        serverMessage.setPosition(stage.getWidth() / 30f, 10);

        stage.addActor(serverMessage);
    }

    public void addIdLabel() {
        idLabel = new Label("ID: " + user, buttonSkin);
        idLabel.setSize(190, 40);
        idLabel.setPosition(stage.getWidth() / 1.3f, stage.getHeight() - 65);
        stage.addActor(idLabel);

    }

    private void addSlider() {
        slider = new Slider(0.0f, 0.7f, 0.01f, false, buttonSkin);
        slider.setValue(music.getVolume());
        slider.setPosition(stage.getWidth() / 1.3f, 20);
        slider.setSize(200, 10);

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                float volume = slider.getValue();
                music.setVolume(volume);
            }
        });
        stage.addActor(slider);
    }

    private void addSkin() {
        buttonSkin = new Skin(Gdx.files.internal("ButtonStyle/quantum-horizon/skin/quantum-horizon-ui.json"));
    }

    private void addMusic() {
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/StartMenuMusic.mp3"));
        clickSound = Gdx.audio.newSound((Gdx.files.internal("Music/clickInterface.mp3")));
        music.setLooping(true);
        music.setVolume(0.01f);
        music.play();
    }

    private void addViewport() {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
    }

    private void createInputField(int mode) {
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
        } else {
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

    private void mainMenu() {
        title.setText("Hauptmenu");
        stage.addActor(buttonLeft);
        stage.addActor(buttonRight);
        stage.addActor(buttonMiddle);

        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
            }
        });
        buttonRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                buttonLeft.remove();
                buttonRight.remove();
                buttonMiddle.remove();
                addButton("Login", "Zurueck", "Registrieren");
                addStatusLabel();
                multiplayerMenu();
            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                buttonLeft.remove();
                buttonRight.remove();
                buttonMiddle.remove();
                addButton("-", "Zurueck", "-");
                settingMenu();
            }
        });
        Gdx.input.setInputProcessor(stage);
    }

    private void settingMenu() {
        title.setText("Einstellungen");
        stage.addActor(buttonMiddle);
        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                buttonLeft.remove();
                buttonRight.remove();
                addButton("Einzelspieler", "Einstellungen", "Mehrspieler");
                mainMenu();
            }
        });
        Gdx.input.setInputProcessor(stage);
    }

    private void multiplayerMenu() {


        if (!Client.connect) {
            Client.connect();
        }

        connectionCheck = true;

        title.setText("Mehrspieler");
        stage.addActor(buttonLeft);
        stage.addActor(buttonRight);
        stage.addActor(buttonMiddle);

        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                buttonLeft.remove();
                buttonMiddle.remove();
                buttonRight.remove();
                addButton("Anmelden", "Zurueck", "-");
                loginMenu();
            }
        });
        buttonRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                buttonLeft.remove();
                buttonMiddle.remove();
                buttonRight.remove();
                addButton("-", "Zurueck", "Registrieren");
                registerMenu();
            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                if (Client.socket != null) Client.socket.disconnect();
                buttonLeft.remove();
                buttonRight.remove();
                buttonMiddle.remove();
                statusTitle.remove();
                status.remove();
                connectionCheck = false;
                addButton("Einzelspieler", "Einstellungen", "Mehrspieler");
                mainMenu();
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    private void registerMenu() {
        title.setText("Registrieren");
        createInputField(1);
        stage.addActor(buttonRight);
        stage.addActor(buttonMiddle);

        buttonRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);

                user = userField.getText();
                password = passwordField.getText();

                if (user.length() > 3 && password.length() > 5 && Client.connect) {
                    userField.setText("");
                    passwordField.setText("");
                    Client.status = "";

                    try {
                        Client.sendRegisterData(user, password);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                updateStatusMessage = true;
                addServerMessage();
            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                buttonMiddle.remove();
                buttonRight.remove();
                userField.remove();
                passwordField.remove();
                userLabel.remove();
                passwordLabel.remove();
                if (serverMessage != null) serverMessage.remove();
                updateStatusMessage = false;
                Client.status = "";

                addButton("Login", "Zurueck", "Registrieren");
                multiplayerMenu();
            }
        });

    }

    private void loginMenu() {

        title.setText("Login");
        createInputField(0);
        stage.addActor(buttonLeft);
        stage.addActor(buttonMiddle);

        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);

                user = userField.getText();
                password = passwordField.getText();

                if (user.length() > 3 && password.length() > 5 && Client.connect) {

                    userField.setText("");
                    passwordField.setText("");
                    Client.status = "";


                    try {
                        Client.sendLoginData(user, password);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    updateStatusMessage = true;
                    addServerMessage();
                }
            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                buttonMiddle.remove();
                buttonLeft.remove();
                userField.remove();
                passwordField.remove();
                userLabel.remove();
                passwordLabel.remove();
                if (serverMessage != null) serverMessage.remove();
                if (idLabel != null) idLabel.remove();
                updateStatusMessage = false;

                Client.status = "";

                addButton("Login", "Zurueck", "Registrieren");
                multiplayerMenu();
            }
        });
    }

    private void lobbyMenu() {
        title.setText("Lobby");
        buttonLeft.remove();
        buttonMiddle.remove();
        userField.remove();
        passwordField.remove();
        userLabel.remove();
        passwordLabel.remove();
        addButton("Bereit", "Abmelden", "Bereit");
        stage.addActor(buttonLeft);
        stage.addActor(buttonRight);
        stage.addActor(buttonMiddle);

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
                buttonLeft.remove();
                buttonMiddle.remove();
                buttonRight.remove();
                idLabel.remove();
                serverMessage.remove();
                updateStatusMessage = false;
                loginSucess = false;
                Client.status = "";
                addButton("Anmelden", "Zurueck", "Registrieren");
                multiplayerMenu();
            }
        });

        buttonRight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
            }
        });

    }

    /**
     * [StartScreen] Fragt ab ob eine Taste gedruckt wurde/ist
     */
    @Override
    public void checkInput(OGRacerGame game) {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, stage.getWidth(), stage.getHeight());
        batch.end();

        stage.act();
        stage.draw();
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (Client.connect && Client.socket != null && connectionCheck && status != null) {
            status.remove();
            status = new Label("ONLINE", buttonSkin, "status1");
            status.setSize(190, 40);
            status.setPosition(stage.getWidth() / 30f, stage.getHeight() - 85);
            stage.addActor(status);
        } else if (!Client.connect && Client.socket != null && connectionCheck && status != null) {
            status.remove();
            status = new Label("OFFLINE", buttonSkin, "status2");
            status.setSize(190, 40);
            status.setPosition(stage.getWidth() / 30f, stage.getHeight() - 85);
            stage.addActor(status);
        }

        if (updateStatusMessage && serverMessage != null) {
            addServerMessage();
        }

        // Wenn anmleden erfolgreich war geht es hier rein
        if (!loginSucess) {
            if (Objects.equals(Client.status, "login_success")) {
                addIdLabel();
                loginSucess = true;
                Client.status = "";
                lobbyMenu();
            }
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        music.dispose();
        clickSound.dispose();
    }
}
