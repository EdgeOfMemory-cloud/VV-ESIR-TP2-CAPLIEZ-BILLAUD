/**
 * @author Louis-Gabriel CAPLIEZ (EdgeOfMemory-cloud), Valere BILLAUD, ESIR 2 Spe INFO, option SI, Groupe 1
 * @date 20221109
 */

package fr.istic.vv;

import java.io.PrintWriter;
import java.util.ArrayList;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

	private int mode = 1;
	private int conteur = 0 ;
	private ArrayList<String> nameMeth;
	private ArrayList<String> nameAtt;
	private String[] attCom;
	private boolean[][] liaison;
	private PrintWriter writer;


	public PublicElementsPrinter(PrintWriter writer) {
		super();
		nameMeth = new ArrayList<>();
		nameAtt = new ArrayList<>();
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
		if(!declaration.isPublic()) return;
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
		reboot();
		visitTypeDeclaration(declaration, arg);
		liaison = new boolean[conteur][conteur];
		for(int i = 0 ; i < conteur  ; i++) {
			for(int j = 0 ; i < conteur  ; i++) {
				liaison[i][j] = (false);
			}
		}
		conteur = 0;
		mode = 0 ;
		visitTypeDeclaration(declaration, arg);
		mode = 2;


		conteur = 0;

		attCom = new String[nameAtt.size()];
		for (int i = 0 ; i<nameAtt.size() ; i++) {
			attCom[i] = "";
		}
		visitTypeDeclaration(declaration, arg);


		result();
	}

	@Override
	public void visit(EnumDeclaration declaration, Void arg) {
		visitTypeDeclaration(declaration, arg);
	}

	@Override
	public void visit(MethodDeclaration declaration, Void arg) {
		String tmp = declaration.getDeclarationAsString(true, true);
		if(mode == 1) {
			conteur ++;
			String[] tab_tmp = tmp.split(" ") ;
			for(String x : tab_tmp) {
				if(x.contains("(")) {
					String meth = x.substring(0, x.indexOf("(")+1) ;
					nameMeth.add(meth);
					break;
				}
			}
		} 
		if (mode == 0){
			conteur ++;
			for(int x = 0 ; x < nameMeth.size() ; x ++) {
				if( declaration.toString().contains(nameMeth.get(x))) {
					int index = ((x + 1) * conteur) ;
					liaison[conteur-1][x] = true;

				}
			}
		}

		if (mode == 2) {
			conteur ++;
			for(int x = 0 ; x < nameAtt.size() ; x ++) {
				if( declaration.toString().contains(nameAtt.get(x))) {
					attCom[x] += (String.valueOf(conteur) + ",");  
				}
			}
		}

	}

	@Override
	public void visit(FieldDeclaration declaration, Void arg) {
		if( mode == 1) {

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
	}

	public void reboot() {
		conteur = 0 ;
		while (! nameMeth.isEmpty()) {
			nameMeth.remove(0) ;
		}
		while (! nameAtt.isEmpty()) {
			nameAtt.remove(0) ;
		}
		mode = 1;
	}

	public void result() {
		int allConnection = nameMeth.size() * (nameMeth.size() - 1) ;
		for (String x : attCom) {

			if (!x.equals(" ")) {
				String[] tmp = x.split(",");
				for (String i : tmp) {
					for (String j : tmp) {

						if (!i.equals("") && !j.equals("")) {
							int index1 = Integer.parseInt((String) i) - 1;
							int index2 = Integer.parseInt(j)-1;
							liaison[index1][index2]= true; 
							liaison[index2][index1]= true; 
						}
					}
				}
			}
		}
		conteur =   - nameMeth.size();
		for (boolean[] x : liaison) {
			for (boolean i : x) {
				if(i) {
					conteur ++;
				}
			}
		}
		if (allConnection != 0) {
			writer.println("Le tcc est de " + conteur + "/" + allConnection + ".");
		} else {
			writer.println("Il n'y a qu'une seule methode donc le tcc est de 1.");
		}
	}

}