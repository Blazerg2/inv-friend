package com.blazinc.invfriend.config

import groovy.util.logging.Log
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * This class is an exception handler, all http request that throws a known Exception
 * will be controlled here
 */
@ControllerAdvice
@Log
class GlobalExceptionHandler {

    public static final String INTERNAL_ERROR_CUSTOM_MESSAGE = '... whatever internal error happened ...'

    /**
     * this method catches every exception to send a custom error json response
     */
    //tODO FIX THIS check for 403
    @ExceptionHandler(Exception)
    @ResponseStatus(code = HttpStatus.OK, reason = 'invalid command')
    void everyException(Exception e) {
        log.info('INVALID COMMAND RECEIVED')
    }

//    /**
//     * This method catches the EntityNotFoundException that is sent if our mongodb return no data
//     * @param EntityNotFoundException
//     * Response with 405 not found response status
//     */
//    @ExceptionHandler(EntityNotFoundException)
//    ResponseEntity<CustomErrorResponse> notFoundException(EntityNotFoundException e) {
//        CustomErrorResponse err = new CustomErrorResponse(code: HttpStatus.NOT_FOUND.value(), message: "Partner with id ${e?.message} not found.")
//        new ResponseEntity<CustomErrorResponse>(err, HttpStatus.NOT_FOUND)
//    }

}
