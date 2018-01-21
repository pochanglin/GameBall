package idv.allen.gameball.plate_appearance;

/**
 * Created by Java on 2018/1/4.
 */

public class PitcherVO {
    private String jerseyNumber;//背號
    private String pitcherId;//投手編號
    private String pitcherName;//投手名稱
    private Integer totalPitch;//共投幾球
    private Integer strikePitch;//好球數(壞球可以用好球算)

    public PitcherVO() {
        super();
    }
    public String getJerseyNumber() {
        return jerseyNumber;
    }
    public void setJerseyNumber(String jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }
    public String getPitcherId() {
        return pitcherId;
    }
    public void setPitcherId(String pitcherId) {
        this.pitcherId = pitcherId;
    }
    public String getPitcherName() {
        return pitcherName;
    }
    public void setPitcherName(String pitcherName) {
        this.pitcherName = pitcherName;
    }
    public Integer getTotalPitch() {
        return totalPitch;
    }
    public void setTotalPitch(Integer totalPitch) {
        this.totalPitch = totalPitch;
    }
    public Integer getStrikePitch() {
        return strikePitch;
    }
    public void setStrikePitch(Integer strikePitch) {
        this.strikePitch = strikePitch;
    }
}
