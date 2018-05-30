package com.ramirezblauvelt.democi.aws.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.ramirezblauvelt.democi.aws.ConstantesAWS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ConsultarFestivos implements ConstantesAWS {

	/* Log de eventos */
	private static final Logger LOGGER = LogManager.getLogger();

	private ConsultarFestivos() {

	}

	/**
	 * Consulta todos los festivos registrados en el sistema
	 * @return la estructura de datos en memoria
	 */
	public static ConcurrentMap<String, ConcurrentMap<Integer, Set<LocalDate>>> listarTodosFestivos() {
		// Crea el objeto
		final ConcurrentMap<String, ConcurrentMap<Integer, Set<LocalDate>>> festivosGlobales = new ConcurrentHashMap<>();

		// Obtiene el cliente
		final AmazonDynamoDB client = ConexionDynamoDB.getCliente();

		// Mapeo
		final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

		// Expresión de consulta
		final DynamoDBScanExpression scan = new DynamoDBScanExpression();

		// Consulta
		final List<ItemFestivos> items = dynamoDBMapper.scan(ItemFestivos.class, scan);

		// Recorre los resultados cargando el mapa
		for(ItemFestivos f : items) {
			// País
			festivosGlobales.putIfAbsent(f.getPais(), new ConcurrentHashMap<>());

			// Convierte los festivos
			final Set<LocalDate> festivos = f.getFestivos().parallelStream().map(LocalDate::parse).collect(Collectors.toSet());

			// Año
			festivosGlobales.get(f.getPais()).putIfAbsent(f.getYyyy(), festivos);
		}

		// Entrega los festivos
		return festivosGlobales;
	}

	/**
	 * Consulta los festivos registrados para el páis indicado
	 * @param pais código del país a consultar
	 * @return todos los festivos registrados para el país
	 */
	public static ConcurrentMap<Integer, Set<LocalDate>> consultarFestivos(String pais) {
		// Crea el objeto
		final ConcurrentMap<Integer, Set<LocalDate>> festivosPais = new ConcurrentHashMap<>();

		// Obtiene el cliente
		final AmazonDynamoDB client = ConexionDynamoDB.getCliente();

		// Mapeo
		final DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);

		// Item a consultar
		final ItemFestivos llaveParticion = new ItemFestivos();
		llaveParticion.setPais(pais);

		// Expresión de consulta
		final DynamoDBQueryExpression<ItemFestivos> query = new DynamoDBQueryExpression<ItemFestivos>()
			.withHashKeyValues(llaveParticion)
		;

		// Consulta
		final List<ItemFestivos> items = dynamoDBMapper.query(ItemFestivos.class, query);

		// Recorre los resultados cargando el mapa
		for(ItemFestivos f : items) {
			// Convierte los festivos
			final Set<LocalDate> festivos = f.getFestivos().parallelStream().map(LocalDate::parse).collect(Collectors.toSet());

			// Año
			festivosPais.putIfAbsent(f.getYyyy(), festivos);
		}

		// Entrega los festivos
		return festivosPais;
	}


	/**
	 * Procedimiento para consultar los festivos de un país y un año
	 * @param pais código del país a consultar
	 * @param yyyy año a consultar
	 */
	public static Set<LocalDate> consultarFestivos(String pais, int yyyy) {
		// Conjunto de festivos para el país y el año
		Set<LocalDate> festivos = new HashSet<>();

		// Obtiene la conexión
		final DynamoDB dynamoDB = ConexionDynamoDB.getConexion();

		// Obtiene la tabla
		final Table table = dynamoDB.getTable(TABLA_FESTIVOS);

		try {
			// Define la consuta
			final QuerySpec spec = new QuerySpec()
				.withKeyConditionExpression("pais = :country AND yyyy = :ano")
				.withValueMap(
					new ValueMap()
						.withString(":country", pais)
						.withInt(":ano", yyyy)
				)
			;

			// Ejecuta la consulta
			final ItemCollection<QueryOutcome> items = table.query(spec);

			// Recorre los resultados
			final Iterator<Item> iterator = items.iterator();
			Item item;
			while (iterator.hasNext()) {
				item = iterator.next();
				festivos = item.getList("festivos").parallelStream().map(s -> LocalDate.parse(s.toString())).collect(Collectors.toSet());
			}

		} catch (Exception e) {
			LOGGER.error("Error consultando registros de DynamoDB", e);
		}

		// Entrega los festivos
		return festivos;
	}
}
