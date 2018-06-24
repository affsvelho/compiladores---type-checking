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
import ast.Statement;
import ast.This;
import ast.Times;
import ast.True;
import ast.Type;
import ast.VarDecl;
import ast.While;
import symboltable.Class;
import symboltable.Method;
import symboltable.SymbolTable;

public class TypeCheckVisitor implements IVisitor<Type> {

	private SymbolTable symbolTable;
	private Class currentClass;
	private Method currentMethod;
	private boolean fromVar = false;

	private String getTypeName(Type t) {
		if (t != null) {
			if (t instanceof BooleanType)
				return "Boolean";
			else if (t instanceof IdentifierType)
				return ((IdentifierType) t).toString();
			else if (t instanceof IntArrayType)
				return "int []";
			else if (t instanceof IntegerType)
				return "int";
			else
				System.out.print("tipo nao valido");
		}
		return "null";
	}
	
	public TypeCheckVisitor(SymbolTable st) {
		symbolTable = st;
	}

	// MainClass m;
	// ClassDeclList cl;
	public Type visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i1,i2;
	// Statement s;
	public Type visit(MainClass n) {
		currentClass = symbolTable.getClass(n.i1.s);
		n.i1.accept(this);

		this.currentMethod = symbolTable.getMethod("main", this.currentClass.getId());
		this.fromVar = true;
		n.i2.accept(this);
		this.fromVar = false;
		currentMethod = null;

		n.s.accept(this);

		currentClass = null;
		return null;
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclSimple n) {
		currentClass = symbolTable.getClass(n.i.toString());
		n.i.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		currentClass = null;
		return null;
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclExtends n) {
		currentClass = symbolTable.getClass(n.i.toString());
		n.i.accept(this);
		n.j.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		currentClass = null;
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(VarDecl n) {
		n.t.accept(this);
		this.fromVar = true;
		n.i.accept(this);
		this.fromVar = false;
		return n.t;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {
		currentMethod = symbolTable.getMethod(n.i.s, currentClass.getId());
		Type t1 = n.t.accept(this);
		n.i.accept(this);

		Type t = symbolTable.getMethodType(n.i.s, currentClass.getId());

		for (int i = 0; i < n.fl.size(); i++) {
			n.fl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.sl.size(); i++) {
			Statement s = n.sl.elementAt(i);
			s.accept(this);
		}
		if(n.e instanceof IdentifierExp) {
			this.fromVar = true;
		}

		Type expType = n.e.accept(this);
		this.fromVar = false;
		
		if (!symbolTable.compareTypes(t1, expType) && expType != null && t1 != null) {
			System.out.print("retorno incompativel com o tipo definido, ");
			System.out.print("recebido ");
			expType.accept(new PrettyPrintVisitor());
			System.out.print(", mas esperado ");
			t1.accept(new PrettyPrintVisitor());
			System.out.println();
		}
		currentMethod = null;
		return t;
	}

	// Type t;
	// Identifier i;
	public Type visit(Formal n) {
		n.t.accept(this);
		this.fromVar = true;
		n.i.accept(this);
		this.fromVar = false;
		return null;
	}

	public Type visit(IntArrayType n) {
		return new IntArrayType();
	}

	public Type visit(BooleanType n) {
		return new BooleanType();
	}

	public Type visit(IntegerType n) {
		return new IntegerType();
	}

	// String s;
	public Type visit(IdentifierType n) {
		if (symbolTable.containsClass(n.s))
			return new IdentifierType(n.s);
		else {
			System.out.println("classe do tipo " + n.s + " nao encontrada");
			return null;
		}
	}

	// StatementList sl;
	public Type visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	// Exp e;
	// Statement s1,s2;
	public Type visit(If n) {
		if(n.e instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType = n.e.accept(this);
		this.fromVar = false;
		
		if (expType == null) {
			return null;
		}
		if (!(expType instanceof BooleanType)) {
			System.out.println("a expressao nao eh de um tipo booleano");
		}
		
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	// Exp e;
	// Statement s;
	public Type visit(While n) {
		if(n.e instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType = n.e.accept(this);
		this.fromVar = false;
		if (expType == null)
			return null;
		if (!(expType instanceof BooleanType)) {
			System.out.println("a expressao nao eh de um tipo booleano");
		}
		n.s.accept(this);
		return null;
	}

	// Exp e;
	public Type visit(Print n) {
		if(n.e instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType = n.e.accept(this);
		this.fromVar = false;
		
		if (expType == null)
			return null;
		if (!(expType instanceof Type)) {
			System.out.print("no PRINT: ");
			n.e.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh um tipo!");

		}
		return null;
	}

	// Identifier i;
	// Exp e;
	public Type visit(Assign n) {
		this.fromVar = true;
		Type idType = n.i.accept(this);
		this.fromVar = false;
		if(n.e instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType = n.e.accept(this);
		this.fromVar = false;
		if (idType == null) {
			return null;
		}
		if (expType == null) {
			return null;
		}
		if (!symbolTable.compareTypes(idType, expType)) {
			System.out.print("o tipo do identificador " + n.i.toString() + " e da expressao ");
			n.e.accept(new PrettyPrintVisitor());
			System.out.println(" nao sao iguais");
		}

		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
		this.fromVar = true;
		Type idType = n.i.accept(this);
		this.fromVar = false;
		if(n.e1 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType1 = n.e1.accept(this);
		this.fromVar = false;
		if(n.e2 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType2 = n.e2.accept(this);
		this.fromVar = false;
		if (idType == null | expType1 == null | expType2 == null) {
			return null;
		}
		if (!(idType instanceof IntArrayType)) {
			System.out.println(n.i.toString() + " nao eh um array");
		}
		if (!(expType1 instanceof IntegerType)) {
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh um inteiro");
		}
		if (!(expType2 instanceof IntegerType)) {
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh um inteiro");
		}
		return null;
	}

	// Exp e1,e2;
	public Type visit(And n) {
		if(n.e1 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType1 = n.e1.accept(this);
		this.fromVar = false;
		if(n.e2 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType2 = n.e2.accept(this);
		this.fromVar = false;
		if (expType1 == null | expType2 == null)
			return null;
		if (!(expType1 instanceof BooleanType)) {
			System.out.print("em AND, ");
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh booleana");
		}
		if (!(expType2 instanceof BooleanType)) {
			System.out.print("em AND, ");
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh booleana");
		}
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(LessThan n) {
		if(n.e1 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType1 = n.e1.accept(this);
		this.fromVar = false;
		if(n.e2 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType2 = n.e2.accept(this);
		this.fromVar = false;
		if (expType1 == null | expType2 == null)
			return null;
		if (!(expType1 instanceof IntegerType)) {
			System.out.print("em LessThan,");
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh um inteiro");
		}
		if (!(expType2 instanceof IntegerType)) {
			System.out.print("em LessThan,");
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh um inteiro");
		}
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(Plus n) {
		if(n.e1 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType1 = n.e1.accept(this);
		this.fromVar = false;
		if(n.e2 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType2 = n.e2.accept(this);
		this.fromVar = false;
		if (expType1 == null | expType2 == null)
			return null;
		if (!(expType1 instanceof IntegerType)) {
			System.out.print("em Plus: ");
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh um inteiro");
		}
		if (!(expType2 instanceof IntegerType)) {
			System.out.print("em Plus,");
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh um inteiro");
		}
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Minus n) {
		if(n.e1 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType1 = n.e1.accept(this);
		this.fromVar = false;
		if(n.e2 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType2 = n.e2.accept(this);
		this.fromVar = false;
		
		if (expType1 == null | expType2 == null)
			return null;
		if (!(expType1 instanceof IntegerType)) {
			System.out.print("em Minus : ");
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh um inteiro");
		}
		if (!(expType2 instanceof IntegerType)) {
			System.out.print("em Minus,");
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh um inteiro");
		}
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Times n) {
		if(n.e1 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType1 = n.e1.accept(this);
		this.fromVar = false;
		if(n.e2 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType2 = n.e2.accept(this);
		this.fromVar = false;
		if (expType1 == null | expType2 == null)
			return null;
		if (!(expType1 instanceof IntegerType)) {
			System.out.print("em Times : ");
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh um inteiro");
		}
		if (!(expType2 instanceof IntegerType)) {
			System.out.print("em Times,");
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" nao eh um inteiro");
		}
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		if(n.e1 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type exp1Type = n.e1.accept(this);
		this.fromVar = false;
		if(n.e2 instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type exp2Type = n.e2.accept(this);
		this.fromVar = false;

		if (exp1Type != null && exp2Type != null) {
			if (!(exp1Type instanceof IntArrayType)) {
				n.e1.accept(new PrettyPrintVisitor());
				System.out.println(" em ArrayLookup nao eh um Int []");
			}

			if (!(exp2Type instanceof IntegerType)) {
				n.e2.accept(new PrettyPrintVisitor());
				System.out.println(" em ArrayLookup nao eh um int");
			}

			return new IntegerType();
		}
		return null;
	}

	// Exp e;
	public Type visit(ArrayLength n) {
		if(n.e instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType = n.e.accept(this);
		this.fromVar = false;
		
		if (expType != null) {
			if (!(expType instanceof IntArrayType)) {
				n.e.accept(new PrettyPrintVisitor());
				System.out.println(" em ArrayLength nao eh do tipo IntArray");
			} else
				return new IntegerType();
		}
		return null;
	}

	// this.m().g()
	// Exp e;
	// Identifier i;
	// ExpList el;
	public Type visit(Call n) {
		if(n.e instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType = n.e.accept(this);
		this.fromVar = false;
		
		String className = "";
		if (expType instanceof IdentifierType) {
			className = ((IdentifierType) expType).s;
		} else {
			System.out.println("");
		}

		if (symbolTable.getMethod(n.i.toString(), className) == null) {
			System.out.print("erro em ");
			n.e.accept(new PrettyPrintVisitor());
			System.out.println(
					", que eh do tipo da classe" + className + " a qual nao possui o metodo: " + n.i.toString());
		}else {
			Type methodType = symbolTable.getMethod(n.i.toString(), className).type();
			Method method = symbolTable.getMethod(n.i.toString(), className);
			
			int sizeMethodParams = 0;
			
			while (true) {
				if (method.getParamAt(sizeMethodParams) != null)
					sizeMethodParams++;
				else
					break;
			}
			
			if (n.el.size() != sizeMethodParams) {
				System.out.println("quantidade de parametros nao sao equivalentes");
			} else {
				for (int i = 0; i < n.el.size(); i++) {
					fromVar = true;
					Type paramType = n.el.elementAt(i).accept(this);
					fromVar = false;
					Type methodParamType = method.getParamAt(i).type();
					
					if (!(symbolTable.compareTypes(paramType, methodParamType))) {
						System.out.println("no parametro " + (i + 1) + " o tipo esperado era "
								+ getTypeName(methodParamType) + " e o recebido foi " + getTypeName(paramType));
					}
				}
			}
			return methodType;
		}


		return null;
	}

	// int i;
	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	public Type visit(True n) {
		return new BooleanType();
	}

	public Type visit(False n) {
		return new BooleanType();
	}

	// String s;
	public Type visit(IdentifierExp n) {
		Type t = symbolTable.getVarType(currentMethod, currentClass, n.s);
		if (t == null) {
			System.out.println("expressao " + n.s + " nao possivel");
		}
		return t;
	}

	public Type visit(This n) {
		return currentClass.type();
	}

	// Exp e;
	public Type visit(NewArray n) {
		if(n.e instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType = n.e.accept(this);
		this.fromVar = false;
		
		if (expType != null) {
			if (!(expType instanceof IntegerType)) {
				n.e.accept(new PrettyPrintVisitor());
				System.out.println(" nao eh do tipo Int, entao nao pode ser o tamanho do array");
			} else
				return new IntArrayType();
		}
		return null;
	}

	// Identifier i;
	public Type visit(NewObject n) {
		Method aux = currentMethod;
		currentMethod = null;
		Type idType = n.i.accept(this);
		currentMethod = aux;

		return idType;
	}

	// Exp e;
	public Type visit(Not n) {
		if(n.e instanceof IdentifierExp) {
			this.fromVar = true;
		}
		Type expType = n.e.accept(this);
		this.fromVar = false;

		if (expType != null) {
			if (!(expType instanceof BooleanType)) {
				System.out.print("em Not,");
				n.e.accept(new PrettyPrintVisitor());
				System.out.println(" nao eh booleana");
			} else
				return new BooleanType();
		}

		return null;
	}

	// String s;
	public Type visit(Identifier n) {
		if (this.fromVar) {
			return symbolTable.getVarType(currentMethod, currentClass, n.toString());
		} else {
			if (currentClass != null) {
				if (currentMethod == null) {
					if (!symbolTable.containsClass(n.toString())) {
						System.out.println("simbolo " + n.toString() + " nao encontrado");
						return null;
					}
				} else {
					if (symbolTable.getClass(currentClass.getId()).parent() != null) {
						if (!symbolTable.getClass(symbolTable.getClass(currentClass.getId()).parent()).containsMethod(n.toString()) &&
								!symbolTable.getClass(currentClass.getId()).containsMethod(n.toString())) {
							System.out.println("simbolo " + n.toString() + " nao "
									+ "encontrado na classe filha " + currentClass.getId() + ", na classe pai "
									+ symbolTable.getClass(currentClass.getId()).parent());
							return null;
						}
					}
					else if (!symbolTable.getClass(currentClass.getId()).containsMethod(n.toString())) {
						System.out.println("simbolo " + n.toString() + " nao encontrado");
						return null;
					}
				}
			}
		}
		return new IdentifierType(n.toString());
	}
}
