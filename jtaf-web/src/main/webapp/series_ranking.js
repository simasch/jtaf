function SeriesRankingController() {
    var util = new Util();
    var ranking;

    this.loadData = function() {
        util.showMessage();
        util.showLoading();

        var id = util.searchMap.id;
        if (id === undefined) {
        } else {
            util.xhrGet("/jtaf/res/rankings/series/" + id, function(response) {
                parseAndFill(response);
                util.i18n();
                util.hideLoading();
            });
        }
    };

    this.openAsPdf = function() {
        var newtab = window.open();
        newtab.location = "/jtaf/res/reports/seriesranking?seriesid=" + ranking.series.id;
    };

    function parseAndFill(response) {
        ranking = JSON.parse(response);

        var table = document.createElement("table");
        table.style.width = "100%;";
        var row = table.insertRow(0);
        var left = row.insertCell(0);
        var hleft = document.createElement("h1");
        hleft.innerHTML = '<span class="i18n">Series ranking</span>';
        left.appendChild(hleft);

        var middle = row.insertCell(1);
        middle.style.textAlign = "center";
        var hmiddle = document.createElement("h1");
        hmiddle.innerHTML = ranking.series.name;
        middle.appendChild(hmiddle);

        document.getElementById("title").appendChild(table);

        createTable();
    }

    function createTable() {
        ranking.categories.forEach(function(category) {
            var table = document.createElement("table");
            table.className = "ranking";
            var row = document.createElement("tr");
            var cell = document.createElement("td");
            cell.setAttribute("colspan", 6);

            var title = document.createElement("h3");
            title.innerHTML = createCategoryTitle(category.category);
            cell.appendChild(title);
            row.appendChild(cell);
            table.appendChild(row);

            var rank = 1;
            category.athletes.forEach(function(athlete) {
                row = document.createElement("tr");

                cell = document.createElement("td");
                cell.setAttribute("style", "width: 40px");
                cell.innerHTML = rank + ".";
                row.appendChild(cell);
                cell = document.createElement("td");
                cell.style.width = "200px";
                cell.innerHTML = athlete.lastName;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.style.width = "200px";
                cell.innerHTML = athlete.firstName;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.style.width = "50px";
                cell.innerHTML = athlete.year;
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.style.width = "150px";
                if (athlete.club !== undefined && athlete.club !== null) {
                    cell.innerHTML = athlete.club.abbreviation;
                }
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.style.textAlign = "right;";
                cell.innerHTML = calculateTotalPoints(athlete);
                row.appendChild(cell);

                table.appendChild(row);

                row = document.createElement("tr");

                cell = document.createElement("td");
                row.appendChild(cell);

                cell = document.createElement("td");
                cell.className = "small";
                cell.setAttribute("colspan", 5);
                cell.innerHTML = createResultRow(athlete);
                row.appendChild(cell);

                table.appendChild(row);

                rank++;
            });
            document.getElementById("ranking").appendChild(table);
        });

    }

    function calculateTotalPoints(athlete) {
        var total = 0;
        athlete.results.forEach(function(result) {
            total += result.points;
        });
        return total;
    }

    function createResultRow(athlete) {
        var text = "";
        var first = true;

        ranking.series.competitions.forEach(function(competition) {
            if (!first) {
                text += "&nbsp;&nbsp;";
            }
            text += competition.name + ": ";

            var total = 0;
            athlete.results.forEach(function(result) {
                if (result.competition.id === competition.id) {
                    total += result.points;
                }
            });
            text += total;
            first = false;
        });
        return text;
    }

    function createCategoryTitle(category) {
        return category.abbreviation + " " + category.name + " " +
                category.yearFrom + " - " + category.yearTo;
    }

}