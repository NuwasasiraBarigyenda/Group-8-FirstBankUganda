// fixed deposit account - min deposit 1,000,000 UGX - highest of all types
// money is locked for a fixed term, earns the highest interest

public class FixedDepositAccount extends Account { // extends Account = inherits all shared fields and summary()

    // passes all details up to the Account constructor
    public FixedDepositAccount(String a, String b, String c, String d,
                                String e, String f, String g, String h, double i) {
        super(a, b, c, d, e, f, g, h, i); // Account stores everything
    }

    // polymorphism - returns 1,000,000 for fixed deposit accounts
    @Override // replacing the abstract method from Account
    public double minimumDeposit() {
        return 1000000;
    }

    // returns the account type label used in the summary line
    @Override // replacing the abstract method from Account
    public String accountTypeName() {
        return "Fixed Deposit";
    }
}
