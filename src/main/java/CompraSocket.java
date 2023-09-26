import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.bson.Document;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@ServerEndpoint("/compra-realizada")
public class CompraSocket {

    Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message) {
        sessions.forEach(s -> s.getAsyncRemote().sendObject(message, result -> {
            if (result.getException() != null) {
                System.out.println("Unable to send message: " + result.getException());
            }
        }));
    }

    @ConsumeEvent("compra-enriquecida")
    public void onMessageVertx(Document message) {
        sessions.forEach(s -> s.getAsyncRemote().sendObject(message.toJson(), result -> {
            if (result.getException() != null) {
                System.out.println("Unable to send message: " + result.getException());
            }
        }));
    }

}
