package com.blazinc.invfriend.domain.repository


import com.blazinc.invfriend.domain.entity.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * User mongoDB repository
 */
@Repository
interface UserRepository extends MongoRepository<User, String> {

}