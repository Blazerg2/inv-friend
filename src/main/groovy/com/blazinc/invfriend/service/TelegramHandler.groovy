package com.blazinc.invfriend.service

import com.blazinc.invfriend.domain.entity.Question
import com.blazinc.invfriend.domain.entity.User
import com.blazinc.invfriend.domain.repository.QuestionRepository
import com.blazinc.invfriend.domain.repository.UserRepository
import com.blazinc.invfriend.model.Partner
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

    @Autowired
    QuestionRepository questionRepository

//    private static final def OLDdestinCodes = ['gethelp', 'join', 'start', 'santatime', 'participants', 'message']
    private static final def destinCodes = ['start']
    private static final def correctAnswers = ['2008', 'Take on me', '2018']


    private String chatId

    void messageReceiver(String message, Update params) {
        message = message - '@invFriendBot'
//        Boolean commandIsMessage = checkForMessageCommand(params, message)

        log.info("X" * 30)
        log.info("${message in correctAnswers}")
        log.info("Y" * 30)

        if (message in correctAnswers) {
            User user = userRepository.findByUserName(params?.message?.from?.first_name)
            user.question++
            userRepository.save(user)
        }

//OLD        if (!commandIsMessage && destinCodes.contains(message)) {

        if (destinCodes.contains(message)) {
            chatId = params?.message?.getChat()?.getId()
            String methodName = message + "Received"
            invokeMethod(methodName, params)
        }
    }

    Boolean checkForMessageCommand(Update params, String message) {
        if (message?.size() > 7 && message?.substring(0, 7) == 'message') {
            messageReceived(params, message?.substring(7))
            return true
        }
        false
    }

    void gethelpReceived(Update params) {
        this.messageService.sendNotificationToTelegram("Available commands $destinCodes", chatId)
    }

    void joinReceived(Update params) {
        User storedUser = userRepository.findByChatId(params?.message?.from?.id as String)
        if (storedUser && storedUser?.verified) {
            storedUser.group = params?.message?.chat?.title
            userRepository.save(storedUser)
            this.messageService.sendNotificationToTelegram("$storedUser.userName is now participating on the secret santa!", chatId)
        } else {
            this.messageService.sendNotificationToTelegram("${params?.message?.from?.first_name}, in order to join the secret santa you must send me a private message with /start first!", chatId)
        }
    }

//    void OLDstartReceived(Update params) {
//        String userId = params?.message?.from?.id
//
//        if (params?.message?.chat?.type != 'private') {
//            this.messageService.sendNotificationToTelegram("You must send a private message to the bot with /start in order to start the bot", chatId)
//        } else {
//            if (!userRepository.findByChatId(userId)) {
//                User user = new User(userName: params?.message?.from?.first_name, chatId: userId, verified: true)
//                userRepository.save(user)
//            }
//            this.messageService.sendNotificationToTelegram("Greetings!, use /getHelp to know more about the bot", chatId)
//        }
//    }

    void startReceived(Update params) {
        String userId = params?.message?.from?.id
        User user = new User(userName: params?.message?.from?.first_name, chatId: userId, question: 0, verified: true)

        if (!userRepository.findByChatId(userId)) {
            userRepository.save(user)
        }

        this.messageService.sendNotificationToTelegram("El juego comienza aquí, debes seleccionar la respuesta correcta para recibir la siguiente pregunta.", chatId)
        this.sendQuestion(user)

    }

    void sendQuestion(User user) {
        Question question = this.questionRepository.findByQuestionNumber(user?.question)
        this.messageService.sendNotificationToTelegram("${question.questionText}", chatId)
        question?.answers?.each {
            this.messageService.sendNotificationToTelegram("$it", chatId)
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
                    this.messageService.sendNotificationToTelegram("your present goes to ${users2[0]?.userName} ", element?.chatId)
                    this.messageService.sendNotificationToTelegram('You can send a secret message to the person who is going to gift you using the /message command, for example, you can send me "/message I love black shirts!"', element?.chatId)


                    element?.partner = new Partner(first_name: users2[0]?.userName, chatId: users2[0]?.chatId)
                    log.info("a ${element?.userName} le corresponde regalar a ${users2[0]?.userName}")
                    users2?.remove(0)
                    worked = true
                }
            }
        }
        userRepository.saveAll(users)
    }

    void participantsReceived(Update params) {
        List<User> users = userRepository.findByGroup(params?.message?.chat?.title)
        List<String> names = new ArrayList<String>()
        if (users) {
            users.each {
                names << it.userName
            }
        }
        this.messageService.sendNotificationToTelegram("$names ", chatId)
    }

    void messageReceived(Update params, String message) {
        if (params?.message?.chat?.type != 'private') {
            this.messageService.sendNotificationToTelegram("You can contact your invisible friend but the /message command must be sent through a private message to me", chatId)
        } else {
            User origin = userRepository.findByChatId(params?.message?.from?.id as String)
            if (origin?.partner && message) {
                this.messageService.sendNotificationToTelegram(message, origin?.partner?.chatId)
            } else {
                this.messageService.sendNotificationToTelegram("You don't have a secret santa or you are not sending a correct message", params?.message?.from?.id as String)
            }
        }
    }
}
