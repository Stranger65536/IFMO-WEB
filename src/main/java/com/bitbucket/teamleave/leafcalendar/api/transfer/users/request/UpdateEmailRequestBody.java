package com.bitbucket.teamleave.leafcalendar.api.transfer.users.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.NEW_EMAIL_BASE64;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.PASSWORD_BASE64;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateEmailRequestBody {
    @JsonProperty(PASSWORD_BASE64)
    private String passwordBase64;
    @JsonProperty(NEW_EMAIL_BASE64)
    private String newEmailBase64;

    public String getPasswordBase64() {
        return passwordBase64;
    }

    public void setPasswordBase64(final String passwordBase64) {
        this.passwordBase64 = passwordBase64;
    }

    public String getNewEmailBase64() {
        return newEmailBase64;
    }

    public void setNewEmailBase64(final String newEmailBase64) {
        this.newEmailBase64 = newEmailBase64;
    }
}