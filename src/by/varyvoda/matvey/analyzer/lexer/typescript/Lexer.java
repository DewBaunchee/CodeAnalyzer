package by.varyvoda.matvey.analyzer.lexer.typescript;

import by.varyvoda.matvey.analyzer.lexer.common.exception.CharAnalyzingException;
import by.varyvoda.matvey.analyzer.lexer.common.exception.LexemeResolvingException;
import by.varyvoda.matvey.analyzer.lexer.common.exception.LexerException;
import by.varyvoda.matvey.analyzer.lexer.typescript.types.LexemeType;
import by.varyvoda.matvey.analyzer.lexer.typescript.tokenizing.LexemeAccumulator;
import by.varyvoda.matvey.analyzer.lexer.typescript.tokenizing.Token;
import by.varyvoda.matvey.analyzer.lexer.typescript.tokenizing.TokenRefiner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Lexer {

    public Lexer(File source) throws IOException {
        this(Files.readString(source.toPath()).toCharArray());
    }

    public Lexer(String code) {
        this(code.toCharArray());
    }

    public Lexer(char[] code) {
        this.lexemeAccumulator = new LexemeAccumulator();
        this.tokenRefiner = new TokenRefiner();
        this.code = code;
    }

    private final LexemeAccumulator lexemeAccumulator;
    private final TokenRefiner tokenRefiner;

    private final char[] code;

    private void init() {
        lexemeAccumulator.reset();
    }

    public List<Token> parseToLexemes() throws LexerException {
        init();
        List<Token> tokenList = new ArrayList<>();
        for (int currentPos = 0; currentPos < code.length; currentPos++) {
            if (stringEquals(code, currentPos, "//")) {
                currentPos = find(code, currentPos + 2, "\n");
                continue;
            }

            if (stringEquals(code, currentPos, "/*")) {
                currentPos = find(code, currentPos + 2, "*/") + 1;
                continue;
            }

            if (code[currentPos] == '"' || code[currentPos] == '\'' || code[currentPos] == '`') {
                int startOfString = currentPos;
                currentPos = findStringEnd(code, currentPos + 1, code[currentPos] + "");
                tokenList.add(new Token(getSubstring(code, startOfString, currentPos + 1), LexemeType.LITERAL));
                continue;
            }

            try {
                if (lexemeAccumulator.analyzeChar(code[currentPos])) {
                    List<Token> rawTokens = lexemeAccumulator.flush();
                    for (Token rawToken : rawTokens) {
                        tokenList.add(tokenRefiner.refineToken(rawToken));
                    }
                }
            } catch (CharAnalyzingException | LexemeResolvingException e) {
                e.printStackTrace();
                int start = currentPos > 20 ? currentPos - 20 : 0;
                int end = code.length - currentPos > 20 ? currentPos + 20 : code.length;
                throw new LexerException("Error near pos " + currentPos + ": " + new String(Arrays.copyOfRange(code, start, end)));
            }
        }
        return tokenList;
    }

    private static int findStringEnd(char[] code, int startIndex, String quote) {
        while (startIndex < code.length) {
            if (code[startIndex] == '\\') {
                startIndex++;
            } else {
                if (stringEquals(code, startIndex, quote)) return startIndex;
            }
            startIndex++;
        }
        return -1;
    }

    private static String getSubstring(char[] array, int start, int end) {
        return new String(Arrays.copyOfRange(array, start, end));
    }

    private static int find(char[] code, int startIndex, String pattern) {
        if (code.length <= startIndex)
            throw new IndexOutOfBoundsException("Array length: " + code.length + ", index: " + startIndex);

        while (code.length - startIndex < pattern.length()) {
            boolean allEqual = true;
            for (int i = startIndex; i < code.length; i++) {
                if (code[i] != pattern.charAt(i - startIndex)) {
                    allEqual = false;
                    break;
                }
            }
            if (allEqual) return startIndex;
            startIndex++;
        }
        return -1;
    }

    private static boolean stringEquals(char[] code, int position, String pattern) {
        if (code.length <= position)
            throw new IndexOutOfBoundsException("Array length: " + code.length + ", index: " + position);
        if (code.length - position < pattern.length()) return false;

        for (int i = 0; i < pattern.length(); i++) {
            if (code[position + i] != pattern.charAt(i)) return false;
        }
        return true;
    }
}
