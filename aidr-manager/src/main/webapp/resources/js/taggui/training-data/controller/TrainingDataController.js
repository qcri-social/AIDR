Ext.define('TAGGUI.training-data.controller.TrainingDataController', {
    extend: 'Ext.app.Controller',

    views: [
        'TrainingDataPanel'
    ],
    // href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/' + MODEL_ID + '/' + MODEL_FAMILY_ID +/training-examples
    init: function () {

        this.control({

            'training-data-view': {
                beforerender: this.beforeRenderView
            },

            "#addNewTrainingData": {
                click: function (btn, e, eOpts) {
                    this.addNewTrainingData();
                }
            },
            'button[action=deleteTrainingExample]': {
                click: this.deleteTrainingExample
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
        taggerCollectionDetailsController = this;

        this.loadModelData();
    },

    addNewTrainingData: function() {
        document.location.href = BASE_URL + "/protected/" + CRISIS_CODE + '/' + MODEL_ID + '/'
            + MODEL_FAMILY_ID + '/' + this.mainComponent.nominalAttributeId +'/training-examples';
    },

    loadModelData: function() {
        var me = this,
            status = '',
            detailsForModel = '';

        if (MODEL_ID && MODEL_ID != 0) {
            status = 'Running';
            detailsForModel = '<a href="' + BASE_URL +  '/protected/' + CRISIS_CODE + '/' + MODEL_ID + '/model-details">Details of running classifier &raquo;</a>';

            Ext.Ajax.request({
                url: BASE_URL + '/protected/tagger/getAllLabelsForModel.action',
                method: 'GET',
                params: {
                    id: MODEL_ID
                },
                headers: {
                    'Accept': 'application/json'
                },
                success: function (response) {
                    var resp = Ext.decode(response.responseText);
                    if (resp.success && resp.data) {
                        var count = resp.data.length;
                        if (count > 0) {
                            var totalMessages = 0,
                                totalExamples = 0,
                                nominalAttributeId = 0;

                            Ext.Array.each(resp.data, function(r, index) {
//                              do not count any data from labels with code == null
                                if (!r.nominalLabel || !r.nominalLabel.nominalLabelCode || r.nominalLabel.nominalLabelCode == 'null'){
                                    return true;
                                }

                                if (r.classifiedDocumentCount && r.classifiedDocumentCount > 0) {
                                    totalMessages += r.classifiedDocumentCount;
                                }
                                if (r.trainingDocuments && r.trainingDocuments > 0) {
                                    totalExamples += r.trainingDocuments;
                                }

                                nominalAttributeId = r.nominalAttributeId;
                            });

                            me.mainComponent.nominalAttributeId = nominalAttributeId;

                            me.mainComponent.taggerDescription.setText('Status: <b>' + status + '</b>. ' +
                                'Has classified <b>' + totalMessages + '</b> messages.&nbsp;' + detailsForModel, false);
                            me.mainComponent.taggerDescription2line.setText('<b>' + totalExamples + '</b> training examples. Click on a message to see/edit details', false);
                        }
                    } else {
                        AIDRFMFunctions.setAlert("Error", resp.message);
                    }
                },
                failure: function () {
                    AIDRFMFunctions.setAlert("Error", "System is down or under maintenance. For further inquiries please contact admin.");
                }
            });
        } else {
            me.mainComponent.breadcrumbs.setText('<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/tagger-home">Tagger</a><span>&nbsp;>&nbsp;</span>' +
                '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">' + CRISIS_NAME + '</a><span>&nbsp;>&nbsp;' +
                MODEL_NAME + '&nbsp;>&nbsp;Training data</span></div>', false);
        }
    },

    deleteTrainingExample: function(button){
        if (!button.exampleId){
            AIDRFMFunctions.setAlert("Error", "Error while delete training example. Document Id not available.");
        }

        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/deleteTrainingExample.action',
            method: 'GET',
            params: {
                id: button.exampleId
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    AIDRFMFunctions.setAlert("REMOVED", "Training Example is removed successfully. Note: this removal will be reflected on the automatic classifier the next time it is retrained.");
                    me.mainComponent.trainingDataStore.load();
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            },
            failure: function () {
                AIDRFMFunctions.setAlert("Error", "System is down or under maintenance. For further inquiries please contact admin.");
            }
        });
    }

});