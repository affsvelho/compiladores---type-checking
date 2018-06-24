package ast;
import visitor.IVisitor;

public class BooleanType extends Type {
	@Override
	public <T> T accept(IVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
