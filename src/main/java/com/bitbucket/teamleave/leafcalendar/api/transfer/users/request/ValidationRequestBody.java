package com.bitbucket.teamleave.leafcalendar.api.transfer.users.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.EMAIL_VALIDATION_TOKEN;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationRequestBody {
    @JsonProperty(EMAIL_VALIDATION_TOKEN)
    private String validationToken;

    public String getValidationToken() {
        return validationToken;
    }

    public void setValidationToken(final String validationToken) {
        this.validationToken = validationToken;
    }
}