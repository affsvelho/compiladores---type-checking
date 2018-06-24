package ast;

import visitor.IVisitor;

public class VarDecl {
	public Type t;
	public Identifier i;

	public VarDecl(Type at, Identifier ai) {
		t = at;
		i = ai;
	}

	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
