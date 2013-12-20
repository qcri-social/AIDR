Ext.define('AIDRFM.common.Footer', {
    extend: 'Ext.container.Container',
    alias: 'widget.aidr-footer',

    width: '100%',
    html: '<div class="site-footer">' +
        '<div class="footer">' +

        '<div style="float: right;">' +
        '<a style="text-decoration: none; color: #ffffff; font-size: 13px;" href="http://www.qcri.qa/">A project by <img align="middle" id="footerqcrilogo" src="' + BASE_URL + '/resources/img/qcri-gray-horiz.png"/></a>' +
        '</div>' +

        '<div style="float: left; padding-top: 16px;">' +
        '<a style="text-decoration: underline; color: #ffffff; font-size: 13px;" href="http://aidr.qcri.org/tos/">Terms of Service</a>' +
        '</div>' +

        '</div>' +
        '</div>'
});