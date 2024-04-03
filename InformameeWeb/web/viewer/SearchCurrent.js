document.addEventListener("DOMContentLoaded", function () {
    var PageOrchestratorInst = PageOrchestrator();
    PageOrchestratorInst.start();
});

function StoredSearch(CapListToSet) {
    this.CapList = CapListToSet;
    var Now = new Date();
    this.CreationTime = Now.getTime();
}

function GetStoredItemDescription(StoredSearchToPr) {
    var WhenStored = FromTimeToString(StoredSearchToPr.CreationTime);
    var Where;
    if (StoredSearchToPr.CapList.length === 1) {
        Where = " al CAP " + (StoredSearchToPr.CapList[0].toString());
    } else if (StoredSearchToPr.CapList.length < 10) {
        Where = " ai CAP";
        for (let i = 0; i < StoredSearchToPr.CapList.length; i++) {
            Where = Where.concat(" " + (StoredSearchToPr.CapList[i].toString()));
        }
    } else {
        Where = " presso " + (StoredSearchToPr.CapList.length.toString()) + " CAP"
    }
    return (WhenStored + ": eventi in corso" + Where);
}

function RecentSearch(RecentSearchContainer, CapInputsOrchInst) {
    var LocalStorageItemName = "recentsearchescurr";
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
            var CurrSearch = RecentSearches[index];
            var CapListArr = CurrSearch.CapList;
            CapInputsOrchInst.ClearAllInputs();
            for (let i = 0; i < CapListArr.length; i++)
                CapInputsOrchInst.WriteCapInInputBox(CapListArr[i]);
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

function Event(CapInputsOrchInst, RecentSearchInst) {
    return {
        submit: function () {
            var CapListArr = CapInputsOrchInst.CreateCapStringBeforeSubmit();
            if (CapListArr === null) {
                alert("Inserisci almeno un CAP");
                return false;
            }
            if (!CapInputsOrchInst.CheckAllCapAreLegit())
                return false;
            var NewSearch = new StoredSearch(CapListArr);
            RecentSearchInst.AddLatestSearch(NewSearch);
            return true;
        }
    }
}

function PageOrchestrator() {
    var Form = document.getElementById("ToSubmit");
    var CapInputsOrchInst = CapInputsOrchestrator(
        document.getElementById("capinputcontainer"),
        Form
    );
    var MapReverseInst = MapReverse(
        document.getElementById("map"),
        CapInputsOrchInst.WriteCapInInputBox);
    var CapBtn = document.getElementById("capbtn");
    var RemoveAllBtn = document.getElementById("removeall");
    var RecentSearchInst = RecentSearch(document.getElementById("recentscontainer"), CapInputsOrchInst);
    var EventInst = Event(CapInputsOrchInst, RecentSearchInst);


    function registerListeners() {
        CapBtn.addEventListener("click", CapInputsOrchInst.AddCapInput);
        Form.addEventListener("submit", function (event) {
            if (EventInst.submit() === false) {
                event.stopPropagation();
                event.preventDefault();
            }
        });
        RemoveAllBtn.addEventListener("click", CapInputsOrchInst.ClearAllInputs);
    }

    return {
        start: function () {
            registerListeners();
            MapReverseInst.Load();
            RecentSearchInst.show();
            CapInputsOrchInst.AddCapListener(document.getElementById("cap0"));
        }
    }
}