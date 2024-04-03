document.addEventListener("DOMContentLoaded", function () {
    var PageOrchestratorInst = PageOrchestrator();
    PageOrchestratorInst.start();
});

function PageOrchestrator() {
    var CapInput = document.getElementById('cap');
    var Form = document.getElementById("ToSubmit");
    function CheckCapFilled() {
        if (CapInput.value.trim().length == 0) {
            alert("Inserisci un CAP");
            return false;
        }
        return true;
    }
    var MapReverseInst = MapReverse(
        document.getElementById("map"),
        WriteCapInSingleBox(
            document.getElementById("capvalid"),
            document.getElementById("cap"))
    );
    function registerListeners() {
        Form.addEventListener("submit", function (event) {
            if (CheckCapFilled() === false || CheckAllCapAreLegit(0) === false) {
                event.stopPropagation();
                event.preventDefault();
            }
        });
        CapInputsOrchestrator().AddCapListener(CapInput);
    }
    return {
        start: function () {
            registerListeners();
            MapReverseInst.Load();
        }
    }
}