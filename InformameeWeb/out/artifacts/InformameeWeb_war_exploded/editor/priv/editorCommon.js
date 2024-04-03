function DateTime(options) {
    var BeginDispOutput = options['BeginDispOutput'];
    var EndDispOutput = options['EndDispOutput'];
    var DurationLabel = options['DurationLabel'];
    var BeginPicker = options['BeginPicker'];
    var EndPicker = options['EndPicker'];
    var BeginSlider = options['BeginSlider'];
    var EndSlider = options['EndSlider'];

    var begindate = options['begindate'];
    var enddate = options['enddate'];
    var internaldatevalid = false;

    var DateValidSetter;
    var BeginDateSetter;
    var EndDateSetter;

    var prevsetBegin = 0;
    var prevsetEnd = 0;

    function updateLabelBegin() {
        var currhour = begindate.getHours();
        var currminutes = begindate.getMinutes();
        if (currhour < 10) {
            currhour = '0' + currhour.toString();
        }
        if (currminutes < 10) {
            currminutes = '0' + currminutes.toString();
        }
        BeginDispOutput.innerHTML = currhour + ':' + currminutes;
    }

    function updateLabelEnd() {
        var currhour = enddate.getHours();
        var currminutes = enddate.getMinutes();
        if (currhour < 10) {
            currhour = '0' + currhour.toString();
        }
        if (currminutes < 10) {
            currminutes = '0' + currminutes.toString();
        }
        EndDispOutput.innerHTML = currhour + ':' + currminutes;
    }

    function updateDuration() {
        var HoursNotTrunc = (enddate - begindate) / 3600000;
        var diffDays = Math.trunc(HoursNotTrunc / 24);
        var diffHours = Math.ceil(HoursNotTrunc - (diffDays * 24));

        if (diffDays < 0 || (diffDays == 0 && diffHours <= 0)) {
            var ToShow = "Non valida! La fine dell'evento deve essere successiva all'inizio.";
            if (internaldatevalid) {
                DurationLabel.style.color = "red";
                internaldatevalid = false;
            }
        } else {
            var ToShow = diffDays + " giorni " + diffHours + "  ore";
            if (!internaldatevalid) {
                DurationLabel.style.color = "black";
                internaldatevalid = true;
            }
        }
        DurationLabel.innerHTML = ToShow;
        DateValidSetter(internaldatevalid)
    }

    function setBegin(selectedval) {
        begindate.setHours(begindate.getHours() + (selectedval - prevsetBegin));
        updateLabelBegin();
        BeginPicker.datepicker("setDate", begindate);
        prevsetBegin = selectedval;
        BeginDateSetter(begindate);
    }

    function setEnd(selectedval) {
        enddate.setHours(enddate.getHours() + (selectedval - prevsetEnd));
        updateLabelEnd();
        EndPicker.datepicker("setDate", enddate);
        prevsetEnd = selectedval;
        EndDateSetter(enddate);
    }

    return {
        startPickers: function () {
            BeginSlider.slider({
                orientation: "horizontal",
                range: false,
                min: 0,
                max: 24,
                value: 0,
                step: 4,
                animate: true,
                slide: function (event, ui) {
                    setBegin(ui.value);
                    updateDuration();
                },
            });

            EndSlider.slider({
                orientation: "horizontal",
                range: false,
                min: 0,
                max: 24,
                value: 0,
                step: 4,
                animate: true,
                slide: function (event, ui) {
                    setEnd(ui.value);
                    updateDuration();
                }
            });

            BeginPicker.datepicker({
                onSelect: function () {
                    var tmpdate = $(this).datepicker('getDate');
                    begindate.setFullYear(tmpdate.getFullYear());
                    begindate.setMonth(tmpdate.getMonth());
                    begindate.setDate(tmpdate.getDate());

                    updateLabelBegin();
                    updateDuration();
                    BeginDateSetter(begindate);
                }
            });

            BeginPicker.datepicker("setDate", begindate);
            updateLabelBegin();

            EndPicker.datepicker({
                onSelect: function () {
                    var tmpdate = $(this).datepicker('getDate');
                    enddate.setFullYear(tmpdate.getFullYear());
                    enddate.setMonth(tmpdate.getMonth());
                    enddate.setDate(tmpdate.getDate());
                    updateLabelEnd();
                    updateDuration();
                    EndDateSetter(enddate);
                }
            });

            EndPicker.datepicker("setDate", enddate);
            updateLabelEnd();
        },
        registerDateValidSetter: function (Setter) {
            DateValidSetter = Setter;
        },
        registerBeginDateSetter: function (Setter) {
            BeginDateSetter = Setter;
        },
        registerEndDateSetter: function (Setter) {
            EndDateSetter = Setter;
        },
        updateDuration: function () {
            updateDuration();
        }
    }

}

function CommonChecks(options) {
    var EmptyEpicentreCapAlertBox = options['EmptyEpicentreCapAlertBox'];
    var EpicentreCapInput = options['EpicentreCapInput'];
    var EmptyMercalliAlertBox = options['EmptyMercalliAlertBox'];
    var MercalliInput = options['MercalliInput'];
    var EmptyRichterAlertBox = options['EmptyRichterAlertBox'];
    var RichterInput = options['RichterInput'];
    var DescriptionInput = options['DescriptionInput'];
    var EmptyDescriptionAlertBox = options['EmptyDescriptionAlertBox'];

    return {
        CheckEpicentreCap: function () {
            if (EpicentreCapInput.value.trim().length < 1) {
                EmptyEpicentreCapAlertBox.innerHTML = "Inserisci il cap del presunto epicentro";
                return false;
            }
            EmptyEpicentreCapAlertBox.innerHTML = "";
            return true;
        },
        CheckMercalli: function () {
            if (MercalliInput.value.trim().length < 1) {
                EmptyMercalliAlertBox.innerHTML = "Inserisci magnitudo in scala Mercalli";
                return false;
            }
            EmptyMercalliAlertBox.innerHTML = "";
            return true;
        },
        CheckRichter: function () {
            if (RichterInput.value.trim().length < 1) {
                EmptyRichterAlertBox.innerHTML = "Inserisci magnitudo in scala Richter";
                return false;
            }
            EmptyRichterAlertBox.innerHTML = "";
            return true;
        },
        CheckDescriptionLegit: function () {
            if (DescriptionInput.value.trim().length < 1) {
                EmptyDescriptionAlertBox.innerHTML = "Compila la descrizione!";
                // $("#emptydescriptionalert").text("Compila la descrizione!");
                return false;
            }
            EmptyDescriptionAlertBox.innerHTML = "";
            return true;
        }
    }
}