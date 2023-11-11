package Connection;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Scanner;


public class Client {

    static int x;
    static int y;
    static int z;

    public static Socket socket = null;

    public Client() {
    }

    public static void connect() {

        try {

            // Erstellt eine Socket.IO-Verbindung
            socket = IO.socket("http://localhost:8080");

            // Überprüft ob wir verbunden sind
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

            socket.on("data", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject obj = (JSONObject)args[0];
                    try {
                        x = (int) obj.get("x");
                        y = (int) obj.get("y");
                        z = (int) obj.get("z");
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

    public static void emitCoordinate(int x, int y, int z) throws JSONException {

        if (socket.connected()) {

            // Sending an object
            JSONObject obj = new JSONObject();
            obj.put("x", x);
            obj.put("y", y);
            obj.put("Z", z);
            socket.emit("data", obj);
        }
    }

}
