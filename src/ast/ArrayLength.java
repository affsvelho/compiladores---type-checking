package ast;

import visitor.IVisitor;

public class ArrayLength extends Exp {
	public Exp e;

	public ArrayLength(Exp ae) {
		e = ae;
	}

	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
