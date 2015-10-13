Ext.define('TAGGUI.model-details.controller.ModelDetailsController', {
    extend: 'Ext.app.Controller',

    views: [
        'ModelDetailsPanel', /*'ClassifierDetailsChart',*/ 'ClassifierHistoryChart'
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
                        var models = [],
                        modelsWithoutPerc =[],
                            totalMessages = 0,
                            totalExamples = 0,
                            totalAUC = 0,
                            totalPrecision = 0,
                            totalRecall = 0,
                            status = '';
                        Ext.Array.each(resp.data, function(r, index) {
//                            do not count any data from labels with code == null
                            if (!r.nominalLabel || !r.nominalLabel.nominalLabelCode || r.nominalLabel.nominalLabelCode == 'null'){
                                return true;
                            }

                            if (r.classifiedDocumentCount && r.classifiedDocumentCount > 0) {
                                totalMessages += r.classifiedDocumentCount;
                            }
                            if (r.trainingDocuments && r.trainingDocuments > 0) {
                                totalExamples += r.trainingDocuments;
                            }
                            if (r.labelAuc && r.labelAuc > 0) {
                                totalAUC += r.labelAuc;
                            }
                            if (r.labelPrecision && r.labelPrecision > 0) {
                                totalPrecision += r.labelPrecision;
                            }
                            if (r.labelRecall && r.labelRecall > 0) {
                                totalRecall += r.labelRecall;
                            }
                            if (r.modelStatus) {
                                status = r.modelStatus;
                            }
                        });
                        
                        Ext.Array.each(resp.data, function(r, index) {
//                            do not show labels with code == null
                            if (!r.nominalLabel || !r.nominalLabel.nominalLabelCode || r.nominalLabel.nominalLabelCode == 'null'){
                                return true;
                            }
                            var model = {};
                            var modelWithoutPerc ={};
                            if (r.nominalLabel && r.nominalLabel.name) {
                                model.value = r.nominalLabel.name;
                                modelWithoutPerc.value = r.nominalLabel.name;
                            }
                            var classifiedMessages = '0 (0%)';
                            if (r.classifiedDocumentCount){
                                var classifiedMessagesPercentage = ((r.classifiedDocumentCount * 100) / totalMessages).toFixed(2);
                                classifiedMessages = r.classifiedDocumentCount.format() + ' (' + classifiedMessagesPercentage + '%)';
                            }
                            var trainingDocuments = '0 (0%)';
                            if (r.trainingDocuments){
                                var trainingDocumentsPercentage = ((r.trainingDocuments * 100) / totalExamples).toFixed(2);
                                trainingDocuments = r.trainingDocuments.format() + ' (' + trainingDocumentsPercentage + '%)';
                            }
                            model.classifiedDocumentCount = classifiedMessages;
                            model.trainingDocumentsCount = trainingDocuments;
                            model.labelAuc = r.labelAuc.toFixed(2);
                            model.labelPrecision = r.labelPrecision.toFixed(2);
                            model.labelRecall = r.labelRecall.toFixed(2);
                            models.push(model);
                            
                            modelWithoutPerc.classifiedDocumentCount = r.classifiedDocumentCount ? r.classifiedDocumentCount : 0;
                            modelWithoutPerc.trainingDocumentsCount =  r.trainingDocuments ? r.trainingDocuments : 0;
                            modelWithoutPerc.labelAuc = r.labelAuc.toFixed(2);
                            modelWithoutPerc.labelPrecision = r.labelPrecision.toFixed(2);
                            modelWithoutPerc.labelRecall = r.labelRecall.toFixed(2);
                            modelWithoutPerc.totalDocuments = r.classifiedDocumentCount + r.trainingDocuments;
                            modelsWithoutPerc.push(modelWithoutPerc);
                        });
                        
                        var totalModel = {};
                        totalModel.value = '';
                        totalModel.classifiedDocumentCount = totalMessages.format() + ' total';
                        totalModel.trainingDocumentsCount = totalExamples.format() + ' total';
                        totalModel.labelAuc = MODEL_AUC;
                        totalModel.labelPrecision = (totalPrecision / count).toFixed(2) + ' avg';
                        totalModel.labelRecall = (totalRecall / count).toFixed(2) + ' avg';
                        
                        models.push(totalModel);
                        
                        var totalModelWithoutPerc = {};
                        totalModelWithoutPerc.value = 'Summary';
                        totalModelWithoutPerc.classifiedDocumentCount = totalMessages;
                        totalModelWithoutPerc.trainingDocumentsCount = totalExamples;
                        totalModelWithoutPerc.totalDocuments = totalMessages + totalExamples;
                        totalModelWithoutPerc.labelAuc = MODEL_AUC;
                        totalModelWithoutPerc.labelPrecision = (totalPrecision / count).toFixed(2);
                        totalModelWithoutPerc.labelRecall = (totalRecall / count).toFixed(2);
                        modelsWithoutPerc.push(totalModelWithoutPerc);
                        
                        me.mainComponent.modelDetails.setText('Status: <b>' + status + '</b>.&nbsp;' +
                            'Machine has tagged <b>' + totalMessages.format() + '</b> '+ COLLECTION_TYPES[TYPE]["plural"] + ' (since last change of the classifier)' +
                            '.&nbsp;' + 'Trained on <b>' + totalExamples.format() + '</b> human-tagged '+ COLLECTION_TYPES[TYPE]["plural"] + '.&nbsp;' +
                            '<br><a href="' + BASE_URL +  '/protected/'
                            + CRISIS_CODE + '/' + MODEL_ID + '/' + MODEL_FAMILY_ID + '/' + ATTRIBUTE_ID
                            + '/training-data">Go to human-tagged '+ COLLECTION_TYPES[TYPE]["plural"] + ' &raquo;</a>', false);
                        me.mainComponent.modelLabelsStore.loadData(models);
                      //  Ext.getStore('modelLabelStoreForClassifierDetailsChart').loadData(modelsWithoutPerc);
                        
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