package by.varyvoda.matvey.analyzer.lexer.typescript.tokenizing;

import by.varyvoda.matvey.analyzer.lexer.common.exception.LexemeResolvingException;
import by.varyvoda.matvey.analyzer.lexer.typescript.types.LexemeType;
import by.varyvoda.matvey.analyzer.lexer.typescript.types.ReservedLexemes;

import java.util.Arrays;

public class TokenRefiner {

    public Token refineToken(Token rawToken) throws LexemeResolvingException {
        if (rawToken.getLexemeType() == LexemeType.WORD
                && ReservedLexemes.keyWords.stream().anyMatch(value -> value.equals(rawToken.getLexeme()))
        ) {
            if (rawToken.getLexeme().equals("false")
                    || rawToken.getLexeme().equals("true")
                    || rawToken.getLexeme().equals("null"))
                return new Token(rawToken.getLexeme(), LexemeType.LITERAL);
            return new Token(rawToken.getLexeme(), LexemeType.KEY_WORD);
        } else if(rawToken.getLexemeType() == LexemeType.NUMBER) {
            return new Token(rawToken.getLexeme(), LexemeType.LITERAL);
        }

        return Arrays.stream(LexemeType.values())
                .filter(value -> value.getLexeme().equals(rawToken.getLexeme()))
                .findAny()
                .map(lexemeType -> new Token(rawToken.getLexeme(), lexemeType))
                .orElse(rawToken);
    }
}
