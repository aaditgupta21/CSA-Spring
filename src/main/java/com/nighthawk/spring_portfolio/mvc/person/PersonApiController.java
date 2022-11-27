package com.nighthawk.spring_portfolio.mvc.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import java.util.*;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/person")
public class PersonApiController {
    /*
     * #### RESTful API ####
     * Resource: https://spring.io/guides/gs/rest-service/
     */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private PersonJpaRepository repository;

    /*
     * GET List of People
     */
    @GetMapping("/")
    public ResponseEntity<List<Person>> getPeople() {
        return new ResponseEntity<>(repository.findAllByOrderByNameAsc(), HttpStatus.OK);
    }

    /*
     * GET individual Person using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable long id) {
        Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) { // Good ID
            Person person = optional.get(); // value from findByID
            return new ResponseEntity<>(person, HttpStatus.OK); // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*
     * DELETE individual Person using ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Person> deletePerson(@PathVariable long id) {
        Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) { // Good ID
            Person person = optional.get(); // value from findByID
            repository.deleteById(id); // value from findByID
            return new ResponseEntity<>(person, HttpStatus.OK); // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*
     * POST Aa record by Requesting Parameters from URI
     */
    @PostMapping("/post")
    public ResponseEntity<Object> postPerson(@RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("dob") String dobString, @RequestParam("height") double height,
            @RequestParam("weight") double weight, @RequestParam("gender") char gender)
            throws NoSuchAlgorithmException {
        Date dob;
        try {
            dob = new SimpleDateFormat("MM-dd-yyyy").parse(dobString);
        } catch (Exception e) {
            return new ResponseEntity<>(dobString + " error; try MM-dd-yyyy", HttpStatus.BAD_REQUEST);
        }
        // A person object WITHOUT ID will create a new record with default roles as
        // student
        Person person = new Person();
        person.setEmail(email);
        person.setPassword(password);
        person.setName(name);
        person.setDob(dob);
        person.setHeight(height);
        person.setWeight(weight);
        char yo;
        yo = Character.toUpperCase(gender);
        person.setGender(yo);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        String computedPasswordHash = new String(encodedHash);
        person.setPasswordHash(computedPasswordHash);

        repository.save(person);
        return new ResponseEntity<>(email + " is created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/getAge/{id}")
    public String getAge(@PathVariable long id) {
        Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) { // Good ID
            Person person = optional.get(); // value from findByID
            return person.getAgeToString();
        }
        // Bad ID
        return "ID Not found";
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> personStats(@RequestParam("id") long id) {
        Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) { // Good ID
            Person person = optional.get(); // value from findByID
            return new ResponseEntity<>(person.getStats(), HttpStatus.OK); // OK HTTP response: status code, headers,
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*
     * The personSearch API looks across database for partial match to term (k,v)
     * passed by RequestEntity body
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> personSearch(@RequestBody final Map<String, String> map) {
        // extract term from RequestEntity
        String term = (String) map.get("term");

        // JPA query to filter on term
        List<Person> list = repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(term, term);

        // return resulting list and status, error checking should be added
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*
     * The personStats API adds stats by Date to Person table
     */
    @PostMapping(value = "/setStats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> personStats(@RequestBody final Map<String, Object> stat_map)
            throws NoSuchAlgorithmException {
        // find ID
        long id = Long.parseLong((String) stat_map.get("id"));
        Optional<Person> optional = repository.findById((id));
        if (optional.isPresent()) { // Good ID
            Person person = optional.get(); // value from findByID
            String password = (String) stat_map.get("password");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] encodedHash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);
            if (!computedPasswordHash.equals(person.getPasswordHash())) {
                return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);
            }

            // Extract Attributes from JSON
            Map<String, Object> attributeMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : stat_map.entrySet()) {
                // Add all attribute other than "date" to the "attribute_map"
                if (!entry.getKey().equals("date") && !entry.getKey().equals("id")
                        && !entry.getKey().equals("password"))
                    attributeMap.put(entry.getKey(), entry.getValue());
            }

            var wrapper = new Object() {
                boolean isGood = false;
            };

            if (stat_map.containsKey("steps") && stat_map.containsKey("calories")
                    && stat_map.containsKey("miles") && stat_map.containsKey("date")
                    && stat_map.containsKey("id") && stat_map.containsKey("password")) {
                wrapper.isGood = true;
            }

            System.out.println(wrapper.isGood);

            if (!wrapper.isGood) {
                return new ResponseEntity<>("Not all keys are present in request", HttpStatus.BAD_REQUEST);
            }

            // Set Date and Attributes to SQL HashMap
            Map<String, Map<String, Object>> date_map = person.getStats();
            date_map.put((String) stat_map.get("date"), attributeMap);
            person.setStats(date_map); // BUG, needs to be customized to replace if existing or append if new
            repository.save(person); // conclude by writing the stats updates

            // return Person with update Stats
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
        // return Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
