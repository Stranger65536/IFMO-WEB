package com.bitbucket.teamleave.leafcalendar.api.facades;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserAlreadyRegisteredException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserAlreadyValidatedException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserInvalidCredentialsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.AuthTokenModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;
import com.bitbucket.teamleave.leafcalendar.api.service.UserService;
import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceGetUserInfoModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceLoginModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceRegistrationModel;
import com.bitbucket.teamleave.leafcalendar.api.transfer.GenericResponseEntity;
import com.bitbucket.teamleave.leafcalendar.api.transfer.users.request.*;
import com.bitbucket.teamleave.leafcalendar.api.transfer.users.response.*;
import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;
import javafx.util.Pair;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.INVALID_FIELD_FORMAT;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Internal.INTERNAL_EXCEPTION;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Internal.OPERATION_UNAVAILABLE;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Users.*;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.*;
import static com.bitbucket.teamleave.leafcalendar.api.utils.LoggerConstants.Users.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("OverlyCoupledClass")
@Service("UserFacade")
public class UserFacadeImpl implements UserFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserFacadeImpl.class);
    @Autowired
    UserService userService;

    @SuppressWarnings("LawOfDemeter")
    @Override
    public GenericResponseEntity<RegistrationResponseData> registerUser(
            final RegistrationRequestBody request) {
        final GenericResponseEntity<RegistrationResponseData> response =
                new GenericResponseEntity<>();
        final String authBase64 = request.getAuthBase64();
        final String firstName = request.getFirstName();
        final String lastName = request.getLastName();
        try {
            final Pair<String, String> decoded = Utils.decodeEmailPassword(authBase64);
            final UserServiceRegistrationModel registrationInfo =
                    userService.registerUser(firstName, lastName,
                            decoded.getKey(), decoded.getValue());
            response.setData(new RegistrationResponseData(registrationInfo));
            LOGGER.info(Utils.wrap(USER_REGISTRATION_OK,
                    FIRST_NAME, firstName,
                    LAST_NAME, lastName,
                    EMAIL, decoded.getKey(),
                    PASSWORD, decoded.getValue(),
                    AUTH_TOKEN, registrationInfo.getUserId(),
                    EMAIL_VALIDATION_TOKEN, registrationInfo.getValidationToken()));
        } catch (UserModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(USER_REGISTRATION_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    FIRST_NAME, firstName,
                    LAST_NAME, lastName,
                    AUTH_BASE64, authBase64));
        } catch (UserAlreadyRegisteredException ignored) {
            response.setStatus(false);
            response.setMessage(USER_ALREADY_REGISTERED);
            LOGGER.warn(Utils.wrap(USER_REGISTRATION_FAIL,
                    USER_ALREADY_REGISTERED, "",
                    FIRST_NAME, firstName,
                    LAST_NAME, lastName,
                    AUTH_BASE64, authBase64));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(USER_REGISTRATION_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    FIRST_NAME, firstName,
                    LAST_NAME, lastName,
                    AUTH_BASE64, authBase64,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<ValidationResponseData> validateUser(
            final String userId,
            final ValidationRequestBody request) {
        final GenericResponseEntity<ValidationResponseData> response =
                new GenericResponseEntity<>();
        final String validationToken = request.getValidationToken();
        try {
            userService.validateUser(userId, validationToken);
            LOGGER.info(Utils.wrap(USER_VALIDATION_OK,
                    USER_ID, userId,
                    EMAIL_VALIDATION_TOKEN, validationToken));
        } catch (UserModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(USER_VALIDATION_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    USER_ID, userId,
                    EMAIL_VALIDATION_TOKEN, validationToken));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(USER_VALIDATION_FAIL,
                    USER_NOT_EXISTS, "",
                    USER_ID, userId,
                    EMAIL_VALIDATION_TOKEN, validationToken));
        } catch (UserAlreadyValidatedException ignored) {
            response.setStatus(false);
            response.setMessage(USER_ALREADY_VALIDATED);
            LOGGER.warn(Utils.wrap(USER_VALIDATION_FAIL,
                    USER_ALREADY_VALIDATED, "",
                    USER_ID, userId,
                    EMAIL_VALIDATION_TOKEN, validationToken));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(USER_VALIDATION_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    USER_ID, userId,
                    EMAIL_VALIDATION_TOKEN, validationToken,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<LoginResponseData> loginUser(
            final LoginRequestBody request) {
        final GenericResponseEntity<LoginResponseData> response =
                new GenericResponseEntity<>();
        final String authBase64 = request.getAuthToken();
        try {
            final Pair<String, String> decoded = Utils.decodeEmailPassword(authBase64);
            final UserServiceLoginModel loginInfo = userService.loginUser(
                    decoded.getKey(), decoded.getValue());
            response.setData(new LoginResponseData(loginInfo));
            LOGGER.info(Utils.wrap(USER_LOGIN_OK,
                    EMAIL, decoded.getKey(),
                    PASSWORD, decoded.getValue()));
        } catch (UserModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(USER_LOGIN_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    AUTH_BASE64, authBase64));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(USER_LOGIN_FAIL,
                    USER_NOT_EXISTS, "",
                    AUTH_BASE64, authBase64));
        } catch (UserInvalidCredentialsException ignored) {
            response.setStatus(false);
            response.setMessage(INVALID_CREDENTIALS);
            LOGGER.warn(Utils.wrap(USER_LOGIN_FAIL,
                    INVALID_CREDENTIALS, "",
                    AUTH_BASE64, authBase64));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(USER_LOGIN_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    AUTH_BASE64, authBase64,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<UpdatePasswordResponseData> updatePassword(
            final String userId,
            final String authToken,
            final UpdatePasswordRequestBody request) {
        final GenericResponseEntity<UpdatePasswordResponseData> response =
                new GenericResponseEntity<>();
        final String oldPasswordBase64 = request.getOldPasswordBase64();
        final String newPasswordBase64 = request.getNewPasswordBase64();
        try {
            final String oldPasswordDecoded = Utils.decodePassword(oldPasswordBase64);
            final String newPasswordDecoded = Utils.decodePassword(newPasswordBase64);
            userService.updatePassword(userId, authToken,
                    oldPasswordDecoded, newPasswordDecoded);
            LOGGER.info(Utils.wrap(USER_UPDATE_PASSWORD_OK,
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    OLD_PASSWORD, oldPasswordBase64,
                    NEW_PASSWORD, newPasswordBase64));
        } catch (UserModelException | AuthTokenModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(USER_UPDATE_PASSWORD_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    OLD_PASSWORD_BASE64, oldPasswordBase64,
                    NEW_EMAIL_BASE64, newPasswordBase64));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(USER_UPDATE_PASSWORD_FAIL,
                    USER_NOT_EXISTS, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    OLD_PASSWORD_BASE64, oldPasswordBase64,
                    NEW_EMAIL_BASE64, newPasswordBase64));
        } catch (UserInvalidCredentialsException ignored) {
            response.setStatus(false);
            response.setMessage(INVALID_CREDENTIALS);
            LOGGER.warn(Utils.wrap(USER_UPDATE_PASSWORD_FAIL,
                    INVALID_CREDENTIALS, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    OLD_PASSWORD_BASE64, oldPasswordBase64,
                    NEW_PASSWORD_BASE64, newPasswordBase64));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(USER_UPDATE_PASSWORD_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    OLD_PASSWORD_BASE64, oldPasswordBase64,
                    NEW_EMAIL_BASE64, newPasswordBase64,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<UpdateEmailResponseData> updateEmail(
            final String userId,
            final String authToken,
            final UpdateEmailRequestBody request) {
        final GenericResponseEntity<UpdateEmailResponseData> response =
                new GenericResponseEntity<>();
        final String passwordBase64 = request.getPasswordBase64();
        final String newEmailBase64 = request.getNewEmailBase64();
        try {
            final String passwordDecoded = Utils.decodePassword(passwordBase64);
            final String newEmailDecoded = Utils.decodeEmail(newEmailBase64);
            userService.updateEmail(userId, authToken,
                    passwordDecoded, newEmailDecoded);
            LOGGER.info(Utils.wrap(USER_UPDATE_EMAIL_OK,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    OLD_PASSWORD, passwordBase64,
                    NEW_PASSWORD, newEmailBase64));
        } catch (UserModelException | AuthTokenModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(USER_UPDATE_EMAIL_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    OLD_PASSWORD_BASE64, passwordBase64,
                    NEW_EMAIL_BASE64, newEmailBase64));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(USER_UPDATE_EMAIL_FAIL,
                    USER_NOT_EXISTS, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    OLD_PASSWORD_BASE64, passwordBase64,
                    NEW_EMAIL_BASE64, newEmailBase64));
        } catch (UserInvalidCredentialsException ignored) {
            response.setStatus(false);
            response.setMessage(INVALID_CREDENTIALS);
            LOGGER.warn(Utils.wrap(USER_UPDATE_EMAIL_FAIL,
                    INVALID_CREDENTIALS, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    OLD_PASSWORD_BASE64, passwordBase64,
                    NEW_PASSWORD_BASE64, newEmailBase64));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(USER_UPDATE_EMAIL_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    OLD_PASSWORD_BASE64, passwordBase64,
                    NEW_EMAIL_BASE64, newEmailBase64,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<SendValidationMailResponseData> sendValidationMail(
            final String userId,
            final String authToken) {
        final GenericResponseEntity<SendValidationMailResponseData> response =
                new GenericResponseEntity<>();
        try {
            userService.sendValidationMail(userId, authToken);
            LOGGER.info(Utils.wrap(USER_SEND_EMAIL_OK,
                    USER_ID, userId,
                    AUTH_TOKEN, authToken));
        } catch (AuthTokenModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.info(Utils.wrap(USER_SEND_EMAIL_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    USER_ID, userId,
                    AUTH_TOKEN, authToken));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.info(Utils.wrap(USER_SEND_EMAIL_FAIL,
                    USER_NOT_EXISTS, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken));
        } catch (UserAlreadyValidatedException ignored) {
            response.setStatus(false);
            response.setMessage(USER_ALREADY_VALIDATED);
            LOGGER.info(Utils.wrap(USER_SEND_EMAIL_FAIL,
                    USER_ALREADY_VALIDATED, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.info(Utils.wrap(USER_SEND_EMAIL_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<UpdateAuthTokenResponseData> updateAuthToken(
            final String userId,
            final String authToken) {
        final GenericResponseEntity<UpdateAuthTokenResponseData> response =
                new GenericResponseEntity<>();
        try {
            userService.updateAuthToken(userId, authToken);
            LOGGER.info(Utils.wrap(USER_UPDATE_TOKEN_OK,
                    USER_ID, userId,
                    AUTH_TOKEN, authToken));
        } catch (AuthTokenModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.info(Utils.wrap(USER_UPDATE_TOKEN_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    USER_ID, userId,
                    AUTH_TOKEN, authToken));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.info(Utils.wrap(USER_UPDATE_TOKEN_FAIL,
                    USER_NOT_EXISTS, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.info(Utils.wrap(USER_UPDATE_TOKEN_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<GetUserInfoResponseData> getUserInfo(
            final String userId,
            final String authToken) {
        final GenericResponseEntity<GetUserInfoResponseData> response =
                new GenericResponseEntity<>();
        try {
            final UserServiceGetUserInfoModel result =
                    userService.getUserInfo(userId, authToken);
            response.setData(new GetUserInfoResponseData(result));
            LOGGER.info(Utils.wrap(USER_GET_INFO_OK,
                    USER_ID, userId,
                    AUTH_TOKEN, authToken));
        } catch (AuthTokenModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.info(Utils.wrap(USER_GET_INFO_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    USER_ID, userId,
                    AUTH_TOKEN, authToken));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.info(Utils.wrap(USER_GET_INFO_FAIL,
                    USER_NOT_EXISTS, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.info(Utils.wrap(USER_GET_INFO_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<UpdateUserInfoResponseData> updateUserInfo(
            final String userId,
            final String authToken,
            final UpdateUserInfoRequestBody request) {
        final GenericResponseEntity<UpdateUserInfoResponseData> response =
                new GenericResponseEntity<>();
        try {
            userService.updateUserInfo(userId, authToken, request);
            LOGGER.info(Utils.wrap(USER_UPDATE_INFO_OK,
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    FIRST_NAME, request.getFirstName(),
                    LAST_NAME, request.getLastName()));
        } catch (UserModelException | AuthTokenModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.info(Utils.wrap(USER_UPDATE_INFO_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    FIRST_NAME, request.getFirstName(),
                    LAST_NAME, request.getLastName()));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.info(Utils.wrap(USER_UPDATE_INFO_FAIL,
                    USER_NOT_EXISTS, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    FIRST_NAME, request.getFirstName(),
                    LAST_NAME, request.getLastName()));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.info(Utils.wrap(USER_UPDATE_INFO_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    FIRST_NAME, request.getFirstName(),
                    LAST_NAME, request.getLastName(),
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<DeleteUserResponseData> deleteUser(
            final String userId,
            final String authToken,
            final DeleteUserRequestBody request) {
        final GenericResponseEntity<DeleteUserResponseData> response =
                new GenericResponseEntity<>();
        final String passwordBase64 = request.getPasswordBase64();
        try {
            final String passwordDecoded = Utils.decodePassword(passwordBase64);
            userService.deleteUser(userId, authToken, passwordDecoded);
            LOGGER.info(Utils.wrap(USER_DELETE_OK,
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    PASSWORD_BASE64, passwordBase64));
        } catch (UserModelException | AuthTokenModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.info(Utils.wrap(USER_DELETE_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    PASSWORD_BASE64, passwordBase64));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.info(Utils.wrap(USER_DELETE_FAIL,
                    USER_NOT_EXISTS, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    PASSWORD_BASE64, passwordBase64));
        } catch (UserInvalidCredentialsException ignored) {
            response.setStatus(false);
            response.setMessage(INVALID_CREDENTIALS);
            LOGGER.info(Utils.wrap(USER_DELETE_FAIL,
                    INVALID_CREDENTIALS, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    PASSWORD_BASE64, passwordBase64));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.info(Utils.wrap(USER_DELETE_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    USER_ID, userId,
                    AUTH_TOKEN, authToken,
                    PASSWORD_BASE64, passwordBase64,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }
}
