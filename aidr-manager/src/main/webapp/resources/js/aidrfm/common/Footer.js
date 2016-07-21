Ext.define('AIDRFM.common.Footer', {
    extend: 'Ext.container.Container',
    alias: 'widget.aidr-footer',

    width: '100%',
    html: '<div class="site-footer">' +
        '<div class="footer">' +

        '<div class="right">' +
        '<a href="http://www.qcri.qa/"><span style="position: absolute;margin-top: 12px;right: 290px;">A project by</span> <img align="middle" src="' + BASE_URL + '/resources/img/qcri_90h.png"/></a>' +
        '</div>' +

        '<div class="left">' +
        '<a href="http://aidr.qcri.org/r/tos" target="_blank">Terms of Service</a> - ' +
        '<a href="http://aidr.qcri.org/r/manual">Help</a> - ' +
        '<a href="http://aidr.qcri.org/r/credits">Credits</a>' +
        '</div>' +

        '</div>' +
        '</div>'
});