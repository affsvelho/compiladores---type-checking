package ast;

import visitor.IVisitor;

public abstract class ClassDecl {
	public abstract <T> T accept(IVisitor<T> visitor);
}
