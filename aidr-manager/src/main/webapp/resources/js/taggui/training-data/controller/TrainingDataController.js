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
	        		},
	        		"#downloadButton": {
                    	click: function (btn, e, eOpts) {
                        this.downloadButtonHandler(btn);
                        }
                    }
	        	});

	        },

	        beforeRenderView: function (component, eOpts) {
	        	  
	        	AIDRFMFunctions.initMessageContainer();
	        	this.mainComponent = component;
	        	this.mainComponent.constraintsString = '{"constraints":[]}';
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

	        	if (MODEL_ID) {
	        		status = AIDRFMFunctions.getStatusWithStyle("RUNNING", TYPE);
	        		detailsForModel = '<a href="' + BASE_URL +  '/protected/' + CRISIS_CODE + '/' + MODEL_ID + '/model-details">Details of running classifier &raquo;</a>';

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
	        						var totalMessages = 0,
	        						totalExamples = 0;

	        						Ext.Array.each(resp.data, function(r, index) {
//	        							do not count any data from labels with code == null
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
	        						//me.getRetrainingThreshold()\
	        						/*
	        						Author:Sushant

	        						*/
	        						// Passed All the values to the getRetrainingThreshold function, so that after it is called, all the variables remain present to be rendered
	        						var a=me.getRetrainingThreshold(totalMessages, count,status);
	        						

	        						

   						



	        						// me.mainComponent.taggerDescription2line.setText('<b>' + totalExamples + '</b> training examples. Note: Value \"N/A\" doesn\'t count as training example.', false);
	        					}
	        				} else {
	        					AIDRFMFunctions.setAlert("Error", resp.message);
	        					AIDRFMFunctions.reportIssue(response);
	        				}
	        			}
	        		});
	        	} else {
	        		// this.getRetrainingThreshold(0,0);
	        		me.mainComponent.breadcrumbs.setText('<div class="bread-crumbs">' +
	        				'<a href="' + BASE_URL + '/protected/home">My Collections</a><span>&nbsp;>&nbsp;</span>' +
	        				'<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">' + CRISIS_NAME + '</a><span>&nbsp;>&nbsp;' +
	        				MODEL_NAME + '&nbsp;>&nbsp;Human-tagged '+ COLLECTION_TYPES[TYPE]["plural"] + '</span></div>', false);

	        	}


	        },

	        getRetrainingThreshold: function(trainingExamplesCount, countTrainingExample,status){
	        	var me = this;
	     

	        	Ext.Ajax.request({
	        		url: BASE_URL + '/protected/tagger/getTrainingDataCountByModelIdAndCrisisId.action',
	        		method: 'GET',
	        		params: {
	        			modelFamilyId: MODEL_FAMILY_ID,
	        			crisisId: CRISIS_ID
	        		},
	        		headers: {
	        			'Accept': 'application/json'
	        		},
	        	
	        		success: function (response) {
	        			var resp = Ext.decode(response.responseText);
	        			if (resp.success && resp.data) {
	        				var totalHumanLabeledCount = resp.data;
	        				var sampleCountThreshold = RETRAINING_THRESHOLD;
	        	        	var retrainingThresholdCount = 0;
	        	        	var statusMessage='';
	        	        	var y = totalHumanLabeledCount % sampleCountThreshold; //TRAINING_EXAMPLE % sampleCountThreshold;
	        	        	
	        	        	if(y < 0){
	        	        		y = y * sampleCountThreshold;
	        	        	}
	        	        	retrainingThresholdCount = sampleCountThreshold - y;

	        	        	/*if( countTrainingExample > 0){
	        	        		statusMessage = retrainingThresholdCount + ' more needed to re-train. Note: Value \"N/A\" doesn\'t count for training.';
	        	        		//me.mainComponent.taggerDescription2line.setText('<b>' + TRAINING_EXAMPLE + '</b> human-tagged '+ COLLECTION_TYPES[TYPE]["plural"] + '. '+ statusMessage, false);
	        	        		me.mainComponent.taggerDescription2line.setText('<b>' + totalHumanLabeledCount + '</b> human-tagged '+ COLLECTION_TYPES[TYPE]["plural"] + '. ' + statusMessage, false);
	        	        	}
	        	        	else{*/

/* 
Author:Sushant
Removed the taggerDescription2line and disabled it as there is no use of two labels.
*/
	        	        		statusMessage = retrainingThresholdCount + ' more needed to re-train';
	        	        		me.mainComponent.taggerDescription.setText(' Status: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +status+', '+ statusMessage+'<br> ' +
	        								'Human-tagged: ' + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ' + totalHumanLabeledCount.format() +'<br>'+'Machine-tagged: '+'&nbsp;&nbsp;&nbsp;&nbsp;'+trainingExamplesCount.format() /*+ detailsForModel*/, false);
                                 
	        	        	
	        			} else {
	        				AIDRFMFunctions.setAlert("Info", "No human tagged "+ COLLECTION_TYPES[TYPE]["plural"] + " present for this classifier.");
	        			}
	        		}
	        	
	        	});

                            
	        },
	        

	        deleteTrainingExample: function(button){
	        	if (!button.exampleId){
	        		AIDRFMFunctions.setAlert("Error", "Error while delete human-tagged "+ COLLECTION_TYPES[TYPE]["singular"] + ". Document Id not available.");
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
	        				AIDRFMFunctions.setAlert("Info", "Human-tagged "+ COLLECTION_TYPES[TYPE]["singular"] + " is removed successfully. Note: this removal will be reflected on the automatic classifier the next time it is retrained.");
	        				me.mainComponent.trainingDataStore.load();
	        			} else {
	        				AIDRFMFunctions.setAlert("Error", resp.message);
	        				AIDRFMFunctions.reportIssue(response);
	        			}
	        		}
	        	});
	        },
	        downloadButtonHandler: function(btn){
                    var me = this;
                    var format = me.mainComponent.downloadFormat.getValue().format;
                   // var contents = 'full';

                    var url = '';
                    var params = {
                        crisisCode: CRISIS_CODE,
                        //count: 1000,
                        //queryString: me.mainComponent.constraintsString
                        queryString: me.mainComponent.constraintsString
                    };

                    params.fileType = format;
                    url = '/protected/tagger/downloadHumanLabeledDocuments.action';

                    btn.setDisabled(true);
                    me.mainComponent.downloadLink.show();
                    Ext.suspendLayouts();
                    me.mainComponent.downloadLink.setText('<div class="loading-block"></div>', false);

                    Ext.Ajax.timeout = 900000;
                    Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
                    Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
                    Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
                    Ext.Ajax.request({
                        url: BASE_URL + url,
                        timeout: 900000,
                        method: 'POST',
                        params: params,
                        headers: {
                            'Accept': 'application/json'
                        },
                        success: function (response) {
                            btn.setDisabled(false);
                            var resp = Ext.decode(response.responseText);
                            if (resp.success) {
                                if (resp.data && resp.data != '') {
                                	if(resp.data.total){
                                		me.mainComponent.downloadLink.setText('<div class="styled-text download-link"><a target="_blank" href="' + resp.data.fileName + '">Download File</a></div>', false);
                                	}
                                	else{
                                		me.mainComponent.downloadLink.setText('', false);
                                		AIDRFMFunctions.setAlert("Info", "No human tagged "+ COLLECTION_TYPES[TYPE]["plural"] + " available to download");
                                	}
                                    
                                } else {
                                    me.mainComponent.downloadLink.setText('', false);
                                    AIDRFMFunctions.setAlert("Error", "Generate Tweet Ids service returned empty url. For further inquiries please contact admin.");
                                    AIDRFMFunctions.reportIssue(response);
                                }
                            } else {
                                me.mainComponent.downloadLink.setText('', false);
                                AIDRFMFunctions.setAlert("Error", resp.message);
                                AIDRFMFunctions.reportIssue(response);
                            }
                        },
                        failure: function () {
                            btn.setDisabled(false);
                        }
                    });
                }

});