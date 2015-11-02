Ext.define('ModelForClassifiedDetailsChart',{
  extend: 'Ext.data.Model',
    fields: [{name : 'value' },
             {name : 'trainingDocumentsCount' },
             {name : 'classifiedDocumentCount' },
             {name : 'totalDocuments' },
             {name : 'labelAuc', convert:function(a, rec){
                return (rec.raw.labelAuc*100);
            }},
             {name : 'labelPrecision' , convert:function(a, rec){
                return (rec.raw.labelPrecision*100);
            }},
             {name : 'labelRecall' , convert:function(a, rec){
                return (rec.raw.labelRecall*100);
            }}
        ]
});

var store = Ext.create('Ext.data.JsonStore', {
    pageSize: 100,
    storeId: 'modelLabelStoreForClassifierDetailsChart',
    model : 'ModelForClassifiedDetailsChart',
    // fields: ['value', 'classifiedDocumentCount', 'trainingDocumentsCount', 'totalDocuments'],
    autoLoad: false
});

Ext.define('TAGGUI.model-details.view.ClassifierDetailsChart', {
    extend: 'Ext.Panel',
    alias: 'widget.ClassifierDetailsChart',
    xtype: 'dashboard-chart',

    initComponent: function() {
        var me = this;

        var barChart = Ext.create('Ext.chart.Chart', {
            flex:4,
           // margin: '0 0 3 0',
            cls: 'x-panel-body-default',
            animate: true,
            style:  {
                border: 0
            },
            legend: {
                position: "bottom"
            },
            store: 'modelLabelStoreForClassifierDetailsChart',
            
            axes: [
            {
                type: 'Numeric',
                position: 'bottom',
                fields: ['trainingDocumentsCount', 'classifiedDocumentCount', 'totalDocuments'],
                title: 'No. of '+COLLECTION_TYPES[TYPE]["plural"],
                grid: true,
                minimum: 0,
                label:{
                	rotate: {
                    	degrees: -45
                    }
                }
                
            },
            {
                type: 'Category',
                position: 'left',
                fields: ['value'],
                title: 'Tags',
            }
        ],
        series: [
            {
                type: 'bar',
                axis: 'left',
                highlight: true,
				style: {
					opacity: .5
				},
                tips: {
                  trackMouse: true,
                  width: 180,
                  height: 50,
                  renderer: function(storeItem, item) {
                      if(item.yField == ('trainingDocumentsCount')){
                          this.setTitle('Human-tagged '+ COLLECTION_TYPES[TYPE]["plural"] +' for ' + storeItem.get('value') + ': ' + storeItem.get('trainingDocumentsCount') +' ' +  COLLECTION_TYPES[TYPE]["plural"]);
                      }
                      else  if(item.yField == ('classifiedDocumentCount')){
                          this.setTitle('Machine-tagged '+ COLLECTION_TYPES[TYPE]["plural"] +' for ' + storeItem.get('value') + ': ' + storeItem.get('classifiedDocumentCount') +' ' +  COLLECTION_TYPES[TYPE]["plural"]);
                      }
                      else  if(item.yField == ('totalDocuments')){
                          this.setTitle('Total tweets '+ COLLECTION_TYPES[TYPE]["plural"] +' for ' + storeItem.get('value') + ': ' + storeItem.get('totalDocuments') +' ' +  COLLECTION_TYPES[TYPE]["plural"]);
                      }
                  }
                },
                label: {
                  display: 'outside',
                  'text-anchor': 'middle',
                    field: ['trainingDocumentsCount','classifiedDocumentCount', 'totalDocuments'],
                    renderer: Ext.util.Format.numberRenderer('0'),
                    orientation: 'horizontal',
                    color: '#333'
                },
                 listeners: {
                    itemmouseup: function(item) {
                         var series = barChart.series.get(0);
                          gridPanel.getSelectionModel().select(Ext.Array.indexOf(series.items, item));
                     }
                 },
                xField: 'value',
                title:  ['Human-Tagged', 'Machine-Tagged', 'Total'],
                yField: ['trainingDocumentsCount', 'classifiedDocumentCount', 'totalDocuments']
            }
        ]
        });
        
        // create radar store.
        var radarStore = Ext.create('Ext.data.JsonStore', {
            fields: ['Name', 'Data', 'Tag'],
            //Default data values
            data: [
                { 'Name': 'AUC', 'Data': 100 },
                { 'Name': 'Precision','Data': 100 },
                { 'Name': 'Recall', 'Data': 100 },
            ]
        });

        updateRadarChart = function(rec) {
            radarStore.loadData([
                { 'Name': 'Auc', 'Data': rec.get('labelAuc'), 'Tag': rec.get('value')},
                { 'Name': 'Precision', 'Data': rec.get('labelPrecision'), 'Tag': rec.get('value')},
                { 'Name': 'Recall', 'Data': rec.get('labelRecall') , 'Tag': rec.get('value')}
              
            ]);
        };
        
        var radarChart = Ext.create('Ext.chart.Chart', {
           // margin: '0 0 0 0',
           // width: 200,
            insetPadding: 40,
            flex: 2,
            animate: true,
            store: radarStore,
            theme: 'Blue',
            axes: [{
                steps: 5,
                type: 'Radial',
                position: 'radial',
                maximum: 100
            }],
            series: [{
                type: 'radar',
                xField: 'Name',
                yField: 'Data',
                showInLegend: true,
                showMarkers: true,
                markerConfig: {
                    radius: 4,
                    size: 4,
                    fill: 'rgb(69,109,159)'
                },
                tips: {
                    trackMouse: true,
                    width: 180,
                    height: 60,
                    renderer: function(storeItem, item) {
                        this.setTitle(storeItem.data.Name +' for ' + storeItem.data.Tag + ': ' + storeItem.data.Data.toFixed(0)+ '%');
                }
                },
                style: {
                    fill: 'rgb(194,214,240)',
                    opacity: 0.5,
                    'stroke-width': 0.5
                }
            }]
        });
        
        function perc(v) {
            return v.toFixed(0) + '%';
        }
        
	    var gridPanel = Ext.create('Ext.grid.Panel', {
	        flex: 1,
	        store: 'modelLabelStoreForClassifierDetailsChart',
	        cls: 'aidr-grid',
	        columns: [
	            {
	            	xtype: 'gridcolumn',
	                text: 'Tag',
	                width: 200,
	                dataIndex: 'value'
	            },
	            {
	                text: 'Human-tagged tweets',
	                dataIndex: 'trainingDocumentsCount',
	                flex:1
	            },
	            {
	                text: 'Machine-tagged tweets',
	                dataIndex: 'classifiedDocumentCount',
	                flex:1
	            },
	            {
	                text: 'Total tweets',
	                dataIndex: 'totalDocuments'
	            },
	            {	text: 'AUC',
	                dataIndex: 'labelAuc',
	                renderer: perc
	            }, 
	            {
	                text: 'Precision',
	                dataIndex: 'labelPrecision',
	                renderer: perc
	            }, 
	            {
	            	text: 'Recall',
	                dataIndex: 'labelRecall',
	                renderer: perc
	            }
	        ],
	
	        listeners: {
	            selectionchange: function(model, records) {
	                if (records[0]) {
	                    selectedRec = records[0];
	                    updateRadarChart(selectedRec);
	                }
	            },
	            render : function(grid){
	            	 grid.on('viewready',function(){  
	                     this.getSelectionModel().select(0);  
	                });
            	 }
	        }
	    });

        me.items = [{
            xtype: 'container',
            width: '100%',
            height: 640,
            fieldDefaults: {
                labelAlign: 'left',
                msgTarget: 'side'
            },
    
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                        {
                            xtype: 'container',
                            height: 400,
                            layout: {
                                type: 'hbox',
                                align: 'stretch',
                            },
                            items: [barChart, radarChart]
                        },
                        gridPanel]
        }];

        this.callParent();
    }
});
