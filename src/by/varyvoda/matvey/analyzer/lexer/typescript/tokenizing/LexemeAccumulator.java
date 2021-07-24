package by.varyvoda.matvey.analyzer.lexer.typescript.tokenizing;

import by.varyvoda.matvey.analyzer.lexer.common.exception.CharAnalyzingException;
import by.varyvoda.matvey.analyzer.lexer.typescript.types.CharType;
import by.varyvoda.matvey.analyzer.lexer.typescript.types.LexemeType;
import by.varyvoda.matvey.analyzer.lexer.typescript.types.ReservedLexemes;

import java.util.ArrayList;
import java.util.List;

public class LexemeAccumulator {

    private String currentLexeme;
    private LexemeType currentLexemeType;
    private List<Token> tokensForFlushing;

    public LexemeAccumulator() {
        init();
    }

    private void init() {
        currentLexeme = "";
        currentLexemeType = LexemeType.UNDEFINED;
        tokensForFlushing = new ArrayList<>();
    }

    public void reset() {
        init();
    }

    public boolean analyzeChar(char c) throws CharAnalyzingException {
        switch (getCharType(c)) {
            case UNKNOWN:
                throw new CharAnalyzingException("Unknown char: '" + c + "'");
            case DELIMITER:
            case NEW_LINE:
                prepareForFlushing();
                return true;
            case DIGIT:
                if (currentLexemeType == LexemeType.UNDEFINED || currentLexeme.equals(".")) {
                    currentLexemeType = LexemeType.NUMBER;
                }
                currentLexeme = currentLexeme + c;
                break;
            case LETTER:
                if (currentLexemeType != LexemeType.NUMBER) {
                    boolean wasFlushed = false;
                    if (currentLexemeType != LexemeType.WORD) {
                        prepareForFlushing();
                        currentLexemeType = LexemeType.WORD;
                        wasFlushed = true;
                    }
                    currentLexeme = currentLexeme + c;
                    return wasFlushed;
                } else {
                    throw new CharAnalyzingException("Char: " + c + " is not compatible with lexeme type '" + currentLexemeType.name() + "'");
                }
            case DOT:
                if (currentLexemeType == LexemeType.NUMBER) {
                    if (currentLexeme.contains(c + "")) {
                        throw new CharAnalyzingException("Dot is already in number.");
                    } else {
                        currentLexeme = currentLexeme + c;
                    }
                    break;
                } else {
                    if (currentLexemeType == LexemeType.THREE_DOT)
                        throw new CharAnalyzingException("Too many dots.");
                    if (currentLexeme.equals(".")) {
                        currentLexeme = currentLexeme + c;
                        currentLexemeType = LexemeType.TWO_DOT;
                        break;
                    } else if (currentLexeme.equals("..")) {
                        currentLexeme = currentLexeme + c;
                        currentLexemeType = LexemeType.THREE_DOT;
                        break;
                    }
                }
                prepareForFlushing();
                currentLexeme = c + "";
                currentLexemeType = LexemeType.DOT;
                return true;
            case OPERATION:
                if (c == '-' && currentLexemeType != LexemeType.NUMBER) {
                    prepareForFlushing();
                    currentLexeme = c + "";
                    currentLexemeType = LexemeType.NUMBER;
                    return true;
                } else if (currentLexemeType == LexemeType.OPERATION) {
                    currentLexeme = currentLexeme + c;
                } else {
                    prepareForFlushing();
                    currentLexeme = c + "";
                    currentLexemeType = LexemeType.OPERATION;
                    return true;
                }
            case SEPARATOR:
                prepareForFlushing();
                currentLexemeType = LexemeType.SEPARATOR;
                currentLexeme = c + "";
                prepareForFlushing();
                return true;

        }
        return false;
    }

    private void prepareForFlushing() {
        if (currentLexemeType != LexemeType.UNDEFINED) {
            tokensForFlushing.add(new Token(currentLexeme, currentLexemeType));
            currentLexeme = "";
            currentLexemeType = LexemeType.UNDEFINED;
        }
    }

    private static CharType getCharType(char c) {
        if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_') return CharType.LETTER;
        if (c >= '0' && c <= '9') return CharType.DIGIT;
        if (ReservedLexemes.separators.stream().anyMatch(value -> value.equals(c + ""))) return CharType.SEPARATOR;
        if (ReservedLexemes.operations.stream().anyMatch(value -> value.startsWith(c + ""))) return CharType.OPERATION;
        switch (c) {
            case ' ':
            case '\t':
                return CharType.DELIMITER;
            case '\n':
            case '\r':
                return CharType.NEW_LINE;
            case '.':
                return CharType.DOT;
            default:
                return CharType.UNKNOWN;
        }
    }

    public List<Token> flush() {
        List<Token> answer = new ArrayList<>(tokensForFlushing);
        tokensForFlushing.clear();
        return answer;
    }
}
