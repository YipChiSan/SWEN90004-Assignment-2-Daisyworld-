import java.util.ArrayList;

public class test {

    private class Dog {
        public String name;
        public Integer age;

        public Dog(String name) {
            this.name = name;
        }
    }

    public void foo(){
        Dog dog = new Dog("Haha");
        Dog secondDog = new Dog("Wawa");
        ArrayList<Dog>another = new ArrayList<Dog>();
        another.add(dog);
        another.add(secondDog);
       
        ArrayList<Dog> dogList = new ArrayList<Dog>();
        Dog mydog = another.get(0);
        Dog mysecondDog = another.get(1);
        dogList.add(mydog);
        dogList.add(mysecondDog);
        dogList.get(0).name = "Changed";
        Integer a = 0;
        Integer b = a;
        b++;
        System.out.println(a);
    }
}