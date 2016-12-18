/**
 * Created by developer123 on 12.02.15.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var EventEmitter = require('events').EventEmitter;
var LoginFormConstants = require('../constants/LoginFormConstants');
var assign = require('object-assign');

var CHANGE_EVENT = 'change';

var _fields = {};

var validationRules = {
    'isValue': function (value) {
        return value !== '';
    },
    'isEmail': function (value) {
        return value.match(/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i);
    },
    'isTrue': function (value) {
        return value === true;
    },
    'isNumeric': function (value) {
        return value.match(/^-?[0-9]+$/);
    },
    'isAlpha': function (value) {
        return value.match(/^[a-zA-Z]+$/);
    },
    'isCName':function(value) {
        return value.match(/^.{1,255}$/);
    },
    'isCDescr':function(value) {
        return value.match(/^.{0,1024}$/);
    },
    'isLogin': function (value) {
        return value.match(/^[a-zA-Z0-9]{6,50}$/);
    },
    'Login': function (value) {
        return value.match(/^[a-zA-Z0-9]+$/);
    },
    'Password': function (value) {
        return value.match(/^[a-zA-Z0-9.,:! ]{8,}$/);
    },
    'isName':function(value) {
        return value.match(/^[a-zA-Z]{1,32}$/);
    },
    isLength: function (value, min, max) {
        if (max !== undefined) {
            return value.length >= min && value.length <= max;
        }
        return value.length >= min;
    },
    equals: function (value, eq) {
        return value == eq;
    }
};

/**
 * Save field info
 * @param name
 * @param value field value
 * @param checkType what rule should apply
 */
function fillField(name, value, checkType) {
    console.log("fillField");
    if (checkType != 'RePass')
        _fields[name] = {
            value: value,
            valid: value==""?"":(validationRules[checkType](value) ? "success" : "error")
        };
    else
        _fields[name] = {
            value: value,
            valid: value==""?"":(validationRules['equals'](value, _fields['password'].value)?"success":"error")
        };
    console.log(_fields);
}

var LoginFormStore = assign({}, EventEmitter.prototype, {

    registerInputs: function (names) {
        console.log(names);
        _fields={};
        names.forEach(function (name) {
            _fields[name] = {
                value: "",
                valid: ""
            }
        });
        console.log(_fields);
    },

    isValid: function (name) {
        console.log(name);
        return _fields[name].valid;
    },

    areAllValid: function () {
        if (!_fields) return false;
        for (var name in _fields)
            if (_fields[name].valid==""||_fields[name].valid=="error") return false;
        return true;
    },

    FormJSONed: function () {
        var response = {};
        for (var name in _fields)
            response[name] = _fields[name].value;
        return response;
    },


    emitChange: function () {
        this.emit(CHANGE_EVENT);
    },

    /**
     * @param {function} callback
     */
    addChangeListener: function (callback) {
        this.on(CHANGE_EVENT, callback);
    },

    /**
     * @param {function} callback
     */
    removeChangeListener: function (callback) {
        this.removeListener(CHANGE_EVENT, callback);
    }
});

AppDispatcher.register(function (action) {

    switch (action.actionType) {

        case LoginFormConstants.LOGIN_FORM_INPUT_CHANGE:
            fillField(action.name, action.value, action.checkType);
            LoginFormStore.emitChange();
            break;

        case LoginFormConstants.LOGIN_FORM_CHECK_ALL:
            LoginFormStore.emitChange();
            break;

        default:
        // no op
    }
});

module.exports = LoginFormStore;