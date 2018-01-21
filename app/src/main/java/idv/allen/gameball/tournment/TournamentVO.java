package idv.allen.gameball.tournment;

import java.sql.Date;

public class TournamentVO {

    private String tourn_id;
    private String mem_id;
    private String tourn_name;
    private String tourn_team_amt;
    private Date tourn_sign_start;
    private Date tourn_sign_end;
    private Date tourn_start;
    private Date tourn_end;
    private String tourn_status;
    private Integer tourn_fee;
    private String tourn_system;
    private Integer tourn_roster_amt;
    private Integer tourn_tour_no;
    private Integer group_no;
    private String tourn_cancle;
    private String tourn_address;
    private String city_id;
    private String loc_latitude;
    private String loc_longitude;
    private byte[] tourn_poster;
    private String tourn_poster_base64;

    public String getTourn_poster_base64() {
        return tourn_poster_base64;
    }
    public void setTourn_poster_base64(String tourn_poster_base64) {
        this.tourn_poster_base64 = tourn_poster_base64;
    }
    // zero-arg Constructor
    public TournamentVO() {

    }
    // getter & setter
    public String getTourn_id() {
        return tourn_id;
    }

    public void setTourn_id(String tourn_id) {
        this.tourn_id = tourn_id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getTourn_name() {
        return tourn_name;
    }

    public void setTourn_name(String tourn_name) {
        this.tourn_name = tourn_name;
    }

    public String getTourn_team_amt() {
        return tourn_team_amt;
    }

    public void setTourn_team_amt(String tourn_team_amt) {
        this.tourn_team_amt = tourn_team_amt;
    }

    public Date getTourn_sign_start() {
        return tourn_sign_start;
    }

    public void setTourn_sign_start(Date tourn_sign_start) {
        this.tourn_sign_start = tourn_sign_start;
    }

    public Date getTourn_sign_end() {
        return tourn_sign_end;
    }

    public void setTourn_sign_end(Date tourn_sign_end) {
        this.tourn_sign_end = tourn_sign_end;
    }

    public Date getTourn_start() {
        return tourn_start;
    }

    public void setTourn_start(Date tourn_start) {
        this.tourn_start = tourn_start;
    }

    public Date getTourn_end() {
        return tourn_end;
    }

    public void setTourn_end(Date tourn_end) {
        this.tourn_end = tourn_end;
    }

    public String getTourn_status() {
        return tourn_status;
    }

    public void setTourn_status(String tourn_status) {
        this.tourn_status = tourn_status;
    }

    public Integer getTourn_fee() {
        return tourn_fee;
    }

    public void setTourn_fee(Integer tourn_fee) {
        this.tourn_fee = tourn_fee;
    }

    public String getTourn_system() {
        return tourn_system;
    }

    public void setTourn_system(String tourn_system) {
        this.tourn_system = tourn_system;
    }

    public Integer getTourn_roster_amt() {
        return tourn_roster_amt;
    }

    public void setTourn_roster_amt(Integer tourn_roster_amt) {
        this.tourn_roster_amt = tourn_roster_amt;
    }

    public Integer getTourn_tour_no() {
        return tourn_tour_no;
    }

    public void setTourn_tour_no(Integer tourn_tour_no) {
        this.tourn_tour_no = tourn_tour_no;
    }

    public Integer getGroup_no() {
        return group_no;
    }

    public void setGroup_no(Integer group_no) {
        this.group_no = group_no;
    }

    public String getTourn_cancle() {
        return tourn_cancle;
    }

    public void setTourn_cancle(String tourn_cancle) {
        this.tourn_cancle = tourn_cancle;
    }

    public String getTourn_address() {
        return tourn_address;
    }

    public void setTourn_address(String tourn_address) {
        this.tourn_address = tourn_address;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getLoc_latitude() {
        return loc_latitude;
    }

    public void setLoc_latitude(String loc_latitude) {
        this.loc_latitude = loc_latitude;
    }

    public String getLoc_longitude() {
        return loc_longitude;
    }

    public void setLoc_longitude(String loc_longitude) {
        this.loc_longitude = loc_longitude;
    }

    public byte[] getTourn_poster() {
        return tourn_poster;
    }

    public void setTourn_poster(byte[] tourn_poster) {
        this.tourn_poster = tourn_poster;
    }

}

