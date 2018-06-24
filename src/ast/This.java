package ast;

import visitor.IVisitor;

public class This extends Exp {
	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
