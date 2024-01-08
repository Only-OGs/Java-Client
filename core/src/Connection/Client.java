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
    public static String lobbyID;
    public static String[] playerAndMessage;
    public static String playerString;
    public static int timer = -1;
    public static boolean timerStatus = false;
    public static boolean waitGame = false;
    public static boolean startGame = false;
    public static boolean updatePos = false;
    public static boolean connect = false;
    public static JSONArray jsonArrayAssets;
    public static JSONArray jsonArrayUpdatePos;
    public static JSONArray jsonArrayLeaderboard;
    public static boolean start = false;
    public static String timerToStart = "";
    public static boolean showLeaderboard = false;

    /**
     * Stellt die Verbindung zum Server her und sorgt dafür, dass wir dauerhaft Daten empfangen können.
     * Checkt gleichzeitig ob der Server Online oder Offline ist.
     */
    public static void connect() {

        try {
            // IP-Adresse vom Server
            socket = IO.socket("http://89.58.1.158:8080");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Überprüft, ob wir Verbunden sind.
        socket.on(Socket.EVENT_CONNECT, args -> connect = true);

        // Überprüft, ob wir nicht mehr verbunden sind.
        socket.on(Socket.EVENT_DISCONNECT, args -> connect = false);

        // Bekommt man Login Informationen.
        socket.on("login", args -> {

            JSONObject obj = (JSONObject) args[0];
            try {

                // login_success oder login_failed
                loginStatus = (String) obj.get("status");

                // Nachricht vom Server
                loginMessage = (String) obj.get("message");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Bekommt man Logout Informationen.
        socket.on("logout", args -> {

            JSONObject obj = (JSONObject) args[0];
            try {

                // logout_success und logout_failed
                logoutStatus = (String) obj.get("status");

                // Nachricht vom Server: Benutzer abgemeldet.
                logoutMessage = (String) obj.get("message");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Bekommt man Informationen über den Registrierungsvorgang.
        socket.on("register", args -> {

            JSONObject obj = (JSONObject) args[0];
            try {

                // register_success und register_failed
                registerStatus = (String) obj.get("status");

                // Nachricht vom Server: Benutzer schon vorhanden.
                registerMessage = (String) obj.get("message");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Spiel mit Lobby Code suchen
        socket.on("search_lobby", args -> {

            JSONObject obj = (JSONObject) args[0];
            try {

                // success und failed
                searchLobbyCodeStatus = (String) obj.get("status");

                // Nachricht vom Server
                searchLobbyCodeMessage = (String) obj.get("message");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Schnelles Spiel suchen
        socket.on("get_lobby", args -> {

            JSONObject obj = (JSONObject) args[0];
            try {

                // success und failed
                quickStatus = (String) obj.get("status");

                // Nachricht vom Server
                quickMessage = (String) obj.get("message");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Hier werden Lobby Information an den Client geschickt.
        socket.on("lobby_management", args -> {

            JSONObject obj = (JSONObject) args[0];
            try {

                playerString = (String) obj.get("players");
                lobbyID = (String) obj.get("lobby");
                joinStatus = (String) obj.get("status");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if ("joined".equals(Client.joinStatus)) {
                LobbyScreen.idList = new ArrayList<>(Arrays.asList(playerString.split(";")));
                searchLobbyCodeMessage = "";
            }

        });

        // Wenn der Timer abgebrochen wird, löst es das Event aus.
        socket.on("timer_abrupt", args -> timerStatus = true);

        // Zeit die abläuft bis das Spiel startet.
        socket.on("timer_countdown", args -> timer = (int) args[0]);

        // Wenn das Spiel startet wird, löst es das Event aus.
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

        // Bekomme ich alle Sprites für die Umgebung geschickt.
        socket.on("load_assets", args -> jsonArrayAssets = (JSONArray) args[0]);

        // Schickt einmalig die Startposition der Spieler Autos.
        socket.on("wait_for_start", args -> {

            startGame = true;
            jsonArrayUpdatePos = (JSONArray) args[0];
        });

        // Schickt alle Daten rund um die AI autos, Spieler Autos, GameHUD sowie Sprite Information.
        socket.on("updated_positions", args -> {

            updatePos = true;
            jsonArrayUpdatePos = (JSONArray) args[0];
        });

        // Wird eine Chatnachricht erhalten mit ID und seine Nachricht.
        socket.on("new_message", args -> playerAndMessage = ((String) args[0]).split(";"));

        // Wenn eine Lobby erstellt wird, bekommt man eine Lobby ID zurück.
        socket.on("lobby_created", args -> {
            JSONObject obj = (JSONObject) args[0];

            try {
                lobbyID = (String) obj.get("message");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Wenn ein Spieler die Lobby verlässt, wird eine neue Liste gesendet mit den aktuellen Spielern.
        socket.on("player_leave", args -> {

            JSONObject obj = (JSONObject) args[0];
            try {
                playerString = (String) obj.get("message");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            LobbyScreen.idList = new ArrayList<>(Arrays.asList(Client.playerString.split(";")));
        });

        // Wird der Timer gesendet, wann die Spieler fahren dürfen.
        socket.on("start_race_timer", args -> timerToStart = args[0].toString());

        // Wird ausgeführt, wenn das Rennen starten darf.
        socket.on("start_race", args -> {

            start = true;
            start_watch();
        });

        // Sobald das Spiel zu Ende ist, bekommt man die Spieler Daten Position, Name und Zeit.
        socket.on("get_leaderboard", args -> {

            jsonArrayLeaderboard = (JSONArray) args[0];
            showLeaderboard = true;
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
        if (socket.connected()) socket.emit("logout");
    }

    public static void sendCreateLobby() {
        if (socket.connected()) socket.emit("create_lobby");
    }

    public static void joinLobby(String lobbyCode) throws JSONException {
        if (socket.connected()) {
            JSONObject obj = new JSONObject();
            obj.put("lobby", lobbyCode);
            socket.emit("join_lobby", obj);
        }
    }

    public static void sendMessage(String message) {
        if (socket.connected()) socket.emit("sent_message", message);
    }

    public static void leaveLobby() {
        if (socket.connected()) socket.emit("leave_lobby");
    }

    public static void getLobby() {
        if (socket.connected()) socket.emit("get_lobby");
    }

    public static void ready() {
        if (socket.connected()) socket.emit("is_ready");
    }

    public static void notReady() {
        if (socket.connected()) socket.emit("not_ready");
    }

    public static void clientReady() {
        if (socket.connected()) socket.emit("client_is_ingame");
    }

    public static void leaveGame() {
        if (socket.connected()) socket.emit("game_leave");
    }

    public static void start_watch(){
        if (socket.connected()) socket.emit("start_watch");
    }

    /**
     * Schickt dem Server immer die aktuelle Position des Spieler-Autos
     *
     * @param offset
     * @param pos
     * @throws JSONException
     */
    public static void ingamePos(float offset, double pos) throws JSONException {
        if (socket.connected()) {
            JSONObject obj = new JSONObject();
            obj.put("offset",offset);
            obj.put("pos",pos);
            socket.emit("ingame_pos", obj);
        }
    }
}