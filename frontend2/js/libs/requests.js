/**
 * Created by developer123 on 31.05.15.
 */
function getXmlHttp(){
    var xmlhttp;
    try {
        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
        try {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (E) {
            xmlhttp = false;
        }
    }
    if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
        xmlhttp = new XMLHttpRequest();
    }
    return xmlhttp;
}
function parseAnswer(response) {
    return response;
}

function sendRequest(url,type,data,headers,callback,async,processData,contentType,xhrfun) {
    async=async||true;
    contentType=(contentType=="!true"?false:"application/json;");
    var requests='ajax';
    if (localStorage['requests'] && localStorage['requests'].length) requests=localStorage['requests'];
    switch (requests) {
        default:
        case 'ajax':
            $.ajax({
                async:async,
                url: url,
                data: data,
                type: type,
                beforeSend: function (request) {
                    $.each(headers,function(i,elem) {
                        request.setRequestHeader(elem.name, elem.value);
                    });
                },
                xhr: xhrfun,
                processData:processData,
                crossDomain:true,
                contentType: "application/json",
                dataType:"json"
            }).always(function(answer){
                callback(parseAnswer(answer));
            });
            break;
        case 'xhr':
            var xmlhttp = getXmlHttp();
            xmlhttp.open(type, url, async);
            if (contentType) xmlhttp.setRequestHeader('Content-Type', 'application/json;');
            for (var i=0;i<headers.length;i++)
                xmlhttp.setRequestHeader(headers[i].name, headers[i].value);
            xmlhttp.onreadystatechange = function() {
                if (xmlhttp.readyState == 4) {
                    callback(parseAnswer(xmlhttp.responseText));
                }
            };
            xmlhttp.send(data);
            break;
        case 'websockets':
            var ws=new WebSocket(url);
            ws.onopen = function(event) {
                console.log('onopen');
                ws.send("Hello Web Socket!");
            };
            ws.onmessage=function(event){
                console.log(event);
            };
            ws.send(json);
            break;
    }
}