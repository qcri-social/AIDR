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
            cls: 'btn btn-blueSmall',
            id: 'toSystemHealthButtonId'
        });

        this.runningCollectionsStore = Ext.create('Ext.data.Store', {
            pageSize: 10,
            storeId: 'runningCollectionsStore',
            fields: ['id', 'name', 'code', 'count', 'startDate', 'user', 'totalCount', 'taggersCount', 'durationHours', 'publiclyListed'],
            remoteSort: true,
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/collection/getAllRunning.action',
                reader: {
                    rootProperty: 'data',
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

                    s.getProxy().setExtraParams({
                        terms: terms
                    });
                }
            },
            sorters: [
                {
                    property: 'startDate',
                    direction: 'DESC'
                }
            ]
        });

        this.durationStore = Ext.create('Ext.data.Store', {
            fields: ['val', 'label'],
            storeId: 'durationStore',
            data : [
                { "val": 12, "label": '12 hours' },
                { "val": 24, "label": '1 day' },
                { "val": 36, "label": '1 day 12 hours' },
                { "val": 48, "label": '2 days'},
                { "val": 60, "label": '2 days 12 hours' },
                { "val": 72, "label": '3 days' },
                { "val": 168, "label": '7 days' },
                { "val": 336, "label": '14 days' },
                { "val": 720, "label": '30 days' },
                { "val": 1440, "label": '60 days' }
            ]
        });

        this.runningCollectionsGrid = Ext.create('Ext.grid.Panel', {
            flex:1,
            store: this.runningCollectionsStore,
            cls: 'aidr-grid',
            columns: [
                {
                    xtype: 'gridcolumn', dataIndex: 'name', text: 'Name', flex: 1,
                    renderer: function (value, meta, record) {
                        return me.getCollectionNameAsLink(value, record.data.code);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'user', text: 'User', width: 160,
                    renderer: function (value, meta, record) {
                        if (value.userName){
                            return value.userName;
                        }
                        return '';
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'startDate', text: 'Last started', width: 120,
                    renderer: function (value, meta, record) {
                        if (value) {
                            return moment(value).calendar();
                        } else {
                            return me.getField(false);
                        }
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'startDate', text: 'Scheduled stop', width: 120,
                    renderer: function (value, meta, record) {
                        var duration = record.data.durationHours;

                        var willEndDate = moment(value);
                        willEndDate.add('h', duration);

//                      round to the next hour
                        willEndDate.second(0);
                        if (willEndDate.minute() > 0) {
                            willEndDate.add('h', 1);
                            willEndDate.minute(0);
                        }

                        willEndDate = moment(willEndDate).calendar();
                        return me.getField(willEndDate);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'totalCount', text: 'Items', width: 80, sortable: false,
                    renderer: function (value, meta, record) {
                        meta.style = "float:right; padding-top: 9px;";
                        return value ? Ext.util.Format.number(value, '0,000') : 0;
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'taggersCount', text: 'Links', width: 80, sortable: false,
                    renderer: function (value, meta, record) {
                        meta.style = "float:left; padding-left:7px;";
                        var rv = me.getCollectorLink(record.data.code);
                        if (value){
                            var result = Ext.util.Format.number(value, '0,000');
                            rv +=  "</br>" + me.getTaggerLink(value, record.data.code);
                        } else {
                            meta.style += "padding-top: 9px;"
                        }
                        return rv;
                    }
                },
                {
                    text: 'Max Duration',
                    flex: 1,
                    xtype: 'widgetcolumn',
                    widget: {
                        xtype: 'combo',
                        displayField: 'label',
                        valueField: 'val',
                        flex: 1,
                        editable: false,
                        listeners: {
                            select: function (cmp, selectedValues, options) {
                                var selectedVal = selectedValues.data.val;
                                var code = this.getWidgetRecord().data.code;
                                var name = this.getWidgetRecord().data.name;
                                if (selectedVal) {
                                    Ext.Ajax.request({
                                        url: BASE_URL + '/protected/collection/updateDuration.action',
                                        method: 'POST',
                                        params: {
                                            code: code,
                                            duration: selectedVal
                                        },
                                        headers: {
                                            'Accept': 'application/json'
                                        },
                                        success: function (response) {
                                            AIDRFMFunctions.setAlert("Info", "Collection <b>" + name + "</b> new duration has been updated");
                                        }
                                    });
                                }
                            }
                        }
                    },
                    onWidgetAttach: function (column, container, record) {
                        // hide the container by default
                        var combo = container;
                        
                        combo.setStore(Ext.data.StoreManager.lookup('durationStore'));
                        combo.setValue(record.data.durationHours);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'publiclyListed', text: 'Public', width: 50,
                    renderer: function (value, meta, record) {
                        var id = Ext.id();
                        var recordId = record.data.id;
                        var recordName = record.data.name;

                        Ext.defer(function () {
                            var widget = Ext.widget('checkbox', {
                                renderTo: id,
                                margin: '0 0 0 12',
                                checked: value,
                                listeners: {
                                    change: function (el, newValue) {
                                        me.updatePublicListing(newValue, recordId, recordName);
                                    }
                                }
                            });
                        }, 10);

                        return Ext.String.format('<div id="{0}" class="no-padding"></div>', id);
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
            cls: 'btn btn-blueSmall',
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
            cls: 'btn btn-blueSmall',
            id: 'stoppedCollectionsSearchButtonId'
        });

        this.stoppedCollectionsStore = Ext.create('Ext.data.Store', {
            pageSize: 10,
            storeId: 'stoppedCollectionsStore',
            fields: ['id', 'name', 'code', 'count', 'startDate', 'user', 'totalCount', 'taggersCount', 'durationHours', 'publiclyListed'],
            remoteSort: true,
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/collection/getAllStopped.action',
                reader: {
                    rootProperty: 'data',
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

                    s.getProxy().setExtraParams({
                        terms: terms
                    });
                }
            },
            sorters: [
                {
                    property: 'startDate',
                    direction: 'DESC'
                }
            ]
        });

        this.stoppedCollectionsGrid = Ext.create('Ext.grid.Panel', {
            flex:1,
            store: this.stoppedCollectionsStore,
            cls: 'aidr-grid',
            columns: [
                {
                    xtype: 'gridcolumn', dataIndex: 'name', text: 'Name', flex: 1,
                    renderer: function (value, meta, record) {
                        return me.getCollectionNameAsLink(value, record.data.code);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'user', text: 'User', width: 160,
                    renderer: function (value, meta, record) {
                        if (value.userName){
                            return value.userName;
                        }
                        return '';
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'startDate', text: 'Last started', width: 120,
                    renderer: function (value, meta, record) {
                        if (value) {
                            return moment(value).calendar();
                        } else {
                            return me.getField(false);
                        }
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'totalCount', text: 'Items', width: 80, sortable: false,
                    renderer: function (value, meta, record) {
                        meta.style = "float:right; padding-top: 9px;";
                        return value ? Ext.util.Format.number(value, '0,000') : 0;
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'taggersCount', text: 'Links', width: 80, sortable: false,
                    renderer: function (value, meta, record) {
                        meta.style = "float:left; padding-left:7px;";
                        var rv = me.getCollectorLink(record.data.code);
                        if (value){
                            var result = Ext.util.Format.number(value, '0,000');
                            rv +=  "</br>" + me.getTaggerLink(value, record.data.code);
                        } else {
                            meta.style += "padding-top: 9px;"
                        }
                        return rv;
                    }
                },
                {
                    text: 'Max Duration',
                    flex: 1,
                    xtype: 'widgetcolumn',
                    widget: {
                        xtype: 'combo',
                        displayField: 'label',
                        valueField: 'val',
                        flex: 1,
                        editable: false,
                        listeners: {
                            select: function (cmp, selectedValues, options) {
                                var selectedVal = selectedValues.data.val;
                                var code = this.getWidgetRecord().data.code;
                                var name = this.getWidgetRecord().data.name;
                                if (selectedVal) {
                                    Ext.Ajax.request({
                                        url: BASE_URL + '/protected/collection/updateDuration.action',
                                        method: 'POST',
                                        params: {
                                            code: code,
                                            duration: selectedVal
                                        },
                                        headers: {
                                            'Accept': 'application/json'
                                        },
                                        success: function (response) {
                                            AIDRFMFunctions.setAlert("Info", "Collection <b>" + name + "</b> new duration has been updated");
                                        }
                                    });
                                }
                            }
                        }
                    },
                    onWidgetAttach: function (column, container, record) {
                        // hide the container by default
                        var combo = container;
                        
                        combo.setStore(Ext.data.StoreManager.lookup('durationStore'));
                        combo.setValue(record.data.durationHours);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'publiclyListed', text: 'Public', width: 50,
                    renderer: function (value, meta, record) {
                        var id = Ext.id();
                        var recordId = record.data.id;
                        var recordName = record.data.name;

                        Ext.defer(function () {
                            var widget = Ext.widget('checkbox', {
                                renderTo: id,
                                margin: '0 0 0 12',
                                checked: value,
                                listeners: {
                                    change: function (el, newValue) {
                                        me.updatePublicListing(newValue, recordId, recordName);
                                    }
                                }
                            });
                        }, 10);

                        return Ext.String.format('<div id="{0}" class="no-padding"></div>', id);
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

    getCollectionNameAsLink: function (value, code) {
        if (value && code) {
            return '<a href="' + BASE_URL + '/protected/' + code + '/interactive-view-download">' + value + '</a>';
        } else {
            return '<span class="na-text">Not specified</span>';
        }
    },

    getTaggerLink: function (value, code) {
        if (value && code) {
            return '<a href="' + BASE_URL + '/protected/' + code + '/tagger-collection-details">classifier (' + value + ')</a>';
        } else {
            return '<span class="na-text">Not specified</span>';
        }
    },

    getCollectorLink: function (code) {
        if (code) {
            return '<a href="' + BASE_URL + '/protected/' + code + '/collection-details">collector</a>';
        } else {
            return '<span class="na-text">Not specified</span>';
        }
    },

    getField: function (r) {
        return r ? r : "<span class='na-text'>Not specified</span>";
    },

    updatePublicListing: function (value, recordId, recordName) {
        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/updatePublicListing.action',
            method: 'POST',
            params: {
                id: recordId,
                publiclyListed: value
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                AIDRFMFunctions.setAlert("Info", "Collection <b>" + recordName + "</b> Listing status has been updated");
            }
        });
    }

});