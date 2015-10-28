Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer',
    'TAGGUI.attribute-details.view.AttributeDetailsMain',
    'TAGGUI.model-details.view.ClassifierDetailsChart',
    'TAGGUI.model-details.view.ClassifierHistoryChart'
]);

Ext.define('TAGGUI.model-details.view.ModelDetailsPanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.model-details-view',

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/home">My Collections</a><span>&nbsp;>&nbsp;</span>' +
                '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">' + CRISIS_NAME + '</a><span>&nbsp;>&nbsp;Classifier Details</span></div>',
            margin: 0,
            padding: 0
        });

        this.taggerTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Classifier "' + MODEL_NAME + '"',
            flex: 1
        });

        this.modelDetails = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            margin: '0 0 15 0',
          /*  html: 'Machine has tagged <b>0</b> '+ COLLECTION_TYPES[TYPE]["singular"] + '.&nbsp;<a href="' + BASE_URL +  '/protected/'
                + CRISIS_CODE + '/' + MODEL_ID + '/' + MODEL_FAMILY_ID + '/' + ATTRIBUTE_ID +
                + '/training-data">Go to human-tagged '+ COLLECTION_TYPES[TYPE]["plural"] + ' &raquo;</a>',*/
            flex: 1
        });

        this.aucHint = Ext.create('Ext.form.Label', {
            html: '<span class="redInfo">*</span><span style="color: #00acee;"> If AUC is lower than 80%, or AUC is 100%, you urgently need more human tagged '+ COLLECTION_TYPES[TYPE]["plural"] + '.</span>'
        });

      
        this.modelHistoryTitle = Ext.create('Ext.form.Label', {
            hidden: true,
            margin: '10 0',
            cls: 'header-h1',
            text: 'Classifier History'
        });

        this.modelHistoryStore = Ext.create('Ext.data.Store', {
            pageSize: 10,
            storeId: 'modelHistoryStore',
            fields: ['modelID', 'avgPrecision', 'avgRecall', 'avgAuc', 'trainingCount', 'trainingTime'],
            remoteSort: true,
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/tagger/modelHistory.action',
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
                    s.getProxy().extraParams = {
                        id: MODEL_FAMILY_ID
                    }
                },
                load: function (s) {
                    var count = s.getCount();

                    if (count > 0 ) {
                        me.modelHistoryTitle.show();
                        me.modelHistoryGrid.show();
                        me.modelHistoryPaging.show();
                    } else {
                        me.modelHistoryTitle.hide();
                        me.modelHistoryGrid.hide();
                        me.modelHistoryPaging.hide();
                        me.tabPanel.html = '<div style="padding-top:150px"><center><div style="font-size:16pt">This collection has not changed since it was created.</div></center></div>';
                    }
                }
            }
        });

      
        this.modelHistoryGrid = Ext.create('Ext.grid.Panel', {
            flex:1,
            store: this.modelHistoryStore,
            cls: 'aidr-grid',
            columns: [
                {
                    xtype: 'gridcolumn', dataIndex: 'avgPrecision', text: 'Precision', width: 150,
                    renderer: function (r, meta, record) {
                    	return r ? (r * 100).toFixed(0) + '%' : '0%';
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'avgRecall', text: 'Recall', flex: 1,
                    renderer: function (r, meta, record) {
                        return r ? (r * 100).toFixed(0) + '%' : '0%';
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'avgAuc', text: 'AUC', width: 150,
                    renderer: function (r, meta, record) {
                        return r ? (r * 100).toFixed(0) + '%' : '0%';
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'trainingCount', text: 'Human-tagged '+COLLECTION_TYPES[TYPE]["plural"], width: 150,
                    renderer: function (r, meta, record) {
                        return r ? r.format() : 0;
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'trainingTime', text: 'Date/Time', width: 200,
                    renderer: function (r, meta, record) {
                       // return r ? moment(r).calendar() : "Not specified";
                       return r ? moment(r).format("MMM Do, YYYY hh:mm A") : "Not specified";
                    }
                }
            ],
			listeners: {
                select: function(model, records) {
                    var selectedRec = records.data;
                    var chart = Ext.ComponentQuery.query('ClassifierHistoryChart');
                    
                    var avgPrecisionSeries = chart[0].series.items[0];
                    var avgRecallSeries = chart[0].series.items[1];
                    var avgAucSeries = chart[0].series.items[2];
                    var trainingCountSeries = chart[0].series.items[3];
                    
                    avgPrecisionSeries.unHighlightItem();
                    Ext.Array.each(avgPrecisionSeries.items, function(pointOnchart, index){
                    	 if (pointOnchart.value[0] == selectedRec.trainingTime && pointOnchart.value[1] == selectedRec.avgPrecision) {
                    		 avgPrecisionSeries.highlightItem(pointOnchart);
                        }
                     });
                    
                    avgRecallSeries.unHighlightItem();
                    Ext.Array.each(avgRecallSeries.items, function(pointOnchart, index){
                    	 if (pointOnchart.value[0] == selectedRec.trainingTime && pointOnchart.value[1] == selectedRec.avgRecall) {
                    		 avgRecallSeries.highlightItem(pointOnchart);
                        }
                     });
                    
                    avgAucSeries.unHighlightItem();
                    Ext.Array.each(avgAucSeries.items, function(pointOnchart, index){
                    	 if (pointOnchart.value[0] == selectedRec.trainingTime && pointOnchart.value[1] == selectedRec.avgAuc) {
                    		 avgAucSeries.highlightItem(pointOnchart);
                        }
                     });
                    
                    trainingCountSeries.unHighlightItem();
                    Ext.Array.each(trainingCountSeries.items, function(pointOnchart, index){
                    	 if (pointOnchart.value[0] == selectedRec.trainingTime && pointOnchart.value[1] == selectedRec.trainingCount) {
                    		 trainingCountSeries.highlightItem(pointOnchart);
                        }
                     });
				},
        
				deselect: function(model, records){
					var chart = Ext.ComponentQuery.query('ClassifierHistoryChart');
					for(i=0 ; i<chart[0].series.items.length;i++){
						chart[0].series.items[i].unHighlightItem();
					}
				}
			}
        });
        

        this.modelHistoryPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'modelHistoryStore',
            displayInfo:true,
            hidden: true,
            displayMsg:'Classifier history records {0} - {1} of {2}',
            emptyMsg:'No classifier history records to display',
        });

        this.attributeDetails = Ext.create('TAGGUI.attribute-details.view.AttributeDetailsMain',{
            showDeleteButton: false
        });

        this.tabPanel = Ext.create('Ext.tab.Panel', {
            cls: 'tabPanel',
            width: '100%',
            minHeight: 400,
            activeTab: 0,
            items: [
                {
                    title: 'Details',
                    items: [
                        {
                            xtype: 'container',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            margin: '15 0 0 0',
                            items: [
                                this.modelDetails
                            ]
                        },
                        {
                            xtype: 'container',
                            width: '100%',
                            //html: '<div class="horizontalLine"></div>'
                        },
                        {
                			xtype:'ClassifierDetailsChart',
                			width: '100%'
                		},
                        
                        {
                            xtype: 'container',
                            layout:  'hbox',
                            margin: '25 0 0 0',
                            items: [
                                {
                                    xtype: 'container',
                                    flex: 1
                                },
                                this.aucHint
                            ]
                        }
                    ]
                },
                {
                    title: 'History',
                    padding: '10 0 0 0',
                    defaults: {
                    },
                    items: [
						{
							xtype:'ClassifierHistoryChart',
							width: 900
						},
                        this.modelHistoryTitle,
                        this.modelHistoryGrid,
                        this.modelHistoryPaging,
                    ]
                },
                {
                    title: 'Edit/Remove',
                    padding: '10 0 0 0',
                    items: [
                        this.attributeDetails
                    ]
                }
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
                    this.taggerTitle
                ]
            },
            this.tabPanel
        ];

        this.callParent(arguments);
    }

});