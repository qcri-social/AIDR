Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
    ]);

Ext.define('AIDRFM.home.view.NewCollectionPanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.collection-view-new',

    initComponent: function () {
        var me = this,
        whiteLoadingMaskCfg = {
            maskCls: 'white-loading-mask x-mask'
        };

        this.taggerTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'My Collections',
            flex: 1
        });

        this.collectionDescription = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            margin: '7 0 0 0',
            text: 'Status as of ' + Ext.Date.format(new Date(), 'F j, Y, g:i:s A'),
            flex: 1
        });
         this.newCollectionButton = Ext.create('Ext.Button', {
            text: 'Create Collection',
            margin: '6 0 0 15',
            cls:'btn btn-blueish1',
            id: 'newCollection'
        });

        this.goToAdminSection = Ext.create('Ext.Button', {
            text: 'Admin Panel',
            margin: '6 0 0 15',
            cls:'btn btn-blueish1',
            id: 'goToAdminSection',
            hidden: true
        });

       

//        this.manageCrisisButton = Ext.create('Ext.Button', {
//            text: 'My Classifiers',
//            margin: '6 0 0 15',
//            cls:'btn btn-blue',
//            id: 'manageCrisis'
//        });

this.refreshButton = Ext.create('Ext.Button', {
    // text: null,
    // height: 32,
    // width: 32,
    margin: '27 395 0 20',
    tooltip: 'Refresh',
    // iconCls: 'refrashIcon',
     cls:'btn btn-blueish4',

    id: 'refreshBtn'
});

