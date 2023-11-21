package danila.sre.proberoncall.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import danila.sre.proberoncall.properties.OncallProperties;
import danila.sre.proberoncall.properties.OncallTestUser;
import feign.FeignException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
@Slf4j
public class OncallProberService {
    private final  OncallService oncallService;
    private final OncallProperties oncallProperties;

    private final OncallTestUser oncallTestUser;
    private final Counter proberLoginTotalScenario;
    private final Counter proberLoginTotalScenarioSuccess;
    private final Counter proberLoginTotalScenarioFail;


    private final Counter proberLogoutTotalScenario;
    private final Counter proberLogoutTotalScenarioSuccess;
    private final Counter proberLogoutTotalScenarioFail;
    private final Counter proberCreateUserTotalScenario;
    private final Counter proberCreateUserTotalScenarioSuccess;
    private final Counter proberCreateUserTotalScenarioFail;

    private final Gauge percentSuccessLoginScenarios;
    private final Gauge percentSuccessLogoutScenarios;

    private final Gauge percentSuccessCreateUserScenarios;

    private Double percentSuccessLoginScenariosValue = 0.0;
    private Double percentSuccessLogoutScenariosValue = 0.0;
    private Double percentSuccessCreateUserScenariosValue = 0.0;


    public OncallProberService(OncallService oncallService, OncallProperties oncallProperties, OncallTestUser oncallTestUser, MeterRegistry registry) {
        this.oncallService = oncallService;
        this.oncallProperties = oncallProperties;
        this.oncallTestUser = oncallTestUser;

        proberLoginTotalScenario = Counter.builder("prober_login_scenario")
                                                .description("Total count of runs the login  scenario to oncall API")
                                                    .register(registry);
        proberLoginTotalScenarioSuccess = Counter.builder("prober_login_scenario_success")
                                                        .description("Total count success of runs the login  scenario to oncall API")
                                                            .register(registry);
        proberLoginTotalScenarioFail = Counter.builder("prober_login_scenario_fail")
                                                    .description("Total count Fail of runs the login  scenario to oncall API")
                                                        .register(registry);
        proberLogoutTotalScenario = Counter.builder("prober_logout_scenario")
                                                .description("Total count of runs the logout  scenario to oncall API")
                                                    .register(registry);
        proberLogoutTotalScenarioSuccess = Counter.builder("prober_logout_scenario_success")
                                                        .description("Total count of runs the logout  scenario to oncall API")
                                                            .register(registry);
        proberLogoutTotalScenarioFail = Counter.builder("prober_logout_scenario_fail")
                                                    .description("Total count of runs the logout  scenario to oncall API")
                                                        .register(registry);
        proberCreateUserTotalScenario = Counter.builder("prober_create_user_scenario")
                                                    .description("Total count of runs the create user  scenario to oncall API")
                                                        .register(registry);
        proberCreateUserTotalScenarioSuccess = Counter.builder("prober_create_user_scenario_success")
                                                            .description("Total count of runs the create user  scenario to oncall API")
                                                                .register(registry);
        proberCreateUserTotalScenarioFail = Counter.builder("prober_create_user_scenario_fail")
                                                        .description("Total count of runs the create user  scenario to oncall API")
                                                            .register(registry);
        percentSuccessLoginScenarios = Gauge.builder("percent_success_login_scenarions",  () -> percentSuccessLoginScenariosValue )
                                                .register(registry);

        percentSuccessLogoutScenarios =  Gauge.builder("percent_success_logout_scenarions", () -> percentSuccessLogoutScenariosValue)
                                                .register(registry);

        percentSuccessCreateUserScenarios =  Gauge.builder("percent_success_create_user_scenarions", () -> percentSuccessCreateUserScenariosValue)
                                                    .register(registry);
    }

    public void countPercent() {
            percentSuccessLoginScenariosValue = proberLoginTotalScenarioSuccess.count() / proberLoginTotalScenario.count();
            percentSuccessLogoutScenariosValue = proberLogoutTotalScenarioSuccess.count() / proberLogoutTotalScenario.count();
            percentSuccessCreateUserScenariosValue = proberCreateUserTotalScenarioSuccess.count() / proberCreateUserTotalScenario.count();
    }

    public void proberCreateUserScenario() {
        proberCreateUserTotalScenario.increment();
        try {
            oncallService.createUser(oncallTestUser);
            proberCreateUserTotalScenarioSuccess.increment();
            log.info("Success create user");


        } catch (FeignException e) {
            proberCreateUserTotalScenarioFail.increment();
            throw new RuntimeException(e);
        }
        finally {
            try {
                oncallService.deleteUser(oncallTestUser.getName());
                log.info("Success delete test user");
            } catch (FeignException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void probeLogin()  {
        proberLoginTotalScenario.increment();
        try {
            oncallService.login(oncallProperties);
            proberLoginTotalScenarioSuccess.increment();
            log.info("Success login ");

        } catch (JsonProcessingException | FeignException e) {
            proberLoginTotalScenarioFail.increment();
            throw new RuntimeException(e);
        }
    }

    public void probeLogout()  {
        String token = "";
        proberLogoutTotalScenario.increment();
        proberLoginTotalScenario.increment();
        try {
            token = oncallService.login(oncallProperties);
            proberLoginTotalScenarioSuccess.increment();
            log.info("Success login ");
            oncallService.logout(token);
            log.info("Success logout ");
            proberLogoutTotalScenarioSuccess.increment();


        } catch (JsonProcessingException | FeignException e) {
            if(token.isEmpty()) {
                proberLoginTotalScenarioFail.increment();
            }
            proberLogoutTotalScenarioFail.increment();
            throw new RuntimeException(e);
        }
    }

}
