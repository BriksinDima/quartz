package com.schedule.quartzdemo.controller;

import com.schedule.quartzdemo.entity.Merchant;
import com.schedule.quartzdemo.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/register")
    public void createJob(Merchant merchant){
       scheduleService.scheduleSimpleJob(merchant);
    }

}
