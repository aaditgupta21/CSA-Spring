package com.nighthawk.spring_portfolio.mvc.calculator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Controller
public class CalculatorApiController {

    @GetMapping("/calculator")
    public String getResult(
            @RequestParam(name = "expression", required = false, defaultValue = "5 * 4") String expression,
            Model model) {

        // Returns jsonified result of expression with tokens and everything
        Calculator newCalc = new Calculator(expression);
        String result = newCalc.toString();
        model.addAttribute("result", result);

        return "calculator";
    }

    @GetMapping("/api/calculator/{expression}")
    public ResponseEntity<String> getResult(@PathVariable String expression) {

        // Returns jsonified result of expression with tokens and everything
        Calculator newCalc = new Calculator(expression);
        String result = newCalc.toJSON();
        return new ResponseEntity<String>(result, HttpStatus.OK);

        // Bad ID
    }

}
