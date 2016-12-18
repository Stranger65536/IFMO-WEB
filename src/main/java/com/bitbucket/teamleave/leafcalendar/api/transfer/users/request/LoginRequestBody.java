package com.bitbucket.teamleave.leafcalendar.api.transfer.users.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.AUTH_BASE64;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequestBody {
    @JsonProperty(AUTH_BASE64)
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(final String authToken) {
        this.authToken = authToken;
    }
}