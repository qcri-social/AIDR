Ext.define('TAGGUI.training-examples.controller.TrainingExamplesController', {
    extend: 'Ext.app.Controller',

    views: [
        'TrainingExamplesPanel'
    ],

    init: function () {

        this.control({

            'training-examples-view': {
                beforerender: this.beforeRenderView
            },

            "#saveLabels": {
                click: function (btn, e, eOpts) {
                    this.saveLabels();
                }
            },

            "#skipTask": {
                click: function (btn, e, eOpts) {
                    this.skipTask();
                }
            },

            "#cancel": {
                click: function (btn, e, eOpts) {
                    this.cancelTraining();
                }
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
        taggerCollectionDetailsController = this;
        var me = this;

        this.loadData();

        if (!MODEL_ID || MODEL_ID == 0) {
            me.mainComponent.breadcrumbs.setText('<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/tagger-home">Tagger</a><span>&nbsp;>&nbsp;</span>' +
                '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">' + CRISIS_NAME + '</a><span>&nbsp;>&nbsp;' +
                MODEL_NAME + '&nbsp;>&nbsp;New training example</span></div>', false);
        }
    },

    loadData: function() {
        var me = this;

        var mask = AIDRFMFunctions.getMask(true);
        mask.show();

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/getAssignableTask.action',
            method: 'GET',
            params: {
                id: CRISIS_ID
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data) {
                        try {
                            var obj = Ext.JSON.decode(resp.data);
                        } catch (e) {
                            AIDRFMFunctions.setAlert("Error", "Examples not available for this crisis.");
                            me.mainComponent.saveLabelsButton.disable();
                            me.mainComponent.skipTaskButton.disable();
                        }
                        if (obj) {
                            me.mainComponent.optionPanel.removeAll();
                            var r = obj[0];
                            me.mainComponent.documentID = r.documentID;
                            me.mainComponent.createDate = Ext.Date.format(new Date(), "c");
                            if (r.data){
                                var tweetData = Ext.JSON.decode(r.data);
                                me.mainComponent.documentTextLabel.setText(tweetData.text.linkify(), false);
                            }
                            if (r.attributeInfo){
                                Ext.each(r.attributeInfo, function (attr) {
//                                    Show labels from only one Category (the one user clicks on previous screen).
                                    if (attr.nominalAttributeID == NOMINAL_ATTRIBUTE_ID) {
                                        var labelPanel = Ext.create('TAGGUI.training-examples.view.LabelPanel', {});
                                        labelPanel.showData(attr);
                                        me.mainComponent.optionPanel.add(labelPanel);
                                    }
                                });
                            }
                        }
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
                mask.hide();
            },
            failure: function () {
                mask.hide();
                AIDRFMFunctions.setAlert("Error", "System is down or under maintenance. For further inquiries please contact admin.");
            }
        });
    },

    cancelTraining: function() {
        document.location.href = BASE_URL +  '/protected/' + CRISIS_CODE + '/' + MODEL_ID + '/' + MODEL_FAMILY_ID + '/training-data';
    },

    saveLabels: function() {
        var me = this;

        if (!me.mainComponent.documentID) {
            AIDRFMFunctions.setAlert("Error", "Can not find Document Id");
            return false;
        }

        var notSelected = false;
        var children = me.mainComponent.optionPanel.items ? me.mainComponent.optionPanel.items.items : [];
        Ext.each(children, function (child) {
            var values = child.optionRG.getChecked();
            if (values.length == 0) {
                AIDRFMFunctions.setAlert("Error", "No label has been selected");
                notSelected = true;
            }
        });

        if (notSelected){
            return false;
        }

        Ext.each(children, function (child) {
            var values = child.optionRG.getChecked();
            Ext.Ajax.request({
                url: BASE_URL + '/protected/tagger/saveTaskAnswer.action',
                method: 'POST',
                params: {
                    documentID: me.mainComponent.documentID,
                    crisisID: CRISIS_ID,
                    category: Ext.String.trim( values[0].code ),
                    taskcreated: me.mainComponent.createDate,
                    taskcompleted: Ext.Date.format(new Date(), "c")
                },
                headers: {
                    'Accept': 'application/json'
                },
                success: function (response) {
                    var resp = Ext.decode(response.responseText);
                    if (resp.success) {
                        me.loadData();
                    } else {
                        AIDRFMFunctions.setAlert("Error", "Error while saving task.");
                    }
                }
            });
        });
    },

    skipTask: function() {
        var me = this;

        if (!me.mainComponent.documentID) {
            AIDRFMFunctions.setAlert("Error", "Can not find Document Id");
            return false;
        }

        var mask = AIDRFMFunctions.getMask(true);
        mask.show();

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/skipTask.action',
            method: 'GET',
            params: {
                id: me.mainComponent.documentID
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data) {
                        if (resp.data == 'success') {
                            me.loadData();
                        } else {
                            AIDRFMFunctions.setAlert("Error", "Error while skipping task.");
                        }
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
                mask.hide();
            },
            failure: function () {
                mask.hide();
                AIDRFMFunctions.setAlert("Error", "System is down or under maintenance. For further inquiries please contact admin.");
            }
        });
    }

});