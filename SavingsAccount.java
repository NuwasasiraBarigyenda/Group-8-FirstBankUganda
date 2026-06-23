package com.firstbank.model;

public class SavingsAccount extends Account {
    public SavingsAccount(String a, String b, String c, String d, String e, String f, String g, String h, double i) {
        super(a,b,c,d,e,f,g,h,i);
    };
    public double minimumDeposit(){
        return 50000.0;}
        public String accountTypeName(){
            return "Savings";
        }
    }
