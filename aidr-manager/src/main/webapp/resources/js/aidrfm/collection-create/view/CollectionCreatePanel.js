Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
]);

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
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs"><a href="home">My Collections</a></div>',
            margin: 0,
            padding: 0
        });

        this.descriptionL = Ext.create('Ext.form.Label', {
            flex: 1,
            text: 'Create New Collection',
            padding: '0 0 0 0',
            cls: 'header-h1 bold-text'
        });

        this.codeE = Ext.create('Ext.form.field.Text', {
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
            width: 370,
            fieldLabel: 'Name',
            id: 'nameTextField',
            name: 'name',
            allowBlank: false,
            labelWidth: 130,
            emptyText: 'e.g., Hurricane Sandy'
        });

        this.keywordsE = Ext.create('Ext.form.field.TextArea', {
            fieldLabel: 'Keywords',
            name: 'track',
            allowBlank: true,
            maxLength: 24000,
            maxLengthText: 'The maximum length for this field is 400',
            flex: 1,
            rows: 4,
            labelWidth: 130,
            emptyText: 'e.g., #sandy, #newyork,#joplin (max 400)'
        });

        this.geoE = Ext.create( 'Ext.form.field.Text', {
            fieldLabel: 'Geographical regions',
            labelWidth: 130,
            name: 'geo',
            flex: 1,
            maxLength: 25,
            maxLengthText: 'The maximum length for this field is 25',
            emptyText: 'e.g., 43.43, 22.44, 89.32, 56.43 (max 25)'
        });

        this.geoDescription = Ext.create('Ext.form.Label', {
            flex: 1,
            id:'geoDescription',
            html: '<span class="redInfo">*</span> <a href="http://boundingbox.klokantech.com/" target="_blank">boundingbox.klokantech.com</a> ("Copy/paste CSV format of a boundingbox")<div><span class="redInfo">*</span>The collection will include <strong>all items from these regions</strong>, independent of whether they contain the keywords or not</div>',
            padding: '2 0 2 135'
        });

        this.followE = Ext.create('Ext.form.field.Text', {
            fieldLabel: 'Follow specific users',
            labelWidth: 130,
            name: 'follow',
            flex: 1,
            emptyText: 'e.g., 47423744, 53324456 (max 5000)'
        });

        this.durationDescription = Ext.create('Ext.form.Label', {
            flex: 1,
            id:'durationDescription',
            html: '<span class="redInfo">*</span> Note: If you need to run your collection for more than 7 days, please contact the AIDR team.',
            padding: '2 0 2 135'
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
                { "val": 168, "label": '7 days' },
                { "val": 336, "label": '14 days' }
            ]
        });

        this.duration = Ext.create('Ext.form.ComboBox', {
            fieldLabel: 'Automatically stop after',
            flex: 1,
            labelWidth: 130,
            name: 'durationHours',
            editable: false,
            text: 'Edit',
            valueField: 'val',
            displayField: 'label',
            width: 125,
            store: this.durationStore,
//            default duration is 2 days (48 hours)
            value: 48
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
            editable: false,
            fieldLabel: 'Language(s)',
            labelWidth: 130,
            name: 'langFilters',
            flex: 1,
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
            flex: 1,
            html:'<div></div>',
            padding: '2 0 2 135'
        });

        this.collectionTypeComboStore = Ext.create('Ext.data.Store', {
            fields: ['val', 'label'],
            data: [
                { "val": 'Twitter', "label": 'Twitter' },
                { "val": 'SMS', "label": 'SMS' }
            ]
        });

        this.collectionTypeCombo = Ext.create('Ext.form.ComboBox', {
            fieldLabel: 'Collection Type',
            labelWidth: 130,
            id: 'CollectionType',
            name: 'collectionType',
            editable: false,
            text: 'Edit',
            valueField: 'val',
            displayField: 'label',
            width: 370,
            store: this.collectionTypeComboStore,
            value: 'Twitter'
        });

        this.collectionTypeNote = Ext.create('Ext.form.Label', {
            flex: 1,
            html:'<div></div>',
            padding: '2 0 2 135'
        });

        this.configurationsL = Ext.create('Ext.form.Label', {
            flex: 1,
            id:'configurationsL',
            text: 'Optional settings',
            padding: '15 0 0 0',
            cls: 'header-h2 bordered-top'
        });
        
        this.notesL = Ext.create('Ext.form.Label', {
            flex: 1,
            text: 'Note',
            padding: '15 0 0 0',
            cls: 'header-h2 bordered-top'
        });
        
        this.notetextL = Ext.create('Ext.form.Label', {
        	flex: 1,
        	html: 'By creating a collection you agree to our <a href="http://aidr.qcri.org/r/tos/" target=_blank>Terms of Service</a>, which basically state that:<br>'
        		 	+ '<ul>'
        			+ '<li><span class="blueInfo">*</span>   You are using AIDR for humanitarian and crisis response purposes.</span></li>'
        		 	+ '<li><span class="blueInfo">*</span>   You understand your collections can be stopped and removed at any time.'
                                + '<li><span class="blueInfo">*</span>   After your collection finishes or is stopped, we will give you the option to download it during at most one week.'
        		 	+ '<li><span class="blueInfo">*</span>   You understand the data you collect will be made available for research purposes.</span></li>'
        		 	+ '</ul>'
        		 	+ '<br> If you have questions, please contact us before starting a collection.',
            padding: '0 0 0 0'
        });
        
        this.saveButton = Ext.create('Ext.Button', {
            text: 'Create Collection',
            margin: '0 10 0 10',
            cls: 'btn btn-green',
            id: 'collectionCreate'
        });

        this.cancelButton = Ext.create('Ext.Button', {
            text: 'Cancel',
            cls: 'btn btn-red',
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
                    root: 'data',
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
            fieldLabel: 'Type',
            flex: 1,
            name: 'crisisType',
            editable: false,
            allowBlank: true,
            labelWidth: 130
        });

        this.editForm = Ext.create('Ext.form.Panel', {
            id: 'collectionForm',
            bodyCls: 'no-border',
            items: [
                {
                    xtype: 'container',
                    defaults: {
                        width: 350,
                        labelWidth: 120
                    },
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
                                this.nameE,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionNameInfo'
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            items: [
                                this.codeE,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionCodeInfo'
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            items: [
                                this.collectionTypeCombo,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionTypeInfo'
                                }
                            ]
                        },
                        this.collectionTypeNote,
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            id:'keywordsPanel',
                            items: [
                                this.keywordsE,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionkeywordsInfo'
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            items: [
                                this.crisisTypesCombo,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'crisisTypesInfo'
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            id:'langPanel',
                            items: [
                                this.langCombo,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionLangInfo'
                                }
                            ]
                        },
                        this.langNote,
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            items: [
                                this.duration,
                                {
                                    border: false,
                                    margin: '5 0 0 0',
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionDurationInfo'
                                }
                            ]
                        },
                        this.durationDescription,
                        this.configurationsL,
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '0 0 5 0',
                            id:'geoPanel',
                            items: [
                                this.geoE,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionGeoInfo'

                                }
                            ]
                        },
                        this.geoDescription,
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            id:'followPanel',
                            items: [
                                this.followE,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'collectionFollowInfo'
                                }
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
            this.descriptionL,
            {
                xtype: 'container',
                margin: '5 0 15 0',
                html: '<div class="horizontalLine"></div>'
            },
            this.editForm
        ];

        this.callParent(arguments);
    }

});
