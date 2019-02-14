package com.blazinc.invfriend.service

import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

/**
 * this service connects to a telegram by sending messages
 */
@Log
@Service
class MessageService {

    @Autowired
    private Environment environment

    /**
     * It sends a message to a telegram bot with a specific chatId
     * @param message to send
     */

    @Async
    void sendNotificationToTelegram(String message, String chatId) {

        RestTemplate restTemplate = new RestTemplate()

        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED)

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>()
        params.add("chat_id", chatId)
        params.add("text", message)

        HttpEntity<LinkedMultiValueMap<String, Object>> request = new HttpEntity<>(params, headers)

        //OLD
        restTemplate.postForEntity(
//                794682489:AAH5gp3ex-7uGWBvJwAU6Po6--6IlcJHGcA
                'https://api.telegram.org/bot751728179:AAFJRaz8sUequ_MgDoxhfSUQSIOWiXjCj7A/sendMessage',
                request, String.class
        )
    }
}
