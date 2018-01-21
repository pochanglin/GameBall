package idv.allen.gameball.roster;

/**
 * Created by Java on 2017/12/27.
 */

public class RosterVO {

    private String tourn_id;
    private String team_id;
    private String mem_id;
    private String mem_name;
    private String batting_order;
    private String field_position;
    private String roster_jersey;
    private String roster_startup;
    private String mem_pic_Base64;//for android

    // Zero-arg constructor
    public RosterVO() {

    }

    public String getMem_pic_Base64() {
        return mem_pic_Base64;
    }


    public void setMem_pic_Base64(String mem_pic_Base64) {
        this.mem_pic_Base64 = mem_pic_Base64;
    }


    // Getter & Setter
    public String getTourn_id() {
        return tourn_id;
    }

    public void setTourn_id(String tourn_id) {
        this.tourn_id = tourn_id;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getBatting_order() {
        return batting_order;
    }

    public void setBatting_order(String batting_order) {
        this.batting_order = batting_order;
    }

    public String getField_position() {
        return field_position;
    }

    public void setField_position(String field_position) {
        this.field_position = field_position;
    }

    public String getRoster_jersey() {
        return roster_jersey;
    }

    public void setRoster_jersey(String roster_jersey) {
        this.roster_jersey = roster_jersey;
    }

    public String getRoster_startup() {
        return roster_startup;
    }

    public void setRoster_startup(String roster_startup) {
        this.roster_startup = roster_startup;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((batting_order == null) ? 0 : batting_order.hashCode());
        result = prime * result + ((field_position == null) ? 0 : field_position.hashCode());
        result = prime * result + ((mem_id == null) ? 0 : mem_id.hashCode());
        result = prime * result + ((mem_name == null) ? 0 : mem_name.hashCode());
        result = prime * result + ((roster_jersey == null) ? 0 : roster_jersey.hashCode());
        result = prime * result + ((roster_startup == null) ? 0 : roster_startup.hashCode());
        result = prime * result + ((team_id == null) ? 0 : team_id.hashCode());
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
        RosterVO other = (RosterVO) obj;
        if (batting_order == null) {
            if (other.batting_order != null)
                return false;
        } else if (!batting_order.equals(other.batting_order))
            return false;
        if (field_position == null) {
            if (other.field_position != null)
                return false;
        } else if (!field_position.equals(other.field_position))
            return false;
        if (mem_id == null) {
            if (other.mem_id != null)
                return false;
        } else if (!mem_id.equals(other.mem_id))
            return false;
        if (mem_name == null) {
            if (other.mem_name != null)
                return false;
        } else if (!mem_name.equals(other.mem_name))
            return false;
        if (roster_jersey == null) {
            if (other.roster_jersey != null)
                return false;
        } else if (!roster_jersey.equals(other.roster_jersey))
            return false;
        if (roster_startup == null) {
            if (other.roster_startup != null)
                return false;
        } else if (!roster_startup.equals(other.roster_startup))
            return false;
        if (team_id == null) {
            if (other.team_id != null)
                return false;
        } else if (!team_id.equals(other.team_id))
            return false;
        if (tourn_id == null) {
            if (other.tourn_id != null)
                return false;
        } else if (!tourn_id.equals(other.tourn_id))
            return false;
        return true;
    }


}
