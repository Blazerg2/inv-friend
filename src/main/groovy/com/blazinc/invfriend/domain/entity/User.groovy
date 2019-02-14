package com.blazinc.invfriend.domain.entity

import com.blazinc.invfriend.model.Partner
import groovy.transform.Canonical
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Entity partner class
 */
@Canonical
@Document(collection = 'user')
class User {

    @Id
    String id
    Question complexQuestion
    Integer question
    String userName
    String group

    @Indexed(unique=true)
    String chatId
    Boolean verified
    Partner partner
}
