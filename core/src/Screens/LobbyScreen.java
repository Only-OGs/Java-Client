package Screens;

import Connection.Client;
import OGRacerGame.OGRacerGame;
import Root.StyleGuide;
import Screens.MenuArea.LobbyMenu;
import Screens.MenuArea.MultiplayerMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private Stage stage = new Stage(viewport);
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

    private Texture muteIcon = new Texture("Icon/muteIcon.png");
    private Texture notMuteIcon = new Texture("Icon/unMuteIcon.png");

    private ImageButton muteButton;

    private boolean check = false;

    private int counter = 0;

    public LobbyScreen(String ID) {
        this.ID = ID;
        Gdx.input.setInputProcessor(stage);
        Constants.title.setText("Die Lobby");
        Constants.title.setSize(Gdx.graphics.getWidth(), 100);
        Constants.title.setPosition(0, stage.getHeight() - 100);
        Constants.title.setAlignment(Align.center);
        stage.addActor(Constants.title);

        addLabelPlayer();
        adddPlayerID();
        addChatLable();
        addLabelLobbyID();
        addLeaveButton();
        buildChat();
        buttonListener();
    }

    private void addMuteButton() {
        ImageButton.ImageButtonStyle muteButtonStyle = new ImageButton.ImageButtonStyle();
        muteButtonStyle.up = new TextureRegionDrawable(muteIcon);
        muteButtonStyle.up = new TextureRegionDrawable(notMuteIcon);
        muteButton = new ImageButton(muteButtonStyle);
        muteButton.setBounds(Gdx.graphics.getWidth() - 70, (float) Gdx.graphics.getHeight() / 4, 50, 50);
        stage.addActor(muteButton);
    }

    private void addChatLable() {
        chatLable = new Label("Chat: ", Constants.buttonSkin);
        chatLable.setSize(190, 40);
        chatLable.setPosition(stage.getWidth() / 1.9f, stage.getHeight() - 138);
        stage.addActor(chatLable);
    }

    public static String splitString(String input) {
        StringBuilder sb = new StringBuilder();
        int length = input.length();
        int index = 0;

        while (index < length) {
            if (index + 24 >= length) {
                sb.append(input.substring(index));
                break;
            }

            int nextSpace = input.lastIndexOf(" ", index + 24);
            if (nextSpace <= index || nextSpace >= index + 24) {
                sb.append(input, index, index + 24).append("-\n");
                index += 24;
            } else {
                sb.append(input, index, nextSpace).append("\n");
                index = nextSpace + 1;
            }
        }

        return sb.toString();
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

        messageField.setBounds(Gdx.graphics.getWidth() / 1.9f, 150, scrollChat.getWidth() - 65, 130);
        messageField.setTextFieldListener((textField, key) -> {
/*
            if(check){
                String tempS = textField.getText();
                String allNotLast = tempS.substring(0, Math.max(tempS.length() - 1, 0));
                char last = tempS.charAt(tempS.length()-1);
                tempS = allNotLast + "\n" +last;

                textField.setText("");
                textField.appendText(tempS);
                check = false;
            }


            counter = (int) Math.floor((double) textField.getText().length() /11);

            if(key == Input.Keys.BACKSPACE && textField.getText().length() == 1){
                textField.setText("");
            }

            if ((textField.getText().length() - counter) % 10 == 0 && textField.getText().length() > 0) {
                textField.appendText("\n");

            }

            /*

            StringBuilder temp = new StringBuilder("");
            for (int i = 0; i < textField.getText().length(); i++) {
                if (i % 11 == 0 && textField.getText().length() != 0) {
                    temp.append("\n");
                    if(textField.getText().length() < 10){
                        temp = new StringBuilder();
                    }
                }
                if (textField.getText().charAt(i) != '\n') {
                    temp.append(textField.getText().charAt(i));
                }
            }
            textField.setText("");
            textField.appendText(temp.toString());
 */

            /*
            String forTemp = textField.getText();
            forTemp.replaceAll("\n","");
            StringBuilder temp = new StringBuilder();
            for (int i = 0; i < forTemp.length(); i++) {

                if (i % 10 == 0 && i != 0) {
                    temp.append("\n");
                }

                if(forTemp.charAt(i) != '\n'){
                    temp.append(forTemp.charAt(i));
                }
            }
            textField.setText("");
            textField.appendText(temp.toString());
             */

        });

        sendMessageButton.setTransform(true);
        sendMessageButton.setRotation(270);
        sendMessageButton.setBounds(messageField.getX() + messageField.getWidth() +10, messageField.getY() +messageField.getHeight(), 130, 55);
        stage.addActor(sendMessageButton);

        chatTable.add("                                                ").left().row();
        stage.addActor(scrollChat);
        stage.addActor(sendMessageButton);
        stage.addActor(messageField);
    }

    private boolean isScrollBarAtBottom() {
        float scrollY = scrollChat.getScrollY();
        float maxY = scrollChat.getActor().getHeight() - scrollChat.getHeight();
        return MathUtils.isEqual(scrollY, maxY);
    }

    public void adddPlayerID() {
        idLabel = new Label("ID: " + ID, Constants.buttonSkin);
        idLabel.setSize(190, 40);
        idLabel.setPosition(stage.getWidth() / 1.3f, stage.getHeight() - 65);
        stage.addActor(idLabel);
    }


    private void addLabelLobbyID() {

        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        font.setColor(Color.WHITE);

        lobbyCode = new Label("Lobby ID: ", style);
        lobbyCode.setSize(190, 40);
        lobbyCode.setPosition(stage.getWidth() / 1.2f, 25);
        stage.addActor(lobbyCode);
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

        if (!Client.connect) OGRacerGame.getInstance().setScreen(new MultiplayerMenu());

        updateField();
        addLobbyID();
        updateChat();

        if (isScrollBarAtBottom()) {
            scrollChat.layout(); // Das Layout aktualisieren, bevor wir scrollen
            scrollChat.scrollTo(0, 0, 0, 0); // Nach unten scrollen (0, 0)
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void updateChat() {
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

                for (int i = 0; i < idList.size(); i++) {

                    if (Client.playerAndMessage[0].equals(idList.get(i))) {
                        playerColor.setText(idList.get(i));
                        playerColor.setColor(StyleGuide.colors[i]);
                    }
                }

                chatTable.add(playerColor).left().row();
                chatTable.add(Client.playerAndMessage[1] + "\n").left().row();
                Client.playerAndMessage = null;
                Constants.messageReceived.play(0.05f);
            }
        }
    }


    private void buttonListener() {

        sendMessageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (messageField.getText().length() > 0) {
                    Client.sendMessage(splitString(messageField.getText()));
                }
            }
        });

        lobbyLeaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.clickButton.play(0.2f);
                idList = new ArrayList<>(8);
                Client.leaveLobby();
                Client.playerString = "";
                Client.joinLobbyStatus = "";
                OGRacerGame.getInstance().setScreen(new LobbyMenu(ID));
            }
        });

        /*
        muteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        */
    }

    private void addLeaveButton() {
        lobbyLeaveButton.setBounds(95, stage.getHeight() - 70, 170, 50);
        stage.addActor(lobbyLeaveButton);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
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
            stage.addActor(player[i]);
            count -= 80;
        }
    }
}
