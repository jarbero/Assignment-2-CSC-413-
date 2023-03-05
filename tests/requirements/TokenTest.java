package tests.requirements;

// Spring 2023 tests
import static org.junit.Assert.assertEquals;

import lexer.Symbol;
import lexer.Token;
import lexer.Tokens;
import org.junit.jupiter.api.Test;

public class TokenTest {
  private static final int LEFT_POSITION = 7, RIGHT_POSITION = 42;
  private static final int LINE_NUMBER = 1001;

  @Test
  void testCurrentConstructorForLineNumber() {
    Token token = new Token(
        LEFT_POSITION,
        RIGHT_POSITION,
        Symbol.symbol("+", Tokens.Plus));

    assertEquals(-1, token.getLineNumber());
  }

  @Test
  void testNewConstructorForLineNumber() {
    Token token = new Token(
        LEFT_POSITION,
        RIGHT_POSITION,
        Symbol.symbol("+", Tokens.Plus),
        LINE_NUMBER);

    assertEquals(LINE_NUMBER, token.getLineNumber());
  }

  @Test
  void testToString() {
    Token token = new Token(
        LEFT_POSITION,
        RIGHT_POSITION,
        Symbol.symbol("+", Tokens.Plus),
        LINE_NUMBER);
    String testString = "+           left: " +
        LEFT_POSITION +
        "        right: " +
        RIGHT_POSITION +
        "       line: " +
        LINE_NUMBER +
        "     Plus";

    assertEquals(testString, token.toString());
  }
}