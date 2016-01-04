Ext.application({
    requires: [
        'Ext.container.Viewport'
    ],

    name: 'AIDRFM.home',

    controllers: [
        'CollectionController'
    ],

    appFolder: BASE_URL + '/resources/js/aidrfm/home',

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
//                            xtype: 'collection-view'
                            xtype: 'collection-view-new'
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