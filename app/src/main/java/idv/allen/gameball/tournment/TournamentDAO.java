package idv.allen.gameball.tournment;

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

/**
 * Created by Java on 2017/12/22.
 */

public class TournamentDAO implements TournamentDAO_interface {
    private static final String TAG = "TournamentDAO";

    public List<TournamentVO> getAllByMember(String mem_id) {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getTournByMemId");
        jsonObject.addProperty("mem_id",mem_id);
        jsonObject.addProperty("imageSize",300);
        String outStr = jsonObject.toString();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_TOURN).openConnection();
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
            Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Type listType = new TypeToken<List<TournamentVO>>() {
            }.getType();
            return gson.fromJson(inStr.toString(),listType);
        } else {
            return null;
        }

    }

    @Override
    public List<TournamentVO> getAll() {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getAll");
        String outStr = jsonObject.toString();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_TOURN).openConnection();
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
            Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Type listType = new TypeToken<List<TournamentVO>>() {
            }.getType();
            return gson.fromJson(inStr.toString(),listType);
        } else {
            return null;
        }
    }

    @Override
    public TournamentVO getTournPoster(String tourn_id) {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getTournPoster");
        jsonObject.addProperty("tourn_id",tourn_id);
        jsonObject.addProperty("imageSize",300);
        String outStr = jsonObject.toString();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_TOURN).openConnection();
            connection.setDoInput(true); // allow inputs
            connection.setDoOutput(true); // allow outputs
            // 不知道請求內容大小時可以呼叫此方法將請求內容分段傳輸，設定0代表使用預設大小
//            connection.setChunkedStreamingMode(0);
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
            Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

            return gson.fromJson(inStr.toString(),TournamentVO.class);
        } else {
            return null;
        }

    }

    @Override
    public TournamentVO getOneTourn(String tourn_id) {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getTournByPK");
        jsonObject.addProperty("tourn_id",tourn_id);
        String outStr = jsonObject.toString();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_TOURN).openConnection();
            connection.setDoInput(true); // allow inputs
            connection.setDoOutput(true); // allow outputs
            // 不知道請求內容大小時可以呼叫此方法將請求內容分段傳輸，設定0代表使用預設大小
//            connection.setChunkedStreamingMode(0);
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
            Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            return gson.fromJson(inStr.toString(),TournamentVO.class);
        } else {
            return null;
        }

    }
}
