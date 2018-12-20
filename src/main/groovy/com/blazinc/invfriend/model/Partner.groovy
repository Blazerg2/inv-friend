package com.blazinc.invfriend.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.Canonical

@Canonical
@JsonIgnoreProperties(ignoreUnknown = true)
class Partner {

    String first_name
    String chatId
}

