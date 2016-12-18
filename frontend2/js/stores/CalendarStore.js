/**
 * Created by developer123 on 25.06.15.
 */
var React=require('react');
var AppDispatcher = require('../dispatcher/AppDispatcher');
var EventEmitter = require('events').EventEmitter;
var CalendarConstants = require('../constants/CalendarConstants');
var assign = require('object-assign');
var CHANGE_EVENT = 'change';
var SELECT_EVENT = 'select';
var ADD_EVENT = 'add';
var CLIST_EVENT = 'load';

var today=new Date(),currentCalendar={
    month:today.getMonth(),
    year:today.getFullYear()
},_calendars=[],_events=[],_events2=[],selected="";

function saveCalendars(calendars) {
    _events=[];
    _calendars=calendars;
}

function pushEvents(event) {
    _events.push(event);
    _events2[event.id]={
        "starttime":event.starttime,
        "event":event
    };
}

function setMonthYear(month,year) {
    _events=[];
    if (month>11) {
        month=0;
        year++;
    }
    if (month<0) {
        year--;
        month=11;
    }
    currentCalendar={
        month:month,
        year:year
    };
}

function saveSelection(select) {
    selected=select;
}

var CalendarStore = assign({}, EventEmitter.prototype, {

    getCurrentCalendar: function() {
        return currentCalendar;
    },

    getCalendars: function() {
        return _calendars;
    },

    getSelect: function() {
        return selected;
    },

    getEvents:function() {
       return _events;
    },

    emitCList: function() {
        this.emit(CLIST_EVENT);
    },

    /**
     * @param {function} callback
     */
    addCListListener: function(callback) {
        this.on(CLIST_EVENT, callback);
    },

    /**
     * @param {function} callback
     */
    removeCListListener: function(callback) {
        this.removeListener(CLIST_EVENT, callback);
    },

    emitAdd: function() {
        this.emit(ADD_EVENT);
    },

    /**
     * @param {function} callback
     */
    addAddListener: function(callback) {
        this.on(ADD_EVENT, callback);
    },

    /**
     * @param {function} callback
     */
    removeAddListener: function(callback) {
        this.removeListener(ADD_EVENT, callback);
    },

    emitSelect: function() {
        this.emit(SELECT_EVENT);
    },

    /**
     * @param {function} callback
     */
    addSelectListener: function(callback) {
        this.on(SELECT_EVENT, callback);
    },

    /**
     * @param {function} callback
     */
    removeSelectListener: function(callback) {
        this.removeListener(SELECT_EVENT, callback);
    },

    emitChange: function() {
        this.emit(CHANGE_EVENT);
    },

    clearEvents:function() {
        _events=[];
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

        case CalendarConstants.CHANGE_MONTH_YEAR:
            setMonthYear(action.month,action.year);
            CalendarStore.emitChange();
            break;

        case CalendarConstants.SAVE_CALENDARS:
            saveCalendars(action.calendars);
            if (action.doEmit)
                CalendarStore.emitCList();
            break;

        case CalendarConstants.CLEAR_SELECTION:
            saveSelection(action.selected);
            CalendarStore.emitSelect();
            break;

        case CalendarConstants.PUSH_EVENTS:
            pushEvents(action.events);
            CalendarStore.emitAdd();
            break;

        default:
        // no op
    }
});

module.exports = CalendarStore;