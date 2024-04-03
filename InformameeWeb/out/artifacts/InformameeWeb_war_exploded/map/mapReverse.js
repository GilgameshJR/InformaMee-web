var ServletContextPath = document.getElementById('ServletContextPath').getAttribute('data-servpath');

function MapReverse(MapNode, CapSetterCbk) {
    var map = null;
    var StartingPoint = new Array(2);
    var StartingLatLng;
    var ctx;
    var canvas;
    function ClearCanvas() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
    }
    function DrawRectangle(NewPoints) {
        var myy = Math.min(NewPoints[1], StartingPoint[1]);
        var myx = Math.min(NewPoints[0], StartingPoint[0]);
        var myHeight = Math.abs(NewPoints[1] - StartingPoint[1]);
        var myWidth = Math.abs(NewPoints[0] - StartingPoint[0]);
        ctx.beginPath();
        ctx.lineWidth = "5";
        ctx.strokeStyle = "red";
        ctx.rect(myx, myy, myWidth, myHeight);
        ctx.stroke();
    }
    var drag = false;
    var DrawDelegate = {
        onDrawLayer: function (info) {
            ctx = info.canvas.getContext('2d');
            canvas = info.canvas;
            if (drag === true) {
                var newPoint = L.point(StartingPoint);
                StartingLatLng = info.layer._map.containerPointToLatLng(newPoint);
            }
        }
    };
    function FetchCapsByUrl(url) {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", url, true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var json = JSON.parse(xhr.responseText);
                if (json === null)
                    alert("Nessun CAP trovato");
                else
                    for (let i = 0; i < json.length; i++)
                        CapSetterCbk(json[i]);
            }
        };
        xhr.send();
        /*
        fetch(url)
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                //console.log(data);
                if (data === null)
                    alert("Nessun CAP trovato");
                else
                    for (let i = 0; i < data.length; i++)
                        CapSetterCbk(data[i]);
            });*/
    }
    function FetchCapsBounds(MinLat, MinLng, MaxLat, MaxLng) {
        var url = ServletContextPath + "/reversebounds?minlat=" + (MinLat.toString()) + "&minlng=" + (MinLng.toString()) + "&maxlat=" + (MaxLat.toString()) + "&maxlng=" + (MaxLng.toString());
        FetchCapsByUrl(url);
    }

    function FetchCapsPoint(Lat, Lng) {
        var url = ServletContextPath + "/reversepoint?lat=" + (Lat.toString()) + "&lng=" + (Lng.toString());
        FetchCapsByUrl(url);
    }
    /*
    function FetchCap(Coordinates) {
        fetch(("https://nominatim.openstreetmap.org/reverse?format=json&lat=" + (Coordinates.lat).toString() + "&lon=" + (Coordinates.lng).toString()))
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                //console.log(data);
                if (data != null && data.hasOwnProperty("address") && data.address.hasOwnProperty("postcode"))
                    CapSetterCbk(data.address.postcode);
            });
    }*/
    function CreateBounds(EndingLatLng) {
        var MinLat = Math.min(StartingLatLng.lat, EndingLatLng.lat);
        var MaxLat = Math.max(StartingLatLng.lat, EndingLatLng.lat);
        var MinLng = Math.min(StartingLatLng.lng, EndingLatLng.lng);
        var MaxLng = Math.max(StartingLatLng.lng, EndingLatLng.lng);
        FetchCapsBounds(MinLat, MinLng, MaxLat, MaxLng);
    }

    return {
        Load: function () {
            if (map === null) {
                MapNode.style.display = "inline";
                map = new L.Map('map', {zoomControl: true});
                map.clicked = 0;
                map.on("click", function (event) {
                    var coordinates = event.latlng;
                    FetchCapsPoint(coordinates.lat, coordinates.lng);
                });
                L.DomEvent.on(MapNode, 'contextmenu', function (ev) {
                    L.DomEvent.stop(ev);
                });
                map.on("mousedown", function (event) {
                    if (event.originalEvent.button === 2 && drag === false) {
                        StartingPoint[0] = event.containerPoint.x;
                        StartingPoint[1] = event.containerPoint.y;
                        StartingLatLng = event.latlng;
                        drag = true;
                        event.originalEvent.stopPropagation();
                    }
                });
                map.on("mousemove", function (event) {
                    if (drag === true) {
                        ClearCanvas();
                        DrawRectangle([event.containerPoint.x, event.containerPoint.y])
                    }
                });
                map.on("mouseup", function (event) {
                    if (event.originalEvent.button === 2 && drag == true) {
                        ClearCanvas();
                        drag = false;
                        CreateBounds(event.latlng);
                        event.originalEvent.stopPropagation();
                    }
                });
                var osmUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',

                    osmAttribution = 'Map data &copy; 2012 <a href="http://openstreetmap.org">OpenStreetMap</a> contributors',

                    osm = new L.TileLayer(osmUrl, {maxZoom: 18, attribution: osmAttribution});
                var bottomleft = new L.LatLng(47, 18.3);
                var topright = new L.LatLng(35.3, 6.4);
                map.fitBounds(new L.LatLngBounds(bottomleft, topright)).addLayer(osm);
                L.canvasLayer()
                    .delegate(DrawDelegate)
                    .addTo(map);
            }
        }
    }
}