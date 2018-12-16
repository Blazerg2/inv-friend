package com.blazinc.invfriend.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Api(value = 'partners', description = 'Just a custom api to document the secret santa telegram project')
@RestController()
class TestController {

    @ApiOperation(
            notes = 'Method used to get all partners',
//            response = PartnerDTO,
            tags = ['partner'],
            value = 'Get all the partners'
    )
    @ApiResponses(value = [
            @ApiResponse(code = 200, message = 'Successful operation'),
    ])

    @GetMapping(value = '/isAlive')
}