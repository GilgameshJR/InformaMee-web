function PrintIntoForm(RecentSearchContainer, RecentSearches, LoadForm) {
    var Label = document.createElement("h3");
    Label.innerHTML = "Ricerche recenti";
    RecentSearchContainer.appendChild(Label);
    for (let i = 0; i < RecentSearches.length; i++) {
        if (!(RecentSearches[i] === null)) {
            const CurrSearch = document.createElement("p");
            const Anchor = document.createElement("a");
            //Anchor.setAttribute("href", RecentSearches[i].url);
            Anchor.setAttribute("class", "recentsearch")
            Anchor.innerHTML = GetStoredItemDescription(RecentSearches[i]);
            const curri = i;
            Anchor.addEventListener("click", function () {
                LoadForm(curri);
            });
            CurrSearch.appendChild(Anchor);
            RecentSearchContainer.appendChild(CurrSearch);
            //const CurrDate=new Date(RecentSearches[i].CreationTime);
            //Anchor.innerHTML=(CurrDate.getDate().toString())+"-"+((CurrDate.getMonth()+1).toString())
        }
    }
}

function AddRecentsArrayJson(RecentSearches, RecentSearchesLenght, SearchToAdd) {
    for (let i = RecentSearchesLenght - 1; i > 0; i--) {
        if (!(RecentSearches[i - 1] === null))
            RecentSearches[i] = RecentSearches[i - 1];
    }
    RecentSearches[0] = SearchToAdd;
    return JSON.stringify(RecentSearches);
}

function FromTimeToString(Time) {
    var TmpDate = new Date(Time);
    var Minutes = TmpDate.getMinutes();
    var MinutesStr = Minutes.toString();
    if (Minutes < 10)
        MinutesStr = "0" + MinutesStr;
    return ((TmpDate.getDate().toString()) + "-" + ((TmpDate.getMonth() + 1).toString()) + "-" + ((TmpDate.getFullYear()).toString()) + " alle ore " + (TmpDate.getHours().toString()) + ":" + MinutesStr);
}