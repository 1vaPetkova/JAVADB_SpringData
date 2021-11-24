package softuni.exam.models.dto.view;

public class PassengerViewDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long ticketsCount;


    public PassengerViewDto(String firstName, String lastName, String email, String phoneNumber, Long ticketsCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ticketsCount = ticketsCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getTicketsCount() {
        return ticketsCount;
    }

    public void setTicketsCount(Long ticketsCount) {
        this.ticketsCount = ticketsCount;
    }

    @Override
    public String toString() {
        return String.format("Passenger %s %s\n" +
                "\tEmail - %s\n" +
                "Phone - %s\n" +
                "\tNumber of tickets - %d\n", this.firstName, this.lastName, this.email, this.phoneNumber, this.ticketsCount);
    }
}
