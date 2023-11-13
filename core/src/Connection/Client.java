package Connection;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;


public class Client {

    public static Socket socket;


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
                System.out.println(obj.get("message"));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        });

        socket.on("connection_success", args -> System.out.println(Arrays.toString(args)));


        // Verbindung herstellen
        socket.connect();

    }

    /**
     * Übermittelt Daten an den Server mithilfe des JSON Formats
     */
    public static void emitCoordinate() throws JSONException {

        if (socket.connected()) {


            JSONObject obj = new JSONObject();
            obj.put("message1", "Hallo");
            obj.put("message2", "Pascal");
            socket.emit("message", obj);
        }
    }
}
