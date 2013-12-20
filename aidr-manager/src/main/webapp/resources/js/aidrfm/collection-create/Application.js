Ext.application({
    requires: [
        'Ext.container.Viewport'
    ],

    name: 'AIDRFM.collection-create',

    controllers: [
        'CollectionCreateController'
    ],

    appFolder: BASE_URL + '/resources/js/aidrfm/collection-create',

    launch: function () {
        Ext.QuickTips.init();
        Ext.create('Ext.container.Viewport', {
            cls: 'mainWraper',
            layout: 'fit',
            items: [
                {
                    xtype: 'container',
                    autoScroll: true,
                    layout: {
                        type: 'vbox',
                        align: 'center'
                    },
                    items: [
                        {
                            xtype: 'aidr-header'
                        },
                        {
                            xtype: 'collection-create'
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