Ext.application({
    requires: [
        'Ext.container.Viewport'
    ],

    name: 'AIDRPUBLIC.home',

    controllers: [
        'PublicHomeController'
    ],

    appFolder: BASE_URL + '/resources/js/aidrpublic/home',

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
                            xtype: 'aidrpublic-home-view'
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