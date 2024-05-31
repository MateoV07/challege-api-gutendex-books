package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibros;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
	private Scanner teclado = new Scanner(System.in);
	private ConsumoAPI consumoAPI = new ConsumoAPI();
	private final String URL_BASE = "https://gutendex.com/books/";
	private ConvierteDatos convierteDatos = new ConvierteDatos();
	private LibroRepository libroRepository;
	private AutorRepository autorRepository;
	private List<Libro> libros;

	public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
		this.libroRepository = libroRepository;
		this.autorRepository = autorRepository;
	}

	public Optional<DatosLibros> getDatosLibros(){
		System.out.println("Ingrese el nombre del libro que desea buscar");
		String tituloLibro = teclado.next();
		String json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.toLowerCase().replace(" ","+"));
		Datos datosBuscados = convierteDatos.obtenerDatos(json,Datos.class);
		Optional<DatosLibros> libroBuscado = datosBuscados.Libros().stream()
				.filter(datosLibros -> datosLibros.titulo().toLowerCase().contains(tituloLibro.toLowerCase()))
				.findFirst();
		return libroBuscado;
	}

	public void buscarLibroPorNombre(){
		Optional<DatosLibros> libroBuscado = getDatosLibros();
		if (libroBuscado.isPresent()){

			Libro libro = new Libro(libroBuscado.get());
			Autor autor = new Autor(libroBuscado.get().autor().get(0));

			Optional<Autor> autorBuscado = autorRepository.findByNombre(autor.getNombre());
			if (autorBuscado.isEmpty()){
				autorRepository.save(autor);
				libro.setAutor(autor);
				libroRepository.save(libro);
				System.out.println(libro.toString());
			}else {
				Optional<Libro> optionalLibro = libroRepository.findByTitulo(libro.getTitulo());
				if ((optionalLibro).isEmpty()){
					libro.setAutor(autor);
					libroRepository.save(libro);
					System.out.println(libro.toString());
				}else {
					System.out.println("No se puede guardar el libro más de una vez");
				}
			}
		}
	}
}
