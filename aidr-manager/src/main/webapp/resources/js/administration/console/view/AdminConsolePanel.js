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
            id: 'toSystemHealthButtonId'
        });

        this.runningCollectionsStore = Ext.create('Ext.data.Store', {
            pageSize: 20,
            storeId: 'runningCollectionsStore',
            fields: ['id', 'name', 'code', 'count', 'startDate', 'user'],
            remoteSort: true,
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/collection/getAllRunning.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                },
                simpleSortMode: true,
                sortParam: 'sortColumn',
                directionParam: 'sortDirection'
            },
            autoLoad: true,
            listeners: {
                beforeload: function (s) {
                    var terms = '';

                    if (me.runningCollectionsSearchField.getValue()) {
                        terms =  me.runningCollectionsSearchField.getValue();
                    }

                    s.getProxy().extraParams = {
                        terms: terms
                    };
                }
            }
        });

        this.runningCollectionsGrid = Ext.create('Ext.grid.Panel', {
            flex:1,
            store: this.runningCollectionsStore,
            cls: 'aidr-grid',
            columns: [
                {
                    xtype: 'gridcolumn', dataIndex: 'name', text: 'Collection', flex: 1,
                    renderer: function (value, meta, record) {
                        return me.getCollectionNameAsLink(value, record.data.code);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'user', text: 'User', width: 200,
                    renderer: function (value, meta, record) {
                        if (value.userName){
                            return value.userName;
                        }
                        return '';
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'startDate', text: 'Last re-started on', width: 150,
                    renderer: function (value, meta, record) {
                        return me.getField(value);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'name', text: 'Collected tweets', width: 150,
                    renderer: function (value, meta, record) {
                        return '';
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'name', text: 'Taggers', width: 100,
                    renderer: function (value, meta, record) {
                        return '';
                    }
                }
            ]
        });

        this.runningCollectionsPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'runningCollectionsStore',
            displayInfo:true,
            displayMsg:'Running Collections {0} - {1} of {2}',
            emptyMsg:'No Running Collections to display'
        });

        this.runningCollectionsSearchField = Ext.create('Ext.form.field.Text', {
            id: 'runningCollectionsSearchFieldId',
            width: 250,
            height: 25,
            emptyText: 'Search...'
        });

        this.runningCollectionsSearchButton = Ext.create('Ext.Button', {
            margin: '0 0 0 10',
            text: 'Search',
            cls: 'btn btn-blue',
            id: 'runningCollectionsSearchButtonId'
        });

        this.stoppedCollectionsSearchField = Ext.create('Ext.form.field.Text', {
            id: 'stoppedCollectionsSearchFieldId',
            width: 250,
            height: 25,
            emptyText: 'Search...'
        });

        this.stoppedCollectionsSearchButton = Ext.create('Ext.Button', {
            margin: '0 0 0 10',
            text: 'Search',
            cls: 'btn btn-blue',
            id: 'stoppedCollectionsSearchButtonId'
        });

        this.stoppedCollectionsStore = Ext.create('Ext.data.Store', {
            pageSize: 20,
            storeId: 'stoppedCollectionsStore',
            fields: ['id', 'name', 'code', 'count', 'startDate', 'user'],
            remoteSort: true,
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/collection/getAllStopped.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                },
                simpleSortMode: true,
                sortParam: 'sortColumn',
                directionParam: 'sortDirection'
            },
            autoLoad: true,
            listeners: {
                beforeload: function (s) {
                    var terms = '';

                    if (me.stoppedCollectionsSearchField.getValue()) {
                        terms =  me.stoppedCollectionsSearchField.getValue();
                    }

                    s.getProxy().extraParams = {
                        terms: terms
                    };
                }
            }
        });

        this.stoppedCollectionsGrid = Ext.create('Ext.grid.Panel', {
            flex:1,
            store: this.stoppedCollectionsStore,
            cls: 'aidr-grid',
            columns: [
                {
                    xtype: 'gridcolumn', dataIndex: 'name', text: 'Collection', flex: 1,
                    renderer: function (value, meta, record) {
                        return me.getCollectionNameAsLink(value, record.data.code);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'user', text: 'User', width: 200,
                    renderer: function (value, meta, record) {
                        if (value.userName){
                            return value.userName;
                        }
                        return '';
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'startDate', text: 'Last re-started on', width: 150,
                    renderer: function (value, meta, record) {
                        return me.getField(value);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'name', text: 'Collected tweets', width: 150,
                    renderer: function (value, meta, record) {
                        return '';
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'name', text: 'Taggers', width: 100,
                    renderer: function (value, meta, record) {
                        return '';
                    }
                }
            ]
        });

        this.stoppedCollectionsPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'stoppedCollectionsStore',
            displayInfo:true,
            displayMsg:'Stopped Collections {0} - {1} of {2}',
            emptyMsg:'No Stopped Collections to display'
        });

        this.tabPanel = Ext.create('Ext.tab.Panel', {
            cls: 'tabPanel',
            width: '100%',
            minHeight: 400,
            activeTab: 0,
            items: [
                {
                    title: 'Running collections',
                    items: [
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '20 0',
                            items: [
                                this.runningCollectionsSearchField,
                                this.runningCollectionsSearchButton
                            ]
                        },
                        this.runningCollectionsGrid,
                        this.runningCollectionsPaging
                    ]
                },
                {
                    title: 'Stopped collections',
                    items: [
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '20 0',
                            items: [
                                this.stoppedCollectionsSearchField,
                                this.stoppedCollectionsSearchButton
                            ]
                        },
                        this.stoppedCollectionsGrid,
                        this.stoppedCollectionsPaging
                    ]
                }
            ]
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
            },
            this.tabPanel
        ];

        this.callParent(arguments);
    },

    getCollectionNameAsLink: function (name, code) {
        if (name && code) {
            return '<a href="' + BASE_URL + '/protected/' + code + '/collection-details">' + name + '</a>';
        } else {
            return '<span class="na-text">Not specified</span>';
        }
    },

    getField: function (r) {
        return r ? r : "<span class='na-text'>Not specified</span>";
    }

});