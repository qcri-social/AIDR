Ext.define('TAGGUI.model-details.controller.ModelDetailsController', {
    extend: 'Ext.app.Controller',

    views: [
        'ModelDetailsPanel', 'ClassifierDetailsChart', 'ClassifierHistoryChart'
    ],

    init: function () {

        this.control({

            'model-details-view': {
                beforerender: this.beforeRenderView
            }

        });

    },

    beforeRenderView: function (component) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
        modelDetailsController = this;

        this.getAllLabelsForModel();
        this.getAttributeInfo();
    },

    getAllLabelsForModel: function() {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/getAllLabelsForModel.action',
            method: 'GET',
            params: {
                id: MODEL_ID,
                code: CRISIS_CODE
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success && resp.data) {
                    var count = resp.data.length;
                    if (count > 0) {
                        models =[],
                        totalMessages = 0,
                        totalExamples = 0,
                        totalAUC = 0,
                        totalPrecision = 0,
                        totalRecall = 0,
                        status = '';
                        Ext.Array.each(resp.data, function(r, index) {
                        	var model ={};
                        	model.value = "";
                        	model.classifiedDocumentCount = 0;
                        	model.trainingDocumentsCount = 0;
                        	model.labelAuc = 0.00;
                        	model.labelPrecision = 0.00;
                        	model.labelRecall = 0.00;
                            if (!r.nominalLabel || !r.nominalLabel.nominalLabelCode || r.nominalLabel.nominalLabelCode == 'null'){
                                return true;
                            }
                            else{
                            	model.value = r.nominalLabel.name;
                            }
                            if (r.classifiedDocumentCount && r.classifiedDocumentCount > 0) {
                            	model.classifiedDocumentCount = r.classifiedDocumentCount;
                                totalMessages += r.classifiedDocumentCount;
                            }
                            if (r.trainingDocuments && r.trainingDocuments > 0) {
                            	model.trainingDocumentsCount =  r.trainingDocuments;
                                totalExamples += r.trainingDocuments;
                            }
                            if (r.labelAuc && r.labelAuc > 0) {
                            	model.labelAuc = r.labelAuc.toFixed(2);
                                totalAUC += r.labelAuc;
                            }
                            if (r.labelPrecision && r.labelPrecision > 0) {
                            	model.labelPrecision = r.labelPrecision.toFixed(2);
                                totalPrecision += r.labelPrecision;
                            }
                            if (r.labelRecall && r.labelRecall > 0) {
                            	model.labelRecall = r.labelRecall.toFixed(2);
                                totalRecall += r.labelRecall;
                            }
                            if (r.modelStatus) {
                                status = r.modelStatus;
                            }
                            
                            model.totalDocuments = r.classifiedDocumentCount + r.trainingDocuments;
                            models.push(model);
                        });
                        
                        var totalModel = {};
                        totalModel.value = 'Summary';
                        totalModel.classifiedDocumentCount = totalMessages;
                        totalModel.trainingDocumentsCount = totalExamples;
                        totalModel.totalDocuments = totalMessages + totalExamples;
                        totalModel.labelAuc = MODEL_AUC;
                        totalModel.labelPrecision = (totalPrecision / count).toFixed(2);
                        totalModel.labelRecall = (totalRecall / count).toFixed(2);
                        models.push(totalModel);
                        
                        me.mainComponent.modelDetails.setText('Status: <b>' + status + '</b>.&nbsp;' +
                            'Machine has tagged <b>' + totalMessages.format() + '</b> '+ COLLECTION_TYPES[TYPE]["plural"] + ' (since last change of the classifier)' +
                            '.&nbsp;' + 'Trained on <b>' + totalExamples.format() + '</b> human-tagged '+ COLLECTION_TYPES[TYPE]["plural"] + '.&nbsp;' +
                            '<br><a href="' + BASE_URL +  '/protected/'
                            + CRISIS_CODE + '/' + MODEL_ID + '/' + MODEL_FAMILY_ID + '/' + ATTRIBUTE_ID
                            + '/training-data">Go to human-tagged '+ COLLECTION_TYPES[TYPE]["plural"] + ' &raquo;</a>', false);
                        Ext.getStore('modelLabelStoreForClassifierDetailsChart').loadData(models.reverse());
                        
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            }
        });
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
                        me.mainComponent.attributeDetails.loadData(resp.data);
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            }
        });
    }

});