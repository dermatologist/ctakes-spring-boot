package com.canehealth.spring.ctakes.service;

/**
 * Created by beapen on 17/05/18.
 * Based on the solution here:
 * https://stackoverflow.com/questions/40838999/getting-output-in-json-format-in-uima
 */

import akka.actor.ActorSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.json.JsonCasSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;

import static org.apache.ctakes.clinicalpipeline.ClinicalPipelineFactory.getFastPipeline;

@Service
@CacheConfig(cacheNames = "ctakes")
public class CtakesService {

    private static Log log = LogFactory.getLog(CtakesService.class);

    public final JCas jcas;
    public final AnalysisEngineDescription aed;

    @Autowired
    private ActorSystem system;


    public CtakesService() throws UIMAException, MalformedURLException {
        jcas = JCasFactory.createJCas();
        aed = getFastPipeline();

    }

    @CacheEvict(allEntries = true)
    public void clearCache() {
    }

    @Cacheable(value = "ctakes")
    public String Jcas2json(String note) throws UIMAException, IOException {
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
