package idv.allen.gameball.team;

import java.io.Serializable;

public class TeamVO implements Serializable{

    private String team_id;
    private String team_name;
    private byte[] team_logo;
    private String team_intro;
    private String team_status;
    private String city_id;
    //for android
    private String team_logo_base64;

    //for android
    public String getTeam_logo_base64() {
        return team_logo_base64;
    }
    public void setTeam_logo_base64(String team_logo_base64) {
        this.team_logo_base64 = team_logo_base64;
    }
    public String getTeam_id() {
        return team_id;
    }
    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }
    public String getTeam_name() {
        return team_name;
    }
    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }
    public byte[] getTeam_logo() {
        return team_logo;
    }
    public void setTeam_logo(byte[] team_logo) {
        this.team_logo = team_logo;
    }
    public String getTeam_intro() {
        return team_intro;
    }
    public void setTeam_intro(String team_intro) {
        this.team_intro = team_intro;
    }
    public String getTeam_status() {
        return team_status;
    }
    public void setTeam_status(String team_status) {
        this.team_status = team_status;
    }
    public String getCity_id() {
        return city_id;
    }
    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

}