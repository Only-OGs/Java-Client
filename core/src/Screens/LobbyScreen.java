package Screens;

import Connection.Client;
import Root.StyleGuide;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class LobbyScreen extends ScreenAdapter {

    private Stage stage1;
    private int searchCounter = 0;
    public static ArrayList<String> idList = new ArrayList<>(8);
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final String ID;
    private Label idLabel, lobbyCode, chatLable;
    private final Label[] player = new Label[8];
    private Table chatTable = new Table(Constants.buttonSkin);
    private ScrollPane scrollChat = new ScrollPane(chatTable, Constants.buttonSkin);
    private TextButton sendButton = new TextButton("Senden", Constants.buttonSkin);
    private TextArea sendMessage = new TextArea("", Constants.buttonSkin);

    public LobbyScreen(String ID) {
        this.ID = ID;

        Constants.title.setText("Die Lobby");
        Constants.stage.clear();
        Constants.title.setSize(Gdx.graphics.getWidth(), 100);
        Constants.title.setPosition(0, Constants.stage.getHeight() - 100);
        Constants.title.setAlignment(Align.center);
        Constants.stage.addActor(Constants.title);
        Constants.stage.addActor(Constants.title);
        addLabelPlayer();
        addLabelID();
        addLobbyID();
        addChatLable();
        FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage1 = new Stage(viewport);
        Gdx.input.setInputProcessor(stage1);
        buildChat();
        addSendListener();

    }

    public void addChatLable() {
        chatLable = new Label("Chat: ", Constants.buttonSkin);
        chatLable.setSize(190, 40);
        chatLable.setPosition(Constants.stage.getWidth() / 1.9f, Constants.stage.getHeight() - 138);
        Constants.stage.addActor(chatLable);
    }

    private void buildChat() {
        Pixmap backgroundPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        backgroundPixmap.setColor(new Color(255, 255, 255, 0.08f)); // Hier setzt du die Hintergrundfarbe auf Rot
        backgroundPixmap.fill();
        Texture backgroundTexture = new Texture(backgroundPixmap);
        backgroundPixmap.dispose();

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
        chatTable.setBackground(backgroundDrawable);

        scrollChat.setBounds(Gdx.graphics.getWidth() / 1.9f, 300, 400, 348);
        scrollChat.setFadeScrollBars(false);
        scrollChat.setScrollbarsVisible(true);
        scrollChat.setScrollingDisabled(true, false); // Horizontales Scrollen deaktivieren
        chatTable.add("").left().row();

        sendMessage.setBounds(Gdx.graphics.getWidth() / 1.9f, 180, scrollChat.getWidth(), 100);
        Constants.stage.addActor(sendButton);
        sendButton.setBounds(sendMessage.getX() + sendMessage.getWidth() - 122, sendMessage.getY() - 60, 130, 50);

        chatTable.add("                                                ").left().row();
        stage1.addActor(scrollChat);
        stage1.addActor(sendButton);
        stage1.addActor(sendMessage);
    }

    private boolean isScrollBarAtBottom() {
        float scrollY = scrollChat.getScrollY();
        float maxY = scrollChat.getActor().getHeight() - scrollChat.getHeight();
        return MathUtils.isEqual(scrollY, maxY);
    }

    public void addLabelID() {
        idLabel = new Label("ID: " + ID, Constants.buttonSkin);
        idLabel.setSize(190, 40);
        idLabel.setPosition(Constants.stage.getWidth() / 1.3f, Constants.stage.getHeight() - 65);
        Constants.stage.addActor(idLabel);
    }

    public void addLobbyID() {
        lobbyCode = new Label("Lobby ID: " + Client.statusMessage, Constants.buttonSkin);
        lobbyCode.setSize(190, 40);
        lobbyCode.setPosition(Constants.stage.getWidth() / 1.3f, 25);
        Constants.stage.addActor(lobbyCode);
        Client.statusMessage = "";
    }

    private void addLabelPlayer() {

        for (int i = 0; i < player.length; i++) {
            player[i] = new Label("Suchen ", Constants.buttonSkin);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(20 / 255f, 21 / 255f, 44 / 255f, 1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);


        updateField();

        if (isScrollBarAtBottom()) {
            scrollChat.layout(); // Das Layout aktualisieren, bevor wir scrollen
            scrollChat.scrollTo(0, 0, 0, 0); // Nach unten scrollen (0, 0)
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.TAB)){
            chatTable.add("Anonym").left().row();
            chatTable.add(sendMessage.getText()).left().row();
            sendMessage.setText("");
        }



        stage1.act(Gdx.graphics.getDeltaTime());
        stage1.draw();
        Constants.stage.act(Gdx.graphics.getDeltaTime());
        Constants.stage.draw();
    }


    private void addSendListener() {

        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (sendMessage.getText().length() > 1) {


                    Label redLabel = new Label("",Constants.buttonSkin);
                    redLabel.setText(ID);
                    redLabel.setColor(StyleGuide.purpleLight);



                    // Füge die Labels zur Tabelle hinzu
                    chatTable.add(redLabel).right().row();
                    chatTable.add((sendMessage.getText()) + "\n").right().row();
                    sendMessage.setText("");
                }
            }
        });

    }

    @Override
    public void resize(int width, int height) {
        Constants.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
    }


    void updateField() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(255, 255, 255, 0.08f));
        shapeRenderer.rect(100, ((float) Gdx.graphics.getHeight() / 16), 400, 40);
        shapeRenderer.rect(100, ((float) Gdx.graphics.getHeight() / 16) + 80, 400, 40);
        shapeRenderer.rect(100, ((float) Gdx.graphics.getHeight() / 16) + 160, 400, 40);
        shapeRenderer.rect(100, ((float) Gdx.graphics.getHeight() / 16) + 240, 400, 40);
        shapeRenderer.rect(100, ((float) Gdx.graphics.getHeight() / 16) + 320, 400, 40);
        shapeRenderer.rect(100, ((float) Gdx.graphics.getHeight() / 16) + 400, 400, 40);
        shapeRenderer.rect(100, ((float) Gdx.graphics.getHeight() / 16) + 480, 400, 40);
        shapeRenderer.rect(100, ((float) Gdx.graphics.getHeight() / 16) + 560, 400, 40);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        createPlayer();

        for (int i = 0; i < idList.size(); i++) {
            player[i].setText(idList.get(i));
            player[i].setColor(StyleGuide.colors[i]);
        }

        searchCounter++;
        if (searchCounter == 60) {
            for (int i = idList.size(); i < player.length; i++) {
                player[i].setColor(StyleGuide.white);
                player[i].setText("Suchen .");
            }
        } else if (searchCounter == 120) {
            for (int i = idList.size(); i < player.length; i++) {
                player[i].setColor(StyleGuide.white);
                player[i].setText("Suchen ..");
            }
        } else if (searchCounter == 180) {
            for (int i = idList.size(); i < player.length; i++) {
                player[i].setColor(StyleGuide.white);
                player[i].setText("Suchen ...");
            }
            searchCounter = 0;
        }

    }


    void createPlayer() {
        int count = 560;
        for (int i = 0; i < player.length; i++) {
            player[i].setPosition(120, ((float) Gdx.graphics.getHeight() / 16) + count);
            player[i].setSize(400, 40);
            Constants.stage.addActor(player[i]);
            count -= 80;
        }
    }
}