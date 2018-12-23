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
    return {
        "resultado": textoSalida
    };
}
