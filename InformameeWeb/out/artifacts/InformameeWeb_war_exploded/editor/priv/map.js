var map=null;
//var totcap=0;
//var fetchctr=0;
//var markerArr=new Array();

//var timer = 0;
//var delay = 200;
//var prevent = false;


function FetchCap(Coordinates) {
    fetch( ("https://nominatim.openstreetmap.org/reverse?format=json&lat="+(Coordinates.lat).toString()+"&lon="+(Coordinates.lng).toString()) )
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            console.log(data);
            if(data != null && data.hasOwnProperty("address") && data.address.hasOwnProperty("postcode"))
                WriteCapInInputBox(data.address.postcode);
        });
}


function WriteCapInInput(CapToWrite) {

}

function LoadMap() {

    map = new L.Map('map', {zoomControl: true});
    map.clicked=0;
    map.on("click", function(event) {
        /*timer = setTimeout(function() {
            if (!prevent) {
                doClickAction();
            }
            prevent = false;
        }, delay);*/
        var SelectedCap=FetchCap(event.latlng);
        //WriteCapInInput(SelectedCap);
    })
    /*
    map.on("dblclick", function() {
            clearTimeout(timer);
            prevent = true;
            doDoubleClickAction();
        });*/

    var osmUrl = 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',

        osmAttribution = 'Map data &copy; 2012 <a href="http://openstreetmap.org">OpenStreetMap</a> contributors',

        osm = new L.TileLayer(osmUrl, {maxZoom: 18, attribution: osmAttribution});


    map.setView(new L.LatLng(42.6384261, 12.674297), 6).addLayer(osm);
}


/*
function CapSearch(CapListString) {
    if (map===null) {
        LoadMap();
    }

    var CapList = new Array();
    CapList =  CapListString.toString().trim().split(" ");

    var lowestlat=10000;
    var highestlat=-10000;
    var lowestlon=10000;
    var highestlon=-10000;

    for (var i=0; i<markerArr.length; i++) {
        map.removeLayer(markerArr[i]);
    }

    totcap=CapList.length;
    fetchctr=0;
    for (var i=0; i<CapList.length; i++) {
        var Cap=CapList[i];
            fetch(("https://nominatim.openstreetmap.org/search?format=json&limit=1&q=" + Cap + " italy"))
                .then((response) => {
                    return response.json();
                })
                .then((data) => {
                    fetchctr++;
                    console.log(data);
                    var myres = data[0];
                    var bb=myres.boundingbox;
                    console.log(bb);

                    if (bb[0]<lowestlat) {
                        lowestlat=bb[0];
                    }
                    if (bb[1]>highestlat) {
                        highestlat=bb[1];
                    }
                    if (bb[2]<lowestlon) {
                        lowestlon=bb[2];
                    }
                    if (bb[3]>highestlon) {
                        highestlon=bb[3];
                    }

                    var marketlatlong=new L.latLng(myres.lat, myres.lon);
                    var marker = new L.marker(marketlatlong);
                    marker.addTo(map);
                    markerArr.push(marker);
                    if (fetchctr===totcap) {
                        var lowerleft = new L.LatLng(lowestlat, lowestlon);
                        var upperright = new L.LatLng(highestlat, highestlon);
                        var bounds = new L.latLngBounds(lowerleft, upperright);
                        map.flyToBounds(bounds, { maxZoom: 13 });
                    }
                });
    }
}
*/
window.onload = LoadMap;