Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
]);

Ext.define('ADMIN.console.view.AdminConsolePanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.admin-console-view',

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/home">AIDR</a><span>&nbsp;&gt;&nbsp;Administrator console</span></div>',
            margin: 0,
            padding: 0
        });

        this.pageTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Administrator console',
            flex: 1
        });

        this.toSystemHealthButton = Ext.create('Ext.Button', {
            text: 'Go to System Health',
            margin: '27 0 0 15',
            cls: 'btn btn-blue',
            id: 'toSystemHealthButton'
        });

        this.items = [
            this.breadcrumbs,
            {
                xtype: 'container',
                margin: '5 0 5 0',
                html: '<div class="horizontalLine"></div>'
            },
            {
                xtype: 'container',
                layout: 'hbox',
                margin: '5 0',
                items: [
                    this.pageTitle,
                    this.toSystemHealthButton
                ]
            }
        ];

        this.callParent(arguments);
    }

});