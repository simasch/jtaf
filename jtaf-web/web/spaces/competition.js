var competition;
var series_id;

function loadData() {
    series_id = param().series_id;
    var id = param().id;
    if (id === undefined) {
        competition = new Object();
        el("competition_name").focus();
    } else {
        xhrGet("/jtaf/res/competitions/" + id, function(response) {
            parseAndFill(response);
        });
    }
}

function back() {
    window.location = "series.html?id=" + series_id;
}

function parseAndFill(response) {
    competition = JSON.parse(response);
    fillForm();
}

function fillForm() {
    el("competition_id").value = competition.id;
    el("competition_name").value = competition.name;
    el("competition_date").value = competition.competitionDate;
    el("competition_name").focus();
}

function save() {
    fillCompetition();
    xhrPost("/jtaf/res/competitions/", function(response) {
        parseAndFill(response);
        info("Competition saved");
    }, competition);
}

function fillCompetition() {
    competition.name = el("competition_name").value;
    competition.competitionDate = el("competition_date").value;
    competition.series_id = series_id;
}

function deleteCompetition(id) {
    if (confirm("Are you sure?")) {
        xhrDelete("/jtaf/res/competitions/" + id, function() {
            loadData();
            info("Competition deleted");
        });
    }
}
