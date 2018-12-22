/**
 * Handler de eventos Lambda
 */
exports.handler = async (event) => {
    // Importa la librería moment
    var moment = require('moment');
    moment.locale("es");

    // Valores recibidos
    console.log("Valores recibidos:");
    console.log(event);

    // Lee la fecha inicial
    var fechaInicial = moment(event.fechaInicial);
    var diasSumar = parseInt(event.diasSumar);

    // Suma los días calendario
    var fechaFinal = fechaInicial.clone().add(diasSumar, "days");

    // Resultado
    var textoFecha = fechaFinal.format("dddd D [de] MMMM [de] YYYY");
    var textoSalida = "El resultado de sumar "
        + diasSumar
        + " días calendario a la fecha "
        + fechaInicial.toISOString().slice(0, 10)
        + " es: \n"
        + fechaFinal.toISOString().slice(0, 10)
        + " ("
        + textoFecha
        + ")"
    ;
    console.log("Resultado:");
    console.log(textoSalida);

    // Entrega el resultado
    const response = {
        statusCode: 200,
        body: {
            "resultado": JSON.stringify(textoSalida)
        }
    };
    return response;
}

/**
 * Función que genera el texto largo de una fecha en español
 * @param fecha el objeto Date a analizar
 * @returns {string}
 */
function getFechaComoText(fecha) {
    // Inicializa la cadena
    var textoFecha = "";

    // Día de la semana
    textoFecha += getDiaSemana(fecha);
    textoFecha += " ";

    // Día del mes
    textoFecha += fecha.getDate();
    textoFecha += " de ";

    // Mes
    textoFecha += getMes(fecha);
    textoFecha += " de ";

    // Año
    textoFecha += fecha.getFullYear();

    // Entrega el texto
    return textoFecha;
}

/**
 * Función que entrega el texto del mes
 * @param fecha el objeto Date a analizar
 * @returns {string}
 */
function getMes(fecha) {
    // Nombre de los meses
    var months = [
        "enero",
        "febrero",
        "marzo",
        "abri",
        "mayo",
        "junio",
        "julio",
        "agosto",
        "septiembre",
        "octubre",
        "noviembre",
        "diciembre"
    ];

    // Entrega el texto correspondiente
    return months[fecha.getMonth()];
}

/**
 * Función que entrega el texto del día de la semana
 * @param fecha el objeto Date a analizar
 * @returns {string}
 */
function getDiaSemana(fecha) {
    // Nombres de los días de la semana
    var weekday = new Array(7);
    weekday[0] =  "domingo";
    weekday[1] = "lunes";
    weekday[2] = "martes";
    weekday[3] = "miércoles";
    weekday[4] = "jueves";
    weekday[5] = "viernes";
    weekday[6] = "sábado";

    // Entrega el día de la semana correspondiente
    return weekday[fecha.getDay()];
}
