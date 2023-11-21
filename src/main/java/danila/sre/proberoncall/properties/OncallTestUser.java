package danila.sre.proberoncall.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "prober.user")
public class OncallTestUser {
    private String name;
    private String fullName;
    private String timeZone;
    private String photoUrl;
    private int active;
    private int god;
    private Contacts contacts;

    @Data
    @Component
    @ConfigurationProperties(prefix = "prober.user.contacts")
    public static class Contacts {
        private String email;
        private String sms;
        private String call;
    }
}
