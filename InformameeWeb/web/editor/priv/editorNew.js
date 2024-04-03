document.addEventListener("DOMContentLoaded", function () {
    var PageOrchestratorInst = PageOrchestrator();
    PageOrchestratorInst.start();
});

function PageOrchestrator() {

    var begindate = new Date();
    var enddate = new Date();

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

    var isWeather = document.getElementById("weathertype");
    var isSeismic = document.getElementById("seismictype");
    var EventInst = Event({
        SelectedDanger: document.getElementById("danger"),
        EmptyDangerAlertBox: document.getElementById("emptydangeralert"),
        WeatherDetailInput: document.getElementById("weatherdetail"),
        EmptyWeatherDetailAlertBox: document.getElementById("emptyweatherdetailalert"),
        isWeather: isWeather,
        isSeismic: isSeismic,
        BeginTimeToSend: document.getElementById("begin"),
        EndTimeToSend: document.getElementById("end"),
        EmptyEpicentreCapAlertBox: document.getElementById("emptyepicentrecapalert"),
        EpicentreCapInput: document.getElementById("epicentrecap"),
        EmptyMercalliAlertBox: document.getElementById("emptymercallialert"),
        MercalliInput: document.getElementById("mercalli"),
        EmptyRichterAlertBox: document.getElementById("emptyrichteralert"),
        RichterInput: document.getElementById("richter"),
        DescriptionInput: document.getElementById("description"),
        EmptyDescriptionAlertBox: document.getElementById("emptydescriptionalert"),
    });

    var SwitchOrchInst = SwitchOrchestrator({
        WeatherInputs: document.getElementsByClassName("weatherinputs"),
        WeatherInputsGlob: document.getElementsByClassName("weatherinputsglob"),
        SeismicInputs: document.getElementsByClassName("seismicinputs"),
        SeismicInputsGlob: document.getElementsByClassName("seismicinputsglob"),
    });

    var MapReverseInst = MapReverse(
        document.getElementById("map"),
        CapInputsOrchInst.WriteCapInInputBox
    );

    var isTerrorist = document.getElementById("terroristtype");
    var CapBtn = document.getElementById("capbtn");
    var RemoveAllBtn = document.getElementById("removeall");
    function RegisterListeners() {
        isWeather.addEventListener("click", SwitchOrchInst.ShowWeatherOnly);
        isSeismic.addEventListener("click", SwitchOrchInst.ShowSeismicOnly);
        isTerrorist.addEventListener("click", SwitchOrchInst.ShowTerroristOnly);
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
            CapInputsOrchInst.AddCapListener(document.getElementById("cap0"));
            CapInputsOrchInst.AddCapListener(document.getElementById("epicentrecap"));
            MapReverseInst.Load();
        }
    }
}

