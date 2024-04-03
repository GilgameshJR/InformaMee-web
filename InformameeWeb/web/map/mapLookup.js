var ServletContextPath = document.getElementById('ServletContextPath').getAttribute('data-servpath');

function MapLookup(MapNode) {
    var map = null;
    var markerArr = new Array();

    function Cbk(data) {
        if (data != null && data.length > 0) {
            var lowestlat = 10000;
            var highestlat = -10000;
            var lowestlon = 10000;
            var highestlon = -10000;
            for (let i = 0; i < data.length; i++) {
                var currcapdata = data[i];
                var minlat = currcapdata[0];
                var minlng = currcapdata[1];
                var maxlat = currcapdata[2];
                var maxlng = currcapdata[3];
                if (minlat < lowestlat) lowestlat = minlat;
                if (maxlat > highestlat) highestlat = maxlat;
                if (minlng < lowestlon) lowestlon = minlng;
                if (maxlng > highestlon) highestlon = maxlng;
                var markerlat = ((minlat + maxlat) / 2);
                var markerlng = ((minlng + maxlng) / 2);
                let marker = new L.Marker(new L.LatLng(markerlat, markerlng));
                marker.addTo(map);
                markerArr.push(marker);
            }
            let lowerleft = new L.LatLng(lowestlat, lowestlon);
            let upperright = new L.LatLng(highestlat, highestlon);
            let bounds = new L.LatLngBounds(lowerleft, upperright);
            map.flyToBounds(bounds, {maxZoom: 13});
        }
    }

    return {
        Load: function () {
            if (map === null) {
                MapNode.style.display = "block";
                map = new L.Map('map', {zoomControl: true});

                var osmUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',

                    osmAttribution = 'Map data &copy; 2012 <a href="http://openstreetmap.org">OpenStreetMap</a> contributors',

                    osm = new L.TileLayer(osmUrl, {maxZoom: 18, attribution: osmAttribution});

                var bottomleft = new L.LatLng(47, 18.3);
                var topright = new L.LatLng(35.3, 6.4);
                map.fitBounds(new L.LatLngBounds(bottomleft, topright)).addLayer(osm);
            }
        }, Search: function (CapListJson) {
            if (map === null)
                this.Load();
            window.location.hash = "#map";
            for (let i = 0; i < markerArr.length; i++) {
                map.removeLayer(markerArr[i]);
            }
            markerArr = new Array();
            var url = ServletContextPath + "/geolocate";

            var xhr = new XMLHttpRequest();
            xhr.open("POST", url, true);
            xhr.setRequestHeader("Content-Type", "application/json");
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var json = JSON.parse(xhr.responseText);
                    Cbk(json);
                }
            };
            xhr.send(CapListJson);
            /*
                        fetch(url,
                            {
                                method: "POST",
                                body: CapListJson
                            }).then((response) => {
                                var ObjReceived=response.json();
                                Cbk(ObjReceived);
                        });*/
        }
    }
}