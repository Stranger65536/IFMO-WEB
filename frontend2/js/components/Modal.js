/**
 * Created by developer123 on 25.06.15.
 */

var React = require('react');
var ValidInput = require('./ValidInput');
var Modal = require('react-bootstrap').Modal;
var Button = require('react-bootstrap').Button;
var Input = require('react-bootstrap').Input;
var Table = require('react-bootstrap').Table;
var ModalStore = require('../stores/ModalStore');
var AlertStore = require('../stores/AlertStore');
var AlertActions = require('../actions/AlertActions');
var ModalActions = require('../actions/ModalActions');
var LoginFormStore=require('../stores/LoginFormStore');
var CalendarStore=require('../stores/CalendarStore');
var CalendarActions=require('../actions/CalendarActions');
var $ = require('jquery');

function getModalData() {
    return {
        currentInfo: ModalStore.getCurrentInfo()
    };
}

function gentoday(initdate) {
    var reformat=initdate.split('.');
    return reformat[2]+'-'+reformat[1]+'-'+reformat[0];
}

function genISO(date,time,timezone) {
    return date+'T'+time+timezone;
}

function JSONForm(id) {
    var formJSON={};
    $('form#'+id).find('input').each(function(i,elem){
        if ($(elem).attr('name'))
        formJSON[$(elem).attr('name')]=$(elem).val();
    });
    $('form#'+id).find('select').each(function(i,elem){
        if ($(elem).attr('name'))
        formJSON[$(elem).attr('name')]=$(elem).val();
    });
    return formJSON;
}

