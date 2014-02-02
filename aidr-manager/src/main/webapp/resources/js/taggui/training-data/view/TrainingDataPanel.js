Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout',
    'AIDRFM.common.Header',
    'AIDRFM.common.Footer'
]);

Ext.define('TAGGUI.training-data.view.TrainingDataPanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.training-data-view',

    nominalAttributeId: 0,

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.form.Label', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/tagger-home">Tagger</a><span>&nbsp;>&nbsp;</span>' +
                '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">' + CRISIS_NAME + '</a><span>&nbsp;>&nbsp;</span>' +
                '<a href="' + BASE_URL +  '/protected/' + CRISIS_CODE + '/' + MODEL_ID + '/model-details">' + MODEL_NAME + '</a><span>&nbsp;>&nbsp;Training data</span></div>',
            margin: 0,
            padding: 0
        });

        this.taggerTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Training data for "' + MODEL_NAME + '" in collection "' + CRISIS_NAME + '"',
            flex: 1
        });

        this.taggerDescription = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            margin: '0 0 15 0',
            html: 'Status: <b>Waiting training examples</b>. Has classified <b>0</b> messages.&nbsp;',
            flex: 1
        });

        this.taggerDescription2line = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            margin: '0 0 15 0',
            html: '<b>0</b> training examples. Click on a message to see/edit details',
            flex: 1
        });

        this.addTrainingData = Ext.create('Ext.Button', {
            text: 'Add training data',
            cls:'btn btn-blue',
            id: 'addNewTrainingData',
            width: 150,
            margin: '0 0 0 0'
        });

        this.trainingDataStore = Ext.create('Ext.data.Store', {
            pageSize: 20,
            storeId: 'trainingDataStore',
            fields: ['documentID', 'labelID', 'labelName', 'labeledTime', 'labelerID', 'labelerName', 'tweetJSON'],
            remoteSort: true,
            proxy: {
                type: 'ajax',
                url: BASE_URL + '/protected/tagger/getTrainingDataByModelIdAndCrisisId.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                },
                simpleSortMode: true,
                sortParam: 'sortColumn',
                directionParam: 'sortDirection'
            },
            autoLoad: true,
            listeners: {
                beforeload: function (s) {
                    s.getProxy().extraParams = {
                        modelFamilyId: MODEL_FAMILY_ID,
                        crisisId: CRISIS_ID
                    }
                }
            }
        });

        this.trainingDataGrid = Ext.create('Ext.grid.Panel', {
            flex:1,
            store: this.trainingDataStore,
            cls: 'aidr-grid',
            columns: [
                {
                    xtype: 'gridcolumn', dataIndex: 'labelName', text: 'Value', width: 150,
                    renderer: function (value, meta, record) {
                        return me.getField(value);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'tweetJSON', text: 'Text', flex: 1,
                    renderer: function (value, meta, record) {
                        return me.getTwitterText(value);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'labelerName', text: 'Labeler', width: 150,
                    renderer: function (value, meta, record) {
                        return me.getField(value);
                    }
                },
                {
                    xtype: 'gridcolumn', dataIndex: 'documentID', text: 'Action', width: 95, sortable: false,
                    renderer: function (recordValue, metaData, record, rowIdx, colIdx, store) {

                        var id = Ext.id();

                        Ext.defer(function () {
                            Ext.widget('button', {
                                exampleId: recordValue,
                                renderTo: id,
                                cls: 'btn btn-red',
                                text: 'Delete',
                                width: 70,
                                action: 'deleteTrainingExample'
                            });
                        }, 50);

                        return Ext.String.format('<div class="margin-left" id="{0}"></div>', id);
                    }
                }
            ]
        });

        this.trainingDataPaging = Ext.create('Ext.toolbar.Paging', {
            cls: 'aidr-paging',
            margin: '12 2 0 2',
            store:'trainingDataStore',
            displayInfo:true,
            displayMsg:'Training Data records {0} - {1} of {2}',
            emptyMsg:'No Training Data records to display'
        });

        this.items = [
            this.breadcrumbs,
            {
                xtype: 'container',
                margin: '5 0 0 0',
                html: '<div class="horizontalLine"></div>'
            },
            {
                xtype: 'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                    this.taggerTitle,
                    {
                        xtype: 'container',
                        layout: 'hbox',
                        items: [
                            this.taggerDescription,
                            this.addTrainingData
                        ]
                    }
                ]
            },
            this.taggerDescription2line,
            this.trainingDataGrid,
            this.trainingDataPaging
        ];

        this.callParent(arguments);
    },

    getField: function (r) {
        return r ? r : "<span class='na-text'>Not specified</span>";
    },

    getTwitterText: function (r) {
        var obj = Ext.JSON.decode(r);
        if (obj && obj.text) {
            return obj.text;
        } else {
            return "<span class='na-text'>Not specified</span>";
        }
    }

});