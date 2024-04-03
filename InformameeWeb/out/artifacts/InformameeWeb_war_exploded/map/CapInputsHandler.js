var ServletContextPath = document.getElementById('ServletContextPath').getAttribute('data-servpath');

function CheckCapAndPrintStatus(Cap, ToWriteIn) {
    ToWriteIn.innerHTML = "";
    var CapTrim = Cap.toString().trim();
    if (CapTrim.length > 0) {
        let xhttp = new XMLHttpRequest();
        let id = ToWriteIn.id;
        let ih = ToWriteIn.innerHTML;
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                ToWriteIn.innerHTML = this.responseText;
            }
        };
        xhttp.open("GET", (ServletContextPath + "/capcheck?cap=" + (CapTrim)), true);
        xhttp.send();
    }
}

function CheckAllCapAreLegit(capcounter) {
    var CapCheckResults = document.getElementsByName("spanwithdata");

    if (((CapCheckResults.length) - 1) < capcounter) {
        alert("Attendi la verifica di tutti i CAP");
        return false;
    }

    for (let i = 0; i < CapCheckResults.length; i++) {
        if (CapCheckResults[i].getAttribute("data-valid") === "false") {
            alert("Hai inserito dei CAP inestistenti, correggi e riprova");
            return false;
        }
    }
    return true;
}

