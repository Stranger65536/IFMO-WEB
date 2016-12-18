package com.bitbucket.teamleave.leafcalendar.api.model;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.regex.Pattern;

import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.Users.*;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.NULL;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "FieldHasSetterButNoGetter"})
public class UserModel {
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^[A-F0-9]{32}$");
    private static final Pattern FIRST_NAME_PATTERN = Pattern.compile("^.{1,255}$");
    private static final Pattern LAST_NAME_PATTERN = Pattern.compile("^.{1,255}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{8,}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
    private static final Pattern EMAIL_VALIDATION_TOKEN_PATTERN = Pattern.compile("^[A-F0-9]{32}$");

    private String userId;
    private String firstName;
    private String lastName;
    private Integer passwordHash;
    private String email;
    private Boolean emailValidated;
    private String emailValidationToken;

    public UserModel() {
    }

    public UserModel(final String userId) throws UserModelException {
        this.userId = validateUserId(userId);
    }

    public static String validateUserId(final String userId) throws
            UserModelException {
        if (userId != null && USER_ID_PATTERN.matcher(userId).matches()) {
            return userId;
        } else {
            throw new UserModelException(USER_ID, userId);
        }
    }

    private static String validateFirstName(final String firstName) throws
            UserModelException {
        final String preparedString;
        if (firstName != null && FIRST_NAME_PATTERN.matcher(preparedString = firstName.trim()).matches()) {
            return preparedString;
        } else {
            throw new UserModelException(FIRST_NAME, firstName);
        }
    }

    private static String validateLastName(final String lastName) throws
            UserModelException {
        final String preparedString;
        if (lastName != null && LAST_NAME_PATTERN.matcher(preparedString = lastName.trim()).matches()) {
            return preparedString;
        } else {
            throw new UserModelException(LAST_NAME, lastName);
        }
    }

    private static Integer validatePassword(final String password) throws
            UserModelException {
        if (password != null && PASSWORD_PATTERN.matcher(password).matches()) {
            return password.hashCode();
        } else if (password == null) {
            throw new UserModelException(PASSWORD_HASH, NULL);
        } else {
            throw new UserModelException(PASSWORD_HASH, password);
        }
    }

    private static Integer validatePasswordHash(final Integer passwordHash) throws
            UserModelException {
        if (passwordHash != null) {
            return passwordHash;
        } else {
            throw new UserModelException(PASSWORD_HASH, NULL);
        }
    }

    private static String validateEmail(final String email) throws
            UserModelException {
        final String preparedString;
        if (email != null && EMAIL_PATTERN.matcher(preparedString = email.trim()).matches()) {
            return preparedString;
        }
        throw new UserModelException(EMAIL, email);
    }

    private static Boolean validateEmailValidated(final Boolean emailValidated) throws
            UserModelException {
        if (emailValidated != null) {
            return emailValidated;
        } else {
            throw new UserModelException(EMAIL_VALIDATED, NULL);
        }
    }

    private static String validateValidationToken(final String emailValidationToken) throws
            UserModelException {
        if (emailValidationToken != null && EMAIL_VALIDATION_TOKEN_PATTERN.matcher(emailValidationToken).matches()) {
            return emailValidationToken;
        } else {
            throw new UserModelException(EMAIL_VALIDATION_TOKEN, emailValidationToken);
        }
    }

    public void populateWithRegistrationResult(final Pair<String, String> generatedInfo) throws
            UserModelException {
        this.setUserId(generatedInfo.getLeft());
        this.setEmailValidationToken(generatedInfo.getRight());
    }

    public void populateWithRegistrationInfo(
            final String firstName,
            final String lastName,
            final String email,
            final String password) throws
            UserModelException {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setPassword(password);
    }

    public void populateWithValidationInfo(
            final String userId,
            final String validationToken) throws
            UserModelException {
        this.setUserId(userId);
        this.setEmailValidationToken(validationToken);
    }

    public void populateWithLoginInfo(
            final String email,
            final String password) throws
            UserModelException {
        this.setEmail(email);
        this.setPassword(password);
    }

    public void populateWithPasswordInfo(
            final String userId,
            final String password) throws
            UserModelException {
        this.setUserId(userId);
        this.setPassword(password);
    }

    public void populateWithUpdateInfo(
            final String firstName,
            final String lastName) throws
            UserModelException {
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public boolean passwordDifference(final UserModel model) {
        return !passwordHash.equals(model.passwordHash);
    }

    public boolean hasDifferentId(final String id) {
        return !userId.equals(id);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) throws
            UserModelException {
        this.userId = validateUserId(userId);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) throws
            UserModelException {
        this.firstName = validateFirstName(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) throws
            UserModelException {
        this.lastName = validateLastName(lastName);
    }

    public Integer getPasswordHash() {
        return passwordHash;
    }

    @SuppressWarnings("unused")
    public void setPasswordHash(final Integer passwordHash) throws
            UserModelException {
        this.passwordHash = validatePasswordHash(passwordHash);
    }

    public void setPassword(final String password) throws
            UserModelException {
        this.passwordHash = validatePassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) throws
            UserModelException {
        this.email = validateEmail(email);
    }

    public Boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(final Boolean emailValidated) throws
            UserModelException {
        this.emailValidated = validateEmailValidated(emailValidated);
    }

    public void validateEmail() throws
            UserModelException {
        this.setEmailValidated(true);
    }

    public void invalidateEmail() throws
            UserModelException {
        this.setEmailValidated(false);
    }

    public String getEmailValidationToken() {
        return emailValidationToken;
    }

    public void setEmailValidationToken(final String emailValidationToken) throws
            UserModelException {
        this.emailValidationToken = validateValidationToken(emailValidationToken);
    }
}
