package ast;

import visitor.IVisitor;

public class Print extends Statement {
	public Exp e;

	public Print(Exp ae) {
		e = ae;
	}

	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
