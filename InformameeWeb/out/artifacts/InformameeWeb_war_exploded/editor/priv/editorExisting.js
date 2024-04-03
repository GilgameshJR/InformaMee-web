document.addEventListener("DOMContentLoaded", function () {
    var PageOrchestratorInst = PageOrchestrator();
    PageOrchestratorInst.start();
});

function PageOrchestrator() {
    var begindate = new Date(parseInt(document.getElementById("beginpicker").getAttribute("data-oldbegin"), 10));
    var enddate = new Date(parseInt(document.getElementById("endpicker").getAttribute("data-oldend"), 10));

    var DateTimeInst = DateTime({
        BeginDispOutput: document.getElementById("beginDisp"),
        EndDispOutput: document.getElementById("endDisp"),
        DurationLabel: document.getElementById("durationDisp"),
        BeginPicker: $("#beginpicker"),
        EndPicker: $("#endpicker"),
        BeginSlider: $("#begin_slider"),
        EndSlider: $("#end_slider"),
        begindate: begindate,
        enddate: enddate
    });

    var datevalid = false;

    DateTimeInst.registerBeginDateSetter(function (BeginDateToSet) {
        begindate = BeginDateToSet;
    });
    DateTimeInst.registerEndDateSetter(function (EndDateToSet) {
        enddate = EndDateToSet;
    });
    DateTimeInst.registerDateValidSetter(function (DateValidToSet) {
        datevalid = DateValidToSet;
    });
    var Form = document.getElementById("ToSubmit");
    var CapInputsOrchInst = CapInputsOrchestrator(
        document.getElementById("capinputcontainer"),
        Form
    );
    CapInputsOrchInst.RegisterExistingInputs(parseInt(document.getElementById("caplist").getAttribute("data-oldcapcounter")));
    var EventTypeSelector = document.getElementById("type");
    var EventInst = Event({
        EventTypeSelector: EventTypeSelector,
        BeginTimeToSend: document.getElementById("begin"),
        EndTimeToSend: document.getElementById("end"),
        EpicentreCapInput: document.getElementById("epicentrecap"),

        EmptyEpicentreCapAlertBox: document.getElementById("emptyepicentrecapalert"),
        EpicentreCapInput: document.getElementById("epicentrecap"),
        EmptyMercalliAlertBox: document.getElementById("emptymercallialert"),
        MercalliInput: document.getElementById("mercalli"),
        EmptyRichterAlertBox: document.getElementById("emptyrichteralert"),
        RichterInput: document.getElementById("richter"),
        DescriptionInput: document.getElementById("description"),
        EmptyDescriptionAlertBox: document.getElementById("emptydescriptionalert"),
    });

    var MapReverseInst = MapReverse(
        document.getElementById("map"),
        CapInputsOrchInst.WriteCapInInputBox
    );
    var CapBtn = document.getElementById("capbtn");
    var RemoveAllBtn = document.getElementById("removeall");
    function RegisterListeners() {
        CapBtn.addEventListener("click", CapInputsOrchInst.AddCapInput);
        RemoveAllBtn.addEventListener("click", CapInputsOrchInst.ClearAllInputs);
        Form.addEventListener("submit", function (event) {
            if (EventInst.checkandsub(datevalid, begindate, enddate, CapInputsOrchInst) === false) {
                event.stopPropagation();
                event.preventDefault();
                window.scrollTo(0, 0);
            }
        });
    }

    return {
        start: function () {
            RegisterListeners();
            DateTimeInst.startPickers();
            DateTimeInst.updateDuration();
            CapInputsOrchInst.AddCapListener(document.getElementById("cap0"));
            if (EventTypeSelector.value === "seismic")
                CapInputsOrchInst.AddCapListener(document.getElementById("epicentrecap"));
            MapReverseInst.Load();
        }
    }
}

function Event(options) {
    var EventTypeSelector = options['EventTypeSelector'];
    var BeginTimeToSend = options['BeginTimeToSend'];
    var EndTimeToSend = options['EndTimeToSend'];
    var EpicentreCapInput = options['EpicentreCapInput'];

    var CommonChecksInst = CommonChecks(options);

    return {
        checkandsub: function (datevalid, begindate, enddate, CapInputsOrchInst) {
            //updateDuration();
            const CheckDescriptionLegitRes = CommonChecksInst.CheckDescriptionLegit();

            var CheckEpicentreCapRes = true;
            var CheckMercalliRes = true;
            var CheckRichterRes = true;

            if (EventTypeSelector.value === "seismic") {
                CheckEpicentreCapRes = CommonChecksInst.CheckEpicentreCap();
                CheckMercalliRes = CommonChecksInst.CheckMercalli();
                CheckRichterRes = CommonChecksInst.CheckRichter();
            }
            if (!(CheckDescriptionLegitRes && datevalid && CheckEpicentreCapRes && CheckMercalliRes && CheckRichterRes)) { //&&CheckWeatherDetailRes&&
                window.scrollTo(0, 0);
                return false;
            }
            BeginTimeToSend.value = begindate.getTime();
            EndTimeToSend.value = enddate.getTime();
            if (CapInputsOrchInst.CreateCapStringBeforeSubmit() === null) {
                alert("Inserisci almeno un CAP");
                return false;
            }
            if (EventTypeSelector.value === "seismic" && !(EpicentreCapInput.value.trim().length === 0))
                return CapInputsOrchInst.CheckAllCapAreLegitSeismic();
            return CapInputsOrchInst.CheckAllCapAreLegit();
        }
    }
}
