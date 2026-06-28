import javax.swing.*;          // JFrame, JPanel, JButton, JTextField, JTextArea - all the GUI components
import java.awt.*;             // BorderLayout, GridLayout - controls how components are arranged
import java.time.*;            // LocalDate for today, Period to calculate age, YearMonth for leap years

// main GUI window - the account opening form for First Bank Uganda
// extends JFrame = inherits the window itself from Swing, same idea as subclasses extending Account

public class BankAccountForm extends JFrame {

    // text fields the user types into - one per required field
    JTextField fn        = new JTextField(); // first name
    JTextField ln        = new JTextField(); // last name
    JTextField nin       = new JTextField(); // national ID - 14 chars UPPERCASE
    JTextField email     = new JTextField(); // email address
    JTextField cemail    = new JTextField(); // confirm email - must match email
    JTextField phone     = new JTextField(); // phone - format +256XXXXXXXXX
    JTextField pin       = new JTextField(); // PIN - 4 to 6 digits, not all same
    JTextField cpin      = new JTextField(); // confirm PIN - must match pin
    JTextField deposit   = new JTextField(); // opening deposit in UGX
    JTextField secondNin = new JTextField(); // second NIN - only needed for joint accounts

    // date of birth split into 3 dropdowns as the question paper requires
    JComboBox<Integer> year  = new JComboBox<>();
    JComboBox<String>  month = new JComboBox<>(new String[]{
        "January","February","March","April","May","June",
        "July","August","September","October","November","December"});
    JComboBox<Integer> day   = new JComboBox<>(); // days update automatically based on month and year

    // exactly 5 account types and 5 branches as the question paper lists
    JComboBox<String> account = new JComboBox<>(
        new String[]{"Savings", "Current", "Fixed Deposit", "Student", "Joint"});
    JComboBox<String> branch  = new JComboBox<>(
        new String[]{"Kampala", "Gulu", "Mbarara", "Jinja", "Mbale"});

    // read only text area - labelled "Account Summary is Below:" as the question paper requires
    JTextArea summary = new JTextArea(6, 40);

    // submit and reset buttons
    JButton submit = new JButton("Submit");
    JButton reset  = new JButton("Reset");

