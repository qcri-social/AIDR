Ext.require([
    'AIDRFM.common.AIDRFMFunctions',
    'AIDRFM.common.StandardLayout'
]);

Ext.define('TAGGUI.home.view.TaggerHomePanel', {
    extend: 'AIDRFM.common.StandardLayout',
    alias: 'widget.tagger-home-view',

    initComponent: function () {
        var me = this;

        this.breadcrumbs = Ext.create('Ext.container.Container', {
            html: '<div class="bread-crumbs"><a href="' + BASE_URL + '/protected/tagger-home">Tagger</a></div>',
            margin: 0,
            padding: 0
        });

        this.taggerTitle = Ext.create('Ext.form.Label', {
            cls: 'header-h1 bold-text',
            text: 'Tagger',
            flex: 1
        });

        this.manageCollectionsButton = Ext.create('Ext.Button', {
            text: 'Manage your collections',
            margin: '27 0 0 0',
            cls:'btn btn-blue',
            id: 'manageCollections'
        });

        this.taggerDescription = Ext.create('Ext.form.Label', {
            cls: 'styled-text',
            margin: '0 0 15 0',
            text: 'Automatic tagging is enabled for the following collections:',
            flex: 1
        });

        this.horisontalLine = Ext.create('Ext.container.Container', {
            width: '100%',
            html: '<div class="horisontalLine"></div>'
        });

        this.crisesStore = Ext.create('Ext.data.JsonStore', {
            pageSize: 10,
            storeId: 'crisesStore',
            fields: ['crisisID', 'code', 'name', 'crisisType', 'users', 'modelFamilyCollection'],
            proxy: {
                type: 'ajax',
                url: 'tagger/getCrisesByUserId.action',
                reader: {
                    root: 'data',
                    totalProperty: 'total',
                    messageProperty: 'message'
                }
            },
            autoLoad: true,
            listeners: {
                load: function (store, records, successful, operation, eOpts) {
                    var proxy = store.getProxy(),
                        reader = proxy.getReader(),
                        raw = reader.rawData,
                        msg = reader.getMessage(raw);
                    if (!successful) {
                        AIDRFMFunctions.setAlert("Error", msg);
                    }
                }
            }
        });

        this.crisesTpl = new Ext.XTemplate(
            '<div class="collections-list">',
            '<tpl for=".">',

            '<div class="collection-item">',


            '<div class="content">',

            '<div class="img">',
            '<a href="{[this.getEncodedCode(values.code)]}/tagger-collection-details"><img alt="Collection image" src="/AIDRFetchManager/resources/img/AIDR/AIDR_EMBLEM_CMYK_COLOUR_HR.jpg" width="70"></a>',
            '</div>',

            '<div class="info">',
            '<div class="collection-title"><a href="{[this.getEncodedCode(values.code)]}/tagger-collection-details">{name}</a></div>',
            '<div class="styled-text-14 div-top-padding" id="statusField_{crisisID}">{[this.getAttributes(values.modelFamilyCollection, values.code)]}</div>',
            '</div>',

            '</div>',
            '</div>',

            '</tpl>',
            '</div>',
            {
                getAttributes: function (attr, code) {
                    var result = '';
                    if (attr && attr.length > 0) {
                        Ext.Array.each(attr, function(r, index) {
                            if (index == 0){
                                result = 'Classifiers being used:&nbsp;&nbsp;';
                            }
                            var nominalAttribute = r.nominalAttribute;
                            if (nominalAttribute && nominalAttribute.name) {
                                result = result + nominalAttribute.name + ', ';
                            }
                        });
                        return result.substring(0, result.length - 2);
                    } else {
                        return '<a href="' + this.getEncodedCode(code) + '/predict-new-attribute">Add a new classifier &raquo;</a>';
                    }
                },
                getEncodedCode: function(code) {
                    return encodeURI(code);
                }
            }
        );

        this.crisesView = Ext.create('Ext.view.View', {
            store: this.crisesStore,
            tpl: this.crisesTpl,
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
                    {
                        xtype: 'container',
                        layout: 'hbox',
                        margin: '5 0',
                        items: [
                            this.taggerTitle,
                            this.manageCollectionsButton
                        ]
                    },
                    this.taggerDescription
                ]
            },
            {
                xtype: 'container',
                width: '100%',
                html: '<div class="horisontalLine"></div>'
            },
            this.crisesView
        ];

        this.callParent(arguments);
    }

})