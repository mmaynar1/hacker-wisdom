package com.mmaynard.util;

import com.mmaynard.scheduled.PopulateCacheTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoadDataOnStartup
{
    @Autowired
    private PopulateCacheTask populateCacheTask;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData()
    {
        populateCacheTask.populateCache();
    }
}
