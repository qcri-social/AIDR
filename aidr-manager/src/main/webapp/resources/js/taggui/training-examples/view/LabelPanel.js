Ext.define('TAGGUI.training-examples.view.LabelPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.label-panel-view',

    layout: 'vbox',
    padding: '7 0 7 0',
    width: '100%',

    initComponent: function () {
        var me = this;

        this.optionRG = Ext.create('Ext.form.RadioGroup', {
            columns: 1,
            vertical: true,
            width: 700,
            items: []
        });

        this.optionPanel = Ext.create('Ext.container.Container', {
            flex: 1,
            margin: 0,
            layout: 'hbox',
            items: [
                this.optionRG
            ]
        });

        this.items = [
            this.optionPanel
        ];

        this.callParent(arguments);
    },

    showData: function(attr){
        var me = this;
        if (attr.nominalLabelJsonModelSet && Ext.isArray(attr.nominalLabelJsonModelSet)) {
            var labels = attr.nominalLabelJsonModelSet;

            labels.sort(function(a, b){
                if(a.sequence < b.sequence) return -1;
                if(a.sequence > b.sequence) return 1;
                return 0;
            });

            Ext.each(attr.nominalLabelJsonModelSet, function (lbl) {
                me.optionRG.add({
                    boxLabel: '<span class="styled-text"><b>' + lbl.name + '</b></span>&nbsp;<span class="small-gray-text">' +lbl.description + '</span>',
                    name: attr.nominalAttributeID,
                    code: lbl.norminalLabelCode
                });
            })
        }
    }

});