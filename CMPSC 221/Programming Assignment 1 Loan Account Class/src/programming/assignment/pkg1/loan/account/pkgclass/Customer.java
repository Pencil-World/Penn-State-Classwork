/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programming.assignment.pkg1.loan.account.pkgclass;
import java.util.ArrayList;

/**
 *
 * @author iayfn
 */
public class Customer {
    private String firstName;
    private String lastName;
    private String SSN;
    private ArrayList<LoanAccount> loanAccounts;
    
    public Customer(String _firstName, String _lastName, String _SSN) {
        firstName = _firstName;
        lastName = _lastName;
        SSN = _SSN;
        loanAccounts = new ArrayList<LoanAccount>();
    }
        
    public void addLoanAccount(LoanAccount account) {
        loanAccounts.add(account);
    }
    
    public void printMonthlyReport() {
        System.out.println("Acount Report for Custormer: " + firstName + " " + lastName + " with SSN " + SSN + "\n");
        for (LoanAccount loanAccount: loanAccounts)
            System.out.println(loanAccount);
    }
}
