/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programming.assignment.pkg1.loan.account.pkgclass;

/**
 *
 * @author iayfn
 */
public class LoanAccount {
    private double annualInterestRate;
    private double principal;
    private int months;
    
    public LoanAccount(double _principal, double _annualInterestRate, int _months) {
        principal = _principal;
        annualInterestRate = _annualInterestRate / 100.0;
        months = _months;
    }
    
    public double calculateMonthlyPayment() {
        double monthlyInterest = annualInterestRate / 12;
        double monthlyPayment = principal * ( monthlyInterest / (1 - Math.pow(1 + monthlyInterest, -months)));
        return monthlyPayment;
    }
    
    @Override
    public String toString(){
        return """
                Principal: $""" + LoanAccount.FP(principal) + """
                                                              
                Annual Interest Rate: """ + LoanAccount.FP(annualInterestRate * 100) + """
                %
                Term of Loan in Months: """ + months + """
                                                       
                Monthly Payment: $""" + LoanAccount.FP(calculateMonthlyPayment());
    }
    
    public static String FP(double number) {
        return String.format("%.2f", number);
    }
}
