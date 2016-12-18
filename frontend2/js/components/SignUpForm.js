/**
 * Created by developer123 on 11.02.15.
 */
var React = require('react');
var Button = require('react-bootstrap').Button;
var Navbar = require('react-bootstrap').Navbar;
var Nav = require('react-bootstrap').Nav;
var NavItem = require('react-bootstrap').NavItem;
var ValidInput = require('./ValidInput');
var Alert = require('./Alert');
var LoginFormStore = require('../stores/LoginFormStore');
var LoginFormActions = require('../actions/LoginFormActions');
var FormLoaderActions = require('../actions/FormLoaderActions');
var AlertActions = require('../actions/AlertActions');
var $ = require('jquery');
var apiurl=localStorage['api']||"";

function getInputState() {
    return {
        FormJSONed: LoginFormStore.FormJSONed(),
        AllValid: LoginFormStore.areAllValid()
    };
}

var SignUpForm = React.createClass({
    getInitialState: function () {
        LoginFormStore.registerInputs(["first_name", "last_name", "email", "password", "passconfirm"]);
        return getInputState();
    },

    componentDidMount: function () {
        LoginFormStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function () {
        LoginFormStore.removeChangeListener(this._onChange);
    },

    render: function () {
        return (
            <div className="container">
                <div className="formContainer">
                    <div className="formCaption">
                        <span className="caption">{Localize("sign up")}</span>
                    </div>
                    <form onSubmit={this.handleSubmit}>
                        <div className="row">
                            <ValidInput name="first_name" type="text" checkType="isName" placeholder={Localize("first name")}/>
                        </div>
                        <div className="row">
                            <ValidInput name="last_name" type="text" checkType="isName" placeholder={Localize("last name")}/>
                        </div>
                        <div className="row">
                            <ValidInput name="email" type="email" checkType="isEmail" placeholder={Localize("email")}/>
                        </div>
                        <div className="row">
                            <ValidInput name="password" type="password" checkType="Password" placeholder={Localize("password")}/>
                        </div>
                        <div className="row">
                            <ValidInput name="passconfirm" type="password" checkType="RePass" placeholder={Localize("retype")+" "+Localize("password")}/>
                        </div>
                        <div className="row">
                            <Button bsStyle="primary" disabled={!this.state.AllValid} onClick={this.handleSubmit}>{Localize("submit")}</Button>
                        </div>
                    </form>
                    <span>{Localize("already a member")} -
                        <a onClick={this.changeFormType}>{Localize("login")}!</a>
                    </span>
                </div>
            </div>
        );
    },

    changeFormType: function () {
        FormLoaderActions.showLogin();
    },

    handleSubmit: function (event) {
        event.preventDefault();
        var json = this.state.FormJSONed;
        var newjson = {};
        newjson.first_name = json.first_name;
        newjson.last_name = json.last_name;
        newjson.auth_base64 = window.btoa(json.email + ":" + json.password);
        sendRequest(apiurl+'/users/',"POST",JSON.stringify(newjson),[],function(answer) {
            if (!answer.status) AlertActions.showError(answer.message,"danger");
            else AlertActions.showError(answer.message,"success");
        });
    },

    _onChange: function () {
        this.setState(getInputState());
    }

});

module.exports = SignUpForm;