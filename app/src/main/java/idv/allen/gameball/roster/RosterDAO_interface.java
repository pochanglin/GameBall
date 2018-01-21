package idv.allen.gameball.roster;

import java.util.List;

/**
 * Created by Java on 2017/12/27.
 */

public interface RosterDAO_interface {
    List<RosterVO> getAll(String tourn_id, String team_id);
    void chagneRoster(List<RosterVO> rosterList);

}
