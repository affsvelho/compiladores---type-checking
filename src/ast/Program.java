package ast;

import visitor.IVisitor;

public class Program {
	public MainClass m;
	public ClassDeclList cl;

	public Program(MainClass am, ClassDeclList acl) {
		m = am;
		cl = acl;
	}

	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
