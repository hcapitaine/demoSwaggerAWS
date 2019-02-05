package com.eulerhermes.demoSwaggerAWS.v1;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class MyController {

    @GetMapping("/businessObject")
    public ResponseEntity<List<MyBusinessObject>> searchObject(@RequestParam("searchText") String searchText){
        List<MyBusinessObject> searchResults = new ArrayList<>();
        MyBusinessObject myBusinessObject = new MyBusinessObject();
        myBusinessObject.setName("MyDearFellow");
        myBusinessObject.setAddress("AnyWhere street");
        searchResults.add(myBusinessObject);
        return ResponseEntity.ok(searchResults);
    }
}
