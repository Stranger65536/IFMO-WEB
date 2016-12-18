/**
 * Created by developer123 on 13.02.15.
 */
var React=require('react');
var Alert=require('react-bootstrap').Alert;
var AlertStore=require('../stores/AlertStore');


function getAlertState() {
    return {
        currentInfo:AlertStore.getCurrentInfo(),
        margin:{marginLeft:0+'px'}
    };
}

var AlertAutoDismissable = React.createClass({
    getInitialState: function () {
        return getAlertState();
    },

    componentDidMount: function() {
        AlertStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function() {
        AlertStore.removeChangeListener(this._onChange);
    },

    render: function () {
        if (this.state.currentInfo.alertVisible) {
            return (
                <Alert bsStyle={this.state.currentInfo.type} onDismiss={this.handleAlertDismiss} dismissAfter={5000} style={this.state.margin}>
                    <p>{this.state.currentInfo.message}</p>
                </Alert>
            );
        }

        return (
            <span></span>
        );
    },

    _onChange:function() {
        var self=this;
        if (this.isMounted()) this.setState(getAlertState(),function() {
            var width=$('div.alert').width();
            self.setState({margin:{marginLeft:(-1)*width/2+'px'}});
        });
    },

    handleAlertDismiss: function () {
        this.setState({currentInfo: {alertVisible: false}});
    },

    handleAlertShow: function () {
        this.setState({currentInfo: {alertVisible: true}});
    }
});

module.exports=AlertAutoDismissable;
