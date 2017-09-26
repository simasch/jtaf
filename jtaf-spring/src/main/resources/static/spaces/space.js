function SpaceController() {
    var self = this;
    var util = new Util();
    
    var space;
    
    this.loadData = function() {
        util.showMessage();
        var id = util.searchMap.id;
        if (id === undefined) {
            space = {};
            createSeriesTableBody();
            createClubsTableBody();
            document.getElementById("space_name").focus();
            util.i18n();
        } else {
            util.xhrGet("/res/spaces/" + id, function(response) {
                parseAndFillSpace(response);
                util.i18n();
            });
        }
    };
    this.deleteSeries = function(id) {
        if (confirm(util.translate("Are you sure?"))) {
            util.xhrDelete("/res/series/" + id, function() {
                self.loadData();
                util.info("Serie deleted");
            });
        }
    };
    this.copySeries = function(id) {
        if (confirm(util.translate("Are you sure?"))) {
            util.xhrPost("/res/series/" + id + "?function=copy", function() {
                self.loadData();
                util.info("Series copied");
            });
        }
    };
    this.deleteClub = function(id) {
        if (confirm(util.translate("Are you sure?"))) {
            util.xhrDelete("/res/clubs/" + id, function() {
                self.loadData();
                util.info("Club deleted");
            });
        }
    };
    this.addSeries = function() {
        window.location.href = "series.html?space_id=" + space.id;
    };
    this.addClub = function() {
        window.location.href = "club.html?space_id=" + space.id;
    };
    this.save = function() {
        fillSpace();
        util.xhrPost("/res/spaces/", function(response) {
            parseAndFillSpace(response);
            util.info("Space saved");
        }, space);
    };

    function fillSpace() {
        space.name = document.getElementById("space_name").value;
    }

    function parseAndFillSpace(response) {
        space = JSON.parse(response);
        fillForm();
        createSeriesTableBody();
        createClubsTableBody();
    }

    function fillForm() {
        document.getElementById("space_id").value = space.id;
        document.getElementById("space_name").value = space.name;
        document.getElementById("space_name").focus();
    }

    function createSeriesTableBody() {
        var table = document.getElementById("series_table");
        table.innerHTML = "";
        if (space.series === undefined || space.series.length === 0) {
            var row = table.insertRow(0);
            var cell = row.insertCell(0);
            cell.innerHTML = util.translate("No series found");
            cell.setAttribute("colspan", 2);
        }
        else {
            var i = 0;
            space.series.forEach(function(series) {
                var row = table.insertRow(i);
                var onclickEdit = "window.location = 'series.html?id=" + series.id + "&space_id=" + space.id + "'";
                var cellName = row.insertCell(0);
                cellName.className = "edit";
                cellName.innerHTML = series.name;
                cellName.setAttribute("onclick", onclickEdit);
                var exporting = document.createElement("a");
                exporting.href = "/res/series/" + series.id + "?function=export";
                exporting.setAttribute("target", "_blank");
                var exportingSpan = document.createElement("span");
                exportingSpan.className = "i18n";
                exportingSpan.innerHTML = "Export";
                exporting.appendChild(exportingSpan);
                var copy = document.createElement("a");
                copy.href = "#";
                copy.setAttribute("onclick", "spaceController.copySeries(" + series.id + ")");
                var copySpan = document.createElement("span");
                copySpan.className = "i18n";
                copySpan.innerHTML = "Copy";
                copy.appendChild(copySpan);
                var del = document.createElement("a");
                del.href = "#";
                del.setAttribute("onclick", "spaceController.deleteSeries(" + series.id + ")");
                var delSpan = document.createElement("span");
                delSpan.className = "i18n";
                delSpan.innerHTML = "Delete";
                del.appendChild(delSpan);
                var cellFunction = row.insertCell(1);
                cellFunction.style.width = "200px";
                cellFunction.style.textAlign = "right";
                cellFunction.appendChild(exporting);
                cellFunction.appendChild(document.createTextNode(" "));
                cellFunction.appendChild(copy);
                cellFunction.appendChild(document.createTextNode(" "));
                cellFunction.appendChild(del);
                cellFunction.appendChild(document.createTextNode(" "));
                var lock = document.createElement("img");
                if (series.locked) {
                    lock.src = "../images/locked.png";
                }
                else {
                    lock.src = "../images/unlocked.png";
                }
                cellFunction.appendChild(lock);
                i++;
            });
        }
    }

    function createClubsTableBody() {
        var table = document.getElementById("club_table");
        table.innerHTML = "";
        if (space.clubs === undefined || space.clubs.length === 0) {
            var row = table.insertRow(0);
            var cell = row.insertCell(0);
            cell.innerHTML = util.translate("No clubs found");
            cell.setAttribute("colspan", 3);
        }
        else {
            var i = 0;
            space.clubs.forEach(function(club) {
                var row = table.insertRow(i);
                var onclickEdit = "window.location = 'club.html?id=" + club.id + "&space_id=" + space.id + "'";
                var cellAbbr = row.insertCell(0);
                cellAbbr.className = "edit";
                cellAbbr.innerHTML = club.abbreviation;
                cellAbbr.setAttribute("onclick", onclickEdit);
                var cellName = row.insertCell(1);
                cellName.className = "edit";
                cellName.innerHTML = club.name;
                cellName.setAttribute("onclick", onclickEdit);
                var del = document.createElement("a");
                del.setAttribute("href", "#");
                del.setAttribute("onclick", "spaceController.deleteClub(" + club.id + ")");
                var delSpan = document.createElement("span");
                delSpan.className = "i18n";
                delSpan.innerHTML = "Delete";
                del.appendChild(delSpan);
                var cellFunction = row.insertCell(2);
                cellFunction.style.width = "150px";
                cellFunction.style.textAlign = "right";
                cellFunction.appendChild(del);
                i++;
            });
        }
    }

}