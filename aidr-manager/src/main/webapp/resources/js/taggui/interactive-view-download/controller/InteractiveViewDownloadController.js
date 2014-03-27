Ext.define('TAGGUI.interactive-view-download.controller.InteractiveViewDownloadController', {
    extend: 'Ext.app.Controller',

    views: [
        'InteractiveViewDownloadPanel'
    ],

    init: function () {

        this.control({

            'interactive-view-download-view': {
                beforerender: this.beforeRenderView
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();
        this.mainComponent = component;

//        this.loadCollectionData();
    }

});