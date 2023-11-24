package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Root.StyleGuide;
import Screens.MenuArea.LobbyMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

    FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private Stage chatStage = new Stage(viewport);
    private int searchCounter = 0;
    public static ArrayList<String> idList = new ArrayList<>(8);
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final String ID;
    private Label idLabel, lobbyCode, chatLable;
    private final Label[] player = new Label[8];
    private Table chatTable = new Table(Constants.buttonSkin);
    private ScrollPane scrollChat = new ScrollPane(chatTable, Constants.buttonSkin);
    private TextButton sendMessageButton = new TextButton("Senden", Constants.buttonSkin);

    private TextButton lobbyLeaveButton = new TextButton("Verlassen", Constants.buttonSkin);
    private TextArea messageField = new TextArea("", Constants.buttonSkin);


    public LobbyScreen(String ID) {
        this.ID = ID;
        Gdx.input.setInputProcessor(chatStage);
        Constants.title.setText("Die Lobby");
        Constants.stage.clear();
        Constants.title.setSize(Gdx.graphics.getWidth(), 100);
        Constants.title.setPosition(0, Constants.stage.getHeight() - 100);
        Constants.title.setAlignment(Align.center);
        Constants.stage.addActor(Constants.title);
        Constants.stage.addActor(Constants.title);
        addLabelPlayer();
        adddPlayerID();
        addChatLable();
        addLabelLobbyID();
        addLeaveButton();
        buildChat();
        buttonListener();

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

        messageField.setBounds(Gdx.graphics.getWidth() / 1.9f, 180, scrollChat.getWidth(), 100);

        sendMessageButton.setBounds(messageField.getX() + messageField.getWidth() - 122, messageField.getY() - 60, 130, 50);
        Constants.stage.addActor(sendMessageButton);

        chatTable.add("                                                ").left().row();
        chatStage.addActor(scrollChat);
        chatStage.addActor(sendMessageButton);
        chatStage.addActor(messageField);
    }

    private boolean isScrollBarAtBottom() {
        float scrollY = scrollChat.getScrollY();
        float maxY = scrollChat.getActor().getHeight() - scrollChat.getHeight();
        return MathUtils.isEqual(scrollY, maxY);
    }

    public void adddPlayerID() {
        idLabel = new Label("ID: " + ID, Constants.buttonSkin);
        idLabel.setSize(190, 40);
        idLabel.setPosition(Constants.stage.getWidth() / 1.3f, Constants.stage.getHeight() - 65);
        Constants.stage.addActor(idLabel);
    }


    private void addLabelLobbyID() {

        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        font.setColor(Color.WHITE);

        lobbyCode = new Label("Lobby ID: ", style);
        lobbyCode.setSize(190, 40);
        lobbyCode.setPosition(Constants.stage.getWidth() / 1.2f, 25);
        Constants.stage.addActor(lobbyCode);
    }

    private void addLobbyID() {
        lobbyCode.setText("Lobby ID: " + Client.lobbyID);

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
        addLobbyID();

        if (isScrollBarAtBottom()) {
            scrollChat.layout(); // Das Layout aktualisieren, bevor wir scrollen
            scrollChat.scrollTo(0, 0, 0, 0); // Nach unten scrollen (0, 0)
        }


        chatStage.act(Gdx.graphics.getDeltaTime());
        chatStage.draw();
        Constants.stage.act(Gdx.graphics.getDeltaTime());
        Constants.stage.draw();


        if (Client.playerAndMessage != null) {
            Label playerColor = new Label("", Constants.buttonSkin);
            if (ID.equals(Client.playerAndMessage[0])) {
                for (int i = 0; i < idList.size(); i++) {
                    if (ID.equals(idList.get(i))) {
                        playerColor.setText(ID);
                        playerColor.setColor(StyleGuide.colors[i]);
                    }
                }

                chatTable.add(playerColor).right().row();
                chatTable.add((Client.playerAndMessage[1]) + "\n").right().row();
                messageField.setText("");
                Client.playerAndMessage = null;

            } else {

                chatTable.add(Client.playerAndMessage[0]).left().row();
                chatTable.add(Client.playerAndMessage[1] + "\n").left().row();
                messageField.setText("");
                Client.playerAndMessage = null;
            }

        }


    }


    private void buttonListener() {

        sendMessageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (messageField.getText().length() > 0) {
                    Client.sendMessage(messageField.getText());
                }
            }
        });

        lobbyLeaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                idList =  new ArrayList<>(8);

                Client.leaveLobby();

                OGRacerGame.getInstance().setScreen(new LobbyMenu(ID));
            }
        });

    }

   private void addLeaveButton(){
       lobbyLeaveButton.setBounds(95, Constants.stage.getHeight() - 70, 170, 50);
       chatStage.addActor(lobbyLeaveButton);
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
