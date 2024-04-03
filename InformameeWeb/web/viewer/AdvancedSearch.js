document.addEventListener("DOMContentLoaded", function () {
    var PageOrchestratorInst = PageOrchestrator();
    PageOrchestratorInst.start();
});

function DateTime(options) {
    var DateTxtInput = options['DateTxtInput'];
    var BeginDateTxtInput = options['BeginDateTxtInput'];
    var EndDateTxtInput = options['EndDateTxtInput'];
    var TimeTxtInput = options['TimeTxtInput'];
    var BeginTimeTxtInput = options['BeginTimeTxtInput'];
    var EndTimeTxtInput = options['EndTimeTxtInput'];
    var DurationLabel = options['DurationLabel'];
    var begindate = options['begindate'];
    var enddate = options['enddate'];
    var instantdate = options['instantdate'];
    var internaldatevalid = false;
    var DateValidSetter;
    var InstantDateSetter;
    var IntervalDateSetter;

    function updateDuration() {
        var MinutesNotTrunc = (enddate - begindate) / 60000;

        var diffDays = Math.trunc(MinutesNotTrunc / 1440);
        var diffHours = Math.trunc(MinutesNotTrunc / 60 - (diffDays * 24));
        var diffMinutes = Math.ceil(MinutesNotTrunc - ((diffHours + diffDays * 24) * 60));

        if (diffDays < 0 || diffHours < 0 || (diffDays == 0 && diffHours == 0 && diffMinutes <= 0)) {
            var ToShow = "Non valida! La fine deve essere successiva all'inizio.";
            if (internaldatevalid) {
                DurationLabel.style.color = "red";
                internaldatevalid = false;
            }
        } else {
            var ToShow = diffDays + " giorni " + diffHours + "  ore " + diffMinutes + " minuti";
            if (!internaldatevalid) {
                DurationLabel.style.color = "black";
                internaldatevalid = true;
            }
        }
        DateValidSetter(internaldatevalid);
        DurationLabel.innerHTML = ToShow;
    }

    function refreshInstant() {
        DateTxtInput.datepicker("setDate", instantdate);
        TimeTxtInput.timepicker('setTime', instantdate);
    }

    function refreshBegin() {
        BeginDateTxtInput.datepicker("setDate", begindate);
        BeginTimeTxtInput.timepicker('setTime', begindate);
    }

    function refreshEnd() {
        EndDateTxtInput.datepicker("setDate", enddate);
        EndTimeTxtInput.timepicker('setTime', enddate);
    }

    function AddCharListener(inputNode) {
        inputNode.keypress(function (e) {
            var key = e.which;
            var val = e.key;
            if ((key < 48 || key > 57) && !(val === ":" || key === "Backspace")) {
                e.preventDefault();
                e.stopImmediatePropagation();
                //alert("Char: " + String.fromCharCode(e.which));
            }
        });
    }

    return {
        startPickers: function () {
            DateTxtInput.datepicker({
                onSelect: function (dateStr) {
                    var tmpdate = $(this).datepicker('getDate');
                    instantdate.setFullYear(tmpdate.getFullYear());
                    instantdate.setMonth(tmpdate.getMonth());
                    instantdate.setDate(tmpdate.getDate());
                    InstantDateSetter(instantdate);
                }
            });
            BeginDateTxtInput.datepicker({
                onSelect: function (dateStr) {
                    var tmpdate = $(this).datepicker('getDate');
                    begindate.setFullYear(tmpdate.getFullYear());
                    begindate.setMonth(tmpdate.getMonth());
                    begindate.setDate(tmpdate.getDate());
                    updateDuration();
                    IntervalDateSetter(begindate, enddate);
                }
            });

            EndDateTxtInput.datepicker({
                onSelect: function (dateStr) {
                    var tmpdate = $(this).datepicker('getDate');
                    enddate.setFullYear(tmpdate.getFullYear());
                    enddate.setMonth(tmpdate.getMonth());
                    enddate.setDate(tmpdate.getDate());
                    updateDuration();
                    IntervalDateSetter(begindate, enddate);
                }
            });

            TimeTxtInput.timepicker({
                onSelect:
                    function () {
                        var InstantHour = $(this).timepicker('getTimeAsDate');
                        instantdate.setHours(InstantHour.getHours(), InstantHour.getMinutes());
                        InstantDateSetter(instantdate);
                    }
            });
            AddCharListener(TimeTxtInput);

            BeginTimeTxtInput.timepicker({
                onSelect:
                    function () {
                        var BegHour = $(this).timepicker('getTimeAsDate');
                        begindate.setHours(BegHour.getHours(), BegHour.getMinutes());
                        updateDuration();
                        IntervalDateSetter(begindate, enddate);
                    }
            });
            AddCharListener(BeginTimeTxtInput);

            EndTimeTxtInput.timepicker({
                onSelect:
                    function () {
                        var EndHour = $(this).timepicker('getTimeAsDate');
                        enddate.setHours(EndHour.getHours(), EndHour.getMinutes());
                        updateDuration();
                        IntervalDateSetter(begindate, enddate);
                    }
            });
            AddCharListener(EndTimeTxtInput);

            refreshInstant();
            refreshBegin();
            refreshEnd();
        },
        registerDateValidSetter: function (Setter) {
            DateValidSetter = Setter;
        },
        registerInstantDateSetter: function (Setter) {
            InstantDateSetter = Setter;
        }, registerIntervalDateSetter: function (Setter) {
            IntervalDateSetter = Setter;
        }, setInstant: function (Instant) {
            instantdate = Instant;
            refreshInstant();
            InstantDateSetter(Instant);
        }, setInterval: function (Begin, End) {
            begindate = Begin;
            enddate = End;
            refreshBegin();
            refreshEnd();
            updateDuration();
            IntervalDateSetter(Begin, End);
        }
    }
}

