package org.cheepskies.ui;

public class Customer extends User {

    // Credential table information
    private String username;
    private String password;
    private String credentialId;
    private String answer;

    // Credential table get/set
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    // Credential table constructor
    public void credentials(String username, String password, String answer) {
        this.username = username;
        this.password = password;
    }

    // Customer table information
    private String firstName;
    private String mI;
    private String lastName;
    private String email;
    private int customerId;

    public String getCredentialId() {
        return credentialId;
    }

    public Customer(int customerId, String firstName, String mI, String lastName, String email){
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

}
