package com.bitbucket.teamleave.leafcalendar.api.transfer.users.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequestBody {
    @JsonProperty(FIRST_NAME)
    private String firstName;
    @JsonProperty(LAST_NAME)
    private String lastName;
    @JsonProperty(AUTH_BASE64)
    private String authBase64;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getAuthBase64() {
        return authBase64;
    }

    public void setAuthBase64(final String authBase64) {
        this.authBase64 = authBase64;
    }
}