package ast;

import visitor.IVisitor;

public class IntegerType extends Type {
	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
