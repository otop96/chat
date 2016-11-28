package demo;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/chat/{id}")
public class chat {
    private static Set<Session> allSessions = new HashSet<Session>();
    @OnOpen
    public void register(@PathParam("id") String id, Session session){
        System.out.println("Registering id : " + id + "for session: " + session.getId());
        session.getUserProperties().put("id", id);
        allSessions.add(session);
        try {
            session.getBasicRemote().sendText(id + " registered successfully!");
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String txt, Session session) throws IOException {
        System.out.println("Received : " + txt);
        //allSessions = session.getOpenSessions();
        System.out.println(session.getOpenSessions().size());
        for (Session s: allSessions) {
            try{
                String msg = (String)session.getUserProperties().get("id") + " >> " + txt;
                s.getBasicRemote().sendText(msg);
                System.out.println("Sent : " + msg);
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        allSessions.remove(session);
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());

    }
}