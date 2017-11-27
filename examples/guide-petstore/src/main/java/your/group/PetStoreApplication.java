package your.group;

import com.tairanchina.csp.dew.core.autoconfigure.DewBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
@DewBootApplication
public class PetStoreApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PetStoreApplication.class).run(args);
    }

}
