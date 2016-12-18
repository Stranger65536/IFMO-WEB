/**
 * Created by developer123 on 25.06.15.
 */
var AppDispatcher = require('../dispatcher/AppDispatcher');
var CalendarConstants = require('../constants/CalendarConstants');

var CalendarActions = {

    changeCalendar:function(month,year) {
        AppDispatcher.dispatch({
            actionType:CalendarConstants.CHANGE_MONTH_YEAR,
            month:month,
            year:year
        });
    },

    saveCalendars:function(calendar,doEmit) {
        AppDispatcher.dispatch({
            actionType:CalendarConstants.SAVE_CALENDARS,
            calendars:calendar,
            doEmit:doEmit
        });
    },

    clearSelection:function(currentSelect) {
        AppDispatcher.dispatch({
            actionType:CalendarConstants.CLEAR_SELECTION,
            selected:currentSelect
        });
    },

    pushEvents:function(events) {
        AppDispatcher.dispatch({
            actionType:CalendarConstants.PUSH_EVENTS,
            events:events
        });
    }

};

module.exports = CalendarActions;