package com.schedule.quartzdemo.service.impl;


import com.schedule.quartzdemo.ScheduleTask;
import org.quartz.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService implements InitializingBean {

    @Autowired
    @Qualifier("quartzSchedulerFactory")
    private SchedulerFactoryBean schedulerFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        String cronExpression = "*/3 * * * * ?";

        scheduleJob(ScheduleTask.class, ScheduleTask.class.getSimpleName(), "RETRY", cronExpression );
    }

    public void register(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroup, boolean replace) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).storeDurably().build();
        scheduler.addJob(jobDetail, replace);
    }


    public void scheduleJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroup, String cronExpression) throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

        if(!scheduler.checkExists(jobKey)){
            register(jobClass, jobName, jobGroup, true);
        }

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .forJob(jobDetail)
                .withSchedule(cronScheduleBuilder)
                .build();

        if(!scheduler.checkExists(triggerKey)){
            scheduler.scheduleJob(cronTrigger);
        } else {
            scheduler.rescheduleJob(triggerKey, cronTrigger);
        }
    }
}
