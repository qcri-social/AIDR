Ext.require([
             'Ext.chart.*'
             ]);

var store = Ext.create('Ext.data.JsonStore', {
    pageSize: 100,
    storeId: 'modelLabelsStoreForClassifierHistoryChart',
    //model : 'ModelForClassifiedDoc',
    fields: ['trainingTime','avgPrecision', 'avgRecall', 'avgAuc', 'trainingCount'],
    proxy: {
        type: 'ajax',
        url: BASE_URL + '/protected/tagger/modelHistory.action',
        reader: {
            root: 'data',
            totalProperty: 'total'
        },
        extraParams : {
                        id: MODEL_FAMILY_ID,
                        'limit' : -1,
                    }
    },
    autoLoad: true
});


Ext.define("TAGGUI.model-details.view.ClassifierHistoryChart", {
	extend: 'Ext.chart.Chart',
	alias: 'widget.ClassifierHistoryChart',
	width: 900,
    height: 350,
    animate: true,
    store: store,
	
    //Will uncomment when working on zooming of the chart
	/*enableMask: true,
    mask: 'true',
    constructor : function(configs){
        configs.enableMask =this.enableMask;
        this.callParent(arguments);
    },
    listeners: {
        select: {
            fn: function(me, selection) {
                me.setZoom(selection);
                me.mask.hide();
            }
        }
    },*/
    legend: {
        position: 'right'
    },
    axes: [{
        type: 'Numeric',
        fields: ['avgPrecision', 'avgRecall', 'avgAuc' ],
        position: 'left',
        grid: true,
        minimum: 0,
        label:{
        	renderer: function (r, meta, record) {
                return r ? (r * 100).toFixed(0) + '%' : '0%';
            }
        }
    }, {
        type: 'Time',
		dateFormat: 'M j,y',
        fields: 'trainingTime',
        position: 'bottom',
        grid: true,
		label:{
			rotate: {
                    	degrees: -90
                    }
		}
    },
    {
        type: 'Numeric',
        fields: ['trainingCount'],
        position: 'right',
        grid: true,
        minimum: 0
    }],
    series: [{
        type: 'line',
        axis: 'left',
        title: 'Avg Precision',
        xField: 'trainingTime',
        yField: 'avgPrecision',
        style: {
            lineWidth: 4
        },
        marker: {
            radius: 4
        },
        highlight: {
            fillStyle: '#000',
            radius: 5,
            lineWidth: 4,
            strokeStyle: '#fff'
        },
		tips: {
              trackMouse: true,
              width: 180,
              //height: 50,
              renderer: function(storeItem, item) {
 	        	 var timestamp = Ext.Date.format(new Date(storeItem.get(item.series.xField)), "Y-m-d H:i");
 	           	 var val = storeItem.get(item.series.yField);
 	           	 val = val ? (val * 100).toFixed(0) + '%' : '0%';
 				 this.setTitle(item.series.title +' at ' + timestamp + ' is ' + val );
 			  }
		}
    }, {
        type: 'line',
        axis: 'left',
        title: 'Avg Recall',
        xField: 'trainingTime',
        yField: 'avgRecall',
        style: {
            lineWidth: 4
        },
        marker: {
            radius: 4
        },
        highlight: {
            fillStyle: '#000',
            radius: 5,
            lineWidth: 4,
            strokeStyle: '#fff'
        },
		tips: {
            trackMouse: true,
            width: 180,
            //height: 50,
            renderer: function(storeItem, item) {
	        	 var timestamp = Ext.Date.format(new Date(storeItem.get(item.series.xField)), "Y-m-d H:i");
	           	 var val = storeItem.get(item.series.yField);
	           	 val = val ? (val * 100).toFixed(0) + '%' : '0%';
				 this.setTitle(item.series.title +' at ' + timestamp + ' is ' + val );
			  }
		}
    }, {
        type: 'line',
        axis: 'left',
        title: 'Avg AUC',
        xField: 'trainingTime',
        yField: 'avgAuc',
        style: {
            lineWidth: 4
        },
        marker: {
            radius: 4
        },
        highlight: {
            fillStyle: '#000',
            radius: 5,
            lineWidth: 4,
            strokeStyle: '#fff'
        },
		tips: {
            trackMouse: true,
            width: 180,
           // height: 50,
            renderer: function(storeItem, item) {
	        	 var timestamp = Ext.Date.format(new Date(storeItem.get(item.series.xField)), "Y-m-d H:i");
	           	 var val = storeItem.get(item.series.yField);
	           	 val = val ? (val * 100).toFixed(0) + '%' : '0%';
				 this.setTitle(item.series.title +' at ' + timestamp + ' is ' + val );
			  }
		}
    }, {
        type: 'line',
        axis: 'right',
        title: 'Training Count',
        xField: 'trainingTime',
        yField: 'trainingCount',
        style: {
            lineWidth: 4
        },
        marker: {
            radius: 4
        },
        highlight: {
            fillStyle: '#000',
            radius: 5,
            lineWidth: 4,
            strokeStyle: '#fff'
        },
		tips: {
            trackMouse: true,
            width: 180,
            //height: 50,
            renderer: function(storeItem, item) {
          	  var timestamp = Ext.Date.format(new Date(storeItem.get(item.series.xField)), "Y-m-d H:i");
				  this.setTitle(item.series.title +' at ' + timestamp + ' is ' + storeItem.get(item.series.yField) + ' ' + COLLECTION_TYPES[TYPE]["plural"]);
			  }
		}
   }]
   
   });


