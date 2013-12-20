Ext.define('AIDRFM.common.Footer', {
    extend: 'Ext.container.Container',
    alias: 'widget.aidr-footer',

    width: '100%',
    html: '<div class="site-footer"><div class="footer"><a style="text-decoration: none;color: #ffffff" href="http://www.qcri.qa/">A project by <img align="middle" id="footerqcrilogo" src="' + BASE_URL + '/resources/img/qcri-gray-horiz.png"></a></div>' +
        '</div>'
});