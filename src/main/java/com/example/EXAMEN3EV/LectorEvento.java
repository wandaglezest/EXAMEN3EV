package com.example.EXAMEN3EV;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class LectorEvento {

	// logs
	private static final Logger logger = Logger.getLogger(LectorEvento.class.getName());

	static {
		try {
			FileHandler fileHandler = new FileHandler("src/main/resources/application.log", true);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);

			ConsoleHandler consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(consoleHandler);

			logger.setLevel(Level.INFO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// eventosfuturos
	public static List<Evento> obtenerEventosFuturos(List<Evento> eventos) {
		LocalDateTime ahora = LocalDateTime.now();
		List<Evento> eventosFuturos = new ArrayList<>();

		logger.info("Obteniendo eventos futuros respecto a la fecha actual: " + ahora);

		for (Evento evento : eventos) {
			if (evento.getFecha().isAfter(ahora)) {
				eventosFuturos.add(evento);
				logger.info("Evento futuro encontrado: " + evento.getNombre() + " para la fecha: " + evento.getFecha());
			}
		}

		return eventosFuturos;
	}

	// leerfichero
	public static List<Evento> leerEventosDesdeFichero(String nombreFichero) {
		List<Evento> eventos = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(nombreFichero))) {
			String linea;

			while ((linea = reader.readLine()) != null) {
				linea = linea.trim();
				String[] atributos = linea.split(",");

				if (atributos.length == 4) {
					String nombre = atributos[0];
					LocalDateTime fecha = LocalDateTime.parse(atributos[1]);
					String ubicacion = atributos[2];
					String descripcion = atributos[3];

					Evento evento = new Evento(nombre, fecha, ubicacion, descripcion);
					eventos.add(evento);
				} else {
					System.out.println("La línea tiene mas partes " + linea);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return eventos;
	}

	// Hacerfichero
	public static void escribirEventosAFichero(List<Evento> eventos, String nombreFichero) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreFichero))) {
			for (Evento evento : eventos) {
				writer.write(evento.getNombre() + "," + evento.getFecha().toString() + "," + evento.getUbicacion() + ","
						+ evento.getDescripcion());
				writer.newLine();
			}
			System.out.println("Eventos escritos en el archivo: " + nombreFichero);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// crearexcel
	public static void crearExcelConEventos(List<Evento> eventos, String nombreFichero) {
		XSSFWorkbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Eventos");

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Nombre");
		headerRow.createCell(1).setCellValue("Fecha");
		headerRow.createCell(2).setCellValue("Ubicación");
		headerRow.createCell(3).setCellValue("Descripción");

		int rowNum = 1;
		for (Evento evento : eventos) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(evento.getNombre());
			row.createCell(1).setCellValue(evento.getFecha().toString());
			row.createCell(2).setCellValue(evento.getUbicacion());
			row.createCell(3).setCellValue(evento.getDescripcion());
		}

		for (int i = 0; i < 4; i++) {
			sheet.autoSizeColumn(i);
		}

		try (FileOutputStream fileOut = new FileOutputStream(nombreFichero)) {
			workbook.write(fileOut);
			System.out.println("Archivo Excel creado: " + nombreFichero);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// crearpdf
	public static void crearPdfConEventos(List<Evento> eventos, String nombreFichero) {
		try {
			PdfWriter escritor = new PdfWriter(nombreFichero);
			PdfDocument pdf = new PdfDocument(escritor);
			Document documento = new Document(pdf);

			documento.add(new Paragraph("Lista de Eventos:"));

			for (Evento evento : eventos) {
				documento.add(new Paragraph("Nombre: " + evento.getNombre()));
				documento.add(new Paragraph("Fecha: " + evento.getFecha().toString()));
				documento.add(new Paragraph("Ubicación: " + evento.getUbicacion()));
				documento.add(new Paragraph("Descripción: " + evento.getDescripcion()));
				documento.add(new Paragraph(" "));
			}

			documento.close();

			System.out.println("PDF creado en: " + nombreFichero);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		List<Evento> eventos = leerEventosDesdeFichero("src/main/resources/eventos.txt");

		for (Evento evento : eventos) {
			System.out.println(evento);
		}

		// escribirEventosAFichero(eventos, "src/main/resources/salida_eventos.txt");

		// crearExcelConEventos(eventos, "src/main/resources/excel_eventos.xlsx");

		// crearPdfConEventos(eventos, "src/main/resources/pdf_eventos.pdf");

		// sacareventosfuturos
		List<Evento> eventoss = leerEventosDesdeFichero("src/main/resources/eventos.txt");

		List<Evento> eventosFuturos = obtenerEventosFuturos(eventoss);

		System.out.println("Eventos futuros:");
		for (Evento evento : eventosFuturos) {
			System.out.println(evento);
		}
	}

}
