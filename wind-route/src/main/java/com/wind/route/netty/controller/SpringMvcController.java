package com.wind.route.netty.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SpringMvcController
 * @Description: <br>
 * @DATE: 2019/8/28 15:10
 * @Author: hyj
 * @Version: 1.0
 */
public class SpringMvcController {

    private static final SpringMvcController INSTANCE = new SpringMvcController();

    public static SpringMvcController getInstance() {
        return INSTANCE;
    }

    private List<Object> controllerObjectList;

    public SpringMvcController() {
        this.controllerObjectList = initController();
    }

    public List<Object> getControllerObjectList() {
        return controllerObjectList;
    }

    private List<Object> initController() {
        final List<Object> controllerList = new ArrayList<Object>();
        controllerList.add(new PrivateController());

        return controllerList;
    }
}
