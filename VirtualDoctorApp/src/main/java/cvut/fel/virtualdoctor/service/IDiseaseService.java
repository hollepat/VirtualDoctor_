package cvut.fel.virtualdoctor.service;


import cvut.fel.virtualdoctor.model.DiseaseDetails;

public interface IDiseaseService {
    DiseaseDetails getDiseaseDetails(String disease);
    String getShortDescription(String disease);
}
