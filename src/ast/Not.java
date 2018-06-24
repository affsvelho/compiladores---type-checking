package ast;

import visitor.IVisitor;

public class Not extends Exp {
	public Exp e;

	public Not(Exp ae) {
		e = ae;
	}

	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
