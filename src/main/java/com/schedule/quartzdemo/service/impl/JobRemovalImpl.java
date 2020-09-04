package com.schedule.quartzdemo.service.impl;

import com.schedule.quartzdemo.service.JobRemoval;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class JobRemovalImpl implements JobRemoval {
    @Autowired
    @Qualifier("quartzSchedulerFactory")
    private SchedulerFactoryBean schedulerFactory;

    @Override
    public void deleteJob(JobKey jobKey){
        Scheduler scheduler = schedulerFactory.getScheduler();
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
