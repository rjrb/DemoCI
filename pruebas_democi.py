import os
import subprocess

import sys

# Cambia la ruta de trabajo
os.chdir(os.path.dirname(os.path.realpath(__file__)))

# Programa sin parámetros
result = subprocess.run(['java', '-jar', 'DemoCI-1.0.jar'], stdout=subprocess.PIPE)
salida1 = result.stdout.decode('ISO-8859-1')
if 'No especificó una acción. Se espera uno de los siguientes parámetros' in salida1:
    print("Programa sin parámetros ---> OK")
else:
    sys.exit("Error en prueba sin argumentos")

# Programa con argumento inválido
result = subprocess.run(['java', '-jar', 'DemoCI-1.0.jar', 'novalido'], stdout=subprocess.PIPE)
salida2 = result.stdout.decode('ISO-8859-1')
if 'No especificó una acción válida. Se espera una de las siguientes acciones' in salida2:
    print("Programa con argumento inválido ---> OK")
else:
    sys.exit("Error en prueba con argumento inválido")

# Programa sólo con argumento
result = subprocess.run(['java', '-jar', 'DemoCI-1.0.jar', 'sfc'], stdout=subprocess.PIPE)
salida3 = result.stdout.decode('ISO-8859-1')
if 'Se esperan 2 argumentos para la operación sfc y se recibieron 0' in salida3:
    print("Programa sólo con argumento ---> OK")
else:
    sys.exit("Error en prueba con argumento 'sfc'")

# Programa sólo con argumento y fecha sin días
result = subprocess.run(['java', '-jar', 'DemoCI-1.0.jar', 'sfc', '2018-03-23'], stdout=subprocess.PIPE)
salida4 = result.stdout.decode('ISO-8859-1')
if 'Se esperan 2 argumentos para la operación sfc y se recibieron 1' in salida4:
    print("Programa sólo con argumento y fecha sin días ---> OK")
else:
    sys.exit("Error en prueba con argumento 'sfc'")

# Programa con dos argumentos pero días inválidos
result = subprocess.run(['java', '-jar', 'DemoCI-1.0.jar', 'sfc', '2018-03-23', 'zfdfsd'], stdout=subprocess.PIPE)
salida5 = result.stdout.decode('ISO-8859-1')
if "No se pudo convertir la cantidad de días 'zfdfsd'" in salida5:
    print("Programa con dos argumentos pero días inválidos ---> OK")
else:
    sys.exit("Error en prueba con argumento 'sfc 2018-03-23 zfdfsd'")

# Programa con dos argumentos pero fecha inválida
result = subprocess.run(['java', '-jar', 'DemoCI-1.0.jar', 'sfc', '20180323', '10'], stdout=subprocess.PIPE)
salida6 = result.stdout.decode('ISO-8859-1')
if "No se pudo convertir la fecha '20180323'" in salida6:
    print("Programa con dos argumentos pero fecha inválida ---> OK")
else:
    sys.exit("Error en prueba con argumento 'sfc 20180323 10'")


# Plantilla de resultados correctos
plantilla = "[INFO] - El resultado de sumar '_dias_' días hábiles en '_pais_' a la fecha '_referencia_' es: '_esperado_'"

# Programa OK
dias = "10"
referencia = "2018-03-23"
esperado = "2018-04-10"
result = subprocess.run(['java', '-jar', 'DemoCI-1.0.jar', 'sfc', referencia, dias], stdout=subprocess.PIPE)
salida7 = result.stdout.decode('ISO-8859-1')
resultado_esperado = plantilla.replace("_dias_", dias).replace("_referencia_", referencia).replace("_esperado_", esperado).replace("_pais_", "col")
if resultado_esperado in salida7:
    print("'sfc " + referencia + " " + dias + "' ---> OK")
else:
    sys.exit("Error en prueba con argumento 'sfc " + referencia + " " + dias + "': " + salida7)

# Programa OK
dias = "5"
referencia = "2018-04-30"
esperado = "2018-05-08"
result = subprocess.run(['java', '-jar', 'DemoCI-1.0.jar', 'sfc', referencia, dias], stdout=subprocess.PIPE)
salida8 = result.stdout.decode('ISO-8859-1')
resultado_esperado = plantilla.replace("_dias_", dias).replace("_referencia_", referencia).replace("_esperado_", esperado).replace("_pais_", "col")
if resultado_esperado in salida8:
    print("'sfc " + referencia + " " + dias + "' ---> OK")
else:
    sys.exit("Error en prueba con argumento 'sfc " + referencia + " " + dias + "': " + salida8)

# Programa OK
dias = "15"
referencia = "2017-12-24"
esperado = "2018-01-17"
result = subprocess.run(['java', '-jar', 'DemoCI-1.0.jar', 'sfc', referencia, dias], stdout=subprocess.PIPE)
salida9 = result.stdout.decode('ISO-8859-1')
resultado_esperado = plantilla.replace("_dias_", dias).replace("_referencia_", referencia).replace("_esperado_", esperado).replace("_pais_", "col")
if resultado_esperado in salida9:
    print("'sfc " + referencia + " " + dias + "' ---> OK")
else:
    sys.exit("Error en prueba con argumento 'sfc " + referencia + " " + dias + "': " + salida9)

# Programa OK
dias = "15"
referencia = "2017-12-24"
esperado = "2018-01-12"
pais = "usa"
result = subprocess.run(['java', '-jar', 'DemoCI-1.0.jar', 'sf', referencia, dias, pais], stdout=subprocess.PIPE)
salida10 = result.stdout.decode('ISO-8859-1')
resultado_esperado = plantilla.replace("_dias_", dias).replace("_referencia_", referencia).replace("_esperado_", esperado).replace("_pais_", pais)
if resultado_esperado in salida10:
    print("'sf " + referencia + " " + dias + " " + pais + "' ---> OK")
else:
    sys.exit("Error en prueba con argumento 'sf " + referencia + " " + dias + " " + pais + "': " + salida10)

