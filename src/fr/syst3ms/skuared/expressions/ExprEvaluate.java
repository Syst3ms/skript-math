package fr.syst3ms.skuared.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.skuared.util.Algorithms;
import fr.syst3ms.skuared.util.StringUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by ARTHUR on 10/06/2017.
 */
public class ExprEvaluate extends SimpleExpression<Number> {
    private Expression<String> expr;

    static {
        Skript.registerExpression(
                ExprEvaluate.class,
                Number.class,
                ExpressionType.COMBINED,
                "eval[uate] [[math[ematic]] expr[ession]] %string%"
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        expr = (Expression<String>) expressions[0];
        return true;
    }

    @NotNull
    @Override
    protected Number[] get(Event event) {
        String e = expr.getSingle(event);
        List<String> rpn = Algorithms.shuntingYard(e);
        return new Number[]{Algorithms.evaluateRpn(rpn)};
    }

    @Nullable
    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    @Contract("-> true")
    public boolean isSingle() {
        return true;
    }

    @NotNull
    @Override
    public String toString(Event event, boolean b) {
        return "evaluate expr " + expr.toString(event, b);
    }
}
