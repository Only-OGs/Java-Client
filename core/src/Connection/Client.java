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

    public static String loginStatus;

    public static String loginMessage;

    public static String logoutStatus;

    public static String logoutMessage;

    public static String registerStatus;

    public static String registerMessage;

    public static String joinMessage;

    public static String searchStatus;

    public static String players;

    public static String joinLobbyStatus;

    public static String lobbyID;

    public static String[] playerAndMessage;

    public static String playerString;

    public static boolean connect = false;

    public Client() {
    }

    /**
     * Stellt die Verbindung zum Server her und sorgt dafür, das wir dauerhaft Daten empfangen können.
     * Checkt gleichzeititg ob der Server Online oder Offline ist.
     */
    public static void connect() {

        try {
            socket = IO.socket("http://89.58.1.158:8080");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Überprüft, ob wir Verbunden sind.
        socket.on(Socket.EVENT_CONNECT, args -> {
            connect = true;
            System.out.println("Verbindung zum Server");
        });

        // Überprüft ob wir nicht mehr verbunden sind.
        socket.on(Socket.EVENT_DISCONNECT, args -> {
            connect = false;
            System.out.println("Verbindung zum Server verloren");
        });

        // Bekommt man Login Informationen
        socket.on("login", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {
                loginStatus = (String) obj.get("status"); // login_success und login_failed
                loginMessage = (String) obj.get("message"); // Nachricht vom Server wie Benutzer nicht vorhanden oder Passwort falsch
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Bekommt man Logout Informationen
        socket.on("logout", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {
                logoutStatus = (String) obj.get("status"); // logout_success und logout_failed
                logoutMessage = (String) obj.get("message"); // Nachricht vom Server Benutzer abgemeldet
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Bekommt man Infotainment über den Registrierungsvorgang
        socket.on("register", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {
                registerStatus = (String) obj.get("status"); // register_success und register_failed
                registerMessage = (String) obj.get("message"); // Nachricht vom Server wie Benutzer schon vorhanden
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Bekommt man Informationen über das schnelles Spiel
        socket.on("search_lobby", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {
                searchStatus = (String) obj.get("status"); // register_success und  register_failed
                joinMessage = (String) obj.get("message");// Nachricht vom Server wie Benutzer schon vorhanden
                players = (String) obj.get("players"); // Liste der Spieler

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Hier werden Lobby Information an den Client geschickt
        socket.on("lobby_management", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {
                playerString = (String) obj.get("players");
                joinLobbyStatus = (String) obj.get("status"); // failed und joined
                lobbyID = (String) obj.get("lobby");
                joinMessage = (String) obj.get("message");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if ("joined".equals(Client.joinLobbyStatus)) {
                LobbyScreen.idList = new ArrayList<>(Arrays.asList(playerString.split(";")));
                joinMessage = "";
            }

        });

        // Wenn eine Lobby erstellt wird, bekommt man eine Lobby ID zurück
        socket.on("lobby_created", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {
                lobbyID = (String) obj.get("message");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Wird eine Chatnachricht erhalten mit ID udn seine Nachricht
        socket.on("new_message", args -> {
            playerAndMessage = ((String) args[0]).split(";");
        });

        // Wenn ein Spieler die Lobby verlässt, wird eie neue Liste gesendet mit dn aktuellen Spielern
        socket.on("player_leave", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {
                playerString = (String) obj.get("message");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            LobbyScreen.idList = new ArrayList<>(Arrays.asList(Client.playerString.split(";")));
        });

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

    public static void sendMessage(String message) {
        if (socket.connected()) {
            socket.emit("sent_message", message);
        }
    }

    public static void leaveLobby() {
        if (socket.connected()) {
            socket.emit("leave_lobby");
        }
    }

    public static void getLobby() {
        if (socket.connected()) {
            socket.emit("get_lobby");
        }
    }
}
