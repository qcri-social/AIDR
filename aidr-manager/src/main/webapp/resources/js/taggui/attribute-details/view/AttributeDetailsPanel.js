Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'TAGGUI.attribute-details.view.AttributeDetailsMain',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
]);

Ext.define('TAGGUI.attribute-details.view.AttributeDetailsPanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.attribute-details-view',

    attributeName: '',

    initComponent: function () {
        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/home">My Collections</a><span>&nbsp;>&nbsp;Classifier details</span></div>',
            margin: 0,
            padding: 0
        });

        this.taggerTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Details for classifier',
            flex: 1
        });

        this.main = Ext.create('TAGGUI.attribute-details.view.AttributeDetailsMain', {
            showRemoveClassifierButton: false
        });

        this.items = [
            this.breadcrumbs,
            {
                xtype: 'container',
                margin: '5 0 0 0',
                html: '<div class="horizontalLine"></div>'
            },            
            {
            	xtype: 'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                    this.main
                ]
            }
        ];

        this.callParent(arguments);
    }

});