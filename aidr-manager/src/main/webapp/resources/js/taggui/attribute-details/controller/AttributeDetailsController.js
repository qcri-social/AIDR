    Ext.define('TAGGUI.attribute-details.controller.AttributeDetailsController', {
    extend: 'Ext.app.Controller',

    views: [
        'AttributeDetailsPanel'
    ],

    init: function () {

        this.control({

            'attribute-details-view': {
                beforerender: this.beforeRenderView
            }

        });

    },

    beforeRenderView: function (component) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
        attributeDetailsController = this;

        this.getAttributeInfo();
    },

    getAttributeInfo: function () {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/getAttributeInfo.action',
            method: 'GET',
            params: {
                id: ATTRIBUTE_ID
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data) {
                        me.mainComponent.taggerTitle.setText("Details for classifier \"" + resp.data.name + "\"");
                        me.mainComponent.main.loadData(resp.data);
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            }
        });
    }

});