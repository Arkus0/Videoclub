package Pelicula;

import java.util.*;

class Pelicula {
    private String titulo;

    public Pelicula(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    @Override
    public String toString() {
        return titulo;
    }
}

class Usuario {
    private String nombre;
    private int id;
    private List<Pelicula> peliculasAlquiladas = new ArrayList<>();

    public Usuario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void alquilarPelicula(Pelicula pelicula) {
        peliculasAlquiladas.add(pelicula);
    }

    public void devolverPelicula(String titulo) {
        peliculasAlquiladas.removeIf(p -> p.getTitulo().equalsIgnoreCase(titulo));
    }

    public List<Pelicula> getPeliculasAlquiladas() {
        return peliculasAlquiladas;
    }

    @Override
    public String toString() {
        String peliculasStr = peliculasAlquiladas.isEmpty() ? "Ninguna" : peliculasAlquiladas.toString();
        return "N° Socio: " + id + " | Nombre: " + nombre + " | Películas alquiladas: " + peliculasStr;
    }
}

class Videoclub {
    private Map<Integer, Usuario> usuarios = new HashMap<>();
    private List<Pelicula> peliculas = new ArrayList<>();
    private int contadorUsuarios = 1;
    private Scanner scanner = new Scanner(System.in);

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
            }
        }
    }

    public void agregarUsuario() {
        while (true) {
            System.out.print("Ingrese el nombre del usuario: ");
            String nombre = scanner.nextLine();
            usuarios.put(contadorUsuarios, new Usuario(contadorUsuarios, nombre));
            System.out.println("Usuario agregado con éxito. Número de socio asignado: " + contadorUsuarios);
            contadorUsuarios++;

            System.out.print("¿Desea agregar otro usuario? (s/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("s")) {
                break;
            }
        }
    }

    public void eliminarUsuario() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        System.out.println("\nUsuarios registrados:");
        listaUsuarios();

        int id = leerEntero("Ingrese el número de socio a eliminar: ");
        if (usuarios.remove(id) != null) {
            System.out.println("Usuario eliminado.");
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    public void agregarPelicula() {
        while (true) {
            System.out.print("Ingrese título de la película: ");
            String titulo = scanner.nextLine();
            peliculas.add(new Pelicula(titulo));
            System.out.println("Película agregada con éxito.");

            System.out.print("¿Desea agregar otra película? (s/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("s")) {
                break;
            }
        }
    }

    public void eliminarPelicula() {
        if (peliculas.isEmpty()) {
            System.out.println("No hay películas registradas.");
            return;
        }

        System.out.println("\nPelículas disponibles:");
        listaPeliculas();

        System.out.print("Ingrese título de la película a eliminar: ");
        String titulo = scanner.nextLine();
        boolean eliminada = peliculas.removeIf(p -> p.getTitulo().equalsIgnoreCase(titulo));
        if (eliminada) {
            System.out.println("Película eliminada.");
        } else {
            System.out.println("Película no encontrada.");
        }
    }

    public void alquilarPelicula() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        System.out.println("\nUsuarios registrados:");
        listaUsuarios();

        int idUsuario = leerEntero("\nIngrese el número de socio: ");
        Usuario usuario = usuarios.get(idUsuario);
        if (usuario == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        if (peliculas.isEmpty()) {
            System.out.println("No hay películas disponibles.");
            return;
        }

        System.out.println("\nPelículas disponibles:");
        listaPeliculas();

        System.out.print("\nIngrese el título de la película que desea alquilar: ");
        String titulo = scanner.nextLine();

        Pelicula peliculaSeleccionada = peliculas.stream()
                .filter(p -> p.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);

        if (peliculaSeleccionada != null) {
            usuario.alquilarPelicula(peliculaSeleccionada);
            peliculas.remove(peliculaSeleccionada);
            System.out.println("Película alquilada con éxito.");
        } else {
            System.out.println("Película no encontrada.");
        }
    }

    public void devolverPelicula() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        System.out.println("\nUsuarios registrados:");
        listaUsuarios();

        int idUsuario = leerEntero("Ingrese el número de socio: ");
        Usuario usuario = usuarios.get(idUsuario);
        if (usuario == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        if (usuario.getPeliculasAlquiladas().isEmpty()) {
            System.out.println("El usuario no tiene películas alquiladas.");
            return;
        }

        System.out.println("\nPelículas alquiladas por " + usuario.getNombre() + ":");
        for (Pelicula p : usuario.getPeliculasAlquiladas()) {
            System.out.println("- " + p.getTitulo());
        }

        System.out.print("\nIngrese el título de la película que desea devolver: ");
        String titulo = scanner.nextLine();

        Pelicula peliculaDevuelta = usuario.getPeliculasAlquiladas().stream()
                .filter(p -> p.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);

        if (peliculaDevuelta != null) {
            usuario.devolverPelicula(titulo);
            peliculas.add(peliculaDevuelta);
            System.out.println("Película devuelta con éxito.");
        } else {
            System.out.println("El usuario no tiene esa película alquilada.");
        }
    }

    public void listaUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            usuarios.values().forEach(System.out::println);
        }
    }

    public void listaPeliculas() {
        if (peliculas.isEmpty() && usuarios.values().stream().allMatch(u -> u.getPeliculasAlquiladas().isEmpty())) {
            System.out.println("No hay películas registradas.");
            return;
        }

        System.out.println("\n--- Lista de Películas ---");
        Set<String> peliculasAlquiladas = new HashSet<>();
        usuarios.values().forEach(usuario -> 
            usuario.getPeliculasAlquiladas().forEach(pelicula -> peliculasAlquiladas.add(pelicula.getTitulo()))
        );

        for (Pelicula pelicula : peliculas) {
            System.out.println(pelicula.getTitulo() + " (Disponible)");
        }

        for (String titulo : peliculasAlquiladas) {
            System.out.println(titulo + " (Alquilada)");
        }
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ DEL VIDEOCLUB ---");
            System.out.println("1. Agregar usuario");
            System.out.println("2. Eliminar usuario");
            System.out.println("3. Agregar película");
            System.out.println("4. Eliminar película");
            System.out.println("5. Alquilar película");
            System.out.println("6. Devolver película");
            System.out.println("7. Lista usuarios");
            System.out.println("8. Lista películas");
            System.out.println("9. Salir del sistema");
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> agregarUsuario();
                case 2 -> eliminarUsuario();
                case 3 -> agregarPelicula();
                case 4 -> eliminarPelicula();
                case 5 -> alquilarPelicula();
                case 6 -> devolverPelicula();
                case 7 -> listaUsuarios();
                case 8 -> listaPeliculas();
                case 9 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 9);
    }
}

public class Main {
    public static void main(String[] args) {
        Videoclub videoclub = new Videoclub();
        videoclub.mostrarMenu();
    }
}


