package idv.allen.gameball.membership;


public class Pitcher_summaryVO {
    private String mem_id;
    private Integer pitcher_summary_games;
    private Integer pitcher_summary_ip;
    private Integer pitcher_summary_pitches;
    private Integer pitcher_summary_strike;
    private Integer pitcher_summary_so;
    private Integer pitcher_summary_bb;
    private Integer pitcher_summary_runs;
    private Integer pitcher_summary_win;
    private Integer pitcher_summary_lose;
    public Pitcher_summaryVO() {
    }
    public Pitcher_summaryVO(String mem_id, int pitcher_summary_games, int pitcher_summary_ip,
                             int pitcher_summary_pitches, int pitcher_summary_strike, int pitcher_summary_so, int pitcher_summary_bb,
                             int pitcher_summary_runs, int pitcher_summary_win, int pitcher_summary_lose) {
        this.mem_id = mem_id;
        this.pitcher_summary_games = pitcher_summary_games;
        this.pitcher_summary_ip = pitcher_summary_ip;
        this.pitcher_summary_pitches = pitcher_summary_pitches;
        this.pitcher_summary_strike = pitcher_summary_strike;
        this.pitcher_summary_so = pitcher_summary_so;
        this.pitcher_summary_bb = pitcher_summary_bb;
        this.pitcher_summary_runs = pitcher_summary_runs;
        this.pitcher_summary_win = pitcher_summary_win;
        this.pitcher_summary_lose = pitcher_summary_lose;
    }


    public String getMem_id() {
        return mem_id;
    }
    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }
    public int getPitcher_summary_games() {
        return pitcher_summary_games;
    }
    public void setPitcher_summary_games(int pitcher_summary_games) {
        this.pitcher_summary_games = pitcher_summary_games;
    }
    public int getPitcher_summary_ip() {
        return pitcher_summary_ip;
    }
    public void setPitcher_summary_ip(int pitcher_summary_ip) {
        this.pitcher_summary_ip = pitcher_summary_ip;
    }
    public int getPitcher_summary_pitches() {
        return pitcher_summary_pitches;
    }
    public void setPitcher_summary_pitches(int pitcher_summary_pitches) {
        this.pitcher_summary_pitches = pitcher_summary_pitches;
    }
    public int getPitcher_summary_strike() {
        return pitcher_summary_strike;
    }
    public void setPitcher_summary_strike(int pitcher_summary_strike) {
        this.pitcher_summary_strike = pitcher_summary_strike;
    }
    public int getPitcher_summary_so() {
        return pitcher_summary_so;
    }
    public void setPitcher_summary_so(int pitcher_summary_so) {
        this.pitcher_summary_so = pitcher_summary_so;
    }
    public int getPitcher_summary_bb() {
        return pitcher_summary_bb;
    }
    public void setPitcher_summary_bb(int pitcher_summary_bb) {
        this.pitcher_summary_bb = pitcher_summary_bb;
    }
    public int getPitcher_summary_runs() {
        return pitcher_summary_runs;
    }
    public void setPitcher_summary_runs(int pitcher_summary_runs) {
        this.pitcher_summary_runs = pitcher_summary_runs;
    }
    public int getPitcher_summary_win() {
        return pitcher_summary_win;
    }
    public void setPitcher_summary_win(int pitcher_summary_win) {
        this.pitcher_summary_win = pitcher_summary_win;
    }
    public int getPitcher_summary_lose() {
        return pitcher_summary_lose;
    }
    public void setPitcher_summary_lose(int pitcher_summary_lose) {
        this.pitcher_summary_lose = pitcher_summary_lose;
    }
}
