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
        User user = new User(userName: params?.message?.from?.first_name, group: params?.message?.chat?.title)
        userRepository.save(user)
//todo check if user already exist
        this.messageService.sendNotificationToTelegram('User added', chatId)
    }

    void startReceived(Update params) {
        //TODO start debe registrar al usuario y comprobar que venga del bot el mensaje
        User user = new User(
                userName: params?.message?.from?.first_name, chatId: params?.message?.from?.id, verified: true
        )
//        List<User> users = userRepository.findByGroup(params?.message?.chat?.title)
//        List<User> users2 = users.clone() as List<User>
//        Collections.shuffle(users2)
//
//        def prueba = []
//        //TODO fix this shit and start command name change
//        users?.each {
//            String partner = users2[0].userName
//            if (partner == it.userName) {
//                partner = users2[1].userName
//                users2.remove(1)
//            } else {
//                users2.remove(0)
//            }

        this.messageService.sendNotificationToTelegram("your present goes to Paco", chatId)
//        }
    }

    void santatimeReceived(Update params, String chatId) {
        List<User> users = userRepository.findByGroup(params?.message?.chat?.title)
        List<User> users2 = users.clone() as List<User>

        Collections.shuffle(users2)
        Boolean worked = false

        users?.eachWithIndex { element, index ->
            while (!worked) {
                log.info(index?.toString())
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
