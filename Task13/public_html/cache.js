$(createCacheTimer);

function createCacheTimer() {
    updateCache();
    setInterval(updateCache, 1000);
}

function updateCache() {
    $.get("/cache", function(data) {
        if (data.includes("<html")) location.replace("/signin.html");
        $("#cache-stats").html(data);
    });
}
