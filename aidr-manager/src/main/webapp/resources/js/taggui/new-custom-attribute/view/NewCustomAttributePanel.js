Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
]);

Ext.define('TAGGUI.new-custom-attribute.view.NewCustomAttributePanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.new-custom-attribute-view',

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/tagger-home">Tagger</a><span>&nbsp;>&nbsp;New Custom Classifier</span></div>',
            margin: 0,
            padding: 0
        });

        this.taggerTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'New Custom Classifier',
            flex: 1
        });

        this.codeE = Ext.create('Ext.form.field.Text', {
            fieldLabel: 'Code',
            name: 'code',
            allowBlank: false,
            flex: 1,
            emptyText: 'e.g. donationtype',
            maxLength: 64,
            maxLengthText: 'The maximum length for this field is 64 ',
            maskRe: /[^ ]/
        });

        this.nameE = Ext.create('Ext.form.field.Text', {
            flex: 1,
            fieldLabel: 'Name',
            name: 'name',
            id: 'nameCustomClassifier',
            allowBlank: false,
            emptyText: 'e.g. Donation type'
        });

        this.descriptionE = Ext.create('Ext.form.field.Text', {
            fieldLabel: 'Description',
            name: 'description',
            allowBlank: false,
            flex: 1,
            emptyText: 'e.g. Indicate the item being donated'
        });

        this.codeLabelE = Ext.create('Ext.form.field.Text', {
            fieldLabel: 'Code',
            name: 'code',
            allowBlank: false,
            flex: 1,
            emptyText: 'e.g. shoesdonation',
            maxLength: 64,
            maxLengthText: 'The maximum length for this field is 64 ',
            maskRe: /[^ ]/
        });

        this.nameLabelE = Ext.create('Ext.form.field.Text', {
            flex: 1,
            fieldLabel: 'Name',
            name: 'name',
            id: 'nameCategory',
            allowBlank: false,
            emptyText: 'e.g. Donation of shoes'
        });

        this.DescriptionLabelE = Ext.create('Ext.form.field.Text', {
            fieldLabel: 'Description',
            name: 'description',
            allowBlank: false,
            flex: 1,
            emptyText: 'e.g. Footwear in general, shoes, boots, etc.'
        });

        this.newLabelL = Ext.create('Ext.form.Label', {
            flex: 1,
            text: 'New Category',
            padding: '15 0 0 0',
            cls: 'header-h2'
        });

        this.saveButton = Ext.create('Ext.Button', {
            text: 'Save',
            margin: '0 10 0 10',
            cls: 'btn btn-green',
            id: 'attributeCreate'
        });

        this.cancelButton = Ext.create('Ext.Button', {
            text: 'Cancel',
            cls: 'btn btn-red',
            id: 'attributeCancelCreate'
        });

        this.addLabelButton = Ext.create('Ext.Button', {
            text: 'Add Category',
            cls: 'btn btn-green',
            id: 'addLabel'
        });

        this.labelsStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 100,
            storeId: 'labelsStore',
            fields: ['code', 'description', 'name'],
            proxy: {
                type: 'ajax',
                url: '',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: false
        });

        this.labelsTpl = new Ext.XTemplate(
            '<div class="attribute-list">',

            '<tpl for=".">',

            '<tpl if="xindex == 1">' +
                '<div class="attributes-title"><div class="inner"><h2>Categories</h2></div></div>' +
            '</tpl>' +

            '<div class="attribute-item">',

            '<div class="content">',

            '<div class="img">',
            '<img alt="Attribute image" src="/AIDRFetchManager/resources/img/AIDR/AIDR_EMBLEM_CMYK_COLOUR_HR.jpg" width="70" height="70">',
            '</div>',

            '<div class="info">',
            '<div class="styled-text-14" id="docCountField_{id}">Name:&nbsp;&nbsp;&nbsp;{name}</div>',
            '<div class="styled-text-14" id="docCountField_{id}">Code:&nbsp;&nbsp;&nbsp;{code}</div>',
            '<div class="styled-text-14" id="docCountField_{id}">Description:&nbsp;&nbsp;&nbsp;{description}</div>',
            '</div>',

            '</div>',
            '</div>',

            '</tpl>',

            '</div>'
        );

        this.labelsView = Ext.create('Ext.view.View', {
            store: this.labelsStore,
            id: 'labelsViewId',
            tpl: this.labelsTpl,
            itemSelector: 'li.crisesItem'
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
                                    id: 'nameInfo'
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
                                    id: 'codeInfo'
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0 0 0',
                            padding: '0 0 20 0',
                            cls: 'bordered-bottom',
                            items: [
                                this.descriptionE,
                                {
                                    border: false,
                                    bodyStyle: 'background:none',
                                    html: '<img src="/AIDRFetchManager/resources/img/info.png"/>',
                                    height: 22,
                                    width: 22,
                                    id: 'descriptionInfo'
                                }
                            ]
                        },
                        this.newLabelL,
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            items: [
                                this.nameLabelE
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0',
                            items: [
                                this.codeLabelE
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            margin: '5 0 0 0',
                            items: [
                                this.DescriptionLabelE
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            padding: '15 0 15 0',
                            cls: 'bordered-bottom',
                            items: [
                                this.addLabelButton
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
            this.editForm,
            this.labelsView,
            {
                xtype: 'container',
                layout: 'hbox',
                margin: '15 0 0 0',
                items: [
                    this.saveButton,
                    this.cancelButton
                ]
            }
        ];

        this.callParent(arguments);
    }

})
