package by.otp.access.config;

import by.otp.access.model.Role;
import by.otp.access.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationRunner {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";

    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        ensureRole(USER_ROLE, "Default role granted on self-registration");
        ensureRole(ADMIN_ROLE, "Administrative access");
    }

    private void ensureRole(String name, String comment) {
        roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(Role.builder().name(name).comment(comment).build()));
    }
}
