package danila.sre.proberoncall.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import danila.sre.proberoncall.apiclient.OncallApiClient;
import danila.sre.proberoncall.properties.OncallProperties;
import danila.sre.proberoncall.properties.OncallTestUser;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OncallService {
    private final OncallApiClient oncallApiClient;


    public OncallService(OncallApiClient oncallApiClient) {
        this.oncallApiClient = oncallApiClient;
    }

    public String login(OncallProperties properties) throws JsonProcessingException, FeignException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(oncallApiClient.login(properties).getBody());
        return  rootNode.get("csrf_token").asText();
    }


    public ResponseEntity<String> logout(String token) throws FeignException {
        return oncallApiClient.logout(token);
    }

    public ResponseEntity<String> createUser(OncallTestUser testUser) throws FeignException {
        return  oncallApiClient.createUser(testUser);
    }

    public ResponseEntity<String> deleteUser(String name) throws  FeignException {
        return oncallApiClient.deleteUser(name);
    }
}
