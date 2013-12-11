Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'TAGGUI.attribute-details.view.AttributeValuePanel'
]);

Ext.define('TAGGUI.attribute-details.view.AttributeDetailsPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.attribute-details-view',
    border: false,
    bodyPadding: 40,
    margin: '25 0',
    width: 1040,
    layout: {
        type: 'vbox',
        align: 'left'
    },

    defaults: {
        width: '100%'
    },

    attributeName: '',

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/tagger-home">Tagger</a><span>&nbsp;>&nbsp;Attribute details</span></div>',
            margin: 0,
            padding: 0
        });

        this.taggerTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Details for attribute',
            flex: 1
        });

        this.horisontalLine = Ext.create('Ext.container.Container', {
            width: '100%',
            html: '<div class="horisontalLine"></div>'
        });

        this.saveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'attributeSave',
            hidden: true,
            disabled: true
        });

        this.cancelButton = Ext.create('Ext.Button', {
            text: 'Cancel',
            margin: '0 0 0 10',
            cls:'btn btn-green',
            id: 'attributeCancel',
            hidden: true
        });

        this.editButton = Ext.create('Ext.Button', {
            text: 'Edit',
            cls:'btn btn-green',
            id: 'attributeEdit'
        });

        this.deleteButton = Ext.create('Ext.Button', {
            text: 'Delete',
            cls:'btn btn-red',
            id: 'attributeDelete',
            margin: '0 0 0 7'
        });

        this.buttonsBlock = Ext.create('Ext.container.Container', {
            layout: 'hbox',
            margin: '5 0',
            padding: '0 10',
            hidden: true,
            items: [
                {
                    xtype: 'container',
                    layout: 'hbox',
                    margin: '0 0 0 0',
                    width: 250,
                    items: [
                        this.editButton,
                        this.saveButton,
                        this.cancelButton
                    ]
                },
                this.deleteButton
            ]
        });

        this.valuesSaveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'valuesSave',
            hidden: true,
            disabled: true
        });

        this.valuesCancelButton = Ext.create('Ext.Button', {
            text: 'Cancel',
            margin: '0 0 0 10',
            cls:'btn btn-green',
            id: 'valuesCancel',
            hidden: true
        });

        this.valuesEditButton = Ext.create('Ext.Button', {
            text: 'Edit',
            cls:'btn btn-green',
            id: 'valuesEdit',
            hidden: true
        });

        this.valuesButtonsBlock = Ext.create('Ext.container.Container', {
            layout: 'hbox',
            margin: '15 0',
            padding: '0 10',
            hidden: true,
            items: [
                {
                    xtype: 'container',
                    layout: 'hbox',
                    margin: '0 0 0 0',
                    width: 250,
                    items: [
                        this.valuesEditButton,
                        this.valuesSaveButton,
                        this.valuesCancelButton
                    ]
                }
            ]
        });

        this.valuesLable = Ext.create('Ext.container.Container', {
            width: '100%',
            html: '<div class="attributes-title"><div class="inner"><h2>Values</h2></div></div>',
            hidden: true
        });

        this.codeValue = Ext.create('Ext.form.Label', {flex: 1});
        this.nameValue = Ext.create('Ext.form.Label', {flex: 1});
        this.typeValue = Ext.create('Ext.form.Label', {flex: 1});

        this.nameTextBox = Ext.create('Ext.form.field.Text', {
            flex: 1,
            allowBlank: false,
            hidden: true,
            listeners: {
            change: function(combo, newValue, oldValue, eOpts) {
                if (newValue == '' || newValue == me.attributeName) {
                    me.saveButton.disable();
                } else {
                    me.saveButton.enable();
                }
            }
        }
        });

        this.codeBlock = Ext.create('Ext.container.Container', {
            defaultType: 'label',
            layout: 'hbox',
            height: 22,
            margin: '2 0',
            padding: '0 10',
            items: [
                {
                    width: 75,
                    html: '<b>Code:</b>'
                },
                this.codeValue
            ]
        });

        this.nameBlock = Ext.create('Ext.container.Container', {
            defaultType: 'label',
            layout: 'hbox',
            height: 22,
            margin: '2 0',
            padding: '0 10',
            items: [
                {
                    width: 75,
                    text: 'Name:'
                },
                this.nameValue,
                this.nameTextBox
            ]
        });

        this.typeBlock = Ext.create('Ext.container.Container', {
            defaultType: 'label',
            layout: 'hbox',
            height: 22,
            margin: '2 0',
            padding: '0 10',
            items: [
                {
                    width: 75,
                    text: 'Type:'
                },
                this.typeValue
            ]
        });

        this.labelsBlock = Ext.create('Ext.container.Container', {
            defaultType: 'attribute-value-view',
            flex: 1,
            layout: 'vbox',
            items: []
        });

        this.items = [
            this.breadcrumbs,
            {
                xtype: 'container',
                margin: '5 0 0 0',
                html: '<div class="horisontalLine"></div>'
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
            {
                xtype: 'container',
                layout: 'vbox',
                items: [
                    {
                        xtype: 'container',
                        width: '100%',
                        html: '<div class="horisontalLine"></div>'
                    },
                    this.codeBlock,
                    this.nameBlock,
                    this.typeBlock,
                    this.buttonsBlock
                ]
            },
            {
                xtype: 'container',
                width: '100%',
                html: '<div class="horisontalLine"></div>'
            },
            this.valuesLable,
            this.labelsBlock,
            this.valuesButtonsBlock
        ];

        this.callParent(arguments);
    }

})