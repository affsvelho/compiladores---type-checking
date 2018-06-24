package ast;

import visitor.IVisitor;

public class Block extends Statement {
	public StatementList sl;

	public Block(StatementList asl) {
		sl = asl;
	}

	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
