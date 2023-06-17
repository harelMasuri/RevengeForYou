package com.example.revengeforyou;

public class Revenge {

    private String etNameOfRevenge;
    private String etWhoWillTakeRevenge;
    private String etWhatTheRevenge;
    private String etReasonForRevenge;
    private Boolean bIsDone;
    private String revengeId;

    public Revenge(String etNameOfRevenge, String etWhoWillTakeRevenge, String etWhatTheRevenge, String etReasonForRevenge) {
        this.etNameOfRevenge        = etNameOfRevenge;
        this.etWhoWillTakeRevenge   = etWhoWillTakeRevenge;
        this.etWhatTheRevenge       = etWhatTheRevenge;
        this.etReasonForRevenge     = etReasonForRevenge;
        this.bIsDone = false;
        this.revengeId              = "";
    }

    public Revenge() {
        bIsDone = false;
    }

    public String getEtNameOfRevenge() {
        return etNameOfRevenge;
    }

    public String getRevengeId() {
        return revengeId;
    }

    public void setRevengeId(String revengeId) {
        this.revengeId = revengeId;
    }

    public void setEtNameOfRevenge(String etNameOfRevenge) {
        this.etNameOfRevenge = etNameOfRevenge;
    }

    public String getEtWhoWillTakeRevenge() {
        return etWhoWillTakeRevenge;
    }

    public void setEtWhoWillTakeRevenge(String etWhoWillTakeRevenge) {
        this.etWhoWillTakeRevenge = etWhoWillTakeRevenge;
    }

    public String getEtWhatTheRevenge() {
        return etWhatTheRevenge;
    }

    public void setEtWhatTheRevenge(String etWhatTheRevenge) {
        this.etWhatTheRevenge = etWhatTheRevenge;
    }

    public String getEtReasonForRevenge() {
        return etReasonForRevenge;
    }

    public void setEtReasonForRevenge(String etReasonForRevenge) {
        this.etReasonForRevenge = etReasonForRevenge;
    }

    public Boolean getbIsDone() {
        return bIsDone;
    }

    public void setbIsDone(Boolean IsDone) {
        this.bIsDone = IsDone;
    }
}
