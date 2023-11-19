package danila.sre.proberoncall.apiclient;

import danila.sre.proberoncall.properties.OncallProperties;
import feign.RetryableException;
import io.micrometer.core.annotation.Timed;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
;

@FeignClient(name = "oncallclient", url = "${oncall.url}")
public interface OncallApiClient {
    @Timed(value ="root_path_request_time",
                description = "Time taken to execute / API request",
                    percentiles = {0.5, 0.8, 0.95, 0.99})
    @GetMapping()
    ResponseEntity<String> getRootPath() throws RetryableException;

    @Timed(value = "probe_login_request_time",
                description = "Time taken to execute /login API request",
                    percentiles = {0.5, 0.8, 0.95, 0.99} )
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<String> login(@RequestBody OncallProperties oncallProperties) throws RetryableException;


}
