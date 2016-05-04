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
        '<a href="http://aidr.data.qcri.org/aidr-data/resources/QSS-Usr-Agr-Print-Version.docx.pdf" target="_blank">Terms of Service</a> - ' +
        '<a href="http://aidr.qcri.org/r/manual">Help</a> - ' +
        '<a href="http://aidr.qcri.org/r/credits">Credits</a>' +
        '</div>' +

        '</div>' +
        '</div>'
});