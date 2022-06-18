package com.ead.course.consumers;

import com.ead.course.dtos.UserEventPublisherDTO;
import com.ead.course.models.UserModel;
import com.ead.course.models.enums.ActionType;
import com.ead.course.services.UserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

  private final UserService userService;

    public UserConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue (value = "${ead.broker.queue.userEventQueue.name}",durable = "true"),
    exchange = @Exchange(value = "${ead.broker.exchange.userEvent}",type = ExchangeTypes.FANOUT,ignoreDeclarationExceptions = "true")))
    public void listenUserEvent(@Payload UserEventPublisherDTO userEventPublisherDTO){
        UserModel userModel = userEventPublisherDTO.convertToUserModel();

        switch (ActionType.valueOf(userEventPublisherDTO.getActionType())){
            case CREATE:
               userService.save(userModel);
               break;
        }
    }
}
