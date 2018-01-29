package idv.allen.gameball.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Locale;

import idv.allen.gameball.R;
import idv.allen.gameball.activity.TournamentActivity;

import static idv.allen.gameball.util.Util.NOTISTARTGAMEWS_URI;

public class GameService extends Service {
    private static final String TAG = "GameService";
//    public static NotiStartGameWS notiStartGameWS;
    public static NotiStartGameWS2 notiStartGameWS2;
    private final static int NOTIFICATION_ID = 0;
    private PowerManager.WakeLock wakeLock;
    NotificationManager notificationManager;

    public GameService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        //connect to the websocket
        connectServer("james");
        return START_STICKY;
    }

    public  void connectServer(String userName) {
        URI uri = null;
        try {
            uri = new URI(NOTISTARTGAMEWS_URI + userName);
            Log.e(TAG,uri.toString());
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (notiStartGameWS2 == null) {
            notiStartGameWS2 = new NotiStartGameWS2(uri);
            notiStartGameWS2.connect();
        }
    }

    public static void disconnectServer() {
        if (notiStartGameWS2 != null) {
            notiStartGameWS2.close();
            notiStartGameWS2 = null;
        }
    }

    public void showNotification(String message){
        Log.d(TAG, "onNotificationMessage: " + message);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
        String tourn_name = jsonObject.get("tourn_name").getAsString();
        String gameday_name = jsonObject.get("gameday_name").getAsString();
        String teamA_name = jsonObject.get("teamA_name").getAsString();
        String teamB_name = jsonObject.get("teamB_name").getAsString();

        Intent intent = new Intent(this, TournamentActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo3);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                // 訊息面板的標題
                .setContentTitle(tourn_name+" "+gameday_name+" 開始比賽囉！")
                // 訊息面板的內容文字
                .setContentText(teamA_name+" V.S."+teamB_name)
                // 訊息的小圖示
                .setSmallIcon(R.drawable.logo3)
                // 訊息的大圖示
                .setLargeIcon(bitmap)
                // 使用者點了之後才會執行指定的Intent
                .setContentIntent(pendingIntent)
                // 加入音效
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                // 點擊後會自動移除狀態列上的通知訊息
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }

    @Override
    public void onDestroy() {
        wakeLock.release();
        disconnectServer();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class NotiStartGameWS2 extends WebSocketClient {
        private static final String TAG = "NotiStartGameWS2";
        private Gson gson;

        public NotiStartGameWS2(URI serverUri) {
            super(serverUri, new Draft_17());
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
        public void onMessage(String message) {
            Log.d(TAG, "onMessage: " + message);
            showNotification(message);
        }

        @Override
        public void onMessage(ByteBuffer message) {
            Log.d(TAG, "onMessage2: " + message);
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
    }
}
