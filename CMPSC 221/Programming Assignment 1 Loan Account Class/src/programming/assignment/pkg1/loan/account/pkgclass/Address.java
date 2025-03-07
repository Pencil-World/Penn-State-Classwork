/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programming.assignment.pkg1.loan.account.pkgclass;

/**
 *
 * @author iayfn
 */
public class Address {
    private String street;
    private String city;
    private String state;
    private String zipcode;
    
    public Address(String _street, String _city, String _state, String _zipcode) {
        street = _street;
        city = _city;
        state = _state;
        zipcode = _zipcode;
    }

    @Override
    public String toString(){
        return "Property Address: \n\t" + street + "\n\t" + city + ", " + state + " " + zipcode;
    }
}
