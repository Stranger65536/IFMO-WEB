/**
 * Created by developer123 on 23.06.15.
 */
var React = require('react');
var $ = require('jquery');
var ButtonGroup = require('react-bootstrap').ButtonGroup;
var Button = require('react-bootstrap').Button;
var AlertActions = require('../actions/AlertActions');
var ModalActions = require('../actions/ModalActions');
var CalendarActions = require('../actions/CalendarActions');
var CalendarStore = require('../stores/CalendarStore');
var apiurl = localStorage['api'] || "";

var CalendarItem=React.createClass({
    editCalendar:function() {
        ModalActions.editCalendar(this.props.el);
    },

    render:function() {
        return (
            <div className="item" onClick={this.editCalendar}>
                <div className="colored" style={{backgroundColor: '#' + (this.props.el.visible==true?this.props.color:'fff')}}></div>
                <span>{this.props.name}</span>
            </div>
        )
    }
});

var CList = React.createClass({
    getInitialState: function () {
        return {
            calendarsList: []
        };
    },

    createCalendar: function () {
        ModalActions.createCalendar();
    },

    createEvent: function () {
        ModalActions.createEvent();
    },

    loadCalendars:function() {
        var callist = [],self=this;
        sendRequest(apiurl + '/users/' + localStorage['userid'] + '/calendars/', "GET", [], JSON.parse(localStorage['defaultheaders']), function (answer) {
            if (!answer.status) AlertActions.showError(answer.message, "danger");
            else {
                var l=answer.data.id.length;
                $.each(answer.data.id, function (i, elem) {
                    sendRequest(apiurl + '/users/' + localStorage['userid'] + '/calendars/' + elem, "GET", [], JSON.parse(localStorage['defaultheaders']), function (answer) {
                        if (!answer.status) AlertActions.showError(answer.message, "danger");
                        else {
                            var cal = answer.data;
                            cal['id'] = elem;
                            callist.push(cal);
                            self.setState({calendarsList: callist},CalendarActions.saveCalendars(callist,(i == l-1)));
                        }
                    });
                });
            }
        },true);
    },

    componentDidMount: function () {
        this.loadCalendars();
        CalendarStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function() {
        CalendarStore.removeChangeListener(this._onChange);
    },

    render: function () {
        return (
            <div className="CList">
                <Button className="create" onClick={this.createCalendar}>Create</Button>
                <div className="calendList">
                    <div>My calendars</div>
                    <div className="list">
                    {this.state.calendarsList.map(function (elem, i) {
                        return (
                            <CalendarItem el={elem} key={i} name={elem.name} color={elem.color} />
                        )
                    })}
                    </div>
                </div>
            </div>
        );
    },

    _onChange: function() {
        console.log("HOW DID I GET HERE");
        //this.loadCalendars();
        var callist = [],self=this;
        sendRequest(apiurl + '/users/' + localStorage['userid'] + '/calendars/', "GET", [], JSON.parse(localStorage['defaultheaders']), function (answer) {
            if (!answer.status) AlertActions.showError(answer.message, "danger");
            else {
                var l=answer.data.id.length;
                $.each(answer.data.id, function (i, elem) {
                    sendRequest(apiurl + '/users/' + localStorage['userid'] + '/calendars/' + elem, "GET", [], JSON.parse(localStorage['defaultheaders']), function (answer) {
                        if (!answer.status) AlertActions.showError(answer.message, "danger");
                        else {
                            var cal = answer.data;
                            cal['id'] = elem;
                            callist.push(cal);
                            self.setState({calendarsList: callist});
                        }
                    });
                });
            }
        },true);
    }


});

module.exports = CList;