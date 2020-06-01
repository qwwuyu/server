package com.qwwuyu.server.ctrl.admin;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.filter.AuthRequired;
import com.qwwuyu.server.helper.robot.RobotHelper;
import com.qwwuyu.server.utils.J2EEUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AuthRequired(permit = Constant.PERMIT_ADMIN)
@Controller
@RequestMapping("/robot")
public class RobotCtrl {
    @RequestMapping("/open")
    public void openQQ(HttpServletRequest request, HttpServletResponse response) {
        User user = J2EEUtil.getUser(request);
        try {
            RobotHelper.getInstance().openRobot();
            J2EEUtil.render(response, J2EEUtil.getSuccessBean());
        } catch (Exception e) {
            J2EEUtil.render(response, J2EEUtil.getErrorBean().setInfo(e.getMessage()));
        }
    }

    @RequestMapping("/close")
    public void closeQQ(HttpServletRequest request, HttpServletResponse response) {
        User user = J2EEUtil.getUser(request);
        try {
            RobotHelper.getInstance().closeRobot();
            J2EEUtil.render(response, J2EEUtil.getSuccessBean());
        } catch (Exception e) {
            J2EEUtil.render(response, J2EEUtil.getErrorBean().setInfo(e.getMessage()));
        }
    }
}