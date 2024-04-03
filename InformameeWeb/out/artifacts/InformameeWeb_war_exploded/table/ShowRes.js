document.addEventListener("DOMContentLoaded", function () {
    var PageOrchestratorInst = PageOrchestrator();
    PageOrchestratorInst.start();
});

function PageOrchestrator() {
    var MapLookupInst= MapLookup(document.getElementById("map"));
    var CapTd=document.getElementsByClassName("captd");

    function RegisterListeners() {
        for (let i=0; i<CapTd.length; i++) {
            CapTd[i].addEventListener("click", function () {
                MapLookupInst.Search(this.getAttribute("data-capjson"));
            })
        }
    }

    return {
        start: function () {
            RegisterListeners();
        }
    }
}