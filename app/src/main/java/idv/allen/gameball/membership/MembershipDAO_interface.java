package idv.allen.gameball.membership;


import java.util.Map;

public interface MembershipDAO_interface {
    boolean isMember(String userId, String password);
    MembershipVO findByPrimaryKey(String mem_id);
    MembershipVO getMemberByAcc(String account, String password);
    boolean update(MembershipVO membershipVO);
    Map<String,String> getAllRoster(String tourn_id,String team_id);
    Hitter_summaryVO getHitterSummary(String mem_id);
    Pitcher_summaryVO getPitcherSummary(String mem_id);

}
