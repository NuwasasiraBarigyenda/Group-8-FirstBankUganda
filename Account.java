// parent class for all account types in First Bank Uganda
// abstract = cant be created directly, must use a subclass like SavingsAccount
// all subclasses inherit the fields and summary() from here - no need to repeat them

public abstract class Account { // abstract = cannot be used directly, only through subclasses

    // fields every account type shares - written once here so subclasses dont repeat them
    // protected so subclasses can read them directly e.g this.firstName
    protected String accountNumber, firstName, lastName, nationalId;
    protected String email, phone, dob, branch;
    protected double openingDeposit;

    // constructor - subclasses call super(...) to fill in all these shared fields
    public Account(String accountNumber, String firstName, String lastName,
                   String nationalId, String email, String phone,
                   String dob, String branch, double openingDeposit) {
        this.accountNumber  = accountNumber; // generated account number e.g KLA-2026-000001
        this.firstName      = firstName;     // customer first name
        this.lastName       = lastName;      // customer last name
        this.nationalId     = nationalId;    // NIN - 14 char national ID
        this.email          = email;         // email address
        this.phone          = phone;         // phone number
        this.dob            = dob;           // date of birth e.g 2000-01-15
        this.branch         = branch;        // branch e.g Kampala
        this.openingDeposit = openingDeposit; // amount paid when opening
    }

    // abstract = each subclass must provide its own version - that is polymorphism
    // e.g Savings returns 50000, Current returns 200000, each one is different
    public abstract double minimumDeposit();

    // each subclass returns its own name e.g "Savings", "Current"
    // used inside summary() below to build the output line
    public abstract String accountTypeName();

    // builds the display line shown in the GUI summary area after submitting
    // format matches exactly what the question paper example shows
    public String summary() {
        return String.format(
            "ACC: %s | %s %s | %s | %s | DOB %s | %s | Deposit %,.0f | %s",
            accountNumber, firstName, lastName, accountTypeName(),
            branch, dob, phone, openingDeposit, email
        );
    }
}
