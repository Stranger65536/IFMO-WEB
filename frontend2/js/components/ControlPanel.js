/**
 * Created by developer123 on 22.06.15.
 */
var React = require('react');
var $ = require('jquery');
var ButtonGroup = require('react-bootstrap').ButtonGroup;
var Button = require('react-bootstrap').Button;
var CalendarStore=require('../stores/CalendarStore');
var CalendarActions=require('../actions/CalendarActions');

var ControlPanel = React.createClass({
    getInitialState: function () {
        var currentCalendar=CalendarStore.getCurrentCalendar();
        return {
            month:currentCalendar.month,
            year:currentCalendar.year
        };
    },

    prevMonth:function() {
        var currentCalendar=CalendarStore.getCurrentCalendar();
        CalendarActions.changeCalendar(currentCalendar.month-1,currentCalendar.year);
    },

    nextMonth:function() {
        var currentCalendar=CalendarStore.getCurrentCalendar();
        CalendarActions.changeCalendar(currentCalendar.month+1,currentCalendar.year);
    },

    goToToday:function() {
        var today=new Date();
        CalendarActions.changeCalendar(today.getMonth(),today.getFullYear());
    },

    componentDidMount:function() {
        CalendarStore.addChangeListener(this._onChange);
    },

    componentWillUnmount:function() {
        CalendarStore.removeChangeListener(this._onChange);
    },

    render: function () {
        var months=['January','February','March','April','May','June','July','August','September','October','November','December'];
        return (
            <div className="topLine row">
                <div className="leftSide">Calendar</div>
                <div className="rightSide">
                    <Button onClick={this.goToToday}>Today</Button>
                    <Button className="switch" onClick={this.prevMonth}>{"<"}</Button>
                    <div className="time">{months[this.state.month]+' '+this.state.year}</div>
                    <Button className="switch" onClick={this.nextMonth}>{">"}</Button>
                </div>
            </div>
        );
    },

    _onChange:function() {
        var currentCalendar=CalendarStore.getCurrentCalendar();
        this.setState({month:currentCalendar.month,year:currentCalendar.year});
    }

});

module.exports = ControlPanel;