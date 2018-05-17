package com.canehealth.spring.ctakes.controller;

/**
 * Created by beapen on 22/06/2017.
 * http://websystique.com/spring-boot/spring-boot-rest-api-example/
 */


import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.*;
import org.apache.uima.jcas.JCas.*;
import org.apache.uima.jcas.cas.*;
import org.apache.uima.util.XMLInputSource.*;

@RestController
@RequestMapping("/api")
public class RestApiController {

    public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);


    @Autowired
    private ActorSystem actorSystem;

    // -------------------Retrieve Single User------------------------------------------

    // {id} @PathVariable /  @RequestBody for post
    // http://websystique.com/spring-boot/spring-boot-rest-api-example/
    @RequestMapping(value = "/body", method = RequestMethod.POST)
    public ResponseEntity<?> sparqlQuery(@RequestBody String query) {
        logger.info(query);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> diagnosis(@RequestParam(value = "text", required = false) String text) {
        logger.info(text);
        return new ResponseEntity<>(text, HttpStatus.OK);
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> process_history(@RequestParam("text") String text) {
        logger.info(text);
        return new ResponseEntity<>(text, HttpStatus.OK);
    }

    public String cas2FeatureMap(JCas jcas){
        AnnotationIndex<Annotation> anIndex = jcas.getAnnotationIndex();
        FSIterator<Annotation> anIter = anIndex.iterator();

        return "";
    }

}


