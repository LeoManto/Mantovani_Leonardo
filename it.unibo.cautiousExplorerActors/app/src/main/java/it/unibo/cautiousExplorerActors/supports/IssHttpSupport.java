package it.unibo.cautiousExplorerActors.supports;

import it.unibo.cautiousExplorerActors.interaction.IssObserver;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.URI;

public class IssHttpSupport implements IssCommSupport {

    private CloseableHttpClient httpClient;
    private String URL = "unknown";

    public IssHttpSupport(String url) {
        httpClient = HttpClients.createDefault();
        URL = url;
        System.out.println("        IssHttpSupport | created IssHttpSupport url=" + url  );
    }

    @Override
    public void registerObserver(IssObserver obs) {

    }

    @Override
    public void removeObserver(IssObserver obs) {

    }

    @Override
    public void close() {
        try{
            httpClient.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void forward(@NotNull String msg) {
        System.out.println( "        IssHttpSupport | forward:" + msg  );
        performRequest(msg);
    }

    @Override
    public void reply(@NotNull String s) {

    }

    @Override
    public void request(@NotNull String msg) {
        System.out.println( "        IssHttpSupport | request:" + msg  );
        performRequest(msg);
    }

    @NotNull
    @Override
    public String requestSynch(@NotNull String msg) {
        System.out.println( "        IssHttpSupport | requestSynch:" + msg  );
        return performRequest(msg);    //the answer is lost
    }

//=================================================================================================================

    private String performRequest(String msg) {
        boolean endmove = false;
        try{
            StringEntity entity = new StringEntity(msg);
            HttpUriRequest httpPost = RequestBuilder.post()
                    .setUri(new URI(URL))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Accept", "application/json")
                    .setEntity(entity)
                    .build();

            CloseableHttpResponse response = httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity());
            JSONObject jsonEndmove = new JSONObject(jsonStr);

            if(jsonEndmove.get("endmove") != null) {
                endmove = jsonEndmove.getBoolean("endmove");
            }
        }catch(Exception e){
            System.out.println("        IssHttpSupport | ERROR:" + e.getMessage());
        }
        return "" + endmove;
    }
}
