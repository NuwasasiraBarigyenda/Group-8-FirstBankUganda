import java.io.File;         // checks if the file already exists so we only write headers once
import java.io.FileWriter;   // for writing/appending to a file
import java.io.PrintWriter;  // makes it easy to write text line by line

// saves each account record to a CSV file after successful submission
// CSV opens in Excel with proper columns - one column per field, headers on row 1
// each new account is added as a new row below the headers

public class DataBaseManager {

    // CSV column headers - written only once when the file is first created
    private static final String HEADERS =
        "Account Number,First Name,Last Name,Account Type,Branch,Date of Birth,Phone,Opening Deposit (UGX),Email,Second NIN";

    // wraps a value in quotes so commas inside names or emails dont break the CSV columns
    private static String q(String value) {
        return "\"" + value + "\"";
    }

    // account is the Account object containing all the data to save
    public static void save(Account account) {
        try {
            File file = new File("accounts.csv");
            boolean isNew = !file.exists(); // true if file doesnt exist yet - means we need to write headers first

            PrintWriter pw = new PrintWriter(new FileWriter(file, true)); // append=true adds rows, never overwrites

            if (isNew) pw.println(HEADERS); // write headers only on the very first save

            // pre-format deposit separately to avoid printf misreading the %,.0f with mixed %s args
            String depositFormatted = String.format("%,.0f", account.openingDeposit); // e.g 50,000

            // joint accounts have a second NIN - all others leave that column blank
            String secondNin = (account instanceof JointAccount)
                ? ((JointAccount) account).getSecondNin() // cast to JointAccount to access getSecondNin()
                : "";                                      // blank for Savings, Current, Fixed Deposit, Student

            // build the CSV row - each field quoted so commas inside values dont break columns
            pw.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                q(account.accountNumber),     // Account Number  e.g KLA-2026-000001
                q(account.firstName),         // First Name      e.g Henry
                q(account.lastName),          // Last Name       e.g Etinu
                q(account.accountTypeName()), // Account Type    e.g Savings
                q(account.branch),            // Branch          e.g Kampala
                q(account.dob),               // Date of Birth   e.g 2000-01-15
                q(account.phone),             // Phone           e.g +256772123456
                q(depositFormatted),          // Opening Deposit e.g 50,000
                q(account.email),             // Email           e.g henry@gmail.com
                q(secondNin)                  // Second NIN      only filled for Joint accounts
            );

            pw.close(); // close the writer to flush and save everything to disk

        } catch (Exception e) {
            // if saving fails, show a simple error so we know what went wrong
            javax.swing.JOptionPane.showMessageDialog(null,
                "Could not save record: " + e.getMessage(),
                "Save Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
