package com.bitbucket.teamleave.leafcalendar.api.utils;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.ModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;
import javafx.util.Pair;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.INVALID_FIELD_FORMAT;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.BASE64_DELIMITER;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.NULL;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"MagicNumber", "ReturnOfNull", "MagicCharacter"})
public class Utils {
    public static final boolean NOT_DELETED = false;
    public static final boolean DELETED = true;
    private static final String ENCODING = "UTF-8";
    private static final int HEX_COLOR_LENGTH = 6;
    private static final String HEXES = "0123456789ABCDEF";

    public static String rawToHex(final byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(raw.length * 2);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt(b & 0x0F));
        }
        return hex.toString();
    }

    @SuppressWarnings("UnsecureRandomNumberGeneration")
    public static String getRandomHexColor() {
        final Random r = new Random();
        final StringBuilder sb = new StringBuilder(HEX_COLOR_LENGTH);
        while (sb.length() < HEX_COLOR_LENGTH) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, HEX_COLOR_LENGTH);
    }

    public static Pair<String, String> decodeEmailPassword(final String authBase64) throws
            UserModelException {
        if (authBase64 == null) {
            throw new UserModelException(AUTH_BASE64, NULL);
        }
        final String email;
        final String password;
        try {
            final String decodedString = new String(Base64.decodeBase64(authBase64), ENCODING);
            final int delimiterPos = decodedString.indexOf(BASE64_DELIMITER);
            if (delimiterPos == -1 || delimiterPos == decodedString.length() - 1) {
                throw new UserModelException(AUTH_BASE64, decodedString);
            }
            email = decodedString.substring(0, delimiterPos).trim().toLowerCase(Locale.US);
            password = decodedString.substring(delimiterPos + 1);
        } catch (UnsupportedEncodingException ignored) {
            throw new UserModelException(AUTH_BASE64, authBase64);
        }
        return new Pair<>(email, password);
    }

    public static String decodeEmail(final String emailBase64) throws
            UserModelException {
        if (emailBase64 == null) {
            throw new UserModelException(EMAIL_BASE64, NULL);
        }
        try {
            return new String(Base64.decodeBase64(emailBase64), ENCODING);
        } catch (UnsupportedEncodingException ignored) {
            throw new UserModelException(AUTH_BASE64, emailBase64);
        }
    }

    public static String decodePassword(final String passwordBase64) throws
            UserModelException {
        if (passwordBase64 == null) {
            throw new UserModelException(PASSWORD_BASE64, NULL);
        }
        try {
            return new String(Base64.decodeBase64(passwordBase64), ENCODING);
        } catch (UnsupportedEncodingException ignored) {
            throw new UserModelException(AUTH_BASE64, passwordBase64);
        }
    }

    public static String wrap(final String message,
                              final Object... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("non-paired argument!");
        }
        final int averageStringLength = 10;
        final StringBuilder logMessage =
                new StringBuilder(message.length() + args.length * averageStringLength);
        logMessage.append(message).append(' ');
        for (int i = 0, j = 1; j < args.length; i += 2, j += 2) {
            final Object field = args[i];
            final Object value = args[j];
            logMessage.append("{\"").append(field).append("\": ");
            logMessage.append('"').append(value).append("\"} ");
        }
        return logMessage.toString();
    }

    public static String prepareInvalidFieldMessage(final ModelException e) {
        return INVALID_FIELD_FORMAT + ": " +
                '"' + e.getInvalidField() + '"' + ' ' +
                '"' + e.getInvalidValue() + '"';
    }

    public static java.sql.Date getDate(final Date date) {
        return date == null ? null : new java.sql.Date(date.getTime());
    }

    public static ZoneOffset getOffset(final String offset) {
        return offset == null ? ZoneOffset.UTC : ZoneOffset.of(offset);
    }

    /**
     * Represents specified timestamp as {@code ZonedDateTime} object with UTC timezone.
     *
     * @param timestamp Not null specified timestamp object
     * @return {@code ZonedDateTime} with initial date and time and concatenated UTC timezone.
     */
    public static ZonedDateTime representAsUTCZoned(final Timestamp timestamp) {
        return ZonedDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.UTC);
    }

    /**
     * Represents specified timestamp as {@code ZonedDateTime} object with specified timezone.
     *
     * @param timestamp Not null specified timestamp
     * @param offset    Not null specified time zone
     * @return {@code ZonedDateTime} with initial date and time and concatenated specified timezone.
     */
    public static ZonedDateTime representAsZoned(
            final Timestamp timestamp,
            final ZoneOffset offset) {
        return ZonedDateTime.of(timestamp.toLocalDateTime(), offset);
    }

    /**
     * Converts specified timestamp to {@code ZonedDateTime} object with shifting to specified
     * timezone (specified timestamp interpreted as UTC timezone before converting).
     *
     * @param timestamp Not null specified timestamp
     * @param offset    Not null specified time zone
     * @return {@code ZonedDateTime} with converted date and time and concatenated specified timezone.
     */
    public static ZonedDateTime convertToZoned(
            final Timestamp timestamp,
            final ZoneOffset offset) {
        return ZonedDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.UTC)
                .withZoneSameInstant(offset);
    }

    /**
     * Converts specified {@code ZonedDateTime} string to timestamp with shifting to UTC timezone
     *
     * @param time Not null valid {@code ZonedDateTime} string
     * @return {@code Timestamp} with converted date and time.
     */
    public static Timestamp convertToUTCTimestamp(final CharSequence time) {
        return Timestamp.valueOf(ZonedDateTime.parse(time)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime());
    }
}