Ext.define('AIDRPUBLIC.interactive-view-download.view.SingleFilterPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.single-filter-panel',

    margin: 0,
    padding: '5 0',
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    rawData: null,

    initComponent: function () {
        var me = this;


        this.mainComboData = [{
            val: 'date_query',
            label: 'Date/Time'
        }];

        this.dataByNominalAttribute =[];

        if (this.rawData.nominalAttributeJsonModelSet instanceof Array && this.rawData.nominalAttributeJsonModelSet.length > 0) {

            Ext.Array.each(this.rawData.nominalAttributeJsonModelSet, function (r) {
                me.mainComboData.push({
                    val: r.code,
                    label: r.name
                });

                var nominalLabels = [];
                if (r.nominalLabelJsonModelSet instanceof Array && r.nominalLabelJsonModelSet.length > 0){
                    Ext.Array.each(r.nominalLabelJsonModelSet, function (l) {
                        nominalLabels.push({
                            val: l.norminalLabelCode,
                            label: l.name
                        });
                    });
                }

                me.dataByNominalAttribute[r.code] = nominalLabels;
            });

        }

        this.mainComboStore = Ext.create('Ext.data.Store', {
            fields: ['val', 'label'],
            data: this.mainComboData
        });

        this.mainCombo = Ext.create('Ext.form.ComboBox', {
            editable: false,
            emptyText: 'Select ...',
            valueField: 'val',
            displayField: 'label',
            width: 150,
            store: this.mainComboStore,
            listeners: {
                select: function (cmp, selectedValues) {
                    me.onMainComboSelect(cmp, selectedValues);
                }
            }
        });

        this.dateTypeStore = Ext.create('Ext.data.Store', {
            fields: ['val', 'label'],
            data: [
                { val: 'is_after', label: 'is after' },
                { val: 'is_before', label: 'is before' }
            ]
        });

        this.dateTypeCombo = Ext.create('Ext.form.ComboBox', {
            editable: false,
            emptyText: 'Select ...',
            valueField: 'val',
            displayField: 'label',
            padding: '0 0 0 15',
            width: 150,
            store: this.dateTypeStore,
            hidden: true,
            listeners: {
                select: function (cmp, selectedValues) {
                    me.onDateTypeComboSelect(cmp, selectedValues);
                }
            }
        });

        this.dateField = Ext.create('Ext.form.field.Date', {
        	padding: '0 0 0 15',
        	hidden: true,
        	format: 'Y-m-d',
        	width: 150,
        	listeners: {
        		select: function (cmp, value) {
        			me.onDateFieldSelect(cmp, value);
        		}
        	}
        });

        //hidden field to save date and time together
        this.dateTime = Ext.create('Ext.form.field.Date',{
        	format: 'Y-m-d h:i A',
        	hidden     : true
        });

        this.dateFieldHint = Ext.create('Ext.form.Label', {
            padding: '3 0 0 15',
            html: '(Year-Month-Day)',
            hidden: true
        });
        
        this.timeField = Ext.create('Ext.form.field.Time', {
        	padding: '0 0 0 15',
        	hidden: true,
        	format: 'h:i A',
        	width: 150,
        	increment: 1,
        	value: Ext.Date.parse('12:01 AM', 'h:i A'),
        	minValue: Ext.Date.parse('12:01 AM', 'h:i A'),
        	maxValue: Ext.Date.parse('11:59 PM', 'h:i A'),            
        	listeners: {
        		select: function (cmp, value) {
                    me.onTimeFieldSelect(cmp, value);
                }
            }
        });
        
        this.timeFieldHint = Ext.create('Ext.form.Label', {
            padding: '3 0 0 15',
            html: 'GMT',
            hidden: true
        });

        this.confidenceStore = Ext.create('Ext.data.Store', {
            fields: ['val', 'label'],
            data: [
                { val: 0.5, label: '50%' },
                { val: 0.6, label: '60%' },
                { val: 0.7, label: '70%' },
                { val: 0.8, label: '80%' },
                { val: 0.9, label: '90%' },
                { val: 1.0, label: '100%' }
            ]
        });

        this.confidenceCombo = Ext.create('Ext.form.ComboBox', {
            editable: false,
            emptyText: 'Select ...',
            valueField: 'val',
            displayField: 'label',
            padding: '0 0 0 15',
            width: 70,
            store: this.confidenceStore,
            hidden: true,
            value: 0.7,
            listeners: {
                select: function (cmp, selectedValues) {
                    me.onConfidenceComboSelect(cmp, selectedValues);
                }
            }
        });

        this.classifierTypeStore = Ext.create('Ext.data.Store', {
            fields: ['val', 'label'],
            data: [
                { val: 'is', label: 'is' },
                { val: 'is_not', label: 'is not' },
                { val: 'has_confidence', label: 'has confidence at least' }
            ]
        });

        this.classifierTypeCombo = Ext.create('Ext.form.ComboBox', {
            editable: false,
            emptyText: 'Select ...',
            valueField: 'val',
            displayField: 'label',
            padding: '0 0 0 15',
            width: 150,
            store: this.classifierTypeStore,
            hidden: true,
            listeners: {
                select: function (cmp, selectedValues) {
                    me.onClassifierTypeComboSelect(cmp, selectedValues);
                }
            }
        });

        this.labelsStore = Ext.create('Ext.data.Store', {
            fields: ['val', 'label']
        });

        this.labelCombo = Ext.create('Ext.form.ComboBox', {
            editable: false,
            emptyText: 'Select ...',
            valueField: 'val',
            displayField: 'label',
            padding: '0 0 0 15',
            width: 150,
            store: this.labelsStore,
            hidden: true,
            queryMode: 'local',
            listeners: {
                select: function (cmp, selectedValues) {
                    me.onLabelComboSelect(cmp, selectedValues);
                }
            }
        });

        this.invalidSign =  Ext.create('Ext.form.Label', {
            height: 16,
            width: 16,
            html: '<image src="/AIDRFetchManager/resources/img/exclamation_red.png" />' ,
            margin: '3 0 0 0',
            listeners: {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: "Please select correct values.",
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            }
        });

        this.removeButton = Ext.create('Ext.button.Button', {
            iconCls: 'remove',
            scale: 'small',
            margin: '0 10 0 0',
            tooltip: 'Remove this filter',
            listeners: {
                click: function (cmp) {
                    me.onRemoveButtonClick(cmp);
                }
            }
        });

        this.items = [
            this.removeButton,
            this.mainCombo,
            this.dateTypeCombo,
            this.dateField,
            this.dateFieldHint,
            this.timeField,
            this.timeFieldHint,
            this.classifierTypeCombo,
            this.labelCombo,
            this.confidenceCombo,
            {
                xtype: 'container',
                layout: 'hbox',
                flex: 1
            },
            this.invalidSign
        ];

        this.callParent(arguments);
    },

    onMainComboSelect: function(cmp, selectedValues){
        var val = selectedValues[0].data.val;

        if (!val){
            this.markNotValid();
            return false;
        }
        this.suspendLayout = true;

        if (val == 'date_query') {
            this.dateTypeCombo.show();
            if (this.dateTypeCombo.getValue()){
                this.dateField.show();
                this.dateFieldHint.show();
                this.timeField.show();
                this.timeFieldHint.show();
            }
            this.classifierTypeCombo.hide();
            this.confidenceCombo.hide();
            this.labelCombo.hide();
        } else {
            this.dateTypeCombo.hide();
            this.dateField.hide();
            this.dateFieldHint.hide();
            this.timeField.hide();
            this.timeFieldHint.hide();
            this.classifierTypeCombo.setValue();
            this.classifierTypeCombo.show();
            this.confidenceCombo.hide();
            this.confidenceCombo.setValue(0.7);
            this.labelCombo.hide();
            this.labelCombo.setValue();
        }

        this.suspendLayout = false;
        this.updateLayout();

        this.onChange();
        return true;
    },

    onDateTypeComboSelect: function(cmp, selectedValues){
        var val = selectedValues[0].data.val;

        if (!val){
            this.dateField.hide();
            this.dateFieldHint.hide();
            this.timeField.hide();
            this.timeFieldHint.hide();
            this.markNotValid();
            return false;
        }

        this.suspendLayout = true;

        this.dateField.show();
        this.dateFieldHint.show();
        this.timeField.show();
        this.timeFieldHint.show();
        
        if(val == 'is_after')
        	this.timeField.setValue('11:59 PM');
        else
        	this.timeField.setValue('12:01 AM');

        this.suspendLayout = false;
        this.updateLayout();

        this.onChange();
        return true;
    },

    onClassifierTypeComboSelect: function(cmp, selectedValues){
        var val = selectedValues[0].data.val;

        if (!val){
            this.labelCombo.hide();
            this.markNotValid();
            return false;
        }

        this.suspendLayout = true;

        if (val == 'has_confidence'){
            this.confidenceCombo.show();
            this.labelCombo.hide();
        } else {
            if (!this.labelCombo.getValue()) {
                this.confidenceCombo.hide();
            }

            var classifier_code = this.mainCombo.getValue();
            var labels = this.dataByNominalAttribute[classifier_code];
            this.labelsStore.loadData(labels);

            this.labelCombo.show();
        }

        this.suspendLayout = false;
        this.updateLayout();

        this.onChange();
        return true;
    },

    onLabelComboSelect: function(cmp, selectedValues){
        var val = selectedValues[0].data.val;

        if (!val){
            this.markNotValid();
            return false;
        }

        this.suspendLayout = true;

        this.confidenceCombo.show();

        this.suspendLayout = false;
        this.updateLayout();

        this.onChange();
        return true;
    },

    onDateFieldSelect: function(cmp, value){
        if (!value){
            this.markNotValid();
            return false;
        }

        this.onChange();
        return true;
    },
    
    onTimeFieldSelect: function(cmp, value){     
	if (!value){
            this.markNotValid();
            return false;
        }

        this.onChange();
        return true;
    },

    getValue: function(){
        var values = {};

        var queryType = this.mainCombo.getValue();
        if (!queryType){
            return values;
        }

        if (queryType == 'date_query'){
        	values.queryType = queryType;
        	values.comparator = this.dateTypeCombo.getValue();
        	
        	//concatenate date and time values and convert to timestamp
        	var d = Ext.Date.format(this.dateField.getValue(),'Y-m-d');
        	var t = Ext.Date.format(this.timeField.getValue(),'h:i A');  
        	this.dateTime.setValue(d + ' ' + t);
        	values.timestamp = Ext.Date.format(this.dateTime.getValue(), 'U');
        } else {
        	values.queryType = 'classifier_query';
        	values.classifier_code = queryType;
        	values.comparator = this.classifierTypeCombo.getValue();
        	if (values.comparator == 'has_confidence'){
        		values.label_code = null;
        	} else {
                values.label_code = this.labelCombo.getValue();
            }
            values.min_confidence = this.confidenceCombo.getValue();
        }

        return values;
    },

    onRemoveButtonClick: function(cmp){
        cmp.disable();
        var parent = this.up('#filterBlock');

        this.suspendLayout = true;

//        Add empty filter if we are going to remove last one from existing
        var parentItemsCount = parent.items.length;
        if (parentItemsCount == 1) {
            parent.insert(0, {
                rawData: this.rawData
            });
        }

//        Remove current filter from filters panel
        parent.remove(this, true);

        this.suspendLayout = false;
        this.updateLayout();

        this.onChange(true);
    },

    onConfidenceComboSelect: function(cmp, selectedValues){
        var val = selectedValues[0].data.val;

        if (!val){
            this.markNotValid();
            return false;
        }

        this.onChange();
        return true;
    },

    markValid: function(){
        this.invalidSign.hide();
    },

    markNotValid: function(){
        this.invalidSign.show();
    },

    isValid: function(){
        var result = true;

        var queryType = this.mainCombo.getValue();
        if (!queryType){
            result = false;
        }

        var comparator;
        if (queryType == 'date_query'){
            comparator = this.dateTypeCombo.getValue();
            var timestamp = this.dateField.getValue();
            if (!(comparator && timestamp)){
                result = false;
            }
        } else {
            comparator = this.classifierTypeCombo.getValue();
            var label_code;
            if (comparator == 'has_confidence'){
                label_code = true;
            } else {
                label_code = this.labelCombo.getValue();
            }
            var confidence = this.confidenceCombo.getValue();
            if (!(comparator && label_code && confidence)){
                result = false;
            }
        }

        if (result){
            this.markValid();
        } else {
            this.markNotValid();
        }

        return result;
    },
    
    onChange: function(fromRemove){
        var parent;
        if (fromRemove) {
            parent = Ext.getCmp('filterBlock');
        } else {
            parent = this.up('#filterBlock');
        }
        parent.fireEvent('filterchange', parent);
    }

});