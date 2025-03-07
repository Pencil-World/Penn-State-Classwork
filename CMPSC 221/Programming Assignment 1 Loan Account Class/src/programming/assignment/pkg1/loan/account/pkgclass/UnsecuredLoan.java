/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programming.assignment.pkg1.loan.account.pkgclass;

/**
 *
 * @author iayfn
 */
public class UnsecuredLoan extends LoanAccount {
    public UnsecuredLoan(double _principal, double _annualInterestRate, int _months) {
        super(_principal, _annualInterestRate, _months);
    }
    
    @Override
    public String toString(){
        return "Unsecured Loan with: \n" + super.toString() + "\n\n";
    }
}
