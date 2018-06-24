package main;

import ast.And;
import ast.Block;
import ast.BooleanType;
import ast.ClassDeclExtends;
import ast.ClassDeclList;
import ast.ClassDeclSimple;
import ast.Identifier;
import ast.IdentifierType;
import ast.IntegerLiteral;
import ast.IntegerType;
import ast.LessThan;
import ast.MainClass;
import ast.MethodDeclList;
import ast.Print;
import ast.Program;
import ast.StatementList;
import ast.VarDecl;
import ast.VarDeclList;
import ast.While;
import visitor.PrettyPrintVisitor;
import visitor.BuildSymbolTableVisitor;
import visitor.TypeCheckVisitor;

public class Main {

	public static void main(String[] args) {
		StatementList sl = new StatementList();
		Print ag = new Print(new IntegerLiteral(2));
		While w = new While(new And(new LessThan(new IntegerLiteral(2),new IntegerLiteral(1)),new LessThan(new IntegerLiteral(1),new IntegerLiteral(1))),ag);
		sl.addElement(w);
		sl.addElement(new Print(new IntegerLiteral(0)));
		Block b = new Block(sl);
		
		
		MainClass main = new MainClass(
				new Identifier("Teste"), 
				new Identifier("Testando"), 
				b);
		
		VarDeclList vdl1 = new VarDeclList();
		vdl1.addElement(new VarDecl(
			new BooleanType(),
			new Identifier("flag")
		));
		
		vdl1.addElement(new VarDecl(
				new IntegerType(),
				new Identifier("num")
		));
		
		MethodDeclList mdl = new MethodDeclList();
		
		ClassDeclSimple A = new ClassDeclSimple(
					new Identifier("A"), vdl1, mdl
		);
		
		ClassDeclExtends B = new ClassDeclExtends(
				new Identifier("B"), new Identifier("C"), 
				new VarDeclList(), new MethodDeclList()
		);
		
		ClassDeclExtends C = new ClassDeclExtends(
				new Identifier("C"), new Identifier("B"), 
				new VarDeclList(), new MethodDeclList()
		);
		
		VarDeclList vdl2 = new VarDeclList();
		vdl2.addElement(new VarDecl(
				new IdentifierType("C"),
				new Identifier("obj")
		));
		
		vdl2.addElement(new VarDecl(
				new IntegerType(),
				new Identifier("n")
		));
		
		VarDeclList vdl3 = new VarDeclList();
		vdl3.addElement(new VarDecl(
				new IntegerType(),
				new Identifier("numbers")
		));
		
		ClassDeclSimple D = new ClassDeclSimple(
				new Identifier("D"), vdl2, new MethodDeclList()
		);
		
		ClassDeclSimple E = new ClassDeclSimple(
				new Identifier("E"), vdl3,mdl
		);
		
		ClassDeclList cdl = new ClassDeclList();
		cdl.addElement(A);
		cdl.addElement(B);
		cdl.addElement(C);
		cdl.addElement(D);
		cdl.addElement(E);

		Program p = new Program(main, cdl);
		
		PrettyPrintVisitor ppv = new PrettyPrintVisitor();
		BuildSymbolTableVisitor bs = new BuildSymbolTableVisitor();
		bs.visit(p);
		TypeCheckVisitor tc = new TypeCheckVisitor(bs.getSymbolTable());
		tc.visit(p);
		ppv.visit(p);
	}

}
