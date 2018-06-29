# Introducción
Proyecto en Java, con Gradle, jUnit y pruebas automatizadas E2E con Python, para ilustar el uso de VSTS para CI/CD.

También se integra con AWS, utilizando servicios como:
- [x] Lambda
- [x] DynamoDB
- [x] API Gateway
- [x] S3

# Estados 

[![Visual Studio Team Services](https://sophosproyectos.visualstudio.com/_apis/public/build/definitions/92704e0a-93c2-4444-a919-6df362b72412/2/badge)](https://sophosproyectos.visualstudio.com/DemoCI/_build/)
[![Visual Studio Team Services](https://rmprodsbr1.vsrm.visualstudio.com/A9552bd68-f839-4b96-8792-861710c377f2/_apis/public/Release/badge/92704e0a-93c2-4444-a919-6df362b72412/1/2)](https://sophosproyectos.visualstudio.com/DemoCI/_release)
[![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.ramirezblauvelt.democi%3ADemoCI&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.ramirezblauvelt.democi%3ADemoCI)

# ¿Qué hace el programa?

Suma días hábiles a fechas, a partir de la consulta de los feriados de un país, desde un servicio web

# Prerrequisitos

1. Java 8
1. Conexión a Internet
1. Intérprete de Python en el ambiente de certificación
1. Acceso a una consola

# Estructura del repositorio

| Ruta                       | Descripción                                                                       | Lenguaje/Herramienta |
|----------------------------|-----------------------------------------------------------------------------------|:--------------------:|
| build.gradle               | Archivo que indica cómo se construye la aplicación y las dependencias externas    |        Gradle        |
| src/main/java              | Fuentes del proyecto en Java                                                      |         Java         |
| src/main/java/.../aws      | Código Java para la Lambda que soporta la API de sumar días hábiles               |      Java/Lambda     |
| src/main/resources         | Recursos del proyecto (archivo de configuración del logger Log4j2)                |      XML/Log4j2      |
| src/test/java              | Pruebas unitarias del proyecto Java con jUnit                                     |      Java/jUnit      |
| src/dist/pruebas_democi.py | Script Python para las pruebas E2E de la aplicación de consola                    |        Python        |
| src/dist/iac               | Scripts YAML para AWS CloudFormation                                              |  YAML/CloudFormation |
| src/dist/países-soportados | Código Pyhton para la Lambda que soporta la API de obtención de países soportados |     Python/Lambda    |
| src/dist/web               | Código de la página web que consume la API                                        |  HTML/CSS/JS/jQuery  |

# ¿Cómo se utiliza?

Es una programa Java stand-alone de línea de comandos, que requiere una serie de argumentos para operar

```
java -jar DemoCI-[version].jar [Acción] [Fecha de referencia] [Días a sumar] [[País]]
```

## Acciones
1. Sumar días hábiles para Colombia específicamente: `sfc`
1. Sumar días hábiles para cualquier país soportado por el servicio web: `sf`

## Fecha de referencia

Es la fecha a partir de la cual, se sumarán los días hábiles indicados.
> La fecha **DEBE** ir en formato `yyyy-MM-dd`

## Días a sumar

Corresponde a un entero con la cantidad de días hábiles a sumarle a la fecha de referencia, según el país elegido y los días feriados que tenga

## País

Corresponde al código del país para el cual, se debe hacer la suma de los días hábiles
> Requerido sólo para acción `sf`
>> Para la acción `sfc` siempre corresponde internamente a `col`

# Ejemplos

| Descripción                                           |         Comando        |
|-------------------------------------------------------|:----------------------:|
| Sumar 10 días hábiles, en Colombia, al 23 de marzo    |   `sfc 2018-03-23 10`  |
| Restar 5 días hábiles, en Colombia, al 5 de enero     |   `sfc 2018-01-05 -5`  |
| Sumar 30 días hábiles, en EE.EE, al 10 de noviembre   | `sf 2018-11-10 30 usa` |
| Sumar 15 días hábiles, en Colombia, al 5 de diciembre | `sf 2018-12-05 15 col` |
