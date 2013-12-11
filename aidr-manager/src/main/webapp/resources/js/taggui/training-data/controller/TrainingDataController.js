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
        document.location.href = BASE_URL + "/protected/" + CRISIS_CODE + '/' + MODEL_ID + '/' + MODEL_FAMILY_ID +'/training-examples';
    },

    loadModelData: function() {
        var me = this,
            status = '',
            detailsForModel = '';

        if (MODEL_ID && MODEL_ID != 0) {
            status = 'Running';
            detailsForModel = '<a href="' + BASE_URL +  '/protected/' + CRISIS_CODE + '/' + MODEL_ID + '/model-details">Details for running model &raquo;</a>';

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
                                totalExamples = 0;
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
                            });

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
    }

});