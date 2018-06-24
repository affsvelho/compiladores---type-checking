package ast;

import visitor.IVisitor;

public class IntArrayType extends Type {
	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
