Ext.define('TAGGUI.new-custom-attribute.controller.NewCustomAttributeController', {
    extend: 'Ext.app.Controller',

    views: [
        'NewCustomAttributePanel'
    ],

    init: function () {

        this.control({

            'new-custom-attribute-view': {
                beforerender: this.beforeRenderView
            },

            "#nameInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: 'Give a name to your classifier.',
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#codeInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: 'Attribute code consists of alpha-numeric short code name to a classifier. ' +
                            'Spaces are not allowed in the code name.',
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#descriptionInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: 'Give a description to your classifier. Note that the description will be used in the question shown to volunteers/workers.',
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#attributeCancelCreate": {
                click: function (btn, e, eOpts) {
                    document.location.href = BASE_URL + '/protected/' + CRISIS_CODE + '/predict-new-attribute';
                }
            },

            "#attributeCreate": {
                click: function (btn, e, eOpts) {
                    this.isExist();
                }
            },

            "#addLabel": {
                click: function (btn, e, eOpts) {
                    this.addLabel();
                }
            },

            "#nameCustomClassifier": {
                blur: function (field, eOpts) {
                    this.generateCustomClassifierCode(field.getValue());
                }
            },

            "#nameCategory": {
                blur: function (field, eOpts) {
                    this.generateCategoryCode(field.getValue());
                }
            }

        });

    },

    generateCustomClassifierCode: function(value) {
        var me = this;

        var currentCode = me.mainComponent.codeE.getValue();
        if (currentCode != ''){
            return false;
        }

        var v = Ext.util.Format.trim(value);
        v = v.replace(/ /g, '_');
        v = Ext.util.Format.lowercase(v);

        var length = value.length;
        if (length > 64){
            length = 64;
        }

        var result = Ext.util.Format.substr(v, 0, length);
        me.mainComponent.codeE.setValue(result);
    },

    generateCategoryCode: function(value) {
        var me = this;

        var currentCode = me.mainComponent.codeLabelE.getValue();
        if (currentCode != ''){
            return false;
        }

        var v = Ext.util.Format.trim(value);
        v = v.replace(/ /g, '_');
        v = Ext.util.Format.lowercase(v);

        var length = value.length;
        if (length > 64){
            length = 64;
        }

        var result = Ext.util.Format.substr(v, 0, length);
        me.mainComponent.codeLabelE.setValue(result);
    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;

        var me = this;

//      create N/A category
        var labels = [],
            r = {};
        r.name = NA_CATEGORY_NAME;
        r.description = 'If these categories do not apply to this message, or you cannot be sure about which is the correct category';
        r.code = 'null';
        r.sequence = 999;
        labels.push(r);
        me.mainComponent.labelsStore.loadData(labels, true);
    },

    addLabel: function() {
        var me = this;

        var labels = me.mainComponent.labelsStore.getRange();

        var r = {},
            name = me.mainComponent.nameLabelE.getValue(),
            code = me.mainComponent.codeLabelE.getValue(),
            description = me.mainComponent.DescriptionLabelE.getValue(),
            error = false,
            msg;

//        find if label with code or name already exists, -1 means that record was not found
        var codeInStore = me.mainComponent.labelsStore.find('code', code);
        var nameInStore = me.mainComponent.labelsStore.find('name', name);

        if (!code) {
            msg = "This field is required";
            me.mainComponent.codeLabelE.markInvalid(msg);
            error = true;
        }
        if (!name) {
            msg = "This field is required";
            me.mainComponent.nameLabelE.markInvalid(msg);
            error = true;
        }
        if (!description) {
            msg = "This field is required";
            me.mainComponent.DescriptionLabelE.markInvalid(msg);
            error = true;
        }
        if (error){
            return false;
        }

        if (codeInStore != -1) {
//            fire code invalid
            msg = "Label with this code already exist for attribute";
            me.mainComponent.codeLabelE.markInvalid(msg);
            error = true;
        }
        if (nameInStore != -1) {
//            fire name invalid
            msg = "Label with this name already exist for attribute";
            me.mainComponent.nameLabelE.markInvalid(msg);
            error = true;
        }
        if (error){
            return false;
        }
        
        var l =  me.mainComponent.labelsStore.getCount();
        log.console(l);

        r.name = name;
        r.description = description;
        r.code = code;
        r.sequence = 100+l;

        me.clearAttributeFields();

        labels.push(r);

        me.mainComponent.labelsStore.loadData(labels, true);
        return true;
    },

    clearAttributeFields: function(){
        var me = this;
        me.mainComponent.nameLabelE.setValue();
        me.mainComponent.DescriptionLabelE.setValue();
        me.mainComponent.codeLabelE.setValue();
        me.mainComponent.nameLabelE.clearInvalid();
        me.mainComponent.DescriptionLabelE.clearInvalid();
        me.mainComponent.codeLabelE.clearInvalid();
    },

    isExist: function () {
        var me = this;

        var name = me.mainComponent.nameE.getValue(),
            code = me.mainComponent.codeE.getValue(),
            description = me.mainComponent.descriptionE.getValue(),
            error = false;

        if (!code) {
            msg = "This field is required";
            me.mainComponent.codeE.markInvalid(msg);
            error = true;
        }
        if (!name) {
            msg = "This field is required";
            me.mainComponent.nameE.markInvalid(msg);
            error = true;
        }
        if (!description) {
            msg = "This field is required";
            me.mainComponent.descriptionE.markInvalid(msg);
            error = true;
        }
        if (error){
            return false;
        }

        var mask = AIDRFMFunctions.getMask(true, 'Saving attribute ...');
        mask.show();

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/attribute-exists.action',
            method: 'GET',
            params: {
                code: code
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data == true){
//                        Attribute exist it Tagger
                        var msg = 'Attribute with this code already exist. Please select another code';
                        AIDRFMFunctions.setAlert('Error', msg);
                        me.mainComponent.codeE.markInvalid(msg);
                        mask.hide();
                    } else {
                        me.saveAttribute(name, code, description, mask);
                    }
                } else {
                    mask.hide();
                    AIDRFMFunctions.setAlert('Error', 'Error in Tagger while validating attribute code');
                    AIDRFMFunctions.reportIssue(response);
                }
            }
        });
    },

    saveAttribute: function (name, code, description, mask) {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/createAttribute.action',
            method: 'POST',
            params: {
                name: Ext.String.trim( name ),
                code: Ext.String.trim( code ),
                description: Ext.String.trim( description )
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success && resp.data) {
                    var labels = me.mainComponent.labelsStore.getRange();

                    me.addAttributeToCrises(resp.data.nominalAttributeID, name);

                    if (labels.length == 0) {
//                        redirect because attribute was created and there is no labels for it
                          me.redirectAfterSave(mask);
                    } else {
                        me.labelsCount = labels.length;
                        Ext.Array.each(labels, function(r, index) {
                            me.saveLabel(r.data.name, r.data.code, r.data.description, resp.data.nominalAttributeID, r.data.sequence, mask);
                        });
                    }
                } else {
                    mask.hide();
                    AIDRFMFunctions.setAlert('Error', 'Error while saving attribute in Tagger');
                    AIDRFMFunctions.reportIssue(response);
                }
            }
        });
    },

    saveLabel: function (name, code, description, attributeID, sequence, mask) {
        var me = this;
        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/createLabel.action',
            method: 'POST',
            params: {
                name: name,
                nominalLabelCode: Ext.String.trim( code ),
                description: Ext.String.trim( description ),
                nominalAttributeID: attributeID,
                sequence: sequence
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success && resp.data) {
                    me.labelsCount = me.labelsCount - 1;
                    if (me.labelsCount == 0) {
                        me.redirectAfterSave(mask);
                    }
                } else {
                    mask.hide();
                    AIDRFMFunctions.setAlert('Error', 'Error while saving labels for attribute in Tagger');
                    AIDRFMFunctions.reportIssue(response);
                }
            }
        });
    },

    addAttributeToCrises: function (id, name) {
        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/addAttributeToCrisis.action',
            method: 'GET',
            params: {
                crisesId: CRISIS_ID,
                attributeId: id,
                isActive: true
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success && resp.data) {
                    AIDRFMFunctions.setAlert("Ok", '"' + name + '" has been added to "' + CRISIS_NAME + '" crisis.');
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            }
        });
    },

    redirectAfterSave: function(mask) {
        AIDRFMFunctions.setAlert("Ok", ["Attribute created successfully.", 'You will be redirected to Predict a new attribute screen.']);
        mask.hide();

        var maskRedirect = AIDRFMFunctions.getMask(true, 'Redirecting ...');
        maskRedirect.show();

//      wait for 3 sec to let user read information box
        var isFirstRun = true;
        Ext.TaskManager.start({
            run: function () {
                if (!isFirstRun) {
                    document.location.href = BASE_URL + '/protected/' + CRISIS_CODE + '/predict-new-attribute';
                }
                isFirstRun = false;
            },
            interval: 3 * 1000
        });
    }

});