package idv.allen.gameball.gameday;

import java.util.List;

public interface GamedayDAO_interface {
    List<GamedayVO> tournGetAll(String tourn_id);

    boolean updateTeamACheck(GamedayVO gamedayVO);

    boolean updateTeamBCheck(GamedayVO gamedayVO);

    void updateBatOrderFirst(String gameday_id, String team_id);

//    public TeamVO findByPK(String team_id);
}
