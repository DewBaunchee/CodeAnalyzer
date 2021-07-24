package by.varyvoda.matvey.analyzer.lexer.typescript.types;

public enum LexemeType {
    NUMBER(""), LITERAL(""), WORD(""), KEY_WORD(""), SEPARATOR(""), OPERATION(""),
    DOT("."), TWO_DOT(".."), THREE_DOT("..."), COMMA(","), COLON(":"), DOT_COMMA(";"),
    O_ROUND_BRACKET("("), C_ROUND_BRACKET(")"), O_SQ_BRACKET("["), C_SQ_BRACKET("]"), O_FIG_BRACKET("{"), C_FIG_BRACKET("}"),
    UNDEFINED("");

    private final String lexeme;

    LexemeType(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }
}
