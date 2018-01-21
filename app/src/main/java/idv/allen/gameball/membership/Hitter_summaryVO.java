package idv.allen.gameball.membership;

public class Hitter_summaryVO {
    private String mem_id;
    private Integer hitter_summary_games;
    private Integer hitter_summary_panum;
    private Integer hitter_summary_1b;
    private Integer hitter_summary_2b;
    private Integer hitter_summary_3b;
    private Integer hitter_summary_hr;
    private Integer hitter_summary_hits;
    private Integer hitter_summary_rbi;
    private Integer hitter_summary_tb;
    private Integer hitter_summary_bb;
    private Integer hitter_summary_so;
    private Integer field_summary_error;

    public Hitter_summaryVO(){
    }

    public Hitter_summaryVO(String mem_id, int hitter_summary_games, int hitter_summary_panum, int hitter_summary_1b,
                            int hitter_summary_2b, int hitter_summary_3b, int hitter_summary_hr, int hitter_summary_hits,
                            int hitter_summary_rbi, int hitter_summary_tb, int hitter_summary_bb, int hitter_summary_so,
                            int field_summary_error) {
        this.mem_id = mem_id;
        this.hitter_summary_games = hitter_summary_games;
        this.hitter_summary_panum = hitter_summary_panum;
        this.hitter_summary_1b = hitter_summary_1b;
        this.hitter_summary_2b = hitter_summary_2b;
        this.hitter_summary_3b = hitter_summary_3b;
        this.hitter_summary_hr = hitter_summary_hr;
        this.hitter_summary_hits = hitter_summary_hits;
        this.hitter_summary_rbi = hitter_summary_rbi;
        this.hitter_summary_tb = hitter_summary_tb;
        this.hitter_summary_bb = hitter_summary_bb;
        this.hitter_summary_so = hitter_summary_so;
        this.field_summary_error = field_summary_error;
    }
    public String getMem_id() {
        return mem_id;
    }
    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }
    public int getHitter_summary_games() {
        return hitter_summary_games;
    }
    public void setHitter_summary_games(int hitter_summary_games) {
        this.hitter_summary_games = hitter_summary_games;
    }
    public int getHitter_summary_panum() {
        return hitter_summary_panum;
    }
    public void setHitter_summary_panum(int hitter_summary_panum) {
        this.hitter_summary_panum = hitter_summary_panum;
    }
    public int getHitter_summary_1b() {
        return hitter_summary_1b;
    }
    public void setHitter_summary_1b(int hitter_summary_1b) {
        this.hitter_summary_1b = hitter_summary_1b;
    }
    public int getHitter_summary_2b() {
        return hitter_summary_2b;
    }
    public void setHitter_summary_2b(int hitter_summary_2b) {
        this.hitter_summary_2b = hitter_summary_2b;
    }
    public int getHitter_summary_3b() {
        return hitter_summary_3b;
    }
    public void setHitter_summary_3b(int hitter_summary_3b) {
        this.hitter_summary_3b = hitter_summary_3b;
    }
    public int getHitter_summary_hr() {
        return hitter_summary_hr;
    }
    public void setHitter_summary_hr(int hitter_summary_hr) {
        this.hitter_summary_hr = hitter_summary_hr;
    }
    public int getHitter_summary_hits() {
        return hitter_summary_hits;
    }
    public void setHitter_summary_hits(int hitter_summary_hits) {
        this.hitter_summary_hits = hitter_summary_hits;
    }
    public int getHitter_summary_rbi() {
        return hitter_summary_rbi;
    }
    public void setHitter_summary_rbi(int hitter_summary_rbi) {
        this.hitter_summary_rbi = hitter_summary_rbi;
    }
    public int getHitter_summary_tb() {
        return hitter_summary_tb;
    }
    public void setHitter_summary_tb(int hitter_summary_tb) {
        this.hitter_summary_tb = hitter_summary_tb;
    }
    public int getHitter_summary_bb() {
        return hitter_summary_bb;
    }
    public void setHitter_summary_bb(int hitter_summary_bb) {
        this.hitter_summary_bb = hitter_summary_bb;
    }
    public int getHitter_summary_so() {
        return hitter_summary_so;
    }
    public void setHitter_summary_so(int hitter_summary_so) {
        this.hitter_summary_so = hitter_summary_so;
    }
    public int getField_summary_error() {
        return field_summary_error;
    }
    public void setField_summary_error(int field_summary_error) {
        this.field_summary_error = field_summary_error;
    }
}
