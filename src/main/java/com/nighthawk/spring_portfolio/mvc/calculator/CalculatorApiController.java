package com.nighthawk.spring_portfolio.mvc.calculator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorApiController {

    @GetMapping("/{expression}")
    public ResponseEntity<String> getResult(@PathVariable String expression) {

        // Returns jsonified result of expression with tokens and everything
        Calculator newCalc = new Calculator(expression);
        String result = newCalc.toJSON();
        return new ResponseEntity<String>(result, HttpStatus.OK);

    }

}
