package com.blazinc.invfriend.service

import com.blazinc.invfriend.model.telegramModel.Update
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Log
@Service
class TelegramHandler {

    @Autowired
    MessageService messageService

    private static final def destinCodes = ["getHelp"]

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
}
