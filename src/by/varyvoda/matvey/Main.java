package by.varyvoda.matvey;

import by.varyvoda.matvey.analyzer.lexer.common.exception.LexerException;
import by.varyvoda.matvey.analyzer.lexer.typescript.Lexer;
import by.varyvoda.matvey.analyzer.lexer.typescript.tokenizing.Token;
import by.varyvoda.matvey.analyzer.lexer.typescript.types.LexemeType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            Lexer lexer = new Lexer(new File("progForAnalyze/def.ts"));

            Map<String, Integer> operators = new HashMap<>();
            Map<String, Integer> operands = new HashMap<>();
            List<Token> tokens = lexer.parseToLexemes();

            for(int i =  0; i < tokens.size(); i++) {
                Token current = tokens.get(i);
                LexemeType lexemeType = current.getLexemeType();
                String lexeme = current.getLexeme();

                if(lexemeType == LexemeType.LITERAL
                        || (lexemeType == LexemeType.WORD && tokens.get(i + 1).getLexemeType() != LexemeType.O_ROUND_BRACKET)) {
                    operands.put(lexeme, operands.getOrDefault(lexeme, 0) + 1);
                } else {
                    if(lexemeType == LexemeType.OPERATION
                    || lexeme.matches("[,.(\\[{]"))
                        operators.put(lexeme, operators.getOrDefault(lexeme, 0) + 1);
                }
            }

            System.out.println(operators);
            System.out.println(operands);
        } catch (IOException | LexerException e) {
            e.printStackTrace();
        }
    }
}
