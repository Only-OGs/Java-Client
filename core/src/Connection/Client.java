package Connection;

import Screens.LobbyScreen;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;


public class Client {

    public static Socket socket;
    public static String status;

    public static String joinLobby;
    public static String statusMessage;

    public static String[] lastMessage;

    public static String player;

    public static boolean connect = false;

    public Client() {
    }

    /**
     * Stellt die Verbindung zum Server her und sorgt dafür, das wir dauerhaft Daten empfangen können.
     * Checkt gleichzeititg ob der Server Online oder Offline ist.
     */
    public static void connect() {


        try {
            socket = IO.socket("http://localhost:8080");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Überprüft ob wir Verbunden sind
        socket.on(Socket.EVENT_CONNECT, args -> {
            connect = true;
            System.out.println("Verbindung zum Server");
        });

        // Überprüft ob wir nicht mehr verbunden sind
        socket.on(Socket.EVENT_DISCONNECT, args ->{
            connect = false;
            System.out.println("Verbindung zum Server verloren");
        });

        // Bekommt man Nachrichten vom Server übermittelt
        socket.on("response", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {
               status = (String) obj.get("status");
               statusMessage = (String) obj.get("message");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        });

        // Wird eine Chat Nachricht
        socket.on("new_message", args -> {
            lastMessage = ((String) args[0]).split(";");
        });


        socket.on("player_joined", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {
                player = (String) obj.get("message");
                joinLobby = (String) obj.get("status");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String[] playerIDS = player.split(";");
            ArrayList<String> tempID = new ArrayList<>(8);

            // Gebe die getrennten Teile aus
            for (String teil : playerIDS) {
                System.out.println("Spieler: " +teil + "\n");
                tempID.add(teil);
            }
            LobbyScreen.idList = tempID;

        });

        socket.on("connection_success", args -> System.out.println(Arrays.toString(args)));

        // Verbindung herstellen
        socket.connect();
    }


    public static void sendRegisterData(String user, String password) throws JSONException {
        if (socket.connected()) {
            JSONObject obj = new JSONObject();
            obj.put("user", user);
            obj.put("password", password);
            socket.emit("register", obj);
        }
    }

    public static void sendLoginData(String user, String password) throws JSONException {
        if (socket.connected()) {
            JSONObject obj = new JSONObject();
            obj.put("user", user);
            obj.put("password", password);
            socket.emit("login", obj);
        }
    }

    public static void sendLogOut() throws JSONException {
        if (socket.connected()) {
            socket.emit("logout");
        }
    }

    public static void sendCreateLobby() {
        if (socket.connected()) {
            socket.emit("create_lobby");
        }
    }
    public static void joinLobby(String lobbyCode) throws JSONException {
        if (socket.connected()) {
            JSONObject obj = new JSONObject();
            obj.put("lobby", lobbyCode);
            socket.emit("join_lobby", obj);
        }
    }

    public static void sendMessage(String message){
        if (socket.connected()) {
            socket.emit("sent_message", message);
        }
    }
}
