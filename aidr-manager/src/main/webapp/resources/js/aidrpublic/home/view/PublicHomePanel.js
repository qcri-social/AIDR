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
            text: 'Browse Data',
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
            fields: ['id', 'code', 'name', 'startDate', 'endDate', 'createdDate','status', 'crisisType', 'user','crisisTypeName','hasTaggerOutput'],
            proxy: {
                type: 'ajax',
                url: 'public/collection/findAllStoped.action',
                reader: {
                    rootProperty: 'data',
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
            fields: ['id', 'code', 'name', 'startDate', 'endDate', 'createdDate','status', 'crisisType', 'user','crisisTypeName','hasTaggerOutput'],
            proxy: {
                type: 'ajax',
                url: 'public/collection/findAllRunning.action',
                reader: {
                    rootProperty: 'data',
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

        this.runningOnlyCollectionStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 10,
            storeId: 'runningOnlyCollectionStore',
            fields: ['id', 'code', 'name', 'startDate', 'endDate', 'createdDate','status', 'crisisType', 'user','crisisTypeName','hasTaggerOutput'],
            proxy: {
                type: 'ajax',
                url: 'public/collection/findAllRunningWithNoOutput.action',
                reader: {
                    rootProperty: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            listeners: {
                load: function (store, records, successful, operation, eOpts) {
                    var count = store.getCount();

                    if (count > 0) {
                           me.runningOnlyCollectionPaging.show();
                    } else {
                          me.runningOnlyCollectionPaging.hide();
                    }
                }
            }
        });

        this.collectionTpl = new Ext.XTemplate(
            '<div class="collections-list">',
            '<tpl for=".">',

            '<div class="collection-item">',

            '<div class="content">',

            '<div class="img"  style="margin-left: -10px">',
            '{[this.getImageLink(values.code,values.name ,values.hasTaggerOutput)]}',
            '</div>',
            '<div class="info">',
            '<div class="collection-title">{[this.getOutputLink(values.code,values.name ,values.hasTaggerOutput)]}</div>',
            '<div class="styled-text-14" id="statusField_{id}">{[this.getCrisisType(values.crisisType)]} &nbsp; {[this.getSharedBy(values.user)]}</div>',
            '<div class="styled-text-14" id="docCountField_{id}">{[this.getDateTimeSet(values.startDate, values.status)]}</div>',
            '</div>',

            '</div>',
            '</div>',

            '</tpl>',
            '</div>',
            {
                getImageLink: function (rawCode, rawName, hasTaggerOutput) {
                    //<a href="http://aidr-dev.qcri.org/AIDROutput/aidrTaggerLatest.html?crisisCode={[this.getEncodedCode(values.code)]}">{name}</a>
                    if(hasTaggerOutput){
//                        return '<a href="http://aidr-prod.qcri.org/AIDROutput/aidrTaggerLatest.html?crisisCode='+ encodeURI(rawCode) +'"><img alt="Collection image" height="70" src="/AIDRFetchManager/resources/img/collection-icon.png" width="70"></a>';
                        //return Ext.String.format('<a href="{0}"><img alt="Collection image" height="70" src="/AIDRFetchManager/resources/img/collection-icon.png" width="70"></a>', SERVER_URL + '/aidrTaggerLatest.html?crisisCode=' + encodeURI(rawCode) );
                        return Ext.String.format('<a href="{0}"><img alt="Collection image" height="70" src="/AIDRFetchManager/resources/img/collection-icon.png" width="70"></a>', '/AIDRFetchManager/public/' + encodeURI(rawCode) + '/interactive-view-download' );
                    }
                    else{
                        return '<img alt="Collection image" height="70" src="/AIDRFetchManager/resources/img/collection-icon.png" width="70">';
                    }
                    // return AIDRFMFunctions.getStatusWithStyle(raw);
                },
                getOutputLink: function (rawCode, rawName, hasTaggerOutput) {
                   //<a href="http://aidr-dev.qcri.org/AIDROutput/aidrTaggerLatest.html?crisisCode={[this.getEncodedCode(values.code)]}">{name}</a>
                    if(hasTaggerOutput){

//                       return '<a href="http://aidr-prod.qcri.org/AIDROutput/aidrTaggerLatest.html?crisisCode='+ encodeURI(rawCode) +'">'+ rawName +'</a>';
                       //return Ext.String.format('<a href="{0}" >{1}</a>', SERVER_URL + 'aidrTaggerLatest.html?crisisCode=' + encodeURI(rawCode), rawName)
                       return Ext.String.format('<a href="{0}" >{1}</a>', '/AIDRFetchManager/public/' + encodeURI(rawCode) + '/interactive-view-download', rawName);
                    }
                    else{
                        return  rawName;
                    }
                   // return AIDRFMFunctions.getStatusWithStyle(raw);
                },
                getStatus: function (raw) {
                    return AIDRFMFunctions.getStatusWithStyle(raw);
                },
                getCrisisType: function (r) {
                    return r ? '<span class="styled-text-14"> Crisis Type: ' + r.name + '</span>' : "<span class='styled-text-14'>Crisis Type: Not specified</span>";
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


        this.runningOnlyCollectionView = Ext.create('Ext.view.View', {
            store: this.runningOnlyCollectionStore,
            tpl: this.collectionTpl,
            itemSelector: 'div.active'
        });


        this.collectionPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'collectionStore',
            displayInfo: true,
            displayMsg:'Running collector and tagger records {0} - {1} of {2}',
            emptyMsg:'No running collector and tagger records to display'
        });

        this.stoppedCollectionPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'stoppedCollectionStore',
            displayInfo: true,
            displayMsg:'Running collector only records {0} - {1} of {2}',
            emptyMsg:'No running collector only records to display'
        });

        this.runningOnlyCollectionPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'runningOnlyCollectionStore',
            displayInfo: true,
            displayMsg:'Stopped crisis collection records {0} - {1} of {2}',
            emptyMsg:'No stopped crisis collection records to display'
        });

        this.tab1Desc = Ext.create('Ext.form.Label', {
            flex: 1,
            html: '<span class="redInfo">*</span> AIDR is currently collecting and tagging new items in these collections.</div>',
            padding: '0 0 2 10'
        });

        this.tab2Desc = Ext.create('Ext.form.Label', {
            flex: 1,
            html: '<span class="redInfo">*</span> AIDR is currently collecting new items in these collections, but not tagging them.</div>',
            padding: '0 0 2 10'
        });

        this.tab3Desc = Ext.create('Ext.form.Label', {
            flex: 1,
            html: '<span class="redInfo">*</span> AIDR has stopped collecting items in these collections.</div>',
            padding: '0 0 2 10'
        });

        this.tabPanel = Ext.create('Ext.tab.Panel', {
            cls: 'tabPanel',
            width: '100%',
            minHeight: 400,
            activeTab: 0,
            items: [
                {
                    id:"tab1",
                    title: 'Collector and Tagger',
                    margin: '20 0 0 0',
                    items: [
                        this.tab1Desc,
                        this.collectionView,
                        this.collectionPaging
                    ]
                },
                {
                       title: 'Collector only',
                       margin: '20 0 0 0',
                       items: [
                           this.tab2Desc,
                           this.runningOnlyCollectionView,
                           this.runningOnlyCollectionPaging
                       ]
                },
                {
                    title: 'Archived data',
                    margin: '20 0 0 0',
                    items: [
                        this.tab3Desc,
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