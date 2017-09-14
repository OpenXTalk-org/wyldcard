package com.defano.hypercard.parts;

import com.defano.hypercard.HyperCard;
import com.defano.hypercard.context.ToolMode;
import com.defano.hypercard.context.ToolsContext;
import com.defano.hypercard.runtime.MessageCompletionObserver;
import com.defano.hypercard.runtime.Interpreter;
import com.defano.hypertalk.ast.common.*;
import com.defano.hypertalk.ast.containers.PartSpecifier;
import com.defano.hypertalk.exception.HtSemanticException;
import com.google.common.util.concurrent.ListenableFuture;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Represents an object that can receive HyperTalk messages.
 */
public interface Messagable {

    /**
     * Gets the script associated with this part.
     * @return The script
     */
    Script getScript();

    /**
     * Gets a part specifier that uniquely identifies this part in the stack. This part will be bound to the 'me'
     * keyword in the script that receives messages.
     *
     * @return The part specifier for the 'me' keyword.
     */
    PartSpecifier getMe();

    /**
     * Sends a message (i.e., 'mouseUp') to this part's message passing hierarchy.
     * @param message The message to be passed.
     */
    default void receiveMessage(String message) {
        receiveMessage(message, new ExpressionList(), (command, trapped, err) -> {});
    }

    /**
     * Sends a message with bound arguments (i.e., 'doMenu') to this part's message passing hierarchy.
     * @param message The message to be passed
     * @param arguments The arguments to the message
     */
    default void receiveMessage(String message, ExpressionList arguments) {
        receiveMessage(message, arguments, (command, trapped, err) -> {});
    }

    /**
     * Sends a message with arguments (i.e., 'doMenu theMenu, theItem') to this part's message passing hierarchy.
     *
     * @param command The name of the command; cannot be null.
     * @param arguments The arguments to pass to this command; cannot be null.
     * @param onCompletion A callback that will fire as soon as the command has been executed in script; cannot be null.
     */
    default void receiveMessage(String command, ExpressionList arguments, MessageCompletionObserver onCompletion) {

        // No commands are sent to buttons or fields when not in browse mode
        if (ToolsContext.getInstance().getToolMode() != ToolMode.BROWSE && getMe().isCardElementSpecifier()) {
            onCompletion.onMessagePassingCompletion(command, false, null);
            return;
        }

        try {
            // Attempt to invoke command handler in this part and listen for completion
            ListenableFuture<Boolean> trapped = Interpreter.executeHandler(getMe(), getScript(), command, arguments);
            trapped.addListener(() -> {
                try {

                    // Did this part trap this command?
                    if (trapped.get()) {
                        onCompletion.onMessagePassingCompletion(command, true, null);
                    } else {
                        // Get next recipient in message passing order; null if no other parts receive message
                        Messagable nextRecipient = getNextMessageRecipient();
                        if (nextRecipient == null) {
                            onCompletion.onMessagePassingCompletion(command, false, null);
                        } else {
                            nextRecipient.receiveMessage(command, arguments, onCompletion);
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    // Thread was interrupted; nothing else we can do.
                    onCompletion.onMessagePassingCompletion(command, false, null);
                }
            }, Executors.newSingleThreadExecutor());
        } catch (HtSemanticException e) {
            onCompletion.onMessagePassingCompletion(command, false, e);
            HyperCard.getInstance().showErrorDialog(e);
        }
    }

    /**
     * Sends a message to this part, and if the part (or any part in the message passing hierarchy) traps the command,
     * then the given key event is consumed ({@link InputEvent#consume()}.
     *
     * In order to prevent Swing from acting on the event naturally, this method consumes the given KeyEvent and
     * re-dispatches a copy of it if this part (or any part in its message passing hierarchy) doesn't trap the message.
     *
     * In order to prevent the re-dispatched event from producing a recursive call back to this method, the
     * {@link DeferredKeyEventComponent#setPendingRedispatch(boolean)} is invoked with 'true' initially, then invoked
     * with 'false' after the message has been completely received.
     *
     * @param command The name of the command
     * @param arguments The arguments to pass to this command
     * @param e The input event to consume if the command is trapped by the part (or fails to invoke 'pass') within
     *          a short period of time).
     */
    default void receiveAndDeferKeyEvent(String command, ExpressionList arguments, KeyEvent e, DeferredKeyEventComponent c) {
        InputEvent eventCopy = new KeyEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation());
        e.consume();

        c.setPendingRedispatch(true);
        receiveMessage(command, arguments, (command1, wasTrapped, error) -> {
            if (!wasTrapped) {
                c.dispatchEvent(eventCopy);
            }

            c.setPendingRedispatch(false);
        });
    }

    /**
     * Invokes a function defined in the part's script, blocking until the function completes.
     *
     * @param function The name of the function to execute.
     * @param arguments The arguments to the function.
     * @return The value returned by the function upon completion.
     * @throws HtSemanticException Thrown if a syntax or semantic error occurs attempting to execute the function.
     */
    default Value invokeFunction(String function, ExpressionList arguments) throws HtSemanticException {
        return Interpreter.executeFunction(getMe(), getScript().getFunction(function), arguments);
    }

    /**
     * Gets the next part in the message passing order.
     * @return The next messagable part in the message passing order, or null, if we've reached the last object in the
     * hierarchy.
     */
    default Messagable getNextMessageRecipient() {

        switch (getMe().type()) {
            case BACKGROUND:
                return HyperCard.getInstance().getStack().getStackModel();
            case MESSAGE_BOX:
                return HyperCard.getInstance().getCard().getCardModel();
            case CARD:
                return HyperCard.getInstance().getCard().getCardBackground();
            case STACK:
                return null;
            case FIELD:
            case BUTTON:
                if (getMe().owner() == Owner.BACKGROUND) {
                    return HyperCard.getInstance().getCard().getCardBackground();
                } else {
                    return HyperCard.getInstance().getCard().getCardModel();
                }
        }

        throw new IllegalArgumentException("Bug! Don't know what the next message recipient is for: " + getMe().owner());
    }

}
