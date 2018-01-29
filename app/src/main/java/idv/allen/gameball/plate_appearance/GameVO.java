package idv.allen.gameball.plate_appearance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Java on 2018/1/4.
 */

public class GameVO implements Serializable  {
    //單場的數據
    String tourn_id;//賽事ID
    public String getTourn_id() {
        return tourn_id;
    }
    public void setTourn_id(String tourn_id) {
        this.tourn_id = tourn_id;
    }
    //不確定要不要放///
    Integer endFirst;//比賽結束時，先攻得分
    Integer endSecond;//比賽結束時，後攻得分
    //////////////
    String firstTeam;//先攻隊
    String secondTeam;//後攻隊
    String firstPitcher;//先攻先發
    String secondPitcher;//後攻先發
    String gamedayId;//場次
    String gameStatus;//比賽狀態 0未開始1進行中2已結束
    Integer inning;//局數
    ArrayList<Integer> firstPoint;//先攻分數
    ArrayList<Integer> secondPoint;//後攻分數
    Integer firstSeq;//先攻棒次(會記錄上一局打到第幾棒)
    Integer secondSeq;//後攻棒次
    //單局的數據(每局reset一次)
    Integer out;//出局數
    Integer currentRun;//這局得分
    //單人次的數據(每人次reset一次)
    Integer currentNumBall;//這人次投球數(最多4)
    Integer currentStrike;//這人次好球數
    Integer currentBall;//這人次壞球數
    //打者序列 HashMap<隊伍,HashMap<棒次,打者物件>>
    HashMap<String,HashMap<Integer,PlayerVO>> playerList;
    //投手打序資訊HashMap<隊伍,HashMap<名字,投手物件>
    HashMap<String,HashMap<String,PitcherVO>> pitcherList;
    //儲存錯誤訊息for android
    List<String> errorMsgs;

    public Integer getCurrentRun() {
        return currentRun;
    }
    public void setCurrentRun(Integer currentRun) {
        this.currentRun = currentRun;
    }
    public String getFirstPitcher() {
        return firstPitcher;
    }
    public void setFirstPitcher(String firstPitcher) {
        this.firstPitcher = firstPitcher;
    }
    public String getSecondPitcher() {
        return secondPitcher;
    }
    public void setSecondPitcher(String secondPitcher) {
        this.secondPitcher = secondPitcher;
    }
    public String getFirstTeam() {
        return firstTeam;
    }
    public void setFirstTeam(String firstTeam) {
        this.firstTeam = firstTeam;
    }
    public String getSecondTeam() {
        return secondTeam;
    }
    public void setSecondTeam(String secondTean) {
        this.secondTeam = secondTean;
    }

    public Integer getEndFirst() {
        return endFirst;
    }
    public void setEndFirst(Integer endFirst) {
        this.endFirst = endFirst;
    }
    public Integer getEndSecond() {
        return endSecond;
    }
    public void setEndSecond(Integer endSecond) {
        this.endSecond = endSecond;
    }
    public String getGamedayId() {
        return gamedayId;
    }
    public void setGamedayId(String gamedayId) {
        this.gamedayId = gamedayId;
    }
    public String getGameStatus() {
        return gameStatus;
    }
    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }
    public Integer getInning() {
        return inning;
    }
    public void setInning(Integer inning) {
        this.inning = inning;
    }
    public ArrayList<Integer> getFirstPoint() {
        return firstPoint;
    }
    public void setFirstPoint(ArrayList<Integer> firstPoint) {
        this.firstPoint = firstPoint;
    }
    public ArrayList<Integer> getSecondPoint() {
        return secondPoint;
    }
    public void setSecondPoint(ArrayList<Integer> secondPoint) {
        this.secondPoint = secondPoint;
    }
    public Integer getFirstSeq() {
        return firstSeq;
    }
    public void setFirstSeq(Integer firstSeq) {
        this.firstSeq = firstSeq;
    }
    public Integer getSecondSeq() {
        return secondSeq;
    }
    public void setSecondSeq(Integer secondSeq) {
        this.secondSeq = secondSeq;
    }
    public Integer getOut() {
        return out;
    }
    public void setOut(Integer out) {
        this.out = out;
    }
    public Integer getCurrentNumBall() {
        return currentNumBall;
    }
    public void setCurrentNumBall(Integer currentNumBall) {
        this.currentNumBall = currentNumBall;
    }
    public Integer getCurrentStrike() {
        return currentStrike;
    }
    public void setCurrentStrike(Integer currentStrike) {
        this.currentStrike = currentStrike;
    }
    public Integer getCurrentBall() {
        return currentBall;
    }
    public void setCurrentBall(Integer currentBall) {
        this.currentBall = currentBall;
    }
    public HashMap<String, HashMap<Integer, PlayerVO>> getPlayerList() {
        return playerList;
    }
    public void setPlayerList(HashMap<String, HashMap<Integer, PlayerVO>> playerList) {
        this.playerList = playerList;
    }
    public HashMap<String, HashMap<String, PitcherVO>> getPitcherList() {
        return pitcherList;
    }
    public void setPitcherList(HashMap<String, HashMap<String, PitcherVO>> pitcherList) {
        this.pitcherList = pitcherList;
    }
    public List<String> getErrorMsgs() {
        return errorMsgs;
    }
    public void setErrorMsgs(List<String> errorMsgs) {
        this.errorMsgs = errorMsgs;
    }
}
