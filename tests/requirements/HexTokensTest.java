package tests.requirements;

// Spring 2023 Tests
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.Stream;
import lexer.Lexer;
import lexer.Token;
import lexer.Tokens;
import tests.lexer.Helpers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class HexTokensTest {
  private final PrintStream standardOut = System.out;
  private final PrintStream standardError = System.err;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @BeforeEach
  public void setUp() {
    PrintStream captor = new PrintStream(outputStreamCaptor);

    System.setOut(captor);
    System.setErr(captor);
  }

  @AfterEach
  public void tearDown() {
    System.setOut(standardOut);
    System.setErr(standardError);
  }

  private static Stream<Arguments> provideValidTokenSources() {
    return Stream.of(
        Arguments.of("0x123456"),
        Arguments.of("0x1A2b3C"),
        Arguments.of("0X123456"),
        Arguments.of("0X1A2b3C"));
  }

  @ParameterizedTest
  @MethodSource("provideValidTokenSources")
  void testValidHexTokens(String source) {
    try {
      Lexer lexer = Helpers.getTestLexer(String.format(" %s ", source));
      Token token = lexer.nextToken();

      assertEquals(Tokens.HexLit, token.getKind());
      assertEquals(source, token.getSymbol().toString());
      assertEquals(1, token.getLeftPosition());
      assertEquals(source.length(), token.getRightPosition());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private static Stream<Arguments> provideValidGreedyTokens() {
    return Stream.of(
        Arguments.of("0x1234567", "0x123456", Tokens.INTeger, "7"),
        Arguments.of("0xabcdefg", "0xabcdef", Tokens.Identifier, "g"),
        Arguments.of("0Xabcdefg", "0Xabcdef", Tokens.Identifier, "g"));
  }

  @ParameterizedTest
  @MethodSource("provideValidGreedyTokens")
  void testValidGreedyTokens(String source, String hexLit, Tokens expectedKind, String literal)
      throws IOException, Exception {
    Lexer lexer = Helpers.getTestLexer(String.format(" %s ", source));

    Token token = lexer.nextToken();
    assertEquals(Tokens.HexLit, token.getKind());
    assertEquals(hexLit, token.getLexeme());

    token = lexer.nextToken();
    assertEquals(expectedKind, token.getKind());
    assertEquals(literal, token.getLexeme());
  }

  private static Stream<Arguments> provideInvalidTokenSources() {
    return Stream.of(
        Arguments.of("0xX123456", "X"),
        Arguments.of("0xx123456", "x"),
        Arguments.of("0xg12345", "g"),
        Arguments.of("0x12345g", "g"),
        Arguments.of("0x12345 ", " "));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidTokenSources")
  void testInvalidTokens(String source, String expectedChar) {
    try {
      Lexer lexer = Helpers.getTestLexer(String.format(" %s ", source));

      lexer.nextToken();
      assertEquals(
          Helpers.getTestOutput(source, expectedChar),
          outputStreamCaptor.toString());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}