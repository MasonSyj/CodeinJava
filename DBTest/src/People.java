public class People {
    private static int nextId = 1;

    private int id;
    private String name;
    private int age;
    private String email;

    private static void increment(){
        nextId++;
    }

    public People(int id, String name, int age, String email){
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public static People lineParser(String line){
        int id = 0;
        String name = "";
        int age = 18;
        String email = "null@null";
        int stage = 0;
        int left = 0;
        for (int i = 0; i < line.length(); i++){
            if (line.charAt(i) == '\t'){
                if (stage == 0){
                    id = Integer.parseInt(line.substring(0, i));
                }else if (stage == 1){
                    name = line.substring(left + 1, i);
                }else if (stage == 2){
                    age = Integer.parseInt(line.substring(left + 1, i));
                    email = line.substring(i + 1, line.length());
                    break;
                }
                stage++;
                left = i;
            }
        }
        return new People(id, name, age, email);
    }

    public String toDefaultString(){
        return this.id + "\t" + this.name + "\t" + this.age + "\t" + this.email;
    }

    public void setRandomage() {
        this.age = (int) (Math.random() * 100);
    }

    public boolean idLookUp(String line, int id){
        int thisid = 0;
        for (int i = 0; i < line.length(); i++){
            if (line.charAt(i) == '\t'){
                thisid = Integer.parseInt(line.substring(0, i));
                break;
            }
        }


        if (thisid == id){
            return true;
        }else{
            return false;
        }

    }
}
