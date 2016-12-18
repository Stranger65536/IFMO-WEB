/**
 * Created by developer123 on 13.02.15.
 */
var React = require('react');
var $=require('jquery');
var Navbar = require('react-bootstrap').Navbar;
var Nav = require('react-bootstrap').Nav;
var NavItem = require('react-bootstrap').NavItem;
var Button = require('react-bootstrap').Button;
var Alert = require('./Alert');
var CustomModalTrigger = require('./Modal');
var apiurl=localStorage['api']||"";

var QueryString = function () {
    var query_string = {};
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (typeof query_string[pair[0]] === "undefined") {
            query_string[pair[0]] = pair[1];
        } else if (typeof query_string[pair[0]] === "string") {
            var arr = [query_string[pair[0]], pair[1]];
            query_string[pair[0]] = arr;
        } else {
            query_string[pair[0]].push(pair[1]);
        }
    }
    return query_string;
}();
function setCookie(name, value, options) {
    options = options || {};

    var expires = options.expires;

    if (typeof expires == "number" && expires) {
        var d = new Date();
        d.setTime(d.getTime() + expires*1000);
        expires = options.expires = d;
    }
    if (expires && expires.toUTCString) {
        options.expires = expires.toUTCString();
    }

    value = encodeURIComponent(value);

    var updatedCookie = name + "=" + value;

    for(var propName in options) {
        updatedCookie += "; " + propName;
        var propValue = options[propName];
        if (propValue !== true) {
            updatedCookie += "=" + propValue;
        }
    }

    document.cookie = updatedCookie;
}

var Header = React.createClass({

    getInitialState:function() {
        return {};
    },

    logout: function() {
        sendRequest(apiurl+'/users/'+localStorage['userid']+'/logout',"POST",[],JSON.parse(localStorage['defaultheaders']),function(answer) {
            if (answer.status) {
                localStorage.clear();
                location.href = "index.html";
            }
        });
    },

    render: function () {
        return (
            <header>
                <Navbar>
                    <Nav>
                        <NavItem href={localStorage['token']?"calendar.html":"index.html"}>
                            <span className="caption">LeafCalendar</span>
                        </NavItem>
                    {this.props.type=="full"?(
                        <NavItem className="logout">
                            <Button bsStyle="primary" className="logout" onClick={this.logout}>Logout</Button>
                        </NavItem>
                    ):""}
                    </Nav>
                </Navbar>
                < Alert />
                <CustomModalTrigger />
            </header>
        );
    }
});
module.exports=Header;