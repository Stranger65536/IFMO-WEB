package com.bitbucket.teamleave.leafcalendar.api.dao;

import com.bitbucket.teamleave.leafcalendar.api.model.UserModel;
import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;

import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.Users.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@Repository("UserDao")
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicCharacter"})
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public Pair<String, String> createUser(final UserModel userModel) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(
                connection -> {
                    final String query = "INSERT INTO " + USERS_TABLE + " (" +
                            FIRST_NAME + ", " +
                            LAST_NAME + ", " +
                            EMAIL + ", " +
                            PASSWORD_HASH +
                            ") VALUES (?,?,?,?)";
                    PreparedStatement ps = connection.prepareStatement(query,
                            new String[]{USER_ID, EMAIL_VALIDATION_TOKEN});
                    DaoUtils.setString(1, ps, userModel.getFirstName());
                    DaoUtils.setString(2, ps, userModel.getLastName());
                    DaoUtils.setString(3, ps, userModel.getEmail());
                    DaoUtils.setInt(4, ps, userModel.getPasswordHash());
                    return ps;
                }, keyHolder);
        return Pair.of(Utils.rawToHex((byte[]) keyHolder.getKeys().get(USER_ID)),
                Utils.rawToHex((byte[]) keyHolder.getKeys().get(EMAIL_VALIDATION_TOKEN)));
    }

    @Override
    public UserModel getUserById(final UserModel userModel) {
        final String query = "SELECT * FROM " + USERS_TABLE + ' ' +
                "WHERE " + USER_ID + " = ?";
        return getJdbcTemplate().queryForObject(query, new Object[]{
                userModel.getUserId()
        }, new BeanPropertyRowMapper<>(UserModel.class));
    }

    @Override
    public UserModel getUserByEmail(final UserModel userModel) {
        final String query = "SELECT * FROM " + USERS_TABLE + ' ' +
                "WHERE " + EMAIL + " = ?";
        return getJdbcTemplate().queryForObject(query, new Object[]{
                userModel.getEmail()
        }, new BeanPropertyRowMapper<>(UserModel.class));
    }

    @Override
    public UserModel getUserByValidationToken(final UserModel userModel) {
        final String query = "SELECT * FROM " + USERS_TABLE + ' ' +
                "WHERE " + EMAIL_VALIDATION_TOKEN + " = ?";
        return getJdbcTemplate().queryForObject(query, new Object[]{
                userModel.getEmailValidationToken()
        }, new BeanPropertyRowMapper<>(UserModel.class));
    }

    @Override
    public void updateUserById(final UserModel userModel) {
        final String query = "UPDATE " + USERS_TABLE + ' ' +
                "SET " +
                FIRST_NAME + " = ?," +
                LAST_NAME + " = ?," +
                PASSWORD_HASH + " = ?," +
                EMAIL + " = ?," +
                EMAIL_VALIDATED + " = ?," +
                EMAIL_VALIDATION_TOKEN + " = ?" + ' ' +
                "WHERE " + USER_ID + " = ?";
        final int affectedRows = getJdbcTemplate().execute(query,
                (PreparedStatement ps) -> {
                    DaoUtils.setString(1, ps, userModel.getFirstName());
                    DaoUtils.setString(2, ps, userModel.getLastName());
                    DaoUtils.setInt(3, ps, userModel.getPasswordHash());
                    DaoUtils.setString(4, ps, userModel.getEmail());
                    DaoUtils.setBoolean(5, ps, userModel.isEmailValidated());
                    DaoUtils.setString(6, ps, userModel.getEmailValidationToken());
                    DaoUtils.setString(7, ps, userModel.getUserId());
                    return ps.executeUpdate();
                });
        if (affectedRows == 0) {
            throw new EmptyResultDataAccessException(0);
        }
    }

    @Override
    public void deleteUserById(final UserModel userModel) {
        final String query = "DELETE FROM " + USERS_TABLE + ' ' +
                "CASCADE WHERE " + USER_ID + " = ?";
        final int affectedRows = getJdbcTemplate().execute(query,
                (PreparedStatement ps) -> {
                    DaoUtils.setString(1, ps, userModel.getUserId());
                    return ps.executeUpdate();
                });
        if (affectedRows == 0) {
            throw new EmptyResultDataAccessException(0);
        }
    }
}