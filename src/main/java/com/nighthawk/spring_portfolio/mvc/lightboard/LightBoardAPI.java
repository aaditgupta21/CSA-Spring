package com.nighthawk.spring_portfolio.mvc.lightboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/lightboard/")
public class LightBoardAPI {

    private LightBoard lightBoard;
    private JsonNode json;

    @GetMapping("/create/{rows}/{cols}/{numOfRows}/{numOfCols}")
    public ResponseEntity<JsonNode> generateLightBoard(@PathVariable int rows, @PathVariable int cols,
            @PathVariable int numOfRows, @PathVariable int numOfCols)
            throws JsonMappingException, JsonProcessingException {
        lightBoard = new LightBoard(rows, cols, numOfRows, numOfCols);

        // Create objectmapper to convert String to JSON
        ObjectMapper mapper = new ObjectMapper();
        json = mapper.readTree(lightBoard.toString());

        return ResponseEntity.ok(json);
    }

    @PostMapping("/setallon")
    public ResponseEntity<JsonNode> setAllOn() throws JsonMappingException, JsonProcessingException {
        lightBoard.setAllOn();

        ObjectMapper mapper = new ObjectMapper();
        json = mapper.readTree(lightBoard.toString());

        return ResponseEntity.ok(json);
    }

}