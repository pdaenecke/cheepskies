package org.cheepskies.ui;
import org.cheepskies.bizlogic.BizLogic;
import org.cheepskies.common.ValueObject;
import org.cheepskiesexceptions.*;
//preston

public class Facade {

    private static BizLogic bizlogic = new BizLogic();

    public static void process(ValueObject vo) {

        try {
            switch (vo.getAction()) {
                case "login":
                    vo.operationResult = bizlogic.login(vo);
                    break;
                case "register":

            }
        } catch (Exception e) {
            vo.operationResult = false;
            System.out.println(e);
            // NEEDS MORE
        }
    }


}
