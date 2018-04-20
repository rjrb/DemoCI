# Introducción
Proyecto pequeño en Java, con Gradle, jUnit y pruebas automatizadas con Python, para ilustar el uso de VSTS para CI/CD

# ¿Qué hace el programa?

Suma días hábiles a fechas, a partir de la consulta de los feriados de un país, desde un servicio web

# ¿Cómo se utiliza?

```
java -jar Demo-[version].jar [Acción] [Fecha de referencia] [Días a sumar] [[País]]
```

## Acciones
1. Sumar días hábiles para Colombia específicamente: `sfc`
1. Sumar días hábiles para cualquier país soportado por el servicio web: `sf`

## Fecha de referencia

Es la fecha a partir de la cual, se sumarán los días hábiles indicados.
> La fecha **DEBE** ir en formato `yyyy-MM-dd`

## Días a sumar

Corresponde a un entero con la cantidad de días hábiles a sumarle a la fecha de referencia, según el país elegido y los días feriados que tenga

## País (requerido sólo para acción `sf`)

Corresponde al código del país para el cual, se debe hacer la suma de los días hábiles
> Para la acción `sfc` siempre corresponde internamente a `col`

# Ejemplos

| Descripción                                           |         Comando        |
|-------------------------------------------------------|:----------------------:|
| Sumar 10 días hábiles, en Colombia, al 23 de marzo    |   `sfc 2018-03-23 10`  |
| Restar 5 días hábiles, en Colombia, al 5 de enero     |   `sfc 2018-01-05 -5`  |
| Sumar 30 días hábiles, en EE.EE, al 10 de noviembre   | `sf 2018-11-10 30 usa` |
| Sumar 15 días hábiles, en Colombia, al 5 de diciembre | `sf 2018-12-05 15 col` |
