package com.simplefanc.controllers;

import com.simplefanc.beans.AutoWired;
import com.simplefanc.service.SalaryService;
import com.simplefanc.web.mvc.Controller;
import com.simplefanc.web.mvc.RequestMapping;
import com.simplefanc.web.mvc.RequestParam;

/**
 * 通过工龄计算工资
 */
@Controller
public class SalaryController {
    @AutoWired
    private SalaryService salaryService;

    //http://localhost:6699/getsalary.json?experience=3&name=cf
    @RequestMapping("/getsalary.json")
    public Integer getSalary(@RequestParam("name") String name, @RequestParam("experience") String experience){
        return salaryService.calSalary(Integer.parseInt(experience));
    }
}
