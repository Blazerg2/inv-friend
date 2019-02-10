package com.blazinc.invfriend.domain.repository

import com.blazinc.invfriend.domain.entity.Question
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * User mongoDB repository
 */
@Repository
interface QuestionRepository extends MongoRepository<Question, String> {

    Question findByQuestionNumber(Integer questionNumber)
}
