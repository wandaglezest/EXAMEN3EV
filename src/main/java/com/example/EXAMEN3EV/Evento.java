package com.example.EXAMEN3EV;

import java.time.LocalDateTime;

public class Evento {

	// Atributos
	private String nombre;
	private LocalDateTime fecha;
	private String ubicacion;
	private String descripcion;

	// Constructor
	public Evento(String nombre, LocalDateTime fecha, String ubicacion, String descripcion) {
		this.nombre = nombre;
		this.fecha = fecha;
		this.ubicacion = ubicacion;
		this.descripcion = descripcion;
	}

	// Métodos Getter y Setter

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "Evento[" + nombre + ", " + fecha + ", " + ubicacion + ", " + descripcion + "] ";
	}
}
