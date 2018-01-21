package idv.allen.gameball.plate_appearance;

import org.json.JSONArray;

/**
 * Created by Java on 2018/1/4.
 */

public interface Plate_appearanceDAO_interface {
    GameVO startGame(String tourn_id,String gameday_id);
    String sendPitchResult(String gameday_id,String pitchResult);
    GameVO sendPlateResult(Plate_appearanceVO plate_appearanceVO);
    JSONArray SendPlateContent(String gameday_id, String dir,String rslt,String point);
    Void OverGame(String tourn_id,String gameday_id);
}
