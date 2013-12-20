Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Footer'
]);

Ext.define('AIDRFM.home.view.CollectionPanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.collection-view',

    initComponent: function () {
        var me = this;

        this.collectionTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'My Collections',
            flex: 1
        });

        this.collectionDescription = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            margin: '7 0 0 0',
            text: 'Status as of ' + Ext.Date.format(new Date(), "Y F d h:i:s A"),
            flex: 1
        });

        this.horisontalLine = Ext.create('Ext.container.Container', {
            width: '100%',
            html: '<div class="horisontalLine"></div>'
        });

        this.newCollectionButton = Ext.create('Ext.Button', {
            text: 'Create New Collection',
            margin: '27 0 0 15',
            cls:'btn btn-blue',
            id: 'newCollection'
        });

        this.manageCrisisButton = Ext.create('Ext.Button', {
            text: 'Go to Tagger',
            margin: '27 0 0 15',
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
            fields: ['id', 'code', 'name', 'target', 'langFilters', 'startDate', 'endDate', 'status', 'count', 'track', 'geo', 'follow', 'lastDocument'],
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
                }
            }
        });

        this.collectionTpl = new Ext.XTemplate(
            '<div class="collections-list">',
            '<tpl for=".">',

            '<div class="collection-item">',

            '<button id="buttonStart_{id}" class="btn btn-green {[this.isButtonStartHidden(values.status)]}" onclick="collectionController.startCollectionCheck({id}, \'{name}\')">',
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
            '<div class="collection-title"><a href="{[this.getEncodedCode(values.code)]}/collection-details">{name}</a></div>',
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
                }
            }
        );

        this.collectionView = Ext.create('Ext.view.View', {
            store: this.collectionStore,
            tpl: this.collectionTpl,
            itemSelector: 'div.active',
            loadMask: false
        });

        this.items = [
            {
                xtype: 'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                    {
                        xtype: 'container',
                        layout: 'hbox',
                        margin: '5 0',
                        items: [
                            this.collectionTitle,
                            this.newCollectionButton,
                            this.manageCrisisButton
                        ]
                    },
                    {
                        xtype: 'container',
                        layout: 'hbox',
                        margin: '5 0',
                        items: [
                            this.collectionDescription,
                            this.refreshButton
                        ]
                    }
                ]
            },
            this.horisontalLine,
            this.collectionView
        ];

        this.callParent(arguments);
    }

})