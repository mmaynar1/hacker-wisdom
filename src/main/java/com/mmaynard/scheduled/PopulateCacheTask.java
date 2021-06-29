package com.mmaynard.scheduled;

import com.mmaynard.domain.AnsweredQuestion;
import com.mmaynard.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class PopulateCacheTask
{
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MainService mainService;

    @Scheduled( fixedDelay = 600000 ) //Populate the cache every 10 minutes
    public void populateCache()
    {
        try
        {
            List<AnsweredQuestion> answeredQuestions = mainService.getAnsweredQuestionsCachePut();
        }
        catch ( Exception e )
        {
            logger.error(Instant.now() + " - Error encountered while populating the cache", e );
        }
        logger.info(Instant.now() + " - Populating the cache");
    }
}
