Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
]);

Ext.define('TAGGUI.tagger-collection-details.view.TaggerCollectionDetailsPanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.tagger-collection-details-view',

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/home">My Collections</a><span>&nbsp;>&nbsp;Classifier Details</span></div>',
            margin: 0,
            padding: 0
        });

        this.taggerTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1',
            text: 'Classifier for "' + CRISIS_NAME + '"',
            margin: '10 0 15 0',
            padding: 0,
            flex: 1
        });

        this.classifiersTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: '',
            margin: '17 0 5 0',
            flex: 1
        });

        this.gotoCollectorButton = Ext.create('Ext.Button', {
            text: 'Go To Collection',
            cls:'btn btn-blue',
            id: 'goToCollector',
            width: 150,
            margin: '0 0 0 0'
        });

        this.addNewClassifierButton = Ext.create('Ext.Button', {
            text: 'Add a new classifier',
            cls:'btn btn-blue',
            id: 'addNewClassifier',
            width: 150,
            margin: '27 0 0 0'
        });

        this.publicLink = Ext.create('Ext.container.Container', {
            html: 'Public link for volunteers (in MicroMappers):',
            margin: '6 15 0 0'
        });

        this.twitterLink =  Ext.create('Ext.form.Label', {
            html: '<image src="/AIDRFetchManager/resources/img/icons/twitter-icon.png" />',
            padding: '0 5 0 0'
        });

        this.facebookLink =  Ext.create('Ext.form.Label', {
            html: '<image src="/AIDRFetchManager/resources/img/icons/facebook-icon.png" />' ,
            padding: '0 5 0 0'
        });

        this.googlePlusLink =  Ext.create('Ext.form.Label', {
            html: '<image src="/AIDRFetchManager/resources/img/icons/google-icon.png" />',
            padding: '0 5 0 0'
        });

        this.pinterestLink =  Ext.create('Ext.form.Label', {
            html: '<image src="/AIDRFetchManager/resources/img/icons/pinterest-icon.png" />',
            padding: '0 5 0 0'
        });

        this.socialIcons = Ext.create('Ext.container.Container', {
            flex: 1,
            layout: 'hbox',
            defaults: {
                margin: '0 5 0 0'
            },
            items: [
                this.twitterLink,
                this.facebookLink,
                this.googlePlusLink,
                this.pinterestLink
            ]
        });

        this.pyBossaLink = Ext.create('Ext.form.Label', {
            html: '<div class="gray-backgrpund"><i>Initializing crowdsourcing task. Please come back in a few minutes.</i></div>',
            margin: '5 0 5 0'
        });

        this.aucHint = Ext.create('Ext.container.Container', {
            html: '<span class="redInfo">*</span>If AUC is lower than 80%, or AUC is 100%, you urgently need more human-tagged '+ COLLECTION_TYPES[TYPE]["plural"] + '.',
            margin: 0
        });

        this.crisisTypesStore = Ext.create('Ext.data.Store', {
            pageSize: 30,
            storeId: 'crisisTypesStore',
            fields: ['crisisTypeID', 'name'],
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/tagger/getAllCrisisTypes.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            listeners: {
                load: function (s) {
                    me.crysisTypesCombo.setValue(CRISIS_TYPE_ID);
                }
            }
        });


        this.crysisTypesCombo = Ext.create('Ext.form.ComboBox', {
            hideLabel: true,
            store: this.crisisTypesStore,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'crisisTypeID',
            width: 280,
            listeners: {
                change: function(combo, newValue, oldValue, eOpts) {
                    if (newValue == CRISIS_TYPE_ID) {
                        me.saveButton.hide();
                    } else {
                        me.saveButton.show();
                    }
                }
            }
        });

        this.crisisModelsStore = Ext.create('Ext.data.Store', {
            pageSize: 30,
            storeId: 'crisisModelsStore',
            fields: ['attribute', 'attributeID', 'auc', 'classifiedDocuments', 'modelID', 'status', 'trainingExamples', 'modelFamilyID','retrainingThreshold'],
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/tagger/getModelsForCrisis.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            listeners: {
                beforeload: function (s) {
                    s.getProxy().extraParams = {
                        id: CRISIS_ID
                    }
                }
            }
        });

        this.crisisModelsTpl = new Ext.XTemplate(
            '<table width="100%" class="crisis-table">',
            '<tpl for=".">',

            '<tr class="crisis-item">',

            '<td class="img">',
            '<img alt="Collection History image" src="/AIDRFetchManager/resources/img/AIDR/AIDR_EMBLEM_CMYK_COLOUR_HR.jpg" width="70" height="70">',
            '</td>',

            '<td class="content"><table><tr>',


            '<td class="styled-text-17">Classifier:</td>',
            '<td class="styled-text-17">{[this.getModelName(values.modelID, values.attribute)]}</td></tr>',


            '<tr><td>Status:</td>',
            '<td>{[this.getStatus(values.modelID, values.trainingExamples, values.retrainingThreshold, values.auc, values.attribute)]}</td></tr>',

            '<tr><td>Human-tagged '+ COLLECTION_TYPES[TYPE]["plural"] + ':</td>',
            '<td>{[this.getNumber(values.trainingExamples)]} &mdash; <a href="' + BASE_URL +  '/protected/'
                + CRISIS_CODE + '/{modelID}/{modelFamilyID}/{attributeID}/training-data">Go to human-tagged s &raquo;</a></td></tr>',


            '<tr><td>Machine-tagged '+ COLLECTION_TYPES[TYPE]["plural"] + ':</td>',
            '<td>{[this.getNumber(values.classifiedDocuments)]} (since last change of the classifier)</td></tr>',

            '<tr><td>Quality (AUC)<span class="redInfo">*</span>:</td>',
            '<td>{[AIDRFMFunctions.getAucNumberAsPercentageWithColors(values.auc)]}</td>',

            '</td></tr></table></td>',

            '</tr>',


            '<tr><td colspan="2">',
            '<div class="horizontalLine"></div>',
            '</td></tr>',

            '</tpl>',
            '</table>',
            {
                getNumber: function (r) {
                    return r ? r.format() : 0;
                },
                getModelName: function (modelId, modelName) {
                    if (modelId && modelId != 0) {
                        return '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/' + modelId + '/model-details">' + modelName + '</a>';
                    } else {
                        return modelName;
                    }
                },
                getStatus: function (modelId, trainingExamples, retrainingThreshold, auc, modelName) {
                    var reqTrainExamNumber = trainingExamples % retrainingThreshold;
                    if(reqTrainExamNumber < 0){
                        reqTrainExamNumber = reqTrainExamNumber * retrainingThreshold;
                    }

                    reqTrainExamNumber = retrainingThreshold - reqTrainExamNumber;

                    if (modelId && modelId != 0) {
                        return 'Running. '+reqTrainExamNumber+' more needed to re-train.';
                    } else {
                        // the next line is for debugging by JLUCAS 04/09/2014
                        Ext.util.Cookies.set(modelName, reqTrainExamNumber);
                        return 'Waiting. '+reqTrainExamNumber+' more needed to re-train.';

                    }
                }
            }
        );


        this.crisisModelsView = Ext.create('Ext.view.View', {
            store: this.crisisModelsStore,
            tpl: this.crisisModelsTpl,
            itemSelector: 'div.active',
            loadMask: false
        });

        this.saveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'crisisSave',
            hidden: true
        });

        this.deleteButton = Ext.create('Ext.Button', {
            text: 'Delete',
            cls:'btn btn-red',
            id: 'crisisDelete',
            margin: '0 0 0 7'
        });

        this.detailsBlock = Ext.create('Ext.container.Container', {
            flex: 1,
            layout: 'vbox',
            items: [
                {
                    xtype: 'container',
                    defaultType: 'label',
                    padding: '0 10',
                    flex: 1,
                    layout: 'vbox',
                    defaults: {
                        margin: '5 0'
                    },
                    height: 120,
                    items: [
                        {
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 75,
                                    text: 'Short name:'
                                },
                                {
                                    text: CRISIS_CODE
                                }
                            ]
                        },{
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 75,
                                    text: 'Name:'
                                },
                                {
                                    text: CRISIS_NAME
                                }
                            ]
                        },{
                            xtype: 'container',
                            defaultType: 'label',
                            layout: 'hbox',
                            items: [
                                {
                                    width: 75,
                                    text: 'Type:'
                                },
                                this.crysisTypesCombo
                            ]
                        },{
                            xtype: 'container',
                            layout: 'hbox',
                            items: [
                                {
                                    xtype: 'container',
                                    layout: 'hbox',
                                    margin: '0 0 0 75',
                                    width: 290,
                                    items: [
                                        this.saveButton
                                    ]
                                },
                                this.deleteButton
                            ]
                        }
                    ]
                },
                {
                    xtype: 'container',
                    width: '100%',
                    html: '<div class="horizontalLine"></div>'
                }
            ]
        });

        this.CSVLink = Ext.create('Ext.form.Label', {
            flex: 1,
            margin: '5 5 5 5',
            html: ''
        });
        this.tweetsIdsLink = Ext.create('Ext.form.Label', {
            flex: 1,
            margin: '5 5 5 5',
            html: ''
        });

        this.generateCSVButton = Ext.create('Ext.Button', {
            text: 'Export tweets (.csv) (Last 100k tweets)',
            margin: '5 5 5 0',
            cls:'btn btn-blue download-button',
            id: 'generateCSVLink',
            width: 300
        });

        this.generateTweetIdsButton = Ext.create('Ext.Button', {
            text: 'Export tweet-ids only (.csv) (All tweets)',
            margin: '5 5 5 0',
            cls:'btn btn-blue download-button',
            id: 'generateTweetIdsLink' ,
            width: 300
        });
        
        this.JSONLink = Ext.create('Ext.form.Label', {
            flex: 1,
            margin: '5 5 5 5',
            html: ''
        });
        this.JsonTweetsIdsLink = Ext.create('Ext.form.Label', {
            flex: 1,
            margin: '5 5 5 5',
            html: ''
        });

        this.generateJSONButton = Ext.create('Ext.Button', {
            text: 'Export tweets (.json) (Last 100k tweets)',
            margin: '5 5 5 0',
            cls:'btn btn-blue download-button',
            id: 'generateJSONLink',
            width: 300
        });

        this.generateJsonTweetIdsButton = Ext.create('Ext.Button', {
            text: 'Export tweet-ids only (.json) (All tweets)',
            margin: '5 5 5 0',
            cls:'btn btn-blue download-button',
            id: 'generateJsonTweetIdsLink',
            width: 300
        });

        this.toInteractiveViewDownloadLink = Ext.create('Ext.form.Label', {
            flex: 1,
            padding: '15 0 0 0',
            html: '<div class="styled-text download-link"><a href="' + BASE_URL + '/protected/' + CRISIS_CODE
                + '/interactive-view-download">Go to interactive view/download</a></div>'
        });



        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        this.curatorDesc = Ext.create('Ext.form.Label', {
            html: '<div>* Curator name. Your name or the name of your organization, to display to volunteers and to people who use your collection.</div>',
            margin: '5 0 5 0',
            width: 350,
            height: 150
        });

        this.classifierCombo = Ext.create('Ext.form.ComboBox', {
            store: this.crisisModelsStore,
            queryMode: 'local',
            displayField: 'attribute',
            valueField: 'attributeID',
            multiSelect: false,
            editable: false,
            emptyText: 'Select a classifier',
            labelWidth: 250,
            name: 'classifierFilters',
            id: 'classifierFilters',
            margin: '0 0 0 10',
            hidden: true,
            flex: 1
        });

        this.uiTemplate = Ext.create('Ext.form.HtmlEditor',{
            width: 550,
            height: 200,
            frame: true,
            layout: 'fit',
            id:'uiTemplatePage',
            enableColors: false,
            enableAlignments: false,
            enableFont:false,
            enableFontSize:false
        });
        this.templateSaveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'templateSave',
            margin: '0 0 0 10',
            hidden: true
        });
        this.uiSkinTypeSaveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'uiSkinTypeSave',
            margin: '0 0 0 10'
        });


        this.optionRG = Ext.create('Ext.form.RadioGroup', {
            columns: 2,
            vertical: true,
            width: 1000,
            height:440,
            items: [
                {   boxLabel: '<span class="styled-text"><b>Plain (Default)</b></span>&nbsp;<span class="img"><img alt="Collection image" src="/AIDRFetchManager/resources/img/defaultSkin.png" class="skinPane"></span>',
                    name: 'skintype',
                    inputValue: 0,
                    checked   : true
                },
                {   boxLabel: '<span class="styled-text"><b>Mobile phone and hand</b></span>&nbsp;<span class="img"><img alt="Collection image" src="/AIDRFetchManager/resources/img/iphoneSkin.png" class="skinPane"></span>',
                    name: 'skintype',
                    inputValue: 1}
            ]
        });

        this.optionPanel = Ext.create('Ext.container.Container', {
            flex: 1,
            margin: '0 0 0 0',
            layout: 'hbox',
            items: [
                this.optionRG
            ]
        });

        this.uiSkinTypeBlock = Ext.create('Ext.container.Container', {
            layout: {
                type: 'vbox',
                align: 'stretch',
                margin: '0 0 0 0'
            },
            items: [
                {
                    xtype: 'container',
                    padding: '0 0 0 0',
                    margin: '0 0 0 0',
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: 'container',
                            flex: 1
                        },
                        this.uiSkinTypeSaveButton
                    ]
                },
                this.optionPanel
            ]
        });

        this.classifierComboForPybossaApp = Ext.create('Ext.form.ComboBox', {
            store: this.crisisModelsStore,
            queryMode: 'local',
            displayField: 'attribute',
            valueField: 'attributeID',
            multiSelect: false,
            editable: false,
            emptyText: 'Select a classifier',
            labelWidth: 250,
            name: 'pybossaClassifierFilters',
            id: 'pybossaClassifierFilters',
            margin: '0 0 0 10',
            flex: 1
        });

        this.uiLandingTemplateOne = Ext.create('Ext.form.HtmlEditor',{
            width: 350,
            height: 150,
            frame: true,
            layout: 'fit',
            id:'uiLandingOneTemplate',
            enableColors: false,
            enableAlignments: false,
            enableFont:false,
            enableFontSize:false,
            margin: '10 0 10 0'
        });
        this.uiLandingTemplateTwo = Ext.create('Ext.form.HtmlEditor',{
            width: 350,
            height: 150,
            frame: true,
            layout: 'fit',
            id:'uiLandingTwoTemplate',
            enableColors: false,
            enableAlignments: false,
            enableFont:false,
            enableFontSize:false ,
            margin: '10 0 10 0'
        });
        this.curatorInfo = Ext.create('Ext.form.HtmlEditor',{
            width: 350,
            height: 150,
            frame: true,
            layout: 'fit',
            id:'curatorInfo',
            enableColors: false,
            enableAlignments: false,
            enableFont:false,
            enableFontSize:false,
            margin: '10 0 10 0'
        });


        this.welcomePageUI = Ext.create('Ext.form.HtmlEditor',{
            width: 350,
            height: 150,
            frame: true,
            layout: 'fit',
            id:'welcomePage',
            enableColors: false,
            enableAlignments: false,
            enableFont:false,
            enableFontSize:false,
            margin: '10 0 10 0'
        });
        this.tutorial1UI = Ext.create('Ext.form.HtmlEditor',{
            width: 350,
            height: 150,
            frame: true,
            layout: 'fit',
            id:'tutorialFirst',
            enableColors: false,
            enableAlignments: false,
            enableFont:false,
            enableFontSize:false,
            margin: '10 0 10 0'
        });
        this.tutorial2UI = Ext.create('Ext.form.HtmlEditor',{
            width: 350,
            height: 150,
            frame: true,
            layout: 'fit',
            id:'tutorialSecond',
            enableColors: false,
            enableAlignments: false,
            enableFont:false,
            enableFontSize:false,
            margin: '10 0 10 0'
        });

        this.numFirst =  Ext.create('Ext.form.Label', {
            padding: '0 0 0 0',
            html: '<span class="img"><img alt="Collection image" src="/AIDRFetchManager/resources/img/welcomeOne.png" width="150" height="30"></span>',
            flex: 1
        });
        this.numSecond =  Ext.create('Ext.form.Label', {
            html: '<span class="img"><img alt="Collection image" src="/AIDRFetchManager/resources/img/tutorialOne.png" width="150" height="30"></span>',
            flex: 1
        });
        this.numThird =  Ext.create('Ext.form.Label', {
            html: '<span class="img"><img alt="Collection image" src="/AIDRFetchManager/resources/img/tutorialTwo.png" width="150" height="30""></span>',
            flex: 1
        });

        this.uiWelcomeSaveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'uiWelcomeSave',
            margin: '3 0 0 10',
            hidden: true

        });
        this.uiTutorialOneSaveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'uiTutorialOneSave',
            margin: '3 0 0 10' ,
            hidden: true
        });
        this.uiTutorialTwoSaveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'uiTutorialTwoSave',
            margin: '3 0 0 10',
            hidden: true
        });

        this.landingTopSaveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'landingTopSave',
            margin: '3 0 0 10'
        });
        this.landingBtnSaveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'landingBtnSave',
            margin: '3 0 0 10'
        });
        this.curatorSaveButton = Ext.create('Ext.Button', {
            text: 'Save',
            cls:'btn btn-green',
            id: 'curatorSave',
            margin: '3 0 0 10'
        });

        this.welcomePageBlock = Ext.create('Ext.container.Container', {
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [
                this.numFirst,
                {
                    xtype: 'container',
                    items: [
                        this.uiWelcomeSaveButton
                    ]
                }

            ]
        });

        this.tutorialOneBlock = Ext.create('Ext.container.Container', {
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [
                this.numSecond,
                {
                    xtype: 'container',
                    items: [
                        this.uiTutorialOneSaveButton
                    ]
                }
            ]
        });

        this.tutorialTwoBlock = Ext.create('Ext.container.Container', {
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [
                this.numThird,
                {
                    xtype: 'container',
                    items: [
                        this.uiTutorialTwoSaveButton
                    ]
                }
            ]
        });

        this.pybossaUIEditorBlock = Ext.create('Ext.container.Container', {
            layout: {
                type: 'hbox',
                align: 'stretch',
                margin: '50 0 0 0',
                flex: 1
            },
            items: [
                {
                    xtype: 'container',
                    layout: 'vbox',
                    margin: '20 0 0 0',
                    flex: 1,
                    items: [
                        {
                            xtype: 'container',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            margin: '20 0 0 0',
                            items: [
                                this.welcomePageBlock
                                , this.welcomePageUI

                            ]
                        },

                        {
                            xtype: 'container',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            margin: '40 0 0 0',
                            items: [
                                this.tutorialOneBlock
                                , this.tutorial1UI

                            ]
                        },
                        {
                            xtype: 'container',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            margin: '40 0 0 0',
                            items: [
                                this.tutorialTwoBlock
                                , this.tutorial2UI

                            ]
                        }
                    ]
                }
                ,
                {
                    xtype: 'label',
                    html: '<span class="img"><img alt="Collection image" src="/AIDRFetchManager/resources/img/pybossaAppPage.png"></span>',
                    margins: '50 0 0 40'
                }
            ]
        });

        this.pybossaUIBlock = Ext.create('Ext.container.Container', {
            layout: {
                type: 'vbox',
                align: 'stretch',
                margin: '60 0 0 0',
                flex:1
            },
            items: [
                this.classifierComboForPybossaApp
                ,
                this.pybossaUIEditorBlock
            ]
        });

        this.landingTopPageBlock =  Ext.create('Ext.container.Container', {
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'label',
                    html: '<span class="img"><img alt="Collection image" src="/AIDRFetchManager/resources/img/landingOne.png" width="150" height="30"></span>',
                    flex: 1
                },{
                    xtype: 'container',
                    items: [
                        this.landingTopSaveButton
                    ]
                }
            ]
        });

        this.landingBtnPageBlock =  Ext.create('Ext.container.Container', {
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'label',
                    html: '<span class="img"><img alt="Collection image" src="/AIDRFetchManager/resources/img/landingTwo.png" width="150" height="30"></span>',
                    flex: 1
                },{
                    xtype: 'container',
                    items: [
                        this.landingBtnSaveButton
                    ]
                }
            ]
        });

        this.curatorPageBlock =  Ext.create('Ext.container.Container', {
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'label',
                    html: '<span class="img"><img alt="Collection image" src="/AIDRFetchManager/resources/img/curator.png" width="150" height="30"></span>',
                    flex: 1
                },{
                    xtype: 'container',
                    items: [
                        this.curatorSaveButton
                    ]
                }
            ]
        });

        this.uiLandingPageBlock = Ext.create('Ext.container.Container', {
            layout: {
                type: 'hbox',
                align: 'stretch',
                margin: '50 0 0 0',
                flex: 1
            },
            items: [
                {
                    xtype: 'container',
                    layout: 'vbox',
                    margin: '20 0 0 0',
                    flex: 1,
                    items: [
                        {
                            xtype: 'container',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            margin: '20 0 0 0',
                            items: [
                                this.landingTopPageBlock
                                , this.uiLandingTemplateOne

                            ]
                        },

                        {
                            xtype: 'container',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            margin: '40 0 0 0',
                            items: [
                                this.landingBtnPageBlock
                                , this.uiLandingTemplateTwo

                            ]
                        },
                        {
                            xtype: 'container',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            margin: '40 0 0 0',
                            items: [
                                this.curatorPageBlock
                                , this.curatorInfo
                                ,this.curatorDesc
                            ]
                        }
                    ]
                }
                ,
                {
                    xtype: 'label',
                    html: '<span class="img"><img alt="Collection image" src="/AIDRFetchManager/resources/img/landingPage.png"></span>',
                    margin: '50 0 0 10'
                }
            ]
        });

        this.UIBlock = Ext.create('Ext.container.Container',{
            cls: 'accordion',
            width: '100%',
            minHeight: 700,
            layout:'accordion',
            defaults: {
                // applied to each contained panel
                bodyStyle: 'padding:15px'
            },
            layoutConfig: {
                // layout-specific configs go here
                titleCollapse: false,
                animate: true,
                activeOnTop: true
            },
            items: [

                {
                    title:'Custom Public Page for Volunteers',
                    items:[this.uiLandingPageBlock]

                },
                {
                    title:'Custom Skin',
                    items:[this.uiSkinTypeBlock]

                }
                ,
                {
                    title:'Custom Tutorial and Welcome Message',
                    items:[this.pybossaUIBlock]
                }

            ]

        });


            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////




        this.tabPanel = Ext.create('Ext.tab.Panel', {
            cls: 'tabPanel',
            width: '100%',
            minHeight: 400,
            activeTab: 0,
            items: [
                {
                    title: 'Details',
                    items: [
                        {
                            xtype: 'container',
                            flex: 1,
                            layout: 'hbox',
                            margin: '25 0 5 0',
                            items: [
                                this.publicLink,
                                this.socialIcons,
                                this.gotoCollectorButton
                            ]
                        },
                        this.pyBossaLink,
                        {
                            xtype: 'container',
                            margin: '15 0 0 0',
                            html: '<div class="horizontalLine"></div>'
                        },

                        {
                            xtype: 'container',
                            layout: 'hbox',
                            items: [
                                this.classifiersTitle,
                                this.addNewClassifierButton
                            ]
                        },
                        this.crisisModelsView,
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            padding: '15 0 0 0',
                            items: [
                                {
                                    xtype: 'container',
                                    flex: 1
                                },
                                this.aucHint
                            ]
                        }
                    ]
                },
                {
                    title: 'Edit',
                    padding: '10 0 0 0',
                    defaults: {
                        anchor: '100%'
                    },
                    items: [
                        this.detailsBlock
                    ]
                },
                {
                    title: 'Customize MicroMappers',
                    padding: '10 0 0 0',
                    items: this.UIBlock
                }
            ]
        });

        this.items = [
            this.breadcrumbs,
            {
                xtype: 'container',
                margin: '5 0 5 0',
                html: '<div class="horizontalLine"></div>'
            },
            {
                xtype: 'container',

                layout: {
                    type: 'vbox',
                    align: 'stretch',
                    padding: '0 0 10 0'
                },
                items: [
                    this.taggerTitle
                ]
            },
            this.tabPanel
        ];

        this.callParent(arguments);
    }

});