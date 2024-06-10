package com.challenge.literAlura;

import com.challenge.literAlura.dto.LibroDto;
import com.challenge.literAlura.dto.ResultadosDto;
import com.challenge.literAlura.model.AutorEntity;
import com.challenge.literAlura.model.LibroEntity;
import com.challenge.literAlura.repository.AutorRepository;
import com.challenge.literAlura.repository.LibroRepository;
import com.challenge.literAlura.service.ConsumptionAPI;
import com.challenge.literAlura.service.ConvertData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

    // todo: Injección por constructor
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public LiterAluraApplication(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiterAluraApplication.class, args);
    }

    @Override
    public void run(String... args) {
        LiterAluraApplication.Main main = new LiterAluraApplication.Main(libroRepository, autorRepository);
        main.mostrarMenu();
    }

    static class Main {
        private final Scanner sc = new Scanner(System.in);
        private final ConvertData convertData = new ConvertData();
        private final ConsumptionAPI consumptionApi = new ConsumptionAPI();
        private final LibroRepository bookRepository;
        private final AutorRepository authorRepository;

        List<LibroEntity> libros;
        List<AutorEntity> autores;

        public Main(LibroRepository bookRepository, AutorRepository authorRepository) {
            this.bookRepository = bookRepository;
            this.authorRepository = authorRepository;
        }

        public void mostrarMenu() {
            final var menu = """
                
                
                
                
                ############# SELECCIONE UNA OPCIÓN ################
                1 - Registro en BD, de libro de API, por TITULO
                2 - Libros registrados en BD
                3 - Autores registrados en BD
                4 - Lista de autores que estuvieros vivos en un año
                5 - Lista de libros por IDIOMA
                0 - SALIR
                ####################################################
                """;

            var opcion = -1;
            while (opcion != 0) {
                System.out.println(menu);
                System.out.print("OPCIÓN -> ");

                opcion = sc.nextInt();
                sc.nextLine();
                switch (opcion) {
                    case 1 -> buscarLibroPorTitulo();
                    case 2 -> librosRegistradosEnBD();
                    case 3 -> autoresRegistradosEnBD();
                    case 4 -> autoresVivosEnAnioEspecificado();
                    case 5 -> librosPorIdioma();
                    case 0 -> System.out.println("-> CERRANDO APLICACIÓN...");
                    default -> System.out.println("-> OPCIÓN INVALIDA, INGRESE OTRA OPCIÓN \n\n");
                }
            }
        }

        // TODO: OPCIÓN 1
        private void buscarLibroPorTitulo() {
            System.out.print("Ingrese un TÍTULO de libro: ");
            String inTitle = sc.nextLine();

            var json = consumptionApi.getData(inTitle.replace(" ", "%20"));
            var data = convertData.getData(json, ResultadosDto.class);

            if (data.results().isEmpty()) {
                System.out.println("Libro no encontrado en la API");
            } else {
                LibroDto bookData = data.results().get(0);

                LibroEntity book = new LibroEntity(bookData);
                AutorEntity author = new AutorEntity().getFirstAuthor(bookData);

                this.guardarLibroEnBD(book, author);
            }
        }

        private void guardarLibroEnBD(LibroEntity book, AutorEntity author) {
            Optional<LibroEntity> bookFound = bookRepository.findByTitleContains(book.getTitle());
            if (bookFound.isPresent()) {
                System.out.println("ESTE LIBRO YA SE ENCUENTRA REGISTRADO.");
            } else {
                try {
                    bookRepository.save(book);
                    System.out.println("SE REGISTRÓ EL LIBRO");
                } catch (Exception e) {
                    System.out.println("Error inesperado: " + e.getMessage());
                }
            }

            Optional<AutorEntity> authorFound = authorRepository.findByNameContains(author.getName());
            if (authorFound.isPresent()) {
                System.out.println("ESTE AUTOR YA SE ENCUENTRA REGISTRADO");
            } else {
                try {
                    authorRepository.save(author);
                    System.out.println("SE REGISTRÓ CON ÉXITO EL AUTOR");
                } catch (Exception e) {
                    System.out.println("Error inesperado: " + e.getMessage());
                }
            }
        }

        // TODO: OPCIÓN 2
        private void librosRegistradosEnBD() {
            System.out.println("$$$$$$$$$$$$$$$$$$ LISTA DE LIBROS DE BD");
            libros = bookRepository.findAll();

            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados en BD");
            }

            libros.stream()
                    .sorted(Comparator.comparing(LibroEntity::getTitle))
                    .forEach(System.out::println);
        }

        // TODO: OPCIÓN 3
        private void autoresRegistradosEnBD() {
            System.out.println("$$$$$$$$$$$$$$$$$$ LISTA DE AUTORES DE BD");
            autores = authorRepository.findAll();
            if (autores.isEmpty()) {
                System.out.println("No hay autores registrados en BD");
            }

            autores.stream()
                    .sorted(Comparator.comparing(AutorEntity::getName))
                    .forEach(System.out::println);
        }

        // TODO: OPCIÓN 4
        private void autoresVivosEnAnioEspecificado() {
            System.out.println("$$$$$$$$$$$$$$$$$$ LISTA DE AUTORES QUE VIVIERON EN UN AÑO DETERMINADO");
            System.out.print("Ingrese un año: ");
            Integer year = Integer.valueOf(sc.nextLine());
            autores = authorRepository
                    .findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores.");
            } else {
                autores.stream()
                        .sorted(Comparator.comparing(AutorEntity::getName))
                        .forEach(System.out::println);
            }
        }

        // TODO: OPCIÓN 5
        private void librosPorIdioma() {

            final var seleccionIdioma = """
                ############# SELECCIONE UN IDIOMA ################
                en - English
                es - Spanish
                fr - French
                pt - Portuguese
                ####################################################
                """;

            System.out.println("$$$$$$$$$$$$$$$$$$ LISTA DE LIBROS POR IDIOMA");
            System.out.println(seleccionIdioma);

            String lang = sc.nextLine();
            libros = bookRepository.findByLanguageContains(lang);
            if (libros.isEmpty()) {
                System.out.println("No se encontró ningun liibro para el IDIOMA seleccionado");
            } else {
                libros.stream()
                        .sorted(Comparator.comparing(LibroEntity::getTitle))
                        .forEach(System.out::println);
            }
        }
    }
}


