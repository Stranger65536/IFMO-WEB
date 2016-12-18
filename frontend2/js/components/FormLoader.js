/**
 * Created by developer123 on 11.02.15.
 */
var React=require('react');
var $=require('jquery');
var LoginForm=require('./LoginForm');
var SignUpForm=require('./SignUpForm');
var Header=require('./Header');
var FormLoaderStore=require('../stores/FormLoaderStore');
var AlertActions=require('../actions/AlertActions');

var FormLoader=React.createClass({
    getInitialState:function() {
        return {
            currentForm:<LoginForm />
        };
    },


    componentDidMount: function() {
        FormLoaderStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function() {
        FormLoaderStore.removeChangeListener(this._onChange);
    },

    render: function() {
        return (
            <div className="page">
                <Header type="minified" />
            {this.state.currentForm}
            </div>
        );
    },

    _onChange: function() {
        this.setState({
            currentForm:FormLoaderStore.getCurrentType()
        });
    }

});

module.exports=FormLoader;