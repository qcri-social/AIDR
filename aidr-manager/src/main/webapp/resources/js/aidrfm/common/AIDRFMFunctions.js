Ext.define('AIDRFM.common.AIDRFMFunctions', {

    initMessageContainer: function (){
        msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div'}, true);
        msgCt.setStyle('position', 'absolute');
        msgCt.setStyle('z-index', 99999);
        msgCt.setWidth(400);
    },

    setAlert: function (status, msg) {
        var message = '<ul>';
        var delay = 3;
        if (Ext.isArray(msg)) {
            Ext.each(msg, function (ms) {
                message += '<li>' + ms + '</li>';
            });
            var arrSize  = msg.length;
            if (arrSize  < 2) {
                delay = 3;
            } else {
                delay = 9;
            }
        } else {
            message = '<li>' + msg + '</li>';

            // add some smarts to msg's duration (div by 13.3 between 3 & 9 seconds)
            var delayMeasure  = msg.length / 13.3;
            if (delayMeasure  < 3) {
                delay = 3;
            } else if (delayMeasure  > 9) {
                delay = 9;
            } else {
                delay = delayMeasure ;
            }
        }
        message += '</ul>';
        delay = delay * 1000;

        msgCt.alignTo(document, 't-t');
        Ext.DomHelper.append(msgCt, {html: this.buildMessageBox(status.toLowerCase(), message)}, true).slideIn('t').ghost("t", {delay: delay, remove: true});
    },
    
    reportIssue : function(resp){
    	var mailType="Issue";
    	var link=window.location.href;
    	var description=JSON.stringify(resp.request.options);
    	 Ext.Ajax.request({
    		 url: BASE_URL + '/protected/tagger/sendEmailService.action',
    		 params: {
    		                 url: link, 
    		                 mailType: mailType,
    		                 description: description
    		             },
             headers: {
                 'Accept': 'application/json'
             },
             success: function (response) {
            	 //alert("success"+response.responseText);
            	// alert(window.location.href);
                 
             },
             failure: function (response) {
            	// alert("failure"+response.responseText);
             }
         });
    },

    buildMessageBox : function(title, msg) {
        return [
            '<span id="notify" class="server-' + title + '">',
                '<span class="title">',
                    this.capitaliseFirstLetter(title),
                '</span>',
                '<span>',
                    msg,
                '</span>',
            '</span>'
        ].join('');
    },

    capitaliseFirstLetter: function (string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    },

    maskCount: 0,

    showMask: function(mask){
        if (mask) {
            if (this.maskCount == 0) {
                mask.show();
            }
            this.maskCount++;
        }
    },

    hideMask: function(mask){
        if (mask) {
            this.maskCount--;
            if (this.maskCount == 0) {
                mask.hide();
            }
        }
    },

    getMask: function (showMessage, msg) {
        if (showMessage) {
            if (!msg) {
                msg = 'Loading ...';
            }
        }
        if (this.maskScreen == null) {
            this.maskScreen = new Ext.LoadMask(Ext.getBody(), {msg: msg});
        } else {
            this.maskScreen.msg = msg;
        }
        return this.maskScreen;
    },

    mandatoryFieldsEntered: function () {
        var me = this;

        var isValid = true;
        var form = Ext.getCmp('collectionForm').getForm();
        if (!form.findField('code').getValue()) {
            form.findField('code').markInvalid(['Collection Code is mandatory']);
            AIDRFMFunctions.setAlert('Error', 'Collection Code is mandatory');
            isValid = false;
        }
        if (form.findField('code').getValue() && form.findField('code').getValue().length > 64) {
            form.findField('code').markInvalid(['The maximum length for Collection Code field is 64']);
            AIDRFMFunctions.setAlert('Error', 'The maximum length for Collection Code field is 64');
            isValid = false;
        }
        if (!form.findField('name').getValue()) {
            form.findField('name').markInvalid(['Collection Name is mandatory']);
            AIDRFMFunctions.setAlert('Error', 'Collection Name is mandatory');
            isValid = false;
        }
        if (!form.findField('crisisType').getValue()) {
            form.findField('crisisType').markInvalid(['Collection Type is mandatory']);
            AIDRFMFunctions.setAlert('Error', 'Collection Type is mandatory');
            isValid = false;
        }
        if (form.findField('collectionType').getValue() === 'Twitter' && !(form.findField('track').getValue() || form.findField('geo').getValue() || form.findField('follow').getValue())) {
            AIDRFMFunctions.setAlert('Error', 'One of Keywords, Geo or Follow field is mandatory');
            isValid = false;
        }
        if (form.findField('track').getValue()) {
            var value = form.findField('track').getValue(),
                keywords = value.split(","),
                errors = [],
                isKeywordValid = true;
            Ext.Array.each(keywords, function(v) {
                if (v.length > 60) {
                    if (isKeywordValid){
                        errors.push('Each keywords should not exceed 60 chars.');
                    }
                    errors.push('<b>' + v + '</b> has more than 60 chars');
                    isKeywordValid = false;
                }
            });
            if (!isKeywordValid) {
                isValid = isKeywordValid;
                AIDRFMFunctions.setAlert('Error', errors);
            }
        }
        //
        //console.log('geo value');
        //console.log(form.findField('geo').getValue());
        if (form.findField('geo').getValue() === '') {
            if(form.findField('geoR').getValue().geoR1 !== 'null') {
               AIDRFMFunctions.setAlert('Error', 'You have to add Geographical regions for this option');
               isValid = false; 
            }
        }
        else {
            if(form.findField('geoR').getValue().geoR1 === 'null') {
                AIDRFMFunctions.setAlert('Error', 'Choose another option of Geo boundry strickness');
                isValid = false;
            }
        }
        return isValid;
    },

    getQueryParam: function (name) {
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regexS = "[\\?&]" + name + "=([^&#]*)";
        var regex = new RegExp(regexS);
        var results = regex.exec(window.location.href);
        if (results == null)
            return null;
        else
            return results[1];
    },

    getStatusWithStyle: function(raw, collectionType) {
        var statusText = '';
        var status = collectionType === 'Twitter' ? 'Running' : 'Collecting Live SMS';
        
        if (raw == 'RUNNING') {
            statusText = "<h class='AidrInfo'> " + status + "</h>";
        } else if (raw == 'INITIALIZING') {
            statusText = "<b class='blueInfo'> INITIALIZING </b>";
        } else if (raw == 'STOPPED' || raw == 'FATAL_ERROR') {
            statusText = "<b class='redInfo'>" + raw + " </b>";
        }  else if (raw == 'NOT_RUNNING') {
            statusText = "<b class='warningFont'>" + raw + " </b>";
        }  else if (raw == 'RUNNING_WARNING') {
            statusText = "<b class='warningFont'>" + status + ", with warnings </b>";
        }
        else {
            statusText = "<b class='warningFont'>" + raw + " </b>";
        }
        return statusText;
    },

    getAucNumberWithColors: function(r) {
        var style;
        if (r){
            if (r < 0.6){
                style = 'redInfo';
            } else if (r <= 0.8){
                style = 'warningFont';
            } else {
                style = 'greenInfo';
            }
        } else {
            r = 0;
            style = 'redInfo';
        }
        return '<span class="' + style + '">' + r.toFixed(2) + '</span>';
    },
    
    getAucNumberAsPercentageWithColors: function(r) {
        var style;
        if (r){
            if (r < 0.6){
                style = 'redInfo';
            } else if (r <= 0.8){
                style = 'warningFont';
            } else {
                style = 'greenInfo';
            }
        } else {
            r = 0;
            style = 'redInfo';
        }
        return '<span class="' + style + '">' + (r * 100).toFixed(0) + '%</span>';
    }
    

});

