Ext.application({
    requires: [
        'Ext.container.Viewport'
    ],

    name: 'TAGGUI.training-examples',

    controllers: [
        'TrainingExamplesController'
    ],

    appFolder: BASE_URL + '/resources/js/taggui/training-examples',

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
                            xtype: 'container',
                            width: '100%',
                            html: '<div class="headerWrapper">' +
                                '<div id="header" class="header"><img class="headeraidrlogo" src="' + BASE_URL + '/resources/img/AIDR/aidr_logo_240x90.png"></div>' +
                                '</div>'
                        },
                        {
                            xtype: 'training-examples-view'
                        },
                        {
                            xtype: 'panel',
                            width: 0,
                            border: false,
                            flex: 1,
                            margin: '0 0 25 0'
                        },
                        {
                            xtype: 'container',
                            width: '100%',
                            html: '<div class="site-footer"><div class="footer"><a style="text-decoration: none;color: #ffffff" href="http://www.qcri.qa/">A project by <img align="middle" id="footerqcrilogo" src="' + BASE_URL + '/resources/img/qcri-gray-horiz.png"></a></div>' +
                                '</div>'
                        }
                    ]
                }
            ]
        });
    }
});