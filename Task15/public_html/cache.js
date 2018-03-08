var ws;

$(initSocket);

$("body")
    .on("click", "#refresh-button", refresh);

function initSocket() {
    ws = new WebSocket("ws://localhost:8080/ms/cache");
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