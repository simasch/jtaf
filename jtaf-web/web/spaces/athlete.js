var athlete;
var clubs;
var series_id;
var space_id;

function loadData() {
    var id = param().id;
    series_id = param().series_id;
    space_id = param().space_id;

    if (id === undefined) {
        fillClubSelect();
        athlete = new Object();
        el("athlete_lastName").focus();
    } else {
        xhrGet("/jtaf/res/athletes/" + id, function(response) {
            parseAndFill(response);
        });
    }
}

function fillClubSelect() {
    xhrGet("/jtaf/res/clubs?space_id=" + space_id, function(response) {
        clubs = JSON.parse(response);
        var select = el("athlete_club");
        select.innerHTML = "";

        var option = document.createElement("option");
        option.innerHTML = "";
        select.appendChild(option);

        clubs.forEach(function(club) {
            option = document.createElement("option");
            option.value = club.id;
            option.innerHTML = club.abbreviation;
            select.appendChild(option);
            if (athlete.club !== undefined && athlete.club !== null &&
                    athlete.club.id === club.id) {
                option.selected = true;
            }
        });
    });
}

function back() {
    window.location = "series.html?id=" + series_id;
}

function parseAndFill(response) {
    athlete = JSON.parse(response);
    fillForm();
}

function fillForm() {
    el("athlete_id").value = athlete.id;
    el("athlete_lastName").value = athlete.lastName;
    el("athlete_lastName").focus();
    el("athlete_firstName").value = athlete.firstName;
    el("athlete_year").value = athlete.year;
    if (athlete.gender !== undefined && athlete.gender !== null) {
        el("athlete_gender_" + athlete.gender).checked = true;
    } else {
        el("athlete_gender_m").checked = false;
        el("athlete_gender_f").checked = false;
    }
    el("athlete_category").value = athlete.category.abbreviation;
    fillClubSelect();
}

function save() {
    fillAthlete();
    xhrPost("/jtaf/res/athletes/", function(response) {
        parseAndFill(response);
        info("Athlete saved");
    }, athlete);
}

function fillAthlete() {
    athlete.firstName = el("athlete_firstName").value;
    athlete.lastName = el("athlete_lastName").value;
    athlete.year = el("athlete_year").value;
    if (el("athlete_gender_m").checked) {
        athlete.gender = "m";
    } else {
        athlete.gender = "f";
    }
    var select = el("athlete_club");
    var id = select.options[select.selectedIndex].value;
    if (id !== null) {
        clubs.forEach(function(club) {
            if (club.id === parseInt(id)) {
                athlete.club = club;
                return;
            }
        });
    }
    athlete.series_id = series_id;
}
