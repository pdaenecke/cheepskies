package org.cheepskies.ui;
//extends customer because admin has more permissions that customer
public class Admin extends Customer {
    /*
    - permissions:
        - all permissions given to customer must also be given to Admin
        - admin can add flights to their account (as if they are flying like customer)
        - AND admin has access to admin flights page (can SELECT, UPDATE, INSERT, or DELETE flights) through SQL or
          specified text fields?
        - should have login specific credentials? admin table in db?
     */
}