function GetStoredItemDescription(StoredSearchToP) {
    var WhenStored = FromTimeToString(StoredSearchToP.CreationTime);
    var Type = "";
    if (StoredSearchToP.isWeather === true)
        Type = (" atmosferici");
    if (StoredSearchToP.isSeismic === true) {
        if (Type.length > 0) {
            Type = Type.concat(",");
        }
        Type = Type.concat(" sismici");
    }
    if (StoredSearchToP.isTerrorist === true) {
        if (Type.length > 0) {
            Type = Type.concat(",");
        }
        Type = Type.concat(" terroristici");
    }
    var Danger = "";
    if (StoredSearchToP.Danger != 0)
        Danger = " con pericolo " + (StoredSearchToP.Danger.toString());
    var Where;
    if (StoredSearchToP.CapList.length === 0)
        Where = " in tutta italia";
    else {
        if (StoredSearchToP.CapList.length === 1) {
            Where = " al CAP " + (StoredSearchToP.CapList[0].toString());
        } else if (StoredSearchToP.CapList.length < 10) {
            Where = " ai CAP";
            for (let i = 0; i < StoredSearchToP.CapList.length; i++) {
                Where = Where.concat(" " + (StoredSearchToP.CapList[i].toString()));
            }
        } else {
            Where = " presso " + (StoredSearchToP.CapList.length.toString()) + " CAP"
        }
    }
    var Time;
    if (StoredSearchToP.isInstant === true) {
        Time = " il " + FromTimeToString(StoredSearchToP.TimeInstant);
    } else {
        Time = " dal " + FromTimeToString(StoredSearchToP.BeginTime) + " al " + FromTimeToString(StoredSearchToP.EndTime);
    }
    return (WhenStored + ": eventi" + Type + Danger + Where + Time);
}

