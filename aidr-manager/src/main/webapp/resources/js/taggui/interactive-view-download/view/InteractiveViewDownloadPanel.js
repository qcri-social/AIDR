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

        this.applyFilterButton = Ext.create('Ext.Button', {
            text: 'Apply Filter',
            cls:'btn btn-blue',
            id: 'applyFilterButton',
            margin: '10 0 0 0'
        });

        this.filterBlock = Ext.create('Ext.form.FieldSet', {
            title: 'Filter',
            defaultType: 'textfield',
            defaults: {anchor: '100%'},
            layout: 'anchor',
            items: [
                {
                    fieldLabel: 'Field 1',
                    name: 'field1',
                    value: 'This text is just for test'
                },
                {
                    fieldLabel: 'Field 2',
                    name: 'field2',
                    value: 'This text is just for test'
                },
                {
                    xtype: 'container',
                    layout: {
                        type: 'vbox',
                        align: 'right'
                    },
                    items: [
                        this.applyFilterButton
                    ]
                }
            ]
        });

        this.downloadTweetsL = Ext.create('Ext.form.Label', {
            flex: 1,
            text: 'Download selected tweets',
            padding: '0 0 0 0',
            cls: 'header-h2'
        });

        this.contactOwnerL = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            margin: '10 0 0 0',
            html: '',
            flex: 1
        });

        this.downloadType = Ext.create('Ext.form.RadioGroup', {
            columns: 1,
            vertical: true,
            items: [
                {
                    boxLabel: 'Up to 1,000 tweets - as shown above',
                    name: 'rb',
                    inputValue: '1',
                    checked: true
                },
                {
                    boxLabel: 'Latest 100,000 tweets',
                    name: 'rb',
                    inputValue: '2'
                },
                {
                    boxLabel: 'All tweets (tweet-ids only)',
                    name: 'rb',
                    inputValue: '3'
                }
            ]
        });

        this.downloadButton = Ext.create('Ext.Button', {
            text: 'Download as .csv',
            cls:'btn btn-blue',
            id: 'downloadButton',
            margin: '10 0 0 0'
        });

        this.tweetsStore = Ext.create('Ext.data.Store', {
            pageSize: 10,
            storeId: 'tweetsStore',
            fields: ['text', 'attribute_name', 'label_name', 'confidence']
        });

        this.tweetsGrid = Ext.create('Ext.grid.Panel', {
            store: this.tweetsStore,
            itemId: 'tweetsGrid',
            margin: '10 0 0 0',
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

        this.tweetsPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'tweetsStore',
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

        this.tweetsPanel = Ext.create('Ext.container.Container', {
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                this.tweetsGrid,
                this.tweetsPaging
            ]
        });

        this.downloadPanel = Ext.create('Ext.container.Container', {
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                this.downloadTweetsL,
                this.downloadType,
                {
                    xtype: 'container',
                    layout: {
                        type: 'vbox',
                        align: 'left'
                    },
                    items: [
                        this.downloadButton
                    ]
                }
            ]
        });

        this.contactOwnerPanel = Ext.create('Ext.container.Container', {
            hidden: true,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                this.contactOwnerL
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
            this.filterBlock,
            this.tweetsPanel,
            {
                xtype: 'container',
                width: '100%',
                margin: '20 0 0 0',
                html: '<div class="horizontalLine"></div>'
            },
            this.downloadPanel,
            this.contactOwnerPanel
        ];

        this.callParent(arguments);
    }

});