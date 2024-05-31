package com.aluracursos.literalura.model;

public enum Idioma {
	ESPANOL("es"),
	INGLES("en"),
	FRANCES("fr"),
	PORTUGUES("pt");

	private String idiomaLibro;

	Idioma(String idiomaLibro){
		this.idiomaLibro = idiomaLibro;
	}

	public static Idioma idiomaPrincipal (String text){
		for (Idioma idioma : Idioma.values()){
			if(idioma.idiomaLibro.equalsIgnoreCase(text)){
				return idioma;
			}
		}
		throw new IllegalArgumentException("Idioma no encontrado:" + text);
	}


}
