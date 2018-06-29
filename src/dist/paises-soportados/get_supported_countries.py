import boto3
import codecs
import json
import requests
import sys

# Procedimiento para manejar eventos AWS Lambda
def handle_request(event, context):
    # Bucket
    bucket_name = 'sumardiashabilesconfestivos'

    # Key name
    key_name = 'paises_soportados.json'

    # Archivo de persistencia local temporal
    archivo = '/tmp/' + key_name

    # Valida si el archivo ya existe
    if file_exists_s3(bucket_name, key_name):
        print('Leyendo archivo')

        # Descarga el archivo de S3
        s3 = boto3.resource('s3')
        s3.meta.client.download_file(bucket_name, key_name, archivo)

        # Lee el contenido del archivo
        with codecs.open(archivo, 'r', 'utf-8') as infile:
            json_salida = json.load(infile)

    else:
        print('Consultando servicio')

        # URL de la API REST para consultar los países soportados por el servicio
        url = 'https://kayaposoft.com/enrico/json/v2.0/?action=getSupportedCountries'

        # Consume el servicio
        response = requests.get(url)

        # Valida el resultado
        if response.status_code != requests.codes.ok:
            sys.exit("Error en el servicio: " + response.reason)

        # Decodifica el resultado
        result = response.json()

        # Arma la respuesta
        paises = []
        for country in result:
            pais = Pais(country['countryCode'], country['fullName'])
            paises.append(pais)

        # JSONify
        string_json_salida = json.dumps(paises, default=jsonDefault, ensure_ascii=False)
        json_salida = json.loads(string_json_salida)

        # Escribe el archivo local
        with codecs.open(archivo, 'w', 'utf-8') as outfile:
            outfile.write(string_json_salida)
        
        # Lo carga en S3
        s3 = boto3.resource('s3')
        s3.meta.client.upload_file(archivo, bucket_name, key_name)

    print(json_salida)

    return json_salida

# Funcion para determinar si un archivo existe
def file_exists_s3(bucket_name, key_name):
    client = boto3.client('s3')
    response = client.list_objects_v2(
        Bucket=bucket_name,
        MaxKeys=1,
        Prefix=key_name
    )
    file_exists = response['KeyCount'] > 0
    return file_exists

# Clase para representar el objeto de salida
class Pais():
    def __init__(self, codigo, nombre):
        self.codigo = codigo
        self.nombre = nombre

# Funcion para convertir objeto personalizado a JSON
def jsonDefault(object):
    return object.__dict__
