Ext.define('TAGGUI.home.controller.TaggerHomeController', {
    extend: 'Ext.app.Controller',

    views: [
        'TaggerHomePanel'
    ],

    init: function () {

        this.control({

            'tagger-home-view': {
                beforerender: this.beforeRenderView
            },

            "#manageCollections": {
                click: function (btn, e, eOpts) {
                    document.location.href = BASE_URL + '/protected/home';
                }
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
        taggerHomeController = this;

        var me = this;
    }

});