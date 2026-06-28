// savings account - min deposit 50,000 UGX
// earns interest, no overdraft allowed

public class SavingsAccount extends Account { // extends Account = inherits all shared fields and summary()

    // passes all details up to the Account constructor - no extra fields needed here
    public SavingsAccount(String a, String b, String c, String d,
                          String e, String f, String g, String h, double i) {
        super(a, b, c, d, e, f, g, h, i); // Account stores everything
    }

    // polymorphism - each subclass returns its own minimum, this one returns 50,000
    @Override // replacing the abstract method from Account
    public double minimumDeposit() {
        return 50000.0;
    }

    // returns the account type label used in the summary line
    @Override // replacing the abstract method from Account
    public String accountTypeName() {
        return "Savings";
    }
}

