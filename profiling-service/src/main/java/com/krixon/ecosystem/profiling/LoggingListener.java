package com.krixon.ecosystem.profiling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Slf4j
public class LoggingListener
{
    @Async
    @TransactionalEventListener
    public void handleEvent(Object event)
    {
        log.info("Handling event {}", event);
    }
}
