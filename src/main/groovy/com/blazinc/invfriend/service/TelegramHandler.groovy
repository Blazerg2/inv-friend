package com.blazinc.invfriend.service

import com.blazinc.invfriend.domain.entity.User
import com.blazinc.invfriend.domain.repository.UserRepository
import com.blazinc.invfriend.model.telegramModel.Update
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Log
@Service
class TelegramHandler {

    @Autowired
    MessageService messageService

    @Autowired
    UserRepository userRepository

    private static final def destinCodes = ['getHelp', 'join']

    void messageReceiver(String message, Update params) {
        if (destinCodes.contains(message)) {
            String methodName = message + "Received"
            invokeMethod(methodName, params)
        }
    }

    void getHelpReceived(Update params) {
        String chatId = params?.message?.getChat()?.getId()
        this.messageService.sendNotificationToTelegram('help for this bot is not enable yet', chatId)
    }

    void joinReceived(Update params){
        String chatId = params?.message?.getChat()?.getId()

        User user = new User(userName: params?.message?.from?.first_name, group: params?.message?.chat?.title)
        userRepository.save(user)

        this.messageService.sendNotificationToTelegram('User added', chatId)

    }
}
