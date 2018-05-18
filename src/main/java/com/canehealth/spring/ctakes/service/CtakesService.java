package com.canehealth.spring.ctakes.service;

/**
 * Created by beapen on 17/05/18.
 * Based on the solution here:
 * https://stackoverflow.com/questions/40838999/getting-output-in-json-format-in-uima
 */

import akka.actor.ActorSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ctakes.clinicalpipeline.ClinicalPipelineFactory;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.json.JsonCasSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;

@Service
@CacheConfig(cacheNames = "ctakes")
public class CtakesService {

    private static Log log = LogFactory.getLog(CtakesService.class);

    public final JCas jcas;
    public final AnalysisEngineDescription aed;

    @Value("${ctakes.pipeline}")
    private String pipeline;

    @Autowired
    private ActorSystem system;


    public CtakesService() throws UIMAException, MalformedURLException {
        jcas = JCasFactory.createJCas();
        switch (pipeline) {
            case "FAST":
                aed = ClinicalPipelineFactory.getFastPipeline();
                break;
            case "DEFAULT":
                aed = ClinicalPipelineFactory.getDefaultPipeline();
                break;
            case "NP":
                aed = ClinicalPipelineFactory.getNpChunkerPipeline();
                break;
            case "COREF":
                aed = ClinicalPipelineFactory.getCoreferencePipeline();
                break;
            case "PARSE":
                aed = ClinicalPipelineFactory.getParsingPipeline();
                break;
            case "CHUNK":
                aed = ClinicalPipelineFactory.getStandardChunkAdjusterAnnotator();
                break;
            case "TOKEN":
                aed = ClinicalPipelineFactory.getTokenProcessingPipeline();
                break;
            default:
                aed = ClinicalPipelineFactory.getDefaultPipeline();
        }

    }

    @CacheEvict(allEntries = true)
    public void clearCache() {
    }

    @Cacheable(value = "ctakes")
    public String Jcas2json(String note) throws UIMAException, IOException {
        jcas.reset();
        jcas.setDocumentText(note);
        SimplePipeline.runPipeline(jcas, aed);
        CAS cas = jcas.getCas();
        JsonCasSerializer jcs = new JsonCasSerializer();
        jcs.setPrettyPrint(true); // do some configuration
        jcs.setJsonContext(JsonCasSerializer.JsonContextFormat.omitSubtypes);
        jcs.setJsonContext(JsonCasSerializer.JsonContextFormat.omitContext);
        jcs.setJsonContext(JsonCasSerializer.JsonContextFormat.omitExpandedTypeNames);

        StringWriter sw = new StringWriter();
        jcs.serialize(cas, sw); // serialize into sw
        return sw.toString();
    }

}
