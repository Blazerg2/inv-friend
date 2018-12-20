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

    private static final def destinCodes = ['gethelp', 'join', 'start', 'santatime']
    private String chatId

    void messageReceiver(String message, Update params) {
        message = message - '@invFriendBot'
        if (destinCodes.contains(message)) {
            chatId = params?.message?.getChat()?.getId()
            String methodName = message + "Received"
            invokeMethod(methodName, params)
        }
    }

    void gethelpReceived(Update params) {
        this.messageService.sendNotificationToTelegram('help for this bot is not enabled yet', chatId)
    }

    void joinReceived(Update params) {
        User storedUser = userRepository.findByChatId(params?.message?.from?.id as String)
        if (storedUser && storedUser?.verified) {
            storedUser.group = params?.message?.chat?.title
            userRepository.save(storedUser)
            this.messageService.sendNotificationToTelegram("$storedUser.userName is now participating on the secret santa!", chatId)
        } else {
            this.messageService.sendNotificationToTelegram("$storedUser.userName, in order to join the secret santa you must send me a private message with /start first!", chatId)
        }
    }

    void startReceived(Update params) {
        String userId = params?.message?.from?.id

        if (params?.message?.chat?.type != 'private') {
            this.messageService.sendNotificationToTelegram("You must send a private message to the bot with /start in order to start the bot", chatId)
        } else {
            if (!userRepository.findByChatId(userId)) {
                User user = new User(userName: params?.message?.from?.first_name, chatId: userId, verified: true)
                userRepository.save(user)
            }
            this.messageService.sendNotificationToTelegram("Greetings!, use /getHelp to know more about the bot", chatId)
        }
    }

    void santatimeReceived(Update params) {
        log.info('entramos')
        List<User> users = userRepository.findByGroup(params?.message?.chat?.title)
        List<User> users2 = users.clone() as List<User>

        Collections.shuffle(users2)

        users?.eachWithIndex { element, index ->
            Boolean worked = false

            while (!worked) {

                if (element?.id == users2[0]?.id) {
                    Collections.shuffle(users2)
                } else {
                    this.messageService.sendNotificationToTelegram("your present goes to ${users2[0]?.userName} ", users2[0]?.chatId)
                    log.info("a ${element?.userName} le corresponde regalar a ${users2[0]?.userName}")
                    users2?.remove(0)
                    worked = true
                }
            }

        }
    }
    //todo comando de mostrar participantes
}
