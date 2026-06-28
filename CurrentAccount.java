// current account - min deposit 200,000 UGX
// overdraft allowed, no interest earned

public class CurrentAccount extends Account { // extends Account = inherits all shared fields and summary()

    // passes all details up to the Account constructor
    public CurrentAccount(String a, String b, String c, String d,
                          String e, String f, String g, String h, double i) {
        super(a, b, c, d, e, f, g, h, i); // Account stores everything
    }

    // polymorphism - returns 200,000 for current accounts
    @Override // replacing the abstract method from Account
    public double minimumDeposit() {
        return 200000;
    }

    // returns the account type label used in the summary line
    @Override // replacing the abstract method from Account
    public String accountTypeName() {
        return "Current";
    }
}


