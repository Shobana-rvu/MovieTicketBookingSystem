import java.util.*;

// Movie class with language options
class Movie {
    private String title;
    private double rating;
    private List<String> languages;

    public Movie(String title, double rating, List<String> languages) {
        this.title = title;
        this.rating = rating;
        this.languages = languages;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public List<String> getLanguages() {
        return languages;
    }
}

// Theater class
class Theater {
    private String name;

    public Theater(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// Seat class
class Seat {
    private int seatNumber;
    private boolean booked;

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.booked = false;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isBooked() {
        return booked;
    }

    public void bookSeat() {
        this.booked = true;
    }

    public void cancelBooking() {
        this.booked = false;
    }
}

// User class
class User {
    private String name;
    private String contact;
    private String email;

    public User(String name, String contact, String email) {
        this.name = name;
        this.contact = contact;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }
}

// Bookable interface
interface Bookable {
    void bookTicket(User user, List<Integer> seatNumbers) throws SeatNotAvailableException, ShowFullException;
    void cancelTicket(User user, int seatNumber);
}

// Custom exception for unavailable seats
class SeatNotAvailableException extends Exception {
    public SeatNotAvailableException(String message) {
        super(message);
    }
}

// Custom exception for a full show
class ShowFullException extends Exception {
    public ShowFullException(String message) {
        super(message);
    }
}

// Abstract class Show with enhanced cancellation feature
abstract class Show implements Bookable {
    protected Movie movie;
    protected Theater theater;
    protected String[] showTimes;
    protected String showDate;
    protected String showDay;
    protected List<Seat> seats;
    protected int totalSeats;

    public Show(Movie movie, Theater theater, String[] showTimes, String showDate, String showDay, int totalSeats) {
        this.movie = movie;
        this.theater = theater;
        this.showTimes = showTimes;
        this.showDate = showDate;
        this.showDay = showDay;
        this.totalSeats = totalSeats;
        seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            seats.add(new Seat(i));
        }
    }

    public abstract double getTicketPrice();

    public void displayShowInfo() {
        System.out.println(theater.getName() + " - " + movie.getTitle());
        System.out.println("Available Seats: " + getAvailableSeats());
        System.out.println("Show Date: " + showDate);
        System.out.println("Show Day: " + showDay);
        System.out.println("Showtimes: ");
        for (int i = 0; i < showTimes.length; i++) {
            System.out.println((i + 1) + ". " + showTimes[i]);
        }
    }

    public int getAvailableSeats() {
        int count = 0;
        for (Seat seat : seats) {
            if (!seat.isBooked()) {
                count++;
            }
        }
        return count;
    }

    public String[] getShowTimes() {
        return showTimes;
    }

    @Override
    public void bookTicket(User user, List<Integer> seatNumbers) throws SeatNotAvailableException, ShowFullException {
        if (getAvailableSeats() < seatNumbers.size()) {
            throw new ShowFullException("Not enough seats available for your request.");
        }

        Set<Integer> bookedSeats = new HashSet<>();
        for (int seatNumber : seatNumbers) {
            if (seatNumber > totalSeats || seatNumber <= 0 || seats.get(seatNumber - 1).isBooked()) {
                throw new SeatNotAvailableException("Seat " + seatNumber + " is not available or already booked. Please choose different seats.");
            }
            if (bookedSeats.contains(seatNumber)) {
                throw new SeatNotAvailableException("Duplicate seat entry: Seat " + seatNumber + " already chosen for booking.");
            }
            bookedSeats.add(seatNumber);
        }

        for (int seatNumber : seatNumbers) {
            seats.get(seatNumber - 1).bookSeat();
        }

        System.out.println("Seats " + seatNumbers + " successfully booked for " + user.getName());
    }

    @Override
    public void cancelTicket(User user, int seatNumber) {
        if (seatNumber > totalSeats || seatNumber <= 0) {
            System.out.println("Seat " + seatNumber + " is out of range.");
        } else if (!seats.get(seatNumber - 1).isBooked()) {
            System.out.println("Seat " + seatNumber + " is not booked, cannot cancel.");
        } else {
            seats.get(seatNumber - 1).cancelBooking();
            System.out.println("Seat " + seatNumber + " booking canceled for " + user.getName());
        }
    }
    
    public void cancelAllTickets(User user) {
        for (Seat seat : seats) {
            if (seat.isBooked()) {
                seat.cancelBooking();
            }
        }
        System.out.println("All tickets canceled for " + user.getName());
    }
}

// RegularShow and PremiumShow classes with 2D and 3D options
class RegularShow extends Show {
    public RegularShow(Movie movie, Theater theater, String[] showTimes, String showDate, String showDay, int totalSeats) {
        super(movie, theater, showTimes, showDate, showDay, totalSeats);
    }

