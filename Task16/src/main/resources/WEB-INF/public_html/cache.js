var ws;

$(initSocket);

$("body")
    .on("click", "#refresh-button", refresh);

function initSocket() {
    var loc = window.location;
    var ws_uri = "ws://" + loc.host + "/cache";

    ws = new WebSocket(ws_uri);
    ws.onopen = function (event) {
        $("#cache-stats").html("Начинаем...");
        refresh();
    };
    ws.onmessage = function (event) {
        $("#cache-stats").html(event.data);
    };
    ws.onclose = function (event) {
        $("#cache-stats").html("Соединение закрыто");
    };
}

function refresh() {
    ws.send(JSON.stringify({
        type: "GET_CACHE_STATS"
    }));
}