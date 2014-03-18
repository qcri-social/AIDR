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
            + MODEL_FAMILY_ID + '/' + ATTRIBUTE_ID +'/training-examples';
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
                           // var self = this;
                            //me.getRetrainingThreshold()
                            me.getRetrainingThreshold(totalMessages, count);
                            me.mainComponent.taggerDescription.setText('Status: <b>' + status + '</b>. ' +
                                'Machine-tagged items: <b>' + totalMessages + '</b>.&nbsp;' + detailsForModel, false);


                          //  me.mainComponent.taggerDescription2line.setText('<b>' + totalExamples + '</b> training examples. Note: Value \"N/A\" doesn\'t count as training example.', false);
                        }
                    } else {
                        AIDRFMFunctions.setAlert("Error", resp.message);
                    }
                }
            });
        } else {
            this.getRetrainingThreshold(0,0);
            me.mainComponent.breadcrumbs.setText('<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/tagger-home">My Classifiers</a><span>&nbsp;>&nbsp;</span>' +
                '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">' + CRISIS_NAME + '</a><span>&nbsp;>&nbsp;' +
                MODEL_NAME + '&nbsp;>&nbsp;Human-tagged items</span></div>', false);

        }
    },

    getRetrainingThreshold: function(trainingExamplesCount, countTrainingExample){
        var me = this;
        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/getRetrainThreshold.action',
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                var data = Ext.JSON.decode(resp.data);
                if (data.sampleCountThreshold){
                    me.sampleCountThreshold= data.sampleCountThreshold;
                } else {
                    me.sampleCountThreshold= 50;

                }

                var retrainingThresholdCount = 0;
                var statusMessage='';
                var y = TRAINING_EXAMPLE % data.sampleCountThreshold;
                if(y < 0){
                  y = y * data.sampleCountThreshold;
                }
                retrainingThresholdCount = data.sampleCountThreshold - y;

                if( countTrainingExample > 0){
                    statusMessage = retrainingThresholdCount + ' more needed to re-train. Note: Value \"N/A\" doesn\'t count for training.';
                    me.mainComponent.taggerDescription2line.setText('<b>' + TRAINING_EXAMPLE + '</b> human-tagged items. '+ statusMessage, false);
                }
                else{

                    statusMessage = retrainingThresholdCount + ' more needed to re-train. Note: Value \"N/A\" doesn\'t count for training.';
                    me.mainComponent.taggerDescription2line.setText('<b>'+TRAINING_EXAMPLE+'</b> human-tagged items. '+statusMessage, false);
                }
            }
        });
    }
    ,
    deleteTrainingExample: function(button){
        if (!button.exampleId){
            AIDRFMFunctions.setAlert("Error", "Error while delete human-tagged item. Document Id not available.");
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
                    AIDRFMFunctions.setAlert("Info", "Human-tagged item is removed successfully. Note: this removal will be reflected on the automatic classifier the next time it is retrained.");
                    me.mainComponent.trainingDataStore.load();
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            }
        });
    }

});