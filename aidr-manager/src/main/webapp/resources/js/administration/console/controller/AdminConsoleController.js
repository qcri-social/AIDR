Ext.define('ADMIN.console.controller.AdminConsoleController', {
    extend: 'Ext.app.Controller',

    views: [
        'AdminConsolePanel'
    ],

    init: function () {

        this.control({

            'admin-console-view': {
                beforerender: this.beforeRenderView
            },

            "#toSystemHealthButton": {
                click: function (btn, e, eOpts) {
                    this.toSystemHealth();
                }
            }

        });

    },

    beforeRenderView: function (component) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
    },

    toSystemHealth: function() {
        document.location.href = BASE_URL + '/protected/administration/admin-health';
    }

});