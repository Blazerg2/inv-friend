package com.blazinc.invfriend.model.telegramModel

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.Canonical

@Canonical
@JsonIgnoreProperties(ignoreUnknown = true)
class Message {
    def message_id
    User from
    Integer date
    Chat chat
    String text
}