package idv.allen.gameball.membership;

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
import java.util.Map;

import idv.allen.gameball.util.Util;

/**
 * Created by Java on 2017/12/22.
 */

public class MembershipDAO implements MembershipDAO_interface{
    private final static String TAG = "MembershipDAO";

    public boolean isMember(String account, String password) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "isMember");
        jsonObject.addProperty("account",account);
        jsonObject.addProperty("password",password);
        String outStr = jsonObject.toString();
//        MembershipVO membershipVO = new MembershipVO();
//        membershipVO.setMem_acc(account);
//        membershipVO.setMem_psw(password);
//        Gson gson = new Gson();
//        String outStr = gson.toJson(membershipVO);

        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_MEM).openConnection();
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
//                MembershipVO mem1 = new MembershipVO();
//                try {
//                    JSONObject jsonObj = new JSONObject(inStr.toString());
//                    mem1.setMem_acc(jsonObj.getString("mem_acc"));
//                    mem1.setMem_psw(jsonObj.getString("mem_psw"));
//                } catch (Exception e) {
//                    return false;
//                }

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
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            MembershipVO membershipVO = gson.fromJson(inStr.toString(),MembershipVO.class);
            //判斷回傳物件帳號密碼內容
            if (membershipVO.getMem_acc().equals("memIdError")||membershipVO.getMem_psw().equals("memPwdError")) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public MembershipVO findByPrimaryKey(String mem_id) {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findByPrimaryKey");
        jsonObject.addProperty("mem_id",mem_id);
        String outStr = jsonObject.toString();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_MEM).openConnection();
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
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            return gson.fromJson(inStr.toString(),MembershipVO.class);
        } else {
            return null;
        }
    }

    @Override
    public MembershipVO getMemberByAcc(String account, String password) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "isMember");
        jsonObject.addProperty("account",account);
        jsonObject.addProperty("password",password);
        String outStr = jsonObject.toString();

        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_MEM).openConnection();
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
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            MembershipVO membershipVO = gson.fromJson(inStr.toString(),MembershipVO.class);
            return membershipVO;
        } else {
            return null;
        }
    }

    @Override
    public boolean update(MembershipVO membershipVO) {

        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String membershipVOJSON = gson.toJson(membershipVO);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "update");
        jsonObject.addProperty("MembershipVO",membershipVOJSON);
        String outStr = jsonObject.toString();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_MEM).openConnection();
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
        gson.fromJson(inStr.toString(),Boolean.class);
        Log.d(TAG, "input: " + inStr);
//        if (inStr != null) {
//        Log.d(TAG,inStr.toString());
//        Log.d(TAG,Boolean.valueOf(""true"").toString());
//        String test = inStr.toString();
//        Log.d(TAG, "b : " + Boolean.valueOf(test).toString());
        return gson.fromJson(inStr.toString(),Boolean.class);
//        } else {
//            return false;
//        }

    }

    @Override
    public Map<String, String> getAllRoster(String tourn_id, String team_id) {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findAllRoster");
        jsonObject.addProperty("tourn_id",tourn_id);
        jsonObject.addProperty("team_id",team_id);
        String outStr = jsonObject.toString();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_MEM).openConnection();
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
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            Type listType = new TypeToken<Map<String, String>>() {}.getType();
            return gson.fromJson(inStr.toString(),listType);
        } else {
            return null;
        }
    }

    @Override
    public Hitter_summaryVO getHitterSummary(String mem_id) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "get_one_hitter_summary");
        jsonObject.addProperty("mem_id",mem_id);
        String outStr = jsonObject.toString();

        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_MEM).openConnection();
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
            Gson gson = new Gson();
            return gson.fromJson(inStr.toString(),Hitter_summaryVO.class);
        } else {
            return null;
        }
    }

    @Override
    public Pitcher_summaryVO getPitcherSummary(String mem_id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "get_one_pitcher_summary");
        jsonObject.addProperty("mem_id",mem_id);
        String outStr = jsonObject.toString();

        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();

        try {
            connection = (HttpURLConnection) new URL(Util.URL_MEM).openConnection();
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
            Gson gson = new Gson();
            return gson.fromJson(inStr.toString(),Pitcher_summaryVO.class);
        } else {
            return null;
        }
    }


}
