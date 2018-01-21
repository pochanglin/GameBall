package idv.allen.gameball.gameday;


import java.sql.Timestamp;

import idv.allen.gameball.team.TeamVO;

public class GamedayVO {
    private String gameday_id;
    private String gameday_name;
    private String tourn_id;
    private String team_a_id;
    private String team_a_check;
    private String team_a_score;
    private String team_b_id;
    private String team_b_check;
    private String team_b_score;
    private String gameday_bat_first;
    private Timestamp gameday_time;
    private String gameday_status;
    //for android
    private TeamVO teamAVO;
    private TeamVO teamBVO;

    //for android
    public TeamVO getTeamAVO() {
        return teamAVO;
    }

    public void setTeamAVO(TeamVO teamAVO) {
        this.teamAVO = teamAVO;
    }

    public TeamVO getTeamBVO() {
        return teamBVO;
    }

    public void setTeamBVO(TeamVO teamBVO) {
        this.teamBVO = teamBVO;
    }

    // Zero-arg constructor
    public GamedayVO() {}

    // Getter & Setter
    public String getGameday_id() {
        return gameday_id;
    }

    public void setGameday_id(String gameday_id) {
        this.gameday_id = gameday_id;
    }

    public String getGameday_name() {
        return gameday_name;
    }

    public void setGameday_name(String gameday_name) {
        this.gameday_name = gameday_name;
    }

    public String getTourn_id() {
        return tourn_id;
    }

    public void setTourn_id(String tourn_id) {
        this.tourn_id = tourn_id;
    }

    public String getTeam_a_id() {
        return team_a_id;
    }

    public void setTeam_a_id(String team_a_id) {
        this.team_a_id = team_a_id;
    }

    public String getTeam_a_check() {
        return team_a_check;
    }

    public void setTeam_a_check(String team_a_check) {
        this.team_a_check = team_a_check;
    }

    public String getTeam_a_score() {
        return team_a_score;
    }

    public void setTeam_a_score(String team_a_score) {
        this.team_a_score = team_a_score;
    }

    public String getTeam_b_id() {
        return team_b_id;
    }

    public void setTeam_b_id(String team_b_id) {
        this.team_b_id = team_b_id;
    }

    public String getTeam_b_check() {
        return team_b_check;
    }

    public void setTeam_b_check(String team_b_check) {
        this.team_b_check = team_b_check;
    }

    public String getTeam_b_score() {
        return team_b_score;
    }

    public void setTeam_b_score(String team_b_score) {
        this.team_b_score = team_b_score;
    }

    public String getGameday_bat_first() {
        return gameday_bat_first;
    }

    public void setGameday_bat_first(String gameday_bat_first) {
        this.gameday_bat_first = gameday_bat_first;
    }

    public Timestamp getGameday_time() {
        return gameday_time;
    }

    public void setGameday_time(Timestamp gameday_time) {
        this.gameday_time = gameday_time;
    }

    public String getGameday_status() {
        return gameday_status;
    }

    public void setGameday_status(String gameday_status) {
        this.gameday_status = gameday_status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gameday_bat_first == null) ? 0 : gameday_bat_first.hashCode());
        result = prime * result + ((gameday_id == null) ? 0 : gameday_id.hashCode());
        result = prime * result + ((gameday_name == null) ? 0 : gameday_name.hashCode());
        result = prime * result + ((gameday_status == null) ? 0 : gameday_status.hashCode());
        result = prime * result + ((gameday_time == null) ? 0 : gameday_time.hashCode());
        result = prime * result + ((team_a_check == null) ? 0 : team_a_check.hashCode());
        result = prime * result + ((team_a_id == null) ? 0 : team_a_id.hashCode());
        result = prime * result + ((team_a_score == null) ? 0 : team_a_score.hashCode());
        result = prime * result + ((team_b_check == null) ? 0 : team_b_check.hashCode());
        result = prime * result + ((team_b_id == null) ? 0 : team_b_id.hashCode());
        result = prime * result + ((team_b_score == null) ? 0 : team_b_score.hashCode());
        result = prime * result + ((tourn_id == null) ? 0 : tourn_id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GamedayVO other = (GamedayVO) obj;
        if (gameday_bat_first == null) {
            if (other.gameday_bat_first != null)
                return false;
        } else if (!gameday_bat_first.equals(other.gameday_bat_first))
            return false;
        if (gameday_id == null) {
            if (other.gameday_id != null)
                return false;
        } else if (!gameday_id.equals(other.gameday_id))
            return false;
        if (gameday_name == null) {
            if (other.gameday_name != null)
                return false;
        } else if (!gameday_name.equals(other.gameday_name))
            return false;
        if (gameday_status == null) {
            if (other.gameday_status != null)
                return false;
        } else if (!gameday_status.equals(other.gameday_status))
            return false;
        if (gameday_time == null) {
            if (other.gameday_time != null)
                return false;
        } else if (!gameday_time.equals(other.gameday_time))
            return false;
        if (team_a_check == null) {
            if (other.team_a_check != null)
                return false;
        } else if (!team_a_check.equals(other.team_a_check))
            return false;
        if (team_a_id == null) {
            if (other.team_a_id != null)
                return false;
        } else if (!team_a_id.equals(other.team_a_id))
            return false;
        if (team_a_score == null) {
            if (other.team_a_score != null)
                return false;
        } else if (!team_a_score.equals(other.team_a_score))
            return false;
        if (team_b_check == null) {
            if (other.team_b_check != null)
                return false;
        } else if (!team_b_check.equals(other.team_b_check))
            return false;
        if (team_b_id == null) {
            if (other.team_b_id != null)
                return false;
        } else if (!team_b_id.equals(other.team_b_id))
            return false;
        if (team_b_score == null) {
            if (other.team_b_score != null)
                return false;
        } else if (!team_b_score.equals(other.team_b_score))
            return false;
        if (tourn_id == null) {
            if (other.tourn_id != null)
                return false;
        } else if (!tourn_id.equals(other.tourn_id))
            return false;
        return true;
    }
} // end class

