package com.simplefanc.controllers;

import com.simplefanc.web.mvc.Controller;
import com.simplefanc.web.mvc.RequestMapping;
import com.simplefanc.web.mvc.RequestParam;

/**
 * 通过工龄计算工资
 */
@Controller
public class SalaryController {
    @RequestMapping("/getsalary.json")
    public Integer getSalary(@RequestParam("name") String name, @RequestParam("experience") String experience){
        return 10000;
    }
}
