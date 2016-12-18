/**
 * Created by developer123 on 22.06.15.
 */
var React = require('react');
var $ = require('jquery');
var Header = require('./Header');
var ControlPanel = require('./ControlPanel');
var CList = require('./CList');
var Calend = require('./Calend');
var ContextMenu = require('./ContextMenu');
var ContextMenuActions = require('../actions/ContextMenuActions');
var CalendarActions = require('../actions/CalendarActions');
var ContextMenuStore = require('../stores/ContextMenuStore');

var Calendar = React.createClass({
    getInitialState: function () {
        return {};
    },

    componentDidMount:function() {
        $(document).click(function(e){
            if (!$(e.target).parent('.modal-open').length) {
                if (!$(e.target).parent('.contextmenu').length && ContextMenuStore.getCurrentInfo().visible)
                    ContextMenuActions.hideMenu();
                if (!$(e.target).parent('td').length)
                    CalendarActions.clearSelection("");
            }
        })
    },

    render: function () {
        return (
            <div className="page">
                <Header type="full" />
                <div className="cont">
                    <ControlPanel />
                    <div className="centralLine row">
                        <div className="leftSide">
                            <CList />
                        </div>
                        <div className="rightSide">
                            <ContextMenu />
                            <Calend />
                        </div>
                    </div>
                </div>
            </div>
        );
    }

});

module.exports = Calendar;