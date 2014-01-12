Ext.define('ADMIN.console.controller.AdminConsoleController', {
    extend: 'Ext.app.Controller',

    views: [
        'AdminConsolePanel'
    ],

    init: function () {

        this.control({

            'admin-console-view': {
                beforerender: this.beforeRenderView
            }

        });

    },

    beforeRenderView: function (component) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
    }

});