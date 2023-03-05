package lexer;

/**
 * The Token class records the information for a token:
 * 1. The Symbol that describes the characters in the token
 * 2. The starting column in the source file of the token and
 * 3. The ending column in the source file of the token
 **/
public class Token {

  private int leftPosition, rightPosition;
  private Symbol symbol;
  public int lineNumber = 1;
  /**
   * Create a new Token based on the given Symbol
   * 
   * @param leftPosition  is the source file column where the Token begins
   * @param rightPosition is the source file column where the Token ends
   */
  public Token(int leftPosition, int rightPosition, Symbol symbol){
    this.leftPosition = leftPosition;
    this.rightPosition = rightPosition;
    this.symbol = symbol;
  }
  public Token(int leftPosition, int rightPosition, Symbol symbol,int lineNumber){
    this.leftPosition = leftPosition;
    this.rightPosition = rightPosition;
    this.symbol = symbol;
    this.lineNumber = lineNumber;
    
  }

  public Symbol getSymbol() {
    return symbol;
  }
  
  
  public void print() {
    String.format(
      "       %s        : %d right: %d",
      symbol.toString(),
      leftPosition,
      rightPosition);
  
  }

  public String toString() {
    return symbol.toString();//String.format("%1s   ",symbol.toString());
  }

  public String getLexeme() {
    return symbol.toString();
  }

  public int getLeftPosition() {
    return leftPosition;
  }

  public int getRightPosition() {
    return rightPosition;
  }

  public int getLineNumber(){
    return lineNumber;
   }

  /**
   * @return the integer that represents the kind of symbol we have which
   *         is actually the type of token associated with the symbol
   */
  public Tokens getKind() {
    return symbol.getKind();
  }
}
