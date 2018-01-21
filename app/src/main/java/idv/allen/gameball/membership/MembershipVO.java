package idv.allen.gameball.membership;

import java.sql.Date;

public class MembershipVO {
    private String mem_id;
    private String mem_acc;
    private String mem_psw;
    private String mem_name;
    private String mem_nickname;
    private byte[] mem_pic;
    private String mem_email;
    private String mem_height;
    private String mem_weight;
    private Date mem_birth;
    private String mem_filed;
    private String mem_bt;
    private Date mem_apdate;
    private String mem_intro;
    private String mem_status;

    public String getMem_id() {
        return mem_id;
    }
    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }
    public String getMem_acc() {
        return mem_acc;
    }
    public void setMem_acc(String mem_acc) {
        this.mem_acc = mem_acc;
    }
    public String getMem_psw() {
        return mem_psw;
    }
    public void setMem_psw(String mem_psw) {
        this.mem_psw = mem_psw;
    }
    public String getMem_name() {
        return mem_name;
    }
    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }
    public String getMem_nickname() {
        return mem_nickname;
    }
    public void setMem_nickname(String mem_nickname) {
        this.mem_nickname = mem_nickname;
    }
    public byte[] getMem_pic() {
        return mem_pic;
    }
    public void setMem_pic(byte[] mem_pic) {
        this.mem_pic = mem_pic;
    }
    public String getMem_email() {
        return mem_email;
    }
    public void setMem_email(String mem_email) {
        this.mem_email = mem_email;
    }
    public String getMem_height() {
        return mem_height;
    }
    public void setMem_height(String mem_height) {
        this.mem_height = mem_height;
    }
    public String getMem_weight() {
        return mem_weight;
    }
    public void setMem_weight(String mem_weight) {
        this.mem_weight = mem_weight;
    }
    public Date getMem_birth() {
        return mem_birth;
    }
    public void setMem_birth(Date mem_birth) {
        this.mem_birth = mem_birth;
    }
    public String getMem_filed() {
        return mem_filed;
    }
    public void setMem_filed(String mem_filed) {
        this.mem_filed = mem_filed;
    }
    public String getMem_bt() {
        return mem_bt;
    }
    public void setMem_bt(String mem_bt) {
        this.mem_bt = mem_bt;
    }
    public Date getMem_apdate() {
        return mem_apdate;
    }
    public void setMem_apdate(Date mem_apdate) {
        this.mem_apdate = mem_apdate;
    }
    public String getMem_intro() {
        return mem_intro;
    }
    public void setMem_intro(String mem_intro) {
        this.mem_intro = mem_intro;
    }
    public String getMem_status() {
        return mem_status;
    }
    public void setMem_status(String mem_status) {
        this.mem_status = mem_status;
    }
}
