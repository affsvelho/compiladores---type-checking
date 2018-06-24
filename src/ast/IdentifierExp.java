package ast;

import visitor.IVisitor;

public class IdentifierExp extends Exp {
	public String s;

	public IdentifierExp(String as) {
		s = as;
	}

	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
