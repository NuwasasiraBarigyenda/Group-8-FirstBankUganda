package com.firstbank.ui;

import com.firstbank.model.*;
import com.firstbank.util.*;

import javax.swing.*;
import java.awt.*;
import java.time.*;

public class BankAccountForm extends JFrame {
    JTextField fn = new JTextField(),
            ln = new JTextField(), // Renamed 'In' to 'ln' for clarity
            nin = new JTextField(),
            email = new JTextField(),
            cemail = new JTextField(),
            phone = new JTextField(),
            pin = new JTextField(),
            cpin = new JTextField(),
            deposit = new JTextField(),
            secondNin = new JTextField();

    JComboBox<Integer> year = new JComboBox<>();
    JComboBox<String> month = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
    JComboBox<Integer> day = new JComboBox<>();
    JComboBox<String> account = new JComboBox<>(new String[]{"Savings", "Current", "Fixed Deposit", "Student", "Joint"});
    JComboBox<String> branch = new JComboBox<>(new String[]{"Kampala", "Gulu", "Mbarara", "Jinja", "Mbale"});

    JTextArea summary = new JTextArea(6, 40);
    JButton submit = new JButton("Submit");
    JButton reset = new JButton("Reset");

    public BankAccountForm() {
        setTitle("First Bank Uganda-New Account");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));

        addField(p, "First Name", fn);
        addField(p, "Last Name", ln);
        addField(p, "National ID", nin);
        addField(p, "Email", email);
        addField(p, "Confirm Email", cemail);
        addField(p, "Phone", phone);
        addField(p, "PIN", pin);
        addField(p, "Confirm PIN", cpin);
        addField(p, "Second NIN(Joint only)", secondNin);

        for (int i = 1950; i <= LocalDate.now().getYear(); i++) year.addItem(i);
        year.setSelectedItem(2000);
        updateDays();
        year.addActionListener(e -> updateDays());
        month.addActionListener(e -> updateDays());

        p.add(new JLabel("Year")); p.add(year);
        p.add(new JLabel("Month")); p.add(month);
        p.add(new JLabel("Day")); p.add(day);
        p.add(new JLabel("AccountType")); p.add(account);
        p.add(new JLabel("Branch")); p.add(branch);
        addField(p, "Opening Deposit", deposit);

        // Action Listeners
        submit.addActionListener(e -> submitAction());
        reset.addActionListener(e -> resetForm());

        JPanel buttons = new JPanel();
        buttons.add(submit);
        buttons.add(reset);

        summary.setEditable(false);

        setLayout(new BorderLayout());
        add(new JScrollPane(p), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.add(buttons, BorderLayout.NORTH);
        south.add(new JLabel("Account Summary is Below:"), BorderLayout.CENTER);
        south.add(new JScrollPane(summary), BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
    }

    private void addField(JPanel p, String name, JTextField t) {
        p.add(new JLabel(name));
        p.add(t);
    }

    private void updateDays() {
        day.removeAllItems();
        int y = (Integer) year.getSelectedItem();
        int m = month.getSelectedIndex() + 1;
        int days = YearMonth.of(y, m).lengthOfMonth();
        for (int i = 1; i <= days; i++) day.addItem(i);
    }

    private void submitAction() {
        try {
            if (fn.getText().trim().length() < 2) throw new Exception("Invalid first name");
            if (!email.getText().equals(cemail.getText())) throw new Exception("Emails do not match");
            if (!pin.getText().equals(cpin.getText())) throw new Exception("PINs do not match");

            String dob = String.format("%04d-%02d-%02d", year.getSelectedItem(), month.getSelectedIndex() + 1, day.getSelectedItem());
            LocalDate birth = LocalDate.parse(dob);
            int age = Period.between(birth, LocalDate.now()).getYears();
            if (age < 18 || age > 75) throw new Exception("Age must be between 18 and 75");

            double dep;
            try {
                dep = Double.parseDouble(deposit.getText());
            } catch (NumberFormatException e) {
                throw new Exception("Invalid deposit amount");
            }

            String type = (String) account.getSelectedItem();
            String accNo = AccountNumberGenerator.generate((String) branch.getSelectedItem());
            Account a;

            switch (type) {
                case "Savings":
                    a = new SavingsAccount(accNo, fn.getText(), ln.getText(), nin.getText(), email.getText(), phone.getText(), dob, (String) branch.getSelectedItem(), dep);
                    break;
                case "Fixed Deposit":
                    a = new FixedDepositAccount(accNo, fn.getText(), ln.getText(), nin.getText(), email.getText(), phone.getText(), dob, (String) branch.getSelectedItem(), dep);
                    break;
                case "Student":
                    if (age > 25) throw new Exception("Student account age is 18-25");
                    a = new StudentAccount(accNo, fn.getText(), ln.getText(), nin.getText(), email.getText(), phone.getText(), dob, (String) branch.getSelectedItem(), dep);
                    break;
                case "Joint":
                    if (secondNin.getText().trim().isEmpty()) throw new Exception("Joint account requires second NIN");
                    // Note: Ensure your JointAccount class exists or handle as needed
                    a = new Account(accNo, fn.getText(), ln.getText(), nin.getText(), email.getText(), phone.getText(), dob, (String) branch.getSelectedItem(), dep, secondNin.getText());
                    break;
                default: // Current
                    a = new Account(accNo, fn.getText(), ln.getText(), nin.getText(), email.getText(), phone.getText(), dob, (String) branch.getSelectedItem(), dep) {
                        @Override
                        public double minimumDeposit() {
                            return 0;
                        }

                        @Override
                        public String accountTypeName() {
                            return "";
                        }
                    };
            }

            if (dep < a.minimumDeposit()) throw new Exception("Minimum deposit is UGX " + (long) a.minimumDeposit());

            String rec = a.summary();
            summary.append(rec + "\n");
            DataBaseManager.save(rec);

            JOptionPane.showMessageDialog(this, "Account Created Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void resetForm() {
        JTextField[] fields = {fn, ln, nin, email, cemail, phone, pin, cpin, deposit, secondNin};
        for (JTextField f : fields) f.setText("");
        year.setSelectedIndex(0);
        month.setSelectedIndex(0);
        account.setSelectedIndex(0);
        branch.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankAccountForm().setVisible(true));
    }
}