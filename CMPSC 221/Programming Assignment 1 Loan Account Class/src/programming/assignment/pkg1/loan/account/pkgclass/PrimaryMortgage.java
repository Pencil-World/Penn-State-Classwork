/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programming.assignment.pkg1.loan.account.pkgclass;

/**
 *
 * @author iayfn
 */
public class PrimaryMortgage extends LoanAccount {
    private double PMIMonthlyAmount;
    private Address address;
    
    public PrimaryMortgage(double _principal, double _annualInterestRate, int _months, double _PMIMonthlyAmount, Address _address) {
        super(_principal, _annualInterestRate, _months);
        PMIMonthlyAmount = _PMIMonthlyAmount;
        address = _address;
    }

    @Override
    public String toString(){
        return "Primary Mortgage Loan with: \n" + super.toString() + 
                "\nPMI Monthly Amount: $" + LoanAccount.FP(PMIMonthlyAmount) + 
                "\nProperty Address: " + address.toString() + "\n\n";
    }
}
