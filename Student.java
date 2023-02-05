/* A Student is a person who is studying on a programme. */

class Student extends Person {
    private String programme;

    Student(String surname0, String forenames0) {
        super(surname0, forenames0);
    }

    String programme() { return programme; }

    // TODO: check permission of caller
    void programme(String u) { programme = u; }

    // Override the username method to check that the username has
    // the right format for a student
    @Override
    void username(String u) {
        // TODO: add format check
        super.username(u);
    }

}
