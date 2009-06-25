package it.unibo.lmc.pjdbc.utils;

import alice.tuprolog.InvalidTermException;
import alice.tuprolog.Number;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Var;

public class TypesPrologList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			
			System.out.println("\\begin{table}[ht]");
			System.out.println("\\caption(Prolog Types)");
			System.out.println("\\centering");
			System.out.println("\\begin{tabular}{c c c c c c c c}");
			System.out.println("\\hline");
			printHead();
			System.out.println("\\hline");
			
			Term elemento = Term.createTerm("a1");
			printDesc(elemento);
			
			Term elemento2 = Term.createTerm("1");
			printDesc(elemento2);
			
			Term array = Term.createTerm("[a,b]");
			printDesc(array);
			
			Term clausola = Term.createTerm("p(a,b)");
			printDesc(clausola);
			
			Term genericVar = Term.createTerm("_");
			printDesc(genericVar);
			
			Term aVar = Term.createTerm("A");
			printDesc(aVar);
			
			System.out.println("\\hline");
			System.out.println("\\end{tabular}");
			System.out.println("\\end{table}");
			
			
		} catch (InvalidTermException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	private static void printHead() {
		
		StringBuilder build = new StringBuilder();
		
		build.append("Term");
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( "Struct" );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( "Var" );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( "Number" );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( "Atom" );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( "Atomic" );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( "Compound" );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( "List" );
//		build.append("\t");
//		build.append("|");
//		build.append("& ");
		
		System.out.println(build.toString());
		
	}

	private static void printDesc(Term e) {
		
		StringBuilder build = new StringBuilder();
		
		build.append(e.toString()+" ");
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( normalize(e instanceof Struct) );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( normalize(e instanceof Var) );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( normalize(e instanceof Number) );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( normalize(e.isAtom()) );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( normalize(e.isAtomic()) );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( normalize(e.isCompound()) );
//		build.append("\t");
//		build.append("|");
		build.append("& ");
		build.append( normalize(e.isList()) );
//		build.append("\t");
//		build.append("|");
//		build.append("& ");
		
		System.out.println(build.toString());
		
		
	}

	private static String normalize(boolean b) {
		if ( b ) return "true";
		return "-";
	}

}
