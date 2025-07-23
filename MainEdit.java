import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

// Abstract base class for all vehicles (Abstraction)
abstract class Vehicle {
    protected String vehicleId;
    protected String brand;
    protected String model;
    protected double basePricePerDay;
    protected boolean isAvailable;
    protected VehicleType type;

    public Vehicle(String vehicleId, String brand, String model, double basePricePerDay, VehicleType type) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
        this.type = type;
    }

    // Abstract method to be implemented by subclasses (Polymorphism)
    public abstract double calculatePrice(int rentalDays);

    public abstract String getVehicleInfo();

    public abstract double getLateFee();

    // Common methods for all vehicles
    public String getVehicleId() {
        return vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public VehicleType getType() {
        return type;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnVehicle() {
        isAvailable = true;
    }
}

// Enum for vehicle types (Better organization)
enum VehicleType {
    ECONOMY_CAR, LUXURY_CAR, SUV, MOTORCYCLE, TRUCK
}

// Interface for special features (Interface Segregation)
interface LuxuryFeatures {
    boolean hasGPS();

    boolean hasSunroof();

    boolean hasLeatherSeats();

    double getLuxuryFee();
}

interface InsuranceProvider {
    double getInsuranceCost(int days);

    String getInsuranceType();
}

// Economy Car class (Inheritance)
class EconomyCar extends Vehicle implements InsuranceProvider {
    private double fuelEfficiency;

    public EconomyCar(String vehicleId, String brand, String model, double basePricePerDay, double fuelEfficiency) {
        super(vehicleId, brand, model, basePricePerDay, VehicleType.ECONOMY_CAR);
        this.fuelEfficiency = fuelEfficiency;
    }

    @Override
    public double calculatePrice(int rentalDays) {
        double basePrice = basePricePerDay * rentalDays;
        // Economy cars get 10% discount for rentals over 7 days
        if (rentalDays > 7) {
            basePrice *= 0.9;
        }
        return basePrice;
    }

    @Override
    public String getVehicleInfo() {
        return String.format("Economy Car: %s %s (Fuel Efficiency: %.1f km/l)", brand, model, fuelEfficiency);
    }

    @Override
    public double getLateFee() {
        return 20.0; // $20 per day late fee
    }

    @Override
    public double getInsuranceCost(int days) {
        return days * 5.0; // $5 per day for basic insurance
    }

    @Override
    public String getInsuranceType() {
        return "Basic Insurance";
    }

    public double getFuelEfficiency() {
        return fuelEfficiency;
    }
}

// Luxury Car class (Inheritance + Interface Implementation)
class LuxuryCar extends Vehicle implements LuxuryFeatures, InsuranceProvider {
    private boolean gps;
    private boolean sunroof;
    private boolean leatherSeats;

    public LuxuryCar(String vehicleId, String brand, String model, double basePricePerDay,
            boolean gps, boolean sunroof, boolean leatherSeats) {
        super(vehicleId, brand, model, basePricePerDay, VehicleType.LUXURY_CAR);
        this.gps = gps;
        this.sunroof = sunroof;
        this.leatherSeats = leatherSeats;
    }

    @Override
    public double calculatePrice(int rentalDays) {
        double basePrice = basePricePerDay * rentalDays;
        basePrice += getLuxuryFee() * rentalDays;
        // Luxury cars get 5% discount for rentals over 14 days
        if (rentalDays > 14) {
            basePrice *= 0.95;
        }
        return basePrice;
    }

    @Override
    public String getVehicleInfo() {
        return String.format("Luxury Car: %s %s (GPS: %s, Sunroof: %s, Leather: %s)",
                brand, model, gps ? "Yes" : "No", sunroof ? "Yes" : "No", leatherSeats ? "Yes" : "No");
    }

    @Override
    public double getLateFee() {
        return 50.0; // $50 per day late fee for luxury cars
    }

    @Override
    public boolean hasGPS() {
        return gps;
    }

    @Override
    public boolean hasSunroof() {
        return sunroof;
    }

    @Override
    public boolean hasLeatherSeats() {
        return leatherSeats;
    }

    @Override
    public double getLuxuryFee() {
        double fee = 0;
        if (gps)
            fee += 10;
        if (sunroof)
            fee += 15;
        if (leatherSeats)
            fee += 20;
        return fee;
    }

    @Override
    public double getInsuranceCost(int days) {
        return days * 15.0; // $15 per day for premium insurance
    }

    @Override
    public String getInsuranceType() {
        return "Premium Insurance";
    }
}

// SUV class (Inheritance)
class SUV extends Vehicle implements InsuranceProvider {
    private int seatingCapacity;
    private boolean fourWheelDrive;

    public SUV(String vehicleId, String brand, String model, double basePricePerDay,
            int seatingCapacity, boolean fourWheelDrive) {
        super(vehicleId, brand, model, basePricePerDay, VehicleType.SUV);
        this.seatingCapacity = seatingCapacity;
        this.fourWheelDrive = fourWheelDrive;
    }

    @Override
    public double calculatePrice(int rentalDays) {
        double basePrice = basePricePerDay * rentalDays;
        if (fourWheelDrive) {
            basePrice += 25 * rentalDays; // $25 extra per day for 4WD
        }
        return basePrice;
    }

    @Override
    public String getVehicleInfo() {
        return String.format("SUV: %s %s (%d seats, 4WD: %s)",
                brand, model, seatingCapacity, fourWheelDrive ? "Yes" : "No");
    }

    @Override
    public double getLateFee() {
        return 35.0; // $35 per day late fee
    }

    @Override
    public double getInsuranceCost(int days) {
        return days * 10.0; // $10 per day for standard insurance
    }

    @Override
    public String getInsuranceType() {
        return "Standard Insurance";
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public boolean hasFourWheelDrive() {
        return fourWheelDrive;
    }
}

// Customer hierarchy (Inheritance)
abstract class Customer {
    protected String customerId;
    protected String name;
    protected String email;
    protected CustomerType type;

    public Customer(String customerId, String name, String email, CustomerType type) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.type = type;
    }

    public abstract double getDiscount();

    public abstract int getMaxRentalDays();

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public CustomerType getType() {
        return type;
    }
}

enum CustomerType {
    REGULAR, PREMIUM, VIP
}

class RegularCustomer extends Customer {
    public RegularCustomer(String customerId, String name, String email) {
        super(customerId, name, email, CustomerType.REGULAR);
    }

    @Override
    public double getDiscount() {
        return 0.0; // No discount for regular customers
    }

    @Override
    public int getMaxRentalDays() {
        return 30; // Max 30 days rental
    }
}

class PremiumCustomer extends Customer {
    private int loyaltyPoints;

    public PremiumCustomer(String customerId, String name, String email, int loyaltyPoints) {
        super(customerId, name, email, CustomerType.PREMIUM);
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public double getDiscount() {
        return 0.1; // 10% discount for premium customers
    }

    @Override
    public int getMaxRentalDays() {
        return 60; // Max 60 days rental
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }
}

// Rental class with composition
class Rental {
    private Vehicle vehicle;
    private Customer customer;
    private int days;
    private LocalDate startDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private double totalCost;
    private boolean insuranceIncluded;

    public Rental(Vehicle vehicle, Customer customer, int days, boolean insuranceIncluded) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.days = days;
        this.startDate = LocalDate.now();
        this.expectedReturnDate = startDate.plusDays(days);
        this.insuranceIncluded = insuranceIncluded;
        calculateTotalCost();
    }

    private void calculateTotalCost() {
        double baseCost = vehicle.calculatePrice(days);
        double discount = customer.getDiscount() * baseCost;
        totalCost = baseCost - discount;

        // Add insurance cost if applicable
        if (insuranceIncluded && vehicle instanceof InsuranceProvider) {
            InsuranceProvider provider = (InsuranceProvider) vehicle;
            totalCost += provider.getInsuranceCost(days);
        }
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public boolean isInsuranceIncluded() {
        return insuranceIncluded;
    }

    public void setActualReturnDate(LocalDate returnDate) {
        this.actualReturnDate = returnDate;
    }

    public double calculateLateFee() {
        if (actualReturnDate != null && actualReturnDate.isAfter(expectedReturnDate)) {
            long lateDays = expectedReturnDate.until(actualReturnDate).getDays();
            return lateDays * vehicle.getLateFee();
        }
        return 0.0;
    }
}

// Enhanced Car Rental System
class CarRentalSystem {
    private List<Vehicle> vehicles;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        vehicles = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, int days, boolean includeInsurance) {
        if (vehicle.isAvailable() && days <= customer.getMaxRentalDays()) {
            vehicle.rent();
            Rental rental = new Rental(vehicle, customer, days, includeInsurance);
            rentals.add(rental);

            // Add loyalty points for premium customers
            if (customer instanceof PremiumCustomer) {
                ((PremiumCustomer) customer).addLoyaltyPoints(days);
            }
        } else {
            System.out.println("Vehicle is not available or rental period exceeds customer limit");
        }
    }

    public void returnVehicle(Vehicle vehicle) {
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getVehicle() == vehicle) {
                rentalToRemove = rental;
                break;
            }
        }

        if (rentalToRemove != null) {
            rentalToRemove.setActualReturnDate(LocalDate.now());
            double lateFee = rentalToRemove.calculateLateFee();

            rentals.remove(rentalToRemove);
            vehicle.returnVehicle();

            System.out.println("Vehicle returned successfully");
            if (lateFee > 0) {
                System.out.printf("Late fee charged: $%.2f%n", lateFee);
            }
        } else {
            System.out.println("Vehicle was not rented.");
        }
    }

    public void displayAvailableVehicles() {
        System.out.println("\nAvailable Vehicles:");
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable()) {
                System.out.println(vehicle.getVehicleId() + " - " + vehicle.getVehicleInfo());
            }
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n====Enhanced Car Rental System====");
            System.out.println("1. Rent a Vehicle");
            System.out.println("2. Return a Vehicle");
            System.out.println("3. View Available Vehicles");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    rentVehicleMenu(scanner);
                    break;
                case 2:
                    returnVehicleMenu(scanner);
                    break;
                case 3:
                    displayAvailableVehicles();
                    break;
                case 4:
                    System.out.println("\nThank you for using the Enhanced Car Rental System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void rentVehicleMenu(Scanner scanner) {
        System.out.println("\n== Rent a Vehicle ==");
        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.println("Customer Type: 1-Regular, 2-Premium");
        System.out.print("Enter customer type: ");
        int customerType = scanner.nextInt();
        scanner.nextLine();

        Customer customer;
        String customerId = "CUS" + (customers.size() + 1);

        if (customerType == 2) {
            customer = new PremiumCustomer(customerId, customerName, email, 0);
        } else {
            customer = new RegularCustomer(customerId, customerName, email);
        }
        addCustomer(customer);

        displayAvailableVehicles();

        System.out.print("\nEnter the vehicle ID you want to rent: ");
        String vehicleId = scanner.nextLine();

        System.out.print("Enter the number of days for rental: ");
        int rentalDays = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Include insurance? (Y/N): ");
        String insuranceChoice = scanner.nextLine();
        boolean includeInsurance = insuranceChoice.equalsIgnoreCase("Y");

        Vehicle selectedVehicle = null;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId) && vehicle.isAvailable()) {
                selectedVehicle = vehicle;
                break;
            }
        }

        if (selectedVehicle != null && rentalDays <= customer.getMaxRentalDays()) {
            displayRentalInfo(selectedVehicle, customer, rentalDays, includeInsurance);

            System.out.print("\nConfirm rental (Y/N): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                rentVehicle(selectedVehicle, customer, rentalDays, includeInsurance);
                System.out.println("\nVehicle rented successfully!");
            } else {
                System.out.println("\nRental cancelled.");
            }
        } else {
            System.out.println("\nInvalid vehicle selection, not available, or rental period exceeds limit.");
        }
    }

    private void displayRentalInfo(Vehicle vehicle, Customer customer, int days, boolean includeInsurance) {
        System.out.println("\n== Rental Information ==");
        System.out.println("Customer: " + customer.getName() + " (" + customer.getType() + ")");
        System.out.println("Vehicle: " + vehicle.getVehicleInfo());
        System.out.println("Rental Days: " + days);

        double baseCost = vehicle.calculatePrice(days);
        double discount = customer.getDiscount() * baseCost;
        double finalCost = baseCost - discount;

        System.out.printf("Base Cost: $%.2f%n", baseCost);
        if (discount > 0) {
            System.out.printf("Discount: -$%.2f%n", discount);
        }

        if (includeInsurance && vehicle instanceof InsuranceProvider) {
            InsuranceProvider provider = (InsuranceProvider) vehicle;
            double insuranceCost = provider.getInsuranceCost(days);
            finalCost += insuranceCost;
            System.out.printf("Insurance (%s): +$%.2f%n", provider.getInsuranceType(), insuranceCost);
        }

        System.out.printf("Total Cost: $%.2f%n", finalCost);
    }

    private void returnVehicleMenu(Scanner scanner) {
        System.out.println("\n== Return a Vehicle ==");
        System.out.print("Enter the vehicle ID to return: ");
        String vehicleId = scanner.nextLine();

        Vehicle vehicleToReturn = null;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId) && !vehicle.isAvailable()) {
                vehicleToReturn = vehicle;
                break;
            }
        }

        if (vehicleToReturn != null) {
            returnVehicle(vehicleToReturn);
        } else {
            System.out.println("Invalid vehicle ID or vehicle is not rented.");
        }
    }
}

public class MainEdit {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        // Adding different types of vehicles
        rentalSystem.addVehicle(new EconomyCar("E001", "Toyota", "Corolla", 45.0, 18.5));
        rentalSystem.addVehicle(new EconomyCar("E002", "Honda", "Civic", 50.0, 17.0));
        rentalSystem.addVehicle(new LuxuryCar("L001", "BMW", "X5", 120.0, true, true, true));
        rentalSystem.addVehicle(new LuxuryCar("L002", "Mercedes", "E-Class", 150.0, true, false, true));
        rentalSystem.addVehicle(new SUV("S001", "Ford", "Explorer", 85.0, 7, true));
        rentalSystem.addVehicle(new SUV("S002", "Jeep", "Wrangler", 95.0, 5, true));

        rentalSystem.menu();
    }
};
