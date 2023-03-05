package lexer;

import java.util.HashMap;

/**
 * The Symbol class is used to store all user strings along with
 * an indication of the kind of strings they are; e.g. the id "abc" will
 * store the "abc" in name and Sym.Tokens.Identifier in kind
 **/
public class Symbol {
  // symbols contains all strings in the source program
  private static HashMap<String, Symbol> symbols = new HashMap<>();

  private String lexeme;
  // token kind of symbol
  private Tokens kind;

  private Symbol(String lexeme, Tokens kind) {
    this.lexeme = lexeme;
    this.kind = kind;
  }

  public String toString() {
    return lexeme;
  }

  public Tokens getKind() {
    return kind;
  }

  /**
   * Return the unique symbol associated with a string.
   * Repeated calls to symbol("abc") will return the same Symbol.
   */
  public static Symbol symbol(String newTokenString, Tokens kind) {
    Symbol s = symbols.get(newTokenString);

    if (s == null) {
      if (kind == Tokens.BogusToken) {
        // bogus string so don't enter into symbols
        return null;
      }
      s = new Symbol(newTokenString, kind);
      symbols.put(newTokenString, s);
    }

    return s;
  }
}
