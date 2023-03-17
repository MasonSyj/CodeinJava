package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExampleDBTests {

    private DBServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    public void setup() {
        server = new DBServer();
    }

    // Random name generator - useful for testing "bare earth" queries (i.e. where tables don't previously exist)
    private String generateRandomName()
    {
        String randomName = "";
        for(int i=0; i<10 ;i++) randomName += (char)( 97 + (Math.random() * 25.0));
        return randomName;
    }

    private String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
        "Server took too long to respond (probably stuck in an infinite loop)");
    }

    // A basic test that creates a database, creates a table, inserts some test data, then queries it.
    // It then checks the response to see that a couple of the entries in the table are returned as expected
    @Test
    public void testBasicCreateAndQuery() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        String response = sendCommandToServer("SELECT * FROM marks;");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        assertTrue(response.contains("Steve"), "An attempt was made to add Steve to the table, but they were not returned by SELECT *");
        assertTrue(response.contains("Clive"), "An attempt was made to add Clive to the table, but they were not returned by SELECT *");
    }

    // A test to make sure that querying returns a valid ID (this test also implicitly checks the "==" condition)
    // (these IDs are used to create relations between tables, so it is essential that they work !)
    @Test
    public void testQueryID() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        String response = sendCommandToServer("SELECT id FROM marks WHERE name == 'Steve';");
        // Convert multi-lined responses into just a single line
        String singleLine = response.replace("\n"," ").trim();
        // Split the line on the space character
        String[] tokens = singleLine.split(" ");
        // Check that the very last token is a number (which should be the ID of the entry)
        String lastToken = tokens[tokens.length-1];
        try {
            Integer.parseInt(lastToken);
        } catch (NumberFormatException nfe) {
            fail("The last token returned by `SELECT id FROM marks WHERE name == 'Steve';` should have been an integer ID, but was " + lastToken);
        }
    }

    // A test to make sure that databases can be reopened after server restart
    @Test
    public void testTablePersistsAfterRestart() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        // Create a new server object
        server = new DBServer();
        sendCommandToServer("USE " + randomName + ";");
        String response = sendCommandToServer("SELECT * FROM marks;");
        System.out.println("-----------S L------------");
        System.out.println(response);
        assertTrue(response.contains("Steve"), "Steve was added to a table and the server restarted - but Steve was not returned by SELECT *");
        System.out.println("---------test selct wildAttributeList-----------");
        response = sendCommandToServer("Select name, pass from marks;");
        assertTrue((response.contains("name")));
        assertTrue((response.contains("pass")));
        assertTrue(!(response.contains("mark")));
    }

    // Test to make sure that the [ERROR] tag is returned in the case of an error (and NOT the [OK] tag)
    @Test
    public void testForErrorTag() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        String response = sendCommandToServer("SELECT * FROM libraryfines;");
        assertTrue(response.contains("[ERROR]"), "An attempt was made to access a non-existent table, however an [ERROR] tag was not returned");
        assertFalse(response.contains("[OK]"), "An attempt was made to access a non-existent table, however an [OK] tag was returned");
    }

    @Test
    public void testDropCommand(){
        String randomName = generateRandomName();
        System.out.println(randomName);
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("CREATE TABLE units (name, points, category);");

        String response = sendCommandToServer("DROP table marks;");
        assertTrue(response.contains("[OK]"));

        response = sendCommandToServer("DROP table marks;");
        assertTrue(response.contains("[ERROR]"));

//        response = sendCommandToServer("DROP table units;");
//        assertTrue(response.contains("[OK]"));

        response = sendCommandToServer("DROP DATABASE " + randomName + ";");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("DROP DATABASE " + randomName + ";");
        assertTrue(response.contains("[ERROR]"));

//        randomName = generateRandomName();
//        sendCommandToServer("CREATE DATABASE " + randomName + ";");
//        sendCommandToServer("USE " + randomName + ";");
//        sendCommandToServer("CREATE TABLE sports (name, category, expenses);");

    }

    @Test
    public void testAlterCommand(){
        String randomName = generateRandomName();
        System.out.println(randomName);
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        String response = sendCommandToServer("ALTER TABLE marks add rank;");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("ALTER TABLE marks add rank;");
        assertTrue(response.contains("[ERROR]"));

        response = sendCommandToServer("ALTER TABLE marks drop pass;");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("ALTER TABLE marks drop pass;");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("ALTER TABLE marks drop UnknownName;");
        assertTrue(response.contains("[ERROR]"));
    }

    @Test
    public void testSelectAllWithConditions() {
        String randomName = generateRandomName();
        System.out.println(randomName);
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate40', Huawei, 5000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate50', Huawei, 5500);");
        sendCommandToServer("SELECT * FROM marks where Price > 6000;");
        System.out.println("-----------Separate Line-----------");
        sendCommandToServer("SELECT * FROM marks where Brand like Huawei;");
    }

    @Test
    public void testUpdate(){
        String randomName = generateRandomName();
        System.out.println(randomName);
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate40', Huawei, 5000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate50', Huawei, 5500);");
        String response = sendCommandToServer("update marks set Price = 10000, Brand = HUAWEINB where Brand like Huawei;");
        assertTrue(response.contains("[OK]"));
    }

    @Test
    public void testSelectWildWithConditions() {
        String randomName = generateRandomName();
        System.out.println(randomName);
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate40', Huawei, 5000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate50', Huawei, 5500);");
        System.out.println("-----------Separate Line-----------");
        sendCommandToServer("SELECT Name, Brand FROM marks where Price > 6000 and Brand like Apple;");
        System.out.println("-----------Separate Line-----------");
        sendCommandToServer("SELECT Name, Price FROM marks where Brand like Huawei;");
    }

    @Test
    public void testDeleteWithCondition(){
        String randomName = generateRandomName();
        System.out.println(randomName);
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate40', Huawei, 5000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate50', Huawei, 5500);");

        FileDealer fd = new FileDealer(randomName, "marks");
        Table table = fd.file2Table();

        assertTrue(table.getNumofItems() == 5);

        String response = sendCommandToServer("DELETE FROM marks Where Price < 5500;");
        assertTrue(response.contains("[OK]"));
        System.out.println(table.getNumofItems());
        // what the....
        table = fd.file2Table();
        assertTrue(table.getNumofItems() == 4);

    }

    @Test
    public void testTableUpdateClass(){
        Table t = new Table(".", "PhoneBrand");
        t.addNewColumn(new Column("Name"));
        t.addNewColumn(new Column("Brand"));
        t.addNewColumn(new Column("Price"));

        List<String> value1 = new ArrayList<String>();
        value1.add("Pixel 7");
        value1.add("Google");
        value1.add("6000");
        t.addValue(value1);

        List<String> value2 = new ArrayList<String>();
        value2.add("Iphone 13");
        value2.add("Apple");
        value2.add("8000");

        t.addValue(value2);

        List<String> value3 = new ArrayList<String>();
        value3.add("Xiaomi 10");
        value3.add("Xiaomi");
        value3.add("7000");
        t.addValue(value3);

        t.updateFile();

        assertTrue(t.getNumofItems() == 3);

        String newvalu1 = "Huawei\tMate40\t5000";
        List<String> ref = new ArrayList<String>();
        ref.add(newvalu1);
        t.updateClass(ref);

        assertTrue(t.getNumofItems() == 1);

    }

    @Test
    public void testListStringAndOrOperation(){
        String a = "Apple 5 UK";
        String b = "Pear 6 France";
        String c = "Banana 4 German";
        String d = "Orange 9 Italy";


        List<String> first = Arrays.asList(a, b, c);
        List<String> second = Arrays.asList(a, b, d);

        FuncTest2 test = new FuncTest2();
        List<String> res1 = test.and(first, second);
        System.out.println(res1.toString());

        List<String> res2 = test.or(first, second);
        System.out.println(res2.toString());
    }


}



//    String DBname = "DigitalDevice";
//        String TableName = "Phones";
//        sendCommandToServer("CREATE DATABASE " + DBname + ";");
//        sendCommandToServer("USE " + DBname + ";");
//        sendCommandToServer("CREATE TABLE marks (name, mark, pass);")
