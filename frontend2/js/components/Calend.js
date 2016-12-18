/**
 * Created by developer123 on 23.06.15.
 */
var React = require('react');
var $ = require('jquery');
var ButtonGroup = require('react-bootstrap').ButtonGroup;
var Button = require('react-bootstrap').Button;
var CalendarStore = require('../stores/CalendarStore');
var ContextMenuActions = require('../actions/ContextMenuActions');
var CalendarActions = require('../actions/CalendarActions');
var AlertActions = require('../actions/AlertActions');
var ModalActions = require('../actions/ModalActions');
var Events=[];

function SortByStartTime(a, b){
    var aVal = a.starttime;
    var bVal = b.starttime;
    return aVal-bVal;
}

function getEvents(date) {
    var res=[];
    if (CalendarStore.getCalendars().length && Events.length) {
        var split = date.split('.');
        var checkDate = new Date(split[2] + '-' + split[1] + '-' + split[0]+'T00:00:00+03:00');
        $.each(Events, function (i, elem) {
            var start = new Date(elem.start_time+(elem.start_time_tz||"+03:00"));
            var end = new Date(elem.end_time+(elem.end_time_tz||"+03:00"));
            if ((start.getTime()<checkDate.getTime() || start.getTime()<(checkDate.getTime()+1000*60*60*24)) && checkDate.getTime()<end.getTime()) {
                var time="";
                if (checkDate.getDate()==start.getDate()) time=('0'+start.getHours()).slice(-2)+":"+('0'+start.getMinutes()).slice(-2);
                var ev=elem;
                ev['color']='#'+ev['color'];
                ev['startdate']=start.getFullYear()+'-'+('0'+(start.getMonth()+1)).slice(-2)+'-'+('0'+start.getDate()).slice(-2);
                ev['starttime']=('0'+start.getHours()).slice(-2)+':'+('0'+start.getMinutes()).slice(-2);
                ev['enddate']=end.getFullYear()+'-'+('0'+(end.getMonth()+1)).slice(-2)+'-'+('0'+end.getDate()).slice(-2);
                ev['endtime']=('0'+end.getHours()).slice(-2)+':'+('0'+end.getMinutes()).slice(-2);
                if (elem.rec_period_id!==null) {
                    var rec = new Date(elem.rec_end_time + (elem.rec_end_time_tz || "+03:00"));
                    ev['recdate'] = rec.getFullYear() + '-' + ('0' + (rec.getMonth()+1)).slice(-2) + '-' + ('0' + rec.getDate()).slice(-2);
                    ev['rectime'] = ('0' + rec.getHours()).slice(-2) + ':' + ('0' + rec.getMinutes()).slice(-2);
                }
                res.push({
                    "name":elem.name,
                    "time":time,
                    "color":ev['color'],
                    "event":ev,
                    "starttime":start.getTime()
                });
            }
        });
    }
    res.sort(SortByStartTime);
    return res;
}

function genEvents(num) {
    var Events = [];
    for (var i = 0; i < num; i++)
        Events.push({
            "time": "11:00",
            "name": "longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglong veryveryverystupid name",
            "color": "rgb(" + Math.floor(Math.random() * 255) + "," + Math.floor(Math.random() * 255) + "," + Math.floor(Math.random() * 255) + ")"
        });
    return Events;
}

function gen_date(type, plus_date, plus_month, plus_year) {
    if (type == 'current') {
        var today = new Date();
        today.setDate(today.getDate() + plus_date);
        today.setMonth(today.getMonth() + plus_month);
        today.setFullYear(today.getFullYear() + plus_year);
    }
    else if (type == 'generate')
        var today = new Date(plus_year, plus_month, plus_date);
    else if (type == 'generate last month') {
        var today = new Date(plus_year, plus_month - 1, 0);
        today.setDate(today.getDate() - (plus_date - 1));
    } else if (type == 'generate next month') {
        var today = new Date(plus_year, plus_month + 1, plus_date);
    }
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();

    if (dd < 10) {
        dd = '0' + dd
    }

    if (mm < 10) {
        mm = '0' + mm
    }

    today = dd + '.' + mm + '.' + yyyy;
    return today;
}

