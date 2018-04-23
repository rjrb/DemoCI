import os
import fnmatch
import subprocess
import sys
import pandas as pd
import locale

# Cambia la ruta de trabajo
os.chdir(os.path.dirname(os.path.realpath(__file__)))

# Obtiene el nombre del ejecutable
exe = fnmatch.filter(os.listdir('.'), '*.jar')[0]

# Comando para ejecutar la aplicación
cmd = "java -jar " + exe

# Obtiene el charset local
charset = locale.getdefaultlocale()[1]

# Pruebas a ejecutar (Argumentos : Salida esperada :  Resultado si exitoso : Error)
pruebas = [
    ['novalido', 'No especificó una acción válida. Se espera una de las siguientes acciones', "Programa sin parámetros", "Error en prueba sin argumentos"], 
    ['sfc', 'Se esperan 2 argumentos para la operación sfc y se recibieron 0', "Programa sólo con argumento", "Error en prueba con argumento 'sfc'"], 
    ['sfc' + ' ' + '2018-03-23', 'Se esperan 2 argumentos para la operación sfc y se recibieron 1', "Programa sólo con argumento y fecha sin días", "Error en prueba con argumento y fecha, sin días"], 
    ['sfc' + ' ' + '2018-03-23' + ' ' + 'zfdfsd', "No se pudo convertir la cantidad de días 'zfdfsd'", "Programa con dos argumentos pero días inválidos", "Error en prueba con argumento y fecha, pero días inválidos"], 
    ['sfc' + ' ' + '20180323' + ' ' + 'zfdfsd', "No se pudo convertir la fecha '20180323'", "Programa con dos argumentos pero fecha inválida", "Error en prueba con argumento, fecha y días, pero fecha inválida"]
]

# Ejecuta las pruebas
for i in range(len(pruebas)):
    sout = subprocess.run((cmd + ' ' + pruebas[i][0]).split(), stdout=subprocess.PIPE)
    resultado = sout.stdout.decode(charset)
    if pruebas[i][1] in resultado:
        print(pruebas[i][2] + " ---> OK")
    else:
        sys.exit(pruebas[i][3] + "(" + pruebas[i][0]  + ") ---> " + resultado)

print('........................')

# Plantilla de resultados correctos
plantilla = "El resultado de sumar '_dias_' días hábiles en '_pais_' a la fecha '_referencia_' es: '_esperado_'"

# Datos de prueba
d = {
    'dias': ["10", "5", "15", "15"], 
    'referencia': ["2018-03-23", "2018-04-30", "2017-12-24", "2017-12-24"], 
    'esperado': ["2018-04-10", "2018-05-08", "2018-01-17", "2018-01-12"], 
    'pais': ['col', 'col', 'col', 'usa'], 
    'comando': ['sfc', 'sfc', 'sfc', 'sf']
}
datos_prueba = pd.DataFrame(data=d)

# Ejecuta los procesos
for index, row in datos_prueba.iterrows():
    comando = cmd + ' ' + row['comando'] + ' ' + row['referencia'] + ' ' + row['dias'] + (' ' + row['pais'] if row['comando'] == 'sf' else '')
    sout = subprocess.run(comando.split(), stdout=subprocess.PIPE)
    resultado = sout.stdout.decode(charset)
    resultado_esperado = plantilla.replace("_dias_", row['dias']).replace("_referencia_", row['referencia']).replace("_esperado_", row['esperado']).replace("_pais_", row['pais'])
    if resultado_esperado in resultado:
        print(comando + " ---> OK")
    else:
        sys.exit("Error en prueba (" + comando  + ") ---> " + resultado)
