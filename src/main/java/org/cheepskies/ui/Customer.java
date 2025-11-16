package org.cheepskies.ui;

public class Customer {
    private String firstName;
    private String mI;
    private String lastName;
    private String email;

    public Customer(String firstName,String mI, String lastName, String email){
        this.firstName = firstName;
        this.mI = mI;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getmI() {
        return mI;
    }

    public void setmI(String mI) {
        this.mI = mI;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Customer() {

    }
//overide equals

}
