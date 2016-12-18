package com.bitbucket.teamleave.leafcalendar.api.transfer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"UnusedParameters", "DuplicateStringLiteralInspection", "unused"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericResponseEntity<T> {
    @JsonProperty("status")
    private boolean successStatus;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private T data;

    @SuppressWarnings("AssignmentToNull")
    public GenericResponseEntity() {
        this.successStatus = true;
        this.message = "";
        this.data = null;
    }

    @JsonCreator
    public GenericResponseEntity(
            @JsonProperty("status") final boolean successStatus,
            @JsonProperty("message") final String message,
            @JsonProperty("data") final T data) {
        this.successStatus = successStatus;
        this.message = message;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public boolean isSuccessStatus() {
        return successStatus;
    }

    public void setStatus(final boolean successStatus) {
        this.successStatus = false;
    }
}