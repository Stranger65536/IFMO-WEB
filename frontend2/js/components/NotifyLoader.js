var React = require('react');
var $ = require('jquery');
var AlertActions = require('../actions/AlertActions');
var Header = require('../components/Header');
var apiurl = localStorage['api'] || "";

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

var NotifyLoader = React.createClass({
    getInitialState: function () {
        return {message: "Incorrect query", style: ""}
    },

    componentDidMount: function () {
        console.log(QueryString.mode);
        this.setState({mode: QueryString.mode, token: QueryString.token, userid: QueryString.userid}, function () {
            if (this.state.mode == "activate") {
                console.log("activation");
                if (this.state.token.length) {
                    var self = this;
                    var activatedata = {"email_validation_token": this.state.token};
                    console.log(activatedata);
                    sendRequest(apiurl + '/users/' + this.state.userid + '/validate/', "POST", JSON.stringify(activatedata), [], function (answer) {
                        console.log(answer);
                        if (!answer.status) self.setState({message: answer.message});
                        else self.setState({message: "Account has been successfully activated"});
                    });
                }
            }
        });
    },

    render: function () {
        return (
            <div className="page">
                <Header />
                <div className="container">
                    <div className="notify">
                        <div className="block_success">
                            <h2 className={this.state.style}>{this.state.message}</h2>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = NotifyLoader;