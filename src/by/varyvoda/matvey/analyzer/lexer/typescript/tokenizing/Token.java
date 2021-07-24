package by.varyvoda.matvey.analyzer.lexer.typescript.tokenizing;

import by.varyvoda.matvey.analyzer.lexer.typescript.types.LexemeType;

import java.util.Objects;

public class Token {

    private final String lexeme;

    private final LexemeType lexemeType;

    public Token(String lexeme, LexemeType lexemeType) {
        this.lexeme = lexeme;
        this.lexemeType = lexemeType;
    }

    public String getLexeme() {
        return lexeme;
    }

    public LexemeType getLexemeType() {
        return lexemeType;
    }

    @Override
    public boolean equals(Object comparable) {
        if (this == comparable) return true;
        if (comparable == null || getClass() != comparable.getClass()) return false;
        Token comparableToken = (Token) comparable;
        return Objects.equals(lexeme, comparableToken.lexeme) && Objects.equals(lexemeType, comparableToken.lexemeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexeme, lexemeType);
    }

    @Override
    public String toString() {
        return "{\"" + lexeme + "\" - " + lexemeType + "}";
    }
}
