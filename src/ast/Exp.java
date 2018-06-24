package ast;

import visitor.IVisitor;

public abstract class Exp {
	public abstract <T> T accept(IVisitor<T> visitor);
}
