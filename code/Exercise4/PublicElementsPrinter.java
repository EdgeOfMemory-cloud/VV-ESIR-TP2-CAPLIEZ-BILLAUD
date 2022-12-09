/**
 * @author Louis-Gabriel CAPLIEZ (EdgeOfMemory-cloud), Valere BILLAUD, ESIR 2 Spe INFO, option SI, Groupe 1
 * @date 20221109
 */

package fr.istic.vv;

import java.util.ArrayList;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import java.io.PrintWriter;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

	private ArrayList<String> nameAtt;

	private ArrayList<String> nameMeth;

	private PrintWriter writer;


	public PublicElementsPrinter(PrintWriter writer) {
		super();
		nameAtt = new ArrayList<>();
		nameMeth = new ArrayList<>();
		this.writer = writer;

	}

	@Override
	public void visit(CompilationUnit unit, Void arg) {
		for(TypeDeclaration<?> type : unit.getTypes()) {
			type.accept(this, null);
		}
	}

	public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
		for(FieldDeclaration method : declaration.getFields()) {
			method.accept(this, arg);
		}
		writer.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
		for(MethodDeclaration method : declaration.getMethods()) {
			method.accept(this, arg);
		}

		// Printing nested types in the top level
		for(BodyDeclaration<?> member : declaration.getMembers()) {
			if (member instanceof TypeDeclaration)
				member.accept(this, arg);
		}

	}

	@Override
	public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
		this.reboot();
		visitTypeDeclaration(declaration, arg);
		this.compareGet();
	}

	@Override
	public void visit(EnumDeclaration declaration, Void arg) {

		visitTypeDeclaration(declaration, arg);
	}

	@Override
	public void visit(MethodDeclaration declaration, Void arg) {
		if(!declaration.isPublic()) return;
		String tmp = declaration.toString() ;
		String[] tab_tmp = tmp.split(" ");
		for (int i =0 ; i< tab_tmp.length ; i++ ) {
			if (tab_tmp[i].length() > 3 && tab_tmp[i].substring(0, 3).equals("get") && !tab_tmp[i].contains(".") && tab_tmp[i].contains("()")) {
				int index_par = tab_tmp[i].indexOf('(') ;
				tmp = tab_tmp[i].substring(0, index_par);
				nameMeth.add(tmp.toLowerCase());
				break;        		
			}
		}
	}

	@Override
	public void visit(FieldDeclaration declaration, Void arg) {
		if(declaration.isPublic() || declaration.isProtected()) return;
		String tmp = declaration.toString() ;
		String[] tab_tmp = tmp.split(" ");
		for (int i =0 ; i< tab_tmp.length ; i++ ) {
			if (tab_tmp[i].charAt(tab_tmp[i].length() -1) == ';') {
				tmp = tab_tmp[i].substring(0, tab_tmp[i].length() -1 );
				nameAtt.add(tmp);
				break;
			}
			if (tab_tmp[i].equals("=")) {
				tmp = tab_tmp[i-1];
				nameAtt.add(tmp);
				break;
			}
		}
	}

	/* Methode reboot pour reinitialiser les deux tableaus en paramètres.
	 * 
	 */
	private void reboot() {
		while (! this.nameAtt.isEmpty()) {
			nameAtt.remove(0);
		}
		while (! this.nameMeth.isEmpty()) {
			nameMeth.remove(0);
		}
	}

	private void compareGet() {
		String sortie = "";

		for(String attribut : nameAtt) {
			if ( ! nameMeth.contains("get" + attribut.toLowerCase())) {
				sortie = sortie + "\n" + "L'attribut " + attribut + " est prive et n'a pas d'accesseur." ; 
			}
			writer.println(sortie);
		}
	}
}
