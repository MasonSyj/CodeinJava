package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import edu.uob.Enums.ItemType;
import edu.uob.Exceptions.interpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
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
        sendCommandToServer("INSERT INTO marks VALUES ('sam', 80, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('tom', 45, FALSE);");
        // Create a new server object
        server = new DBServer();
        sendCommandToServer("USE " + randomName + ";");
        String response;
        sendCommandToServer("SELECT * FROM marks;");
        response = sendCommandToServer("SELECT * FROM marks where pass like TRUE;");
        assertFalse(response.contains("tom"));
        assertTrue(response.contains("Steve"), "Steve was added to a table and the server restarted - but Steve was not returned by SELECT *");
        response = sendCommandToServer("Select pass, name from marks;");
        assertTrue((response.contains("name")));
        assertTrue((response.contains("pass")));
        assertFalse((response.contains("mark")));
        response = sendCommandToServer("Select pass, name from marks where mark > 50;");
        assertFalse(response.contains("tom"));
        assertTrue(response.contains("Steve"));
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
    public void testCreate(){
        String randomName = generateRandomName();
        String reponse = sendCommandToServer("CREATE DATABASE . ;");
        assertTrue(reponse.contains("[ERROR]"));
        reponse = sendCommandToServer("CREATE DATABASE " + randomName + ";");
        assertTrue(reponse.contains("[OK]"));
        reponse = sendCommandToServer("CREATE DATABASE " + randomName + ";");
        assertTrue(reponse.contains("[ERROR]"));

        sendCommandToServer("USE " + randomName + ";");
        reponse = sendCommandToServer("CREATE TABLE blank;");
        assertTrue(reponse.contains("[OK]"));
        reponse = sendCommandToServer("CREATE TABLE empty ();");
        assertTrue(reponse.contains("[OK]"));
        reponse = sendCommandToServer("CREATE TABLE marks (name, mark, pass, pass);");
        assertTrue(reponse.contains("[ERROR] one table cannot have two same attribute name"));
        reponse = sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        assertTrue(reponse.contains("[OK]"));
        reponse = sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        assertTrue(reponse.contains("[ERROR]"));

        sendCommandToServer("CREATE TABLE unit (name, points, number);");
        String randomName2 = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName2 + ";");
        sendCommandToServer("USE " + randomName2 + ";");
        sendCommandToServer("CREATE TABLE phone (name, brand, price);");
        reponse = sendCommandToServer("CREATE TABLE laptop (name, brand, usage, function, select);");
        assertTrue(reponse.contains("[ERROR] table attribute cannot be SQL Keywords"));
    }
    @Test
    public void testUse(){
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        assertTrue(Parser.getCurrentDBName().equals(randomName));

        String randomName2 = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName2 + ";");
        sendCommandToServer("USE " + randomName2 + ";");
        // no place can show the db name, but in my code it's static in the Parser Class.
        assertTrue(Parser.getCurrentDBName().equals(randomName2));

        sendCommandToServer("USE " + randomName + ";");
        assertTrue(Parser.getCurrentDBName().equals(randomName));

        String response = sendCommandToServer("use " + generateRandomName() + ";");
        assertTrue(response.contains("[ERROR]"));
    }

    @Test
    public void testDropCommand(){
        String randomName = generateRandomName();
        String response = "";
        sendCommandToServer("CREATE DATABASE " + randomName + ";");

        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("CREATE TABLE units (name, points, category);");

        response = sendCommandToServer("DROP table marks;");
        assertTrue(response.contains("[OK]"));

        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        response = sendCommandToServer("DROP table marks;");
        assertTrue(response.contains("[OK]"));

        response = sendCommandToServer("DROP table marks;");
        assertTrue(response.contains("[ERROR]"));

        response = sendCommandToServer("DROP DATABASE " + randomName + ";");
        System.out.println(response);
        assertTrue(response.contains("[OK]"));

        response = sendCommandToServer("DROP DATABASE " + randomName + ";");
        assertTrue(response.contains("[ERROR]"));
    }

    @Test
    public void testAlterCommand(){
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        String response = sendCommandToServer("ALTER TABLE MARKS add rank;");
        System.out.println(response);
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("ALTER TABLE marks add Rank;");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("ALTER TABLE marks add rank;");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("ALTER TABLE marks add select;");
        assertTrue(response.contains("[ERROR]"));
        assertTrue(response.contains("attribute Name cannot be SQL Keywords."));

        response = sendCommandToServer("ALTER TABLE MARKS drop pass;");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("ALTER TABLE marks drop pass;");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("ALTER TABLE marks drop Pass;");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("ALTER TABLE marks drop UnknownName;");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("ALTER TABLE marks drop id;");
        assertTrue(response.contains("[ERROR], you can not drop auto id column"));
    }

    @Test
    public void testInsert(){
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        String response = sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("INSERT INTO marks VALUES (Xiaomi, 6000);");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("select * from marks;");
        assertTrue(response.contains("Google"));
        assertTrue(response.contains("Apple"));
        assertFalse(response.contains("Xiaomi"));
    }

    @Test
    public void testUpdate(){
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate40', Huawei, 5000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate50', Huawei, 5500);");
        sendCommandToServer("update marks set price =10000, brand= HW where brand like Huawei;");
        String response = sendCommandToServer("select * from marks;");
        System.out.println(response);
        assertTrue(response.contains("[OK]"));
        assertTrue(response.contains("HW"));
        assertFalse(response.contains("Huawei"));
    }

    @Test
    public void testSelectAllWithConditions() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate40', Huawei, 5000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate50', Huawei, 5500);");
        String response = "";
        response = sendCommandToServer("SELECT marks.brand, marks.price FROM marks where Price > 6000;");
        System.out.println(response);
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("SELECT marks.brand, noname.price FROM marks where Price > 6000;");
        System.out.println(response);
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("SELECT * FROM marks where Brand like Huawei and (Price > 5000 or Brand != Huawei);");
        System.out.println(response);
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("SELECT * FROM marks where Price > 5500 and (Brand like Google or Brand like Apple);");
        System.out.println(response);
        assertTrue(response.contains("[OK]"));

        response = sendCommandToServer("SELECT * FROM marks where Brand like Huawei and (Price > 5000 or Brand ! = Huawei);");
        System.out.println(response);
        assertTrue(response.contains("[ERROR]"));

        response = sendCommandToServer("SELECT * FROM marks where Price > 5500 or Brand like Google and Brand like Apple;");
        System.out.println(response);
        assertTrue(response.contains("[OK]"));
        assertTrue(response.contains("Apple"));
        assertFalse(response.contains("Huawei"));

    }
    @Test
    public void testSelectWildWithConditions() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate40', Huawei, 5000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate50', Huawei, 5500);");
        String response = sendCommandToServer("SELECT Name, Brand FROM marks where Price >=6000 and Brand like Apple;");
        assertTrue(response.contains("Apple"));
        assertFalse(response.contains("Mate"));
        response = sendCommandToServer("SELECT Name, Price FROM marks where Brand like Huawei or Price<= 6000;");
        assertTrue(response.contains("Xiaomi"));
        assertFalse(response.contains("Iphone 13"));
        assertFalse(response.contains("Huawei")); //didn't choose the column, so also not exist
    }
    @Test
    public void testJoin(){
        String randomName = generateRandomName();
        String response;
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        response = sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE customers (Customid, name, email);");
        sendCommandToServer("CREATE TABLE orders (Orderid, order_date, cost);");
        sendCommandToServer("INSERT INTO customers VALUES (21, 'John Doe', 'johndoe@example.com');");
        sendCommandToServer("INSERT INTO customers VALUES (22, 'Jane Smith', 'janesmith@example.com');");
        sendCommandToServer("INSERT INTO customers VALUES (23, 'Bob Johnson', 'bjohnson@example.com');");

        sendCommandToServer("INSERT INTO orders VALUES (21, '2022-01-01', 100);");
        sendCommandToServer("INSERT INTO orders VALUES (23, '2022-02-15', 75);");
        response = sendCommandToServer("JOIN customers AND orders ON orders.Orderid AND customers.Customid;");
        String response2 = sendCommandToServer("JOIN customers AND orders ON ORDERID AND customers.CUSTOMID;");
        System.out.println(response2);
        String response3 = sendCommandToServer("JOIN customers AND orders ON orders.Orderid AND Customid;");
        assertTrue(response.equals(response2));
        assertTrue(response2.equals(response3));

        response = sendCommandToServer("JOIN customers AND orders ON tom AND jerry;");
        assertTrue(response.contains("[ERROR] Join command can't find common thing to join"));
    }

    @Test
    public void testDeleteWithCondition() throws interpException {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate40', Huawei, 5000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate50', Huawei, 5500);");

        String response = sendCommandToServer("select * from marks;");
        assertTrue(response.contains("Mate40"));

        response = sendCommandToServer("DELETE FROM marks Where Price<5500;");
        System.out.println(response);
        response = sendCommandToServer("select * from marks;");
        assertFalse(response.contains("Mate40"));

        response = sendCommandToServer("select * from marks where Brand>Unknown;");
        assertFalse(response.contains("Mate40"));
        assertFalse(response.contains("Google"));
        assertFalse(response.contains("Apple"));

    }

    @Test
    public void testTableUpdateClass() throws interpException {
        Table t = new Table("Electronics", "PhoneBrand");
        File directory = new File("databases" + File.separator + "Electronics");
        if (!directory.exists()){
            assertTrue(directory.mkdir(), "unable to create directory as prerequisite work for this test");
        }

        t.addNewColumn("id");
        t.addNewColumn("Name");
        t.addNewColumn("Brand");
        t.addNewColumn("Price");

        List<String> value1 = new ArrayList<String>();
        value1.add("Pixel 7");
        value1.add("Google");
        value1.add("6000");
        t.addItem(value1, ItemType.NEW);

        List<String> value2 = new ArrayList<String>();
        value2.add("Iphone 13");
        value2.add("Apple");
        value2.add("8000");

        t.addItem(value2, ItemType.NEW);

        List<String> value3 = new ArrayList<String>();
        value3.add("Xiaomi 10");
        value3.add("Xiaomi");
        value3.add("7000");
        t.addItem(value3, ItemType.NEW);

        t.write2File();

        assertTrue(t.getNumofItems() == 3);

        String newvalu1 = "4\tHuawei\tMate40\t5000";
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
        String e = "Orange 9 Italy";

        List<String> first = Arrays.asList(a, b, c);
        List<String> second = Arrays.asList(a, b, d);

        List<String> minus = BoolOperation.AMinusB(first, second);
        assertTrue(minus.size() == 1);

        List<String> res1 = BoolOperation.and(first, second);
        assertTrue(res1.size() == 2);

        List<String> res2 = BoolOperation.or(first, second);
        assertTrue(res2.size() == 4);

        first = Arrays.asList(a, b, c, e);
        second = Arrays.asList(a, b, d, e);

        res1 = BoolOperation.and(first, second);
        assertTrue(res1.size() == 3);

        res2 = BoolOperation.or(first, second);
        assertTrue(res2.size() == 4);
    }

    @Test
    public void testConditionFunctions(){
        //test infix to suffix
        List<String> conditionTokens = new ArrayList<>();
        conditionTokens.add("Brand like APPLE");
        conditionTokens.add("and");
        conditionTokens.add("Price > 700");
        List<String> res = ConditionDealer.convertToSuffix(conditionTokens);
        assertTrue(res.get(2).equals("and"));

        conditionTokens.add("or");
        conditionTokens.add("Name like Huawei");
        res = ConditionDealer.convertToSuffix(conditionTokens);
        assertTrue(res.get(4).equals("or"));
    }

    @Test
    public void testAutoId() {
        String randomName = generateRandomName();
        String response = "";
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate40', Huawei, 5000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate50', Huawei, 5500);");
        sendCommandToServer("delete from marks where Brand like Huawei;");
        sendCommandToServer("INSERT INTO marks VALUES ('galaxy23', Samsung, 6000);");
        response = sendCommandToServer("select id from marks");
        assertFalse(response.contains("4"));
        assertFalse(response.contains("5"));
        sendCommandToServer("delete from marks where Price >= 7000;");
        sendCommandToServer("INSERT INTO marks VALUES ('galaxy22', Samsung, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        response = sendCommandToServer("select id from marks;");
        assertTrue(response.contains("7"));
        assertTrue(response.contains("8"));
    }

    //table db attribute name can't be keyword, and keyword is not case sensitive
    @Test
    public void testKeyWordsCase(){
        String response = sendCommandToServer("CREATE DATABASE " + "create" + ";");
        assertTrue(response.contains("[ERROR]"));

        String randomName = generateRandomName();
        response = sendCommandToServer("CREATE DATABASE " + randomName + ";");
        assertTrue(response.contains("[OK]"));
        sendCommandToServer("USE " + randomName + ";");

        response = sendCommandToServer("CREATE TABLE and (Name, Brand, Price);");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("CREATE TABLE marks (Name, or, Price);");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        assertTrue(response.contains("[OK]"));
    }

    @Test
    public void testWrongCondition() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        String response = sendCommandToServer("select * from marks where Price>=7000;");
        assertTrue(response.contains("Google"));
        assertTrue(response.contains("Apple"));
        assertFalse(response.contains("Xiami"));

        response = sendCommandToServer("select * from marks where Price> =7000;");
        assertTrue(response.contains("Query contains wrong comparator."));

        response = sendCommandToServer("select * from marks where Price>=7000");
        assertTrue(response.contains("[ERROR] Query doesn't end with ';'"));
    }

    @Test
    public void testJoin2(){
        String randomName = generateRandomName();
        sendCommandToServer("Create database " + randomName + " ;");
        sendCommandToServer("Use " + randomName + " ;");
        sendCommandToServer("Create table grade (name ,mark, resit);");
        sendCommandToServer("Insert into grade VALUES ('sam', 75, false);");
        sendCommandToServer("Insert into grade VALUES ('tom', 86, false);");
        sendCommandToServer("Insert into grade VALUES ('marry', 62, true);");
        sendCommandToServer("Insert into grade VALUES ('lucy', 60, true);");
        String response = sendCommandToServer("select * from grade;");
        assertTrue(response.contains("resit"));

        sendCommandToServer("Create table ref (mark ,subject, category);");
        sendCommandToServer("Insert into ref VALUES (25, 'EN', 1);");
        sendCommandToServer("Insert into ref VALUES (75, 'CN', 2);");
        sendCommandToServer("Insert into ref VALUES (86, 'MH', 3);");
        sendCommandToServer("Insert into ref VALUES (65, 'CS', 4);");
        sendCommandToServer("Insert into ref VALUES (62, 'EE', 3);");
        sendCommandToServer("Insert into ref VALUES (12, 'HR', 1);");
        response = sendCommandToServer(" JOIN grade and ref on mark and mark;");
        assertTrue(response.contains("resit"));
        assertTrue(response.contains("subject"));
        assertFalse(response.contains("mark"));
        assertFalse(response.contains("lucy"));

    }

    @Test
    public void testParserIssue(){
        String response = "";
        response = sendCommandToServer("Unknown operation here;");
        assertTrue(response.contains("[ERROR], Don't know what operation you want to do;"));

        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        response = sendCommandToServer("CREATE marks (Name, Brand, Price);");
        assertTrue(response.contains("[ERROR]"));
        sendCommandToServer("CREATE TABLE marks (Name, Brand, Price);");
        response = sendCommandToServer("INSERT marks VALUES ('Pixel 7', Google, 7000);");
        assertTrue(response.contains("INSERT cmd should come with a INTO"));
        response = sendCommandToServer("INSERT INTO marks ('Pixel 7', Google, 7000);");
        assertTrue(response.contains("INSERT cmd should come with a VALUES"));
        response = sendCommandToServer("INSERT INTO marks VALUES 'Pixel 7', Google, 7000);");
        assertTrue(response.contains("INSERT cmd should come with a ( after VALUES)"));
        response = sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000;");
        assertTrue(response.contains("ValueList didn't end correctly"));

        sendCommandToServer("INSERT INTO marks VALUES ('Pixel 7', Google, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Iphone 13', Apple, 8000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate40', Huawei, 5000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Mate50', Huawei, 5500);");
        response = sendCommandToServer("update marks Price = 7000;");
        assertTrue(response.contains("Update operation needs to have a set"));
        response = sendCommandToServer("update marks set Price = 7000 Brand like Huawei;");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("update marks set Price = 7000 where;");
        assertTrue(response.contains("Update operation must have condition(s)"));
        response = sendCommandToServer("update marks set Price 7000 where Brand like Huawei;");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("update marks set Price = where Brand like Huawei;");
        assertTrue(response.contains("NameValueList is incomplete"));

        response = sendCommandToServer("delete marks where Price > 7000;");
        assertTrue(response.contains("DELETE operation must come with a FROM"));
        response = sendCommandToServer("delete from marks Price > 7000;");
        assertTrue(response.contains("DELETE operation must hava a WHERE"));

        response = sendCommandToServer("alter marks add year;");
        assertTrue(response.contains("ALTER operation must come with a TABLE"));

        response = sendCommandToServer("alter table marks year;");
        assertTrue(response.contains("Alter operation doesn't know to do add or drop."));

        response = sendCommandToServer("drop marks;");
        assertTrue(response.contains("Drop Query needs to know it's database or table"));

        response = sendCommandToServer("select * marks;");
        assertTrue(response.contains("Select Query needs to have FROM"));

        response = sendCommandToServer("select Brand, Price marks;");
        assertTrue(response.contains("Select Query needs to have FROM"));

        response = sendCommandToServer("CREATE TABLE markii (id, year, level);");
        assertTrue(response.contains("[OK]"));

        response = sendCommandToServer("join marks and markii on Price and level;");
        assertTrue(response.contains("[OK]"));

        response = sendCommandToServer("join marks markii on Price and level;");
        assertTrue(response.contains("Join operation needs an AND after first tablename"));

        response = sendCommandToServer("join marks and markii Price and level;");
        assertTrue(response.contains("Join operation needs an ON after second tablename"));

        response = sendCommandToServer("join marks and markii on Price level;");
        assertTrue(response.contains("Join operation needs an AND after first Attribute name"));
    }


    //---------------------------------------------------------------------------
    //---------------Test for invalid command cause parser error-----------------
    //---------------------------------------------------------------------------
    @Test
    public void testForErrorParser() {
        String randomName = generateRandomName();
        String response;
        // missing ; at end
        response = sendCommandToServer("USE " + randomName);
        assertTrue(response.contains("[ERROR]"), "Command can't pass parser, however an [ERROR] tag was not returned");
        // wrong command keyword
        response = sendCommandToServer("vim DATABASE " + randomName + ";");
        assertTrue(response.contains("[ERROR]"), "Command can't pass parser, however an [ERROR] tag was not returned");
        response = sendCommandToServer("ALTER TABLE " + randomName + " XOR name;");
        assertTrue(response.contains("[ERROR]"), "Command can't pass parser, however an [ERROR] tag was not returned");
        // missing keyword
        response = sendCommandToServer("CREATE " + randomName + ";");
        assertTrue(response.contains("[ERROR]"), "Command can't pass parser, however an [ERROR] tag was not returned");
        response = sendCommandToServer("JOIN tableA AND tableB ON key1;");
        assertTrue(response.contains("[ERROR]"), "Command can't pass parser, however an [ERROR] tag was not returned");
        response = sendCommandToServer("INSERT INTO tableA VALUES 3,5,6;"); // missing brackets of value list
        assertTrue(response.contains("[ERROR]"), "Command can't pass parser, however an [ERROR] tag was not returned");
        response = sendCommandToServer("DELETE FROM marks name == 'Dave';");
        assertTrue(response.contains("[ERROR]"), "Command can't pass parser, however an [ERROR] tag was not returned");
    }

    @Test
    public void testParserInvalidPlainText() {
        String response;
        // Use reserved words as table/database/attribute name
        response = sendCommandToServer("USE TABLE;");
        assertTrue(response.contains("[ERROR]"), "Invalid plain text, however an [ERROR] tag was not returned");
        response = sendCommandToServer("CREATE TABLE drop;");
        assertTrue(response.contains("[ERROR]"), "Invalid plain text, however an [ERROR] tag was not returned");
        response = sendCommandToServer("CREATE TABLE test (first, USE);");
        assertTrue(response.contains("[ERROR]"), "Invalid plain text, however an [ERROR] tag was not returned");
        // Having symbols in table/database/attribute name
        response = sendCommandToServer("USE Sim29*(b;");
        assertTrue(response.contains("[ERROR]"), "Invalid plain text, however an [ERROR] tag was not returned");
        response = sendCommandToServer("CREATE TABLE *;");
        assertTrue(response.contains("[ERROR]"), "Invalid plain text, however an [ERROR] tag was not returned");
        response = sendCommandToServer("CREATE TABLE test (first, one_row);");
        System.out.println(response);
        assertTrue(response.contains("[ERROR]"), "Invalid plain text, however an [ERROR] tag was not returned");
    }

    @Test
    public void testParserInvalidAttribute() {
        String response;
        // Invalid attribute name
        response = sendCommandToServer("CREATE TABLE test (table.name.error);");
        assertTrue(response.contains("[ERROR]"), "Invalid attribute name, however an [ERROR] tag was not returned");
        response = sendCommandToServer("CREATE TABLE test (table..name);");
        assertTrue(response.contains("[ERROR]"), "Invalid attribute name, however an [ERROR] tag was not returned");
        // Invalid attribute list
        response = sendCommandToServer("CREATE TABLE marks (name, mark, );");
        assertTrue(response.contains("[ERROR]"), "Invalid attribute list, however an [ERROR] tag was not returned");
        response = sendCommandToServer("CREATE TABLE marks ();");
        assertTrue(response.contains("[ERROR]"), "Invalid attribute list, however an [ERROR] tag was not returned");
    }

    @Test
    public void testParserInvalidValue() {
        String response;
        // Invalid value name
        response = sendCommandToServer("INSERT INTO marks VALUES (Steve, 65, TRUE);");// StringLiteral without ''
        assertTrue(response.contains("[ERROR]"), "Invalid value name, however an [ERROR] tag was not returned");
        response = sendCommandToServer("INSERT INTO marks VALUES (3.2.2);");// Invalid FloatLiteral
        assertTrue(response.contains("[ERROR]"), "Invalid value name, however an [ERROR] tag was not returned");
        response = sendCommandToServer("INSERT INTO marks VALUES (XOR);");// Invalid BooleanLiteral
        assertTrue(response.contains("[ERROR]"), "Invalid value name, however an [ERROR] tag was not returned");
        // Invalid value list
        response = sendCommandToServer("INSERT INTO marks VALUES (, 65, TRUE);");
        assertTrue(response.contains("[ERROR]"), "Invalid value list, however an [ERROR] tag was not returned");
        response = sendCommandToServer("INSERT INTO marks VALUES ();");
        assertTrue(response.contains("[ERROR]"), "Invalid value list, however an [ERROR] tag was not returned");
    }

    @Test
    public void testParserInvalidCondition() {
        String response;
        // Invalid Comparator
        response = sendCommandToServer("SELECT * FROM marks WHERE name = 'Steve';");
        assertTrue(response.contains("[ERROR]"), "Invalid condition, however an [ERROR] tag was not returned");
        // Invalid BoolOperator
        response = sendCommandToServer("SELECT * FROM marks WHERE name == 'Steve' XOR mark > 65;");
        assertTrue(response.contains("[ERROR]"), "Invalid condition, however an [ERROR] tag was not returned");
        // Invalid Condition list
        response = sendCommandToServer("SELECT * FROM marks WHERE (name == 'Steve' OR mark > 65;");
        assertTrue(response.contains("[ERROR]"), "Invalid condition, however an [ERROR] tag was not returned");
        response = sendCommandToServer("SELECT * FROM marks WHERE name == 'Steve') OR mark > 65;");
        assertTrue(response.contains("[ERROR]"), "Invalid condition, however an [ERROR] tag was not returned");
        response = sendCommandToServer("SELECT * FROM marks WHERE name == 'Steve') ()OR mark( > 65;");
        assertTrue(response.contains("[ERROR]"), "Invalid condition, however an [ERROR] tag was not returned");
        response = sendCommandToServer("SELECT * FROM marks WHERE (name() == 'Steve') OR mark > 65;");
        assertTrue(response.contains("[ERROR]"), "Invalid condition, however an [ERROR] tag was not returned");
        response = sendCommandToServer("SELECT * FROM marks WHERE ();");
        assertTrue(response.contains("[ERROR]"), "Invalid condition, however an [ERROR] tag was not returned");
        response = sendCommandToServer("SELECT * FROM marks WHERE ;");
        assertTrue(response.contains("[ERROR]"), "Invalid condition, however an [ERROR] tag was not returned");
    }

    @Test
    public void testParserInvalidNameValueList() {
        String response;
        // Don't have space in NameValueList
        response = sendCommandToServer("UPDATE test SET mark==5 WHERE name == 'Steve';");
        assertTrue(response.contains("[ERROR]"), "Invalid NameValueList, however an [ERROR] tag was not returned");
        // Invalid NameValueList
        response = sendCommandToServer("UPDATE test SET mark WHERE name == 'Steve';");
        assertTrue(response.contains("[ERROR]"), "Invalid NameValueList, however an [ERROR] tag was not returned");
        response = sendCommandToServer("UPDATE test SET mark = 5 name = 'Bob' WHERE name == 'Steve';");
        assertTrue(response.contains("[ERROR]"), "Invalid NameValueList, however an [ERROR] tag was not returned");
        response = sendCommandToServer("UPDATE test SET mark = 5, name = 'Bob', WHERE name == 'Steve';");
        assertTrue(response.contains("[ERROR]"), "Invalid NameValueList, however an [ERROR] tag was not returned");
    }

}
