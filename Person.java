/* A Person object holds some personal details about someone. */

class Person {
    final String surname, forenames;
    private String username;

    Person(String surname0, String forenames0) {
        surname = surname0;
        forenames = forenames0;
    }

    String username() { return username; }

    // TODO: check permission of caller
    void username(String u) { username = u; }
}