    // constructor - builds and arranges all the GUI components when the window opens
    public BankAccountForm() {
        setTitle("First Bank Uganda - New Account Opening");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // GridLayout with 2 columns: label on left, input field on right
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));

        // add each label+field pair to the panel
        addField(p, "First Name",              fn);
        addField(p, "Last Name",               ln);
        addField(p, "National ID (NIN)",       nin);
        addField(p, "Email",                   email);
        addField(p, "Confirm Email",           cemail);
        addField(p, "Phone (+256...)",         phone);
        addField(p, "PIN (4-6 digits)",        pin);
        addField(p, "Confirm PIN",             cpin);
        addField(p, "Second NIN (Joint only)", secondNin);

        // fill year dropdown - range covers ages 18 to 75 as the question paper requires
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 75; i <= currentYear - 18; i++) year.addItem(i); // loop adds each valid birth year
        year.setSelectedItem(2000); // default selection

        updateDays(); // load the correct number of days for the default month and year

        // refresh the day dropdown whenever the user changes month or year
        year.addActionListener(e -> updateDays());
        month.addActionListener(e -> updateDays());

        p.add(new JLabel("Year"));         p.add(year);
        p.add(new JLabel("Month"));        p.add(month);
        p.add(new JLabel("Day"));          p.add(day);
        p.add(new JLabel("Account Type")); p.add(account);
        p.add(new JLabel("Branch"));       p.add(branch);
        addField(p, "Opening Deposit (UGX)", deposit);

        // wire buttons to their methods
        submit.addActionListener(e -> submitAction());
        reset.addActionListener(e -> resetForm());

        summary.setEditable(false); // user cannot type in the summary area

        // BorderLayout splits the window: form in center, buttons+summary at bottom
        setLayout(new BorderLayout());
        add(new JScrollPane(p), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel();
        buttons.add(submit);
        buttons.add(reset);
        south.add(buttons, BorderLayout.NORTH);
        south.add(new JLabel("Account Summary is Below:"), BorderLayout.CENTER);
        south.add(new JScrollPane(summary), BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
    }

    // helper - adds a label and a text field side by side into the panel
    private void addField(JPanel p, String name, JTextField t) {
        p.add(new JLabel(name));
        p.add(t);
    }

    // recalculates how many days to show based on the selected month and year
    // YearMonth.lengthOfMonth() handles leap years automatically
    // e.g Feb 2000 = 29 days, Feb 2001 = 28 days
    private void updateDays() {
        day.removeAllItems();
        if (year.getSelectedItem() == null) return; // guard: year not ready yet, skip to avoid crash
        int y = (Integer) year.getSelectedItem(); // cast Object to Integer - JComboBox stores Objects
        int m = month.getSelectedIndex() + 1;     // +1 because index 0 = January = month 1
        int days = YearMonth.of(y, m).lengthOfMonth(); // correct day count including leap years
        for (int i = 1; i <= days; i++) day.addItem(i); // loop fills the day dropdown
    }

    // runs when user clicks Submit - validates all fields then saves if everything is ok
    private void submitAction() {
        try {

            // first name: letters only, 2-30 chars
            String fnVal = fn.getText().trim();
            if (fnVal.isEmpty() || !fnVal.matches("[a-zA-Z]{2,30}"))
                throw new Exception("First Name: letters only, 2-30 characters");

            // last name: same rules as first name
            String lnVal = ln.getText().trim();
            if (lnVal.isEmpty() || !lnVal.matches("[a-zA-Z]{2,30}"))
                throw new Exception("Last Name: letters only, 2-30 characters");

            // NIN: exactly 14 uppercase letters and numbers
            String ninVal = nin.getText().trim();
            if (!ninVal.matches("[A-Z0-9]{14}"))
                throw new Exception("NIN must be exactly 14 alphanumeric UPPERCASE characters");

            // email: must be valid format and match the confirm field
            String emailVal = email.getText().trim();
            if (!emailVal.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
                throw new Exception("Invalid email format");
            if (!emailVal.equals(cemail.getText().trim())) // both fields must be identical
                throw new Exception("Emails do not match");

            // phone: Ugandan format +256 followed by exactly 9 digits
            String phoneVal = phone.getText().trim();
            if (!phoneVal.matches("\\+256\\d{9}"))
                throw new Exception("Phone must be in format +256XXXXXXXXX");

            // PIN: digits only, 4-6 long, must not be all the same digit e.g 0000
            String pinVal = pin.getText().trim();
            if (!pinVal.matches("\\d{4,6}"))
                throw new Exception("PIN must be 4-6 numeric digits");
            if (pinVal.matches("(\\d)\\1{3,5}")) // regex checks if every digit is the same
                throw new Exception("PIN cannot be all identical digits e.g 0000");
            if (!pinVal.equals(cpin.getText().trim())) // confirm must match
                throw new Exception("PINs do not match");

            // build date string from the 3 dropdowns e.g "2000-01-15"
            String dob = String.format("%04d-%02d-%02d",
                year.getSelectedItem(),
                month.getSelectedIndex() + 1,
                day.getSelectedItem());

            // calculate exact age in years from DOB to today
            LocalDate birth = LocalDate.parse(dob);
            int age = Period.between(birth, LocalDate.now()).getYears();

            if (age < 18 || age > 75) // age range rule from the question paper
                throw new Exception("Age must be between 18 and 75 (yours is " + age + ")");

            // parse deposit - replace commas first in case user typed 50,000 instead of 50000
            double dep;
            try {
                dep = Double.parseDouble(deposit.getText().trim().replace(",", ""));
            } catch (NumberFormatException e) {
                throw new Exception("Opening deposit must be a number");
            }

            String type      = (String) account.getSelectedItem(); // cast Object to String - JComboBox always holds Strings here
            String branchVal = (String) branch.getSelectedItem();  // same cast for branch
            String accNo     = AccountNumberGenerator.generate(branchVal); // generate the account number

            // polymorphism: create the correct subclass depending on account type chosen
            // each subclass overrides minimumDeposit() with its own amount - that is polymorphism in action
            Account a;
            switch (type) {

                case "Savings":
                    a = new SavingsAccount(accNo, fnVal, lnVal, ninVal,
                            emailVal, phoneVal, dob, branchVal, dep);
                    break;

                case "Fixed Deposit":
                    a = new FixedDepositAccount(accNo, fnVal, lnVal, ninVal,
                            emailVal, phoneVal, dob, branchVal, dep);
                    break;

                case "Student":
                    if (age > 25) // student accounts have a tighter age rule: 18-25 only
                        throw new Exception("Student account age must be 18-25 (yours is " + age + ")");
                    a = new StudentAccount(accNo, fnVal, lnVal, ninVal,
                            emailVal, phoneVal, dob, branchVal, dep);
                    break;

                case "Joint":
                    // joint account needs a second NIN - validate it the same way as the first
                    String snVal = secondNin.getText().trim();
                    if (snVal.isEmpty())
                        throw new Exception("Joint account requires a Second NIN");
                    if (!snVal.matches("[A-Z0-9]{14}"))
                        throw new Exception("Second NIN must be 14 alphanumeric UPPERCASE characters");
                    a = new JointAccount(accNo, fnVal, lnVal, ninVal,
                            emailVal, phoneVal, dob, branchVal, dep, snVal);
                    break;

                default: // Current account - the one type not listed as its own case above
                    a = new CurrentAccount(accNo, fnVal, lnVal, ninVal,
                            emailVal, phoneVal, dob, branchVal, dep);
                    break;
            }

            // polymorphism: a.minimumDeposit() calls the right subclass version automatically
            // e.g if a is SavingsAccount it returns 50000, if CurrentAccount it returns 200000
            if (dep < a.minimumDeposit())
                throw new Exception("Minimum deposit for " + type
                    + " is UGX " + String.format("%,.0f", a.minimumDeposit())
                    + "\nYou entered: UGX " + String.format("%,.0f", dep));

            // show the formatted summary line in the read only area
            // JointAccount overrides summary() to also include the second NIN
            String rec = a.summary();
            summary.append(rec + "\n");

            // save to CSV - passing the account object so DataBaseManager splits it into proper columns
            DataBaseManager.save(a);

            JOptionPane.showMessageDialog(this,
                "Account created!\nAccount Number: " + accNo);

        } catch (Exception ex) {
            // any failed validation throws an exception - caught here and shown to the user
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // resets all fields back to empty/default when user clicks Reset
    private void resetForm() {
        // loop clears all text fields at once instead of clearing them one by one
        JTextField[] fields = {fn, ln, nin, email, cemail, phone, pin, cpin, deposit, secondNin};
        for (JTextField f : fields) f.setText("");

        // reset all dropdowns back to their first item (index 0)
        year.setSelectedIndex(0);
        month.setSelectedIndex(0);
        day.setSelectedIndex(0);
        account.setSelectedIndex(0);
        branch.setSelectedIndex(0); // index 0 = "Kampala"
    }

    // entry point - invokeLater is standard Swing practice to start the GUI on the right thread
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankAccountForm().setVisible(true));
    }
}
