# Ctakes-spring-boot 

## Deprecated: [Use this one instead](https://github.com/E-Health/ctakes-rest-service).

### Planning a container optimized rewrite in [Quarkus](https://quarkus.io/). 

## Ctakes

Apache cTAKES™ is a natural language processing system for extraction of information from electronic medical record clinical free-text.
This packages cTakes as a spring boot application providing REST api.
Hazelcast caching is added.

## How to Use

* STEP 1: Clone this repo and add UMLS username and password in CtakesApplication.java
* STEP 2: Add the pipeline in application properties (FAST is recommended)
* STEP 3: mvn clean install spring-boot:repackage
* STEP 4: Follow steps below so that the org folder is inside 'classes')

```

wget http://sourceforge.net/projects/ctakesresources/files/ctakes-resources-4.0-bin.zip
unzip ctakes-resources-4.0-bin.zip
cp -r resources/org/ target/classes 


```
* STEP 5: mvn spring-boot:run and access the application at 
http://localhost:8080/api?text=papules on the face

* STEP 6 (If you select DEFAULT in STEP 2): Copy the LookupDesc_Db.xml file in the doc folder to target / classes / org / apache / ctakes / dictionary / lookup / LookupDesc_Db.xml 

## Contributors

* [Bell Eapen](https://nuchange.ca) (McMaster U)
* inspired by [this repo](https://github.com/GoTeamEpsilon/ctakes-rest-service).
