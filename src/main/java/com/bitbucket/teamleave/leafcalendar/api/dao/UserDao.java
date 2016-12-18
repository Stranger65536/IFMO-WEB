package com.bitbucket.teamleave.leafcalendar.api.dao;

import com.bitbucket.teamleave.leafcalendar.api.model.UserModel;
import org.apache.commons.lang3.tuple.Pair;

import javax.sql.DataSource;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface UserDao {
    void setDataSource(final DataSource dataSource);

    Pair<String, String> createUser(final UserModel userModel);

    UserModel getUserById(final UserModel userModel);

    UserModel getUserByEmail(final UserModel userModel);

    UserModel getUserByValidationToken(final UserModel userModel);

    void updateUserById(final UserModel userModel);

    void deleteUserById(final UserModel userModel);
}