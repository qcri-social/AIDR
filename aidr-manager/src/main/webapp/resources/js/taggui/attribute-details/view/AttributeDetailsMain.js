Ext.require([
    'TAGGUI.attribute-details.view.AttributeValuePanel'
]);

Ext.define('TAGGUI.attribute-details.view.AttributeDetailsMain', {
    extend: 'Ext.container.Container',
    alias: 'widget.attribute-details-main',

    layout: {
        type: 'vbox',
        align: 'left'
    },

    defaults: {
        width: '100%'
    },

    showDeleteButton: true,
    showRemoveClassifierButton: true,

    initComponent: function () {
        var me = this;

        this.codeValue = Ext.create('Ext.form.Label', {flex: 1});
        this.nameValue = Ext.create('Ext.form.Label', {flex: 1});
        this.typeValue = Ext.create('Ext.form.Label', {flex: 1});

        this.nameTextBox = Ext.create('Ext.form.field.Text', {
            flex: 1,
            allowBlank: false,
            hidden: true,
            listeners: {
                change: function(combo, newValue) {
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

        this.saveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            hidden: true,
            disabled: true,
            listeners: {
                click: function(btn){
                    me.attributeSave(btn, me);
                }
            }
        });

        this.cancelButton = Ext.create('Ext.Button', {
            text: 'Cancel',
            margin: '0 0 0 10',
            cls:'btn btn-green',
            hidden: true,
            listeners: {
                click: function(btn){
                    me.attributeCancel(btn, me);
                }
            }
        });

        this.editButton = Ext.create('Ext.Button', {
            text: 'Edit',
            cls:'btn btn-green',
            listeners: {
                click: function(btn){
                    me.attributeEdit(btn, me);
                }
            }
        });

        this.deleteButton = Ext.create('Ext.Button', {
            text: 'Delete',
            cls:'btn btn-red',
            margin: '0 0 0 7',
            hidden: !me.showDeleteButton,
            listeners: {
                click: function(btn){
                    me.attributeDeleteHandler(btn, me);
                }
            }
        });

        this.removeClassifierButton = Ext.create('Ext.Button', {
            text: 'Remove Classifier',
            cls:'btn btn-red',
            margin: '5 0',
            hidden: !me.showRemoveClassifierButton,
            listeners: {
                click: function(btn){
                    me.removeClassifierHandler(btn, me);
                }
            }
        });

        this.buttonsBlock = Ext.create('Ext.container.Container', {
            layout: 'hbox',
            margin: '5 10',
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

        this.noButtonsBlock = Ext.create('Ext.container.Container', {
            layout: 'hbox',
            margin: '5 10',
            hidden: true,
            width: 250,
            items: [
                {
                    xtype: 'container'
                }
            ]
        });

        this.valuesLable = Ext.create('Ext.container.Container', {
            width: '100%',
            html: '<div class="attributes-title"><div class="inner"><h2>Categories</h2></div></div>',
            hidden: true
        });

        this.labelsBlock = Ext.create('Ext.container.Container', {
            defaultType: 'attribute-value-view',
            flex: 1,
            layout: 'vbox',
            items: []
        });

        this.valuesSaveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            hidden: true,
            disabled: true,
            id: 'valuesSave',
            listeners: {
                click: function(btn){
                    me.valuesSave(btn, me);
                }
            }
        });

        this.valuesCancelButton = Ext.create('Ext.Button', {
            text: 'Cancel',
            margin: '0 0 0 10',
            cls:'btn btn-green',
            hidden: true,
            listeners: {
                click: function(btn){
                    me.valuesCancel(btn, me);
                }
            }
        });

        this.valuesEditButton = Ext.create('Ext.Button', {
            text: 'Edit',
            cls:'btn btn-green',
            hidden: true,
            listeners: {
                click: function(btn){
                    me.valuesEdit(btn, me);
                }
            }
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

        this.items = [
            {
                xtype: 'container',
                layout: 'vbox',
                items: [
                    this.codeBlock,
                    this.nameBlock,
                    this.typeBlock,
                    {
                        xtype: 'container',
                        layout:  'hbox',
                        items: [
                            this.buttonsBlock,
                            this.noButtonsBlock,
                            this.removeClassifierButton
                        ]
                    }
                ]
            },
            {
                xtype: 'container',
                width: '100%',
                html: '<div class="horizontalLine"></div>'
            },
            this.valuesLable,
            this.labelsBlock,
            this.valuesButtonsBlock
        ];

        this.callParent(arguments);
    },

    loadData: function (r) {
        var me = this,
            type;

        me.attributeName = r.name;
        me.nameValue.setText(r.name, false);
        me.nameTextBox.setValue(r.name);
        me.codeValue.setText("<b>" + r.code + "</b>", false);

        if (r.users && r.users.userID) {
            if (r.users.userID == 1) {
                type = 'Standard (shared)';
            } else {
                type = 'Custom';
            }
            if (r.users.userID == USER_ID) {
                me.buttonsBlock.show();
                me.valuesEditButton.show();
            } else {
                me.noButtonsBlock.show();
            }
        }
        me.typeValue.setText(type, false);

        if (r.nominalLabelCollection && Ext.isArray(r.nominalLabelCollection)) {
            me.valuesLable.show();
            me.valuesButtonsBlock.show();
            Ext.each(r.nominalLabelCollection, function (lbl) {
                me.labelsBlock.add({
                    name: lbl.name,
                    code: lbl.nominalLabelCode,
                    description: lbl.description,
                    labelId: lbl.nominalLabelID
                });
            });
        }
    },

    attributeDeleteHandler: function (btn, me) {
        Ext.MessageBox.confirm('Confirm Attribute Delete', 'Do you want to delete <b>"' + me.attributeName + '"</b>?',
            function (buttonId) {
                if (buttonId === 'yes') {
                    me.attributeDelete(me);
                }
            }
        );
    },

    attributeDelete: function (me) {
        me.saveButton.disable();

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/deleteAttribute.action',
            method: 'GET',
            params: {
                id: ATTRIBUTE_ID
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    AIDRFMFunctions.setAlert("Ok", ["Attribute was deleted successfully.", "You will be redirected to Tagger Home screen."]);

                    var maskRedirect = AIDRFMFunctions.getMask(true, 'Redirecting ...');
                    maskRedirect.show();

//                    wait for 3 sec to let user read information box
                    var isFirstRun = true;
                    Ext.TaskManager.start({
                        run: function () {
                            if (!isFirstRun) {
                                document.location.href = BASE_URL + '/protected/tagger-home';
                            }
                            isFirstRun = false;
                        },
                        interval: 3 * 1000
                    });
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    me.saveButton.enable();
                }
            },
            failure: function () {
                me.saveButton.enable();
            }
        });
    },

    removeClassifierHandler: function (btn, me) {
        Ext.MessageBox.confirm('Confirm Remove Classifier', 'Do you want to remove Classifier <b>"' + me.attributeName + '"</b>?',
            function (buttonId) {
                if (buttonId === 'yes') {
                    me.removeClassifier(me);
                }
            }
        );
    },

    removeClassifier: function (me) {
        me.removeClassifierButton.disable();

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/removeAttributeFromCrises.action',
            method: 'GET',
            params: {
                id: MODEL_FAMILY_ID
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    AIDRFMFunctions.setAlert("Ok", "Classifier was removed successfully.");
                    document.location.href = BASE_URL + '/protected/' + CRISIS_CODE +'/tagger-collection-details';
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    me.removeClassifierButton.enable();
                }
            },
            failure: function () {
                me.removeClassifierButton.enable();
            }
        });
    },

    attributeEdit: function (btn, me) {
        me.nameValue.hide();
        me.editButton.hide();

        me.nameTextBox.show();
        me.saveButton.show();
        me.cancelButton.show();
    },

    attributeCancel: function (btn, me) {
        me.nameValue.show();
        me.editButton.show();

        me.nameTextBox.hide();
        me.saveButton.hide();
        me.cancelButton.hide();

        me.nameTextBox.setValue(me.attributeName);
    },

    attributeSave: function (btn, me) {
        me.cancelButton.disable();
        me.deleteButton.disable();
        me.removeClassifierButton.disable();

        var attributeName = me.nameTextBox.getValue();

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/updateAttribute.action',
            method: 'POST',
            params: {
                attributeID: ATTRIBUTE_ID,
                attributeName: Ext.String.trim( attributeName )
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (resp) {
                var response = Ext.decode(resp.responseText);
                if (response.success) {
                    me.nameTextBox.hide();
                    me.saveButton.hide();
                    me.cancelButton.hide();

                    me.nameValue.setText(attributeName, false);
                    me.nameValue.show();
                    me.editButton.show();
                } else {
                    AIDRFMFunctions.setAlert("Error", 'Error while updating attribute in Tagger.');
                }
                me.cancelButton.enable();
                me.deleteButton.enable();
                me.removeClassifierButton.enable();
            },
            failure: function () {
                me.attributeCancel(null, me);
                me.cancelButton.enable();
                me.deleteButton.enable();
                me.removeClassifierButton.enable();
            }
        });
    },

    valuesEdit: function (btn, me) {
        var children = me.labelsBlock.items ? me.labelsBlock.items.items : [];
        Ext.each(children, function (child) {
            if (child.name != NA_CATEGORY_NAME) {
                child.edit();
            }
        });

        me.valuesEditButton.hide();
        me.valuesSaveButton.show();
        me.valuesCancelButton.show();
    },

    valuesSave: function (btn, me) {
        var children = me.labelsBlock.items ? me.labelsBlock.items.items : [];
        Ext.each(children, function (child) {
            if (child.name != NA_CATEGORY_NAME) {
                child.save();
            }
        });

        me.valuesEditButton.show();
        me.valuesSaveButton.hide();
        me.valuesSaveButton.disable();
        me.valuesCancelButton.hide();
    },

    valuesCancel: function (btn, me) {
        var children = me.labelsBlock.items ? me.labelsBlock.items.items : [];
        Ext.each(children, function (child) {
            if (child.name != NA_CATEGORY_NAME) {
                child.cancel();
            }
        });

        me.valuesEditButton.show();
        me.valuesSaveButton.hide();
        me.valuesSaveButton.disable();
        me.valuesCancelButton.hide();
    }

});