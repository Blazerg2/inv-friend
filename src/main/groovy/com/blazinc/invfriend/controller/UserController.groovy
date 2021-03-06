package com.blazinc.invfriend.controller

import com.blazinc.invfriend.model.telegramModel.Update
import com.blazinc.invfriend.service.TelegramHandler
import groovy.util.logging.Log
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView
import springfox.documentation.annotations.ApiIgnore

import java.util.concurrent.Callable

@Log
@Api(value = 'partners', description = 'Just a custom api to document the secret santa telegram project')
@RestController()
class UserController {

    @Autowired
    TelegramHandler telegramHandler

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
    String isAlive() {
        'Hello world, I am alive!!!!!!'
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    void webhook(@RequestBody Update update) {
        String inputMessage = update?.message?.text

        log.info("message received $inputMessage")
        log.info("$update")

        this.telegramHandler.messageReceiver(inputMessage?.substring(1), update)
    }

//    @RequestMapping(value="/sleep")
//    Callable<String> myControllerMethod() {
//        Callable<String> asyncTask = { try {
//            System.out.println(" WAITING STARTED:"+new Date());
//            String inputMessage = update?.message?.text
//
//            log.info("message received $inputMessage")
//            log.info("$update")
//
//            this.telegramHandler.messageReceiver(inputMessage?.substring(1), update)
//            Thread.sleep(5000);
//            System.out.println(" WAITING COMPLETED:"+new Date());
//            return "Return";//Send the result back to View Return.jsp
//        } catch(InterruptedException iexe) {
//            //log exception
//            return "ReturnFail";
//        }}
//        return asyncTask;
//    }

//    @ApiIgnore
//    @GetMapping(value = '/')
//    RedirectView emptyUrlRedirect() {
//        return new RedirectView('http://localhost:8080/swagger-ui.html')
//    }
}
