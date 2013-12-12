Ext.define('AIDRFM.common.StandardLayout', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.standard-layout',

    border: false,
    bodyPadding: 40,
    margin: '25 0 0 0',
    width: 1040,
    layout: {
        type: 'vbox',
        align: 'left'
    },

    defaults: {
        width: '100%'
    }
});