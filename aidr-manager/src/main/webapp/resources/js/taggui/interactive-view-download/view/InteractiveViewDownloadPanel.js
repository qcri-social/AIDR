Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer',
    'Ext.ux.data.PagingMemoryProxy'
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

        this.taggerFetchStore = Ext.create('Ext.data.Store', {
            pageSize: 10,
            storeId: 'taggerFetchStore',
            fields: ['text', 'attribute_name', 'label_name', 'confidence']
        });

        this.taggerFetchGrid = Ext.create('Ext.grid.Panel', {
            store: this.taggerFetchStore,
            itemId: 'taggerFetchGrid',
            cls: 'aidr-grid',
            columns: [
                {
                    xtype: 'gridcolumn', dataIndex: 'text', text: 'Tweet', flex: 1
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'attribute_name', text: 'Attribute', width: 150
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'label_name', text: 'Label', width: 150
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'confidence', text: 'Confidence', width: 150
                }
            ]
        });

        this.taggerFetchPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'taggerFetchStore',
            displayInfo:true,
            displayMsg:'Collection history records {0} - {1} of {2}',
            emptyMsg:'No collection history records to display',
            items: [
                {
                    xtype: 'tbseparator'
                },
                {
                    xtype : 'trigger',
                    itemId : 'gridTrigger',
                    fieldLabel: 'Filter Grid Data',
                    triggerCls : 'x-form-clear-trigger',
                    emptyText : 'Start typing to filter data',
                    size : 30,
                    minChars : 1,
                    enableKeyEvents : true,
                    onTriggerClick : function(){
                        this.reset();
                        this.fireEvent('triggerClear');
                    }
                }
            ]
        });

        this.taggerFetchPanel = Ext.create('Ext.container.Container', {
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                this.taggerFetchGrid,
                this.taggerFetchPaging
            ]
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
                    this.screenTitle
                ]
            },
            this.taggerFetchPanel
        ];

        this.callParent(arguments);
    }

});