//Patch for Mask i.e used for zooming of chart.
//Will uncomment this patch when working on zooming of chart
/*Ext.override(Ext.chart.Mask, {
    require: ['Ext.chart.MaskLayer'],
    *//**
     * Creates new Mask.
     * @param {Object} config (optional) Config object.
     *//*
    constructor: function(config) {
        var me = this;


        me.addEvents('select');


        if (config) {
            Ext.apply(me, config);
        }
        if (me.mask) {
            me.on('afterrender', function() {
                //create a mask layer component
                var comp = Ext.create('Ext.chart.MaskLayer', {
                    renderTo: me.el,
                    // BEGIN SETH CHANGE
                    maskType : me.mask
                    // END SETH CHANGE
                });
                comp.el.on({
                    // BEGIN SETH CHANGE
                    'mousedown': function(e) {
                        // hack to hide tooltips
                        // code duplicated below, but can't move to function because override
                        // won't recognize it
                        Ext.each(me.series.items, function(ser, idx) {
                           if (ser.tooltip) {
                              ser.savedTooltip = ser.tooltip;
                              ser.tooltip = null;
                           }
                        });
                    },
                    // END SETH CHANGE
                    'mousemove': function(e) {
                        me.onMouseMove(e);
                        // BEGIN SETH CHANGE
                        // used below in mouseup listener
                        me.mouseMoved = true;
                        // END SETH CHANGE
                    },
                    'mouseup': function(e) {
                       // BEGIN SETH CHANGE
                       // the resizer may accidentally catch the first mouse up after mouse down
                       // if the cursor happens to fall *within* the mask
                       if (me.mouseDown)
                          me.onMouseUp(e);
                       else if (me.mouseMoved)
                          // "resized" should be renamed "reselect"
                          me.resized(e);
                       // END SETH CHANGE
                    }
                });
                // BEGIN SETH CHANGE
                var handles = 'all';
                switch (me.mask) {
                case 'horizontal':
                   handles = 'e w';
                   break;
                case 'vertical':
                   handles = 'n s';
                   break;
                }
                // END SETH CHANGE
                //create a resize handler for the component
                var resizeHandler = Ext.create('Ext.resizer.Resizer', {
                    el: comp.el,
                    // BEGIN SETH CHANGE
                    handles: handles,
                    pinned: false
                    // END SETH CHANGE
                });
                resizeHandler.on({
                    'resize': function(e) {
                        me.resized(e);    
                    }    
                });
                // BEGIN SETH CHANGE
                // vertical or horizontal should not be able to be dragged around
                if(me.mask === true) 
                   comp.initDraggable();
                // END SETH CHANGE
                me.maskType = me.mask;
                me.mask = comp;
                me.maskSprite = me.surface.add({
                    type: 'path',
                    path: ['M', 0, 0],
                    zIndex: 1001,
                    opacity: 0.7,
                    hidden: true,
                    stroke: '#444'
                });
            }, me, { single: true });
        }
    },
    
    resized: function(e) {
        var me = this,
            bbox = me.bbox || me.chartBBox,
            x = bbox.x,
            y = bbox.y,
            width = bbox.width,
            height = bbox.height,
            box = me.mask.getBox(true),
            max = Math.max,
            min = Math.min,
            staticX = box.x - x,
            staticY = box.y - y;
        
        staticX = max(staticX, x);
        staticY = max(staticY, y);
        staticX = min(staticX, width);
        staticY = min(staticY, height);
        box.x = staticX;
        box.y = staticY;
        me.fireEvent('select', me, box);
    },


    onMouseUp: function(e) {
        var me = this,
            bbox = me.bbox || me.chartBBox,
            sel = me.maskSelection;
        me.maskMouseDown = false;
        me.mouseDown = false;
        if (me.mouseMoved) {
            me.onMouseMove(e);
            me.mouseMoved = false;
            me.fireEvent('select', me, {
                x: sel.x - bbox.x,
                y: sel.y - bbox.y,
                width: sel.width,
                height: sel.height
            });
        }
        
        // BEGIN SETH CHANGE
        // hack to undo hack to hide tooltips
        Ext.each(me.series.items, function(ser, idx) {
           if (ser.savedTooltip) {
              ser.tooltip = ser.savedTooltip;
           }
        });
        // END SETH CHANGE
    },


    onMouseDown: function(e) {
        var me = this;
        me.mouseDown = true;
        me.mouseMoved = false;
        me.maskMouseDown = {
            x: e.getPageX() - me.el.getX(),
            y: e.getPageY() - me.el.getY()
        };
        
        // BEGIN SETH CHANGE
        // hack to hide tooltips
        Ext.each(me.series.items, function(ser, idx) {
           if (ser.tooltip) {
              ser.savedTooltip = ser.tooltip;
              ser.tooltip = null;
           }
        });
        // END SETH CHANGE
    },


    onMouseMove: function(e) {
        var me = this,
            mask = me.maskType,
            bbox = me.bbox || me.chartBBox,
            x = bbox.x,
            y = bbox.y,
            math = Math,
            floor = math.floor,
            abs = math.abs,
            min = math.min,
            max = math.max,
            height = floor(y + bbox.height),
            width = floor(x + bbox.width),
            posX = e.getPageX(),
            posY = e.getPageY(),
            staticX = posX - me.el.getX(),
            staticY = posY - me.el.getY(),
            maskMouseDown = me.maskMouseDown,
            path;
        
        me.mouseMoved = me.mouseDown;
        staticX = max(staticX, x);
        staticY = max(staticY, y);
        staticX = min(staticX, width);
        staticY = min(staticY, height);
        if (maskMouseDown && me.mouseDown) {
            if (mask == 'horizontal') {
                // BEGIN SETH CHANGE
                staticY = y - me.insetPadding;
                maskMouseDown.y = height;
                posY = me.el.getY() + y - me.insetPadding;
                // END SETH CHANGE
            }
            else if (mask == 'vertical') {
                // BEGIN SETH CHANGE 
                staticX = x - me.insetPadding;
                maskMouseDown.x = width;
                 posX = me.el.getX() + x - me.insetPadding;
                // END SETH CHANGE
            }
            width = maskMouseDown.x - staticX;
            height = maskMouseDown.y - staticY;
            path = ['M', staticX, staticY, 'l', width, 0, 0, height, -width, 0, 'z'];
            // this x and y are relative to the chart
            me.maskSelection = {
                x: width > 0 ? staticX : staticX + width,
                y: height > 0 ? staticY : staticY + height,
                width: abs(width),
                height: abs(height)
            };
            
            // BEGIN SETH CHANGE
            // this x and y must be relative to the page
            me.mask.updateBox({
               // if dragging left-to-right, go from the click to the cursor
               // if dragging right-to-left, go from the cursor to the click
               x: posX + (width < 0 ? width : 0),
               y: posY + (height < 0 ? height : 0),
               width: abs(width),
               height: abs(height)
            });
            // END SETH CHANGE
            
            me.mask.show();
            me.maskSprite.setAttributes({
                hidden: true    
            }, true);
        }
        else {
            if (mask == 'horizontal') {
                path = ['M', staticX, y, 'L', staticX, height];
            }
            else if (mask == 'vertical') {
                path = ['M', x, staticY, 'L', width, staticY];
            }
            else {
                path = ['M', staticX, y, 'L', staticX, height, 'M', x, staticY, 'L', width, staticY];
            }
            me.maskSprite.setAttributes({
                path: path,
                fill: me.maskMouseDown ? me.maskSprite.stroke : false,
                'stroke-width': mask === true ? 1 : 3,
                hidden: false
            }, true);
        }
    },


    onMouseLeave: function(e) {
        var me = this;
        me.mouseMoved = false;
        me.mouseDown = false;
        me.maskMouseDown = false;
        // me.mask.hide();
        // me.maskSprite.hide(true);
    }
 });
 
 // overriding to control styles
 Ext.override(Ext.chart.MaskLayer, {
    extend: 'Ext.Component',
    
    constructor: function(config) {
        // BEGIN SETH CHANGE
        // only show a move cursor if you should be moving the mask
        var style = 'position:absolute;background-color:#888;opacity:0.4;border:1px solid #222;';
        if (config.maskType === true)
           style += 'cursor:move;';
        // END SETH CHANGE
        config = Ext.apply(config || {}, {
            style: style
        });
        this.callParent([config]);    
    },
    
    initComponent: function() {
        var me = this;
        me.callParent(arguments);
        me.addEvents(
            'mousedown',
            'mouseup',
            'mousemove',
            'mouseenter',
            'mouseleave'
        );
    },


    initDraggable: function() {
        this.callParent(arguments);
        this.dd.onStart = function (e) {
            var me = this,
                comp = me.comp;
    
            // Cache the start [X, Y] array
            this.startPosition = comp.getPosition(true);
    
            // If client Component has a ghost method to show a lightweight version of itself
            // then use that as a drag proxy unless configured to liveDrag.
            if (comp.ghost && !comp.liveDrag) {
                 me.proxy = comp.ghost();
                 me.dragTarget = me.proxy.header.el;
            }
    
            // Set the constrainTo Region before we start dragging.
            if (me.constrain || me.constrainDelegate) {
                me.constrainTo = me.calculateConstrainRegion();
            }
        };
    }
});*/