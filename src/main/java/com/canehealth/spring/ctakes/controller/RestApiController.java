package com.canehealth.spring.ctakes.controller;

/**
 * Created by beapen on 17/05/2018.
 * Based on the solution here:
 * https://stackoverflow.com/questions/40838999/getting-output-in-json-format-in-uima
 *
 */


import akka.actor.ActorSystem;
import com.canehealth.spring.ctakes.service.CtakesService;
import org.apache.uima.UIMAException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class RestApiController {

    public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private ActorSystem system;

    @Autowired
    private CtakesService ctakesService;

    public RestApiController() {
    }

    // -------------------Retrieve Single User------------------------------------------

    // {id} @PathVariable /  @RequestBody for post
    // http://websystique.com/spring-boot/spring-boot-rest-api-example/
    @RequestMapping(value = "/api", method = RequestMethod.POST)
    public ResponseEntity<?> post_text_body(@RequestBody String text) throws IOException, UIMAException {
        logger.info(text);
        return new ResponseEntity<>(ctakesService.Jcas2json(text), HttpStatus.OK);
    }

    @RequestMapping(value = "/text", method = RequestMethod.GET)
    public ResponseEntity<?> get_text(@RequestParam(value = "text", required = false) String text) throws IOException, UIMAException {
        logger.info(text);
        return new ResponseEntity<>(ctakesService.Jcas2json(text), HttpStatus.OK);

    }

    @RequestMapping(value = "/text", method = RequestMethod.POST)
    public ResponseEntity<?> post_text(@RequestParam("text") String text) throws IOException, UIMAException {
        logger.info(text);
        return new ResponseEntity<>(ctakesService.Jcas2json(text), HttpStatus.OK);

    }

}


