package com.challenge.literAlura.model;

import com.challenge.literAlura.dto.AutorDto;
import com.challenge.literAlura.dto.LibroDto;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autor")
public class AutorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name = "nombre")
    private String name;

    @Column(name = "anio_nacimiento")
    private Integer deathYear;

    @Column(name = "anio_fallecimiento")
    private Integer birthYear;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<LibroEntity> books;

    //TODO: SOBRECARGA DE CONSTRUCTORES
    public AutorEntity() {
    }

    public AutorEntity(AutorDto authorData) {
        this.name = authorData.name();
        this.birthYear = Integer.valueOf(authorData.birthYear());
        this.deathYear = Integer.valueOf(authorData.deathYear());
    }

    //TODO: ENCAPSULACIÓN
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public AutorEntity getFirstAuthor(LibroDto bookData) {
        AutorDto authorData = bookData.author().get(0);
        return new AutorEntity(authorData);
    }

    @Override
    public String toString() {
        return "**** Información de Autor ****" +
                "\n - Nombre: " + name +
                "\n - Año de nacimiento: " + birthYear +
                "\n - Año  de fallecimineto: " + deathYear;
    }
}