function StoredSearch(options) {
    this.isWeather = options['isWeather'];
    this.isSeismic = options['isSeismic'];
    this.isTerrorist = options['isTerrorist'];
    this.isInstant = options['isInstant'];
    this.TimeInstant = options['TimeInstant'];
    this.BeginTime = options['BeginTime'];
    this.EndTime = options['EndTime'];
    this.CapList = options['CapList'];
    this.Danger = options['Danger'];
    this.WeatherType = options['WeatherType'];
    var Now = new Date();
    this.CreationTime = Now.getTime();
}

function RecentSearch(RecentSearchContainer, CapInputsOrchInst, DateTimeInst, options) {
    var isWeather = options['isWeather'];
    var isSeismic = options['isSeismic'];
    var isTerrorist = options['isTerrorist'];
    var isInstant = options['isInstant'];
    var isInterval = options['isInterval'];
    var Danger = options['Danger'];
    var WeatherType = options['WeatherType'];

    var LocalStorageItemName = "recentsearches";
    var RecentSearchesJson = localStorage.getItem(LocalStorageItemName);
    var RecentSearches;
    var RecentSearchesIsNew;
    var RecentSearchesLenght = 5;
    if (RecentSearchesJson === null) {
        RecentSearches = new Array(RecentSearchesLenght);
        RecentSearchesIsNew = true;
    } else {
        RecentSearches = JSON.parse(RecentSearchesJson);
        RecentSearchesIsNew = false;
    }

    function LoadForm(index) {
        if (index < RecentSearchesLenght && !(RecentSearches[index] === null)) {
            var ClickEv = document.createEvent('HTMLEvents');
            ClickEv.initEvent("click", true, false);

            var CurrSearch = RecentSearches[index];
            /*isWeather.checked=CurrSearch.isWeather;
            isTerrorist.checked=CurrSearch.isTerrorist;
            isSeismic.checked=CurrSearch.isSeismic;*/
            isWeather.checked = !CurrSearch.isWeather;
            isWeather.dispatchEvent(ClickEv);
            isSeismic.checked = CurrSearch.isSeismic;
            isTerrorist.checked = CurrSearch.isTerrorist;
            if (CurrSearch.isInstant === true) isInstant.dispatchEvent(ClickEv);
            else isInterval.dispatchEvent(ClickEv);
            Danger.selectedIndex = CurrSearch.Danger;
            WeatherType.selectedIndex = CurrSearch.WeatherType;
            var CapListArr = CurrSearch.CapList;
            CapInputsOrchInst.ClearAllInputs();
            for (let i = 0; i < CapListArr.length; i++)
                CapInputsOrchInst.WriteCapInInputBox(CapListArr[i]);

            DateTimeInst.setInstant(new Date(CurrSearch.TimeInstant));
            DateTimeInst.setInterval(new Date(CurrSearch.BeginTime), new Date(CurrSearch.EndTime))
        }
    }

    return {
        show: function () {
            if (RecentSearchesIsNew === false) {
                PrintIntoForm(RecentSearchContainer, RecentSearches, LoadForm);
            }
        }, AddLatestSearch: function (SearchToAdd) {
            localStorage.setItem(LocalStorageItemName, AddRecentsArrayJson(RecentSearches, RecentSearchesLenght, SearchToAdd));
        }
    }
}

