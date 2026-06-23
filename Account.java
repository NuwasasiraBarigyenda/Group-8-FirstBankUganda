package com.firstbank.model;
    public abstract class Account {
        protected String accountNumber, firstName, lastName, nationalId, email, phone, dob, branch;
        protected double openingDeposit;

        public Account(String accountNumber,String firstName,String lastName,String nationalId,
                       String email,String phone,String dob,String branch,double openingDeposit){
            this.accountNumber=accountNumber;
            this.firstName=firstName;
            this.lastName=lastName;
            this.nationalId=nationalId;
            this.email=email;
            this.phone=phone;
            this.dob=dob;
            this.branch=branch;
            this.openingDeposit=openingDeposit;
        }

        public abstract double minimumDeposit();
        public abstract String accountTypeName();

        public String summary(){
            return String.format(
                    "ACC: %s | %s %s | %s | %s | DOB %s | %s | Deposit %,.0f | %s",
                    accountNumber, firstName, lastName, accountTypeName(),
                    branch, dob, phone, openingDeposit, email
            );
        }
    }

