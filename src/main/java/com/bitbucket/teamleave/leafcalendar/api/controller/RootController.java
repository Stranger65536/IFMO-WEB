package com.bitbucket.teamleave.leafcalendar.api.controller;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vladislav.trofimov@emc.com
 */
@RestController
@SuppressWarnings("HardcodedFileSeparator")
public class RootController {
    @RequestMapping(
            value = "/test",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String test() {
        final JSONObject result = new JSONObject().put("все", "заебца");
        return result.toString();
    }
}