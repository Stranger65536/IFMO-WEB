/**
 * Created by developer123 on 25.06.15.
 */

var AppDispatcher = require('../dispatcher/AppDispatcher');
var EventEmitter = require('events').EventEmitter;
var ModalConstants = require('../constants/ModalConstants');
var assign = require('object-assign');

var CHANGE_EVENT = 'change';

var modal={};

function saveInner(caption,type,additionalInfo) {
    modal={
        caption:caption,
        type:type,
        additionalInfo:additionalInfo
    }
}

var ModalStore = assign({}, EventEmitter.prototype, {

    getCurrentInfo: function() {
        return modal;
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
    var caption,linkedFunction;

    switch(action.actionType) {

        case ModalConstants.CREATE_CALENDAR_SHOW:
            caption=action.caption.trim();
            if (caption !== '') {
                saveInner(caption,"NEW_CALENDAR",null);
            }
            ModalStore.emitChange();
            break;

        case ModalConstants.EDIT_CALENDAR_SHOW:
            caption=action.caption.trim();
            if (caption !== '') {
                saveInner(caption,"EDIT_CALENDAR",action.additionalInfo);
            }
            ModalStore.emitChange();
            break;

        case ModalConstants.CREATE_EVENT_SHOW:
            caption=action.caption.trim();
            if (caption !== '') {
                saveInner(caption,"CREATE_EVENT",action.date);
            }
            ModalStore.emitChange();
            break;

        case ModalConstants.EDIT_EVENT_SHOW:
            caption=action.caption.trim();
            if (caption !== '') {
                saveInner(caption,"EDIT_EVENT",action.event);
            }
            ModalStore.emitChange();
            break;
        default:
        // no op
    }
});

module.exports = ModalStore;