package com.schedule.quartzdemo.service;

import org.quartz.JobKey;

public interface JobRemoval {

    void deleteJob(JobKey jobName);
}
