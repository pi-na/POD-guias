Evalúe indicando si son correctos o no para funcionar en un ambiente concurrente, explicando la manera por la cual se puede provocar un error, excepción, inconsistencia o mal uso además de proveer una solución si no lo fueran.

# item a)
```java
public class CountingFactorizer implements Servlet {
    private Long count = 0L;
    
    @Override
    public void service(ServletRequest req, ServletResponse resp) {
        synchronized (count) {
            BigInteger i = extractFromRequest(req);
            BigInteger[] factors = factor(i);
            ++count;
            encodeIntoResponse(resp, factors);
        }
    }

    public Long getCount() {
        synchronized (count) {
            return count;
        }
    }
}
```
No es thread-safe. Por autoboxing, al modificar el valor de count, se esta creando una nueva instancia de la variable.
Entonces, se crea un nuevo monitor interno para la variable. Si un thread obtiene acceso al bloque synch, al hacer ++count,
esta "soltando" el lock sobre `Long count` accidentalmente. Para resolverlo, podriamos reemplazar el bloque synchronized
por definir metodos synchronized tipo `public synchronized void service()`.  


# item b)
```java
public class ExpensiveObjectFactory {
    private ExpensiveObject instance = null;

    // Es un singleton: a todos les retorno la misma instancia.
    public ExpensiveObject getInstance() {
        if (instance == null) {
            instance = new ExpensiveObject(); // tarda mucho en construir
        }
        return instance;
    }
}
```
No es segura para concurrencia. Podria pasar que dos threads hagan un getInstance() cuando todavia instance estaba en null,
causando que se instancie dos veces el expensiveObject(). Ademas, puede pasar que dos threads que llamaron en simultaneo
a getInstance() obtengan dos instancias distintas del expensiveObject().
Para resolverlo reemplazamos en getInstance() el codigo del if:
```java
synchronized(ExpensiveObjectFactory.class){
        if(instance == null){
            instance = new ExpensiveObject(); // tarda mucho en construir
        }
    }
```
`Atencion!` Hay costo de conseguir el lock en cada ejecucion del metodo getInstance. Podemos hacer un chequeo doble:
```java
if(instance == null){
    synchronized(ExpensiveObjectFactory.class){
        if(instance == null){
            instance = new ExpensiveObject(); // tarda mucho en construir
            }
        }
    }
```


# item c)
```java
public class Account {
private double balance;
private int id;

    public void withdraw(double amount) {
        balance -= amount;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public static void transfer(Account from, Account to, double amount) {
        synchronized (from) {
            synchronized (to) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }
}
```
No es seguro para un ambiente concurrente.  
Si se llama a withdraw o deposit directamente, podria pasar que se llame de manera concurrente a withdraw() o deposit() de
una misma Account. Asi, los distintos threads podrian conseguir un valor de balance que quede desactualizado a la hora de 
realizar la suma o resta de dinero al balance, y luego sobreescribir el valor en memoria con ese valor desactualizado.  
Analizando el metodo transfer, vemos que tiene riesgo de caer en un deadlock: Pide 2 locks en sucesion sin criterio
de soltar el primero ('from') si se encuentra con que alguien mas tiene el lock de 'to' (_Hold & Wait_). En la practica,
se podria causar un deadlock en una situacion en la que se quiere hacer una transferencia 'cruzada', donde A le transfiere 
a B, y B le transfiere a A.


Intentemos resolverlo:

```java
public class Account {
    private double balance;
    private int id;

    // Usa de monitor "this", la instancia de la clase Account
    public synchronized void withdraw(double amount) {
        balance -= amount;
    }

    // Usa de monitor "this", la instancia de la clase Account
    public synchronized void deposit(double amount) {
        balance += amount;
    }
    //(...)
}
```
La catedra no toma en cuenta el caso en el que se llame a withdraw() o deposit() fuera de transfer().
Para resolver el deadlock, podriamos asegurarnos que los lock se obtienen siempre en el mismo orden:
```java 
public class Account {
    public static void transfer(Account from, Account to, double amount) {
        Account min = (from.id < to.id) ? from : to;
        Account max = (from.id < to.id) ? to : from;
        synchronized (min) {
            synchronized (max) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }
}
```
Combinando ambos; Los metodos withdraw() y deposit() usan synchronized para tomar el monitor/lock de la instancia
de cada Account, y el synchronized(from) o synchronized(to) tambien toma el lock de instancia. Esto funciona pues al
usar synchronized, actua como un lock RE-ENTRANTE, i.e. si se hace synchronized sobre un lock ya adquirido te deja pasar.
```java

public class Account {
    private double balance;
    private int id;
    // Usa de monitor "this", la instancia de la clase Account
    public synchronized void withdraw(double amount) {
        balance -= amount;
    }

    // Usa de monitor "this", la instancia de la clase Account
    public synchronized void deposit(double amount) {
        balance += amount;
    }
    
    public static void transfer(Account from, Account to, double amount) {
        Account min = (from.id < to.id) ? from : to;
        Account max = (from.id < to.id) ? to : from;
        
        synchronized (min) {
            synchronized (max) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }
}
```


# itemd d) 
```java
public class MovieTicketsAverager {
    private int tope;
    private List<Movie> movies;

    public MovieTicketsAverager(int tope, List<Movie> movies) {
        super();
        this.tope = tope;
        this.movies = new ArrayList<>(movies);
    }

    public double average() {
        return movies.stream()
                     .filter(movie -> movie.getYear() > tope)
                     .collect(Collectors.averagingInt(Movie::getTicketsSold));
    }
}

class Movie {
    private int year;
    private String title;
    private int ticketsSold;

    public Movie(int year, String title, int ticketsSold) {
        super();
        this.year = year;
        this.title = title;
        this.ticketsSold = ticketsSold;
    }

    public int getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }
}
```
No es correcto para un ambiente concurrente. Debido a que los dos campos de
la clase MovieTicketsAverager (tope, movies), son públicos, por lo que se puede
tener un thread calculando el promedio y otro modificando tanto la lista de películas
como el tope. La solución sería pasar la visibilidad de los campos a privata y proveer
métodos de acceso donde se pueda sincronizar el valor y la lista.