function generatemonth(month, year) {
    var start = new Date(year, month, 1).getDay(),
        days = new Date(year, month + 1, 0).getDate(),
        result = [],
        newtable = [],
        offset = 0,
        print = 0,
        today = gen_date('current', 0, 0, 0);
    if (!start) start = 7;
    var weeks = Math.ceil((days + start - 1) / 7);

    for (var j = 0; j < weeks; j++) {
        newtable = [];
        if (start > 1) {
            for (var z = 1; z < start; z++) {
                var label = gen_date('generate last month', (start - z), month, year);
                var splitted = label.split('.');
                newtable.push({flag: "notCurrent", date: label, print: splitted[0]});
            }
            start = start - 1;
        } else {
            start = 0;
        }
        var init_value = j * 7 + 1 - offset;
        var end_value = (j * 7 + (7 - start));
        if (end_value - init_value > 6) end_value = init_value + 6;
        for (var i = init_value; i <= end_value && i <= (days); i++) {
            print = i < 10 ? '0' + i : i;
            var nowadate = gen_date('generate', i, month, year);
            if (nowadate == today)
                newtable.push({flag: "Today", date: nowadate, print: print});
            else
                newtable.push({flag: "Current", date: nowadate, print: print});
        }
        if (start) {
            offset = start;
            start = 0;
        }
        if ((weeks - j) < 2) {
            var rows = Math.ceil(weeks);
            var end = (rows * 7) - days - offset;
            for (var z = 1; z <= end; z++) {
                var label = gen_date('generate next month', z, month, year);
                var splitted = label.split('.');
                newtable.push({flag: "notCurrent", date: label, print: splitted[0]});
            }
        }
        result.push(newtable);
    }
    return result;
}

function formrange(month,year) {
    month++;
    var days = new Date(year, month, 0).getDate();
    return {"start":year+'-'+('0'+month).slice(-2)+'-01T00:00:00+03:00',"end":year+'-'+('0'+month).slice(-2)+'-'+('0'+days).slice(-2)+'T23:59:59+03:00'};
}


var EventWidth = 0;

var CalendarEvent = React.createClass({
    getInitialState:function() {
        return {
            display:"none"
        }
    },

    componentDidMount: function() {
        this.setState({display:"block"});
    },

    editEvent:function(e) {
        e.preventDefault();
        e.stopPropagation();
        ModalActions.editEvent(this.props.event);
    },

    render: function () {
        return (
            <div className="calendarEvent" style={{borderLeft: "solid 2px " + this.props.color,display:this.state.display}} onClick={this.editEvent}>
                <div className="eventInner">
                    <span className="time">{this.props.time}</span>
                    <span className="description">{this.props.name}</span>
                </div>
            </div>
        )
    }
});

var DateElement = React.createClass({

    getInitialState: function () {
        return {
            selected: false
        }
    },

    showMenu: function (e) {
        var evt = window.event || e;
        evt.preventDefault();
        if (this.props.classify == "Current") {
            var top = evt.pageY,
                left = evt.pageX,
                offsetx = 120,
                offsety = 80;
            if (top + offsety > $(document).height()) top -= Math.abs(($(document).height()) - (top + offsety));
            if (left + offsetx > $(document).width()) left -= Math.abs(($(document).width()) - (left + offsetx));
            ContextMenuActions.showMenu(left, top, this.props);
            this.setState({selected: true}, CalendarActions.clearSelection(this.props.dataLabel));
        }
    },

    createEvent:function() {
        ModalActions.createEvent(this.props);
    },

    render: function () {
        var events = this.props.events;
        return (
            <td data-label={this.props.dataLabel} className={this.props.classify + " " + (this.props.selected ? "selected" : "")} onContextMenu={this.showMenu}>
                <div className="inner" onClick={this.createEvent}>
                    <div className="labelOverlay">
                        <span className="label">{this.props.print}</span>
                    </div>
                {events?events.map(function (elem, i) {
                    return (
                        <CalendarEvent time={elem.time} name={elem.name} color={elem.color} event={elem.event} key={i} marg={i * 20}/>
                    )
                }):""}
                </div>
            </td>
        )
    }
});

