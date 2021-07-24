package by.varyvoda.matvey.analyzer.lexer.typescript.types;

import java.util.List;

public class ReservedLexemes {

    public static final List<String> operations = List.of(
            "+", "-", "*", "/", "%",
            "=", "+=", "-=", "/=", "*=", "%=",
            ",", ".", "..", "...", "&", "|", "~", "^",
            "<", ">", "<=", ">=", "==", "===", "!", "!=", "&&", "||");

    public static final List<String> separators = List.of("(", ")", "[", "]", "{", "}", ":", ";");

    public static final List<String> keyWords = List.of(
            "break", "case", "catch", "continue", "default", "delete", "do",
            "else", "false", "finally", "for", "function", "if", "in", "let",
            "instanceof", "new", "null", "return", "switch", "this", "throw",
            "true", "try", "typeof", "var", "void", "while", "with",
            "double", "abstract", "byte", "char", "class", "const", "debugger",
            "enum", "export", "extends", "final", "float", "goto", "implements",
            "import", "int", "interface", "long", "native", "package", "private",
            "protected", "public", "short", "static", "super", "synchronized", "throws",
            "transient", "string", "number", "volatile");
}
