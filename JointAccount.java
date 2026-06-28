// joint account - min deposit 100,000 UGX
// two people share this account so a second NIN is required

public class JointAccount extends Account { // extends Account = inherits all shared fields and summary()

    // extra field only joint accounts have - the second person's national ID
    private String secondNin;

    // getter so DataBaseManager can put secondNin in its own CSV column
    public String getSecondNin() { return secondNin; }

    // passes shared details to Account constructor, stores secondNin separately
    public JointAccount(String a, String b, String c, String d, String e,
                        String f, String g, String h, double i, String secondNin) {
        super(a, b, c, d, e, f, g, h, i); // Account stores all the common fields
        this.secondNin = secondNin;        // only JointAccount needs this
    }

    // polymorphism - returns 100,000 for joint accounts
    @Override // replacing the abstract method from Account
    public double minimumDeposit() {
        return 100000;
    }

    // returns the account type label used in the summary line
    @Override // replacing the abstract method from Account
    public String accountTypeName() {
        return "Joint";
    }

    // overrides summary() to add the second NIN at the end of the display line
    // super.summary() gets the base line from Account, then we append to it
    @Override // replacing summary() from Account - polymorphism + inheritance together
    public String summary() {
        return super.summary() + " | Second NIN: " + secondNin;
    }
}
