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

    private static final def destinCodes = ['getHelp', 'join', 'start']

    void messageReceiver(String message, Update params) {
        if (destinCodes.contains(message)) {
            String methodName = message + "Received"
            invokeMethod(methodName, params)
        }
    }

    void getHelpReceived(Update params) {
        String chatId = params?.message?.getChat()?.getId()
        this.messageService.sendNotificationToTelegram('help for this bot is not enabled yet', chatId)
    }

    void joinReceived(Update params) {
        String chatId = params?.message?.getChat()?.getId()

        User user = new User(userName: params?.message?.from?.first_name, group: params?.message?.chat?.title)
        userRepository.save(user)

        this.messageService.sendNotificationToTelegram('User added', chatId)
    }

    void startReceived(Update params) {
        String chatId = params?.message?.from?.id

        List<User> users = userRepository.findByGroup(params?.message?.chat?.title)
        List<User> users2 = users.clone() as List<User>
        Collections.shuffle(users2)

        users?.each {
            String partner = users2[0].userName
            if (partner == it.userName) {
                partner = users2[1].userName
                users2.remove(1)
            } else {
                users2.remove(0)
            }

            this.messageService.sendNotificationToTelegram("your present goes to $partner", chatId)
        }
    }
}
