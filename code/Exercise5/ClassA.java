/**
 * @author Louis-Gabriel CAPLIEZ (EdgeOfMemory-cloud), Valere BILLAUD, ESIR 2 Spe INFO, option SI, Groupe 1
 * @date 20221109
 */

package fr.istic.vv;

public class ClassA {

	private int attributA;
	
	public int attributA_no_getter;
	
	private int attributB;
	
	protected int attributC;
	
    private static final long serialVersionUID = 5826987063535505652L;
    
	public final static int attributA_bis = 12;
	
	
	public ClassA() {
		attributA = 0;
		attributA_no_getter = 0;
		attributB= 0;
		attributC = 0;
	}

	public int getAttributA() {
		return attributA;
	}

	public void setAttributA(int attributA) {
		this.attributA = attributA;
	}

	public int getAttributB() {
		return attributB;
	}

	public void setAttributB(int attributB) {
		this.attributB = attributB;
	}

	public int getAttributC() {
		return attributC;
	}

	public void setAttributC(int attributC) {
		this.attributC = attributC;
	}
	
	public void meth1() {	
		attributC =1;
	}
	
	public void meth2() {	
	}
	
	public void meth3() {	
		meth2();
		meth4();
	}
	
	public void meth4() {
		meth2();
		meth3();
	}
	
	public void meth5() {	
		meth2();
		meth3();
		meth4();
	}
	
	public void meth6() {	
		meth2();
		meth3();
		meth4();
		meth5();
	}
	
	public void meth7() {	
		meth7();
	}
	
	public void meth8() {	
		meth3();
	}
	
	public void meth9() {	
		attributC =1;
	}
	
	public void meth10() {	
		attributC =1;
	}
	
	public void meth11() {	
		attributC =1;
	}
	
	public void meth12() {	
		attributC =1;
	}
	
	public void meth13() {	
	}
	
	public void meth14() {	
	}
	
	
	
}
