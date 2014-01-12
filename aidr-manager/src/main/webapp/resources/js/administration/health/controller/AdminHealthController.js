Ext.define('ADMIN.health.controller.AdminHealthController', {
    extend: 'Ext.app.Controller',

    views: [
        'AdminHealthPanel'
    ],

    init: function () {

        this.control({

            'admin-health-view': {
                beforerender: this.beforeRenderView
            }

        });

    },

    beforeRenderView: function (component) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
    }

});