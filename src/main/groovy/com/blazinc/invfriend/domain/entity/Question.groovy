package com.blazinc.invfriend.domain.entity

import groovy.transform.Canonical
import org.springframework.data.mongodb.core.mapping.Document

@Canonical
@Document(collection = 'question')
class Question {
    String id
    Integer questionNumber
    String questionText
    String correctAnswer
    List<String> answers
    Boolean isLast
}
