package com.yandex.volkov.java_kanban.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KVServer {
	public static final int PORT = 8078;
	private final String apiToken;
	private final HttpServer server;
	private final Map<String, String> data = new HashMap<>();

	public KVServer() throws IOException {
		apiToken = generateApiToken();
		server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);

		server.createContext("/register", this::register);
		server.createContext("/save", this::save);
		server.createContext("/load", this::load);
	}

	public void start() {
		System.out.println("API_TOKEN: " + apiToken);
		server.start();
		System.out.println("KVServer на порту ".concat(Integer.toString(PORT).concat(" запущен")));
	}

	public void stop() {
		server.stop(1);
		System.out.println("KVServer на порту ".concat(Integer.toString(PORT).concat(" остановлен")));
	}

	private void load(HttpExchange exchange) throws IOException {
		try (exchange) {
			System.out.println("\n/load");
			if (!hasAuth(exchange)) {
				System.out.println("Запрос не авторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
				return;
			}
			if ("GET".equals(exchange.getRequestMethod())) {
				String key = exchange.getRequestURI().getPath().substring("/load/".length());
				if (key.isEmpty()) {
					System.out.println("Key для сохранения пустой. key указывается в пути: /load/{key}");
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
					return;
				}
				sendText(exchange, data.get(key));
				System.out.println("Значение для ключа " + key + " успешно загружено!");
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			} else {
				System.out.println("/load ждёт GET-запрос, а получил: " + exchange.getRequestMethod());
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
			}
		}
	}

	private void save(HttpExchange exchange) throws IOException {
		try (exchange) {
			System.out.println("\n/save");
			if (!hasAuth(exchange)) {
				System.out.println("Запрос не авторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
				return;
			}
			if ("POST".equals(exchange.getRequestMethod())) {
				String key = exchange.getRequestURI().getPath().substring("/save/".length());
				if (key.isEmpty()) {
					System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
					return;
				}
				String value = readText(exchange);
				if (value.isEmpty()) {
					System.out.println("Value для сохранения пустой. value указывается в теле запроса");
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
					return;
				}
				data.put(key, value);
				System.out.println("Значение для ключа " + key + " успешно обновлено!");
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			} else {
				System.out.println("/save ждёт POST-запрос, а получил: " + exchange.getRequestMethod());
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
			}
		}
	}

	private void register(HttpExchange exchange) throws IOException {
		try (exchange) {
			System.out.println("\n/register");
			if ("GET".equals(exchange.getRequestMethod())) {
				sendText(exchange, apiToken);
			} else {
				System.out.println("/register ждёт GET-запрос, а получил " + exchange.getRequestMethod());
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
			}
		}
	}

	private String generateApiToken() {
		return "" + System.currentTimeMillis();
	}

	protected boolean hasAuth(HttpExchange exchange) {
		String rawQuery = exchange.getRequestURI().getRawQuery();
		return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
	}

	protected String readText(HttpExchange exchange) throws IOException {
		return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
	}

	protected void sendText(HttpExchange exchange, String text) throws IOException {
		byte[] resp = text.getBytes(UTF_8);
		exchange.getResponseHeaders().add("Content-Type", "application/json");
		exchange.sendResponseHeaders(200, resp.length);
		exchange.getResponseBody().write(resp);
	}
}