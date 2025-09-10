package ar.edu.itba.pod.concurrency.iii.inmutable;

import java.util.Date;  // no es inmutable
import java.util.List;

// Queremos lograr que la clase sea inmutable -- aunque seria impractico pues nuestro sistema necesitaria q se modifiquen los datos...
public class Subscriber {
    // Ponerle final a las variables de instancia vuelve immutable AL PUNTERO de la variable de instancia,
    // pero no evita que usemos metodos del objeto para modificar al objeto.
    private  final Integer id;
    private  final String fullName;
    private  final Date dateOfBirth;
    private  final List<Subscription> subscriptions;

    // Si quisiera que la clase sea inmutable, es peligroso recibir la lista por constructor y guardar el puntero!
    // Estoy copiando un puntero a la lista, lo cual implica
    // que fuera de la definicion de la clase se tiene acceso al puntero. Estoy expuesto a que se mute la lista.
    public Subscriber(Integer id, String fullName, Date dateOfBirth,  List<Subscription> subscriptions) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.subscriptions = subscriptions;
    }

    public Integer getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }
}

