package com.example.backend.websocket;

import io.kubernetes.client.Metrics;
import io.kubernetes.client.openapi.ApiException;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ServerEndpoint("/websocket")
public class MetricWebsocket {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        session.getAsyncRemote().sendText("Received: " + message);
    }

    public void sendMessage(String message) throws IOException, ApiException {
        Metrics metrics = new Metrics();
        metrics.getPodMetrics("default");
        
    }
}
