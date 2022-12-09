/**
 * @author Louis-Gabriel CAPLIEZ (EdgeOfMemory-cloud), Valere BILLAUD, ESIR 2 Spe INFO, option SI, Groupe 1
 * @date 20221109
 */

package fr.istic.vv;

public class ClassD {
	
	private int att1 = 1;
	
	
	public void meth1() {	
		att1 = 1;
		meth3();
		meth4();
		
	}
	
	public void meth2() {
		att1 = 1;
		meth3();
		meth4();
	}
	
	public void meth3() {	
		meth1();
		meth2();
		meth4();
	}
	
	public void meth4() {	
		meth1();
		meth2();
		meth3();
	}
}
