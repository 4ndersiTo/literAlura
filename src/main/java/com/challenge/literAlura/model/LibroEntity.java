package com.challenge.literAlura.model;

import com.challenge.literAlura.dto.AutorDto;
import com.challenge.literAlura.dto.LibroDto;
import jakarta.persistence.*;

@Entity
@Table(name = "libro")
public class LibroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name = "titulo")
    private String title;

    @Column(name = "autor")
    private String author;

    @Column(name = "idioma")
    private String language;

    @Column(name = "numero_descargas")
    private Double downloads;

    public LibroEntity() {
    }

    public LibroEntity(LibroDto bookData) {
        this.title = bookData.title();
        this.author = getFirstAuthor(bookData).getName();
        this.language = getFirstLanguage(bookData);
        this.downloads = bookData.downloads();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public AutorEntity getFirstAuthor(LibroDto bookData) {
        AutorDto authorData = bookData.author().get(0);
        return new AutorEntity(authorData);
    }

    public String getFirstLanguage(LibroDto bookData) {
        return bookData.language().get(0);
    }

    @Override
    public String toString() {
        return ">>>> Información de LIBRO <<<<<" +
                "\n - Titulo: " + title +
                "\n - Autor: " + author +
                "\n - Idioma: " + language +
                "\n - Número de descargas: " + downloads;
    }
}
