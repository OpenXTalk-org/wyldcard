/*
 * ExpNumberOfFun
 * hypertalk-java
 *
 * Created by Matt DeFano on 2/19/17 3:11 PM.
 * Copyright © 2017 Matt DeFano. All rights reserved.
 */

/**
 * NumberOfFunc.java
 * @author matt.defano@gmail.com
 * 
 * Implementation of the built-in function "the number of"
 */

package com.defano.hypertalk.ast.functions;

import com.defano.hypercard.HyperCard;
import com.defano.hypertalk.ast.common.*;
import com.defano.hypertalk.ast.expressions.Expression;
import com.defano.hypertalk.exception.HtSemanticException;

public class NumberOfFunc extends Expression {

    public final Countable itemtype;
    public final Expression expression;

    public NumberOfFunc(Countable itemtype) {
        this.itemtype = itemtype;
        this.expression = null;
    }

    public NumberOfFunc(Countable itemtype, Expression expression) {
        this.itemtype = itemtype;
        this.expression = expression;
    }

    public Value evaluate() throws HtSemanticException {
        switch (itemtype) {
            case CHAR:
                return new Value(expression.evaluate().charCount());
            case WORD:
                return new Value(expression.evaluate().wordCount());
            case LINE:
                return new Value(expression.evaluate().lineCount());
            case ITEM:
                return new Value(expression.evaluate().itemCount());
            case CARD_PARTS:
                return new Value(HyperCard.getInstance().getCard().getPartCount(null, Owner.CARD));
            case BKGND_PARTS:
                return new Value(HyperCard.getInstance().getCard().getPartCount(null, Owner.BACKGROUND));
            case CARD_BUTTONS:
                return new Value(HyperCard.getInstance().getCard().getPartCount(PartType.BUTTON, Owner.CARD));
            case BKGND_BUTTONS:
                return new Value(HyperCard.getInstance().getCard().getPartCount(PartType.BUTTON, Owner.BACKGROUND));
            case CARD_FIELDS:
                return new Value(HyperCard.getInstance().getCard().getPartCount(PartType.FIELD, Owner.CARD));
            case BKGND_FIELDS:
                return new Value(HyperCard.getInstance().getCard().getPartCount(PartType.FIELD, Owner.BACKGROUND));
            default:
                throw new RuntimeException("Bug! Unimplemented countable item type: " + itemtype);
        }
    }
}
