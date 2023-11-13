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

public class StartScreen extends ScreenAdapter implements IInputHandler {


    private Viewport viewport;
    private Stage stage;

    private SpriteBatch batch = new SpriteBatch();
    private Texture backgroundTexture;

    private Slider slider;

    private TextButton buttonLeft, buttonMiddle, buttonRight;

    private Label title;

    private Skin buttonSkin;

    private Sound clickSound;

    static Music music;

    TextField user;

    TextField password;

    public StartScreen() {
        addSkin();
        addVieport();
        addBackground();
        addLabel();
        setButtons("Einzelspieler","Einstellungen","Mehrspieler");
        mainMenu();
        addStartMusic();
        addSlider();
    }


    private void addSlider(){
        slider  = new Slider(0.0f,0.7f,0.01f,false,buttonSkin);
        slider.setValue(music.getVolume());
        slider.setPosition(10,20);
        slider.setSize(200,10);

       slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                float volume = slider.getValue();
                music.setVolume(volume);
            }
        });

        stage.addActor(slider);
    }


    private void addSkin(){
        buttonSkin = new Skin(Gdx.files.internal("ButtonStyle/quantum-horizon/skin/quantum-horizon-ui.json"));
    }

    private void addBackground(){
        backgroundTexture = new Texture("background/BackgroundOGRacerGame.png");
    }
    private void addVieport(){
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(20 /255f, 21 /255f, 44 /255f, 1.0f);
        batch.begin();
        batch.draw(backgroundTexture,0,0,stage.getWidth(),stage.getHeight());
        batch.end();

        stage.act();
        stage.draw();
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        music.dispose();
        clickSound.dispose();

    }

    private void createUserField(){
        user = new TextField("",buttonSkin);
        password = new TextField("",buttonSkin);

        user.setSize(160,40);
        password.setSize(160,40);
        user.setPosition(stage.getWidth()/8,200);
        password.setPosition(stage.getWidth()/8,100);

        password.setPasswordCharacter('*');
        password.setPasswordMode(true);

        stage.addActor(user);
        stage.addActor(password);
    }

    private void addStartMusic(){
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/StartMenuMusic.mp3"));
        clickSound = Gdx.audio.newSound((Gdx.files.internal("Music/clickInterface.mp3")));
        music.setLooping(true);
        music.setVolume(0.01f);
        music.play();
    }

    private void mainMenu(){

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
                setButtons("Login","Zurueck","Registrieren");
                muliplayerMenu();


            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                buttonLeft.remove();
                buttonRight.remove();
                buttonMiddle.remove();
                setButtons("-","Zurueck","-");
                settingMenu();


            }
        });


        Gdx.input.setInputProcessor(stage);

    }

    private void settingMenu(){

        title.setText("Einstellungen");

        stage.addActor(buttonMiddle);

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                buttonLeft.remove();
                buttonRight.remove();
                setButtons("Einzelspieler","Einstellungen","Mehrspieler");
                mainMenu();

            }
        });

        Gdx.input.setInputProcessor(stage);

    }
    private void addLabel(){
        title = new Label("Hauptmenu",buttonSkin,"title");
        title.setSize(Gdx.graphics.getWidth(),100);
        title.setPosition(0, stage.getHeight()-100);
        title.setAlignment(Align.center);
        stage.addActor(title);
    }

    private void setButtons(String btn1, String btn2, String btn3){

        buttonLeft = new TextButton(btn1,buttonSkin);
        buttonLeft .setSize(250,60);
        buttonLeft .setPosition((stage.getWidth() - buttonLeft.getWidth()) / 2 - buttonLeft.getWidth() -50, stage.getHeight() / 2 - (buttonLeft.getHeight())+20);

        buttonRight = new TextButton(btn3,buttonSkin);
        buttonRight.setSize(250,60);
        buttonRight.setPosition((stage.getWidth() - buttonRight.getWidth()) / 2 + buttonRight.getWidth() +50, stage.getHeight() / 2 - (buttonRight.getHeight())+20);

        buttonMiddle = new TextButton(btn2,buttonSkin);
        buttonMiddle.setSize(250,60);
        buttonMiddle.setPosition((stage.getWidth() - buttonMiddle.getWidth()) / 2, stage.getHeight() / 2 - (buttonMiddle.getHeight()));
    }
    private void muliplayerMenu(){

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
                setButtons("Anmelden","Zurueck","-");
                loginMenu();


            }
        });
        buttonRight.addListener(new ClickListener() {
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
                buttonRight.remove();
                setButtons("Einzelspieler","Einstellungen","Mehrspieler");
                mainMenu();

            }
        });


        Gdx.input.setInputProcessor(stage);

    }

    private void loginMenu(){

        title.setText("Login");

        createUserField();

        stage.addActor(buttonLeft);
        stage.addActor(buttonMiddle);

        buttonLeft.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);

                if(!Client.connect){
                    Client.connect();
                }


            }
        });

        buttonMiddle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(0.2f);
                buttonMiddle.remove();
                buttonLeft.remove();
                user.remove();
                password.remove();
                Client.socket.disconnect();

                setButtons("Login","Zurueck","Registrieren");
                muliplayerMenu();


            }
        });



    }

    /** [StartScreen] Fragt ab ob eine Taste gedruckt wurde/ist */
    @Override
    public void checkInput(OGRacerGame game) {}

}
