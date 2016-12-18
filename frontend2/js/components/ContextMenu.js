/**
 * Created by developer123 on 26.06.15.
 */
var React = require('react');
var $ = require('jquery');
var ContextMenuStore = require('../stores/ContextMenuStore');
var ContextMenuActions = require('../actions/ContextMenuActions');
var ModalActions = require('../actions/ModalActions');
var AlertActions = require('../actions/AlertActions');
var apiurl=localStorage['api']||"";

var ContextMenu = React.createClass({

    getInitialState: function () {
        return {
            display: "none",
            top: 0,
            left: 0,
            info: [],
            type: "files"
        }
    },

    componentDidMount: function () {
        ContextMenuStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function () {
        ContextMenuStore.removeChangeListener(this._onChange);
    },

    disableSubMenu:function(e) {
        e.preventDefault();
    },

    createEvent:function() {
        ModalActions.createEvent(this.state.info);
        ContextMenuActions.hideMenu();
    },

    render: function () {
        if (this.state.display != "none")
                return (
                    <div className="contextmenu" style={this.state} onContextMenu={this.disableSubMenu}>
                        <div className="contextitem" onClick={this.createEvent}>Create Event</div>
                    </div>
                );
        else
            return (<span></span>)
    },

    _onChange: function () {
        //noinspection JSSuspiciousNameCombination
        var newinfo = ContextMenuStore.getCurrentInfo();
        console.log(newinfo);
        if (newinfo.X != "-1")
            this.replaceState({
                display: newinfo.visible ? "block" : "none",
                top: newinfo.Y,
                left: newinfo.X,
                info: newinfo.info
            }, null);
        else
            this.replaceState({
                display: newinfo.visible ? "block" : "none",
                top: newinfo.Y,
                right: 0,
                info: newinfo.info
            }, null);
    }
});

module.exports = ContextMenu;