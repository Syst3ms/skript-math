package fr.syst3ms.skuared.util.evaluation;

import fr.syst3ms.skuared.util.MathUtils;

import java.util.function.BinaryOperator;

public class LeftBitShift extends DoubleOperandTerm {

	public LeftBitShift(MathTerm first, MathTerm second) {
		super(first, second);
	}

	@Override
	BinaryOperator<Number> getFunction() {
		return MathUtils::shl;
	}

	@Override
	public String asString() {
		return "(" + first + " << " + second + ")";
	}
}
