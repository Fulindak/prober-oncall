package danila.sre.proberoncall.scheduler;

import danila.sre.proberoncall.service.OncallProberService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OncallSheduler {
    private final OncallProberService oncallProberService;

    public OncallSheduler(OncallProberService oncallProberService) {
        this.oncallProberService = oncallProberService;
    }

    @Scheduled(fixedRateString = "${oncall.interval}")
    public void probeRootPath()    {
        oncallProberService.probeRootPath();
    }

    @Scheduled(fixedRateString = "${oncall.interval}")
    public void proberLogin() {
        oncallProberService.probeLogin();
    }
}
