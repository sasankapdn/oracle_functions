package com.example.fn;

public class stateRates {

    String state;
    double rate;

    public void setState (String a) {
        state=a.toLowerCase();
    }

    public String getState(){
        return state;
    }

    public void setRate(String a){
        rate = Double.parseDouble(a);
    }

    public double getRate(){
        return rate;
    }

}