var CustomModalTrigger = React.createClass({
    getInitialState: function () {
        return {
            ismodalOpen: false,
            calendars:[]
        }
    },

    handleToggle: function () {
        this.setState({
            isModalOpen: !this.state.isModalOpen
        }, null);
    },

    componentDidMount: function () {
        ModalStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function () {
        ModalStore.removeChangeListener(this._onChange);
    },

    handleSubmit: function () {
        var req={},self=this;
        switch (this.state.currentInfo.type) {
            case 'NEW_CALENDAR':
                req=JSONForm('new_calendar');
                req['user_id']=localStorage['userid'];
                req['color']=req['color'].substr(-6);
                sendRequest(apiurl+'/users/'+localStorage['userid']+'/calendars/',"POST",JSON.stringify(req),JSON.parse(localStorage['defaultheaders']),function(answer){
                    if (!answer.status) AlertActions.showError(answer.message,"danger");
                    else self.setState({isModalOpen:false});
                });
                break;
            case 'EDIT_CALENDAR':
                req=JSONForm('edit_calendar');
                req['color']=req['color'].substr(-6);
                sendRequest(apiurl+'/users/'+localStorage['userid']+'/calendars/'+this.state.currentInfo.additionalInfo.id+'/update/',"POST",JSON.stringify(req),JSON.parse(localStorage['defaultheaders']),function(answer){
                    if (!answer.status) AlertActions.showError(answer.message,"danger");
                    else self.setState({isModalOpen:false});
                });
                break;
            case 'CREATE_EVENT':
                req=JSONForm('create_event');
                req['start_time']=genISO(req["datetime[start][0]"],req["datetime[start][1]"],req["datetime[start][2]"]);
                req['start_time_tz']=req["datetime[start][2]"];
                req['end_time']=genISO(req["datetime[end][0]"],req["datetime[end][1]"],req["datetime[end][2]"]);
                req['end_time_tz']=req["datetime[end][2]"];
                if (req['rec_period_id']!=="null") {
                    req['rec_end_time'] = genISO(req["datetime[rec][0]"], req["datetime[rec][1]"], req["datetime[rec][2]"]);
                    req['rec_end_time_tz'] = req["datetime[rec][2]"];
                } else {
                    req['rec_end_time']="null";
                    req['rec_end_time_tz']="null";
                    req['rec_period']="null";
                }
                delete req['datetime[start][0]'];
                delete req['datetime[start][1]'];
                delete req['datetime[start][2]'];
                delete req['datetime[end][0]'];
                delete req['datetime[end][1]'];
                delete req['datetime[end][2]'];
                delete req['datetime[rec][0]'];
                delete req['datetime[rec][1]'];
                delete req['datetime[rec][2]'];
                var calendarid=req['calendar_id'];
                delete req['calendar_id'];
                req['color']=req['color'].substr(-6);
                sendRequest(apiurl+"/users/"+localStorage['userid']+"/calendars/"+calendarid+"/events/","POST",JSON.stringify(req),JSON.parse(localStorage['defaultheaders']),function(answer){
                    if (!answer.status) AlertActions.showError(answer.message,"danger");
                    else {
                        self.setState({isModalOpen:false});
                        AlertActions.showError("Event \""+req['name']+"\" is successfully created","success");
                        console.log(answer);
                    }
                });
                break;
            case 'EDIT_EVENT':
                req=JSONForm('edit_event');
                req['start_time']=genISO(req["datetime[start][0]"],req["datetime[start][1]"],req["datetime[start][2]"]);
                req['start_time_tz']=req["datetime[start][2]"];
                req['end_time']=genISO(req["datetime[end][0]"],req["datetime[end][1]"],req["datetime[end][2]"]);
                req['end_time_tz']=req["datetime[end][2]"];
                if (req['rec_period_id']!=="null") {
                    req['rec_end_time'] = genISO(req["datetime[rec][0]"], req["datetime[rec][1]"], req["datetime[rec][2]"]);
                    req['rec_end_time_tz'] = req["datetime[rec][2]"];
                } else {
                    req['rec_end_time']=null;
                    req['rec_end_time_tz']=null;
                    req['rec_period']=null;
                }
                delete req['datetime[start][0]'];
                delete req['datetime[start][1]'];
                delete req['datetime[start][2]'];
                delete req['datetime[end][0]'];
                delete req['datetime[end][1]'];
                delete req['datetime[end][2]'];
                delete req['datetime[rec][0]'];
                delete req['datetime[rec][1]'];
                delete req['datetime[rec][2]'];
                var calendarid=req['calendar_id'];
                delete req['calendar_id'];
                req['color']=req['color'].substr(-6);
                sendRequest(apiurl+"/users/"+localStorage['userid']+"/calendars/"+calendarid+"/events/"+this.state.currentInfo.additionalInfo.id+'/update/',"POST",JSON.stringify(req),JSON.parse(localStorage['defaultheaders']),function(answer){
                    if (!answer.status) AlertActions.showError(answer.message,"danger");
                    else {
                        self.setState({isModalOpen:false});
                        AlertActions.showError("Event \""+req['name']+"\" is successfully updated","success");
                        console.log(answer);
                    }
                });
                break;
            default:
                break;
        }
        CalendarStore.clearEvents();
        CalendarActions.changeCalendar(CalendarStore.getCurrentCalendar().month,CalendarStore.getCurrentCalendar().year);
    },

    deleteSubmit:function() {
        var self=this;
        switch (this.state.currentInfo.type) {
            case 'EDIT_CALENDAR':
                sendRequest(apiurl+'/users/'+localStorage['userid']+'/calendars/'+this.state.currentInfo.additionalInfo.id,"DELETE",[],JSON.parse(localStorage['defaultheaders']),function(answer){
                    if (!answer.status) AlertActions.showError(answer.message,"danger");
                    else self.setState({isModalOpen:false});
                });
                break;
            case 'EDIT_EVENT':
                sendRequest(apiurl+'/users/'+localStorage['userid']+'/calendars/'+this.state.currentInfo.additionalInfo.calendarid+'/events/'+this.state.currentInfo.additionalInfo.id,"DELETE",[],JSON.parse(localStorage['defaultheaders']),function(answer){
                    if (!answer.status) AlertActions.showError(answer.message,"danger");
                    else self.setState({isModalOpen:false});
                });
                break;
            default:
                break;
        }
        CalendarStore.clearEvents();
        CalendarActions.changeCalendar(CalendarStore.getCurrentCalendar().month,CalendarStore.getCurrentCalendar().year);
    },

    render: function () {
        if (!this.state.isModalOpen) {
            return <span/>;
        }
        var calendars=this.state.calendars;
        return (
            <Modal title={this.state.currentInfo.caption} className="Modalwindow" onRequestHide={this.handleToggle}>
                <div className="modal-body">
                {this.state.currentInfo.type == "NEW_CALENDAR" ?
                    (
                        <form id="new_calendar">
                            <div className="row">
                                <ValidInput name="name" placeholder="Calendar name" checkType="isCName" autoFocus required/>
                            </div>
                            <div className="row">
                                <ValidInput name="description" placeholder="Calendar description" checkType="isCDescr" />
                            </div>
                            <div className="row">
                                <Input name="color" className="preview" type="text" name="color" id="color" placeholder="Color" defaultValue="#000000" style={{backgroundColor: "#000000"}} readOnly />
                                <div className="col-container" style={{display: "none"}}>
                                    <div className="colorpicker" style={{display: "none"}}>
                                        <canvas id="picker" var="1" width="300" height="300"></canvas>
                                        <div className="controls">
                                            <div>
                                                <label>R</label>
                                                <input type="text" id="rVal" />
                                            </div>
                                            <div>
                                                <label>G</label>
                                                <input type="text" id="gVal" />
                                            </div>
                                            <div>
                                                <label>B</label>
                                                <input type="text" id="bVal" />
                                            </div>
                                            <div>
                                                <label>RGB</label>
                                                <input type="text" id="rgbVal" />
                                            </div>
                                            <div>
                                                <label>HEX</label>
                                                <input type="text" id="hexVal" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <Input name="visible" type="select">
                                    <option value="true">Visible</option>
                                    <option value="false">Invisible</option>
                                </Input>
                            </div>
                            <div className="row">
                                <Input name="timezone" type="select" placeholder="Timezone" defaultValue="+03:00">
                                    <option value="-12:00">UTC-12:00</option>
                                    <option value="-11:00">UTC-11:00</option>
                                    <option value="-10:00">UTC-10:00</option>
                                    <option value="-09:30">UTC-09:30</option>
                                    <option value="-09:00">UTC-09:00</option>
                                    <option value="-08:00">UTC-08:00</option>
                                    <option value="-07:00">UTC-07:00</option>
                                    <option value="-06:00">UTC-06:00</option>
                                    <option value="-05:00">UTC-05:00</option>
                                    <option value="-04:30">UTC-04:30</option>
                                    <option value="-04:00">UTC-04:00</option>
                                    <option value="-03:30">UTC-03:30</option>
                                    <option value="-03:00">UTC-03:00</option>
                                    <option value="-02:00">UTC-02:00</option>
                                    <option value="-01:00">UTC-01:00</option>
                                    <option value="-00:00">UTC-00:00</option>
                                    <option value="+01:00">UTC+01:00</option>
                                    <option value="+02:00">UTC+02:00</option>
                                    <option value="+03:00">UTC+03:00</option>
                                    <option value="+03:30">UTC+03:30</option>
                                    <option value="+04:00">UTC+04:00</option>
                                    <option value="+04:30">UTC+04:30</option>
                                    <option value="+05:00">UTC+05:00</option>
                                    <option value="+05:30">UTC+05:30</option>
                                    <option value="+05:45">UTC+05:45</option>
                                    <option value="+06:00">UTC+06:00</option>
                                    <option value="+06:30">UTC+06:30</option>
                                    <option value="+07:00">UTC+07:00</option>
                                    <option value="+08:00">UTC+08:00</option>
                                    <option value="+08:45">UTC+08:45</option>
                                    <option value="+09:00">UTC+09:00</option>
                                    <option value="+09:30">UTC+09:30</option>
                                    <option value="+10:00">UTC+10:00</option>
                                    <option value="+10:30">UTC+10:30</option>
                                    <option value="+11:00">UTC+11:00</option>
                                    <option value="+11:30">UTC+11:30</option>
                                    <option value="+12:00">UTC+12:00</option>
                                    <option value="+12:45">UTC+12:45</option>
                                    <option value="+13:00">UTC+13:00</option>
                                    <option value="+14:00">UTC+14:00</option>
                                </Input>
                            </div>
                        </form>
                    ) : ""}
                     {this.state.currentInfo.type == "EDIT_CALENDAR" ?
                         (
                             <form id="edit_calendar">
                                 <div className="row">
                                     <ValidInput name="name" placeholder="Calendar name" checkType="isCName" value={this.state.currentInfo.additionalInfo.name} autoFocus required/>
                                 </div>
                                 <div className="row">
                                     <ValidInput name="description" placeholder="Calendar description" value={this.state.currentInfo.additionalInfo.description} checkType="isCDescr" />
                                 </div>
                                 <div className="row">
                                     <Input name="color" className="preview" type="text" name="color" id="color" placeholder="Color" defaultValue={"#"+this.state.currentInfo.additionalInfo.color} style={{backgroundColor: "#"+this.state.currentInfo.additionalInfo.color}} readOnly />
                                     <div className="col-container" style={{display: "none"}}>
                                         <div className="colorpicker" style={{display: "none"}}>
                                             <canvas id="picker" var="1" width="300" height="300"></canvas>
                                             <div className="controls">
                                                 <div>
                                                     <label>R</label>
                                                     <input type="text" id="rVal" />
                                                 </div>
                                                 <div>
                                                     <label>G</label>
                                                     <input type="text" id="gVal" />
                                                 </div>
                                                 <div>
                                                     <label>B</label>
                                                     <input type="text" id="bVal" />
                                                 </div>
                                                 <div>
                                                     <label>RGB</label>
                                                     <input type="text" id="rgbVal" />
                                                 </div>
                                                 <div>
                                                     <label>HEX</label>
                                                     <input type="text" id="hexVal" />
                                                 </div>
                                             </div>
                                         </div>
                                     </div>
                                 </div>
                                 <div className="row">
                                     <Input name="visible" type="select" defaultValue={this.state.currentInfo.additionalInfo.visible}>
                                         <option value="true">Visible</option>
                                         <option value="false">Invisible</option>
                                     </Input>
                                 </div>
                                 <div className="row">
                                     <Input name="timezone" type="select" placeholder="Timezone" defaultValue={this.state.currentInfo.additionalInfo.timezone}>
                                         <option value="-12:00">UTC-12:00</option>
                                         <option value="-11:00">UTC-11:00</option>
                                         <option value="-10:00">UTC-10:00</option>
                                         <option value="-09:30">UTC-09:30</option>
                                         <option value="-09:00">UTC-09:00</option>
                                         <option value="-08:00">UTC-08:00</option>
                                         <option value="-07:00">UTC-07:00</option>
                                         <option value="-06:00">UTC-06:00</option>
                                         <option value="-05:00">UTC-05:00</option>
                                         <option value="-04:30">UTC-04:30</option>
                                         <option value="-04:00">UTC-04:00</option>
                                         <option value="-03:30">UTC-03:30</option>
                                         <option value="-03:00">UTC-03:00</option>
                                         <option value="-02:00">UTC-02:00</option>
                                         <option value="-01:00">UTC-01:00</option>
                                         <option value="-00:00">UTC-00:00</option>
                                         <option value="+01:00">UTC+01:00</option>
                                         <option value="+02:00">UTC+02:00</option>
                                         <option value="+03:00">UTC+03:00</option>
                                         <option value="+03:30">UTC+03:30</option>
                                         <option value="+04:00">UTC+04:00</option>
                                         <option value="+04:30">UTC+04:30</option>
                                         <option value="+05:00">UTC+05:00</option>
                                         <option value="+05:30">UTC+05:30</option>
                                         <option value="+05:45">UTC+05:45</option>
                                         <option value="+06:00">UTC+06:00</option>
                                         <option value="+06:30">UTC+06:30</option>
                                         <option value="+07:00">UTC+07:00</option>
                                         <option value="+08:00">UTC+08:00</option>
                                         <option value="+08:45">UTC+08:45</option>
                                         <option value="+09:00">UTC+09:00</option>
                                         <option value="+09:30">UTC+09:30</option>
                                         <option value="+10:00">UTC+10:00</option>
                                         <option value="+10:30">UTC+10:30</option>
                                         <option value="+11:00">UTC+11:00</option>
                                         <option value="+11:30">UTC+11:30</option>
                                         <option value="+12:00">UTC+12:00</option>
                                         <option value="+12:45">UTC+12:45</option>
                                         <option value="+13:00">UTC+13:00</option>
                                         <option value="+14:00">UTC+14:00</option>
                                     </Input>
                                 </div>
                             </form>
                         ) : ""}
                    {this.state.currentInfo.type == "CREATE_EVENT" ?
                        (
                            <form id="create_event">
                                <div className="row">
                                    <ValidInput name="name" placeholder="Event name" checkType="isCName" autoFocus required/>
                                </div>
                                <div className="row">
                                    <ValidInput name="description" placeholder="Event description" checkType="isCDescr" />
                                </div>
                                <div className="row">
                                    <ValidInput name="location" placeholder="Event location" checkType="isCDescr" />
                                </div>
                                <div className="row">
                                    <Input name="calendar_id" type="select">
                                        {calendars.map(function(elem){
                                            return (
                                                <option value={elem.id}>{elem.name}</option>
                                                )
                                            })}
                                    </Input>
                                </div>
                                <div className="row"  style={{padding: 0}}>
                                    <Input name="color" className="preview" type="text" name="color" id="color" placeholder="Color" defaultValue="#000000" style={{backgroundColor: "#000"}} readOnly />
                                    <div className="col-container" style={{display: "none"}}>
                                        <div className="colorpicker" style={{display: "none"}}>
                                            <canvas id="picker" var="1" width="300" height="300"></canvas>
                                            <div className="controls">
                                                <div>
                                                    <label>R</label>
                                                    <input type="text" id="rVal" />
                                                </div>
                                                <div>
                                                    <label>G</label>
                                                    <input type="text" id="gVal" />
                                                </div>
                                                <div>
                                                    <label>B</label>
                                                    <input type="text" id="bVal" />
                                                </div>
                                                <div>
                                                    <label>RGB</label>
                                                    <input type="text" id="rgbVal" />
                                                </div>
                                                <div>
                                                    <label>HEX</label>
                                                    <input type="text" id="hexVal" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="row daterow" style={{padding:0}}>
                                    <h5>Start</h5>
                                    <Input type="date" name="datetime[start][0]" id="startdate" defaultValue={gentoday(this.state.currentInfo.additionalInfo.dataLabel)} />
                                    <Input type="time" name="datetime[start][1]" id="starttime" defaultValue="12:00" />
                                    <Input name="datetime[start][2]" id="starttimezone" type="select" placeholder="Timezone" defaultValue="+03:00">
                                        <option value="-12:00">UTC-12:00</option>
                                        <option value="-11:00">UTC-11:00</option>
                                        <option value="-10:00">UTC-10:00</option>
                                        <option value="-09:30">UTC-09:30</option>
                                        <option value="-09:00">UTC-09:00</option>
                                        <option value="-08:00">UTC-08:00</option>
                                        <option value="-07:00">UTC-07:00</option>
                                        <option value="-06:00">UTC-06:00</option>
                                        <option value="-05:00">UTC-05:00</option>
                                        <option value="-04:30">UTC-04:30</option>
                                        <option value="-04:00">UTC-04:00</option>
                                        <option value="-03:30">UTC-03:30</option>
                                        <option value="-03:00">UTC-03:00</option>
                                        <option value="-02:00">UTC-02:00</option>
                                        <option value="-01:00">UTC-01:00</option>
                                        <option value="-00:00">UTC-00:00</option>
                                        <option value="+01:00">UTC+01:00</option>
                                        <option value="+02:00">UTC+02:00</option>
                                        <option value="+03:00">UTC+03:00</option>
                                        <option value="+03:30">UTC+03:30</option>
                                        <option value="+04:00">UTC+04:00</option>
                                        <option value="+04:30">UTC+04:30</option>
                                        <option value="+05:00">UTC+05:00</option>
                                        <option value="+05:30">UTC+05:30</option>
                                        <option value="+05:45">UTC+05:45</option>
                                        <option value="+06:00">UTC+06:00</option>
                                        <option value="+06:30">UTC+06:30</option>
                                        <option value="+07:00">UTC+07:00</option>
                                        <option value="+08:00">UTC+08:00</option>
                                        <option value="+08:45">UTC+08:45</option>
                                        <option value="+09:00">UTC+09:00</option>
                                        <option value="+09:30">UTC+09:30</option>
                                        <option value="+10:00">UTC+10:00</option>
                                        <option value="+10:30">UTC+10:30</option>
                                        <option value="+11:00">UTC+11:00</option>
                                        <option value="+11:30">UTC+11:30</option>
                                        <option value="+12:00">UTC+12:00</option>
                                        <option value="+12:45">UTC+12:45</option>
                                        <option value="+13:00">UTC+13:00</option>
                                        <option value="+14:00">UTC+14:00</option>
                                    </Input>
                                </div>
                                <div className="row daterow">
                                    <h5>End</h5>
                                    <Input type="date" name="datetime[end][0]" id="enddate" defaultValue={gentoday(this.state.currentInfo.additionalInfo.dataLabel)} />
                                    <Input type="time" name="datetime[end][1]" id="endtime" defaultValue="13:00" />
                                    <Input name="datetime[end][2]" id="endtimezone" type="select" placeholder="Timezone" defaultValue="+03:00">
                                        <option value="-12:00">UTC-12:00</option>
                                        <option value="-11:00">UTC-11:00</option>
                                        <option value="-10:00">UTC-10:00</option>
                                        <option value="-09:30">UTC-09:30</option>
                                        <option value="-09:00">UTC-09:00</option>
                                        <option value="-08:00">UTC-08:00</option>
                                        <option value="-07:00">UTC-07:00</option>
                                        <option value="-06:00">UTC-06:00</option>
                                        <option value="-05:00">UTC-05:00</option>
                                        <option value="-04:30">UTC-04:30</option>
                                        <option value="-04:00">UTC-04:00</option>
                                        <option value="-03:30">UTC-03:30</option>
                                        <option value="-03:00">UTC-03:00</option>
                                        <option value="-02:00">UTC-02:00</option>
                                        <option value="-01:00">UTC-01:00</option>
                                        <option value="-00:00">UTC-00:00</option>
                                        <option value="+01:00">UTC+01:00</option>
                                        <option value="+02:00">UTC+02:00</option>
                                        <option value="+03:00">UTC+03:00</option>
                                        <option value="+03:30">UTC+03:30</option>
                                        <option value="+04:00">UTC+04:00</option>
                                        <option value="+04:30">UTC+04:30</option>
                                        <option value="+05:00">UTC+05:00</option>
                                        <option value="+05:30">UTC+05:30</option>
                                        <option value="+05:45">UTC+05:45</option>
                                        <option value="+06:00">UTC+06:00</option>
                                        <option value="+06:30">UTC+06:30</option>
                                        <option value="+07:00">UTC+07:00</option>
                                        <option value="+08:00">UTC+08:00</option>
                                        <option value="+08:45">UTC+08:45</option>
                                        <option value="+09:00">UTC+09:00</option>
                                        <option value="+09:30">UTC+09:30</option>
                                        <option value="+10:00">UTC+10:00</option>
                                        <option value="+10:30">UTC+10:30</option>
                                        <option value="+11:00">UTC+11:00</option>
                                        <option value="+11:30">UTC+11:30</option>
                                        <option value="+12:00">UTC+12:00</option>
                                        <option value="+12:45">UTC+12:45</option>
                                        <option value="+13:00">UTC+13:00</option>
                                        <option value="+14:00">UTC+14:00</option>
                                    </Input>
                                </div>
                                <div className="row" style={{padding:0}}>
                                    <Input name="rec_period_id" type="select" defaultValue="null" onChange={this.changeRecPeriod}>
                                        <option value="null">Not reccurent</option>
                                        <option value="0">Every N days</option>
                                        <option value="1">Every N weeks</option>
                                        <option value="2">Every N months</option>
                                        <option value="3">Every N years</option>
                                    </Input>
                                </div>
                                <div className="hideable hided">
                                    <div className="row">
                                        <h5>N</h5>
                                        <Input name="rec_period" type="number" defaultValue="1"></Input>
                                    </div>
                                    <div className="row daterow">
                                        <h5>Reccurence end</h5>
                                        <Input type="date" name="datetime[rec][0]" id="RECenddate" defaultValue={gentoday(this.state.currentInfo.additionalInfo.dataLabel)} />
                                        <Input type="time" name="datetime[rec][1]" id="RECendtime" defaultValue="13:00" />
                                        <Input name="datetime[rec][2]" id="RECendtimezone" type="select" placeholder="Timezone" defaultValue="+03:00">
                                            <option value="-12:00">UTC-12:00</option>
                                            <option value="-11:00">UTC-11:00</option>
                                            <option value="-10:00">UTC-10:00</option>
                                            <option value="-09:30">UTC-09:30</option>
                                            <option value="-09:00">UTC-09:00</option>
                                            <option value="-08:00">UTC-08:00</option>
                                            <option value="-07:00">UTC-07:00</option>
                                            <option value="-06:00">UTC-06:00</option>
                                            <option value="-05:00">UTC-05:00</option>
                                            <option value="-04:30">UTC-04:30</option>
                                            <option value="-04:00">UTC-04:00</option>
                                            <option value="-03:30">UTC-03:30</option>
                                            <option value="-03:00">UTC-03:00</option>
                                            <option value="-02:00">UTC-02:00</option>
                                            <option value="-01:00">UTC-01:00</option>
                                            <option value="-00:00">UTC-00:00</option>
                                            <option value="+01:00">UTC+01:00</option>
                                            <option value="+02:00">UTC+02:00</option>
                                            <option value="+03:00">UTC+03:00</option>
                                            <option value="+03:30">UTC+03:30</option>
                                            <option value="+04:00">UTC+04:00</option>
                                            <option value="+04:30">UTC+04:30</option>
                                            <option value="+05:00">UTC+05:00</option>
                                            <option value="+05:30">UTC+05:30</option>
                                            <option value="+05:45">UTC+05:45</option>
                                            <option value="+06:00">UTC+06:00</option>
                                            <option value="+06:30">UTC+06:30</option>
                                            <option value="+07:00">UTC+07:00</option>
                                            <option value="+08:00">UTC+08:00</option>
                                            <option value="+08:45">UTC+08:45</option>
                                            <option value="+09:00">UTC+09:00</option>
                                            <option value="+09:30">UTC+09:30</option>
                                            <option value="+10:00">UTC+10:00</option>
                                            <option value="+10:30">UTC+10:30</option>
                                            <option value="+11:00">UTC+11:00</option>
                                            <option value="+11:30">UTC+11:30</option>
                                            <option value="+12:00">UTC+12:00</option>
                                            <option value="+12:45">UTC+12:45</option>
                                            <option value="+13:00">UTC+13:00</option>
                                            <option value="+14:00">UTC+14:00</option>
                                        </Input>
                                    </div>
                                </div>
                            </form>
                        ) : ""}
                    {this.state.currentInfo.type == "EDIT_EVENT" ?
                        (
                            <form id="edit_event">
                                <div className="row">
                                    <ValidInput name="name" placeholder="Event name" checkType="isCName" value={this.state.currentInfo.additionalInfo.name} autoFocus required/>
                                </div>
                                <div className="row">
                                    <ValidInput name="description" placeholder="Event description" checkType="isCDescr" value={this.state.currentInfo.additionalInfo.description} />
                                </div>
                                <div className="row">
                                    <ValidInput name="location" placeholder="Event location" checkType="isCDescr" value={this.state.currentInfo.additionalInfo.location} />
                                </div>
                                <div className="row">
                                    <Input name="calendar_id" type="select" defaultValue={this.state.currentInfo.additionalInfo.calendarid}>
                                        {calendars.map(function(elem){
                                            return (
                                                <option value={elem.id}>{elem.name}</option>
                                            )
                                        })}
                                    </Input>
                                </div>
                                <div className="row"  style={{padding: 0}}>
                                    <Input name="color" className="preview" type="text" name="color" id="color" placeholder="Color" defaultValue={'#'+this.state.currentInfo.additionalInfo.color.substr(-6)} style={{backgroundColor: '#'+this.state.currentInfo.additionalInfo.color.substr(-6)}} readOnly />
                                    <div className="col-container" style={{display: "none"}}>
                                        <div className="colorpicker" style={{display: "none"}}>
                                            <canvas id="picker" var="1" width="300" height="300"></canvas>
                                            <div className="controls">
                                                <div>
                                                    <label>R</label>
                                                    <input type="text" id="rVal" />
                                                </div>
                                                <div>
                                                    <label>G</label>
                                                    <input type="text" id="gVal" />
                                                </div>
                                                <div>
                                                    <label>B</label>
                                                    <input type="text" id="bVal" />
                                                </div>
                                                <div>
                                                    <label>RGB</label>
                                                    <input type="text" id="rgbVal" />
                                                </div>
                                                <div>
                                                    <label>HEX</label>
                                                    <input type="text" id="hexVal" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="row daterow" style={{padding:0}}>
                                    <h5>Start</h5>
                                    <Input type="date" name="datetime[start][0]" id="startdate" defaultValue={this.state.currentInfo.additionalInfo.startdate} />
                                    <Input type="time" name="datetime[start][1]" id="starttime" defaultValue={this.state.currentInfo.additionalInfo.starttime} />
                                    <Input name="datetime[start][2]" id="starttimezone" type="select" placeholder="Timezone" defaultValue={this.state.currentInfo.additionalInfo.start_time_tz}>
                                        <option value="-12:00">UTC-12:00</option>
                                        <option value="-11:00">UTC-11:00</option>
                                        <option value="-10:00">UTC-10:00</option>
                                        <option value="-09:30">UTC-09:30</option>
                                        <option value="-09:00">UTC-09:00</option>
                                        <option value="-08:00">UTC-08:00</option>
                                        <option value="-07:00">UTC-07:00</option>
                                        <option value="-06:00">UTC-06:00</option>
                                        <option value="-05:00">UTC-05:00</option>
                                        <option value="-04:30">UTC-04:30</option>
                                        <option value="-04:00">UTC-04:00</option>
                                        <option value="-03:30">UTC-03:30</option>
                                        <option value="-03:00">UTC-03:00</option>
                                        <option value="-02:00">UTC-02:00</option>
                                        <option value="-01:00">UTC-01:00</option>
                                        <option value="-00:00">UTC-00:00</option>
                                        <option value="+01:00">UTC+01:00</option>
                                        <option value="+02:00">UTC+02:00</option>
                                        <option value="+03:00">UTC+03:00</option>
                                        <option value="+03:30">UTC+03:30</option>
                                        <option value="+04:00">UTC+04:00</option>
                                        <option value="+04:30">UTC+04:30</option>
                                        <option value="+05:00">UTC+05:00</option>
                                        <option value="+05:30">UTC+05:30</option>
                                        <option value="+05:45">UTC+05:45</option>
                                        <option value="+06:00">UTC+06:00</option>
                                        <option value="+06:30">UTC+06:30</option>
                                        <option value="+07:00">UTC+07:00</option>
                                        <option value="+08:00">UTC+08:00</option>
                                        <option value="+08:45">UTC+08:45</option>
                                        <option value="+09:00">UTC+09:00</option>
                                        <option value="+09:30">UTC+09:30</option>
                                        <option value="+10:00">UTC+10:00</option>
                                        <option value="+10:30">UTC+10:30</option>
                                        <option value="+11:00">UTC+11:00</option>
                                        <option value="+11:30">UTC+11:30</option>
                                        <option value="+12:00">UTC+12:00</option>
                                        <option value="+12:45">UTC+12:45</option>
                                        <option value="+13:00">UTC+13:00</option>
                                        <option value="+14:00">UTC+14:00</option>
                                    </Input>
                                </div>
                                <div className="row daterow">
                                    <h5>End</h5>
                                    <Input type="date" name="datetime[end][0]" id="enddate" defaultValue={this.state.currentInfo.additionalInfo.enddate} />
                                    <Input type="time" name="datetime[end][1]" id="endtime" defaultValue={this.state.currentInfo.additionalInfo.endtime} />
                                    <Input name="datetime[end][2]" id="endtimezone" type="select" placeholder="Timezone" defaultValue={this.state.currentInfo.additionalInfo.end_time_tz}>
                                        <option value="-12:00">UTC-12:00</option>
                                        <option value="-11:00">UTC-11:00</option>
                                        <option value="-10:00">UTC-10:00</option>
                                        <option value="-09:30">UTC-09:30</option>
                                        <option value="-09:00">UTC-09:00</option>
                                        <option value="-08:00">UTC-08:00</option>
                                        <option value="-07:00">UTC-07:00</option>
                                        <option value="-06:00">UTC-06:00</option>
                                        <option value="-05:00">UTC-05:00</option>
                                        <option value="-04:30">UTC-04:30</option>
                                        <option value="-04:00">UTC-04:00</option>
                                        <option value="-03:30">UTC-03:30</option>
                                        <option value="-03:00">UTC-03:00</option>
                                        <option value="-02:00">UTC-02:00</option>
                                        <option value="-01:00">UTC-01:00</option>
                                        <option value="-00:00">UTC-00:00</option>
                                        <option value="+01:00">UTC+01:00</option>
                                        <option value="+02:00">UTC+02:00</option>
                                        <option value="+03:00">UTC+03:00</option>
                                        <option value="+03:30">UTC+03:30</option>
                                        <option value="+04:00">UTC+04:00</option>
                                        <option value="+04:30">UTC+04:30</option>
                                        <option value="+05:00">UTC+05:00</option>
                                        <option value="+05:30">UTC+05:30</option>
                                        <option value="+05:45">UTC+05:45</option>
                                        <option value="+06:00">UTC+06:00</option>
                                        <option value="+06:30">UTC+06:30</option>
                                        <option value="+07:00">UTC+07:00</option>
                                        <option value="+08:00">UTC+08:00</option>
                                        <option value="+08:45">UTC+08:45</option>
                                        <option value="+09:00">UTC+09:00</option>
                                        <option value="+09:30">UTC+09:30</option>
                                        <option value="+10:00">UTC+10:00</option>
                                        <option value="+10:30">UTC+10:30</option>
                                        <option value="+11:00">UTC+11:00</option>
                                        <option value="+11:30">UTC+11:30</option>
                                        <option value="+12:00">UTC+12:00</option>
                                        <option value="+12:45">UTC+12:45</option>
                                        <option value="+13:00">UTC+13:00</option>
                                        <option value="+14:00">UTC+14:00</option>
                                    </Input>
                                </div>
                                <div className="row" style={{padding:0}}>
                                    <Input name="rec_period_id" type="select" defaultValue={this.state.currentInfo.additionalInfo.rec_period_id} onChange={this.changeRecPeriod}>
                                        <option value="null">Not reccurent</option>
                                        <option value="0">Every N days</option>
                                        <option value="1">Every N weeks</option>
                                        <option value="2">Every N months</option>
                                        <option value="3">Every N years</option>
                                    </Input>
                                </div>
                                <div className={"hideable "+(this.state.currentInfo.additionalInfo.rec_period_id===null?"hided":"")}>
                                    <div className="row">
                                        <h5>N</h5>
                                        <Input name="rec_period" type="number" defaultValue={this.state.currentInfo.additionalInfo.rec_period}></Input>
                                    </div>
                                    <div className="row daterow">
                                        <h5>Reccurence end</h5>
                                        <Input type="date" name="datetime[rec][0]" id="RECenddate" defaultValue={this.state.currentInfo.additionalInfo.recdate} />
                                        <Input type="time" name="datetime[rec][1]" id="RECendtime" defaultValue={this.state.currentInfo.additionalInfo.rectime} />
                                        <Input name="datetime[rec][2]" id="RECendtimezone" type="select" placeholder="Timezone" defaultValue={this.state.currentInfo.additionalInfo.rec_end_time_tz}>
                                            <option value="-12:00">UTC-12:00</option>
                                            <option value="-11:00">UTC-11:00</option>
                                            <option value="-10:00">UTC-10:00</option>
                                            <option value="-09:30">UTC-09:30</option>
                                            <option value="-09:00">UTC-09:00</option>
                                            <option value="-08:00">UTC-08:00</option>
                                            <option value="-07:00">UTC-07:00</option>
                                            <option value="-06:00">UTC-06:00</option>
                                            <option value="-05:00">UTC-05:00</option>
                                            <option value="-04:30">UTC-04:30</option>
                                            <option value="-04:00">UTC-04:00</option>
                                            <option value="-03:30">UTC-03:30</option>
                                            <option value="-03:00">UTC-03:00</option>
                                            <option value="-02:00">UTC-02:00</option>
                                            <option value="-01:00">UTC-01:00</option>
                                            <option value="-00:00">UTC-00:00</option>
                                            <option value="+01:00">UTC+01:00</option>
                                            <option value="+02:00">UTC+02:00</option>
                                            <option value="+03:00">UTC+03:00</option>
                                            <option value="+03:30">UTC+03:30</option>
                                            <option value="+04:00">UTC+04:00</option>
                                            <option value="+04:30">UTC+04:30</option>
                                            <option value="+05:00">UTC+05:00</option>
                                            <option value="+05:30">UTC+05:30</option>
                                            <option value="+05:45">UTC+05:45</option>
                                            <option value="+06:00">UTC+06:00</option>
                                            <option value="+06:30">UTC+06:30</option>
                                            <option value="+07:00">UTC+07:00</option>
                                            <option value="+08:00">UTC+08:00</option>
                                            <option value="+08:45">UTC+08:45</option>
                                            <option value="+09:00">UTC+09:00</option>
                                            <option value="+09:30">UTC+09:30</option>
                                            <option value="+10:00">UTC+10:00</option>
                                            <option value="+10:30">UTC+10:30</option>
                                            <option value="+11:00">UTC+11:00</option>
                                            <option value="+11:30">UTC+11:30</option>
                                            <option value="+12:00">UTC+12:00</option>
                                            <option value="+12:45">UTC+12:45</option>
                                            <option value="+13:00">UTC+13:00</option>
                                            <option value="+14:00">UTC+14:00</option>
                                        </Input>
                                    </div>
                                </div>
                            </form>
                        ) : ""}
                </div>
                <div className="modal-footer">
                {((this.state.currentInfo.type == "EDIT_CALENDAR"||this.state.currentInfo.type == "EDIT_EVENT")&&!this.state.currentInfo.additionalInfo.required)?
                    (
                        <Button bsStyle="danger" onClick={this.deleteSubmit}>Delete</Button>
                    )
                    :""}
                    <Button onClick={this.handleSubmit}>Submit</Button>
                </div>
            </Modal>
        );
    },

    changeRecPeriod:function(e) {
        if (e.target.value!=="null") $('.hideable').removeClass('hided');
        else $('.hideable').addClass('hided');
    },

    _onChange: function () {
        this.setState(getModalData(), function () {
            if (AlertStore.getCurrentInfo().alertVisible)
                AlertActions.hideError();
            switch (this.state.currentInfo.type) {
                case 'NEW_CALENDAR':
                case 'EDIT_CALENDAR':
                case 'EDIT_EVENT':
                case 'CREATE_EVENT':
                    initPicker();
                    LoginFormStore.registerInputs(["name","description","location"]);
                    this.setState({calendars:CalendarStore.getCalendars()});
                    break;
                default:
                    break;
            }
        });
        this.setState({isModalOpen: true}, null);
    }
});

module.exports = CustomModalTrigger;