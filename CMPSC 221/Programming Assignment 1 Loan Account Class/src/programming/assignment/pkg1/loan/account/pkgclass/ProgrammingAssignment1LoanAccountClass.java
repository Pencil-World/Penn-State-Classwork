///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
// */
//package programming.assignment.pkg1.loan.account.pkgclass;
//
///**
// *
// * @author iayfn
// */
//public class ProgrammingAssignment1LoanAccountClass {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        LoanAccount loan1 = new LoanAccount(5000.00);
//        LoanAccount loan2 = new LoanAccount(31000.00);
//        LoanAccount.setAnnualInterestRate(1);
//        System.out.println("Monthly payments for loan1 of $5000.00 and loan2 $31000.00 for 3, 5, and 6 year loans at 1% interest.");
//        System.out.println("Loan\t3 years\t5 years\t6 years");
//        System.out.printf("Loan1\t%.2f\t%.2f\t%.2f\n", loan1.calculateMonthlyPayment(36), loan1.calculateMonthlyPayment(60), loan1.calculateMonthlyPayment(72));
//        System.out.printf("Loan2\t%.2f\t%.2f\t%.2f\n\n", loan2.calculateMonthlyPayment(36), loan2.calculateMonthlyPayment(60), loan2.calculateMonthlyPayment(72));
//        LoanAccount.setAnnualInterestRate(5);
//        System.out.printf("Loan1\t%.2f\t%.2f\t%.2f\n", loan1.calculateMonthlyPayment(36), loan1.calculateMonthlyPayment(60), loan1.calculateMonthlyPayment(72));
//        System.out.printf("Loan2\t%.2f\t%.2f\t%.2f\n\n", loan2.calculateMonthlyPayment(36), loan2.calculateMonthlyPayment(60), loan2.calculateMonthlyPayment(72));
//    }
//}
