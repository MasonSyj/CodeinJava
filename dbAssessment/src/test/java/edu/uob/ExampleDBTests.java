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
        System.out.println(randomName);
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
        System.out.println(response);
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
        System.out.println("-----------Condition 1------------");
        response = sendCommandToServer("SELECT * FROM marks;");
        System.out.println(response);
        System.out.println("-----------Condition 2------------");
        response = sendCommandToServer("SELECT * FROM marks where pass like TRUE;");
        System.out.println(response);
        assertTrue(response.contains("Steve"), "Steve was added to a table and the server restarted - but Steve was not returned by SELECT *");
        System.out.println("-----------Condition 3------------");
        response = sendCommandToServer("Select pass, name from marks;");
        System.out.println(response);
        assertTrue((response.contains("name")));
        assertTrue((response.contains("pass")));
        assertFalse((response.contains("mark")));
        System.out.println("-----------Condition 4------------");
        response = sendCommandToServer("Select pass, name from marks where mark > 50;");
        System.out.println(response);
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
        String reponse = sendCommandToServer("CREATE DATABASE " + randomName + ";");
        assertTrue(reponse.contains("[OK]"));
        reponse = sendCommandToServer("CREATE DATABASE " + randomName + ";");
        assertTrue(reponse.contains("[ERROR]"));

        sendCommandToServer("USE " + randomName + ";");
        reponse = sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        assertTrue(reponse.contains("[OK]"));
        reponse = sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        assertTrue(reponse.contains("[ERROR]"));

        sendCommandToServer("CREATE TABLE unit (name, points, number);");
        String randomName2 = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName2 + ";");
        sendCommandToServer("USE " + randomName2 + ";");
        sendCommandToServer("CREATE TABLE phone (name, brand, price);");
        sendCommandToServer("CREATE TABLE laptop (name, brand, usage, function);");
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
        assertTrue(Parser.getCurrentDBName().equals(randomName2));

        sendCommandToServer("USE " + randomName + ";");
        assertTrue(Parser.getCurrentDBName().equals(randomName));
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

        response = sendCommandToServer("DROP DATABASE " + randomName + ";");
        System.out.println("1: " + response);
        assertTrue(response.contains("[OK]"));

        response = sendCommandToServer("DROP DATABASE " + randomName + ";");
        System.out.println("2: " + response);
        assertTrue(response.contains("[ERROR]"));
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
        System.out.println(response);
        assertTrue(response.contains("[ERROR]"));

        response = sendCommandToServer("ALTER TABLE marks drop pass;");
        assertTrue(response.contains("[OK]"));
        response = sendCommandToServer("ALTER TABLE marks drop pass;");
        assertTrue(response.contains("[ERROR]"));
        response = sendCommandToServer("ALTER TABLE marks drop UnknownName;");
        assertTrue(response.contains("[ERROR]"));
    }

    @Test
    public void testInsert(){
        String randomName = generateRandomName();
        System.out.println(randomName);
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
        String response = sendCommandToServer("update marks set Price =10000, Brand= HUAWEINB where Brand like Huawei;");
        assertTrue(response.contains("[OK]"));
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
        String response = sendCommandToServer("SELECT Name, Brand FROM marks where Price >=6000 and Brand like Apple;");
        System.out.println(response);
        assertTrue(response.contains("Apple"));
        System.out.println("-----------Separate Line-----------");
        response = sendCommandToServer("SELECT Name, Price FROM marks where Brand like Huawei or Price<= 6000;");
        System.out.println(response);
        assertTrue(response.contains("Xiaomi"));
        assertFalse(response.contains("Huawei"));
    }
    @Test
    public void testJoin(){
        String randomName = generateRandomName();
        System.out.println(randomName);
        String response;
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        response = sendCommandToServer("USE " + randomName + ";");
        System.out.println(response);
        sendCommandToServer("CREATE TABLE customers (Customid, name, email);");
        sendCommandToServer("CREATE TABLE orders (Orderid, order_date, cost);");
        sendCommandToServer("INSERT INTO customers VALUES (21, 'John Doe', 'johndoe@example.com');");
        sendCommandToServer("INSERT INTO customers VALUES (22, 'Jane Smith', 'janesmith@example.com');");
        sendCommandToServer("INSERT INTO customers VALUES (23, 'Bob Johnson', 'bjohnson@example.com');");
        response = sendCommandToServer("Select * from customers;");
        System.out.println(response);

        sendCommandToServer("INSERT INTO orders VALUES (21, '2022-01-01', 100);");
        sendCommandToServer("INSERT INTO orders VALUES (23, '2022-02-15', 75);");
        response = sendCommandToServer("JOIN customers AND orders ON orders.Orderid AND customers.Customid;");
        System.out.println(response);
    }

    @Test
    public void testDeleteWithCondition() throws interpException {
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

        String response = sendCommandToServer("select * from marks;");
        System.out.println(response);
        assertTrue(response.contains("Mate40"));

        sendCommandToServer("DELETE FROM marks Where Price<5500;");
        response = sendCommandToServer("select * from marks");
        assertFalse(response.contains("Mate40"));

        response = sendCommandToServer("select * from marks where Brand>Unknown;");
        System.out.println(response);
        assertFalse(response.contains("Mate40"));
        assertFalse(response.contains("Google"));
        assertFalse(response.contains("Apple"));

    }

    @Test
    public void testTableUpdateClass(){
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
    public void testEdgeCasesQuery(){
        List<String> tokens = Token.setup("pass==False id=>7 num=<5 mark > = 45 height > = 180 'height >= 180 height> =160 name == 'steve' name=='steve' name=='sam");
        for (String token: tokens){
            System.out.println(token);
        }
    }

    @Test
    public void testAutoId() {
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
        sendCommandToServer("delete from marks where Brand like Huawei;");
        sendCommandToServer("INSERT INTO marks VALUES ('galaxy23', Samsung, 6000);");
        String reponse = sendCommandToServer("select * from marks;");
        System.out.println(reponse);
        System.out.println("-----------------------");
        sendCommandToServer("delete from marks where Price >= 7000;");
        sendCommandToServer("INSERT INTO marks VALUES ('galaxy22', Samsung, 7000);");
        sendCommandToServer("INSERT INTO marks VALUES ('Xiaomi 10', Xiaomi, 6000);");
        reponse = sendCommandToServer("select * from marks;");
        System.out.println(reponse);
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
        System.out.println(response);
        assertTrue(response.contains("[OK]"));
    }

    @Test
    public void testWrongCondition() {
        String randomName = generateRandomName();
        System.out.println(randomName);
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
        System.out.println(response);

        response = sendCommandToServer("select * from marks where Price>=7000");
        assertTrue(response.contains("[ERROR] Query doesn't end with ';'"));
        System.out.println(response);

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
        System.out.println(sendCommandToServer(" JOIN grade and ref on mark and mark;"));
        sendCommandToServer("  Drop database delBeforeTest ;");
    }


}
