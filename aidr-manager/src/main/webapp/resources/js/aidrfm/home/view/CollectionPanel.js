Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
]);

Ext.define('AIDRFM.home.view.CollectionPanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.collection-view',

    initComponent: function () {
        var me = this;

        this.collectionDescription = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            margin: '7 0 0 0',
            text: 'Status as of ' + Ext.Date.format(new Date(), 'F j, Y, g:i:s A'),
            flex: 1
        });

        this.goToAdminSection = Ext.create('Ext.Button', {
            text: 'Administrator console',
            margin: '0 0 0 15',
            cls:'btn btn-blue',
            id: 'goToAdminSection',
            hidden: true
        });

        this.newCollectionButton = Ext.create('Ext.Button', {
            text: 'Create New Collection',
            margin: '0 0 0 15',
            cls:'btn btn-blue',
            id: 'newCollection'
        });

        this.manageCrisisButton = Ext.create('Ext.Button', {
            text: 'My Classifiers',
            margin: '0 0 0 15',
            cls:'btn btn-blue',
            id: 'manageCrisis'
        });

        this.refreshButton = Ext.create('Ext.Button', {
            text: null,
            height: 32,
            width: 32,
            tooltip: 'Refresh',
            iconCls: 'refrashIcon',
            id: 'refreshBtn'
        });

        this.collectionStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 10,
            storeId: 'collectionStore',
            fields: ['id', 'code', 'name', 'target', 'langFilters', 'startDate', 'endDate', 'status', 'count', 'track', 'geo', 'follow', 'lastDocument', 'user'],
            proxy: {
                type: 'ajax',
                url: 'collection/findAll.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            listeners: {
                load: function (store, records, successful, operation, eOpts) {
                    if (successful) {
                        collectionController.updateLastRefreshDate();
                    } else {
                        /*
                         * user is not authenticated,
                         * redirecting to "home page" (which will redirect to "connect to twitter page" )
                         *
                         */
                        document.location.href = BASE_URL + '/protected/home'
                    }
                    var count = store.getCount();

                    if (count > 0) {
                        me.collectionPaging.show();
                    } else {
                        me.collectionPaging.hide();
                    }
                }
            }
        });

        this.collectionTpl = new Ext.XTemplate(
            '<div class="collections-list">',
            '<tpl for=".">',

            '<div class="collection-item">',

            '<button id="buttonStart_{id}" class="btn btn-green {[this.isButtonStartHidden(values.status)]}" onclick="collectionController.startCollectionCheck({id}, \'{name}\', \'{user.userName}\', {user.id})">',
            '<span>Start</span>',
            '</button>',
            '<button id="buttonStop_{id}" class="btn btn-red {[this.isButtonStopHidden(values.status)]}" onclick="collectionController.stopCollection({id})">',
                '<span>Stop</span>',
            '</button>',

            '<div class="content">',

            '<div class="img">',
            '<a href="{[this.getEncodedCode(values.code)]}/collection-details"><img alt="Collection image" height="70" src="/AIDRFetchManager/resources/img/collection-icon.png" width="70"></a>',
            '</div>',

            '<div class="info">',
            '<div class="collection-title"><a href="{[this.getEncodedCode(values.code)]}/collection-details">{name}</a>{[this.getSharedBy(values.user)]}</div>',
            '<div class="styled-text-14" id="statusField_{id}">{[this.getStatus(values.status)]}</div>',
            '<div class="styled-text-14" id="docCountField_{id}">Downloaded items (since last re-start):&nbsp;&nbsp;&nbsp;{[this.getDocNumber(values.count)]}</div>',
            '<div class="styled-text-14" id="lastDocField_{id}">Last downloaded item:&nbsp;&nbsp;&nbsp;{[this.getLastDoc(values.lastDocument)]}</div>',
            '</div>',

            '</div>',
            '</div>',

            '</tpl>',
            '</div>',
            {
                getStatus: function (raw) {
                    return AIDRFMFunctions.getStatusWithStyle(raw);
                },
                getLastDoc: function (r) {
                    return r ? '<span class="tweet">' + r + '</span>' : "<span class='na-text'>N/A</span>";
                },
                getDocNumber: function (r) {
                    return r ? Ext.util.Format.number(r,'0,000') : 0;
                },
                isButtonStartHidden: function (r) {
                    if (r == 'RUNNING-WARNNING' || r == 'RUNNING' || r == 'INITIALIZING'){
                        return 'hidden';
                    } else {
                        return '';
                    }
                },
                isButtonStopHidden: function (r) {
                    if (r == 'RUNNING-WARNNING' || r == 'RUNNING' || r == 'INITIALIZING'){
                        return '';
                    } else {
                        return 'hidden';
                    }
                },
                getEncodedCode: function(code) {
                    return encodeURI(code);
                },
                getSharedBy: function(owner) {
                    if (owner.userName == USER_NAME){
                        return '';
                    }
                    return '<span class="styled-text-14"> (Shared by &#45; &#64;' + owner.userName + ')</span>';
                }
            }
        );

        this.collectionView = Ext.create('Ext.view.View', {
            store: this.collectionStore,
            tpl: this.collectionTpl,
            itemSelector: 'div.active'
        });

        this.collectionPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'collectionStore',
            displayInfo: true,
            displayMsg:'Collection records {0} - {1} of {2}',
            emptyMsg:'No collection records to display'
        });

        this.collectionTrashedStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 3,
            storeId: 'collectionTrashedStore',
            fields: ['id', 'code', 'name', 'target', 'langFilters', 'startDate', 'endDate', 'status', 'count', 'track', 'geo', 'follow', 'lastDocument', 'user'],
            proxy: {
                type: 'ajax',
                url: 'collection/findAll.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            listeners: {
                beforeload: function (s) {
                    s.getProxy().extraParams = {
                        trashed: "yes"
                    };
                },
                load: function (store, records, successful, operation, eOpts) {
                    var count = store.getCount();
                    if (count > 0) {
                        Ext.getCmp('collectionsTabs').child('#trashedCollection').tab.show();
                        me.collectionTrashedView.show();
                        me.collectionTrashedPaging.show();
                    } else {
                        Ext.getCmp('collectionsTabs').child('#trashedCollection').tab.hide();
                        me.collectionTrashedView.hide();
                        me.collectionTrashedPaging.hide();
                    }
                }
            }
        });

        this.collectionTrashedTpl = new Ext.XTemplate(
            '<div class="collections-list">',
            '<tpl for=".">',

            '<div class="collection-item">',
            '<div class="content">',

            '<div class="img">',
            '<a href="{[this.getEncodedCode(values.code)]}/collection-details"><img alt="Collection image" height="70" src="/AIDRFetchManager/resources/img/collection-icon.png" width="70"></a>',
            '</div>',

            '<div class="info">',
            '<div class="collection-title"><a href="{[this.getEncodedCode(values.code)]}/collection-details">{name}</a>{[this.getSharedBy(values.user)]}</div>',
            '<div class="styled-text-14" id="statusField_{id}">{[this.getStatus(values.status)]}</div>',
            '<div class="styled-text-14" id="docCountField_{id}">Downloaded items (since last re-start):&nbsp;&nbsp;&nbsp;{[this.getDocNumber(values.count)]}</div>',
            '<div class="styled-text-14" id="lastDocField_{id}">Last downloaded item:&nbsp;&nbsp;&nbsp;{[this.getLastDoc(values.lastDocument)]}</div>',
            '</div>',

            '</div>',
            '</div>',

            '</tpl>',
            '</div>',
            {
                getStatus: function (raw) {
                    return AIDRFMFunctions.getStatusWithStyle(raw);
                },
                getLastDoc: function (r) {
                    return r ? '<span class="tweet">' + r + '</span>' : "<span class='na-text'>N/A</span>";
                },
                getDocNumber: function (r) {
                    return r ? Ext.util.Format.number(r,'0,000') : 0;
                },
                getEncodedCode: function(code) {
                    return encodeURI(code);
                },
                getSharedBy: function(owner) {
                    if (owner.userName == USER_NAME){
                        return '';
                    }
                    return '<span class="styled-text-14"> (Shared by &#45; &#64;' + owner.userName + ')</span>';
                }
            }
        );

        this.collectionTrashedView = Ext.create('Ext.view.View', {
            store: this.collectionTrashedStore,
            tpl: this.collectionTrashedTpl,
            itemSelector: 'div.active'
        });

        this.collectionTrashedPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'collectionTrashedStore',
            displayInfo: true,
            displayMsg:'Collection records {0} - {1} of {2}',
            emptyMsg:'No collection records to display'
        });

        this.items = [
            {
                xtype:'tabpanel',
                cls: 'tabPanel',
                id:'collectionsTabs',
                plain: true,

                tabBar: {
                    items: [
                        {
                            xtype: 'tbfill'
                        },
                        this.goToAdminSection,
                        this.newCollectionButton,
                        this.manageCrisisButton
                    ]
                },

                items: [
                    {
                        title:'My Collections',
                        items:[
                            {
                                xtype: 'container',
                                layout: 'hbox',
                                margin: '5 0',
                                items: [
                                    this.collectionDescription,
                                    this.refreshButton
                                ]
                            },
                            {
                                xtype: 'container',
                                width: '100%',
                                html: '<div class="horizontalLine"></div>'
                            },
                            this.collectionView,
                            this.collectionPaging
                        ]
                    },

                    {
                        title:'Trashed Collections',
                        itemId:'trashedCollection',
                        items: [
                            this.collectionTrashedView,
                            this.collectionTrashedPaging
                        ]
                    }
                ]
            }

        ]

        this.callParent(arguments);
    }

});