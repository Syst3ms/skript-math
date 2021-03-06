package fr.syst3ms.skriptmath.util.evaluation;

import fr.syst3ms.skriptmath.util.MathUtils;

import java.util.function.BinaryOperator;

public class Difference extends DoubleOperandTerm {

	public Difference(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::minus;
	}

	@Override
	protected String getAsString(Class<? extends DoubleOperandTerm> calling, boolean isSecond) {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Difference.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Difference.class, true) : second.asString();
		if (calling != getClass() && calling != Sum.class || isSecond) {
			return String.format("(%s - %s)", f, s);
		} else {
			return String.format("%s - %s", f, s);
		}
	}

	@Override
	public MathTerm simplifyOperation() {
		if (first == Constant.ZERO) {
			return second.getNegative();
		} else if (second == Constant.ZERO) {
			return first;
		} else if (second instanceof Constant && ((Constant) second).isNegative()) {
			return new Sum(first, second.getNegative()).simplify();
		} else if (second instanceof Sum) {
			Sum s = (Sum) second;
			return new Difference(first, new Difference(s.getFirst().getNegative(), s.getSecond())).simplify();
		} else if (second instanceof Difference) {
			Difference s = (Difference) second;
			return new Difference(first, new Sum(s.getFirst().getNegative(), s.getSecond())).simplify();
		} else if (first.equals(second)) {
			return Constant.ZERO;
		} else if (first instanceof Division && second instanceof Division) {
			Division f = (Division) first;
			Division s = (Division) second;
			if (f.getSecond().equals(s.getSecond())) {
				return new Division(new Product(f.getFirst(), s.getFirst()), f.getSecond()).simplify();
			}
		}
		return this;
	}

	@Override
	public MathTerm getNegative() {
		return new Difference(second, first);
	}

	@Override
	public String asString() {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Difference.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Difference.class, true) : second.asString();
		return String.format("%s - %s", f, s);
	}
}
