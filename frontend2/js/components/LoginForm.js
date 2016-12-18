/**
 * Created by developer123 on 11.02.15.
 */
var React = require('react');
var Button = require('react-bootstrap').Button;
var ValidInput = require('./ValidInput');
var Alert = require('./Alert');
var LoginFormStore = require('../stores/LoginFormStore');
var AlertActions = require('../actions/AlertActions');
var FormLoaderActions = require('../actions/FormLoaderActions');
var $ = require('jquery');
var apiurl = localStorage['api'] || '';

function getInputState() {
    return {
        FormJSONed: LoginFormStore.FormJSONed(),
        AllValid: LoginFormStore.areAllValid()
    };
}

var LoginForm = React.createClass({
    getInitialState: function () {
        LoginFormStore.registerInputs(["email", "password"]);
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
                        <span className="caption">{Localize("login")}</span>
                    </div>
                    <form onSubmit={this.handleSubmit}>
                        <div className="row">
                            <ValidInput name="email" type="text" checkType="isEmail" placeholder={Localize("email")}/>
                        </div>
                        <div className="row">
                            <ValidInput name="password" type="password" checkType="Password" placeholder={Localize("password")}/>
                        </div>
                        <div className="row">
                            <Button bsStyle="primary" type="submit" disabled={!this.state.AllValid} onClick={this.handleSubmit}>{Localize("submit")}</Button>
                        </div>
                    </form>
                    <span>{Localize("not yet a member")} -
                        <a onClick={this.changeFormType}>{Localize("sign up")}!</a>
                    </span>
                </div>
            </div>
        );
    },

    changeFormType: function () {
        FormLoaderActions.showSignUp();
    },

    handleSubmit: function (event) {
        event.preventDefault();
        var json = this.state.FormJSONed;
        var newjson = {};
        newjson.auth_base64 = window.btoa(json.email + ":" + json.password);
        sendRequest(apiurl+"/users/login", "POST", JSON.stringify(newjson), [], function (answer) {
            console.log(answer);
            if (!answer.status) AlertActions.showError(answer.message, "danger");
            else {
                localStorage['userid']=answer.data.id;
                localStorage['token']=answer.data.auth_token;
                localStorage['defaultheaders']=JSON.stringify([{"name":"auth_token","value":answer.data.auth_token}]);
                location.href="calendar.html";
            }
        });
    },

    _onChange: function () {
        this.setState(getInputState());
    }

});

module.exports = LoginForm;