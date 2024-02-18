package com.epam.mjc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MethodParser {

    /**
     * Parses string that represents a method signature and stores all it's members into a {@link MethodSignature} object.
     * signatureString is a java-like method signature with following parts:
     *      1. access modifier - optional, followed by space: ' '
     *      2. return type - followed by space: ' '
     *      3. method name
     *      4. arguments - surrounded with braces: '()' and separated by commas: ','
     * Each argument consists of argument type and argument name, separated by space: ' '.
     * Examples:
     *      accessModifier returnType methodName(argumentType1 argumentName1, argumentType2 argumentName2)
     *      private void log(String value)
     *      Vector3 distort(int x, int y, int z, float magnitude)
     *      public DateTime getCurrentDateTime()
     *
     * @param signatureString source string to parse
     * @return {@link MethodSignature} object filled with parsed values from source string
     */
    public MethodSignature parseFunction(String signatureString) {
        String[] signatures = parseMethodSignature(signatureString);
        List<MethodSignature.Argument> argumentList = parseMethodArguments(signatureString);

        MethodSignature ms = argumentList.isEmpty() ?
                new MethodSignature(signatures[2]) :
                new MethodSignature(signatures[2], argumentList);

        ms.setAccessModifier(signatures[0]);
        ms.setReturnType(signatures[1]);
        ms.setMethodName(signatures[2]);

        return ms;
    }

    private String[] parseMethodSignature(String signatureString) {
        String accessModifier = null;
        String[] splitStr = signatureString.split(" ");

        if (Objects.equals(splitStr[0], "public") || Objects.equals(splitStr[0], "protected") || Objects.equals(splitStr[0], "private")) {
            accessModifier = splitStr[0];
        }

        String returnType = accessModifier == null ? splitStr[0] : splitStr[1];
        String methodName = accessModifier == null ? splitStr[1] : splitStr[2];
        String finalMethodName = methodName.split("\\(")[0];

        return new String[]{accessModifier, returnType, finalMethodName};
    }

    private List<MethodSignature.Argument> parseMethodArguments(String signatureString) {
        List<MethodSignature.Argument> argumentList = new ArrayList<>();
        String[] splitStr = signatureString.split("\\(");

        String methodArgsStr = null;
        if (splitStr[1] != null && !splitStr[1].isEmpty()) {
            methodArgsStr = splitStr[1].substring(0, splitStr[1].length() - 1);
        }

        String[] spitMethodArgs = null;
        if (methodArgsStr != null && !Objects.equals(methodArgsStr, "")) {
            spitMethodArgs = methodArgsStr.split(", ");

            for (String spitMethodArg : spitMethodArgs) {
                String[] newSplitStr = spitMethodArg.split(" ");
                argumentList.add(new MethodSignature.Argument(newSplitStr[0], newSplitStr[1]));
            }
        }

        return argumentList;
    }
}
