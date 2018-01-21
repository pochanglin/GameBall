package idv.allen.gameball.plate_appearance;

public class Plate_appearanceVO implements java.io.Serializable {
    private String pa_id;
    private String mem_id;
    private String gameday_id;
    private Integer pa_out;
    private Integer inning;
    private String bat_dir_id;
    private String pa_rslt_id;
    private String pa_error_hitter;
    private String pa_pitcher;
    private Integer pa_rbi;
    private Integer pa_order;
    private int type;
    private PlayerVO playerVO;

    public Plate_appearanceVO() {
    }
    //只有第一球是必要所以先輸，如果有其他的再用set就好
    public Plate_appearanceVO(String pa_id,String mem_id, String gameday_id, Integer pa_out, Integer inning, String bat_dir_id,
                              String pa_rslt_id, String pa_error_hitter, String pa_pitcher, String ball_1, Integer pa_rbi,Integer pa_order) {
        this.pa_id =pa_id;
        this.mem_id = mem_id;
        this.gameday_id = gameday_id;
        this.pa_out = pa_out;
        this.inning = inning;
        this.bat_dir_id = bat_dir_id;
        this.pa_rslt_id = pa_rslt_id;
        this.pa_error_hitter = pa_error_hitter;
        this.pa_pitcher = pa_pitcher;
        this.pa_rbi = pa_rbi;
        this.pa_order=pa_order;
    }

    public PlayerVO getPlayerVO() {
        return playerVO;
    }

    public void setPlayerVO(PlayerVO playerVO) {
        this.playerVO = playerVO;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPa_id() {
        return pa_id;
    }
    public void setPa_id(String pa_id) {
        this.pa_id = pa_id;
    }
    public String getMem_id() {
        return mem_id;
    }
    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }
    public String getGameday_id() {
        return gameday_id;
    }
    public void setGameday_id(String gameday_id) {
        this.gameday_id = gameday_id;
    }
    public Integer getPa_out() {
        return pa_out;
    }
    public void setPa_out(Integer pa_out) {
        this.pa_out = pa_out;
    }
    public Integer getInning() {
        return inning;
    }
    public void setInning(Integer inning) {
        this.inning = inning;
    }
    public String getBat_dir_id() {
        return bat_dir_id;
    }
    public void setBat_dir_id(String bat_dir_id) {
        this.bat_dir_id = bat_dir_id;
    }
    public String getPa_rslt_id() {
        return pa_rslt_id;
    }
    public void setPa_rslt_id(String pa_rslt_id) {
        this.pa_rslt_id = pa_rslt_id;
    }
    public String getPa_error_hitter() {
        return pa_error_hitter;
    }
    public void setPa_error_hitter(String pa_error_hitter) {
        this.pa_error_hitter = pa_error_hitter;
    }
    public String getPa_pitcher() {
        return pa_pitcher;
    }
    public void setPa_pitcher(String pa_pitcher) {
        this.pa_pitcher = pa_pitcher;
    }
    public Integer getPa_rbi() {
        return pa_rbi;
    }
    public void setPa_rbi(Integer pa_rbi) {
        this.pa_rbi = pa_rbi;
    }

    public Integer getPa_order() {
        return pa_order;
    }
    public void setPa_order(Integer pa_order) {
        this.pa_order = pa_order;
    }
    @Override
    public String toString() {
        return  "PA_id:"+pa_id+
                " Member:"+mem_id+","+
                " Gameday:"+gameday_id+","+
                " PA_out:"+pa_out+","+
                " Inning:"+inning+","+
                " Bat_dir:"+bat_dir_id+","+
                " PA_rslt:"+pa_rslt_id+","+
                " PA_ERROR:"+pa_error_hitter+","+
                " PA_Pitcher:"+pa_pitcher+","+
                " RBI:"+pa_rbi+
                " PA_order:"+pa_order;
    }

}

