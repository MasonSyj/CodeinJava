/* A Staff is a person who has a job. */

class Staff extends Person {
    private String job;

    Staff(String surname0, String forenames0) {
        super(surname0, forenames0);
    }

    String job() { return job; }

    // TODO: check permission of caller
    void job(String u) { job = u; }
}
