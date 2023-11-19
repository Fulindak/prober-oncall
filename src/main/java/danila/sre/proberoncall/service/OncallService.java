package danila.sre.proberoncall.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import danila.sre.proberoncall.apiclient.OncallApiClient;
import danila.sre.proberoncall.properties.OncallProperties;
import feign.RetryableException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OncallService {
    private final OncallApiClient oncallApiClient;
    private final OncallProperties oncallProperties;

    public OncallService(OncallApiClient oncallApiClient, OncallProperties oncallProperties) {
        this.oncallApiClient = oncallApiClient;
        this.oncallProperties = oncallProperties;
    }

    public String login() throws JsonProcessingException, RetryableException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(oncallApiClient.login(oncallProperties).getBody());
        return  rootNode.get("csrf_token").asText();
    }

    public ResponseEntity<String> getRootPath() throws RetryableException{
        return oncallApiClient.getRootPath();
    }
}
