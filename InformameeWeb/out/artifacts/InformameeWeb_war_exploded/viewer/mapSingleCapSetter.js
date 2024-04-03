var ToCheckResultBox = document.getElementById("capvalid");
var ToWriteCapIn = document.getElementById("cap");

function WriteCapInInputBox(CapToWrite) {
    CheckCapAndPrintStatus(CapToWrite, ToCheckResultBox);
    ToWriteCapIn.value = CapToWrite;
}