function Event(options) {
    var isWeather = options['isWeather'];
    var isSeismic = options['isSeismic'];
    var isTerrorist = options['isTerrorist'];
    var isInstant = options['isInstant'];
    var InstantToSend = options['InstantToSend'];
    var BeginTimeToSend = options['BeginTimeToSend'];
    var EndTimeToSend = options['EndTimeToSend'];
    var EmptyTypeAlertBox = options['EmptyTypeAlertBox'];
    var Cap0Input = options['Cap0Input'];
    var Danger = options['Danger'];
    var WeatherType = options['WeatherType'];
    return {
        check: function (datevalid, CapInputsOrchInst) {
            function CheckOneAtLeast() {
                if (isWeather.checked || isSeismic.checked || isTerrorist.checked) {
                    EmptyTypeAlertBox.innerHTML = "";
                    return true;
                } else {
                    EmptyTypeAlertBox.innerHTML = "Scegli almeno un tipo di evento!";
                    return false;
                }
            }

            if (!(CheckOneAtLeast() && (datevalid || isInstant))) {
                window.scrollTo(0, 0);
                return false;
            }
            if (!(Cap0Input.value.trim().length === 0 && CapInputsOrchInst.getCapCounter() === 0) && !CapInputsOrchInst.CheckAllCapAreLegit()) {
                window.scrollTo(0, 0);
                return false;
            }
            return true;
        },
        submitandstore: function (instantdate, begindate, enddate, RecentSearchInst, CapInputsOrchInst) {
            if (isInstant.checked) {
                InstantToSend.value = instantdate.getTime();
            } else {
                BeginTimeToSend.value = begindate.getTime();
                EndTimeToSend.value = enddate.getTime();
            }
            var CapListArr = CapInputsOrchInst.CreateCapStringBeforeSubmit();
            if (CapListArr === null)
                CapListArr = [];
            var ToSave = new StoredSearch({
                isWeather: isWeather.checked,
                isSeismic: isSeismic.checked,
                isTerrorist: isTerrorist.checked,
                isInstant: isInstant.checked,
                TimeInstant: instantdate.getTime(),
                BeginTime: begindate.getTime(),
                EndTime: enddate.getTime(),
                CapList: CapListArr,
                Danger: Danger.selectedIndex,
                WeatherType: WeatherType.selectedIndex
            });
                RecentSearchInst.AddLatestSearch(ToSave);
        },
        registerListeners: function () {

        }
    }
}

function PageOrchestrator() {
    var DateTimeInstance = DateTime({
        DateTxtInput: $("#datetxt"),
        BeginDateTxtInput: $("#begindatetxt"),
        EndDateTxtInput: $("#enddatetxt"),
        TimeTxtInput: $('#timetxt'),
        BeginTimeTxtInput: $('#begintimetxt'),
        EndTimeTxtInput: $('#endtimetxt'),
        DurationLabel: document.getElementById("durationDisp"),
        begindate: new Date(),
        enddate: new Date(),
        instantdate: new Date()
    });

    var isInstant = document.getElementById("instantradio");
    var isInterval = document.getElementById("intervalradio");

    var isWeather = document.getElementById("isWeather");
    var isSeismic = document.getElementById("isSeismic");
    var isTerrorist = document.getElementById("isTerrorist");
    var Danger = document.getElementById("danger");
    var WeatherType = document.getElementById("weatherdetail");

    var Form = document.getElementById("ToSubmit");
    var EventInst = Event({
        isWeather: isWeather,
        isSeismic: isSeismic,
        isTerrorist: isTerrorist,
        EmptyTypeAlertBox: document.getElementById("emptytypealert"),
        isInstant: document.getElementById("instantradio"),
        InstantToSend: document.getElementById("instant"),
        BeginTimeToSend: document.getElementById("begin"),
        EndTimeToSend: document.getElementById("end"),
        Cap0Input: document.getElementById("cap0"),
        Danger: Danger,
        WeatherType: WeatherType
    });

    var SwitchOrchestratorInst = SwitchOrchestrator(
        {
            WeatherInputsGlob: document.getElementsByClassName("weatherinputsglob"),
            WeatherInputs: document.getElementsByClassName("weatherinputs"),
            IntervalGeneric: document.getElementsByClassName("intervalgeneric"),
            InstantGeneric: document.getElementsByClassName("instantgeneric"),
            IntervalInput: document.getElementsByClassName("intervalinput"),
            InstantInput: document.getElementsByClassName("instantinput"),
            isInstant: isInstant,
            isInterval: isInterval
        }
    );

    var DateValid;
    var instantdate;
    var begindate;
    var enddate;

    var CapInputsOrchInst = CapInputsOrchestrator(
        document.getElementById("capinputcontainer"),
        Form
    );

    var RecentSearchesInst = RecentSearch(
        document.getElementById("recentscontainer"), CapInputsOrchInst, DateTimeInstance, {
            isInterval: isInterval,
            isInstant: isInstant,
            isWeather: isWeather,
            isSeismic: isSeismic,
            isTerrorist: isTerrorist,
            Danger: Danger,
            WeatherType: WeatherType
        }
    );

    var CapBtn = document.getElementById("capbtn");
    var RemoveAllBtn = document.getElementById("removeall");

    var MapReverseInst = MapReverse(
        document.getElementById("map"),
        CapInputsOrchInst.WriteCapInInputBox
    );

    function registerListeners() {
        isInstant.addEventListener("click", SwitchOrchestratorInst.ShowInstant);
        isInterval.addEventListener("click", SwitchOrchestratorInst.ShowInterval);
        isWeather.addEventListener("click", function () {
            if (isWeather.checked)
                SwitchOrchestratorInst.ShowWeather();
            else
                SwitchOrchestratorInst.HideWeather();
        });
        CapBtn.addEventListener("click", CapInputsOrchInst.AddCapInput);
        Form.addEventListener("submit", function (event) {
            if (EventInst.check(DateValid, CapInputsOrchInst) === false) {
                event.stopPropagation();
                event.preventDefault();
            } else {
                EventInst.submitandstore(instantdate, begindate, enddate, RecentSearchesInst, CapInputsOrchInst);
            }
        });
        RemoveAllBtn.addEventListener("click", CapInputsOrchInst.ClearAllInputs);
    }

    return {
        start: function () {
            RecentSearchesInst.show();
            DateTimeInstance.registerDateValidSetter(function (Valid) {
                DateValid = Valid;
            });
            DateTimeInstance.registerInstantDateSetter(function (InstantDate) {
                instantdate = InstantDate;
            });
            DateTimeInstance.registerIntervalDateSetter(function (BeginDate, EndDate) {
                begindate = BeginDate;
                enddate = EndDate;
            });
            DateTimeInstance.startPickers();
            registerListeners();
            CapInputsOrchInst.AddCapListener(document.getElementById("cap0"));
            MapReverseInst.Load();
        }
    }
}

