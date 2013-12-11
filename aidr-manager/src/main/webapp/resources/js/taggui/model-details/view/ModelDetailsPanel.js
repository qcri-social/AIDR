Ext.require([
    'AIDRFM.common.AIDRFMFunctions'
]);

Ext.define('TAGGUI.model-details.view.ModelDetailsPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.model-details-view',
    border: false,
    bodyPadding: 40,
    margin: '25 0',
    width: 1040,
    layout: {
        type: 'vbox',
        align: 'left'
    },

    defaults: {
        width: '100%'
    },

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/tagger-home">Tagger</a><span>&nbsp;>&nbsp;</span>' +
                '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">' + CRISIS_NAME + '</a><span>&nbsp;>&nbsp;Classifier details</span></div>',
            margin: 0,
            padding: 0
        });

        this.taggerTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Classifier "' + MODEL_NAME,
            flex: 1
        });

        this.modelDetails = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            margin: '0 0 15 0',
            html: 'Has classified <b>0</b> messages.&nbsp;<a href="' + BASE_URL +  '/protected/' + CRISIS_CODE + '/' + MODEL_ID + '/' + MODEL_FAMILY_ID
                + '/training-data">Manage training examples &raquo;</a>',
            flex: 1
        });

        this.linkToAttribute = Ext.create('Ext.container.Container', {
//            TODO check if this is correct place we need to go to
            html: '<div class="bread-crumbs"><a href="' + BASE_URL + '/protected/' + MODEL_ID + '/attribute-details">Edit classifier "' + MODEL_NAME + '" &raquo;</a></div>',
            margin: 0,
            padding: '30 0 0 0',
            flex:1
        });

        this.horisontalLine = Ext.create('Ext.container.Container', {
            width: '100%',
            html: '<div class="horisontalLine"></div>'
        });

        this.modelLabelsStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 100,
            storeId: 'modelLabelsStore',
            fields: ['value', 'classifiedDocumentCount', 'labelAuc', 'labelPrecision', 'labelRecall', 'trainingDocumentsCount'],
            proxy: {
                type: 'ajax',
                url: '',
                reader: {
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            autoLoad: false
        });

        this.modelLabelsTpl = new Ext.XTemplate(
            '<div class="collections-list">',
            '<tpl for=".">',

            '<div class="collection-item">',

            '<div class="img">',
            '<tpl if="xindex != xcount">' +
                '<img alt="Collection History image" src="/AIDRFetchManager/resources/img/AIDR/tag.png" width="70">',
            '</tpl>',
            '<tpl if="xindex == xcount">' +
                '<div style="width: 70px;"></div>',
            '</tpl>',
            '</div>',

            '<div class="content">',

            '<div class="rightColumn">',
            '<tpl if="xindex != xcount">' +
                '<div class="styled-text-17">Category:</div>',
            '</tpl>',
            '<tpl if="xindex == xcount">' +
                '<div class="styled-text-17">Summary:</div>',
            '</tpl>',
            
            '<div>Training examples:</div>',
            '<div>Classified messages:</div>',
            '<div>Precision:</div>',
            '<div>Recall:</div>',
            '<div>AUC:</div>',
            '</div>',

            '<div class="leftColumn">',

            '<tpl if="xindex != xcount">' +
                '<div class="styled-text-17">{[this.getField(values.value)]}</div>',
            '</tpl>',
            '<tpl if="xindex == xcount">' +
                '<div class="styled-text-17">&nbsp;</div>',
            '</tpl>',

            '<div>{[this.getNumber(values.trainingDocumentsCount)]}</div>',
            '<div>{[this.getNumber(values.classifiedDocumentCount)]}</div>',
            '<div>{[this.getNumber(values.labelPrecision)]}</div>',
            '<div>{[this.getNumber(values.labelRecall)]}</div>',
            '<div>{[this.getNumber(values.labelAuc)]}</div>',

            '</div>',

            '</div>',
            '</div>',

            '</tpl>',
            '</div>',
            {
                getField: function (r) {
                    return r ? r : "<span class='na-text'>Not specified</span>";
                },
                getNumber: function (r) {
                    return r ? r : 0;
                }
            }
        );

        this.modelLabelsView = Ext.create('Ext.view.View', {
            store: this.modelLabelsStore,
            tpl: this.modelLabelsTpl,
            itemSelector: 'div.active',
            loadMask: false
        });

        this.items = [
            this.breadcrumbs,
            {
                xtype: 'container',
                margin: '5 0 0 0',
                html: '<div class="horisontalLine"></div>'
            },
            {
                xtype: 'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                    this.taggerTitle,
                    this.modelDetails
                ]
            },
            {
                xtype: 'container',
                width: '100%',
                html: '<div class="horisontalLine"></div>'
            },
            this.modelLabelsView,
            this.linkToAttribute
        ];

        this.callParent(arguments);
    }

})