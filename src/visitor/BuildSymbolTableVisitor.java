package visitor;

import ast.And;
import ast.ArrayAssign;
import ast.ArrayLength;
import ast.ArrayLookup;
import ast.Assign;
import ast.Block;
import ast.BooleanType;
import ast.Call;
import ast.ClassDeclExtends;
import ast.ClassDeclSimple;
import ast.False;
import ast.Formal;
import ast.Identifier;
import ast.IdentifierExp;
import ast.IdentifierType;
import ast.If;
import ast.IntArrayType;
import ast.IntegerLiteral;
import ast.IntegerType;
import ast.LessThan;
import ast.MainClass;
import ast.MethodDecl;
import ast.Minus;
import ast.NewArray;
import ast.NewObject;
import ast.Not;
import ast.Plus;
import ast.Print;
import ast.Program;
import ast.This;
import ast.Times;
import ast.True;
import ast.VarDecl;
import ast.While;
import symboltable.Class;
import symboltable.Method;
import symboltable.SymbolTable;

public class BuildSymbolTableVisitor implements IVisitor<Void> {

	SymbolTable symbolTable;

	public BuildSymbolTableVisitor() {
		symbolTable = new SymbolTable();
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	private Class currentClass;
	private Method currMethod;

	// MainClass m;
	// ClassDeclList cl;
	public Void visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i1,i2;
	// Statement s;
	public Void visit(MainClass n) {
		symbolTable.addClass(n.i1.toString(), null);
		currentClass = symbolTable.getClass(n.i1.toString());
		currentClass.addMethod("main", null);
		this.currMethod = currentClass.getMethod("main");
		this.currMethod.addParam(n.i2.toString(), new IntArrayType());
		
		n.i1.accept(this);
		n.i2.accept(this);
		n.s.accept(this);
		currMethod = null;
		currentClass = null;
			
		return null;
	
	}

	public Void visit(ClassDeclSimple n) {
		if(!symbolTable.addClass(n.i.toString(), null)) {
			System.out.println("A classe " + n.i.toString() + " ja foi definida");
		}else {
			currentClass = symbolTable.getClass(n.i.toString());
			n.i.accept(this);
			
			for (int i = 0; i < n.vl.size(); i++) {
				n.vl.elementAt(i).accept(this);
			}
			for (int i = 0; i < n.ml.size(); i++) {
				n.ml.elementAt(i).accept(this);
			}
			currentClass = null;
		}
		
		return null;
	}

	public Void visit(ClassDeclExtends dec) {
		if(!symbolTable.addClass(dec.i.toString(), dec.j.toString())) {
			System.out.println("A classe " + dec.i.toString() + " ja foi definida");
		}else {
			currentClass = symbolTable.getClass(dec.i.toString());
			dec.i.accept(this);
			dec.j.accept(this);
			
			for (int i = 0; i < dec.vl.size(); i++) {
				dec.vl.elementAt(i).accept(this);
			}
			for (int i = 0; i < dec.ml.size(); i++) {
				dec.ml.elementAt(i).accept(this);
			}
			currentClass = null;
		}
		return null;
	}

	// Type t;
	// Identifier i;
	public Void visit(VarDecl varRec) {
		boolean flag = false;
		if(currentClass != null){
			if(currMethod != null){
				if(currMethod.containsVar(varRec.i.toString())){
					System.out.println("A variavel " + varRec.i.toString() + " ja foi declarada no metodo");	
				}else {
					currMethod.addVar(varRec.i.toString(), varRec.t);
					flag = true;
				}
			}else{
				if(currentClass.containsVar(varRec.i.toString())){
					System.out.println("A variavel" + varRec.i.toString() + " ja foi declarada na classe");			
				}else {
					currentClass.addVar(varRec.i.toString(), varRec.t);
					flag = true;
				}
				
			}
			if(flag) {
				varRec.t.accept(this);
				varRec.i.accept(this);
			}
		}
		return null;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Void visit(MethodDecl method) {
		if(!currentClass.addMethod(method.i.toString(), method.t)) {
			System.out.println("O metodo " + method.i.toString() + " ja foi definido");
		}else {
			currMethod = currentClass.getMethod(method.i.toString());
			method.t.accept(this);
			method.i.accept(this);
			for (int i = 0; i < method.fl.size(); i++) {
				method.fl.elementAt(i).accept(this);
			}
			for (int i = 0; i < method.vl.size(); i++) {
				method.vl.elementAt(i).accept(this);
			}
			for (int i = 0; i < method.sl.size(); i++) {
				method.sl.elementAt(i).accept(this);
			}
			method.e.accept(this);
			currMethod = null;
		}
		return null;
	}

	// Type t;
	// Identifier i;
	public Void visit(Formal n) {
		if(currMethod.containsParam(n.i.toString())){
			System.out.println("A variavel " + n.i.toString() + " ja foi passada como parametro do metodo");		
		}else {
			currMethod.addParam(n.i.toString(), n.t);
			n.t.accept(this);
			n.i.accept(this);
		}
		
		return null;
	}

	public Void visit(IntArrayType n) {
		return null;
	}

	public Void visit(BooleanType n) {
		return null;
	}

	public Void visit(IntegerType n) {
		return null;
	}

	// String s;
	public Void visit(IdentifierType n) {
		return null;
	}

	// StatementList sl;
	public Void visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	// Exp e;
	// Statement s1,s2;
	public Void visit(If n) {
		n.e.accept(this);
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	// Exp e;
	// Statement s;
	public Void visit(While n) {
		n.e.accept(this);
		n.s.accept(this);
		return null;
	}

	// Exp e;
	public Void visit(Print n) {
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	// Exp e;
	public Void visit(Assign n) {
		n.i.accept(this);
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Void visit(ArrayAssign n) {
		n.i.accept(this);
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Void visit(And n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Void visit(LessThan n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Void visit(Plus n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Void visit(Minus n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Void visit(Times n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Void visit(ArrayLookup n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e;
	public Void visit(ArrayLength n) {
		n.e.accept(this);
		return null;
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public Void visit(Call n) {
		n.e.accept(this);
		n.i.accept(this);
		for (int i = 0; i < n.el.size(); i++) {
			n.el.elementAt(i).accept(this);
		}
		return null;
	}

	// int i;
	public Void visit(IntegerLiteral n) {
		return null;
	}

	public Void visit(True n) {
		return null;
	}

	public Void visit(False n) {
		return null;
	}

	// String s;
	public Void visit(IdentifierExp n) {
		return null;
	}

	public Void visit(This n) {
		return null;
	}

	// Exp e;
	public Void visit(NewArray n) {
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	public Void visit(NewObject n) {
		return null;
	}

	// Exp e;
	public Void visit(Not n) {
		n.e.accept(this);
		return null;
	}

	// String s;
	public Void visit(Identifier n) {
		return null;
	}
}