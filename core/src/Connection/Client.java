package Connection;

import Road.RoadPart;
import Screens.GameScreen;
import Screens.LobbyScreen;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONArray;
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

    public static String searchLobbyCodeStatus;

    public static String searchLobbyCodeMessage;

    public static String quickStatus;

    public static String quickMessage;

    public static String joinStatus;

    public static String joinMessage;

    public static String lobbyID;

    public static String[] playerAndMessage;

    public static String playerString;

    public static int timer = -1;

    public static boolean timerStatus = false;

    public static boolean waitGame = false;

    public static boolean startGame = false;

    public static boolean updatePos = false;

    public static boolean connect = false;

    public static JSONArray jsonArrayStartPos;

    public static JSONArray jsonArrayUpdatePos;
    public static JSONArray jsonArrayLeaderboard;

    public static boolean start = false;
    public static String timerToStart = "";

    public static boolean showleaderboard;


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

        // Bekommt man Informationen über den Registrierungsvorgang
        socket.on("register", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {
                registerStatus = (String) obj.get("status"); // register_success und register_failed
                registerMessage = (String) obj.get("message"); // Nachricht vom Server wie Benutzer schon vorhanden
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });


        // Spiel mit Lobby Code suchen
        socket.on("search_lobby", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {

                searchLobbyCodeStatus = (String) obj.get("status"); // success und failed
                searchLobbyCodeMessage = (String) obj.get("message");// Nachricht vom Server

                System.out.println("Search IDLobby: " + searchLobbyCodeStatus);
                System.out.println("Search IDLobby: " + searchLobbyCodeMessage);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });


        // Schnelles Spiel suchen
        socket.on("get_lobby", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {

                quickStatus = (String) obj.get("status"); // success und failed
                quickMessage = (String) obj.get("message");// Nachricht vom Server

                System.out.println("Quick Lobby: " + quickStatus);
                System.out.println("Quick Lobby: " + quickMessage);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Hier werden Lobby Information an den Client geschickt
        socket.on("lobby_management", args -> {
            JSONObject obj = (JSONObject) args[0];
            try {
                playerString = (String) obj.get("players");
                lobbyID = (String) obj.get("lobby");
                //joinMessage = (String) obj.get("message");
                joinStatus = (String) obj.get("status");

                System.out.println("Managment: " + playerString);
                System.out.println("Managment: " + lobbyID);
                System.out.println("Managment: " + joinStatus);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if ("joined".equals(Client.joinStatus)) {
                LobbyScreen.idList = new ArrayList<>(Arrays.asList(playerString.split(";")));
                searchLobbyCodeMessage = "";
            }

        });

        // Wenn der Timer abgebrochen wird, löst es das Event aus
        socket.on("timer_abrupt", args -> {
            timerStatus = true;
        });

        // Zeit die abläuft bis das Spiel startet
        socket.on("timer_countdown", args -> {
            timer = (int) args[0];

        });

        // Wenn das Spiel startet wird, löst es das Event aus
        socket.on("load_level", args -> {
            waitGame = true;

            ArrayList<RoadPart> road = new ArrayList<>();

            try {
                // Erstelle ein JSONArray-Objekt aus dem JSON-String
                JSONArray jsonArrayString = (JSONArray) args[0];

                // Iteriere durch jedes JSON-Objekt im Array
                for (int i = 0; i < jsonArrayString.length(); i++) {
                    JSONObject jsonObj = jsonArrayString.getJSONObject(i);

                    // Greife auf die Werte der Schlüssel zu
                    String key1 = jsonObj.getString("segment_length");
                    String key2 = jsonObj.getString("curve_strength");
                    String key3 = jsonObj.getString("hill_height");
                    road.add(new RoadPart(key1, key2, key3));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GameScreen.roadBuilders = road;
        });

        // Schickt einmalig die Startposition
        socket.on("wait_for_start", args -> {

            startGame = true;
            // Erstelle ein JSONArray-Objekt aus dem JSON-String
            jsonArrayStartPos = (JSONArray) args[0];

        });

        // Schickt jedesmal etwas, wenn was im Spiel sich bewegt
        socket.on("updated_positions", args -> {
            updatePos = true;

            // Erstelle ein JSONArray-Objekt aus dem JSON-String
            jsonArrayUpdatePos = (JSONArray) args[0];

        });

        // Wird eine Chatnachricht erhalten mit ID udn seine Nachricht
        socket.on("new_message", args -> {
            playerAndMessage = ((String) args[0]).split(";");
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

        // Wird der Timer gesendet, wann die Spieler fahren dürfen
        socket.on("start_race_timer", args -> {
            timerToStart = args[0].toString();
        });

        // Wird ausgeführt, wenn das Rennen starten darf.
        socket.on("start_race", args -> {
            start = true;
            start_watch();
        });



        // Sobald das Spiel zuende ist, bekommt man die Spieler Daten Posi, Name und Zeit
        socket.on("get_leaderboard", args -> {
            showleaderboard = true;

            // Erstelle ein JSONArray-Objekt aus dem JSON-String
            jsonArrayLeaderboard = (JSONArray) args[0];

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

    public static void ready() {
        if (socket.connected()) {
            socket.emit("is_ready");
        }
    }

    public static void notReady() {
        if (socket.connected()) {
            socket.emit("not_ready");
        }
    }

    public static void clientReady() {
        if (socket.connected()) {
            socket.emit("client_is_ingame");
        }
    }

    public static void leaveGame() {
        if (socket.connected()) {
            socket.emit("game_leave");
        }
    }

    public static void start_watch(){
        if (socket.connected()) {
            socket.emit("start_watch");
        }
    }


    public static void ingamePos(float offset, double pos) throws JSONException {
        if (socket.connected()) {
            JSONObject obj = new JSONObject();
            obj.put("offset",offset);
            obj.put("pos",pos);
            socket.emit("ingame_pos", obj);
        }
    }
}