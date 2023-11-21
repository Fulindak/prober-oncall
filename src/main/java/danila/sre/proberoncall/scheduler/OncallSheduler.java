package danila.sre.proberoncall.scheduler;

import danila.sre.proberoncall.service.OncallProberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OncallSheduler {
    private final OncallProberService oncallProberService;

    public OncallSheduler(OncallProberService oncallProberService) {
        this.oncallProberService = oncallProberService;
    }

    @Scheduled(fixedRateString = "${oncall.interval_low}")
    public void proberCreateUserScenario() {
        log.info("Start scheduled create user prober");
        oncallProberService.proberCreateUserScenario();
        log.info("End scheduled create user prober");
    }

    @Scheduled(fixedRateString = "${oncall.interval_low}")
    public void proberLogin() {
        log.info("Start scheduled login prober");
        oncallProberService.probeLogin();
        log.info("End scheduled login prober");
    }

    @Scheduled(fixedRateString = "${oncall.interval_low}")
    public void proberLogout() {
        log.info("Start scheduled logout prober");
        oncallProberService.probeLogout();
        log.info("End scheduled logout prober");
    }

    @Scheduled(fixedRateString = "${oncall.interval_low}")
    public void countPercentProbe() {
        log.info("Start scheduled count percent");
        oncallProberService.countPercent();
        log.info("End scheduled count prober");
    }
}
