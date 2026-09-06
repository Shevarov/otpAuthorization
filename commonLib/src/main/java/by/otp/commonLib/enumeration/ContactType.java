package by.otp.commonLib.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Supported channels a user can be contacted through and authenticated with.
 */
@AllArgsConstructor
@Getter
public enum ContactType {
    PHONE("PHONE"),
    EMAIL("EMAIL");

    private final String displayName;
}
