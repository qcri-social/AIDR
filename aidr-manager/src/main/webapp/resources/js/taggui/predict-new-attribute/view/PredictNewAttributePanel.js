Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout'
]);

Ext.define('TAGGUI.predict-new-attribute.view.PredictNewAttributePanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.predict-new-attribute-view',

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs">' +
                '<a href="' + BASE_URL + '/protected/tagger-home">Tagger</a><span>&nbsp;>&nbsp;</span>' +
                '<a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/tagger-collection-details">' + COLLECTION_NAME + '</a><span>&nbsp;>&nbsp;Add classifier</span></div>',
            margin: 0,
            padding: 0
        });

        this.newCustomAttribute = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs"><a href="' + BASE_URL + '/protected/' + CRISIS_CODE + '/new-custom-attribute">Add a custom classifier &raquo;</a></div>',
            margin: 0,
            padding: '30 0 0 0'
        });

        this.emptySpace = Ext.create('Ext.container.Container', {
            html: '',
            margin: 0,
            padding: '30 0 0 0'
        });

        this.pageTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Add a classifier to "' + COLLECTION_NAME + '"',
            flex: 1
        });

        this.pageDescription = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            margin: '0 0 15 0',
            text: 'Displaying available classifiers for your collection, except for those you are already using.',
            flex: 1
        });

        this.horisontalLine = Ext.create('Ext.container.Container', {
            width: '100%',
            html: '<div class="horisontalLine"></div>'
        });

        this.standardAttributesStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 100,
            storeId: 'standardAttributesStore',
            fields: ['code', 'description', 'name', 'nominalAttributeID', 'nominalLabelCollection'],
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

        this.customAttributesStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 100,
            storeId: 'customAttributesStore',
            fields: ['code', 'description', 'name', 'nominalAttributeID', 'nominalLabelCollection'],
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

        this.standardAttributesTpl = new Ext.XTemplate(
            '<div class="attribute-list">',

            '<tpl for=".">',

            '<tpl if="xindex == 1">' +
            '<div class="attributes-title"><div class="inner"><h2>Standard classifiers</h2></div></div>' +
            '</tpl>' +

            '<div class="attribute-item">',

            '<button id="buttonAddAttribute_{nominalAttributeID}" class="btn btn-blue" onclick="predictNewAttributeController.addAttributeToCrises({nominalAttributeID}, \'{name}\')">',
            '<span>Add</span>',
            '</button>',

            '<div class="content">',

            '<div class="img">',
            '<a href="' + BASE_URL + '/protected/{nominalAttributeID}/attribute-details"><img alt="Attribute image" src="/AIDRFetchManager/resources/img/AIDR/AIDR_EMBLEM_CMYK_COLOUR_HR.jpg" width="70"></a>',
            '</div>',

            '<div class="info">',
            '<div class="collection-title"><a href="' + BASE_URL +  '/protected/{nominalAttributeID}/attribute-details">{name}</a></div>',
            '<div class="styled-text-14" id="docCountField_{id}">Description:&nbsp;&nbsp;&nbsp;{description}</div>',
            '<div class="styled-text-14" id="lastDocField_{id}">Categories:&nbsp;&nbsp;&nbsp;{[this.getLabels(values.nominalLabelCollection)]}</div>',
            '</div>',

            '</div>',
            '</div>',

            '</tpl>',

            '</div>',
            {
                getLabels: function (lables) {
                    var result = '';
                    if (lables && lables.length > 0) {
                        Ext.Array.each(lables, function(r, index) {
                            if (r.name) {
                                result = result + r.name + ', ';
                            }
                        });
                        return result.substring(0, result.length - 2);
                    }
                    return result;
                }
            }
        );

        this.customAttributesTpl = new Ext.XTemplate(
            '<div class="collections-list">',

            '<tpl for=".">',

            '<tpl if="xindex == 1">' +
            '<div class="attributes-title"><div class="inner"><h2>Custom classifiers</h2></div></div>' +
            '</tpl>' +

            '<div class="collection-item">',

            '<button id="buttonAddAttribute_{nominalAttributeID}" class="btn btn-blue" onclick="predictNewAttributeController.addAttributeToCrises({nominalAttributeID}, \'{name}\')">',
            '<span>Add</span>',
            '</button>',

            '<div class="content">',

            '<div class="img">',
            '<a href="' + BASE_URL + '/protected/{nominalAttributeID}/attribute-details"><img alt="Attribute image" src="/AIDRFetchManager/resources/img/AIDR/AIDR_EMBLEM_CMYK_COLOUR_HR.jpg" width="70"></a>',
            '</div>',

            '<div class="info">',
            '<div class="collection-title"><a href="' + BASE_URL +  '/protected/{nominalAttributeID}/attribute-details">{name}</a></div>',
            '<div class="styled-text-14" id="docCountField_{id}">Description:&nbsp;&nbsp;&nbsp;{description}</div>',
            '<div class="styled-text-14" id="lastDocField_{id}">Values:&nbsp;&nbsp;&nbsp;{[this.getLabels(values.nominalLabelCollection)]}</div>',
            '</div>',

            '</div>',
            '</div>',

            '</tpl>',

            '</div>',
            {
                getLabels: function (lables) {
                    var result = '';
                    if (lables && lables.length > 0) {
                        Ext.Array.each(lables, function(r, index) {
                            if (r.name) {
                                result = result + r.name + ', ';
                            }
                        });
                        return result.substring(0, result.length - 2);
                    }
                    return result;
                }
            }
        );

        this.standardAttributesView = Ext.create('Ext.view.View', {
            store: this.standardAttributesStore,
            id: 'standardAttributesViewId',
            tpl: this.standardAttributesTpl,
            itemSelector: 'li.crisesItem'
        });

        this.customAttributesView = Ext.create('Ext.view.View', {
            store: this.customAttributesStore,
            id: 'customAttributesViewId',
            tpl: this.customAttributesTpl,
            itemSelector: 'li.crisesItem'
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
                    this.pageTitle,
                    this.pageDescription
                ]
            },
            {
                xtype: 'container',
                width: '100%',
                html: '<div class="horisontalLine"></div>'
            },
            this.standardAttributesView,
            this.emptySpace,
            this.customAttributesView,
            this.newCustomAttribute
        ];

        this.callParent(arguments);
    }

})