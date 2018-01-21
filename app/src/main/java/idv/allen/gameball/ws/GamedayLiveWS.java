package idv.allen.gameball.ws;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Locale;

import idv.allen.gameball.util.Util;

public class GamedayLiveWS extends WebSocketClient {
    private static final String TAG = "GamedayLiveWS";
    private LocalBroadcastManager broadcastManager;
    private Gson gson;

    public GamedayLiveWS(URI serverURI, Context context) {
        super(serverURI,new Draft_17());
        broadcastManager = LocalBroadcastManager.getInstance(context);
        gson = new Gson();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        String text = String.format(Locale.getDefault(),
                "onOpen: Http status code = %d; status message = %s",
                handshakedata.getHttpStatus(),
                handshakedata.getHttpStatusMessage());
        Log.d(TAG, "onOpen: " + text);
    }

    @Override
    public void onMessage(String pa_appearance) {
        Log.d(TAG, "onMessage: " + pa_appearance);
        sendPlateBroadcast(pa_appearance);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        String text = String.format(Locale.getDefault(),
                "code = %d, reason = %s, remote = %b",
                code, reason, remote);
        Log.d(TAG, "onClose: " + text);
    }

    @Override
    public void onError(Exception ex) {
        Log.d(TAG, "onError: exception = " + ex.toString());
    }

    private void sendPlateBroadcast(String pa_appearance) {
        JsonObject jsonObject = gson.fromJson(pa_appearance, JsonObject.class);
        String type = jsonObject.get("type").getAsString();

        if ("start".equals(type)) {
            Intent intent = new Intent(Util.WS_GAME_START);
            intent.putExtra("pa_appearance", pa_appearance);
            broadcastManager.sendBroadcast(intent);
            Log.d(TAG,"發送廣播囉~~~");
        }
        if ("pa_result".equals(type)) {
            Intent intent = new Intent(Util.WS_PLATE);
            intent.putExtra("pa_appearance", pa_appearance);
            broadcastManager.sendBroadcast(intent);
            Log.d(TAG,"發送廣播囉~~~");
        }
        if ("end".equals(type)) {
            Intent intent = new Intent(Util.WS_GAME_START);
            intent.putExtra("pa_appearance", pa_appearance);
            broadcastManager.sendBroadcast(intent);
            Log.d(TAG,"發送廣播囉~~~");
        }



    }
}
