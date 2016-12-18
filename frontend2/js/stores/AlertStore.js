/**
 * Created by developer123 on 13.02.15.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var EventEmitter = require('events').EventEmitter;
var AlertConstants = require('../constants/AlertConstants');
var assign = require('object-assign');

var CHANGE_EVENT = 'change';

var alert={};

function saveError(message,type) {
    alert={
        message:message,
        type:type,
        alertVisible:true
    }
}
function hideEror() {
    alert={
        alertVisible:false
    }
}

var AlertStore = assign({}, EventEmitter.prototype, {

    getCurrentInfo: function() {
        return alert;
    },

    emitChange: function() {
        this.emit(CHANGE_EVENT);
    },

    /**
     * @param {function} callback
     */
    addChangeListener: function(callback) {
        this.on(CHANGE_EVENT, callback);
    },

    /**
     * @param {function} callback
     */
    removeChangeListener: function(callback) {
        this.removeListener(CHANGE_EVENT, callback);
    }
});

// Register callback to handle all updates
AppDispatcher.register(function(action) {

    switch(action.actionType) {

        case AlertConstants.ALERT_SHOW_ERROR:
            if (action.message !== '' && action.message.trim()!=='') {
                saveError(action.message,action.type);
            }
            AlertStore.emitChange();
            break;
        case AlertConstants.ALERT_HIDE_ERROR:
            hideEror();
            AlertStore.emitChange();
            break;
        default:
        // no op
    }
});

module.exports = AlertStore;