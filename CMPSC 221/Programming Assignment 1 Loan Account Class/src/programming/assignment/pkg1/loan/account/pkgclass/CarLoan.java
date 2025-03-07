/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programming.assignment.pkg1.loan.account.pkgclass;

/**
 *
 * @author iayfn
 */
public class CarLoan extends LoanAccount {
    private String vehicleVin;
    
    public CarLoan(double _principal, double _annualInterestRate, int _months, String _vehicleVin) {
        super(_principal, _annualInterestRate, _months);
        vehicleVin = _vehicleVin;
    }
    
    @Override
    public String toString(){
        return "Car Loan with: \n" + super.toString() + "\nVehicle Vin: "+ vehicleVin + "\n\n";
    }
}
