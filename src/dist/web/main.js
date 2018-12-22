window.onload = init;

var currentApiURL  = "#{AWS_AG_URL}#";
var fallbackApiUrl = "https://55mg4yvj1m.execute-api.us-east-1.amazonaws.com/cf";
var apiUrl = (currentApiURL.startsWith("#") ? fallbackApiUrl : currentApiURL);

/**
 * Inicializa el formulario
 * Establece la fecha por defecto
 * Carga la lista de países admitidos desde la API
 */
function init() {
    // Indicador de carga
    $(document).ajaxStart(function() {
        $('#loading').show();
    }).ajaxStop(function() {
        $('#loading').hide();
    });

    // Fecha por defecto
    $('#fechaInicial').val(new Date().toISOString().slice(0, 10));

    // Carga la lista de países permitidos
    var url = apiUrl +  "/paisessoportados/";

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
    var url = apiUrl +  "/sumardiashabiles/";

    // Llama la API
    $.post(url, JSON.stringify(request), function(data) {
        txtResponse.val(data.resultado);
    });
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