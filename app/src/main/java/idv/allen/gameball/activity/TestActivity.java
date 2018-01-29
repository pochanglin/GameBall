package idv.allen.gameball.activity;

import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import idv.allen.gameball.R;

public class TestActivity extends AppCompatActivity {
    URI uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        try {
            uri = new URI("ws://10.120.39.16:8081/GAMEBALL/NotiStartGameWS/james");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ChatWebSocketClient webSocketClient = new ChatWebSocketClient(uri);
//        webSocketClient.connect();
    }

    public class ChatWebSocketClient extends WebSocketClient {
        private static final String TAG = "ChatWebSocketClient";
        private LocalBroadcastManager broadcastManager;
        private Gson gson;

        public ChatWebSocketClient(URI serverURI) {
            // Draft_17是連接協議，就是標準的RFC 6455（JSR256）
            super(serverURI, new Draft_17());
            gson = new Gson();
        }

        @Override
        public void onOpen(ServerHandshake handshakeData) {
            String text = String.format(Locale.getDefault(),
                    "onOpen: Http status code = %d; status message = %s",
                    handshakeData.getHttpStatus(),
                    handshakeData.getHttpStatusMessage());
            Log.d(TAG, "onOpen: " + text);
        }

        @Override
        public void onMessage(String message) {
//            JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
            // type: 訊息種類，有open(有user連線), close(有user離線), chat(其他user傳送來的聊天訊息)
//            String type = jsonObject.get("type").getAsString();
//            sendMessageBroadcast(type, message);
            Log.d(TAG, "onMessage: " + message);
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

//        private void sendMessageBroadcast(String messageType, String message) {
//            Intent intent = new Intent(messageType);
//            intent.putExtra("message", message);
//            broadcastManager.sendBroadcast(intent);
//        }
    }


}
