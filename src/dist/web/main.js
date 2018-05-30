window.onload = init;

/**
 * Inicializa el formulario
 * Establece la fecha por defecto
 * Carga la lista de países admitidos desde la API
 */
function init() {
    // Fecha por defecto
    $('#fechaInicial').val(new Date().toISOString().slice(0, 10));

    // Carga la lista de países permitidos
    var url = "https://v30stkwesf.execute-api.us-east-1.amazonaws.com/v1/paisessoportados/";

    var dropdown = $('#pais');
    dropdown.empty();

    $.getJSON(url, function (data) {
        $.each(data, function (key, entry) {
            dropdown.append($('<option></option>').attr('value', entry.codigo).text(entry.nombre));
        });
        dropdown.val('col');
    });
}

/**
 * Invoca la API que calcula la suma de días hábiles
 */
function invokeRestApi() {
    // Valida el formulario
    if (!validar()) {
        return;
    }

    // Área de texto
    var txtResponse = $('#response');

    // Limpia
    txtResponse.val('');

    // Lee los valores del formulario
    var fechaInicial = $("#fechaInicial").val();
    var diasHabilesSumar = $("#diasHabilesSumar").val();
    var pais = $("#pais").val();

    // Crea el objeto de Request
    var request = {};
    request["fechaInicial"] = fechaInicial;
    request["diasHabilesSumar"] = diasHabilesSumar;
    request["pais"] = pais;

    // URL de la API
    var url = "https://v30stkwesf.execute-api.us-east-1.amazonaws.com/v1/sumardiashabiles/";

    // Construct an HTTP request
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

    // send the collected data as JSON
    xhr.send(JSON.stringify(request));

    xhr.onloadend = function () {
        document.getElementById("response").value = JSON.parse(xhr.responseText);
    };
}

/**
 * Valida el formulario
 */
function validar() {
    var formulario = $("#formulario");

    formulario.validate({
        rules: {
            fechaInicial: {
                required: true,
                dateISO: true
            },
            diasHabilesSumar: {
                required: true,
                digits: true
            },
            pais: {
                required: true,
                minlength: 3, 
                maxlength: 3
            }
        }
    });

    return formulario.valid();
}