Ext.define('TAGGUI.predict-new-attribute.controller.PredictNewAttributeController', {
    extend: 'Ext.app.Controller',

    views: [
        'PredictNewAttributePanel'
    ],

    init: function () {

        this.control({

            'predict-new-attribute-view': {
                beforerender: this.beforeRenderView
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
        predictNewAttributeController = this;

        var me = this;

        this.getAttributesForCrises();
    },

    getAttributesForCrises: function() {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/getAttributesForCrises.action',
            method: 'GET',
            params: {
                id: CRISIS_ID
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success && resp.data) {
                    var count = resp.data.length;
                    if (count > 0) {
                        var standard = [],
                            custom = [];
                        Ext.Array.each(resp.data, function(r, index) {
                            if (r.users && r.users.userID) {
                                if (r.users.userID == 1) {
                                    delete r.users;
                                    standard.push(r);
                                } else {
                                    delete r.users;
                                    custom.push(r);
                                }
                            }
                        });

                        if (standard.length > 0 && custom.length > 0){
                            me.mainComponent.emptySpace.show();
                        } else {
                            me.mainComponent.emptySpace.hide();
                        }

                        me.mainComponent.standardAttributesStore.loadData(standard);
                        me.mainComponent.customAttributesStore.loadData(custom);
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            }
        });
    },

    addAttributeToCrises: function(id, name, type) {
        var me = this;

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
                    if (type === 'standard') {
                        document.location.href = BASE_URL + '/protected/' + CRISIS_CODE +'/tagger-collection-details';
                    } else {
                        me.getAttributesForCrises();
                        AIDRFMFunctions.setAlert("Ok", '"' + name + '" has been added.');
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            }
        });

    },

    viewImportCrisisDataModal : function(nominalAttributeID) {
        var store = Ext.create('Ext.data.Store', {
            storeId: 'trainingDataStore1',
            fields: ['code', 'name', 'language', 'trainingCount', 'owner'],
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/tagger/attribute/'+ nominalAttributeID +'/collections?collectionId=' + CRISIS_ID,
                reader: {
                    rootProperty: 'data'
                }
            },
            autoLoad: true
        });

        importTrainingSetGrid = new Ext.grid.GridPanel({
            store: store,
            loadMask: true,
            //title: 'Collection List',
            width: 800,
            height: 320,
            border: true,
            renderTo:Ext.getBody(),
            columns: [
                { header: 'Collection Code',  dataIndex: 'code', width: 200 },
                { header: 'Collection Name', dataIndex: 'name',  width: 200 },
                { header: 'Language',  dataIndex: 'language',  width: 100 },
                { header: 'Training Count', dataIndex: 'trainingCount',  width: 100  },
                { header: 'Owner', dataIndex: 'owner', width: 200
            }],
            viewConfig: {
              deferEmptyText: false,
              emptyText: '<div style="text-align: center; margin-top: 50px">No Collections are using this Classifier</div>'
            }
        });

        var window = Ext.create('Ext.window.Window', {
            title: 'Import Training set from other collections',
            height: 400,
            width: 800,
            modal : true,
            resizable : false,
            items: [ importTrainingSetGrid ],
            bbar: [{
                text: 'Import/Add Classifier',
                handler: function(btn) {
                  if(importTrainingSetGrid.getSelectionModel().hasSelection()) {
                    var selectedCollectionCode = importTrainingSetGrid.getSelectionModel().getSelection()[0].data.code;
                    Ext.Ajax.request({
                        url: BASE_URL + '/protected/tagger/import/training-set',
                        params: {
                            sourceCollectionCode: selectedCollectionCode,
                            attributeId: nominalAttributeID,
                            targetCollectionId: CRISIS_ID
                        },
                        headers: {
                            'Accept': 'application/json'
                        },
                        success: function (response) {
                            var resp = Ext.decode(response.responseText);
                            if (resp.success) {
                                document.location.href = BASE_URL + '/protected/' + CRISIS_CODE +'/tagger-collection-details';
                            } else {
                                AIDRFMFunctions.setAlert("Error", resp.message);
                                AIDRFMFunctions.reportIssue(response);
                            }
                        }
                    });

                  } else {
                    predictNewAttributeController.addAttributeToCrises(nominalAttributeID, COLLECTION_NAME, 'standard');
                  }
                }
            }, {
                text: 'Cancel',
                handler: function(btn) {
                  var win = Ext.WindowManager.getActive();
                  if (win) {
                    win.close();
                  }
                }
            }]
        });
        window.show();

		// then iframe
		//Ext.DomHelper.insertFirst(window.body, div)
	}

});