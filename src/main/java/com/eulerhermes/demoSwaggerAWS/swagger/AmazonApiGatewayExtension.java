package com.eulerhermes.demoSwaggerAWS.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.jaxrs.ext.AbstractSwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.models.Operation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AmazonApiGatewayExtension extends AbstractSwaggerExtension {

    public static final Logger LOGGER = LoggerFactory.getLogger(AmazonApiGatewayExtension.class);

    @Override
    public void decorateOperation(Operation operation, Method method, Iterator<SwaggerExtension> chain) {

        HashMap amazonExtensionDefinitions = new HashMap();

        try {
            AmazonVendorExtension annotationInstanceOnType = method.getDeclaringClass().getAnnotation(AmazonVendorExtension.class);
            Map<String, Object > definitionsOnType = getDefinitionMap(annotationInstanceOnType);
            fillInAmazonExtensions(amazonExtensionDefinitions, definitionsOnType);

            AmazonVendorExtension annotationInstanceOnMethod = method.getAnnotation(AmazonVendorExtension.class);
            Map<String, Object> definitionsOnMethod = getDefinitionMap(annotationInstanceOnMethod);
            fillInAmazonExtensions(amazonExtensionDefinitions, definitionsOnMethod);

            if(!amazonExtensionDefinitions.isEmpty()){
                operation.getVendorExtensions().putAll(amazonExtensionDefinitions);
            }
            super.decorateOperation(operation, method, chain);
        } catch (IOException e) {
            LOGGER.error("Cannot apply amazon extension definition", e);
        }



    }

    protected void fillInAmazonExtensions(Map<String, Object> amazonExtensionDefinitions, Map<String, Object> definitionsFromAnnotation) {
        definitionsFromAnnotation.entrySet().stream()
            .forEach(
                entry ->{
                    String amazonDefinitionKey = entry.getKey();
                    Map<String, Object> amazonExtensionSubDefinition = (Map<String, Object>) amazonExtensionDefinitions.get(amazonDefinitionKey);
                    if(amazonExtensionSubDefinition == null){
                        amazonExtensionSubDefinition = new HashMap();
                        amazonExtensionDefinitions.put(amazonDefinitionKey, amazonExtensionSubDefinition);
                    }
                    Map<String, Object> subDefinition = (Map<String, Object>) entry.getValue();
                    amazonExtensionSubDefinition.putAll(subDefinition);
                }
            );
    }

    protected Map<String, Object> getDefinitionMap(AmazonVendorExtension annotationInstance) throws IOException {
        HashMap amazonExtensionDefinitions = new HashMap<String, Object>();
        if (annotationInstance != null){

            if(StringUtils.isNotBlank(annotationInstance.definitionFilePath()) && StringUtils.isNotBlank(annotationInstance.jsonDefinition())){
                LOGGER.warn("Only json definition from the annotationOnType will be taken into account");
            }

            if(StringUtils.isNotBlank(annotationInstance.jsonDefinition())){
                try {
                    amazonExtensionDefinitions = new ObjectMapper().readValue(annotationInstance.jsonDefinition(), HashMap.class);
                } catch (IOException e) {
                    LOGGER.error("Cannot parse amazon extension definition from annotationOnType", e);
                    throw e;
                }
            } else {
                if (StringUtils.isNotBlank(annotationInstance.definitionFilePath())){
                    URL resource = this.getClass().getClassLoader().getResource(annotationInstance.definitionFilePath());
                    if(resource ==null){
                        LOGGER.warn("Extension file definition not found");
                    } else {
                        try {
                            amazonExtensionDefinitions = new ObjectMapper().readValue(resource, HashMap.class);
                        } catch (IOException e) {
                            LOGGER.error("Cannot parse amazon extension file definition from file:" + annotationInstance.definitionFilePath(), e);
                            throw e;
                        }
                    }
                }
            }
        }
        return amazonExtensionDefinitions;
    }
}
