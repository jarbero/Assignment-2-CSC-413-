package lexer;

import lexer.readers.IReader;
import lexer.readers.SourceReader;

/**
 * The Lexer class is responsible for scanning the source file
 * which is a stream of characters and returning a stream of
 * tokens; each token object will contain the string (or access
 * to the string) that describes the token along with an
 * indication of its location in the source program to be used
 * for error reporting; we are tracking line numbers; white spaces
 * are space, tab, newlines
 */
public class Lexer implements ILexer {

  // next character to process
  private char ch;
  private IReader source;

  // positions in line of current token
  private int startPosition, endPosition;

  /**
   * Lexer constructor
   * 
   * @param sourceFile is the name of the File to read the program source from
   */
  public Lexer(String sourceFile) throws Exception {
    this(new SourceReader(sourceFile));
  }

  public Lexer(IReader reader) throws Exception {
    TokenType.init();
    this.source = reader;
    nextChar();
  }

  public Token newToken(String tokenString, int start, int end, Tokens type) {
    return new Token(start, end, Symbol.symbol(tokenString, type));
  }

  private void nextChar() {
    ch = source.read();
    endPosition++;
  }

  private void scanPastWhitespace() {
    while (Character.isWhitespace(ch) && !eofReached()) {
      nextChar();
    }
  }

  private Token reservedWordOrIdentifier() {
    String identifier = "";

    do {
      identifier += ch;
      nextChar();
    } while (Character.isJavaIdentifierPart(ch) && !eofReached());

    return newToken(
        identifier,
        startPosition,
        endPosition - 1,
        Tokens.Identifier);
  }

  private Token integer() {
    String number = "";

    do {
      number += ch;
      nextChar();
    } while (Character.isDigit(ch) && !eofReached());

    return newToken(number, startPosition, endPosition - 1, Tokens.INTeger);
  }

  /**
   * Prints out an error string and returns the EOF token to halt lexing
   */
  private Token error(String errorString) {
    System.err.println(
        String.format("******** illegal character: %s", errorString));

    return newToken(null, startPosition, endPosition, Tokens.EOF);
  }

  private void ignoreComment() {
    int oldLine = source.getLineNumber();

    do {
      nextChar();
    } while (oldLine == source.getLineNumber() && !eofReached());
  }

  private boolean eofReached() {
    return ch == '\0';
  }

  private Token singleCharacterOperatorOrSeparator(String character) {
    Symbol symbol = Symbol.symbol(character, Tokens.BogusToken);

    // If symbol is still null, we did not find an operator in the symbol table,
    // and did not encounter the end of file, so this is an error
    if (symbol == null) {
      return error(character);
    } else {
      return newToken(
          character,
          startPosition,
          // -1 since we got next character to test for 2 char operators
          endPosition - 1,
          symbol.getKind());
    }
  }

  private Token operatorOrSeparator() {
    String singleCharacter = "" + ch;

    if (eofReached()) {
      return newToken(singleCharacter, startPosition, endPosition, Tokens.EOF);
    }

    // We might have a two character operator, so we need to test for that first
    // by looking ahead one character.
    nextChar();

    String doubleCharacter = singleCharacter + ch;
    Symbol symbol = Symbol.symbol(doubleCharacter, Tokens.BogusToken);

    if (symbol == null) {
      // A two character operator was not found in the symbol table,
      // so this must be a single character operator (or invalid)
      return singleCharacterOperatorOrSeparator(singleCharacter);
    } else if (symbol.getKind() == Tokens.Comment) {
      ignoreComment();
      return nextToken();
    } else {
      // We have a valid, two character operator (advance past second char)
      nextChar();

      return newToken(
          doubleCharacter,
          startPosition,
          endPosition - 1,
          symbol.getKind());
    }
  }

  /**
   * @return the next Token found in the source file
   */
  public Token nextToken() {
    
    scanPastWhitespace();

    startPosition = source.getColumn();
    endPosition = startPosition;
     
    if (Character.isJavaIdentifierStart(ch)) {
      return reservedWordOrIdentifier();
    }

    if(ch =='0'){
     String hex = "";
      hex += ch;
      nextChar();
      if(Character.toLowerCase(ch) == 'x'){
        hex += ch;
        nextChar();
        int i =0;
        if((Character.isDigit(ch))||(ch =='A')||(ch =='B')||(ch =='C')||(ch =='D')||(ch =='E')||(ch =='F')){
          do{
             hex+=ch;
             nextChar();
             i++;
            
          }while(i !=6 && Character.isDigit(ch)||(ch =='A')||(ch =='B')||(ch =='C')||(ch =='D')||(ch =='E')||(ch =='F')||(ch =='a')||(ch =='b')||(ch =='c')||(ch =='d')||(ch =='e')||(ch =='f'));
          if(i < 6 ||Character.isDigit(ch)||(ch !='A')||(ch !='B')||(ch !='C')||(ch !='D')||(ch !='E')||(ch !='F')||(ch !='a')||(ch !='b')||(ch !='c')||(ch !='d')||(ch !='e')||(ch !='f') ){
            System.out.println("******** illegal character: "+ch);
 
          }
        }else{
          System.out.println("******** illegal character: "+ch);
        }
      }
        return newToken(hex, startPosition, endPosition-1, Tokens.HexLit);
     
    }

    if(ch == '@'){  
        String literal = "";

          do {
            literal += ch;
            nextChar();
          } while (ch != '@');
          
    if(eofReached()){
      System.out.println("END OF FILE REACHED");
    }
      literal = literal.substring(1);
      return newToken(literal, startPosition, endPosition, Tokens.StringLit);
    }
    
    if (Character.isDigit(ch)) {
      return integer();
    }

    return operatorOrSeparator();
  }

  /**
   * Used by the constrainer to build intrinsic trees
   */
  public Token anonymousIdentifierToken(String identifier) {
    return newToken(identifier, -1, -1, Tokens.Identifier);
  }
  public static void main(String args[]) {
  
 if(args.length == 1){
    System.out.println(args[0]);
     try {
      Lexer lex = new Lexer("sample_files/simple.x");
      Token token = lex.nextToken();

      while (token.getKind() != Tokens.EOF) {
         String p = String.format(
           "%s   Left:  %d Right: %d",
            TokenType.tokens.get(token.getKind()),
            token.getLeftPosition(),
            token.getRightPosition());

        if (token.getKind() == Tokens.Identifier ||
            token.getKind() == Tokens.INTeger) {
          p += " " + token.toString();
        }

        System.out.println(p + " " + lex.source.getLineNumber());

        token = lex.nextToken();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    }else{
      System.out.println("usage: java lexer.Lexer filename.x");
    }
  }
}
