package Messenger;

import Connection.Client;
import Root.StyleGuide;
import Helpers.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;

public class MessageChat {

    private String ID;

    private Label chatLable;

    private Table chatWindow = new Table(Constants.buttonSkin);

    private ScrollPane scrollChat = new ScrollPane(chatWindow, Constants.buttonSkin);

    private TextButton sendButton = new TextButton("Senden", Constants.buttonSkin);

    private TextArea textfield = new TextArea("", Constants.buttonSkin);

    private final Stage stage;

    public MessageChat(String user, Stage stage) {
        this.ID = user;
        this.stage = stage;
        buttonListener();
    }

    /*
     * Hier wird ein Label für das Chatfenster erstellt.
     */
    private void addChatLable() {
        chatLable = new Label("Chat: ", Constants.buttonSkin);
        chatLable.setBounds(scrollChat.getX(), scrollChat.getY() + scrollChat.getHeight(), 190, 40);
        stage.addActor(chatLable);
    }

    /**
     * Hier wird das Chatfenster gebaut, sowie das Eingabefeld mit dme dazugehörigen Senden Button.
     */
    public void build() {
        Pixmap backgroundPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        backgroundPixmap.setColor(new Color(255, 255, 255, 0.08f)); // Hier setzt du die Hintergrundfarbe auf Rot
        backgroundPixmap.fill();
        Texture backgroundTexture = new Texture(backgroundPixmap);
        backgroundPixmap.dispose();

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
        chatWindow.setBackground(backgroundDrawable);

        textfield.setBounds(Gdx.graphics.getWidth() / 1.9f, ((float) Gdx.graphics.getHeight() / 16) + 80, 400 - 65, 130);

        sendButton.setTransform(true);
        sendButton.setRotation(270);
        sendButton.setBounds(textfield.getX() + textfield.getWidth() + 10, textfield.getY() + textfield.getHeight(), 130, 55);
        stage.addActor(sendButton);

        scrollChat.setBounds(Gdx.graphics.getWidth() / 1.9f, textfield.getY() + 160, 400, 360);
        scrollChat.setFadeScrollBars(false);
        scrollChat.setScrollbarsVisible(true);
        scrollChat.setScrollingDisabled(true, false); // Horizontales Scrollen deaktivieren
        chatWindow.add("").left().row();

        chatWindow.add("                                                ").left().row();

        addChatLable();
        stage.addActor(scrollChat);
        stage.addActor(sendButton);
        stage.addActor(textfield);
    }

    /*
     * Durch Ausführen der Methode wird ein Listener angestoßen, der überprüft, ob der Senden-Button geklickt wurde.
     */
    private void buttonListener() {
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (textfield.getText().length() > 0) {
                    Client.sendMessage(textfield.getText());
                }
            }
        });
    }

    /**
     * Hier wird überprüft, von wem eine Nachricht hereinkam und zeigt sie dementsprechend anders an.
     */
    public void update(ArrayList<String> idList) {

        if (Client.playerAndMessage != null) {
            Label playerColor = new Label("", Constants.buttonSkin);
            if (ID.equals(Client.playerAndMessage[0])) {
                for (int i = 0; i < idList.size(); i++) {
                    if (idList.get(i).equals(ID) || idList.get(i).equals(ID + "  -  READY")) {
                        playerColor.setText(ID);
                        playerColor.setColor(StyleGuide.colors[i]);
                    }
                }

                chatWindow.add(playerColor).right().row();
                chatWindow.add((convertMessage(Client.playerAndMessage[1])) + "\n").right().row();
                textfield.setText("");
                Client.playerAndMessage = null;

            } else {

                for (int i = 0; i < idList.size(); i++) {

                    if (Client.playerAndMessage[0].equals(idList.get(i)) || idList.get(i).equals(Client.playerAndMessage[0] + "  -  READY")) {
                        playerColor.setText(Client.playerAndMessage[0]);
                        playerColor.setColor(StyleGuide.colors[i]);
                    }
                }

                chatWindow.add(playerColor).left().row();
                chatWindow.add(convertMessage(Client.playerAndMessage[1]) + "\n").left().row();
                Client.playerAndMessage = null;
                Constants.messageReceived.play(0.05f);
            }
        }
    }

    /**
     * Sorgt dafür, dass wenn man eine Nachricht erhält das Nachrichtenfenster nach unten scrollt,
     * wenn man sich unten befindet.
     */
    public void updateScrollBar() {
        if (isScrollBarAtBottom()) {
            scrollChat.layout(); // Das Layout aktualisieren, bevor wir scrollen
            scrollChat.scrollTo(0, 0, 0, 0); // Nach unten scrollen (0, 0)
        }
    }

    /**
     * Sorgt dafür das automatisch gescrollt wird.
     */
    public boolean isScrollBarAtBottom() {
        float scrollY = scrollChat.getScrollY();
        float maxY = scrollChat.getActor().getHeight() - scrollChat.getHeight();
        return MathUtils.isEqual(scrollY, maxY);
    }

    /*
     *  Sorgt dafür das eine Chat Nachricht so bearbeitet wird, das sie passend im Chat angezeigt wird.
     */
    private String convertMessage(String input) {
        StringBuilder sb = new StringBuilder();
        int length = input.length();
        int index = 0;

        while (index < length) {
            if (index + 23 >= length) {
                sb.append(input.substring(index));
                break;
            }

            int nextSpace = input.lastIndexOf(" ", index + 23);
            if (nextSpace <= index || nextSpace >= index + 23) {
                sb.append(input, index, index + 23).append("-\n");
                index += 23;
            } else {
                sb.append(input, index, nextSpace).append("\n");
                index = nextSpace + 1;
            }
        }
        return sb.toString();
    }
}
