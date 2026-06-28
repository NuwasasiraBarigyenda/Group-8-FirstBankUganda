// student account - min deposit 10,000 UGX - lowest of all types
// applicant must be aged 18-25 (that check is done in BankAccountForm)

public class StudentAccount extends Account { // extends Account = inherits all shared fields and summary()

    // passes all details up to the Account constructor
    public StudentAccount(String a, String b, String c, String d,
                          String e, String f, String g, String h, double i) {
        super(a, b, c, d, e, f, g, h, i); // Account stores everything
    }

    // polymorphism - returns 10,000 for student accounts
    @Override // replacing the abstract method from Account
    public double minimumDeposit() {
        return 10000.0;
    }

    // returns the account type label used in the summary line
    @Override // replacing the abstract method from Account
    public String accountTypeName() {
        return "Student";
    }
}
