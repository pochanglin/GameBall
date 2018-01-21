package idv.allen.gameball.roster;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import idv.allen.gameball.util.Util;


public class RosterDAO implements RosterDAO_interface {
    private static final String TAG = "RosterDAO";

    @Override
    public List<RosterVO> getAll(String tourn_id, String team_id) {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findByPK");
        jsonObject.addProperty("tourn_id",tourn_id);
        jsonObject.addProperty("team_id",team_id);
        String outStr = jsonObject.toString();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_ROSTER).openConnection();
            connection.setDoInput(true); // allow inputs
            connection.setDoOutput(true); // allow outputs
            // 不知道請求內容大小時可以呼叫此方法將請求內容分段傳輸，設定0代表使用預設大小
            connection.setChunkedStreamingMode(0);
            connection.setUseCaches(false); // do not use a cached copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");

            OutputStream o = connection.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(o);
            BufferedWriter bw = new BufferedWriter(ow);
            //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(outStr);
            Log.d(TAG, "output: " + outStr);
            bw.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    inStr.append(line);
                }
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.d(TAG, "input: " + inStr);
        if (inStr != null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            Type listType = new TypeToken<List<RosterVO>>() {
            }.getType();
            return gson.fromJson(inStr.toString(),listType);
        } else {
            return null;
        }
    }

    @Override
    public void chagneRoster(List<RosterVO> rosterList) {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();
        Gson gson = new Gson();
        String rosterListJSON = gson.toJson(rosterList);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "check_roster");
        jsonObject.addProperty("rosterListJSON",rosterListJSON);

        String outStr = jsonObject.toString();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_ROSTER).openConnection();
            connection.setDoInput(true); // allow inputs
            connection.setDoOutput(true); // allow outputs
            // 不知道請求內容大小時可以呼叫此方法將請求內容分段傳輸，設定0代表使用預設大小
            connection.setChunkedStreamingMode(0);
            connection.setUseCaches(false); // do not use a cached copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");

            OutputStream o = connection.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(o);
            BufferedWriter bw = new BufferedWriter(ow);
            //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(outStr);
            Log.d(TAG, "output: " + outStr);
            bw.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    inStr.append(line);
                }
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.d(TAG, "input: " + inStr);
        if (inStr != null) {
//            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            Type listType = new TypeToken<List<RosterVO>>() {
            }.getType();
//            return gson.fromJson(inStr.toString(),listType);
        } else {
//            return null;
        }
    }
}
