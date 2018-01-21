package idv.allen.gameball.plate_appearance;

import java.util.ArrayList;

import idv.allen.gameball.membership.MembershipVO;

/**
 * Created by Java on 2018/1/4.
 */

public class PlayerVO {
    private String jerseyNumber;//背號
    private String playerId;//打者編號
    private String playerName;//打者姓名
    private String battingOrder;//打者棒次
    private String fieldPosition;//守備位置
    private String playerBT;//投打習慣
    private ArrayList<Plate_appearanceVO> record;
    //for android
    private MembershipVO membershipVO;

    //for android
    public MembershipVO getMembershipVO() {
        return membershipVO;
    }
    public void setMembershipVO(MembershipVO membershipVO) {
        this.membershipVO = membershipVO;
    }
    public ArrayList<Plate_appearanceVO> getRecord() {
        return record;
    }
    public void setRecord(ArrayList<Plate_appearanceVO> record) {
        this.record = record;
    }
    public PlayerVO(){
        super();
    }
    public String getJerseyNumber() {
        return jerseyNumber;
    }
    public void setJerseyNumber(String jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }
    public String getPlayerId() {
        return playerId;
    }
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public String getBattingOrder() {
        return battingOrder;
    }
    public void setBattingOrder(String battingOrder) {
        this.battingOrder = battingOrder;
    }
    public String getFieldPosition() {
        return fieldPosition;
    }
    public void setFieldPosition(String fieldPosition) {
        this.fieldPosition = fieldPosition;
    }
    public String getPlayerBT() {
        return playerBT;
    }
    public void setPlayerBT(String playerBT) {
        this.playerBT = playerBT;
    }
}
