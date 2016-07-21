Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
]);
//Author:Sushant
//Code to listen to the onclick event of the Bounding Box hyperlink and display it in a popup window
 Ext.onReady(function() {
        Ext.BLANK_IMAGE_URL = 'http://extjs.cachefly.net/ext-3.2.1/resources/images/default/s.gif';
        App.Demo.init();
    });


    Ext.ns('App');
    App.Demo = {
        init:function()
        {
            
        },

        openWindow:function(url)
        {
            var win = new Ext.Window({
                width:800,
                height:600,
                title:'Pick up the Coordinates:' + 'www.boundingbox.klokantech.com/',
                scrollable:false,
                modal:false
            });

            var iframeid = win.getId() + '_iframe';

            var iframe = {
                id:iframeid,
                tag:'iframe',
                src:'http://boundingbox.klokantech.com/',
                width:'100%',
                height:'100%',
                frameborder:0
            }

            // show first
            win.show();
            // then iframe
            Ext.DomHelper.insertFirst(win.body, iframe)

        }

    }
    //Code Ends

Ext.define('AIDRFM.collection-create.view.CollectionCreatePanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.collection-create',

    fieldDefaults: {
        labelAlign: 'left',
        msgTarget: 'side'
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    initComponent: function () {

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs"><a href="home">My Collections</a><span>&nbsp;>&nbsp;Create Collection</span></div>',
            margin: 0,
            padding: 0
        });

        this.pageTitle = Ext.create('Ext.form.Label', {
            text: 'Create New Collection',
            padding: '0 0 0 0',
            cls: 'header-h1 bold-text'
        });

        this.codeE = Ext.create('Ext.form.field.Text', {
            hidden:true,
            fieldLabel: 'Short name',
            name: 'code',
            allowBlank: false,
            width: 370,
            emptyText: 'e.g., Sandy2012 or EQJapan2011',
            maxLength: 64,
            maxLengthText: 'The maximum length for this field is 64',
            labelWidth: 130,
            maskRe: /[^ \\\/]/
        });

        this.nameE = Ext.create('Ext.form.field.Text', {
            fieldLabel: 'Crisis Name',
            id: 'nameTextField',
            name: 'name',
			suspendLayout:true,
            allowBlank: false,
            labelWidth: 240,
            maxLength: 50,
            maxLengthText: 'The maximum length for this field is 50',
            emptyText: 'e.g., Hurricane Sandy',
            flex:1 ,
            maskRe: /[ a-z0-9-_ ]/i,
            regex: /^[ a-zA-Z0-9-_ ]+$/,
            invalidText: 'Not a valid crisis name. Supports alphabets and numbers no special characters except underscore and hyphen".'
        });

        this.keywordsE = Ext.create('Ext.form.field.TextArea', {
            fieldLabel: 'Comma-Separated Terms and Hashtags (Tip: Be specific with terms)',
            name: 'track',
            allowBlank: true,
			suspendLayout:true,
            maxLength: 24000,
            maxLengthText: 'The maximum length for this field is 400',
            flex: 1,
            height: 109,
            labelWidth: 240,
            emptyText: 'e.g. earthquake california, quake california, #earthquakeca, #sfearthquake, #earthquakesf',

            afterLabelTextTpl:
                '<div style="line-height: 17px">' +
                    '<br><span style="color: #FD4737">Bad term: earthquake (too generic)</span>' +
                    '<br><span style="color: #FD4737">Bad term: california (too generic)</span>' +
                    '<br><span style="color: #008000">Good term: earthquake california</span>' +
                '</div>'
        });

       /* this.geoE = Ext.create( 'Ext.form.field.Text', {
            fieldLabel: 'Additionally, collect all tweets from these geographical boundaries, independent of the keywords they contain',
            labelWidth: 240,
            name: 'geo',
            flex: 1,
            maxLength: 25,
            maxLengthText: 'The maximum length for this field is 25',
            emptyText: 'e.g., 43.43, 22.44, 89.32, 56.43 (max 25)'
        });*/
        this.geoE = Ext.create('Ext.form.FormPanel', {
            border: false,
            //bodyPadding: 5,
            padding: '0 0 10 0',
            items:[{
                fieldLabel: 'Collect tweets only within these geographical boundaries:',
                labelWidth: 240,
                width: 698,
                xtype: 'textareafield',
				suspendLayout:true,
                growMax: 100,
                name: 'geo',
                anchor: '100%',
                maxLengthText: 'The maximum length for this field is 25',
                emptyText: 'e.g., 43.43, 22.44, 89.32, 56.43 (max 25)'
            }]
        });
        
        this.geoR = Ext.create('Ext.form.Panel', {
            border: false,
            id:'geoRPanel',
        	items:[{
            	name: 'geoR',
            	xtype: 'radiogroup',
				suspendLayout:true,
            	fieldLabel: 'Geographical boundary strictness',
            	labelWidth: 240,
            	columns: 1,
            	vertical: true,
            	items: [
            	        
                    { margin: '0 0 0 -5', boxLabel: 'Does not apply (no geographical boundary)', name: 'geoR1', inputValue: 'null', checked: true},
                    { margin: '0 0 0 -5', boxLabel: 'Approximate: a tweet may be collected if it comes from a country that overlaps with the geographical boundaries.', name: 'geoR1', inputValue: 'approximate' },
                    { margin: '0 0 0 -5', boxLabel: 'Strict: a tweet can only be collected if it has geographical coordinates strictly inside the geographical boundaries.', name: 'geoR1', inputValue: 'strict'}
                ],
        	}]
        });

        this.geoDescription = Ext.create('Ext.form.Label', {
            flex: 1,
            //added additional onclick event to display bounding box in a popup window instead of opening up in a new tab
            id:'geoDescription',
			suspendLayout:true,
            html: '<span class="redInfo">*</span> ' +
                'Click here to get coordinates: <a href="#" onclick="App.Demo.openWindow()" >boundingbox.klokantech.com</a> ' +
                '("Copy/paste CSV format of a boundingbox")',
            padding: '2 0 2 245'
        });

        this.followE = Ext.create('Ext.form.field.Text', {
            fieldLabel: 'Additionally, collect all tweets from these twitter user ids, independent of their keywords or location',
            labelWidth: 230,
            labelPad: 15,
			suspendLayout:true,
            name: 'follow',
            width:698,
            emptyText: 'e.g. cnnbrk, bbcbreaking, fema, lastquake'
        });

        this.durationDescription = Ext.create('Ext.form.Label', {
            flex: 1,
            id:'durationDescription',
			suspendLayout:true,
            html: '<span class="redInfo">*</span> A normal setting for most crises is 2 to 5 days. If you need more than 7 days contact the <a href="https://github.com/qcri-social/AIDR/issues" target="_blank">AIDR team</a>.',
            padding: '2 0 2 245'
        });

        this.durationStore = Ext.create('Ext.data.Store', {
            fields: ['val', 'label'],
            data : [
                { "val": 12, "label": '12 hours' },
                { "val": 24, "label": '1 day' },
                { "val": 36, "label": '1 day 12 hours' },
                { "val": 48, "label": '2 days'},
                { "val": 60, "label": '2 days 12 hours' },
                { "val": 72, "label": '3 days' },
                { "val": 96, "label": '4 days' },
                { "val": 120, "label": '5 days' },
                { "val": 144, "label": '6 days' },
                { "val": 168, "label": '7 days' },
                { "val": 336, "label": '14 days' }
            ]
        });

        this.duration = Ext.create('Ext.form.ComboBox', {
            fieldLabel: 'Collect data for',
            width:698,
            labelWidth: 240,
            name: 'durationHours',
			suspendLayout:true,
            editable: false,
            text: 'Edit',
            valueField: 'val',
            displayField: 'label',
            store: this.durationStore,
//            default duration is 2 days (48 hours)
            value: 48
        });
        
        this.fetchIntervalStore = Ext.create('Ext.data.Store', {
            fields: ['val', 'label'],
            data : [
                { "val": 2, "label": '2 hours' },
                { "val": 6, "label": '6 hours' },
                { "val": 12, "label": '12 hours' },
                { "val": 24, "label": '1 day'},
                { "val": 72, "label": '3 days' },
                { "val": 168, "label": '7 days' }
            ]
        });
        
		this.fetchFromStore = Ext.create('Ext.data.Store', {
            fields: ['val', 'label'],
            data : [
                { "val": 168, "label": '7 days' },
                { "val": 360, "label": '15 days' },
                { "val": 720, "label": '1 month' },
                { "val": 2160, "label": '3 months'},
                { "val": 4320, "label": '6 months' }
            ]
        });
		
        this.fetchInterval = Ext.create('Ext.form.ComboBox', {
        	  fieldLabel: 'Fetch Interval',
              width:698,
              labelWidth: 240,
              name: 'fetchInterval',
              editable: false,
			  suspendLayout:true,
              text: 'Edit',
              valueField: 'val',
              displayField: 'label',
              store: this.fetchIntervalStore,
//              default duration is 2 hours
              value: 2
        });
		
		this.fetchFrom = Ext.create('Ext.form.ComboBox', {
        	  fieldLabel: 'Fetch From Last',
              width:698,
              labelWidth: 240,
              name: 'fetchFrom',
              editable: false,
			  suspendLayout:true,
              text: 'Edit',
              valueField: 'val',
              displayField: 'label',
              store: this.fetchFromStore,
              value: 168
        });

        this.langComboStore = Ext.create('Ext.data.ArrayStore', {
            autoDestroy: true,
            storeId: 'langComboStore',
            idIndex: 0,
            fields: [
                'name',
                'code'
            ],
            data: LANG
        });

        this.langCombo = Ext.create('Ext.form.ComboBox', {
            store: this.langComboStore,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'code',
            multiSelect: true,
			suspendLayout:true,
            editable: false,
            fieldLabel: 'Language of tweets',
            flex:1,
            labelWidth: 240,
            name: 'langFilters',
            id: 'CollectionLang',
            value: 'en',
            emptyText: 'e.g., en, ar, ja',
            tpl: '<tpl for=".">' +
                '<li role="option" class="x-boundlist-item">{name}</li>' +
                '<tpl if="xindex == 9"><hr/></tpl>' +
                '<tpl if="xindex == 56"><hr/>Other ...<hr/></tpl>' +
                '</tpl>'
        });

        this.langNote = Ext.create('Ext.form.Label', {
            hidden:true,
            flex: 1,
            html:'<div></div>',
            padding: '2 0 2 245'
        });
        
        var collection_type = [{ "val": 'SMS', "label": 'SMS' }];
		if (SIGNED_IN_PROVIDER == "twitter") {
			collection_type.push({ "val": 'Twitter', "label": 'Twitter' });
		} else if (SIGNED_IN_PROVIDER == "facebook") {
			collection_type.push({ "val": 'Facebook', "label": 'Facebook' });
		}
        this.collectionTypeComboStore = Ext.create('Ext.data.Store', {
            fields: ['val', 'label'],
            data: collection_type
        });

        this.collectionTypeCombo = Ext.create('Ext.form.ComboBox', {
            fieldLabel: 'Collection Source',
            flex:1,
            labelWidth: 240,
            id: 'CollectionType',
			suspendLayout:true,
            name: 'collectionType',
            editable: false,
            text: 'Edit',
            valueField: 'val',
            displayField: 'label',
            store: this.collectionTypeComboStore,
            value: ''
        });

        this.notesL = Ext.create('Ext.form.Label', {
            text: 'Note',
            cls: 'header-h2',
            margin:'25 0 10 0'
        });
        
        this.notetextL = Ext.create('Ext.form.Label', {
        	flex: 1,
        	html: 'By creating a collection you agree to our <a href="http://aidr.qcri.org/r/tos" target=_blank>Terms of Service</a>, which basically state that:<br>'
        		 	+ '<ul>'
        			+ '<li><span class="blueInfo"></span>   You are using AIDR for humanitarian and crisis response purposes.</span></li>'
        		 	+ '<li><span class="blueInfo"></span>   You understand your collections can be stopped and removed at any time.'
                                + '<li><span class="blueInfo"></span>   After your collection finishes or is stopped, we will give you the option to download it within a week.'
        		 	+ '<li><span class="blueInfo"></span>   You understand the data you collect will be made available for research purposes.</span></li>'
        		 	+ '</ul>'
        		 	+ '<br> If you have questions, please contact us.',
            margin:'20 0 0 0'
        });
        
        this.saveButton = Ext.create('Ext.Button', {
            text: 'Create and Start',
            margin: '0 0 0 -100',
            cls: 'btn btn-blueBig',
            id: 'collectionCreate'
        });

        this.cancelButton = Ext.create('Ext.Button', {
            text: 'Cancel',
                         margin: '0 140 0 240',

            cls: 'btn btn-redBig',
            id: 'collectionCancelCreate'
        });

        this.crisisTypesStore = Ext.create('Ext.data.Store', {
            pageSize: 30,
            storeId: 'crisisTypesStore',
            fields: ['crisisTypeID', 'name'],
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/tagger/getAllCrisisTypes.action',
                reader: {
                    rootProperty: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true
        });

        this.crisisTypesCombo = Ext.create('Ext.form.ComboBox', {
            store: this.crisisTypesStore,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'crisisTypeID',
            fieldLabel: 'Crisis Type',
			suspendLayout:true,
            flex: 1,
            name: 'crisisType',
            editable: false,
            allowBlank: true,
            labelWidth: 240
        });

        var wrapFieldWithInfo = function(field, infoId, iconMargin, fieldMargin, wrapId) {
            return {
                xtype: 'container',
                id:wrapId,
                layout: 'hbox',
                margin:fieldMargin,
				suspendLayout:true,
                items: [
                    field,
                    {
                        border: false,
                        xtype:'component',
                        bodyStyle: 'background:none',
                        autoEl:'span',
                        html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                        height: 22,
                        width: 22,
                        id: infoId,
                        margin:iconMargin
                    }
                ]
            }
        }

        this.editForm = Ext.create('Ext.form.Panel', {
            id: 'collectionForm',
            bodyCls: 'no-border',
            layout:'anchor',
            defaults: {
                anchor: '75%',
                labelWidth: 240,
                margin: '20 0 0 5'
            },
            defaultType: 'container',
            items: [
                this.codeE,
                wrapFieldWithInfo(this.nameE, 'collectionNameInfo'),

                wrapFieldWithInfo(this.collectionTypeCombo, 'collectionTypeInfo', undefined, '20 0 0 0'),

                wrapFieldWithInfo(this.langCombo, 'collectionLangInfo', undefined, '20 0 5 0', 'langPanel'),
                this.langNote,

                wrapFieldWithInfo(this.keywordsE, 'collectionkeywordsInfo', undefined, '20 0 0 0', 'keywordsPanel'),

                wrapFieldWithInfo(this.crisisTypesCombo, 'crisisTypesInfo', undefined, '20 0 0 0'),

                //Advanced configuration
                {
                    xtype:'panel',
                    title:'Advanced configuration',
                    anchor:'100%',
                    collapsible:true,
                    collapsed:true,
					titleCollapse:true,
                    cls:'collapse-style',
                   // bodyPadding: '5 5 20 5',
                    margin: '20 0 0 0',
                    layout:'anchor',
                    defaults: {
                        anchor: '100%',
                        margin: '20 0 0 0'
                    },
                    items:[
                        wrapFieldWithInfo(this.geoE, 'collectionGeoInfo', '0 0', '20 0 -6 0', 'geoPanel'),
                        this.geoDescription,
                        this.geoR,

                        wrapFieldWithInfo(this.followE, 'collectionFollowInfo', '12 0', undefined, 'followPanel'),
                        wrapFieldWithInfo(this.fetchInterval, 'fetchIntervalInfo', undefined, '20 0 5 0', 'fetchIntervalPanel'),
						wrapFieldWithInfo(this.fetchFrom, 'fetchFromInfo', undefined, '20 0 5 0', 'fetchFromPanel'),
                        wrapFieldWithInfo(this.duration, 'collectionDurationInfo', undefined, '20 0 5 0'),
                        this.durationDescription
                    ]
                },

                this.notesL,
                this.notetextL,

                {
                    xtype: 'container',
                    layout: 'hbox',
                     padding: '15 0 0 0',
                    items: [
                        this.cancelButton,
                        this.saveButton
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
            this.pageTitle,
            this.editForm
        ];

        this.callParent(arguments);
    }

});
