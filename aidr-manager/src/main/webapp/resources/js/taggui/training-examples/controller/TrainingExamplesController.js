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
                    btn.setDisabled();
                    this.saveLabels();
                }
            },

            "#skipTask": {
                click: function (btn, e, eOpts) {
                    this.skipTaskHandler();
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
                '<a href="' + BASE_URL + '/protected/home">My Collections</a><span>&nbsp;>&nbsp;</span>' +
                '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">' + CRISIS_NAME + '</a><span>&nbsp;>&nbsp;' +
                MODEL_NAME + '&nbsp;>&nbsp;Tag more '+ COLLECTION_TYPES[TYPE]["plural"] + '</span></div>', false);
        }
    },

    loadData: function(fromSkipAction) {
        var me = this;

        var mask = AIDRFMFunctions.getMask(true);
        AIDRFMFunctions.showMask(mask);

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/getAssignableTask.action',
            method: 'GET',
            params: {
                id: CRISIS_ID
//                id: 117
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
                            me.mainComponent.saveLabelsButton.disable();
                            me.mainComponent.skipTaskButton.disable();
                        }
                        if (obj && obj[0]) {
                        	if (fromSkipAction){
                                var skippedDocumentID = me.mainComponent.documentID;
                        		me.skipTask(skippedDocumentID);
                            }
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
                                    if (NOMINAL_ATTRIBUTE_ID == 0 || attr.nominalAttributeID == NOMINAL_ATTRIBUTE_ID) {
//                                    if (NOMINAL_ATTRIBUTE_ID == 0 || attr.nominalAttributeID == 533) {
                                        var labelPanel = Ext.create('TAGGUI.training-examples.view.LabelPanel', {});
                                        labelPanel.showData(attr);
                                        me.mainComponent.optionPanel.add(labelPanel);
                                    }
                                });
                            }
                            /*
                            if (fromSkipAction){
                                me.skipTask();
                            }*/
                        } else{
                            AIDRFMFunctions.setAlert("Error", "No "+ COLLECTION_TYPES[TYPE]["plural"] + " to tag available for this crisis. Please come back to this page later.");
                            me.mainComponent.documentTextLabel.setText("No "+ COLLECTION_TYPES[TYPE]["plural"] + " to tag available for this crisis. Please come back to this page later.", false);
                            me.mainComponent.buttonsBlock.removeAll();
                        }
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
                AIDRFMFunctions.hideMask(mask);
            },
            failure: function () {
                AIDRFMFunctions.hideMask(mask);
            }
        });
    },

    cancelTraining: function() {
        document.location.href = BASE_URL +  '/protected/' + CRISIS_CODE + '/' + MODEL_ID + '/' + MODEL_FAMILY_ID
            + '/' + NOMINAL_ATTRIBUTE_ID + '/training-data';
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

        var mask = AIDRFMFunctions.getMask(true);
        AIDRFMFunctions.showMask(mask);

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
                    taskcompleted: Ext.Date.format(new Date(), "c"),
                    attributeID: NOMINAL_ATTRIBUTE_ID
                },
                headers: {
                    'Accept': 'application/json'
                },
                success: function (response) {
                    var resp = Ext.decode(response.responseText);
                    if (resp.success) {
                        me.loadData();
                    } else {
                        me.loadData();
                        //AIDRFMFunctions.setAlert("Info", "Task has been saved.");
                    }
                    AIDRFMFunctions.hideMask(mask);
                },
                failure: function () {
                    AIDRFMFunctions.hideMask(mask);
                }
            });
        });
    },

    skipTaskHandler: function(){
        var me = this;
        me.mainComponent.previousDocumentID = me.mainComponent.documentID;
        me.loadData(true);
    },

    skipTask: function(skippedDocumentID) {
        var me = this;

        if (!me.mainComponent.previousDocumentID) {
            AIDRFMFunctions.setAlert("Error", "Can not find Document Id to skip it");
            return false;
        }

        var mask = AIDRFMFunctions.getMask(true);
        AIDRFMFunctions.showMask(mask);

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/saveTaskAnswer.action',
            method: 'POST',
            params: {
                //documentID: me.mainComponent.documentID,
            	documentID: skippedDocumentID,
                crisisID: CRISIS_ID,
                category: Ext.String.trim('null'),
                taskcreated: me.mainComponent.createDate,
                taskcompleted: Ext.Date.format(new Date(), "c"),
                attributeID: NOMINAL_ATTRIBUTE_ID
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (!resp.success) {
                    AIDRFMFunctions.setAlert("Error", "Error while skip task.");
                }
                AIDRFMFunctions.hideMask(mask);
            },
            failure: function () {
                AIDRFMFunctions.hideMask(mask);
            }
        });
    }

});