AIDRFMFunctions = new AIDRFM.common.AIDRFMFunctions();

NA_CATEGORY_NAME = 'N/A: does not apply, or cannot judge';

Number.prototype.format = function(n, x) {
    var re = '(\\d)(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\.' : '$') + ')';
    return this.toFixed(Math.max(0, ~~n)).replace(new RegExp(re, 'g'), '$1,');
};

if(!String.linkify) {
    String.prototype.linkify = function() {

        // http://, https://, ftp://
        var urlPattern = /\b(?:https?|ftp):\/\/[a-z0-9-+&@#\/%?=~_|!:,.;]*[a-z0-9-+&@#\/%=~_|]/gim;

        // www. sans http:// or https://
        var pseudoUrlPattern = /(^|[^\/])(www\.[\S]+(\b|$))/gim;

        // Email addresses
        var emailAddressPattern = /\w+@[a-zA-Z_]+?(?:\.[a-zA-Z]{2,6})+/gim;

        return this
            .replace(urlPattern, '<a href="$&" target="_blank">$&</a>')
            .replace(pseudoUrlPattern, '$1<a href="http://$2" target="_blank">$2</a>')
            .replace(emailAddressPattern, '<a href="mailto:$&">$&</a>');
    };
}

Ext.Ajax.on('requestexception', function (conn, response, options) {
    if (response.status == 901) {
        document.location.href = BASE_URL + '/index.jsp'
    } else{
        if(response.status != 0){
            //AIDRFMFunctions.setAlert("Error", "System is down or under maintenance. For further inquiries please contact admin.");
        }
    }
});

Ext.Date.patterns = {
    ISO8601Long:"Y-m-d H:i:s",
    ISO8601Short:"Y-m-d",
    ShortDate: "n/j/Y",
    LongDate: "l, F d, Y",
    FullDateTime: "l, F d, Y g:i:s A",
    MedumDateTime: "F d, Y g:i:s A",
    MonthDay: "F d",
    ShortTime: "g:i A",
    LongTime: "g:i:s A",
    SortableDateTime: "Y-m-d\\TH:i:s",
    UniversalSortableDateTime: "Y-m-d H:i:sO",
    YearMonth: "F, Y"
};