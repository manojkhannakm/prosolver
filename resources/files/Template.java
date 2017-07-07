public class Template {

    //Template

    public static final int INT = 10;

    public static void main(String[] args) {
        System.out.println(Template.INT);

        Template template = new Template();

        String s = "Template";
    }

    public static Template template() {
        return null;
    }

    public static class D {

        public static Template template() {
            return null;
        }

    }

    public class A {


        public class B {

        }

    }

    public class C {

    }

}