this.collectionStore = Ext.create('Ext.data.JsonStore', {
    pageSize: 10,
    storeId: 'collectionStore',
    fields: ['id', 'code', 'name', 'target', 'langFilters', 'startDate', 'endDate', 'status', 'count', 'track', 'geo', 'follow', 'lastDocument', 'user', 'collectionType', 'classifiersNumber', 'crisisType'],
    proxy: {
        type: 'ajax',
        url: 'protected/collection/findAll.action',
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
                         document.location.href = BASE_URL + '/protected/home';
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

    '<tpl if="classifiersNumber != null">',
    '<button id="buttonGoToClassifiers_{id}" class="btn btn-blueish2" onclick="document.location.href=\'{[this.getEncodedCode(values.code)]}/tagger-collection-details\'">',
    '<span>Classifier ({classifiersNumber})</span>',
    '</button>',
    '<tpl else>',
    '<button id="buttonEnableClassifiers_{id}" class="btn btn-blueish3 {[this.isEnableClassifierButtonDisabled(values.status)]}" onclick="collectionController.enableTagger({crisisType.id}, \'{code}\',\'{name}\');" {[this.isEnableClassifierButtonDisabled(values.status)]}>',
    '<span>Enable Classifier</span>',
    '</button>',
    '</tpl>',

    '<button id="buttonCollector_{id}" class="btn btn-blueish2" style="margin-right: 10px" onclick="document.location.href=\'{[this.getEncodedCode(values.code)]}/collection-details\'">',
    '<span>Collector</span>',
    '</button>',
    '<div class="content">',

    '<div class="img">',
    '<a href="{[this.getEncodedCode(values.code)]}/collection-details">' +
    '<tpl if="this.isTwitter(values.collectionType)">'+
    '<img alt="Collection image" height="70" src="resources/img/collection-icon.png" width="70">' +
    '<tpl else>'+
    '<img alt="Collection image" height="70" src="resources/img/sms_icon.png" width="70">' +
    '</tpl>'+
    '</a>',
    '</div>',

    '<div class="info" style="width: 600px !important;">',
    '<div class="collection-title">' +
                //'<tpl if="classifiersNumber != null">',
                '<a href="{[this.getEncodedCode(values.code)]}/interactive-view-download">{name}</a>' +
                //'<tpl else>',
                    //'<span>{name}</span>',
                //'</tpl>',
                '{[this.getSharedBy(values.user)]}</div>',

                '<div class="styled-text-14" id="statusField_{id}">{[this.getStatus(values.status, values.collectionType)]}' +
                '<tpl if="values.status == \'RUNNING\' || values.status == \'RUNNING_WARNING\'">'+
                '&nbsp;&#45;&nbsp; {[this.getDocNumber(values.count)]} {[COLLECTION_TYPES[values.collectionType]["plural"]]} collected (since last restart)' +
                '</tpl>',
                '</div>',
                '<tpl if="values.status == \'RUNNING\' || values.status == \'RUNNING_WARNING\'">',
                '<div class="styled-text-14" id="lastDocField_{id}">Last collected:&nbsp;&nbsp;&nbsp;{[this.getLastDoc(values.lastDocument)]}</div>',
                '</tpl>',
                '</div>',

                '</div>',
                '</div>',

                '</tpl>',
                '</div>',
                {
                    isEnableClassifierButtonDisabled: function(r){
                        if (r == 'RUNNING_WARNING' || r == 'RUNNING' || r == 'INITIALIZING' || r == 'WARNING'){
                            return "";
                        } else {
                            return "disabled";
                        }
                    },
                    getStatus: function (raw, collectionType) {
                        return AIDRFMFunctions.getStatusWithStyle(raw, collectionType);
                    },
                    getLastDoc: function (r) {
                        return r ? '<span class="tweet">' + r + '</span>' : "<span class='na-text'>N/A</span>";
                    },
                    getDocNumber: function (r) {
                        return r ? Ext.util.Format.number(r,'0,000') : 0;
                    },
                    isButtonStartHidden: function (r) {
                        if (r == 'RUNNING_WARNING' || r == 'RUNNING' || r == 'INITIALIZING' || r == 'WARNING'){
                            return 'hidden';
                        } else {
                            return '';
                        }
                    },
                    isButtonStopHidden: function (r) {
                        if (r == 'RUNNING_WARNING' || r == 'RUNNING' || r == 'INITIALIZING' || r == 'WARNING'){
                            return '';
                        } else {
                            return 'hidden';
                        }
                    },
                    isTwitter: function (r) {
                        return r == 'Twitter';
                    },
                    getEncodedCode: function(code) {
                       URIString = BASE_URL + '/protected/' + code;
                       return encodeURI(URIString);
                   },
                   getSharedBy: function(owner) {
                    if (owner.userName == USER_NAME){
                        return '';
                    }
                    return '<span class="styled-text-14">  by &#64;' + owner.userName + '</span>';
                }
            }
            );


this.collectionView = Ext.create('Ext.view.View', {
    store: this.collectionStore,
    tpl: this.collectionTpl,
    itemSelector: 'div.active',
    loadMask: whiteLoadingMaskCfg

});

this.collectionPaging = Ext.create('Ext.toolbar.Paging', {
    cls: 'aidr-paging',
    margin: '12 2 0 2',
    store:'collectionStore',
    displayInfo: true,
    displayMsg:'Collections {0} - {1} of {2}',
    emptyMsg:'No collection records to display'
});

this.collectionTrashedStore = Ext.create('Ext.data.JsonStore', {
    pageSize: 3,
    storeId: 'collectionTrashedStore',
    fields: ['id', 'code', 'name', 'target', 'langFilters', 'startDate', 'endDate', 'status', 'count', 'track', 'geo', 'follow', 'lastDocument', 'user', 'collectionType'],
    proxy: {
        type: 'ajax',
        url: 'protected/collection/findAll.action',
        reader: {
            root: 'data',
            totalProperty: 'total'
        }
    },
    autoLoad: true,
    listeners: {
        beforeload: function (s) {
            s.getProxy().setExtraParams ({
                trashed: "yes"
            });
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
     //'<button id="buttonStart_{id}" class="btn btn-green" onclick="collectionController.untrashCollection({id}, \'{code}\')">',
     //'<span>Recover from Trash Bin</span>',
    // '</button>',
     '<div class="content">',

    '<div class="img">',
    '<a href="{[this.getEncodedCode(values.code)]}/collection-details">' +
    '<tpl if="this.isTwitter(values.collectionType)">'+
    '<img alt="Collection image" height="70" src="resources/img/collection-icon.png" width="70">' +
    '<tpl else>'+
    '<img alt="Collection image" height="70" src="resources/img/sms_icon.png" width="70">' +
    '</tpl>'+
    '</a>',
    '</div>',

    '<div class="info" style="width: 600px !important;">',
    '<div class="collection-title"><a href="{[this.getEncodedCode(values.code)]}/collection-details">{name}</a>{[this.getSharedBy(values.user)]}</div>',
    '<div class="styled-text-14" id="statusField_{id}">{[this.getStatus(values.status)]}</div>',
           // '<div class="styled-text-14" id="docCountField_{id}">Downloaded {[COLLECTION_TYPES[values.collectionType]["plural"]]} (since last re-start):&nbsp;&nbsp;&nbsp;{[this.getDocNumber(values.count)]}</div>',
           // '<div class="styled-text-14" id="lastDocField_{id}">Last downloaded {[COLLECTION_TYPES[values.collectionType]["singular"]]}:&nbsp;&nbsp;&nbsp;{[this.getLastDoc(values.lastDocument)]}</div>',
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
        URIString = BASE_URL + '/protected/' + code;
        return encodeURI(URIString);
    },
    isTwitter: function (r) {
        return r == 'Twitter';
    },
    getSharedBy: function(owner) {
        if (owner.userName == USER_NAME){
            return '';
        }
        return '<span class="styled-text-14"> by &#64;' + owner.userName + '</span>';
    }
}
);

this.collectionTrashedView = Ext.create('Ext.view.View', {
    store: this.collectionTrashedStore,
    tpl: this.collectionTrashedTpl,
    itemSelector: 'div.active',
    loadMask: whiteLoadingMaskCfg
});

this.collectionTrashedPaging = Ext.create('Ext.toolbar.Paging', {
    cls: 'aidr-paging',
    margin: '12 2 0 2',
    store:'collectionTrashedStore',
    displayInfo: true,
    displayMsg:'Collections {0} - {1} of {2}',
    emptyMsg:'No collection records to display'
});

this.tabs =  Ext.create('Ext.tab.Panel', {
    cls: 'tabPanel',
    id:'collectionsTabs',
    plain: true,
    listeners: {
        tabchange: function( tabPanel, newCard, oldCard, eOpts ){
            if(newCard.itemId == "trashedCollection"){
                me.refreshButton.hide();
            } else {
                me.refreshButton.show();
            }
        }
    },
    items: [
    {
        title:'My Collections',
        height: '500',
        itemId: "myCollectionTab",
        id: "myCollectionTab",
        items:[
        {
            xtype: "container",
            margin: "0 0"
        },
        this.collectionView,
        this.collectionPaging
        ]
    },

    {
        title:'Archived',
        itemId:'trashedCollection',
        items: [
        {
            xtype: "container",
            margin: "0 0"
        },

        this.collectionTrashedView,
        this.collectionTrashedPaging
        ]
    }

    ]
});
        /*
        *  "Browse public collections" is currently http://aidr-prod.qcri.org/AIDRFetchManager/public.jsp
        * */
        this.browseCollectionLink = "";

        this.items = [
        {
            xtype: 'container',
            layout: 'hbox',
            margin: '5 0 0 0',
            items: [
             
            this.taggerTitle,
            this.refreshButton,
            this.newCollectionButton,
              this.goToAdminSection
            
            ]
        }, {
            xtype: 'container',
            layout: 'hbox',
            margin: '0 0 40 0',
            items: [
            this.collectionDescription,
            // this.refreshButton
            ]
        },
        me.tabs,
        {
            xtype: 'container',
            layout: 'hbox',
            items: [
            {
                xtype:'container',
                flex:1
            },
            {
                xtype: 'component',
                margin: "15 0 0 5",
                cls: 'link-style',
                html:"<a href='" + BASE_URL + "/public.jsp'>Browse public collections</a>"
            }
            ]
        }

        ]

        this.callParent(arguments);
    }

});