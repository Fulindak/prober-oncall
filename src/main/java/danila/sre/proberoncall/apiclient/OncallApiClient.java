package danila.sre.proberoncall.apiclient;

import danila.sre.proberoncall.properties.OncallProperties;
import danila.sre.proberoncall.properties.OncallTestUser;
import feign.FeignException;
import io.micrometer.core.annotation.Timed;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
;

@FeignClient(name = "oncallclient", url = "${oncall.url}")
public interface OncallApiClient {

    @Timed(value = "probe_login_request_time",
                description = "Time taken to execute /login API request",
                    percentiles = {0.5, 0.8, 0.95, 0.99} )
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<String> login(@RequestBody OncallProperties oncallProperties) throws FeignException;

    @Timed(value = "probe_logout_request_time",
            description = "Time taken to execute /logout API request",
            percentiles = {0.5, 0.8, 0.95, 0.99} )
    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<String> logout(@RequestAttribute("X-CSRF-TOKEN") String token) throws FeignException;

    @Timed(value = "probe_logout_request_time",
            description = "Time taken to execute /logout API request",
            percentiles = {0.5, 0.8, 0.95, 0.99} )
    @PostMapping(value = "/api/v0/users/", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createUser(@RequestBody OncallTestUser oncallTestUser);

    @DeleteMapping("api/v0/users/{userName}")
    ResponseEntity<String> deleteUser(@PathVariable(value = "userName")String name);
}
