import java.util.ArrayList;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class LRparser {

    private ArrayList<Token> tokenList;
    private Stack<Integer> stateStack;
    private Stack<String> symbolStack;
    private int tokenIdx;
    private String parsingErrorMsg;

    /**
     * Holds actions for each state.
     * 
     * 1st key: state -> corresponding action row
     * 2nd key: symbol -> action (shift, reduce, accept, error)
     */
    private Map<Integer, Map<String, String>> actionTable;
    /**
     * Holds goto for each state,
     * 
     * 1st key: state -> corresponding goto row
     * 2nd key: symbol -> goto (new state)
     */
    private Map<Integer, Map<String, Integer>> gotoTable;

    public LRparser(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
        this.stateStack = new Stack<>();
        this.symbolStack = new Stack<>();
        this.tokenIdx = 0;
        this.parsingErrorMsg = "";

        initializeParsingTables();
    }

    private void initializeParsingTables() {
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();

        // ACTION
        addAction(0, "program", "s2");
        addAction(10, "break", "r7");
        addAction(10, "display", "r7");
        addAction(10, "end", "r7");
        addAction(10, "for", "r7");
        addAction(10, "identifier", "r7");
        addAction(10, "if", "r7");
        addAction(10, "int", "r7");
        addAction(10, "integer", "r7");
        addAction(10, "print_line", "r7");
        addAction(10, "while", "r7");
        addAction(100, "(", "s139");
        addAction(100, "identifier", "s137");
        addAction(100, "number_literal", "s138");
        addAction(101, "(", "s74");
        addAction(101, "identifier", "s72");
        addAction(101, "number_literal", "s73");
        addAction(102, "(", "s74");
        addAction(102, "identifier", "s72");
        addAction(102, "number_literal", "s73");
        addAction(103, "*", "r39");
        addAction(103, "+", "r39");
        addAction(103, "-", "r39");
        addAction(103, "/", "r39");
        addAction(103, ";", "r39");
        addAction(103, "<", "r39");
        addAction(103, "=", "r39");
        addAction(103, "==", "r39");
        addAction(103, ">", "r39");
        addAction(104, ")", "s142");
        addAction(105, ";", "r21");
        addAction(106, ";", "r22");
        addAction(107, ",", "s78");
        addAction(107, ";", "r25");
        addAction(108, ",", "r27");
        addAction(108, ";", "r27");
        addAction(109, "+", "s52");
        addAction(109, ",", "r30");
        addAction(109, "-", "s53");
        addAction(109, ";", "r30");
        addAction(109, "<", "s48");
        addAction(109, "=", "s50");
        addAction(109, "==", "s51");
        addAction(109, ">", "s49");
        addAction(11, ";", "s29");
        addAction(110, "*", "s55");
        addAction(110, "+", "r33");
        addAction(110, ",", "r33");
        addAction(110, "-", "r33");
        addAction(110, "/", "s56");
        addAction(110, ";", "r33");
        addAction(110, "<", "r33");
        addAction(110, "=", "r33");
        addAction(110, "==", "r33");
        addAction(110, ">", "r33");
        addAction(111, "*", "r35");
        addAction(111, "+", "r35");
        addAction(111, ",", "r35");
        addAction(111, "-", "r35");
        addAction(111, "/", "r35");
        addAction(111, ";", "r35");
        addAction(111, "<", "r35");
        addAction(111, "=", "r35");
        addAction(111, "==", "r35");
        addAction(111, ">", "r35");
        addAction(112, "*", "r36");
        addAction(112, "+", "r36");
        addAction(112, "++", "s147");
        addAction(112, ",", "r36");
        addAction(112, "-", "r36");
        addAction(112, "/", "r36");
        addAction(112, ";", "r36");
        addAction(112, "<", "r36");
        addAction(112, "=", "r36");
        addAction(112, "==", "r36");
        addAction(112, ">", "r36");
        addAction(113, "*", "r37");
        addAction(113, "+", "r37");
        addAction(113, ",", "r37");
        addAction(113, "-", "r37");
        addAction(113, "/", "r37");
        addAction(113, ";", "r37");
        addAction(113, "<", "r37");
        addAction(113, "=", "r37");
        addAction(113, "==", "r37");
        addAction(113, ">", "r37");
        addAction(114, "(", "s64");
        addAction(114, "identifier", "s62");
        addAction(114, "number_literal", "s63");
        addAction(115, ";", "r29");
        addAction(116, "begin", "s66");
        addAction(117, "begin", "s45");
        addAction(118, "break", "r2");
        addAction(118, "display", "r2");
        addAction(118, "else", "r2");
        addAction(118, "else_if", "r2");
        addAction(118, "end", "r2");
        addAction(118, "for", "r2");
        addAction(118, "identifier", "r2");
        addAction(118, "if", "r2");
        addAction(118, "int", "r2");
        addAction(118, "integer", "r2");
        addAction(118, "print_line", "r2");
        addAction(118, "while", "r2");
        addAction(119, "(", "s89");
        addAction(119, "identifier", "s87");
        addAction(119, "number_literal", "s88");
        addAction(12, "(", "s36");
        addAction(12, "identifier", "s34");
        addAction(12, "number_literal", "s35");
        addAction(120, "(", "s89");
        addAction(120, "identifier", "s87");
        addAction(120, "number_literal", "s88");
        addAction(121, "*", "r39");
        addAction(121, "+", "r39");
        addAction(121, "-", "r39");
        addAction(121, "/", "r39");
        addAction(121, "begin", "r39");
        addAction(122, ")", "s153");
        addAction(123, ")", "r31");
        addAction(123, "+", "s52");
        addAction(123, "-", "s53");
        addAction(124, ")", "r33");
        addAction(124, "*", "s55");
        addAction(124, "+", "r33");
        addAction(124, "-", "r33");
        addAction(124, "/", "s56");
        addAction(125, ")", "r35");
        addAction(125, "*", "r35");
        addAction(125, "+", "r35");
        addAction(125, "-", "r35");
        addAction(125, "/", "r35");
        addAction(126, ")", "r36");
        addAction(126, "*", "r36");
        addAction(126, "+", "r36");
        addAction(126, "++", "s156");
        addAction(126, "-", "r36");
        addAction(126, "/", "r36");
        addAction(127, ")", "r37");
        addAction(127, "*", "r37");
        addAction(127, "+", "r37");
        addAction(127, "-", "r37");
        addAction(127, "/", "r37");
        addAction(128, "(", "s64");
        addAction(128, "identifier", "s62");
        addAction(128, "number_literal", "s63");
        addAction(129, ")", "r32");
        addAction(129, "*", "s55");
        addAction(129, "+", "r32");
        addAction(129, "-", "r32");
        addAction(129, "/", "s56");
        addAction(129, "<", "r32");
        addAction(129, "=", "r32");
        addAction(129, "==", "r32");
        addAction(129, ">", "r32");
        addAction(13, "(", "s36");
        addAction(13, "identifier", "s34");
        addAction(13, "number_literal", "s35");
        addAction(130, ")", "r34");
        addAction(130, "*", "r34");
        addAction(130, "+", "r34");
        addAction(130, "-", "r34");
        addAction(130, "/", "r34");
        addAction(130, "<", "r34");
        addAction(130, "=", "r34");
        addAction(130, "==", "r34");
        addAction(130, ">", "r34");
        addAction(131, ")", "r38");
        addAction(131, "*", "r38");
        addAction(131, "+", "r38");
        addAction(131, "-", "r38");
        addAction(131, "/", "r38");
        addAction(131, "<", "r38");
        addAction(131, "=", "r38");
        addAction(131, "==", "r38");
        addAction(131, ">", "r38");
        addAction(132, "break", "r2");
        addAction(132, "display", "r2");
        addAction(132, "end", "r2");
        addAction(132, "for", "r2");
        addAction(132, "identifier", "r2");
        addAction(132, "if", "r2");
        addAction(132, "int", "r2");
        addAction(132, "integer", "r2");
        addAction(132, "print_line", "r2");
        addAction(132, "while", "r2");
        addAction(133, ";", "s158");
        addAction(134, "+", "s52");
        addAction(134, "-", "s53");
        addAction(134, ";", "r31");
        addAction(135, "*", "s55");
        addAction(135, "+", "r33");
        addAction(135, "-", "r33");
        addAction(135, "/", "s56");
        addAction(135, ";", "r33");
        addAction(136, "*", "r35");
        addAction(136, "+", "r35");
        addAction(136, "-", "r35");
        addAction(136, "/", "r35");
        addAction(136, ";", "r35");
        addAction(137, "*", "r36");
        addAction(137, "+", "r36");
        addAction(137, "++", "s161");
        addAction(137, "-", "r36");
        addAction(137, "/", "r36");
        addAction(137, ";", "r36");
        addAction(138, "*", "r37");
        addAction(138, "+", "r37");
        addAction(138, "-", "r37");
        addAction(138, "/", "r37");
        addAction(138, ";", "r37");
        addAction(139, "(", "s64");
        addAction(139, "identifier", "s62");
        addAction(139, "number_literal", "s63");
        addAction(14, "(", "s38");
        addAction(140, "*", "s55");
        addAction(140, "+", "r32");
        addAction(140, "-", "r32");
        addAction(140, "/", "s56");
        addAction(140, ";", "r32");
        addAction(140, "<", "r32");
        addAction(140, "=", "r32");
        addAction(140, "==", "r32");
        addAction(140, ">", "r32");
        addAction(141, "*", "r34");
        addAction(141, "+", "r34");
        addAction(141, "-", "r34");
        addAction(141, "/", "r34");
        addAction(141, ";", "r34");
        addAction(141, "<", "r34");
        addAction(141, "=", "r34");
        addAction(141, "==", "r34");
        addAction(141, ">", "r34");
        addAction(142, "*", "r38");
        addAction(142, "+", "r38");
        addAction(142, "-", "r38");
        addAction(142, "/", "r38");
        addAction(142, ";", "r38");
        addAction(142, "<", "r38");
        addAction(142, "=", "r38");
        addAction(142, "==", "r38");
        addAction(142, ">", "r38");
        addAction(143, ";", "r24");
        addAction(144, "(", "s168");
        addAction(144, "identifier", "s166");
        addAction(144, "number_literal", "s167");
        addAction(145, "(", "s114");
        addAction(145, "identifier", "s112");
        addAction(145, "number_literal", "s113");
        addAction(146, "(", "s114");
        addAction(146, "identifier", "s112");
        addAction(146, "number_literal", "s113");
        addAction(147, "*", "r39");
        addAction(147, "+", "r39");
        addAction(147, ",", "r39");
        addAction(147, "-", "r39");
        addAction(147, "/", "r39");
        addAction(147, ";", "r39");
        addAction(147, "<", "r39");
        addAction(147, "=", "r39");
        addAction(147, "==", "r39");
        addAction(147, ">", "r39");
        addAction(148, ")", "s171");
        addAction(149, "break", "r10");
        addAction(149, "display", "r10");
        addAction(149, "end", "r10");
        addAction(149, "for", "r10");
        addAction(149, "identifier", "r10");
        addAction(149, "if", "r10");
        addAction(149, "int", "r10");
        addAction(149, "integer", "r10");
        addAction(149, "print_line", "r10");
        addAction(149, "while", "r10");
        addAction(15, ";", "r15");
        addAction(150, "break", "r12");
        addAction(150, "display", "r12");
        addAction(150, "else", "r12");
        addAction(150, "else_if", "s82");
        addAction(150, "end", "r12");
        addAction(150, "for", "r12");
        addAction(150, "identifier", "r12");
        addAction(150, "if", "r12");
        addAction(150, "int", "r12");
        addAction(150, "integer", "r12");
        addAction(150, "print_line", "r12");
        addAction(150, "while", "r12");
        addAction(151, "*", "s55");
        addAction(151, "+", "r32");
        addAction(151, "-", "r32");
        addAction(151, "/", "s56");
        addAction(151, "begin", "r32");
        addAction(152, "*", "r34");
        addAction(152, "+", "r34");
        addAction(152, "-", "r34");
        addAction(152, "/", "r34");
        addAction(152, "begin", "r34");
        addAction(153, "*", "r38");
        addAction(153, "+", "r38");
        addAction(153, "-", "r38");
        addAction(153, "/", "r38");
        addAction(153, "begin", "r38");
        addAction(154, "(", "s128");
        addAction(154, "identifier", "s126");
        addAction(154, "number_literal", "s127");
        addAction(155, "(", "s128");
        addAction(155, "identifier", "s126");
        addAction(155, "number_literal", "s127");
        addAction(156, ")", "r39");
        addAction(156, "*", "r39");
        addAction(156, "+", "r39");
        addAction(156, "-", "r39");
        addAction(156, "/", "r39");
        addAction(157, ")", "s175");
        addAction(158, "(", "s64");
        addAction(158, "identifier", "s62");
        addAction(158, "number_literal", "s63");
        addAction(159, "(", "s139");
        addAction(159, "identifier", "s137");
        addAction(159, "number_literal", "s138");
        addAction(16, ";", "r16");
        addAction(160, "(", "s139");
        addAction(160, "identifier", "s137");
        addAction(160, "number_literal", "s138");
        addAction(161, "*", "r39");
        addAction(161, "+", "r39");
        addAction(161, "-", "r39");
        addAction(161, "/", "r39");
        addAction(161, ";", "r39");
        addAction(162, ")", "s179");
        addAction(163, "+", "s52");
        addAction(163, ",", "r31");
        addAction(163, "-", "s53");
        addAction(163, ";", "r31");
        addAction(164, "*", "s55");
        addAction(164, "+", "r33");
        addAction(164, ",", "r33");
        addAction(164, "-", "r33");
        addAction(164, "/", "s56");
        addAction(164, ";", "r33");
        addAction(165, "*", "r35");
        addAction(165, "+", "r35");
        addAction(165, ",", "r35");
        addAction(165, "-", "r35");
        addAction(165, "/", "r35");
        addAction(165, ";", "r35");
        addAction(166, "*", "r36");
        addAction(166, "+", "r36");
        addAction(166, "++", "s182");
        addAction(166, ",", "r36");
        addAction(166, "-", "r36");
        addAction(166, "/", "r36");
        addAction(166, ";", "r36");
        addAction(167, "*", "r37");
        addAction(167, "+", "r37");
        addAction(167, ",", "r37");
        addAction(167, "-", "r37");
        addAction(167, "/", "r37");
        addAction(167, ";", "r37");
        addAction(168, "(", "s64");
        addAction(168, "identifier", "s62");
        addAction(168, "number_literal", "s63");
        addAction(169, "*", "s55");
        addAction(169, "+", "r32");
        addAction(169, ",", "r32");
        addAction(169, "-", "r32");
        addAction(169, "/", "s56");
        addAction(169, ";", "r32");
        addAction(169, "<", "r32");
        addAction(169, "=", "r32");
        addAction(169, "==", "r32");
        addAction(169, ">", "r32");
        addAction(17, ";", "r17");
        addAction(170, "*", "r34");
        addAction(170, "+", "r34");
        addAction(170, ",", "r34");
        addAction(170, "-", "r34");
        addAction(170, "/", "r34");
        addAction(170, ";", "r34");
        addAction(170, "<", "r34");
        addAction(170, "=", "r34");
        addAction(170, "==", "r34");
        addAction(170, ">", "r34");
        addAction(171, "*", "r38");
        addAction(171, "+", "r38");
        addAction(171, ",", "r38");
        addAction(171, "-", "r38");
        addAction(171, "/", "r38");
        addAction(171, ";", "r38");
        addAction(171, "<", "r38");
        addAction(171, "=", "r38");
        addAction(171, "==", "r38");
        addAction(171, ">", "r38");
        addAction(172, "break", "r11");
        addAction(172, "display", "r11");
        addAction(172, "else", "r11");
        addAction(172, "end", "r11");
        addAction(172, "for", "r11");
        addAction(172, "identifier", "r11");
        addAction(172, "if", "r11");
        addAction(172, "int", "r11");
        addAction(172, "integer", "r11");
        addAction(172, "print_line", "r11");
        addAction(172, "while", "r11");
        addAction(173, ")", "r32");
        addAction(173, "*", "s55");
        addAction(173, "+", "r32");
        addAction(173, "-", "r32");
        addAction(173, "/", "s56");
        addAction(174, ")", "r34");
        addAction(174, "*", "r34");
        addAction(174, "+", "r34");
        addAction(174, "-", "r34");
        addAction(174, "/", "r34");
        addAction(175, ")", "r38");
        addAction(175, "*", "r38");
        addAction(175, "+", "r38");
        addAction(175, "-", "r38");
        addAction(175, "/", "r38");
        addAction(176, ")", "s184");
        addAction(177, "*", "s55");
        addAction(177, "+", "r32");
        addAction(177, "-", "r32");
        addAction(177, "/", "s56");
        addAction(177, ";", "r32");
        addAction(178, "*", "r34");
        addAction(178, "+", "r34");
        addAction(178, "-", "r34");
        addAction(178, "/", "r34");
        addAction(178, ";", "r34");
        addAction(179, "*", "r38");
        addAction(179, "+", "r38");
        addAction(179, "-", "r38");
        addAction(179, "/", "r38");
        addAction(179, ";", "r38");
        addAction(18, ";", "r18");
        addAction(180, "(", "s168");
        addAction(180, "identifier", "s166");
        addAction(180, "number_literal", "s167");
        addAction(181, "(", "s168");
        addAction(181, "identifier", "s166");
        addAction(181, "number_literal", "s167");
        addAction(182, "*", "r39");
        addAction(182, "+", "r39");
        addAction(182, ",", "r39");
        addAction(182, "-", "r39");
        addAction(182, "/", "r39");
        addAction(182, ";", "r39");
        addAction(183, ")", "s187");
        addAction(184, "begin", "s66");
        addAction(185, "*", "s55");
        addAction(185, "+", "r32");
        addAction(185, ",", "r32");
        addAction(185, "-", "r32");
        addAction(185, "/", "s56");
        addAction(185, ";", "r32");
        addAction(186, "*", "r34");
        addAction(186, "+", "r34");
        addAction(186, ",", "r34");
        addAction(186, "-", "r34");
        addAction(186, "/", "r34");
        addAction(186, ";", "r34");
        addAction(187, "*", "r38");
        addAction(187, "+", "r38");
        addAction(187, ",", "r38");
        addAction(187, "-", "r38");
        addAction(187, "/", "r38");
        addAction(187, ";", "r38");
        addAction(188, "break", "r14");
        addAction(188, "display", "r14");
        addAction(188, "end", "r14");
        addAction(188, "for", "r14");
        addAction(188, "identifier", "r14");
        addAction(188, "if", "r14");
        addAction(188, "int", "r14");
        addAction(188, "integer", "r14");
        addAction(188, "print_line", "r14");
        addAction(188, "while", "r14");
        addAction(19, ";", "r19");
        addAction(2, "identifier", "s3");
        addAction(20, "=", "s39");
        addAction(21, "(", "s40");
        addAction(22, "identifier", "s42");
        addAction(23, ";", "r28");
        addAction(24, "(", "s43");
        addAction(25, "identifier", "r48");
        addAction(26, "identifier", "r49");
        addAction(27, "$", "r2");
        addAction(28, "end", "r3");
        addAction(29, "break", "r8");
        addAction(29, "display", "r8");
        addAction(29, "end", "r8");
        addAction(29, "for", "r8");
        addAction(29, "identifier", "r8");
        addAction(29, "if", "r8");
        addAction(29, "int", "r8");
        addAction(29, "integer", "r8");
        addAction(29, "print_line", "r8");
        addAction(29, "while", "r8");
        addAction(3, "begin", "s5");
        addAction(30, "begin", "s45");
        addAction(31, "+", "s52");
        addAction(31, "-", "s53");
        addAction(31, "<", "s48");
        addAction(31, "=", "s50");
        addAction(31, "==", "s51");
        addAction(31, ">", "s49");
        addAction(31, "begin", "r30");
        addAction(32, "*", "s55");
        addAction(32, "+", "r33");
        addAction(32, "-", "r33");
        addAction(32, "/", "s56");
        addAction(32, "<", "r33");
        addAction(32, "=", "r33");
        addAction(32, "==", "r33");
        addAction(32, ">", "r33");
        addAction(32, "begin", "r33");
        addAction(33, "*", "r35");
        addAction(33, "+", "r35");
        addAction(33, "-", "r35");
        addAction(33, "/", "r35");
        addAction(33, "<", "r35");
        addAction(33, "=", "r35");
        addAction(33, "==", "r35");
        addAction(33, ">", "r35");
        addAction(33, "begin", "r35");
        addAction(34, "*", "r36");
        addAction(34, "+", "r36");
        addAction(34, "++", "s57");
        addAction(34, "-", "r36");
        addAction(34, "/", "r36");
        addAction(34, "<", "r36");
        addAction(34, "=", "r36");
        addAction(34, "==", "r36");
        addAction(34, ">", "r36");
        addAction(34, "begin", "r36");
        addAction(35, "*", "r37");
        addAction(35, "+", "r37");
        addAction(35, "-", "r37");
        addAction(35, "/", "r37");
        addAction(35, "<", "r37");
        addAction(35, "=", "r37");
        addAction(35, "==", "r37");
        addAction(35, ">", "r37");
        addAction(35, "begin", "r37");
        addAction(36, "(", "s64");
        addAction(36, "identifier", "s62");
        addAction(36, "number_literal", "s63");
        addAction(37, "begin", "s66");
        addAction(38, "int", "s25");
        addAction(38, "integer", "s26");
        addAction(39, "(", "s74");
        addAction(39, "identifier", "s72");
        addAction(39, "number_literal", "s73");
        addAction(4, "$", "r1");
        addAction(40, "identifier", "s76");
        addAction(40, "string_literal", "s75");
        addAction(41, ",", "s78");
        addAction(41, ";", "r25");
        addAction(42, ",", "r26");
        addAction(42, ";", "r26");
        addAction(42, "=", "s79");
        addAction(43, "string_literal", "s80");
        addAction(44, "break", "r12");
        addAction(44, "display", "r12");
        addAction(44, "else", "r12");
        addAction(44, "else_if", "s82");
        addAction(44, "end", "r12");
        addAction(44, "for", "r12");
        addAction(44, "identifier", "r12");
        addAction(44, "if", "r12");
        addAction(44, "int", "r12");
        addAction(44, "integer", "r12");
        addAction(44, "print_line", "r12");
        addAction(44, "while", "r12");
        addAction(45, "break", "s23");
        addAction(45, "display", "s24");
        addAction(45, "for", "s14");
        addAction(45, "identifier", "s20");
        addAction(45, "if", "s12");
        addAction(45, "int", "s25");
        addAction(45, "integer", "s26");
        addAction(45, "print_line", "s21");
        addAction(45, "while", "s13");
        addAction(46, "(", "s89");
        addAction(46, "identifier", "s87");
        addAction(46, "number_literal", "s88");
        addAction(47, "(", "s36");
        addAction(47, "identifier", "s34");
        addAction(47, "number_literal", "s35");
        addAction(48, "(", "r40");
        addAction(48, "identifier", "r40");
        addAction(48, "number_literal", "r40");
        addAction(49, "(", "r41");
        addAction(49, "identifier", "r41");
        addAction(49, "number_literal", "r41");
        addAction(5, "break", "s23");
        addAction(5, "display", "s24");
        addAction(5, "for", "s14");
        addAction(5, "identifier", "s20");
        addAction(5, "if", "s12");
        addAction(5, "int", "s25");
        addAction(5, "integer", "s26");
        addAction(5, "print_line", "s21");
        addAction(5, "while", "s13");
        addAction(50, "(", "r42");
        addAction(50, "identifier", "r42");
        addAction(50, "number_literal", "r42");
        addAction(51, "(", "r43");
        addAction(51, "identifier", "r43");
        addAction(51, "number_literal", "r43");
        addAction(52, "(", "r44");
        addAction(52, "identifier", "r44");
        addAction(52, "number_literal", "r44");
        addAction(53, "(", "r45");
        addAction(53, "identifier", "r45");
        addAction(53, "number_literal", "r45");
        addAction(54, "(", "s36");
        addAction(54, "identifier", "s34");
        addAction(54, "number_literal", "s35");
        addAction(55, "(", "r46");
        addAction(55, "identifier", "r46");
        addAction(55, "number_literal", "r46");
        addAction(56, "(", "r47");
        addAction(56, "identifier", "r47");
        addAction(56, "number_literal", "r47");
        addAction(57, "*", "r39");
        addAction(57, "+", "r39");
        addAction(57, "-", "r39");
        addAction(57, "/", "r39");
        addAction(57, "<", "r39");
        addAction(57, "=", "r39");
        addAction(57, "==", "r39");
        addAction(57, ">", "r39");
        addAction(57, "begin", "r39");
        addAction(58, ")", "s92");
        addAction(59, ")", "r30");
        addAction(59, "+", "s52");
        addAction(59, "-", "s53");
        addAction(59, "<", "s48");
        addAction(59, "=", "s50");
        addAction(59, "==", "s51");
        addAction(59, ">", "s49");
        addAction(6, "end", "s27");
        addAction(60, ")", "r33");
        addAction(60, "*", "s55");
        addAction(60, "+", "r33");
        addAction(60, "-", "r33");
        addAction(60, "/", "s56");
        addAction(60, "<", "r33");
        addAction(60, "=", "r33");
        addAction(60, "==", "r33");
        addAction(60, ">", "r33");
        addAction(61, ")", "r35");
        addAction(61, "*", "r35");
        addAction(61, "+", "r35");
        addAction(61, "-", "r35");
        addAction(61, "/", "r35");
        addAction(61, "<", "r35");
        addAction(61, "=", "r35");
        addAction(61, "==", "r35");
        addAction(61, ">", "r35");
        addAction(62, ")", "r36");
        addAction(62, "*", "r36");
        addAction(62, "+", "r36");
        addAction(62, "++", "s96");
        addAction(62, "-", "r36");
        addAction(62, "/", "r36");
        addAction(62, "<", "r36");
        addAction(62, "=", "r36");
        addAction(62, "==", "r36");
        addAction(62, ">", "r36");
        addAction(63, ")", "r37");
        addAction(63, "*", "r37");
        addAction(63, "+", "r37");
        addAction(63, "-", "r37");
        addAction(63, "/", "r37");
        addAction(63, "<", "r37");
        addAction(63, "=", "r37");
        addAction(63, "==", "r37");
        addAction(63, ">", "r37");
        addAction(64, "(", "s64");
        addAction(64, "identifier", "s62");
        addAction(64, "number_literal", "s63");
        addAction(65, "break", "r13");
        addAction(65, "display", "r13");
        addAction(65, "end", "r13");
        addAction(65, "for", "r13");
        addAction(65, "identifier", "r13");
        addAction(65, "if", "r13");
        addAction(65, "int", "r13");
        addAction(65, "integer", "r13");
        addAction(65, "print_line", "r13");
        addAction(65, "while", "r13");
        addAction(66, "break", "s23");
        addAction(66, "display", "s24");
        addAction(66, "for", "s14");
        addAction(66, "identifier", "s20");
        addAction(66, "if", "s12");
        addAction(66, "int", "s25");
        addAction(66, "integer", "s26");
        addAction(66, "print_line", "s21");
        addAction(66, "while", "s13");
        addAction(67, ";", "s99");
        addAction(68, ";", "r20");
        addAction(69, "+", "s52");
        addAction(69, "-", "s53");
        addAction(69, ";", "r30");
        addAction(69, "<", "s48");
        addAction(69, "=", "s50");
        addAction(69, "==", "s51");
        addAction(69, ">", "s49");
        addAction(7, "break", "s23");
        addAction(7, "display", "s24");
        addAction(7, "end", "r4");
        addAction(7, "for", "s14");
        addAction(7, "identifier", "s20");
        addAction(7, "if", "s12");
        addAction(7, "int", "s25");
        addAction(7, "integer", "s26");
        addAction(7, "print_line", "s21");
        addAction(7, "while", "s13");
        addAction(70, "*", "s55");
        addAction(70, "+", "r33");
        addAction(70, "-", "r33");
        addAction(70, "/", "s56");
        addAction(70, ";", "r33");
        addAction(70, "<", "r33");
        addAction(70, "=", "r33");
        addAction(70, "==", "r33");
        addAction(70, ">", "r33");
        addAction(71, "*", "r35");
        addAction(71, "+", "r35");
        addAction(71, "-", "r35");
        addAction(71, "/", "r35");
        addAction(71, ";", "r35");
        addAction(71, "<", "r35");
        addAction(71, "=", "r35");
        addAction(71, "==", "r35");
        addAction(71, ">", "r35");
        addAction(72, "*", "r36");
        addAction(72, "+", "r36");
        addAction(72, "++", "s103");
        addAction(72, "-", "r36");
        addAction(72, "/", "r36");
        addAction(72, ";", "r36");
        addAction(72, "<", "r36");
        addAction(72, "=", "r36");
        addAction(72, "==", "r36");
        addAction(72, ">", "r36");
        addAction(73, "*", "r37");
        addAction(73, "+", "r37");
        addAction(73, "-", "r37");
        addAction(73, "/", "r37");
        addAction(73, ";", "r37");
        addAction(73, "<", "r37");
        addAction(73, "=", "r37");
        addAction(73, "==", "r37");
        addAction(73, ">", "r37");
        addAction(74, "(", "s64");
        addAction(74, "identifier", "s62");
        addAction(74, "number_literal", "s63");
        addAction(75, ")", "s105");
        addAction(76, ")", "s106");
        addAction(77, ";", "r23");
        addAction(78, "identifier", "s42");
        addAction(79, "(", "s114");
        addAction(79, "identifier", "s112");
        addAction(79, "number_literal", "s113");
        addAction(8, "break", "r5");
        addAction(8, "display", "r5");
        addAction(8, "end", "r5");
        addAction(8, "for", "r5");
        addAction(8, "identifier", "r5");
        addAction(8, "if", "r5");
        addAction(8, "int", "r5");
        addAction(8, "integer", "r5");
        addAction(8, "print_line", "r5");
        addAction(8, "while", "r5");
        addAction(80, ")", "s115");
        addAction(81, "break", "r9");
        addAction(81, "display", "r9");
        addAction(81, "else", "s116");
        addAction(81, "end", "r9");
        addAction(81, "for", "r9");
        addAction(81, "identifier", "r9");
        addAction(81, "if", "r9");
        addAction(81, "int", "r9");
        addAction(81, "integer", "r9");
        addAction(81, "print_line", "r9");
        addAction(81, "while", "r9");
        addAction(82, "(", "s36");
        addAction(82, "identifier", "s34");
        addAction(82, "number_literal", "s35");
        addAction(83, "end", "s118");
        addAction(84, "+", "s52");
        addAction(84, "-", "s53");
        addAction(84, "begin", "r31");
        addAction(85, "*", "s55");
        addAction(85, "+", "r33");
        addAction(85, "-", "r33");
        addAction(85, "/", "s56");
        addAction(85, "begin", "r33");
        addAction(86, "*", "r35");
        addAction(86, "+", "r35");
        addAction(86, "-", "r35");
        addAction(86, "/", "r35");
        addAction(86, "begin", "r35");
        addAction(87, "*", "r36");
        addAction(87, "+", "r36");
        addAction(87, "++", "s121");
        addAction(87, "-", "r36");
        addAction(87, "/", "r36");
        addAction(87, "begin", "r36");
        addAction(88, "*", "r37");
        addAction(88, "+", "r37");
        addAction(88, "-", "r37");
        addAction(88, "/", "r37");
        addAction(88, "begin", "r37");
        addAction(89, "(", "s64");
        addAction(89, "identifier", "s62");
        addAction(89, "number_literal", "s63");
        addAction(9, "break", "r6");
        addAction(9, "display", "r6");
        addAction(9, "end", "r6");
        addAction(9, "for", "r6");
        addAction(9, "identifier", "r6");
        addAction(9, "if", "r6");
        addAction(9, "int", "r6");
        addAction(9, "integer", "r6");
        addAction(9, "print_line", "r6");
        addAction(9, "while", "r6");
        addAction(90, "*", "s55");
        addAction(90, "+", "r32");
        addAction(90, "-", "r32");
        addAction(90, "/", "s56");
        addAction(90, "<", "r32");
        addAction(90, "=", "r32");
        addAction(90, "==", "r32");
        addAction(90, ">", "r32");
        addAction(90, "begin", "r32");
        addAction(91, "*", "r34");
        addAction(91, "+", "r34");
        addAction(91, "-", "r34");
        addAction(91, "/", "r34");
        addAction(91, "<", "r34");
        addAction(91, "=", "r34");
        addAction(91, "==", "r34");
        addAction(91, ">", "r34");
        addAction(91, "begin", "r34");
        addAction(92, "*", "r38");
        addAction(92, "+", "r38");
        addAction(92, "-", "r38");
        addAction(92, "/", "r38");
        addAction(92, "<", "r38");
        addAction(92, "=", "r38");
        addAction(92, "==", "r38");
        addAction(92, ">", "r38");
        addAction(92, "begin", "r38");
        addAction(93, "(", "s128");
        addAction(93, "identifier", "s126");
        addAction(93, "number_literal", "s127");
        addAction(94, "(", "s64");
        addAction(94, "identifier", "s62");
        addAction(94, "number_literal", "s63");
        addAction(95, "(", "s64");
        addAction(95, "identifier", "s62");
        addAction(95, "number_literal", "s63");
        addAction(96, ")", "r39");
        addAction(96, "*", "r39");
        addAction(96, "+", "r39");
        addAction(96, "-", "r39");
        addAction(96, "/", "r39");
        addAction(96, "<", "r39");
        addAction(96, "=", "r39");
        addAction(96, "==", "r39");
        addAction(96, ">", "r39");
        addAction(97, ")", "s131");
        addAction(98, "end", "s132");
        addAction(99, "(", "s74");
        addAction(99, "identifier", "s72");
        addAction(99, "number_literal", "s73");
        addGoto(0, "PROGRAM", "1");
        addGoto(100, "FACTOR", "136");
        addGoto(100, "SIMPLE_EXPRESSION", "134");
        addGoto(100, "TERM", "135");
        addGoto(101, "FACTOR", "71");
        addGoto(101, "TERM", "140");
        addGoto(102, "FACTOR", "141");
        addGoto(107, "VARIABLE_DECLARATIONS", "143");
        addGoto(109, "ADDING_OPERATOR", "145");
        addGoto(109, "RELATIONAL_OPERATOR", "144");
        addGoto(110, "MULTIPLYING_OPERATOR", "146");
        addGoto(114, "EXPRESSION", "148");
        addGoto(114, "FACTOR", "61");
        addGoto(114, "SIMPLE_EXPRESSION", "59");
        addGoto(114, "TERM", "60");
        addGoto(116, "COMPOUND_STMT", "149");
        addGoto(117, "COMPOUND_STMT", "150");
        addGoto(119, "FACTOR", "86");
        addGoto(119, "TERM", "151");
        addGoto(12, "EXPRESSION", "30");
        addGoto(12, "FACTOR", "33");
        addGoto(12, "SIMPLE_EXPRESSION", "31");
        addGoto(12, "TERM", "32");
        addGoto(120, "FACTOR", "152");
        addGoto(123, "ADDING_OPERATOR", "154");
        addGoto(124, "MULTIPLYING_OPERATOR", "155");
        addGoto(128, "EXPRESSION", "157");
        addGoto(128, "FACTOR", "61");
        addGoto(128, "SIMPLE_EXPRESSION", "59");
        addGoto(128, "TERM", "60");
        addGoto(129, "MULTIPLYING_OPERATOR", "95");
        addGoto(13, "EXPRESSION", "37");
        addGoto(13, "FACTOR", "33");
        addGoto(13, "SIMPLE_EXPRESSION", "31");
        addGoto(13, "TERM", "32");
        addGoto(134, "ADDING_OPERATOR", "159");
        addGoto(135, "MULTIPLYING_OPERATOR", "160");
        addGoto(139, "EXPRESSION", "162");
        addGoto(139, "FACTOR", "61");
        addGoto(139, "SIMPLE_EXPRESSION", "59");
        addGoto(139, "TERM", "60");
        addGoto(140, "MULTIPLYING_OPERATOR", "102");
        addGoto(144, "FACTOR", "165");
        addGoto(144, "SIMPLE_EXPRESSION", "163");
        addGoto(144, "TERM", "164");
        addGoto(145, "FACTOR", "111");
        addGoto(145, "TERM", "169");
        addGoto(146, "FACTOR", "170");
        addGoto(150, "ELSE_IF_STMT", "172");
        addGoto(151, "MULTIPLYING_OPERATOR", "120");
        addGoto(154, "FACTOR", "125");
        addGoto(154, "TERM", "173");
        addGoto(155, "FACTOR", "174");
        addGoto(158, "EXPRESSION", "176");
        addGoto(158, "FACTOR", "61");
        addGoto(158, "SIMPLE_EXPRESSION", "59");
        addGoto(158, "TERM", "60");
        addGoto(159, "FACTOR", "136");
        addGoto(159, "TERM", "177");
        addGoto(160, "FACTOR", "178");
        addGoto(163, "ADDING_OPERATOR", "180");
        addGoto(164, "MULTIPLYING_OPERATOR", "181");
        addGoto(168, "EXPRESSION", "183");
        addGoto(168, "FACTOR", "61");
        addGoto(168, "SIMPLE_EXPRESSION", "59");
        addGoto(168, "TERM", "60");
        addGoto(169, "MULTIPLYING_OPERATOR", "146");
        addGoto(173, "MULTIPLYING_OPERATOR", "155");
        addGoto(177, "MULTIPLYING_OPERATOR", "160");
        addGoto(180, "FACTOR", "165");
        addGoto(180, "TERM", "185");
        addGoto(181, "FACTOR", "186");
        addGoto(184, "COMPOUND_STMT", "188");
        addGoto(185, "MULTIPLYING_OPERATOR", "181");
        addGoto(22, "VARIABLE_DECLARATION", "41");
        addGoto(3, "COMPOUND_STMT", "4");
        addGoto(30, "COMPOUND_STMT", "44");
        addGoto(31, "ADDING_OPERATOR", "47");
        addGoto(31, "RELATIONAL_OPERATOR", "46");
        addGoto(32, "MULTIPLYING_OPERATOR", "54");
        addGoto(36, "EXPRESSION", "58");
        addGoto(36, "FACTOR", "61");
        addGoto(36, "SIMPLE_EXPRESSION", "59");
        addGoto(36, "TERM", "60");
        addGoto(37, "COMPOUND_STMT", "65");
        addGoto(38, "DECLARATION_STMT", "67");
        addGoto(38, "TYPE", "22");
        addGoto(38, "TYPE", "22");
        addGoto(39, "EXPRESSION", "68");
        addGoto(39, "FACTOR", "71");
        addGoto(39, "SIMPLE_EXPRESSION", "69");
        addGoto(39, "TERM", "70");
        addGoto(41, "VARIABLE_DECLARATIONS", "77");
        addGoto(44, "ELSE_IF_STMT", "81");
        addGoto(45, "ASSIGNMENT_STMT", "15");
        addGoto(45, "BREAK_STMT", "18");
        addGoto(45, "CONDITIONAL_STMT", "8");
        addGoto(45, "DECLARATION_STMT", "17");
        addGoto(45, "DISPLAY_STMT", "19");
        addGoto(45, "FOR_STMT", "10");
        addGoto(45, "PRINT_STMT", "16");
        addGoto(45, "SIMPLE_STMT", "11");
        addGoto(45, "STMT", "7");
        addGoto(45, "STMTS", "83");
        addGoto(45, "TYPE", "22");
        addGoto(45, "TYPE", "22");
        addGoto(45, "WHILE_STMT", "9");
        addGoto(46, "FACTOR", "86");
        addGoto(46, "SIMPLE_EXPRESSION", "84");
        addGoto(46, "TERM", "85");
        addGoto(47, "FACTOR", "33");
        addGoto(47, "TERM", "90");
        addGoto(5, "ASSIGNMENT_STMT", "15");
        addGoto(5, "BREAK_STMT", "18");
        addGoto(5, "CONDITIONAL_STMT", "8");
        addGoto(5, "DECLARATION_STMT", "17");
        addGoto(5, "DISPLAY_STMT", "19");
        addGoto(5, "FOR_STMT", "10");
        addGoto(5, "PRINT_STMT", "16");
        addGoto(5, "SIMPLE_STMT", "11");
        addGoto(5, "STMT", "7");
        addGoto(5, "STMTS", "6");
        addGoto(5, "TYPE", "22");
        addGoto(5, "TYPE", "22");
        addGoto(5, "WHILE_STMT", "9");
        addGoto(54, "FACTOR", "91");
        addGoto(59, "ADDING_OPERATOR", "94");
        addGoto(59, "RELATIONAL_OPERATOR", "93");
        addGoto(60, "MULTIPLYING_OPERATOR", "95");
        addGoto(64, "EXPRESSION", "97");
        addGoto(64, "FACTOR", "61");
        addGoto(64, "SIMPLE_EXPRESSION", "59");
        addGoto(64, "TERM", "60");
        addGoto(66, "ASSIGNMENT_STMT", "15");
        addGoto(66, "BREAK_STMT", "18");
        addGoto(66, "CONDITIONAL_STMT", "8");
        addGoto(66, "DECLARATION_STMT", "17");
        addGoto(66, "DISPLAY_STMT", "19");
        addGoto(66, "FOR_STMT", "10");
        addGoto(66, "PRINT_STMT", "16");
        addGoto(66, "SIMPLE_STMT", "11");
        addGoto(66, "STMT", "7");
        addGoto(66, "STMTS", "98");
        addGoto(66, "TYPE", "22");
        addGoto(66, "TYPE", "22");
        addGoto(66, "WHILE_STMT", "9");
        addGoto(69, "ADDING_OPERATOR", "101");
        addGoto(69, "RELATIONAL_OPERATOR", "100");
        addGoto(7, "ASSIGNMENT_STMT", "15");
        addGoto(7, "BREAK_STMT", "18");
        addGoto(7, "CONDITIONAL_STMT", "8");
        addGoto(7, "DECLARATION_STMT", "17");
        addGoto(7, "DISPLAY_STMT", "19");
        addGoto(7, "FOR_STMT", "10");
        addGoto(7, "PRINT_STMT", "16");
        addGoto(7, "SIMPLE_STMT", "11");
        addGoto(7, "STMT", "7");
        addGoto(7, "STMTS", "28");
        addGoto(7, "TYPE", "22");
        addGoto(7, "TYPE", "22");
        addGoto(7, "WHILE_STMT", "9");
        addGoto(70, "MULTIPLYING_OPERATOR", "102");
        addGoto(74, "EXPRESSION", "104");
        addGoto(74, "FACTOR", "61");
        addGoto(74, "SIMPLE_EXPRESSION", "59");
        addGoto(74, "TERM", "60");
        addGoto(78, "VARIABLE_DECLARATION", "107");
        addGoto(79, "EXPRESSION", "108");
        addGoto(79, "FACTOR", "111");
        addGoto(79, "SIMPLE_EXPRESSION", "109");
        addGoto(79, "TERM", "110");
        addGoto(82, "EXPRESSION", "117");
        addGoto(82, "FACTOR", "33");
        addGoto(82, "SIMPLE_EXPRESSION", "31");
        addGoto(82, "TERM", "32");
        addGoto(84, "ADDING_OPERATOR", "119");
        addGoto(85, "MULTIPLYING_OPERATOR", "120");
        addGoto(89, "EXPRESSION", "122");
        addGoto(89, "FACTOR", "61");
        addGoto(89, "SIMPLE_EXPRESSION", "59");
        addGoto(89, "TERM", "60");
        addGoto(90, "MULTIPLYING_OPERATOR", "54");
        addGoto(93, "FACTOR", "125");
        addGoto(93, "SIMPLE_EXPRESSION", "123");
        addGoto(93, "TERM", "124");
        addGoto(94, "FACTOR", "61");
        addGoto(94, "TERM", "129");
        addGoto(95, "FACTOR", "130");
        addGoto(99, "EXPRESSION", "133");
        addGoto(99, "FACTOR", "71");
        addGoto(99, "SIMPLE_EXPRESSION", "69");
        addGoto(99, "TERM", "70");


    }

    private void addAction(int state, String symbol, String action) {
        actionTable.putIfAbsent(state, new HashMap<>());
        actionTable.get(state).put(symbol, action);
    }

    private void addGoto(int state, String symbol, String nextState) {
        gotoTable.putIfAbsent(state, new HashMap<>());
        gotoTable.get(state).put(symbol, Integer.parseInt(nextState));
    }

    private void preprocess() {
        removeComments();
    }

    private void removeComments() {
        tokenList.removeIf(token -> "comment".equals(token.getTokenAttribute()));
    }

    private void printStackFromBottom(String action) {
        System.out.print(action + " ");
        int maxSize = Math.max(stateStack.size(), symbolStack.size());
        for (int i = 0; i < maxSize; i++) {
            if (i < stateStack.size()) {
                System.out.print("S" + stateStack.get(i) + " ");
            }
            if (i < symbolStack.size()) {
                System.out.print(symbolStack.get(i) + " ");
            }
        }
    
        System.out.println();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java LRParser <filename>");
            return;
        }

        SmallLexer smallLexer = new SmallLexer();
        smallLexer.setPrintTokenList(false);
        smallLexer.lex(args[0]);

        LRparser lrParser = new LRparser(smallLexer.getTokenList());
        if (lrParser.parse() != 0) {
            System.out.println(lrParser.parsingErrorMsg);
            System.out.println("Parsing Failed");
        } else {
            System.out.println("Parsing Ok");
        }
    }

    public int parse() {
        preprocess();

        stateStack.push(0); // initial state, 0
        symbolStack.push("$"); // EOS
        printStackFromBottom("[S]");

        while (true) {
            int currentState = stateStack.peek();
            Token currentToken = tokenList.get(tokenIdx);

            /**
             * Checks if current iteration satisfies the accepting condition
             * 
             * Accepting condition: state = 1, token = $
             * Signifies: START -> PROGRAM., $
             */
            if (isAccept(currentState, currentToken)) {
                return 0;
            }

            // get action based on [state, token]
            String action = "";
            if (isIdentifier(currentToken) || isNumberLiteral(currentToken) || isStringLiteral(currentToken)) {
                action = actionTable.get(currentState).get(currentToken.getTokenAttribute());
            } else {
                action = actionTable.get(currentState).get(currentToken.getTokenName());
            }

            if (action == null) {
                parsingErrorMsg = "Syntax error at token index " + tokenIdx + ": " + currentToken.getTokenName();
                return 1;
            }

            if (action.charAt(0) == 's') { // ex. s5, 's'hift to 5
                // Shift action
                int nextState = Integer.parseInt(action.substring(1));

                stateStack.push(nextState);
                if (isIdentifier(currentToken) || isNumberLiteral(currentToken) || isStringLiteral(currentToken)) {
                    symbolStack.push(currentToken.getTokenAttribute());
                } else {
                    symbolStack.push(currentToken.getTokenName());
                }
                
                tokenIdx++;

                printStackFromBottom("[S]");
            } else if (action.charAt(0) == 'r') { // ex. r5, 'r'educe to 5
                // Reduce action
                int productionNumber = Integer.parseInt(action.substring(1));
                String lhs = getLHS(productionNumber);
                String[] rhs = getRHS(productionNumber);

                // Pop |rhs| symbols from the stack
                for (int i = 0; i < rhs.length; i++) {
                    stateStack.pop();
                    symbolStack.pop();
                }

                // Push lhs and goto state
                symbolStack.push(lhs);
                int gotoState = gotoTable.get(stateStack.peek()).get(lhs);
                stateStack.push(gotoState);

                printStackFromBottom("[R]");
            }
        }
    }

    private String getLHS(int productionNumber) {
        // Define LHS for each production
        switch (productionNumber) {
            case 1: return "PROGRAM";
            case 2: return "COMPOUND_STMT";
            case 3:
            case 4: return "STMTS";
            case 5:
            case 6:
            case 7:
            case 8: return "STMT";
            case 9:
            case 10: return "CONDITIONAL_STMT";
            case 11:
            case 12: return "ELSE_IF_STMT";
            case 13: return "WHILE_STMT";
            case 14: return "FOR_STMT";
            case 15:
            case 16:
            case 17:
            case 19:
            case 18: return "SIMPLE_STMT";
            case 20: return "ASSIGNMENT_STMT";
            case 21:
            case 22: return "PRINT_STMT";
            case 23: return "DECLARATION_STMT";
            case 24:
            case 25: return "VARIABLE_DECLARATIONS";
            case 26:
            case 27: return "VARIABLE_DECLARATION";
            case 28: return "BREAK_STMT";
            case 29: return "DISPLAY_STMT";
            case 30:
            case 31: return "EXPRESSION";
            case 32:
            case 33: return "SIMPLE_EXPRESSION";
            case 34:
            case 35: return "TERM";
            case 36:
            case 37:
            case 38:
            case 39: return "FACTOR";
            case 40:
            case 43: return "RELATIONAL_OPERATOR";
            case 44: return "ADDING_OPERATOR";
            case 46: return "MULTIPLYING_OPERATOR";
            case 48:
            case 49: return "TYPE";
            // Add more cases for each production rule
            default: return "";
        }
    }

    private String[] getRHS(int productionNumber) {
        // Define RHS for each production
        switch (productionNumber) {
            case 1: return new String[] {"program", "identifier", "COMPOUND_STMT"};
            case 2: return new String[] {"begin", "STMTS", "end"};
            case 3: return new String[] {"STMT", "STMTS"};
            case 4: return new String[] {"STMT"};
            case 5: return new String[] {"CONDITIONAL_STMT"};
            case 6: return new String[] {"WHILE_STMT"};
            case 7: return new String[] {"FOR_STMT"};
            case 8: return new String[] {"SIMPLE_STMT", ";"};
            case 9: return new String[] {"if", "EXPRESSION", "COMPOUND_STMT", "ELSE_IF_STMT"};
            case 10: return new String[] {"if", "EXPRESSION", "COMPOUND_STMT", "ELSE_IF_STMT", "else", "COMPOUND_STMT"};
            case 11: return new String[] {"else_if", "EXPRESSION", "COMPOUND_STMT", "ELSE_IF_STMT"};
            case 12: return new String[] {};
            case 13: return new String[] {"while", "EXPRESSION", "COMPOUND_STMT"};
            case 14: return new String[] {"for", "(", "DECLARATION_STMT", ";", "EXPRESSION", ";", "EXPRESSION", ")", "COMPOUND_STMT"};
            case 15: return new String[] {"ASSIGNMENT_STMT"};
            case 16: return new String[] {"PRINT_STMT"};
            case 17: return new String[] {"DECLARATION_STMT"};
            case 18: return new String[] {"BREAK_STMT"};
            case 19: return new String[] {"DISPLAY_STMT"};
            case 20: return new String[] {"identifier", "=", "EXPRESSION"};
            case 21: return new String[] {"print_line", "(", "string_literal", ")"};
            case 22: return new String[] {"print_line", "(", "identifier", ")"};
            case 23: return new String[] {"TYPE", "VARIABLE_DECLARATION", "VARIABLE_DECLARATIONS"};
            case 24: return new String[] {",", "VARIABLE_DECLARATION", "VARAIBLE_DECLARATIONS"};
            case 25: return new String[] {};
            case 26: return new String[] {"identifier"};
            case 27: return new String[] {"identifier", "=", "EXPRESSION"};
            case 28: return new String[] {"break"};
            case 29: return new String[] {"display", "(", "string_literal", ")"};
            case 30: return new String[] {"SIMPLE_EXPRESSION"};
            case 31: return new String[] {"SIMPLE_EXPRESSION", "RELATIONAL_OPERATOR", "SIMPLE_EXPRESSION"};
            case 32: return new String[] {"SIMPLE_EXPRESSION", "ADDING_OPERATOR", "TERM"};
            case 33: return new String[] {"TERM"};
            case 34: return new String[] {"TERM", "MULTIPLYING_OPERATOR", "FACTOR"};
            case 35: return new String[] {"FACTOR"};
            case 36: return new String[] {"identifier"};
            case 37: return new String[] {"number_literal"};
            case 38: return new String[] {"(", "EXPRESSION", ")"};
            case 39: return new String[] {"identifier", "++"};
            case 40: return new String[] {"<"};
            case 43: return new String[] {"=="};
            case 44: return new String[] {"+"};
            case 46: return new String[] {"*"};
            case 48: return new String[] {"int"};
            case 49: return new String[] {"integer"};
            // Add more cases for each production rule
            default: return new String[] {};
        }
    }

    private boolean isAccept(int state, Token token) { return (state == 1 && token.getTokenName().equals("$")); }
    private boolean isIdentifier(Token token) { return token.getTokenAttribute().equals("identifier"); }
    private boolean isNumberLiteral(Token token) { return token.getTokenAttribute().equals("number_literal"); }
    private boolean isStringLiteral(Token token) { return token.getTokenAttribute().equals("string_literal"); }

}
