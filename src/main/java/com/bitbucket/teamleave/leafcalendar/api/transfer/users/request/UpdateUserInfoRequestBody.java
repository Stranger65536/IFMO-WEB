package com.bitbucket.teamleave.leafcalendar.api.transfer.users.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.FIRST_NAME;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.LAST_NAME;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserInfoRequestBody {
    @JsonProperty(FIRST_NAME)
    private String firstName;
    @JsonProperty(LAST_NAME)
    private String lastName;

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
}