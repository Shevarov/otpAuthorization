package by.otp.access.service;

import by.otp.access.model.Role;

import java.util.Set;

public interface RoleService {
    Role getDefaultRole();
    Set<Role> resolveRoles(Set<String> roleNames);
}
