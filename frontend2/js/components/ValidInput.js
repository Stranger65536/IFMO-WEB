/**
 * Created by developer123 on 11.02.15.
 */
var React = require('react');
var LoginFormActions = require('../actions/LoginFormActions');
var LoginFormStore = require('../stores/LoginFormStore');

var ValidInput=React.createClass({
    getInitialState: function() {
        return {
            value:this.props.value,
            valid:""
        }
    },

    componentDidMount: function() {
        LoginFormStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function() {
        LoginFormStore.removeChangeListener(this._onChange);
    },

    render:function() {
        return (
            <input ref="inner" type={this.props.type} className={this.state.valid} value={this.state.value} onChange={this.handleChange} name={this.props.name} placeholder={this.props.placeholder||this.props.name}/>
        )
    },

    handleChange: function(event) {
        var value=event.target.value;
        this.setState({value:value},function(){
            LoginFormActions.inputChange(this.props.name,value,this.props.checkType);
        });
    },

    _onChange:function() {
        this.setState({
            valid:LoginFormStore.isValid(this.props.name)
        });
    }
});

module.exports=ValidInput;