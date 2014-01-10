Ext.define('TAGGUI.attribute-details.view.AttributeValuePanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.attribute-value-view',

    name: '',
    code: '',
    description: '',

    layout: 'hbox',
    border: true,
    cls: 'horizontalLineTop',
    padding: '10 0 10 0',
    width: '100%',
    labelId: 0,

    initComponent: function () {
        var me = this;

        this.nameValue = Ext.create('Ext.form.Label', {
            flex: 1,
            cls: 'styled-text-14',
            text: this.name
        });

        this.nameTextBox = Ext.create('Ext.form.field.Text', {
            flex: 1,
            allowBlank: false,
            hidden: true,
            value: this.name,
            listeners: {
                change: function(combo, newValue) {
                    if (newValue != '' && newValue != me.name) {
                        me.enableParentSaveButton();
                    }
                }
            }
        });

        this.descriptionTextBox = Ext.create('Ext.form.field.Text', {
            flex: 1,
            allowBlank: false,
            hidden: true,
            value: this.description,
            listeners: {
                change: function(combo, newValue) {
                    if (newValue != '' && newValue != me.description) {
                        me.enableParentSaveButton();
                    }
                }
            }
        });

        this.codeValue = Ext.create('Ext.form.Label', {
            flex: 1,
            cls: 'styled-text-14',
            text: this.code
        });

        this.descriptionValue = Ext.create('Ext.form.Label', {
            flex: 1,
            cls: 'styled-text-14',
            text: this.description
        });

        this.items = [
            {
                xtype: 'image',
                width: 70,
                height: 70,
                src: '/AIDRFetchManager/resources/img/AIDR/AIDR_EMBLEM_CMYK_COLOUR_HR.jpg'
            },
            {
                xtype: 'container',
                layout: 'vbox',
                padding: '5 0 0 20',
                items: [
                    {
                        xtype: 'container',
                        layout: 'hbox',
                        height: 22,
                        items: [
                            {
                                xtype: 'label',
                                width: 100,
                                html: 'Name:',
                                cls: 'styled-text-14'
                            },
                            this.nameValue,
                            this.nameTextBox
                        ]
                    },
                    {
                        xtype: 'container',
                        layout: 'hbox',
                        height: 22,
                        items: [
                            {
                                xtype: 'label',
                                width: 100,
                                html: 'Code:',
                                cls: 'styled-text-14'
                            },
                            this.codeValue
                        ]
                    },
                    {
                        xtype: 'container',
                        layout: 'hbox',
                        height: 22,
                        items: [
                            {
                                xtype: 'label',
                                width: 100,
                                html: 'Description:',
                                cls: 'styled-text-14'
                            },
                            this.descriptionValue,
                            this.descriptionTextBox
                        ]
                    }
                ]
            }
        ];

        this.callParent(arguments);
    },

    edit: function(){
        this.nameValue.hide();
        this.nameTextBox.show();

        this.descriptionValue.hide();
        this.descriptionTextBox.show();
    },

    cancel: function(){
        this.nameValue.show();
        this.nameTextBox.hide();
        this.nameTextBox.setValue(this.name);

        this.descriptionValue.show();
        this.descriptionTextBox.hide();
        this.descriptionTextBox.setValue(this.name);
    },

    save: function(){
        var me = this;

        var newName = this.nameTextBox.getValue();
        var newDescription = this.descriptionTextBox.getValue();
        if ((this.name != newName && newName != '') ||
            (this.description != newDescription && newDescription != '')) {

            Ext.Ajax.request({
                url: BASE_URL + '/protected/tagger/updateLabel.action',
                method: 'POST',
                params: {
                    labelID: me.labelId,
                    labelName: Ext.String.trim( newName ),
                    labelDescription: Ext.String.trim( newDescription ),
                    attributeID: ATTRIBUTE_ID
                },
                headers: {
                    'Accept': 'application/json'
                },
                success: function (resp) {
                    var response = Ext.decode(resp.responseText);
                    if (!response.success) {
                        AIDRFMFunctions.setAlert("Error", 'Error while updating label in Tagger.');
                    }
                }
            });

            this.nameValue.setText(newName);
            this.name = newName;

            this.descriptionValue.setText(newDescription);
            this.description = newDescription;
        }
        this.cancel();
    },

    enableParentSaveButton: function() {
        Ext.getCmp('valuesSave').enable();
    }

});