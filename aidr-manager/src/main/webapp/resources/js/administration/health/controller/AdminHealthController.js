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

        this.pingService('tagger');
        this.pingService('collector');
        this.pingService('trainer');
        this.pingService('AIDROutput');
        this.pingService('persister');

        this.mainComponent = component;
    },

    pingService: function (service) {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/pingService.action',
            method: 'GET',
            params: {
                service: service
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                var status;
                if (resp.success && resp.data === true) {
                    status = '<span class="greenInfo">Running OK</span>';
                } else {
                    status = '<span class="redInfo">Problem: ' + service + ' not running</span>';
                    AIDRFMFunctions.reportIssue(response);
                }
                if (service === 'tagger') {
                    me.mainComponent.taggerStatus.setText(status, false);
                } else if (service === 'collector') {
                    me.mainComponent.collectorStatus.setText(status, false);
                } else if (service === 'trainer') {
                    me.mainComponent.trainerStatus.setText(status, false);
                } else if (service === 'AIDROutput') {
                    me.mainComponent.AIDROutputStatus.setText(status, false);
                } else if (service === 'persister') {
                    me.mainComponent.persisterStatus.setText(status, false);
                }
            }
        });
    }

});