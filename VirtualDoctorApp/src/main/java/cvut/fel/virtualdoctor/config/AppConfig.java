package cvut.fel.virtualdoctor.config;

import cvut.fel.virtualdoctor.classifier.client.ClassifierClientRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${differential-list.resultsLimit}")
    private int resultsLimit;

    @Bean
    public int getResultsLimit() {
        return resultsLimit;
    }

    @Bean
    public ClassifierClientRest modelClient() {
        return new ClassifierClientRest("http://localhost:5500/classify");
    }
}
