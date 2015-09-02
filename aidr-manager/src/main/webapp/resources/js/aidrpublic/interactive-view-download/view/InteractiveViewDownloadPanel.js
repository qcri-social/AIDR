Ext.require([
             'AIDRFM.common.AIDRFMFunctions',
             'AIDRFM.common.StandardLayout',
             'AIDRFM.common.Header',
             'AIDRFM.common.Footer',
             'Ext.ux.data.PagingMemoryProxy',
             'AIDRPUBLIC.interactive-view-download.view.SingleFilterPanel'
             ]);

Ext.define('AIDRPUBLIC.interactive-view-download.view.InteractiveViewDownloadPanel', {
	extend: 'AIDRFM.common.StandardLayout',
	alias: 'widget.interactive-view-download-view',

	initComponent: function () {
		var me = this;

		this.breadcrumbsNoClassifier = Ext.create('Ext.form.Label', {
			html: '<div class="bread-crumbs">' +
			'<a href="' + BASE_URL + '/protected/home">My Collections</a><span>&nbsp;>&nbsp;</span>' +
			'<span>Download</span></div>',
			margin: 0,
			padding: 0
		});

		this.breadcrumbs = Ext.create('Ext.form.Label', {
			html: '<div class="bread-crumbs">' +
			'<a href="' + BASE_URL + '/protected/home">My Collections</a><span>&nbsp;>&nbsp;</span>' +
			'<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">Classifier for ' + CRISIS_NAME + '</a>' +
			'<span>&nbsp;>&nbsp;Interactive View/Download</span></div>',
			margin: 0,
			padding: 0
		});

		this.screenTitle = Ext.create('Ext.form.Label', {
			cls: 'header-h1 bold-text',
			text: CRISIS_NAME,
			margin: '10 0 10 0'
		});

		this.statusL = Ext.create('Ext.form.Label', {
			padding: '0 0 10 0'
		});

		this.applyFilterButton = Ext.create('Ext.Button', {
			text: 'Apply Filter',
			cls:'btn btn-blue',
			id: 'applyFilterButton',
			disabled: true
		});

		this.resetFiltersButton = Ext.create('Ext.Button', {
			text: 'Reset Filters',
			cls:'btn btn-red',
			id: 'resetFilters',
			margin: '0 10 0 0',
			disabled: true
		});

		this.addFilterButton = Ext.create('Ext.button.Button', {
			iconCls: 'add',
			margin: '1 10 0 0',
			text : null,
			scale: 'small',
			id: 'addFilter',
			tooltip: 'Add new filter',
			disabled: true
		});

		this.filterBlock = Ext.create('Ext.container.Container', {
			margin: 0,
			padding: 0,
			defaultType: 'single-filter-panel',
			id: 'filterBlock',
			layout: {
				type: 'vbox',
				align: 'stretch'
			},
			items: []
		});

		this.filterFieldSet = Ext.create('Ext.form.FieldSet', {
			title: 'Filter',
			layout: {
				type: 'vbox',
				align: 'stretch'
			},
			items: [
			        this.filterBlock,
			        {
			        	xtype: 'container',
			        	margin: '10 0 0 0',
			        	layout: {
			        		type: 'hbox',
			        		align: 'stretch'
			        	},
			        	items: [
			        	        this.addFilterButton,
			        	        {
			        	        	xtype: 'container',
			        	        	layout: 'hbox',
			        	        	flex: 1
			        	        },
			        	        this.resetFiltersButton,
			        	        this.applyFilterButton
			        	        ]
			        }
			        ]
		});

		this.downloadTweetsL = Ext.create('Ext.form.Label', {
			text: 'Download',
			padding: '0 0 0 0',
			cls: 'header-h2'
		});

		this.downloadTweetsDescription = Ext.create('Ext.form.Label', {
			id: 'downloadTweetsDescriptionId',
			text: '',
			padding: '0 0 10 0'
		});

		this.contactOwnerL = Ext.create('Ext.form.Label', {
			cls: 'styled-text',
			margin: '10 0 0 0',
			html: '',
			flex: 1
		});

		this.curatorInfoR = Ext.create('Ext.form.Label', {
			cls: 'styled-text',
			margin: '10 0 0 0',
			html: ''
		});

		this.downloadFormat = Ext.create('Ext.form.RadioGroup', {
			fieldLabel: 'Format',
			labelWidth: 55,
			columns: [150, 210, 240],
			items: [
			        {
			        	boxLabel: 'Spreadsheet (.csv)',
			        	name: 'format',
			        	inputValue: 'csv',
			        	checked: true
			        },
			        {
			        	boxLabel: 'Single JSON object (.json)',
			        	name: 'format',
			        	inputValue: 'JSON'
			        },
			        {
			        	boxLabel: 'One JSON per line (.txt-json)',
			        	name: 'format',
			        	inputValue: 'TEXT_JSON'
			        }
			        ]
		});

		this.downloadContents = Ext.create('Ext.form.RadioGroup', {
			fieldLabel: 'Contents',
			labelWidth: 55,
			columns: [205, 90],
			items: [
			        {
			        	boxLabel: 'Full tweets (max. 100K items)',
			        	name: 'contents',
			        	inputValue: 'full',
			        	checked: true
			        },
//			        {
//			        	boxLabel: 'Ids only',
//			        	name: 'contents',
//			        	inputValue: 'ids'
//			        }
			        ]
		});

		this.downloadButton = Ext.create('Ext.Button', {
			text: 'Generate Downloadable File',
			cls:'btn btn-blue',
			id: 'downloadButton',
			margin: '10 0 0 0'
		});

		this.downloadLink = Ext.create('Ext.form.Label', {
			flex: 1,
			margin: '10 5 5 5',
			html: ''
		});

		this.tweetsStore = Ext.create('Ext.data.Store', {
			pageSize: 25,
			storeId: 'tweetsStore',
			fields: ['text', 'attribute_name', 'label_name', 'confidence', 'createdAt']
		});

		this.tweetsGrid = Ext.create('Ext.grid.Panel', {
			store: this.tweetsStore,
			id: 'viewDownloadTweetsGrid' ,
			itemId: 'tweetsGrid',
			margin: '10 0 0 0',
			cls: 'aidr-grid',
			viewConfig: {
				loadMask: false
			},
			columns: [
			          {
			        	  xtype: 'gridcolumn', dataIndex: 'createdAt', text: 'Date/Time', width: 175
			          },
			          {
			        	  xtype: 'gridcolumn', dataIndex: 'text', text: 'Tweet', flex: 1
			          }
			          ]
		});

		this.tweetsPaging = Ext.create('Ext.toolbar.Paging', {
			cls: 'aidr-paging',
			margin: '12 2 0 2',
			store:'tweetsStore',
			displayInfo:true,
			displayMsg:COLLECTION_TYPES[TYPE]["plural"] + ' {0} - {1} of {2}',
			emptyMsg:'No '+ COLLECTION_TYPES[TYPE]["plural"] + ' to display',
			items: [
			        {
			        	xtype: 'tbseparator'
			        },
			        {
			        	xtype : 'trigger',
			        	itemId : 'gridTrigger',
			        	fieldLabel: 'Filter by keywords',
			        	triggerCls : 'x-form-clear-trigger',
			        	labelWidth: 110,
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
				type: 'vbox'
			},
			items: [
			        this.downloadTweetsL,
			        this.downloadTweetsDescription,
			        this.downloadFormat,
			        this.downloadContents,
			        {
			        	xtype: 'container',
			        	layout: 'hbox',
			        	items: [
			        	        this.downloadButton,
			        	        this.downloadLink
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

		this.curatorInfoPanel = Ext.create('Ext.container.Container', {
//			hidden: true,
			layout: {
				type: 'hbox',
				pack: 'end'
			},
			items: [
			        {
			        	xtype:'container',
			        	layout: {
			        		type:'vbox'
			        	},
			        	items: [
			        	        this.curatorInfoR,
			        	        this.contactOwnerL
			        	        ]
			        }
			        ]
		});

		if(USER_NAME != null && USER_NAME != '') {
			if(CRISIS_NAME != '') {
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
				            	  margin: 0,
				            	  padding: 0,
				            	  items: [
				            	          this.screenTitle,
				            	          this.statusL
				            	          ]
				              },
				              this.filterFieldSet,
				              this.tweetsPanel,
				              this.contactOwnerPanel,
				              {
				            	  xtype: 'container',
				            	  margin: '15 0 0 0',
				            	  html: '<div class="horizontalLine"></div>'
				              },
				              this.downloadPanel
				              ];
			}
			else {
				this.items = [
				              this.breadcrumbsNoClassifier,
				              {
				            	  xtype: 'container',
				            	  margin: '5 0 0 0',
				            	  html: '<div class="horizontalLine"></div>'
				              },
				              this.downloadPanel
				              ];
			}
		}
		else {
			this.items = [
			              {
			            	  xtype: 'container',
			            	  layout: {
			            		  type: 'vbox',
			            		  align: 'stretch'
			            	  },
			            	  margin: 0,
			            	  padding: 0,
			            	  items: [
			            	          this.screenTitle
			            	          ]
			              },
			              this.tweetsPanel,
			              this.curatorInfoPanel,
			              {
			            	  xtype: 'container',
			            	  margin: '15 0 0 0',
			            	  html: '<div class="horizontalLine"></div>'
			              },
			              this.downloadPanel
			              ];
		}

		this.callParent(arguments);
	}

});