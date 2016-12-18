package com.bitbucket.teamleave.leafcalendar.api.transfer.users.response;

import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceGetUserInfoModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"FieldHasSetterButNoGetter", "unused"})
public class GetUserInfoResponseData {
    @JsonProperty(FIRST_NAME)
    private String firstName;
    @JsonProperty(LAST_NAME)
    private String lastName;
    @JsonProperty(EMAIL)
    private String email;
    @JsonProperty(EMAIL_VALIDATED)
    private Boolean emailValidated;

    public GetUserInfoResponseData(final UserServiceGetUserInfoModel source) {
        this.firstName = source.getFirstName();
        this.lastName = source.getLastName();
        this.email = source.getEmail();
        this.emailValidated = source.isEmailValidated();
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(final Boolean emailValidated) {
        this.emailValidated = emailValidated;
    }
}