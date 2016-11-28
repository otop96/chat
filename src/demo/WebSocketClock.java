package demo;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.awt.SystemColor.text;

/**
 * Created by 152107W on 11/28/2016.
 */



@ServerEndpoint("/clock")
    public class WebSocketClock {
    static ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    @OnOpen
    public void showTime(Session session){

        System.out.println("Opened: " + session.getId());
        timer.scheduleAtFixedRate(
                () -> sendTime(session),0,1, TimeUnit.SECONDS);

    }

    private void sendTime(Session session){
        try{
            String data = "Time: " + LocalTime.now().format(timeFormatter);
            session.getBasicRemote().sendText(data);

        }
        catch(IOException ioe){

            ioe.printStackTrace();

        }
    }

    @OnMessage
    public void OnMessage(String txt, Session session)throws IOException{
        System.out.println("Message recieved " + text);
        session.getBasicRemote().sendText(txt);

    }

}
