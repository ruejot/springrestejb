package wildfly;

public class testingRef {
	public static void main(String[] args) {
        A a1 = new A(1);
        System.out.println("a1:" + a1.getN()); // a1:1

        modify(a1);

        System.out.println("a1:" + a1.getN()); // a1:3
    }

    static void modify(A a) {
        a.setN(3);
        System.out.println("a:" + a.getN()); // a:3
    }
}

	class A {

	    private int n;

	    public A(int n) {
	        this.n = n;
	    }

	    public int getN() {
	        return n;
	    }

	    public void setN(int n) {
	        this.n = n;
	    }
}
