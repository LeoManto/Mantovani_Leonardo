package it.unibo.cautiousExplorerActors.supports;

import it.unibo.interaction.IssObserver;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;

@ClientEndpoint
public class IssWsSupport extends IssObservableCommSupport implements IssCommSupport {

    private String URL;
    private Session userSession = null;
    private AnswerAvailable answerSupport;

    public IssWsSupport(String url) {

        try{
            URL = url;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://"+url));
            answerSupport = new AnswerAvailable();
        }catch (Exception e){
            System.out.println("IssWsSupport | ERROR: " + e.getMessage());
        }

    }

    @OnOpen
    public void onOpen(Session userSession){
        Principal userPrincipal = userSession.getUserPrincipal();

        if(userPrincipal != null) {
            System.out.println("        IssWsSupport | onOpen user=" + userPrincipal.getName());
        }

        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason){
        System.out.println("IssWsSupport | closing websocket");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        JSONObject jsonObj = new JSONObject(message);

        if(jsonObj.has("endmove")) {
            String endmove = jsonObj.getString("endmove");
            String move = jsonObj.getString("move");

            if(!endmove.equals("notallowed")){
                answerSupport.put(endmove, move);
            }
        }else if(jsonObj.has("collision")){
            boolean collision = jsonObj.getBoolean("collision");
        }else if(jsonObj.has("sonarName")){

        }

        updateObservers(jsonObj);
    }

    @OnError
    public void disconnected(Session session, Throwable error){
        System.out.println("IssWsSupport | disconnected  " + error.getMessage());
    }

//===========================================================================


    @Override
    public void registerObserver(IssObserver obs) {

    }

    @Override
    public void removeObserver(IssObserver obs) {

    }

    @Override
    public void close() {
        try{
            userSession.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

//===============================================================================

    @Override
    public void forward(@NotNull String msg) {
        try{
            userSession.getBasicRemote().sendText(msg);
        }catch(IOException e){
            System.out.println("        IssWsSupport | ERROR forward  " + e.getMessage());
        }
    }

    @Override
    public void reply(@NotNull String s) {

    }

    @Override
    public void request(@NotNull String msg) {
        try {
            this.userSession.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            System.out.println("        IssWsSupport | request ERROR " + e.getMessage());
        }
    }

    @NotNull
    @Override
    public String requestSynch(@NotNull String msg) {
        request(msg);
        return answerSupport.get();
    }
}
