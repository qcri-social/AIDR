Ext.define('AIDRFM.common.Footer', {
    extend: 'Ext.container.Container',
    alias: 'widget.aidr-footer',

    width: '100%',
    html: '<div class="site-footer">' +
        '<div class="footer">' +

        '<div class="right">' +
        '<a href="http://www.qcri.qa/">A project by <img align="middle" src="' + BASE_URL + '/resources/img/qcri-gray-horiz.png"/></a>' +
        '</div>' +

        '<div class="left">' +
        '<a href="http://aidr.qcri.org/tos/">Terms of Service</a>' +
        '</div>' +

        '</div>' +
        '</div>'
});