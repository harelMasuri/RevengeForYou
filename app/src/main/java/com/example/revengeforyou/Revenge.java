package com.example.revengeforyou;

public class Revenge {

    private String NameOfRevenge;
    private String WhoWillTakeRevenge;
    private String WhatTheRevenge;
    private String ReasonForRevenge;

    public Revenge(String nameOfRevenge, String whoWillTakeRevenge, String whatTheRevenge, String reasonForRevenge) {
        NameOfRevenge = nameOfRevenge;
        WhoWillTakeRevenge = whoWillTakeRevenge;
        WhatTheRevenge = whatTheRevenge;
        ReasonForRevenge = reasonForRevenge;
    }

    public String getNameOfRevenge() {
        return NameOfRevenge;
    }

    public void setNameOfRevenge(String nameOfRevenge) {
        NameOfRevenge = nameOfRevenge;
    }

    public String getWhoWillTakeRevenge() {
        return WhoWillTakeRevenge;
    }

    public void setWhoWillTakeRevenge(String whoWillTakeRevenge) {
        WhoWillTakeRevenge = whoWillTakeRevenge;
    }

    public String getWhatTheRevenge() {
        return WhatTheRevenge;
    }

    public void setWhatTheRevenge(String whatTheRevenge) {
        WhatTheRevenge = whatTheRevenge;
    }

    public String getReasonForRevenge() {
        return ReasonForRevenge;
    }

    public void setReasonForRevenge(String reasonForRevenge) {
        ReasonForRevenge = reasonForRevenge;
    }
}
