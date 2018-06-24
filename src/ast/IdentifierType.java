package ast;

import visitor.IVisitor;

public class IdentifierType extends Type {
	public String s;

	public IdentifierType(String as) {
		s = as;
	}

	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
