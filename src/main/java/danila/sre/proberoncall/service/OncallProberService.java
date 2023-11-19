package danila.sre.proberoncall.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import feign.RetryableException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class OncallProberService {
    private final  OncallService oncallService;
    private final Counter proberLoginTotalScenario;
    private final Counter proberLoginTotalScenarioSuccess;
    private final Counter proberLoginTotalScenarioFail;


    public OncallProberService(OncallService oncallService, MeterRegistry registry) {
        this.oncallService = oncallService;

        proberLoginTotalScenario = Counter.builder("prober_login_scenario")
                                                    .description("Total count of runs the login  scenario to oncall API")
                                                        .register(registry);
        proberLoginTotalScenarioSuccess = Counter.builder("prober_login_scenario_success")
                                                    .description("Total count success of runs the login  scenario to oncall API")
                                                        .register(registry);
        proberLoginTotalScenarioFail = Counter.builder("prober_login_scenario_fail")
                                                .description("Total count Fail of runs the login  scenario to oncall API")
                                                    .register(registry);
    }

    public void probeRootPath() {

    }

    public void probeLogin()  {
        proberLoginTotalScenario.increment();
        try {
            String token = oncallService.login();
            proberLoginTotalScenarioSuccess.increment();

        } catch (JsonProcessingException | RetryableException e) {
            proberLoginTotalScenarioFail.increment();
            throw new RuntimeException(e);
        }
    }

}