function SwitchOrchestrator(options) {
    var WeatherInputsGlob = options['WeatherInputsGlob'];
    var WeatherInputs = options['WeatherInputs'];
    var IntervalGeneric = options['IntervalGeneric'];
    var InstantGeneric = options['InstantGeneric'];
    var IntervalInput = options['IntervalInput'];
    var InstantInput = options['InstantInput'];
    //var isInstant = options['isInstant'];
    //var isInterval = options['isInterval'];

    function showElementsAndInputs(GlobalArray, InputArray, Mode) {
        for (let i = 0; i < GlobalArray.length; i++) {
            GlobalArray[i].style.display = Mode;
        }
        for (let i = 0; i < InputArray.length; i++) {
            InputArray[i].removeAttribute("disabled");
        }
    }

    function hideElementsAndInputs(GenericArray, InputArray) {
        for (let i = 0; i < InputArray.length; i++) {
            InputArray[i].setAttribute("disabled", "disabled");
        }
        for (let i = 0; i < GenericArray.length; i++) {
            GenericArray[i].style.display = "none";
        }
    }

    return {
        ShowWeather: function () {
            showElementsAndInputs(WeatherInputsGlob, WeatherInputs, "inline");
        },
        HideWeather: function () {
            hideElementsAndInputs(WeatherInputsGlob, WeatherInputs);
        },
        ShowInterval: function () {
            showElementsAndInputs(IntervalGeneric, IntervalInput, "block");
            hideElementsAndInputs(InstantGeneric, InstantInput);
        },
        ShowInstant: function () {
            showElementsAndInputs(InstantGeneric, InstantInput, "block");
            hideElementsAndInputs(IntervalGeneric, IntervalInput);
        }
    }
}