var Calend = React.createClass({
    getInitialState: function () {
        var today = new Date();
        return {
            currentMonth: today.getMonth(),
            currentYear: today.getFullYear(),
            list: generatemonth(today.getMonth(), today.getFullYear()),
            selected: "",
            events: []
        };
    },

    componentDidMount: function () {
        var self = this, list = this.state.list, docheight = $('body').height(), calend = $('.Calend tbody'), caltop = calend.offset().top, datewidth = calend.width() / 7;
        calend.css('height', (docheight - caltop) + 'px').find('tr').find('td').css('height', Math.floor((docheight - caltop) / list.length - 2) + 'px').css('max-height', datewidth + 'px').find('div.inner').css('height', Math.floor((docheight - caltop) / list.length - 2) + 'px');
        EventWidth = Math.floor(calend.width() / 7);
        calend.find('.calendarEvent').css('width', EventWidth + 'px').fadeIn("slow");
        $(window).resize(function () {
            var list = self.state.list, docheight = $('body').height(), calend = $('.Calend tbody'), caltop = calend.offset().top, datewidth = calend.width() / 7;
            calend.css('height', (docheight - caltop) + 'px').find('tr').find('td').css('height', Math.floor((docheight - caltop) / list.length - 2) + 'px').css('max-height', datewidth + 'px').find('div.inner').css('height', Math.floor((docheight - caltop) / list.length - 2) + 'px');
            EventWidth = Math.floor(calend.width() / 7);
            calend.find('.calendarEvent').css('width', EventWidth + 'px');
        });
        CalendarStore.addCListListener(this._onChange);
        CalendarStore.addChangeListener(this._onChange);
        CalendarStore.addSelectListener(this._onSelect);
        CalendarStore.addAddListener(this._onAdd);
    },

    componentWillUnmount: function () {
        CalendarStore.removeChangeListener(this._onChange);
        CalendarStore.removeCListListener(this._onChange);
        CalendarStore.removeSelectListener(this._onSelect);
        CalendarStore.removeAddListener(this._onAdd);
    },

    render: function () {
        var selectId = this.state.selected;
        return (
            <table className="Calend">
                <thead>
                    <tr>
                        <th>Mon</th>
                        <th>Tue</th>
                        <th>Wed</th>
                        <th>Thu</th>
                        <th>Fri</th>
                        <th>Sat</th>
                        <th>Sun</th>
                    </tr>
                </thead>
                <tbody>
                {this.state.list.map(function (elem, i) {
                    return (
                        <tr key={i}>
                            {elem.map(function (el2, i) {
                                return (
                                    <DateElement dataLabel={el2.date} classify={el2.flag} selected={selectId == el2.date} print={el2.print} events={getEvents(el2.date)} key={i} />
                                )
                            })}
                        </tr>
                    )
                })}
                </tbody>
            </table>
        );
    },

    loadEvents: function (range) {
        var self = this;
        console.log(CalendarStore.getCalendars());
        $.each(CalendarStore.getCalendars(), function (i, elem) {
            if (elem.visible) {
                var urlget = '/users/' + localStorage['userid'] + '/calendars/' + elem.id + '/events/?range_start=' + encodeURIComponent(range.start) + '&range_end=' + encodeURIComponent(range.end) + '&request_tz=' + encodeURIComponent('+03:00');
                sendRequest(apiurl + urlget, "GET", [], JSON.parse(localStorage['defaultheaders']), function (answer) {
                    if (!answer.status) AlertActions.showError(answer.message, "danger");
                    else {
                        var eventsIds = answer.data.events;
                        if (eventsIds.length)
                            $.each(eventsIds, function (j, elem2) {
                                var id = (elem2.rel == "self") ? elem2.id : elem2.rel;
                                var start = elem2.start_time;
                                var end = elem2.end_time;
                                sendRequest(apiurl + "/users/" + localStorage['userid'] + '/calendars/' + elem.id + '/events/' + id, "GET", [], JSON.parse(localStorage['defaultheaders']), function (answer2) {
                                    if (!answer2.status) AlertActions.showError(answer2.message, "danger");
                                    else {
                                        var event = answer2.data;
                                        event.start_time = start;
                                        event.end_time = end;
                                        event.calendarid = elem.id;
                                        CalendarActions.pushEvents(event);
                                    }
                                });
                            })
                    }
                });
            }
        });
    },

    _onAdd: function () {
        this.setState({
            events: CalendarStore.getEvents()
        },function() {
            Events=CalendarStore.getEvents();

        });
    },

    _onChange: function () {
        var currentCalendar = CalendarStore.getCurrentCalendar();
        this.setState({
            currentMonth: currentCalendar.month,
            currentYear: currentCalendar.year,
            list: generatemonth(currentCalendar.month, currentCalendar.year)
        }, function () {
            var list = this.state.list, docheight = $('body').height(), calend = $('.Calend tbody'), caltop = calend.offset().top, datewidth = calend.width() / 7;
            calend.css('height', (docheight - caltop) + 'px').find('tr').find('td').css('height', Math.floor((docheight - caltop) / list.length - 2) + 'px').css('max-height', datewidth + 'px').find('div.inner').css('height', Math.floor((docheight - caltop) / list.length - 2) + 'px');
            CalendarStore.clearEvents();
            this.loadEvents(formrange(currentCalendar.month,currentCalendar.year));
        });
    },

    _onSelect: function () {
        this.setState({selected: CalendarStore.getSelect()});
    }

});

module.exports = Calend;