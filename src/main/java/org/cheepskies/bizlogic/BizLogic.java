package org.cheepskies.bizlogic;

import org.cheepskies.common.ValueObject;
import org.cheepskies.ui.Customer;
import org.cheepskiesdb.DatabaseUtils;
import org.cheepskiesexceptions.*;

public class BizLogic {

    public boolean login(ValueObject vo) throws Exception {

        Customer c = vo.getCustomer();

        if (!DatabaseUtils.usernameScan(c)) {
            vo.setAction("login_failed");
            return false;
        }

        if (!DatabaseUtils.loginValidation(c)) {
            vo.setAction("login_failed");
            return false;
        }

        DatabaseUtils.login(vo);
        vo.setAction("login_success");

        return true;
    }
}