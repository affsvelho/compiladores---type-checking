package ast;

import visitor.IVisitor;

public abstract class Statement {
	public abstract <T> T accept(IVisitor<T> visitor);
}
