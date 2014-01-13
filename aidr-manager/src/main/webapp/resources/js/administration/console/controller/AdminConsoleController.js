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

            "#toSystemHealthButtonId": {
                click: function () {
                    this.toSystemHealth();
                }
            },
            
            '#runningCollectionsSearchFieldId': {
                specialkey: this.runningCollectionsSearchFieldHandler
            },

            '#runningCollectionsSearchButtonId': {
                click: this.searchRunningCollections
            },

            '#stoppedCollectionsSearchFieldId': {
                specialkey: this.stoppedCollectionsSearchFieldHandler
            },

            '#stoppedCollectionsSearchButtonId': {
                click: this.searchStoppedCollections
            }

        });

    },

    beforeRenderView: function (component) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
    },

    toSystemHealth: function() {
        document.location.href = BASE_URL + '/protected/administration/admin-health';
    },

    runningCollectionsSearchFieldHandler: function(field, e) {
        if (e.getKey() == e.ENTER) {
            this.searchRunningCollections();
        }
    },

    searchRunningCollections: function() {
        this.mainComponent.runningCollectionsStore.loadPage(1);
    },

    stoppedCollectionsSearchFieldHandler: function(field, e) {
        if (e.getKey() == e.ENTER) {
            this.searchStoppedCollections();
        }
    },

    searchStoppedCollections: function() {
        this.mainComponent.stoppedCollectionsStore.loadPage(1);
    }

});