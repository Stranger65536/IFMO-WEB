package com.bitbucket.teamleave.leafcalendar.api.transfer.users.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.NEW_PASSWORD_BASE64;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.OLD_PASSWORD_BASE64;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePasswordRequestBody {
    @JsonProperty(OLD_PASSWORD_BASE64)
    private String oldPasswordBase64;
    @JsonProperty(NEW_PASSWORD_BASE64)
    private String newPasswordBase64;

    public String getOldPasswordBase64() {
        return oldPasswordBase64;
    }

    public void setOldPasswordBase64(final String oldPasswordBase64) {
        this.oldPasswordBase64 = oldPasswordBase64;
    }

    public String getNewPasswordBase64() {
        return newPasswordBase64;
    }

    public void setNewPasswordBase64(final String newPasswordBase64) {
        this.newPasswordBase64 = newPasswordBase64;
    }
}