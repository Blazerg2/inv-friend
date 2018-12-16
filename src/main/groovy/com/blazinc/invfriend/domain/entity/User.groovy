package com.blazinc.invfriend.domain.entity

import groovy.transform.Canonical
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Entity partner class
 */
@Canonical
@Document(collection = 'partner')
class User {

    @Id
    String id
    String userName
    String group
}
