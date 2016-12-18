package com.bitbucket.teamleave.leafcalendar.api.service;

import com.bitbucket.teamleave.leafcalendar.api.dao.UserDao;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.InternalServerException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserAlreadyRegisteredException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserAlreadyValidatedException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserInvalidCredentialsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.AuthTokenModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.CalendarModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;
import com.bitbucket.teamleave.leafcalendar.api.mail.MailService;
import com.bitbucket.teamleave.leafcalendar.api.model.AuthTokenModel;
import com.bitbucket.teamleave.leafcalendar.api.model.UserModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceGetUserInfoModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceLoginModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.UserServiceRegistrationModel;
import com.bitbucket.teamleave.leafcalendar.api.transfer.users.request.UpdateUserInfoRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("OverlyCoupledClass")
@Service("UserService")
@Transactional(propagation = Propagation.REQUIRED,
        isolation = Isolation.SERIALIZABLE,
        rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    MailService mailService;
    @Autowired
    AuthTokenService authTokenService;
    @Autowired
    CalendarService calendarService;

    private static void checkPasswordEquals(
            final String authToken,
            final UserModel model,
            final UserModel fetchModel) throws
            UserInvalidCredentialsException {
        if (fetchModel.passwordDifference(model)) {
            throw new UserInvalidCredentialsException(authToken);
        }
    }

    private static void checkEmailNotValidated(
            final String cause,
            final UserModel model) throws
            UserAlreadyValidatedException {
        if (model.isEmailValidated()) {
            throw new UserAlreadyValidatedException(cause);
        }
    }

    @SuppressWarnings({"LawOfDemeter", "MagicCharacter"})
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class)
    public UserServiceRegistrationModel registerUser(
            final String firstName,
            final String lastName,
            final String email,
            final String password) throws
            UserModelException, UserAlreadyRegisteredException,
            InternalServerException {
        final UserModel model = new UserModel();
        model.populateWithRegistrationInfo(firstName, lastName, email, password);
        try {
            model.populateWithRegistrationResult(userDao.createUser(model));
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyRegisteredException(e);
        }
        try {
            final String authToken = authTokenService.createTokenByUserId(model.getUserId());
            calendarService.createDefaultCalendar(authToken, model.getUserId(),
                    model.getFirstName() + ' ' + model.getLastName());
            mailService.sendVerificationMail(model);
        } catch (UserNotExistsException | AuthTokenModelException e) {
            throw new InternalServerException(e);
        } catch (CalendarModelException e) {
            e.printStackTrace();
        }
        return new UserServiceRegistrationModel(model);
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public void validateUser(
            final String userId,
            final String validationToken) throws
            UserModelException, UserAlreadyValidatedException,
            UserNotExistsException {
        UserModel model = new UserModel();
        model.populateWithValidationInfo(userId, validationToken);
        try {
            model = fetchModelByValidationTokenAndCheckId(model, userId);
            checkEmailNotValidated(validationToken, model);
            model.validateEmail();
            //todo send mail
            userDao.updateUserById(model);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotExistsException(e);
        }
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public UserServiceLoginModel loginUser(
            final String email,
            final String password) throws
            UserModelException, UserNotExistsException,
            UserInvalidCredentialsException {
        final UserModel model = new UserModel();
        model.populateWithLoginInfo(email, password);
        final UserModel fetchModel;
        try {
            fetchModel = userDao.getUserByEmail(model);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotExistsException(e);
        }
        checkPasswordEquals(email, model, fetchModel);
        final String authToken = authTokenService.getTokenIdByUserId(fetchModel.getUserId());
        return new UserServiceLoginModel(fetchModel.getUserId(), authToken);
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public void updatePassword(
            final String userId,
            final String authToken,
            final String oldPassword,
            final String newPassword) throws
            UserNotExistsException, InternalServerException,
            UserModelException, AuthTokenModelException,
            UserInvalidCredentialsException {
        final UserModel model = new UserModel();
        model.populateWithPasswordInfo(userId, oldPassword);
        fetchModelByTokenIdAndCheckId(userId, authToken, model);
        final UserModel fetchModel = userDao.getUserById(model);
        checkPasswordEquals(authToken, model, fetchModel);
        fetchModel.setPassword(newPassword);
        try {
            userDao.updateUserById(fetchModel);
            authTokenService.updateTokenByUserId(userId);
            //todo send mail
        } catch (UserNotExistsException e) {
            throw new InternalServerException(e);
        }
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public void updateEmail(
            final String userId,
            final String authToken,
            final String password,
            final String newEmail) throws
            UserModelException, InternalServerException,
            UserInvalidCredentialsException, AuthTokenModelException,
            UserNotExistsException {
        final UserModel model = new UserModel();
        model.populateWithPasswordInfo(userId, password);
        fetchModelByTokenIdAndCheckId(userId, authToken, model);
        final UserModel fetchModel = userDao.getUserById(model);
        checkPasswordEquals(authToken, model, fetchModel);
        fetchModel.setEmail(newEmail);
        try {
            userDao.updateUserById(fetchModel);
            authTokenService.updateTokenByUserId(userId);
            //todo send mail
        } catch (UserNotExistsException e) {
            throw new InternalServerException(e);
        }
    }

    @Override
    public void sendValidationMail(
            final String userId,
            final String authToken) throws
            AuthTokenModelException,
            UserAlreadyValidatedException, UserNotExistsException,
            UserModelException {
        final UserModel model = new UserModel(userId);
        fetchModelByTokenIdAndCheckId(userId, authToken, model);
        final UserModel fetchModel = userDao.getUserById(model);
        checkEmailNotValidated(authToken, fetchModel);
        mailService.sendVerificationMail(fetchModel);
    }

    @Override
    public void updateAuthToken(
            final String userId,
            final String authToken) throws
            AuthTokenModelException,
            UserNotExistsException, UserModelException {
        final UserModel model = new UserModel(userId);
        fetchModelByTokenIdAndCheckId(userId, authToken, model);
        authTokenService.updateTokenByTokenId(authToken);
    }

    @Override
    public UserServiceGetUserInfoModel getUserInfo(
            final String userId,
            final String authToken) throws
            AuthTokenModelException, UserNotExistsException,
            UserModelException {
        final UserModel model = new UserModel(userId);
        fetchModelByTokenIdAndCheckId(userId, authToken, model);
        final UserModel fetchModel = userDao.getUserById(model);
        return new UserServiceGetUserInfoModel(fetchModel);
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public void updateUserInfo(
            final String userId,
            final String authToken,
            final UpdateUserInfoRequestBody request) throws
            AuthTokenModelException, UserNotExistsException,
            UserModelException {
        final UserModel model = new UserModel(userId);
        fetchModelByTokenIdAndCheckId(userId, authToken, model);
        final UserModel fetchModel = userDao.getUserById(model);
        fetchModel.populateWithUpdateInfo(request.getFirstName(), request.getLastName());
        userDao.updateUserById(fetchModel);
    }

    @Override
    public void deleteUser(
            final String userId,
            final String authToken,
            final String password) throws
            UserInvalidCredentialsException, AuthTokenModelException,
            UserNotExistsException, UserModelException {
        final UserModel model = new UserModel();
        model.populateWithPasswordInfo(userId, password);
        fetchModelByTokenIdAndCheckId(userId, authToken, model);
        final UserModel fetchModel = userDao.getUserById(model);
        checkPasswordEquals(authToken, model, fetchModel);
        userDao.deleteUserById(fetchModel);
    }

    @SuppressWarnings("LawOfDemeter")
    private UserModel fetchModelByValidationTokenAndCheckId(
            final UserModel model,
            final String userId) throws
            UserNotExistsException {
        final UserModel fetchModel = userDao.getUserByValidationToken(model);
        if (fetchModel.hasDifferentId(userId)) {
            throw new UserNotExistsException(userId);
        }
        return fetchModel;
    }

    @Override
    public void fetchModelByTokenIdAndCheckId(
            final String userId,
            final String authToken,
            final UserModel model) throws
            AuthTokenModelException, UserNotExistsException {
        final AuthTokenModel authTokenModel = new AuthTokenModel();
        authTokenModel.setTokenId(authToken);
        if (model.hasDifferentId(authTokenService.getUserIdByTokenId(authTokenModel.getTokenId()))) {
            throw new UserNotExistsException(userId);
        }
    }
}