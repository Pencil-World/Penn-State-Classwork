/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functionalprogrammingtemplate;

/**
 *
 * @author acv
 */
public class Invoice {
    private int partNumber;
    private String partDescription;
    private int quantity;
    private double pricePerItem;

    public Invoice(int partNumber, String partDescription, int quantity, double pricePerItem) {
        this.partNumber = partNumber;
        this.partDescription = partDescription;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public String getPartDescription() {
        return partDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerItem() {
        return pricePerItem;
    }
    
    public double getValue() {
        return getPricePerItem() * getQuantity();
    }
    
    @Override
    public String toString() {
        return String.format("%11d\t%-16s\t%8d\t%14.2f\t%8.2f\n", 
                getPartNumber(), getPartDescription(), getQuantity(), getPricePerItem(), getValue());
    }
}
