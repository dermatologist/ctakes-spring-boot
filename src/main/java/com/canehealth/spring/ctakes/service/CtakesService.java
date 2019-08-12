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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
    private String pipeline = "FAST";

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
//        jcs.setJsonContext(JsonCasSerializer.JsonContextFormat.omitSubtypes);
//        jcs.setJsonContext(JsonCasSerializer.JsonContextFormat.omitContext);
//        jcs.setJsonContext(JsonCasSerializer.JsonContextFormat.omitExpandedTypeNames);

        StringWriter sw = new StringWriter();
        jcs.serialize(cas, sw); // serialize into sw
        return jsonClinical(sw.toString(), note);
        //return sw.toString();
    }

    public String jsonClinical(String ctakes, String note) {
        JSONParser jsonParser = new JSONParser();
        JSONObject obj = new JSONObject();
        try {
            obj = (JSONObject) jsonParser.parse(ctakes);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        obj = (JSONObject) obj.get("_views");
        obj = (JSONObject) obj.get("_InitialView");
        JSONArray MedicationMention = (JSONArray) obj.get("MedicationMention");
        JSONArray AnatomicalSiteMention = (JSONArray) obj.get("AnatomicalSiteMention");
        JSONArray DiseaseDisorderMention = (JSONArray) obj.get("DiseaseDisorderMention");
        JSONArray SignSymptomMention = (JSONArray) obj.get("SignSymptomMention");
        JSONArray ProcedureMention = (JSONArray) obj.get("ProcedureMention");
        JSONArray WordToken = (JSONArray) obj.get("WordToken");

        JSONObject output = new JSONObject();
        output.put("MedicationMention", parseJsonMention(note, WordToken, MedicationMention));
        output.put("AnatomicalSiteMention", parseJsonMention(note, WordToken, AnatomicalSiteMention));
        output.put("DiseaseDisorderMention", parseJsonMention(note, WordToken, DiseaseDisorderMention));
        output.put("SignSymptomMention", parseJsonMention(note, WordToken, SignSymptomMention));
        output.put("ProcedureMention", parseJsonMention(note, WordToken, ProcedureMention));
        // output.put("Original", obj);
        return output.toJSONString();
    }

    private JSONArray parseJsonMention(String document, JSONArray wordtoken, JSONArray jsonArray) {

        JSONArray output = new JSONArray();
        if(jsonArray != null) {
            for (int i = 0, size = jsonArray.size(); i < size; i++) {
                JSONObject objectInArray = (JSONObject) jsonArray.get(i);
                long begin = (long) objectInArray.get("begin");
                long end = (long) objectInArray.get("end");
                String original_word = document.substring((int) begin, (int) end);
                String canonical_form = "";
                for (int i2 = 0, size2 = wordtoken.size(); i2 < size2; i2++) {
                    JSONObject tokenInArray = (JSONObject) wordtoken.get(i2);
                    long begin2 = (long) tokenInArray.get("begin");
                    long end2 = (long) tokenInArray.get("end");
                    if (begin == begin2 && end == end2) canonical_form = (String) tokenInArray.get("canonicalForm");
                }
                objectInArray.put("originalWord", original_word);
                objectInArray.put("canonicalForm", canonical_form);
                output.add(objectInArray);
            }
        }
        return output;
    }

}
