package com.simplefanc.service;

import com.simplefanc.beans.Bean;


@Bean
public class SalaryService {
    public Integer calSalary(Integer experience) {
        return experience * 5000;
    }
}
