Ext.define('AIDRPUBLIC.home.controller.PublicHomeController', {
    extend: 'Ext.app.Controller',

    views: [
        'PublicHomePanel'
    ],

    init: function () {

        this.control({

            'public-home-view': {
                beforerender: this.beforeRenderView
            },

            "#manageCollections": {
                click: function (btn, e, eOpts) {
                    document.location.href = BASE_URL + '/protected/home';
                }
            }

        });

    },
    updateLastRefreshDate: function() {
        this.mainComponent.collectionDescription.setText('Status as of ' + Ext.Date.format(new Date(), 'F j, Y, g:i:s A'));
    },
    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
        PublicHomeController = this;

        var me = this;
    }

});