package ast;

import visitor.IVisitor;

public class Identifier {
	public String s;

	public Identifier(String as) {
		s = as;
	}

	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public String toString() {
		return s;
	}
}
