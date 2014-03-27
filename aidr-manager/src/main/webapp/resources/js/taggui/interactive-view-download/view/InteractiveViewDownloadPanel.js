Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
]);

Ext.define('TAGGUI.interactive-view-download.view.InteractiveViewDownloadPanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.interactive-view-download-view',

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.form.Label', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/tagger-home">My Classifiers</a><span>&nbsp;>&nbsp;</span>' +
                '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">' + CRISIS_NAME + '</a>' +
                '<span>&nbsp;>&nbsp;Interactive View/Download</span></div>',
            margin: 0,
            padding: 0
        });

        this.screenTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Interactive View/Download for collection "' + CRISIS_NAME + '"',
            flex: 1
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
                    align: 'stretch',
                    padding: '0 0 10 0'
                },
                items: [
                    this.screenTitle
                ]
            }
        ];

        this.callParent(arguments);
    }

});