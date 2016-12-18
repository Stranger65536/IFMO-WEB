/**
 * Created by developer123 on 25.06.15.
 */
var AppDispatcher = require('../dispatcher/AppDispatcher');
var ModalConstants = require('../constants/ModalConstants');

var ModalActions = {

    createCalendar:function() {
        AppDispatcher.dispatch({
            actionType:ModalConstants.CREATE_CALENDAR_SHOW,
            caption:"Create calendar"
        });
    },

    createEvent:function(date) {
        AppDispatcher.dispatch({
            actionType:ModalConstants.CREATE_EVENT_SHOW,
            caption:"Create event",
            date:date
        });
    },

    editEvent:function(event) {
        AppDispatcher.dispatch({
            actionType:ModalConstants.EDIT_EVENT_SHOW,
            caption:"Edit event",
            event:event
        });
    },

    editCalendar:function(additionalInfo) {
        AppDispatcher.dispatch({
            actionType:ModalConstants.EDIT_CALENDAR_SHOW,
            caption:"Edit calendar",
            additionalInfo:additionalInfo
        });
    }

};

module.exports = ModalActions;