//CAP input box add or remove
function CapInputsOrchestrator(CapInputContainer, Form) {
    if (CapInputContainer) {
        var capcounter = 0;
        var LastNodeCapPar = document.getElementById("cappar0");
        var MoreCapContainer = CapInputContainer.firstElementChild.nextElementSibling;
    }
    const TimeoutInMs = 1000;
    function RemoveCapInput(IndexStr) {
        var Index = parseInt(IndexStr);
        if (Index <= capcounter) {
            var CurrCapPar = document.getElementById("cappar" + (Index.toString()));
            for (let i = Index; i < capcounter; i++) {
                let CurrInput = CurrCapPar.firstElementChild;
                let CurrResultLabel = CurrInput.nextElementSibling;
                let NextCapPar = CurrCapPar.nextElementSibling;
                let NextInput = NextCapPar.firstElementChild;
                let NextResultLabel = NextInput.nextElementSibling;
                /*
            let CurrInput = document.getElementById("cap" + (i.toString()));
            let CurrResultLabel = document.getElementById("capvalid" + (i.toString()));
            let NextInput = document.getElementById("cap" + ((i + 1).toString()));
            let NextResultLabel = document.getElementById(("capvalid" + ((i + 1).toString())));
*/
                CurrInput.value = NextInput.value;
                CurrResultLabel.innerHTML = NextResultLabel.innerHTML;

                CurrCapPar = NextCapPar;
            }
            var LastCapParBak = LastNodeCapPar;
            if (capcounter === 1)
                LastNodeCapPar = document.getElementById("cappar0");
            else
                LastNodeCapPar = LastNodeCapPar.previousElementSibling;
            MoreCapContainer.removeChild(LastCapParBak);
            capcounter--;
        }
    }

    function AddCapListener(CapInput, CapValid) {
        function CheckCap() {
            CheckCapAndPrintStatus(CapInput.value, CapValid)
        }

        var Timeout;
        CapInput.addEventListener("keyup", function () {
            CapValid.innerHTML = "";
            if (Timeout) clearTimeout(Timeout);
            Timeout = setTimeout(CheckCap, TimeoutInMs)
        });
        CapInput.addEventListener("change", function () {
            if (CapValid.innerHTML.length === 0) {
                clearTimeout(Timeout);
                CheckCap();
            }
        });
    }

    function AddCapInput() {
        capcounter++;
        //CapCounterPageOrchSetter(capcounter);

        var par = document.createElement("p");
        par.setAttribute("id", "cappar" + capcounter);

        var CapInput = document.createElement("input");

        CapInput.setAttribute("id", "cap" + capcounter);
        CapInput.setAttribute("type", "text");
        var CapValid = document.createElement("span");
        CapValid.setAttribute("id", "capvalid" + capcounter);

        var RemoveButton = document.createElement("button");
        RemoveButton.setAttribute("id", "caprembtn" + capcounter);
        const capctrconst = capcounter;
        RemoveButton.addEventListener("click", function () {
            RemoveCapInput(capctrconst);
        });
        RemoveButton.setAttribute("type", "button");
        RemoveButton.innerHTML = "-";
        /*
        function CheckCap() {
            CheckCapAndPrintStatus(CapInput.value, CapValid)
        }

        var Timeout;
        CapInput.addEventListener("keyup", function () {
            CapValid.innerHTML = "";
            if (Timeout) clearTimeout(Timeout);
            Timeout = setTimeout(CheckCap, TimeoutInMs);
        });
        CapInput.addEventListener("change", function () {
            if (CapValid.innerHTML.length === 0) {
                clearTimeout(Timeout);
                CheckCap();
            }
        });*/
        par.appendChild(CapInput);
        par.appendChild(RemoveButton);
        par.appendChild(CapValid);
        MoreCapContainer.appendChild(par);
        LastNodeCapPar = par;
        AddCapListener(CapInput, CapValid);
    }

    return {
        AddCapInput: AddCapInput,
        /*registerCapCounterSetter: function (Setter) {
            CapCounterPageOrchSetter = Setter;
        },*/
        CheckAllCapAreLegit: function () {
            return CheckAllCapAreLegit(capcounter);
        },
        CheckAllCapAreLegitSeismic: function () {
            return CheckAllCapAreLegit(capcounter + 1);
        }, ClearAllInputs: function () {
            capcounter = 0;
            MoreCapContainer.innerHTML = "";
            var CapPar0 = document.getElementById("cappar0");
            LastNodeCapPar = CapPar0;
            var Input = CapPar0.firstElementChild;
            var CapValid = Input.nextElementSibling;
            Input.value = "";
            CapValid.innerHTML = "";
        },
        CreateCapStringBeforeSubmit: function () {
            var CapListToSend = document.getElementById("caplist");
            var CapListArr = new Array();

            var cappar0 = CapInputContainer.firstElementChild;
            var CapListStr = cappar0.firstElementChild.value.trim();
            if (CapListStr.length > 0) {
                CapListArr.push(CapListStr);
            }
            if (capcounter > 0) {
                var currCapPar = MoreCapContainer.firstElementChild;
                let shouldcontinue = true;
                let i = 1;
                while (shouldcontinue) {
                    let TmpStr = currCapPar.firstElementChild.value.trim();
                    if (TmpStr.length > 0) {
                        CapListStr += " " + TmpStr;
                        CapListArr.push(TmpStr);
                    }
                    i++;
                    if (i <= capcounter) {
                        currCapPar = currCapPar.nextElementSibling;
                    } else {
                        shouldcontinue = false;
                    }
                }
                /*
                for (let i = 0; i <= capcounter; i++) {
                    let TmpStr = document.getElementById("cap" + i).value.trim();
                    if (TmpStr.length > 0) {
                        CapListStr += TmpStr + " ";
                        CapListArr.push(TmpStr);
                    }
                }*/
            }
            if (CapListStr.trim().length === 0) {
                return null;
            }
            CapListToSend.value = CapListStr;
            if (CapListStr.length > 1800)
                Form.setAttribute("method", "post");
            return CapListArr;
        }, getCapCounter: function () {
            return capcounter;
        }, RegisterExistingInputs: function (CapCounterToSet) {
            if (CapCounterToSet > 0) {
                capcounter = CapCounterToSet;
                var CurrNode = document.getElementById("cappar1");

                var shouldcontinue = true;
                var i = 1;
                while (shouldcontinue) {
                    const capctrconst = i;
                    CurrNode.firstElementChild.nextElementSibling.addEventListener("click", function () {
                        RemoveCapInput(capctrconst);
                    });
                    i++;
                    if (i <= CapCounterToSet)
                        CurrNode = CurrNode.nextElementSibling;
                    else shouldcontinue = false;
                }
                LastNodeCapPar = CurrNode;
            }
        }, WriteCapInInputBox: function (CapToWrite) {
            const ToCheck = LastNodeCapPar.firstElementChild;
            var ToWriteIn;
            if (ToCheck.value.length == 0) {
                ToWriteIn = ToCheck;
            } else {
                AddCapInput();
                ToWriteIn = LastNodeCapPar.firstElementChild;
            }
            ToWriteIn.value = CapToWrite.toString();

            /*  needed for check
            if (capcounter === 0)
                    CheckCapAndPrintStatus(CapToWrite, ToWriteIn.nextElementSibling);
                else
                    CheckCapAndPrintStatus(CapToWrite, ToWriteIn.nextElementSibling.nextElementSibling);*/
            //mark as checked
            var NodeToWriteValidInto;
            if (capcounter === 0)
                NodeToWriteValidInto = ToWriteIn.nextElementSibling;
            else
                NodeToWriteValidInto = ToWriteIn.nextElementSibling.nextElementSibling;
            NodeToWriteValidInto.innerHTML = "<span style=\"color: green\" data-valid=\"true\" name=\"spanwithdata\"><img alt=\"OK\" height=\"18\" src=\"" + ServletContextPath + "/ok.jpg\" width=\"18\"></span>";
        }, AddCapListener: function (CapInput) {
            AddCapListener(CapInput, CapInput.nextElementSibling);
        }
    }
}

function WriteCapInSingleBox(ResultBoxToCheck, ToWriteCapIn) {
    return function (CapToWrite) {
        CheckCapAndPrintStatus(CapToWrite, ResultBoxToCheck);
        ToWriteCapIn.value = CapToWrite;
    }
}

