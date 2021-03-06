package fr.syst3ms.skriptmath.util.evaluation;

import fr.syst3ms.skriptmath.util.MathUtils;

import java.util.function.BinaryOperator;

public class Product extends DoubleOperandTerm {

	public Product(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::times;
	}

	@Override
	protected String getAsString(Class<? extends DoubleOperandTerm> calling, boolean isSecond) {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Product.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Product.class, true) : second.asString();
		if (calling == Power.class || (calling == Division.class || calling == Modulo.class) && isSecond) {
			return String.format("(%s * %s)", f, s);
		} else {
			return String.format("%s * %s", f, s);
		}
	}

	@Override
	public MathTerm simplifyOperation() {
		if (first == Constant.ONE) {
			return second;
		} else if (second == Constant.ONE) {
			return first;
		} else if (first == Constant.ZERO || second == Constant.ZERO) {
			return Constant.ZERO;
		} else if (first.equals(second)) {
			return first.getSquared();
		} /*else if (second.equals(first.getReciprocal())) {
			return Constant.ONE;
		} */else if (first.isSimple() && (second instanceof Sum || second instanceof Difference)) {
			DoubleOperandTerm s = (DoubleOperandTerm) second;
			if (second instanceof Sum) {
				return new Sum(new Product(first, s.getFirst()), new Product(first, s.getSecond())).simplify();
			} else {
				return new Difference(new Product(first, s.getFirst()), new Product(first, s.getSecond())).simplify();
			}
		} else if ((first instanceof Sum || first instanceof Difference) && second.isSimple()) {
			DoubleOperandTerm f = (DoubleOperandTerm) first;
			return new Sum(new Product(second, f.getFirst()), new Product(second, f.getSecond())).simplify();
		} else if ((first instanceof Sum || first instanceof Difference) && (second instanceof Sum || second instanceof Difference)) {
			DoubleOperandTerm f = (DoubleOperandTerm) first;
			DoubleOperandTerm s = (DoubleOperandTerm) second;
			MathTerm a = f.getFirst();
			MathTerm b = f.getSecond();
			MathTerm c = s.getFirst();
			MathTerm d = s.getSecond();
			if (first instanceof Sum && second instanceof Sum) {
				return new Sum(
						new Sum(new Product(a, c), new Product(a, d)),
						new Sum(new Product(b, c), new Product(b, d))
				).simplify();
			} else if (first instanceof Sum && second instanceof Difference) {
				return new Sum(
						new Difference(new Product(a, c), new Product(a, d)),
						new Difference(new Product(b, c), new Product(b, d))
				).simplify();
			} else if (first instanceof Difference && second instanceof Sum) {
				return new Difference(
						new Sum(new Product(a, c), new Product(a, d)),
						new Sum(new Product(b, c), new Product(b, d))
				).simplify();
			} else {
				return new Difference(
						new Difference(new Product(a, c), new Product(a, d)),
						new Difference(new Product(b, c), new Product(b, d))
				).simplify();
			}
		} else if (first instanceof Division && second instanceof Division) {
			Division f = (Division) first;
			Division s = (Division) second;
			return new Division(new Product(f.getFirst(), s.getFirst()), new Product(f.getSecond(), s.getSecond())).simplify();
		} else if (first instanceof Division || second instanceof Division) {
			if (first instanceof Division) {
				Division f = (Division) first;
				MathTerm a = f.getFirst();
				MathTerm b = f.getSecond();
				return new Division(new Product(a, second), b).simplify();
			} else {
				Division s = (Division) second;
				MathTerm b = s.getFirst();
				MathTerm c = s.getSecond();
				return new Division(new Product(first, b), c).simplify();
			}
		} else if (first instanceof Product && second instanceof Product) {
			MathTerm a, b, c, d;
			Product f = (Product) first;
			Product s = (Product) second;
			a = f.getFirst();
			b = f.getSecond();
			c = s.getFirst();
			d = s.getSecond();
			MathTerm firstTry = new Product(a, c).simplify();
			if (!firstTry.equals(new Product(a, c))) {
				return new Product(firstTry, new Product(b, d)).simplify();
			}
			MathTerm secondTry = new Product(a, d).simplify();
			if (!secondTry.equals(new Product(a, d))) {
				return new Product(secondTry, new Product(b, c)).simplify();
			}
			MathTerm thirdTry = new Product(b, c).simplify().simplify();
			if (!thirdTry.equals(new Product(b, c))) {
				return new Product(thirdTry, new Product(a, d));
			}
			MathTerm fourthTry = new Product(b, d).simplify();
			if (!fourthTry.equals(new Product(b, d))) {
				return new Product(fourthTry, new Product(a, c)).simplify();
			}
		} else if (first instanceof Product) {
			MathTerm a, b, c;
			Product f = (Product) first;
			a = f.getFirst();
			b = f.getSecond();
			c = second;
			MathTerm secondTry = new Product(a, c).simplify();
			if (!secondTry.equals(new Product(a, c))) {
				return new Product(b, secondTry);
			}
			MathTerm thirdTry = new Product(b, c).simplify();
			if (!thirdTry.equals(new Product(b, c))) {
				return new Product(a, thirdTry);
			}
		} else if (second instanceof Product) {
			MathTerm a, b, c;
			Product s = (Product) second;
			a = first;
			b = s.getFirst();
			c = s.getSecond();
			MathTerm firstTry = new Product(a, c).simplify();
			if (!firstTry.equals(new Product(a, c))) {
				return new Product(b, firstTry);
			}
			MathTerm secondTry = new Product(b, c).simplify();
			if (!secondTry.equals(new Product(b, c))) {
				return new Product(a, secondTry);
			}
		}
		return this;
	}

	@Override
	public String asString() {
		String f = first instanceof DoubleOperandTerm ? ((DoubleOperandTerm) first).getAsString(Product.class, false) : first.asString();
		String s = second instanceof DoubleOperandTerm ? ((DoubleOperandTerm) second).getAsString(Product.class, true) : second.asString();
		if (first instanceof Constant)
			if (second instanceof Unknown || !(second instanceof Power || second instanceof Product || second instanceof Division || second instanceof Modulo))
				return String.format("%s%s", f, s);
			else
				return String.format("%s(%s)", f, s);
		else if (second instanceof Constant)
			if (first instanceof Unknown || !(first instanceof Power || first instanceof Product || first instanceof Division || first instanceof Modulo))
				return String.format("%s%s", f, s);
			else
				return String.format("%s(%s)", f, s);
		return String.format("%s * %s", f, s);
	}

	@Override
	public MathTerm getNegative() {
		return new Product(first.getNegative(), second);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DoubleOperandTerm)) {
			return false;
		} else if (this == obj) {
			return true;
		} else {
			DoubleOperandTerm o = (DoubleOperandTerm) obj;
			return first.equals(o.first) && second.equals(o.second) || first.equals(o.second) && second.equals(o.first);
		}
	}
}
