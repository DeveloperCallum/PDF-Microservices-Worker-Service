package com.example.workerservicenode.rabbitMQ.listener;

import com.example.workerservicenode.event.ImageProcessingEvent;
import com.willcocks.callum.model.ImageRequest;
import com.willcocks.callum.model.ImageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RabbitListener(queues = "imageProcessingQueue", containerFactory = "prefetchRabbitListenerContainerFactory")
@Component
public class JobImageProcessingQueueHandler {
    private static final Logger logger = LoggerFactory.getLogger(DocumentExtractionQueueHandler.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public JobImageProcessingQueueHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @RabbitHandler //TODO: Move this into an event!
    public ImageResponse handle(ImageRequest msg) {
        ImageProcessingEvent e = new ImageProcessingEvent(this, msg);
        applicationEventPublisher.publishEvent(e); //Fire the image processing event.

        //Create the response object to sent back to the client.
        ImageResponse res = new ImageResponse(msg);
        res.setImageEncodedArr(e.getProcessedImages());
        return res;
    }
}
