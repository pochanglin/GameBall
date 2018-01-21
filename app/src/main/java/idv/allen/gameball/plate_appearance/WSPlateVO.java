package idv.allen.gameball.plate_appearance;

import java.util.List;



public class WSPlateVO {

    private List<Plate_appearanceVO> paList;
    private int type;
    private int currentInning;

    public WSPlateVO() {
    }

    public WSPlateVO(List<Plate_appearanceVO> paList, int type) {
        this.paList = paList;
        this.type = type;
    }

    public WSPlateVO(List<Plate_appearanceVO> paList, int type, int currentInning) {
        this.paList = paList;
        this.type = type;
        this.currentInning = currentInning;
    }

    public List<Plate_appearanceVO> getPaList() {
        return paList;
    }

    public void setPaList(List<Plate_appearanceVO> paList) {
        this.paList = paList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCurrentInning() {
        return currentInning;
    }

    public void setCurrentInning(int currentInning) {
        this.currentInning = currentInning;
    }
}
