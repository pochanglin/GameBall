package idv.allen.gameball.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import idv.allen.gameball.ws.GamedayLiveWS;

import static android.content.Context.MODE_PRIVATE;

public class Util {
    private final static String TAG = "Util";
    public final static String URL_MEM = "http://10.120.39.16:8081/GAMEBALL_Android/android/MembershipServlet";
    public final static String URL_TOURN = "http://10.120.39.16:8081/GAMEBALL_Android/android/TournamentServlet";
    public final static String URL_GAMEDAY = "http://10.120.39.16:8081/GAMEBALL_Android/android/GamedayServlet";
    public final static String URL_TEAM = "http://10.120.39.16:8081/GAMEBALL_Android/android/TeamServlet";
    public final static String URL_ROSTER = "http://10.120.39.16:8081/GAMEBALL_Android/android/RosterServlet";
    public final static String URL_PLATE_APPEARANCE = "http://10.120.39.16:8081/GAMEBALL/android/Plate_appearanceServlet";
    public final static String PREF_FILE = "preference";
    public static final String BROADCAST_PLATE = "idv.allen.gameball.platefragment";
    public static final String WS_PLATE = "idv.allen.gameball.GamedayLiveWS";
    public static final String WS_GAME_START = "idv.allen.gameball.GamedayLiveWS.StartGame";
    public final static String SERVER_URI = "ws://10.120.39.16:8081/GAMEBALL/TogetherWS/";
    public final static String NOTISTARTGAMEWS_URI = "ws://10.120.39.16:8081/GAMEBALL_Android/NotiStartGameWS/";
    public static GamedayLiveWS gamedayLiveWS;



    public static byte[] bitmapToPNG(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 轉成PNG不會失真，所以quality參數值會被忽略
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //回傳縮放倍率 scale設為2 (寬變1/2，高變1/2)
    public static int getImageScale(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        int scale = 1;
        while (options.outWidth / scale >= width ||
                options.outHeight / scale >= height) {
            scale *= 2;
        }
        return scale;
    }
    // 建立WebSocket連線
    public static void connectServer(Context context, String userName) {
        URI uri = null;
        try {
            uri = new URI(SERVER_URI + userName);
            Log.e(TAG,uri.toString());
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (gamedayLiveWS == null) {
            gamedayLiveWS = new GamedayLiveWS(uri, context);
            gamedayLiveWS.connect();
        }
    }
    // 中斷WebSocket連線
    public static void disconnectServer() {
        if (gamedayLiveWS != null) {
            gamedayLiveWS.close();
            gamedayLiveWS = null;
        }
    }
    //取出會員id
    public static String getUserName(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("user", MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        Log.d(TAG, "userName = " + userName);
        return userName;
    }
}
