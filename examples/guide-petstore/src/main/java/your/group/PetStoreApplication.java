package your.group;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 工程启动类
 */
@SpringBootApplication
public class PetStoreApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PetStoreApplication.class).run(args);
    }

}
