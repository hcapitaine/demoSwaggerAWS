package com.eulerhermes.demoSwaggerAWS.v2;

import com.eulerhermes.demoSwaggerAWS.swagger.AmazonVendorExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/v2", produces = MediaType.APPLICATION_JSON_VALUE)
@AmazonVendorExtension(definitionFilePath = "swaggerV2.json")
public class MyController {

    @AmazonVendorExtension(jsonDefinition =
            "{" +
            "  \"x-amazon-apigateway-integration\": {" +
            "    \"method\": \"GET\"" +
            "  }" +
            "}")
    @GetMapping("/businessObject")
    public ResponseEntity<List<MyBusinessObject>> searchObject(@RequestParam("searchValue") List<String> searchValues){
        List<MyBusinessObject> searchResults = new ArrayList<>();
        MyBusinessObject myBusinessObject = new MyBusinessObject();
        myBusinessObject.setLastName("MyDearFellow");
        myBusinessObject.setAddress("AnyWhere street");
        searchResults.add(myBusinessObject);
        return ResponseEntity.ok(searchResults);
    }
}
