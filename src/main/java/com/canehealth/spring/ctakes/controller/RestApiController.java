package com.canehealth.spring.ctakes.controller;

/**
 * Created by beapen on 17/05/2018.
 * Based on the solution here:
 * https://stackoverflow.com/questions/40838999/getting-output-in-json-format-in-uima
 *
 */


import akka.actor.ActorSystem;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.json.JsonCasSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;

import static org.apache.ctakes.clinicalpipeline.ClinicalPipelineFactory.getFastPipeline;

@RestController
public class RestApiController {

    public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    public final JCas jcas;
    public final AnalysisEngineDescription aed;

    @Autowired
    private ActorSystem system;

    public RestApiController() throws UIMAException, MalformedURLException {
        jcas = JCasFactory.createJCas();
        aed = getFastPipeline();
    }

    // -------------------Retrieve Single User------------------------------------------

    // {id} @PathVariable /  @RequestBody for post
    // http://websystique.com/spring-boot/spring-boot-rest-api-example/
    @RequestMapping(value = "/api", method = RequestMethod.POST)
    public ResponseEntity<?> post_text_body(@RequestBody String text) throws IOException, UIMAException {
        logger.info(text);
        return new ResponseEntity<>(jcas2json(text), HttpStatus.OK);
    }

    @RequestMapping(value = "/text", method = RequestMethod.GET)
    public ResponseEntity<?> get_text(@RequestParam(value = "text", required = false) String text) throws IOException, UIMAException {
        logger.info(text);
        return new ResponseEntity<>(jcas2json(text), HttpStatus.OK);

    }

    @RequestMapping(value = "/text", method = RequestMethod.POST)
    public ResponseEntity<?> post_text(@RequestParam("text") String text) throws IOException, UIMAException {
        logger.info(text);
        return new ResponseEntity<>(jcas2json(text), HttpStatus.OK);

    }


    public String jcas2json(String note) throws UIMAException, IOException {
        jcas.setDocumentText(note);
        SimplePipeline.runPipeline(jcas, aed);
        CAS cas = jcas.getCas();
        JsonCasSerializer jcs = new JsonCasSerializer();
        jcs.setPrettyPrint(true); // do some configuration
        StringWriter sw = new StringWriter();
        jcs.serialize(cas, sw); // serialize into sw
        return sw.toString();
    }

}


