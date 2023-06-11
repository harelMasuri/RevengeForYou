package com.example.revengeforyou;

public class Revenge {

    private String etNameOfRevenge;
    private String etWhoWillTakeRevenge;
    private String etWhatTheRevenge;
    private String etReasonForRevenge;
    private Boolean revengeIsDone;

    public Revenge(String etNameOfRevenge, String etWhoWillTakeRevenge, String etWhatTheRevenge, String etReasonForRevenge) {
        this.etNameOfRevenge = etNameOfRevenge;
        this.etWhoWillTakeRevenge = etWhoWillTakeRevenge;
        this.etWhatTheRevenge = etWhatTheRevenge;
        this.etReasonForRevenge = etReasonForRevenge;
        this.revengeIsDone = false;
    }

    public Revenge() {
    }

    public String getEtNameOfRevenge() {
        return etNameOfRevenge;
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

    public Boolean RevengeIsDone() {
        return revengeIsDone;
    }

    public void setRevengeIsDone(Boolean revengeIsDone) {
        this.revengeIsDone = revengeIsDone;
    }
}