function Event(options) {
    var SelectedDanger = options['SelectedDanger'];
    var EmptyDangerAlertBox = options['EmptyDangerAlertBox'];
    var WeatherDetailInput = options['WeatherDetailInput'];
    var EmptyWeatherDetailAlertBox = options['EmptyWeatherDetailAlertBox'];
    var isWeather = options['isWeather'];
    var isSeismic = options['isSeismic'];
    var BeginTimeToSend = options['BeginTimeToSend'];
    var EndTimeToSend = options['EndTimeToSend'];
    var EpicentreCapInput = options['EpicentreCapInput'];

    var CommonChecksInst = CommonChecks(options);

    function CheckDangerLegit() {
        if (SelectedDanger.selectedIndex === 0) {
            EmptyDangerAlertBox.innerHTML = "Scegli il livello di pericolo!";
            return false;
        }
        EmptyDangerAlertBox.innerHTML = "";
        return true;
    }

    function CheckWeatherDetail() {
        if (WeatherDetailInput.selectedIndex === 0) {
            EmptyWeatherDetailAlertBox.innerHTML = "Seleziona un tipo di evento atmosferico o seleziona altro";
            return false;
        }
        EmptyWeatherDetailAlertBox.innerHTML = "";
        return true;
    }

    return {
        checkandsub: function (datevalid, begindate, enddate, CapInputsOrchInst) {
            //updateDuration();
            const CheckDangerLegitRes = CheckDangerLegit();
            const CheckDescriptionLegitRes = CommonChecksInst.CheckDescriptionLegit();

            var CheckWeatherDetailRes = true;
            if (isWeather.checked) {
                CheckWeatherDetailRes = CheckWeatherDetail();
            }
            var CheckEpicentreCapRes = true;
            var CheckMercalliRes = true;
            var CheckRichterRes = true;

            if (isSeismic.checked) {
                CheckEpicentreCapRes = CommonChecksInst.CheckEpicentreCap();
                CheckMercalliRes = CommonChecksInst.CheckMercalli();
                CheckRichterRes = CommonChecksInst.CheckRichter();
            }
            if (!(CheckDangerLegitRes && CheckDescriptionLegitRes && datevalid && CheckWeatherDetailRes && CheckEpicentreCapRes && CheckMercalliRes && CheckRichterRes)) {
                window.scrollTo(0, 0);
                return false;
            }

            BeginTimeToSend.value = begindate.getTime();
            EndTimeToSend.value = enddate.getTime();
            if (CapInputsOrchInst.CreateCapStringBeforeSubmit() === null) {
                alert("Inserisci almeno un CAP");
                return false;
            }

            if (isSeismic && !(EpicentreCapInput.value.trim().length === 0))
                return CapInputsOrchInst.CheckAllCapAreLegitSeismic();
            return CapInputsOrchInst.CheckAllCapAreLegit();
        }
    }
}

//SHOW HIDE elements according to event type
function SwitchOrchestrator(options) {
    const WeatherInputs = options['WeatherInputs'];
    const WeatherInputsGlob = options['WeatherInputsGlob'];
    const SeismicInputs = options['SeismicInputs'];
    const SeismicInputsGlob = options['SeismicInputsGlob'];
    return {
        ShowWeatherOnly: function () {
            for (let i = 0; i < WeatherInputsGlob.length; i++) {
                WeatherInputsGlob[i].style.display = "block";
            }
            for (let i = 0; i < SeismicInputsGlob.length; i++) {
                SeismicInputsGlob[i].style.display = "none";
            }
            for (let i = 0; i < SeismicInputs.length; i++) {
                SeismicInputs[i].setAttribute("disabled", "disabled");
            }
            for (let i = 0; i < WeatherInputs.length; i++) {
                WeatherInputs[i].removeAttribute("disabled");
            }
        },
        ShowSeismicOnly: function () {
            for (let i = 0; i < WeatherInputsGlob.length; i++) {
                WeatherInputsGlob[i].style.display = "none";
                WeatherInputsGlob[i].style.display = "none";
            }
            for (let i = 0; i < SeismicInputsGlob.length; i++) {
                SeismicInputsGlob[i].style.display = "block";
            }
            for (let i = 0; i < WeatherInputs.length; i++) {
                WeatherInputs[i].setAttribute("disabled", "disabled");
            }
            for (let i = 0; i < SeismicInputs.length; i++) {
                SeismicInputs[i].removeAttribute("disabled");
            }
        },
        ShowTerroristOnly: function () {
            for (let i = 0; i < WeatherInputsGlob.length; i++) {
                WeatherInputsGlob[i].style.display = "none";
            }
            for (let i = 0; i < SeismicInputsGlob.length; i++) {
                SeismicInputsGlob[i].style.display = "none";
            }
            for (let i = 0; i < SeismicInputs.length; i++) {
                SeismicInputs[i].setAttribute("disabled", "disabled");
            }
            for (let i = 0; i < WeatherInputs.length; i++) {
                WeatherInputs[i].setAttribute("disabled", "disabled");
            }
        }
    }
}