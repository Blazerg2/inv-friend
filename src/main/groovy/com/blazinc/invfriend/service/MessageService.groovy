package com.blazinc.invfriend.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

/**
 * this service connects to a telegram by sending messages
 */
@Service
class MessageService {

    @Autowired
    private Environment environment

    /**
     * It sends a message to a telegram bot with a specific chatId
     * @param message to send
     */
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
                'https://api.telegram.org/bot794682489:AAH5gp3ex-7uGWBvJwAU6Po6--6IlcJHGcA/sendMessage',
                request, String.class
        )
    }
}
