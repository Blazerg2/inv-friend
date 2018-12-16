
@Log
@Service
class TelegramHandlerService {

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
