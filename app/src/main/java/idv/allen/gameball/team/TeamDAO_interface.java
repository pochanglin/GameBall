package idv.allen.gameball.team;

import java.util.List;

public interface TeamDAO_interface {
    TeamVO findByPK(String team_id);
    List<TeamVO> getAll();
    List<TeamVO> getTournAllTeam(String tourn_id);
    List<TeamVO> getAllMyTeam(String mem_id);

}
