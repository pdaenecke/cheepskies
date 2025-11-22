package org.cheepskies.ui;

public class Customer {

    // Credential table information
    private String username;
    private String password;
    private String credentialId;

    // Credential table get/set
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Credential table constructor
    public void credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Customer table information
    private String firstName;
    private String mI;
    private String lastName;
    private String email;
    private int customerId;

    public Customer(int customerId, String firstName,String mI, String lastName, String email){
        this.firstName = firstName;
        this.mI = mI;
        this.lastName = lastName;
        this.email = email;
        this.customerId = customerId;
    }

    // Customer ID set/get
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    //  First Name set/get
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Middle Initial set/get
    public String getmI() {
        return mI;
    }
    public void setmI(String mI) {
        this.mI = mI;
    }

    // Last Name set/get
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Email set/get
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // Customer table constructors
    public Customer() {
    }

    public Customer(String firstName, String mI, String lastName, String email, int customerId) {
        this.firstName = firstName;
        this.mI = mI;
        this.lastName = lastName;
        this.email = email;
        this.customerId = customerId;
    }

    /* Overriding equals so that we check if customer is comparing to same person (true), if object is null or class in not class of this object (false)
    rest of logic allows us to populate full customer table when all search fields are empty */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;

        if(!customer.firstName.isEmpty() && !firstName.equals(customer.firstName)) {
            return false;
        }
        if(!customer.mI.isEmpty() && !mI.equals(customer.mI)) {
            return false;
        }
        if (!customer.lastName.isEmpty() && !lastName.equals(customer.lastName)) {
            return false;
        }
        return true;
    }

}