    @Override
    public double getTicketPrice() {
        return 150; // Example price for regular shows
    }
}

class PremiumShow extends Show {
    public PremiumShow(Movie movie, Theater theater, String[] showTimes, String showDate, String showDay, int totalSeats) {
        super(movie, theater, showTimes, showDate, showDay, totalSeats);
    }

    @Override
    public double getTicketPrice() {
        return 250; // Example price for premium shows
    }
}

// Payment class for processing payment
class Payment {
    public static void processPayment(double amount) {
        System.out.println("Payment of Rs." + amount + " processed successfully.");
    }
}

// Main class to run the application
public class MovieTicketBookingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<String> languages = Arrays.asList("Tamil", "English", "Kannada", "Hindi", "Malayalam", "Telugu");

        Movie[] movies = {
            new Movie("Bagheera", 4.6, languages),
            new Movie("Amaran", 4.8, languages),
            new Movie("Singham Again", 3.7, languages),
            new Movie("Bhool Bhulaiyaa 3", 3, languages),
            new Movie("Venom: The Last Dance", 4, languages),
            
        };
        Theater[] theaters = new Theater[10];
{theaters[0] = new Theater("Navrang");
theaters[1] = new Theater("Limeright Private Theatre");
theaters[2] = new Theater("Anjana");
theaters[3] = new Theater("Veeresh");
theaters[4] = new Theater("Gopalan");
theaters[5] = new Theater("PVR Premium");
theaters[6] = new Theater("Urvashi");
theaters[7] = new Theater("PVR MSR");
theaters[8] = new Theater("Victoria");
theaters[9] = new Theater("PVR - Nexus Mall");
        }
        String[][] showTimes = {
            {"10:00 AM", "01:00 PM", "04:00 PM"},
            {"11:00 AM", "02:00 PM", "05:00 PM"},
            {"12:00 PM", "03:00 PM", "06:00 PM"},
            {"01:00 PM", "04:00 PM", "07:00 PM"},
            {"10:30 PM", "06:15 PM", "08:45 PM"},
            
        };

        Show[] regularShows = new Show[5];
        Show[] premiumShows = new Show[5];
        for (int i = 0; i < 5; i++) {
            regularShows[i] = new RegularShow(movies[i], theaters[i], showTimes[i], "2024-12-01", "Sunday", 78);
            premiumShows[i] = new PremiumShow(movies[i], theaters[i + 5], showTimes[i], "2024-12-01", "Sunday", 78);
        }

        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter your contact number: ");
        String contact = scanner.nextLine();
        System.out.print("Enter your email ID: ");
        String email = scanner.nextLine();
        User user = new User(userName, contact, email);

        System.out.println("Available Movies:");
        for (int i = 0; i < movies.length; i++) {
            System.out.println((i + 1) + ". " + movies[i].getTitle() + " - Ratings " + movies[i].getRating());
        
        System.out.println("Languages available: " + String.join(", ", movies[i].getLanguages()));

        }
        System.out.print("Choose a movie (1-5): ");
        int movieChoice = scanner.nextInt();

                System.out.println("Available Theaters:");
        for (int i = 0; i < theaters.length; i++) {
            System.out.println((i + 1) + ". " + theaters[i].getName());
        }
        System.out.print("Choose a theater (1-10): ");
        int theaterChoice = scanner.nextInt();

        Show chosenShow;
        if (theaterChoice <= 5) {
            chosenShow = regularShows[movieChoice - 1];
        } else {
            chosenShow = premiumShows[movieChoice - 1];
        }

        System.out.println("Available Languages:");
        List<String> movieLanguages = movies[movieChoice - 1].getLanguages();
        for (int i = 0; i < movieLanguages.size(); i++) {
            System.out.println((i + 1) + ". " + movieLanguages.get(i));
        }
        System.out.print("Choose a language: ");
        int languageChoice = scanner.nextInt();
        String chosenLanguage = movieLanguages.get(languageChoice - 1);

