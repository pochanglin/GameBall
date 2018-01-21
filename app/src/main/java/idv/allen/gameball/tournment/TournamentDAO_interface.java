package idv.allen.gameball.tournment;

import java.util.List;

/**
 * Created by Java on 2017/12/22.
 */

public interface TournamentDAO_interface {
    public List<TournamentVO> getAllByMember(String mem_id);// 查詢單一會員所主辦賽事
    public List<TournamentVO> getAll();                     // 查詢所有賽事資料
    public TournamentVO getTournPoster(String tourn_id);
    public TournamentVO getOneTourn(String tourn_id);
}
