/**
 * Created by developer123 on 16.03.15.
 */
$.ajax({
    async: false,
    url: 'config.json',
    method: "GET",
    dataType: "json"
}).always(function (data) {
    for (var key in data)
        localStorage[key] = data[key];
});
localStorage['path'] = (localStorage['debug'] == 'true') ? "" : localStorage['revision'] + '/';
var language = window.navigator.userLanguage || window.navigator.language;
localStorage['locale']=((language.match(/^[a-zA-Z]{2}/)[0]).toLowerCase())||"en";
var localeflag=false,locale={};
while (!localeflag) {
    $.ajax({
        async: false,
        url: 'locale/' + localStorage['locale'] + '.json',
        method: "GET",
        dataType: "json"
    }).always(function (data) {
        if (data.status==404) {
            localStorage['locale']='en';
            localeflag=true;
        }
        else {
            localeflag=true;
            for (var key in data)
                locale[key] = data[key];
            localStorage['locale']=JSON.stringify(locale);
        }
    });
}

function Localize(str) {
    var locale=JSON.parse(localStorage['locale']);
    return (locale[str]);
}

var apiurl = localStorage["api"] || "/api";
if (localStorage['defaultheaders']) {
    var session = JSON.parse(localStorage['defaultheaders']);
    session = session[0].value;
} else session = null;