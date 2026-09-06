package by.otp.access.service.impl;

import by.otp.access.config.properties.SecurityProperties;
import by.otp.access.exception.RecordNotFoundException;
import by.otp.access.model.Role;
import by.otp.access.repository.RoleRepository;
import by.otp.access.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final SecurityProperties securityProperties;

    @Override
    public Role getDefaultRole() {
        return roleRepository.findByName(securityProperties.defaultRoleName())
                .orElseThrow(() -> new IllegalStateException(
                        "Default role '%s' is not configured".formatted(securityProperties.defaultRoleName())));
    }

    @Override
    public Set<Role> resolveRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Set.of(getDefaultRole());
        }
        return roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new RecordNotFoundException("Role not found: " + name)))
                .collect(Collectors.toSet());
    }
}