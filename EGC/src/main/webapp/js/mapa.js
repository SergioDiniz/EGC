var geocoder;
var map;
var marker;

function initialize() {
    var latlng = new google.maps.LatLng(-6.7227885, -38.641765599999985);
    var options = {
        zoom: 16,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById("mapa"), options);

    geocoder = new google.maps.Geocoder();

    marker = new google.maps.Marker({
        map: map,
        draggable: true,
    });

    marker.setPosition(latlng);
}


//if (navigator.geolocation) {
//   navigator.geolocation.getCurrentPosition(function (position)
//      { var ponto = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
//      map.setCenter(ponto);
//      map.setZoom(16);
//   });
//}

function  posicaoAtual() {
    // verifica se o navegador tem suporte a geolocalização
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) { // callback de sucesso
            // ajusta a posição do marker para a localização do usuário
            marker.setPosition(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));
            
           
            carregarNoMapa($(this).val());

            
        },
                function (error) { // callback de erro
                    alert('Erro ao obter localização!');
                    console.log('Erro ao obter localização.', error);
                });
    } else {
        console.log('Navegador não suporta Geolocalização!');
    }
}

$(document).ready(function () {
    initialize();
});


$(document).ready(function () {


    function carregarNoMapa(endereco) {
        geocoder.geocode({'address': endereco + ', Brasil', 'region': 'BR'}, function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                    var latitude = results[0].geometry.location.lat();
                    var longitude = results[0].geometry.location.lng();

                    $('#txtEndereco').val(results[0].formatted_address);
                    $('#txtLatitude').val(latitude);
                    $('#txtLongitude').val(longitude);

                    var location = new google.maps.LatLng(latitude, longitude);
                    marker.setPosition(location);
                    map.setCenter(location);
                    map.setZoom(16);
                }
            }
        });
    }
});


$(document).ready(function () {


    $("#btnEndereco").click(function () {
        if ($(this).val() != "")
            carregarNoMapa($("#txtEndereco").val());
    })

    $("#txtEndereco").blur(function () {
        if ($(this).val() != "")
            carregarNoMapa($(this).val());
    })

});


$(document).ready(function () {


    google.maps.event.addListener(marker, 'drag', function () {
        geocoder.geocode({'latLng': marker.getPosition()}, function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                    $('#txtEndereco').val(results[0].formatted_address);
                    $('#txtLatitude').val(marker.getPosition().lat());
                    $('#txtLongitude').val(marker.getPosition().lng());
                }
            }
        });
    });
});


$(document).ready(function () {


    $("#txtEndereco").autocomplete({
        source: function (request, response) {
            geocoder.geocode({'address': request.term + ', Brasil', 'region': 'BR'}, function (results, status) {
                response($.map(results, function (item) {
                    return {
                        label: item.formatted_address,
                        value: item.formatted_address,
                        latitude: item.geometry.location.lat(),
                        longitude: item.geometry.location.lng()
                    }
                }));
            })
        },
        select: function (event, ui) {
            $("#txtLatitude").val(ui.item.latitude);
            $("#txtLongitude").val(ui.item.longitude);
            var location = new google.maps.LatLng(ui.item.latitude, ui.item.longitude);
            marker.setPosition(location);
            map.setCenter(location);
            map.setZoom(16);
        },
    });




});


