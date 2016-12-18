package com.bitbucket.teamleave.leafcalendar.api.service;

import com.bitbucket.teamleave.leafcalendar.api.dao.AuthTokenDao;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.AuthTokenModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;
import com.bitbucket.teamleave.leafcalendar.api.model.AuthTokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * @author vladislav.trofimov@emc.com
 */
@Service("AuthTokenService")
public class AuthTokenServiceImpl implements AuthTokenService {
    @Autowired
    AuthTokenDao authTokenDao;

    @Override
    public String createTokenByUserId(final String userId) throws
            UserNotExistsException, UserModelException {
        final AuthTokenModel model = new AuthTokenModel();
        model.setUserId(userId);
        try {
            return authTokenDao.createToken(model);
        } catch (DataIntegrityViolationException e) {
            throw new UserNotExistsException(e);
        }
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public String getTokenIdByUserId(final String userId) throws
            UserNotExistsException, UserModelException {
        final AuthTokenModel model = new AuthTokenModel();
        model.setUserId(userId);
        try {
            return authTokenDao.getTokenByUserId(model).getTokenId();
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotExistsException(e);
        }
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public String getUserIdByTokenId(final String tokenId) throws
            AuthTokenModelException, UserNotExistsException {
        final AuthTokenModel model = new AuthTokenModel();
        model.setTokenId(tokenId);
        try {
            return authTokenDao.getTokenByTokenId(model).getUserId();
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotExistsException(e);
        }
    }

    @Override
    public void updateTokenByTokenId(final String tokenId) throws
            AuthTokenModelException, UserNotExistsException {
        final AuthTokenModel model = new AuthTokenModel();
        model.setTokenId(tokenId);
        try {
            authTokenDao.updateTokenByTokenId(model);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotExistsException(e);
        }
    }

    @Override
    public void updateTokenByUserId(final String userId) throws
            UserNotExistsException, UserModelException {
        final AuthTokenModel model = new AuthTokenModel();
        model.setUserId(userId);
        try {
            authTokenDao.updateTokenByUserId(model);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotExistsException(e);
        }
    }
}