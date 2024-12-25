package cvut.fel.virtualdoctor.controller;

import org.springframework.http.ResponseEntity;

public interface IDiseaseController {
    ResponseEntity<String> getLongDescription(String disease);
    ResponseEntity<String> getShortDescription(String disease);
}
