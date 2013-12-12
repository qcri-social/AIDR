Ext.define('TAGGUI.attribute-details.controller.AttributeDetailsController', {
    extend: 'Ext.app.Controller',

    views: [
        'AttributeDetailsPanel'
    ],

    init: function () {

        this.control({

            'attribute-details-view': {
                beforerender: this.beforeRenderView
            },

            "#attributeDelete": {
                click: function (btn, e, eOpts) {
                    this.attributeDeleteHandler();
                }
            },

            "#attributeSave": {
                click: function (btn, e, eOpts) {
                    this.attributeSave();
                }
            },

            "#attributeEdit": {
                click: function (btn, e, eOpts) {
                    this.attributeEdit();
                }
            },

            "#attributeCancel": {
                click: function (btn, e, eOpts) {
                    this.attributeCancel();
                }
            },

            "#valuesSave": {
                click: function (btn, e, eOpts) {
                    this.valuesSave();
                }
            },

            "#valuesEdit": {
                click: function (btn, e, eOpts) {
                    this.valuesEdit();
                }
            },

            "#valuesCancel": {
                click: function (btn, e, eOpts) {
                    this.valuesCancel();
                }
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
        attributeDetailsController = this;
        var me = this;

        this.getAttributeInfo();
    },

    getAttributeInfo: function () {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/getAttributeInfo.action',
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
                    if (resp.data) {
                        var r = resp.data,
                            type;
                        me.mainComponent.taggerTitle.setText("Details for attribute \"" + r.name + "\"");
                        me.mainComponent.attributeName = r.name;
                        me.mainComponent.nameValue.setText(r.name, false);
                        me.mainComponent.nameTextBox.setValue(r.name);
                        me.mainComponent.codeValue.setText("<b>" + r.code + "</b>", false);

                        if (r.users && r.users.userID) {
                            if (r.users.userID == 1) {
                                type = 'Standard (shared)';
                            } else {
                                type = 'Custom';
                            }
                            if (r.users.userID == USER_ID) {
                                me.mainComponent.buttonsBlock.show();
                                me.mainComponent.valuesEditButton.show();
                            }
                        }
                        me.mainComponent.typeValue.setText(type, false);

                        if (r.nominalLabelCollection && Ext.isArray(r.nominalLabelCollection)) {
                            me.mainComponent.valuesLable.show();
                            me.mainComponent.valuesButtonsBlock.show();
                            Ext.each(r.nominalLabelCollection, function (lbl) {
                                me.mainComponent.labelsBlock.add({
                                    name: lbl.name,
                                    code: lbl.nominalLabelCode,
                                    description: lbl.description,
                                    labelId: lbl.nominalLabelID
                                });
                            });
                        }
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            },
            failure: function () {
                AIDRFMFunctions.setAlert("Error", "System is down or under maintenance. For further inquiries please contact admin.");
            }
        });
    },

    attributeDeleteHandler: function () {
        var me = this;

        Ext.MessageBox.confirm('Confirm Attribute Delete', 'Do you want to delete <b>"' + me.mainComponent.attributeName + '"</b>?',
            function (buttonId) {
                if (buttonId === 'yes') {
                    attributeDetailsController.attributeDelete();
                }
            }
        );
    },

    attributeDelete: function () {
        var me = this;

        me.mainComponent.saveButton.disable();

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
                    me.mainComponent.saveButton.enable();
                }
            },
            failure: function () {
                AIDRFMFunctions.setAlert("Error", "System is down or under maintenance. For further inquiries please contact admin.");
                me.mainComponent.saveButton.enable();
            }
        });
    },

    attributeEdit: function () {
        var me = this;
        me.mainComponent.nameValue.hide();
        me.mainComponent.editButton.hide();

        me.mainComponent.nameTextBox.show();
        me.mainComponent.saveButton.show();
        me.mainComponent.cancelButton.show();
    },

    attributeSave: function () {
        var me = this;

        me.mainComponent.cancelButton.disable();
        me.mainComponent.deleteButton.disable();

        var attributeName = me.mainComponent.nameTextBox.getValue();

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
                    me.mainComponent.nameTextBox.hide();
                    me.mainComponent.saveButton.hide();
                    me.mainComponent.cancelButton.hide();

                    me.mainComponent.nameValue.setText(attributeName, false);
                    me.mainComponent.nameValue.show();
                    me.mainComponent.editButton.show();
                } else {
                    AIDRFMFunctions.setAlert("Error", 'Error while updating attribute in Tagger.');
                }
                me.mainComponent.cancelButton.enable();
                me.mainComponent.deleteButton.enable();
            },
            failure: function () {
                AIDRFMFunctions.setAlert("Error", "System is down or under maintenance. For further inquiries please contact admin.");
                me.attributeCancel();
                me.mainComponent.cancelButton.enable();
                me.mainComponent.deleteButton.enable();
            }
        });

    },

    attributeCancel: function () {
        var me = this;
        me.mainComponent.nameValue.show();
        me.mainComponent.editButton.show();

        me.mainComponent.nameTextBox.hide();
        me.mainComponent.saveButton.hide();
        me.mainComponent.cancelButton.hide();

        me.mainComponent.nameTextBox.setValue(me.mainComponent.attributeName);
    },

    valuesEdit: function () {
        var me = this;

        var children = me.mainComponent.labelsBlock.items ? me.mainComponent.labelsBlock.items.items : [];
        Ext.each(children, function (child) {
            if (child.name != NA_CATEGORY_NAME) {
                child.edit();
            }
        });

        me.mainComponent.valuesEditButton.hide();
        me.mainComponent.valuesSaveButton.show();
        me.mainComponent.valuesCancelButton.show();
    },

    valuesSave: function () {
        var me = this;

        var children = me.mainComponent.labelsBlock.items ? me.mainComponent.labelsBlock.items.items : [];
        Ext.each(children, function (child) {
            if (child.name != NA_CATEGORY_NAME) {
                child.save();
            }
        });

        me.mainComponent.valuesEditButton.show();
        me.mainComponent.valuesSaveButton.hide();
        me.mainComponent.valuesSaveButton.disable();
        me.mainComponent.valuesCancelButton.hide();
    },

    valuesCancel: function () {
        var me = this;

        var children = me.mainComponent.labelsBlock.items ? me.mainComponent.labelsBlock.items.items : [];
        Ext.each(children, function (child) {
            if (child.name != NA_CATEGORY_NAME) {
                child.cancel();
            }
        });

        me.mainComponent.valuesEditButton.show();
        me.mainComponent.valuesSaveButton.hide();
        me.mainComponent.valuesSaveButton.disable();
        me.mainComponent.valuesCancelButton.hide();
    }

});