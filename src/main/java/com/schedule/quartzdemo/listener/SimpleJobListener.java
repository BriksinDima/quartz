package com.schedule.quartzdemo.listener;

import com.schedule.quartzdemo.service.JobRemoval;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleJobListener implements JobListener {

    public static final String LISTENER_NAME = "simpleJobListener5";

    @Autowired
    private JobRemoval jobRemoval;

    @Override
    public String getName() {
        return this.LISTENER_NAME;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        String jobName = jobExecutionContext.getJobDetail().getKey().toString();
        System.out.println("jobToBeExecuted");
        System.out.println("Job : " + jobName + " is going to start...");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        System.out.println("jobExecutionVetoed");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        System.out.println("jobWasExecuted");

        JobKey key = jobExecutionContext.getJobDetail().getKey();

        // Здесь можно проверять изменился ли статус ответа в нспк, если да, то  вызывать delete

        jobRemoval.deleteJob(key);

//        if (!e.getMessage().equals("")) {
//            System.out.println("Exception thrown by: " + jobName
//                    + " Exception: " + e.getMessage());
//        }
    }
}
