Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
]);

Ext.define('AIDRPUBLIC.home.view.PublicHomePanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.aidrpublic-home-view',

    initComponent: function () {
        var me = this;

        this.pageTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Public Crisis Collection List',
            flex: 1
        });

        this.collectionDescription = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            text: 'Status as of ' + Ext.Date.format(new Date(), 'F j, Y, g:i:s A')
        });

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs"><a href="http://adir.qcri.org">Home</a></div>',
            margin: 0,
            padding: 0
        });

        this.stoppedCollectionStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 10,
            storeId: 'stoppedCollectionStore',
            fields: ['id', 'code', 'name', 'startDate', 'endDate', 'createdDate','status', 'crisisType', 'user','crisisTypeName'],
            proxy: {
                type: 'ajax',
                url: 'public/collection/findAllStoped.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            listeners: {
                load: function (store, records, successful, operation, eOpts) {
                    var count = store.getCount();

                    if (count > 0) {
                        me.stoppedCollectionPaging.show();
                    } else {
                        me.stoppedCollectionPaging.hide();
                    }
                }
            }
        });

        this.collectionStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 10,
            storeId: 'collectionStore',
            fields: ['id', 'code', 'name', 'startDate', 'endDate', 'createdDate','status', 'crisisType', 'user','crisisTypeName'],
            proxy: {
                type: 'ajax',
                url: 'public/collection/findAllRunning.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            listeners: {
                load: function (store, records, successful, operation, eOpts) {
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

            '<div class="content">',

            '<div class="img">',
            '<a href="http://aidr-dev.qcri.org/AIDROutput/aidrTaggerLatest.html?crisisCode={[this.getEncodedCode(values.code)]}"><img alt="Collection image" height="70" src="/AIDRFetchManager/resources/img/collection-icon.png" width="70"></a>',
            '</div>',
            '<div class="info">',
            '<div class="collection-title"><a href="http://aidr-dev.qcri.org/AIDROutput/aidrTaggerLatest.html?crisisCode={[this.getEncodedCode(values.code)]}">{name}</a></div>',
            '<div class="styled-text-14" id="statusField_{id}">{[this.getCrisisType(values.crisisTypeName)]} &nbsp; {[this.getSharedBy(values.user)]}</div>',
            '<div class="styled-text-14" id="docCountField_{id}">{[this.getDateTimeSet(values.startDate, values.status)]}</div>',
            '</div>',

            '</div>',
            '</div>',

            '</tpl>',
            '</div>',
            {
                getStatus: function (raw) {
                    return AIDRFMFunctions.getStatusWithStyle(raw);
                },
                getCrisisType: function (r) {
                    return r ? '<span class="styled-text-14"> Crisis Type: ' + r + '</span>' : "<span class='styled-text-14'>Crisis Type: Not specified</span>";
                },
                getEncodedCode: function(code) {
                    return encodeURI(code);
                },
                getSharedBy: function(owner) {
                    return '<span class="styled-text-14"> Curator: &#64;' + owner.userName + '</span>';
                } ,
                getDateTimeSet: function(value, statusValue){
                    //console.log("statusValue" + statusValue) ;
                    //console.log("value" + value) ;
                    if (value) {
                        if (statusValue == 'RUNNING') {
                            return '<span class="styled-text-14"> Last started: ' + moment(value).calendar()+ '</span>';
                        }
                        else{
                            return '<span class="styled-text-14"> Stopped: ' + moment(value).calendar()+ '</span>';
                        }

                    } else {
                        return me.getField(false);
                    }
                }
            }
        );

        this.collectionView = Ext.create('Ext.view.View', {
            store: this.collectionStore,
            tpl: this.collectionTpl,
            itemSelector: 'div.active'
        });

        this.stoppedCollectionView = Ext.create('Ext.view.View', {
            store: this.stoppedCollectionStore,
            tpl: this.collectionTpl,
            itemSelector: 'div.active'
        });


        this.collectionPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'collectionStore',
            displayInfo: true,
            displayMsg:'Running crisis collection records {0} - {1} of {2}',
            emptyMsg:'No running crisis collection records to display'
        });

        this.stoppedCollectionPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'stoppedCollectionStore',
            displayInfo: true,
            displayMsg:'Stopped crisis collection records {0} - {1} of {2}',
            emptyMsg:'No stopped crisis collection records to display'
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
                        this.collectionView,
                        this.collectionPaging
                    ]
                },
                {
                    title: 'Stopped collections',
                    items: [
                        this.stoppedCollectionView,
                        this.stoppedCollectionPaging
                    ]
                }
            ]
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
                        margin: '0 0 0 0',
                        items: [
                            this.pageTitle,
                            this.collectionDescription
                        ]
                    }
                ]
            },
            this.tabPanel
        ];


        this.callParent(arguments);
    }
});