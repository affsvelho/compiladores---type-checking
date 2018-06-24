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

public interface IVisitor<T> {
	public T visit(Program n);

	public T visit(MainClass n);

	public T visit(ClassDeclSimple n);

	public T visit(ClassDeclExtends n);

	public T visit(VarDecl n);

	public T visit(MethodDecl n);

	public T visit(Formal n);

	public T visit(IntArrayType n);

	public T visit(BooleanType n);

	public T visit(IntegerType n);

	public T visit(IdentifierType n);

	public T visit(Block n);

	public T visit(If n);

	public T visit(While n);

	public T visit(Print n);

	public T visit(Assign n);

	public T visit(ArrayAssign n);

	public T visit(And n);

	public T visit(LessThan n);

	public T visit(Plus n);

	public T visit(Minus n);

	public T visit(Times n);

	public T visit(ArrayLookup n);

	public T visit(ArrayLength n);

	public T visit(Call n);

	public T visit(IntegerLiteral n);

	public T visit(True n);

	public T visit(False n);

	public T visit(IdentifierExp n);

	public T visit(This n);

	public T visit(NewArray n);

	public T visit(NewObject n);

	public T visit(Not n);

	public T visit(Identifier n);
}
