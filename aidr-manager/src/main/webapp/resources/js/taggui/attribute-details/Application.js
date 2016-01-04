Ext.application({
    requires: [
        'Ext.container.Viewport'
    ],

    name: 'TAGGUI.attribute-details',

    controllers: [
        'AttributeDetailsController'
    ],

    appFolder: BASE_URL + '/resources/js/taggui/attribute-details',
    //'<a href="' + BASE_URL + '/protected/tagger-home">Tagger</a><span>&nbsp;>&nbsp;Attribute details</span></div>',
    launch: function () {
        Ext.QuickTips.init();
        Ext.create('Ext.container.Viewport', {
            cls: 'mainWraper',
            layout: 'fit',
            items: [
                {
                    xtype: 'container',
                    scrollable: true,
                    layout: {
                        type: 'vbox',
                        align: 'center'
                    },
                    items: [
                        {
                            xtype: 'aidr-header'
                        },
                        {
                            xtype: 'attribute-details-view'
                        },
                        {
                            xtype: 'panel',
                            width: 0,
                            border: false,
                            flex: 1,
                            margin: '0 0 25 0'
                        },
                        {
                            xtype: 'aidr-footer'
                        }
                    ]
                }
            ]
        });
    }
});