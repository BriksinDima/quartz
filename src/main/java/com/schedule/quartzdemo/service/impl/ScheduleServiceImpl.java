package com.schedule.quartzdemo.service.impl;

import com.schedule.quartzdemo.entity.Merchant;
import com.schedule.quartzdemo.job.SimpleJob;
import com.schedule.quartzdemo.service.ScheduleService;
import org.quartz.*;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    @Qualifier("quartzSchedulerFactory")
    private SchedulerFactoryBean schedulerFactory;

    @Autowired
    private JobListener jobListener;

    @Override
    public void scheduleSimpleJob(Merchant merchant) {

        String jobName = String.valueOf(merchant.getId());
        String jobGroup = merchant.getName();

        JobKey jobKey = new JobKey(jobName, jobGroup);

        JobDetail job = JobBuilder.newJob().ofType(SimpleJob.class)
                .storeDurably()
                .withIdentity(jobKey)
                .usingJobData("merchantData", "merchantObject")
                .withDescription("Invoke Sample Job service...")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity(jobName,jobGroup)
                .withSchedule(cronSchedule("*/10 * * * * ?"))
                .build();


        Scheduler scheduler = schedulerFactory.getScheduler();

        try {
            scheduler.getListenerManager().addJobListener(jobListener, KeyMatcher.keyEquals(jobKey));
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
