package ast;

import visitor.IVisitor;

public class NewArray extends Exp {
	public Exp e;

	public NewArray(Exp ae) {
		e = ae;
	}

	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
