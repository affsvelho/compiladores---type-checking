package ast;

import visitor.IVisitor;

public abstract class Type {
	public abstract <T> T accept(IVisitor<T> visitor);
}