        System.out.println("Available Show Dates and Days:");
        System.out.println("1. 2024-12-01 (Sunday)");
        System.out.println("2. 2024-12-02 (Monday)");
        System.out.print("Choose a date and day (1-2): ");
        int dateDayChoice = scanner.nextInt();
        String showDate = dateDayChoice == 1 ? "2024-12-01" : "2024-12-02";
        String showDay = dateDayChoice == 1 ? "Sunday" : "Monday";

        System.out.println("Available Viewing Options:");
        System.out.println("1. 2D");
        System.out.println("2. 3D (if available)");
        System.out.print("Choose a viewing option: ");
        int viewOption = scanner.nextInt();
        String chosenViewOption = (viewOption == 1) ? "2D" : "3D";

        System.out.println("Available Show Times:");
        for (int i = 0; i < chosenShow.getShowTimes().length; i++) {
            System.out.println((i + 1) + ". " + chosenShow.getShowTimes()[i]);
        }
        System.out.print("Choose a show time (1-3): ");
        int showTimeChoice = scanner.nextInt();
        String chosenTime = chosenShow.getShowTimes()[showTimeChoice - 1];

        System.out.println("Ticket Price: Rs." + chosenShow.getTicketPrice());
       // System.out.print("Enter the number of seats you want to book: ");
        //int numSeats = scanner.nextInt();
        //List<Integer> seatNumbers = new ArrayList<>();

        System.out.println("Available Seats: ");
        for (Seat seat : chosenShow.seats) {
            if (!seat.isBooked()) {
                System.out.print(seat.getSeatNumber() + " ");
            }
        }
        System.out.print("Enter the number of seats you want to book: ");
        int seatCount = scanner.nextInt();
        List<Integer> seatNumbers = new ArrayList<>();
        for (int i = 0; i < seatCount; i++) {
            while (true) {
                System.out.print("Enter seat number for seat " + (i + 1) + ": ");
                int seatNumber = scanner.nextInt();
                if (!seatNumbers.contains(seatNumber)) {
                    seatNumbers.add(seatNumber);
                    break;
                } else {
                    System.out.println("You have already chosen this seat. Please choose a different seat.");
                }
            }
        }
        try {
            chosenShow.bookTicket(user, seatNumbers);
            Payment.processPayment(chosenShow.getTicketPrice() * seatCount);
            System.out.println("Booking confirmed for " + user.getName() + "!");
            System.out.println("Movie: " + movies[movieChoice - 1].getTitle());
            System.out.println("Theater: " + theaters[theaterChoice - 1].getName());
            System.out.println("Date: " + showDate);
            System.out.println("Day: " + showDay);
            System.out.println("Language: " + chosenLanguage);
            System.out.println("Viewing Option: " + chosenViewOption);
            System.out.println("Show Time: " + chosenTime);
            System.out.println("Seats Booked: " + seatNumbers);
        } catch (SeatNotAvailableException | ShowFullException e) {
            System.out.println(e.getMessage());
        }
System.out.print("Do you want to cancel any booked seats? (yes/no): ");
scanner.nextLine(); // consume newline
String cancelChoice = scanner.nextLine();

if (cancelChoice.equalsIgnoreCase("yes")) {
    System.out.print("Enter the number of seats to cancel: ");
    int numSeatsToCancel = scanner.nextInt();
    
    if (numSeatsToCancel > seatNumbers.size()) {
        System.out.println("Error: You cannot cancel more seats than you booked.");
    } else {
        List<Integer> seatsToCancel = new ArrayList<>();
        for (int i = 0; i < numSeatsToCancel; i++) {
            System.out.print("Enter seat number to cancel (seat " + (i + 1) + "): ");
            int seatNumber = scanner.nextInt();
            
            if (seatNumbers.contains(seatNumber) && !seatsToCancel.contains(seatNumber)) {
                seatsToCancel.add(seatNumber);
            } else {
                System.out.println("Error: Seat " + seatNumber + " is either not booked or already selected for cancellation.");
                i--; // repeat this iteration for a val
            }
        }
        
        for (int seatNumber : seatsToCancel) {
            chosenShow.cancelTicket(user, seatNumber);
            seatNumbers.remove((Integer) seatNumber); // remove from booked seats list
        }
        
        System.out.println("Cancellation completed for seats: " + seatsToCancel);
    }
}

        scanner.close();
    }
}
