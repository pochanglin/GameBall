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
    //不確定要不要放///
    private Integer totalRun;//得分R
    private Integer totalHit;//安打
    private Integer totalError;//失誤E
    //////////////
    private String firstTeam;//先攻隊
    private String secondTeam;//後攻隊
    private String firstPitcher;//先攻先發
    private String secondPitcher;//後攻先發
    private String gamedayId;//場次
    private String gameStatus;//比賽狀態 0未開始1進行中2已結束
    private Integer inning;//局數
    private ArrayList<Integer> firstPoint;//先攻分數
    private ArrayList<Integer> secondPoint;//後攻分數
    private Integer firstSeq;//先攻棒次(會記錄上一局打到第幾棒)
    private Integer secondSeq;//後攻棒次
    //單局的數據(每局reset一次)
    private Integer out;//出局數
    private Integer currentRun;//這局得分
    //單人次的數據(每人次reset一次)
    private Integer currentNumBall;//這人次投球數(最多4)
    private Integer currentStrike;//這人次好球數
    private Integer currentBall;//這人次壞球數
    //打者序列 HashMap<隊伍,HashMap<棒次,打者物件>>
    private HashMap<String,HashMap<Integer,PlayerVO>> playerList;
    //投手打序資訊HashMap<隊伍,HashMap<名字,投手物件>
    private HashMap<String,HashMap<String,PitcherVO>> pitcherList;
    //for android
    List<String> errorMsgs;
    //for android
    public List<String> getErrorMsgs() {
        return errorMsgs;
    }
    public void setErrorMsgs(List<String> errorMsgs) {
        this.errorMsgs = errorMsgs;
    }
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
    public Integer getTotalRun() {
        return totalRun;
    }
    public void setTotalRun(Integer totalRun) {
        this.totalRun = totalRun;
    }
    public Integer getTotalHit() {
        return totalHit;
    }
    public void setTotalHit(Integer totalHit) {
        this.totalHit = totalHit;
    }
    public Integer getTotalError() {
        return totalError;
    }
    public void setTotalError(Integer totalError) {
        this.totalError = totalError;
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

}
