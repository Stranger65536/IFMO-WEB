/**
 * Created by developer123 on 26.06.15.
 */
var AppDispatcher = require('../dispatcher/AppDispatcher');
var ContextMenuConstants = require('../constants/ContextMenuConstants');

var ContextMenuActions = {

    showMenu:function(X,Y,info) {
        console.log("Context Menu Actions -- show menu");
        AppDispatcher.dispatch({
            actionType:ContextMenuConstants.TOGGLE_MENU,
            isVisible:true,
            x:X,
            y:Y,
            info:info
        });
    },

    hideMenu:function() {
        console.log("Context Menu Actions -- hide menu");
        AppDispatcher.dispatch({
            actionType:ContextMenuConstants.TOGGLE_MENU,
            isVisible:false,
            x:0,
            y:0
        });
    }

};

module.exports = ContextMenuActions;