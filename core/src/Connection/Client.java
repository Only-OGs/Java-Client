package Connection;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class Client {

    public static Socket socket = null;

    public Client() {
    }

    /**
     * Stellt die Verbindung zum Server her und sorgt dafür, das wir dauerhaft Daten empfangen können.
     * Checkt gleichzeititg ob der Server Online oder Offline ist.
     */
    public static void connect() {

        try {

            // Erstellt eine Socket.IO-Verbindung
            socket = IO.socket("http://localhost:8080");

            // Überprüft ob wir Verbunden sind
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Verbunden mit dem Server\n");
                }
            });

            // Überprüft ob wir nicht mehr verbunden sind
            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Verbindung zum Server verloren\n");
                }
            });

            // Bekommt man Nachrichten vom Server übermittelt
            socket.on("response", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject obj = (JSONObject) args[0];
                    try {
                        System.out.println(obj.get("message"));

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            });


            // Verbindung herstellen
            socket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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
