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
        addAction(1, "$", "ACCEPT");
        addAction(10, "$", "r2");
        addAction(100, "*", "s53 / r18");
        addAction(100, "+", "r18");
        addAction(100, "<", "r18");
        addAction(101, "=", "s122");
        addAction(102, "|", "s123");
        addAction(103, "*", "r19");
        addAction(103, "|", "r19");
        addAction(104, "|", "s124");
        addAction(105, "|", "s125");
        addAction(106, "identifier", "s74");
        addAction(107, "|", "s127");
        addAction(108, "|", "s128");
        addAction(109, "(", "s129");
        addAction(11, "|", "s16");
        addAction(110, "|", "r10");
        addAction(111, "*", "s25");
        addAction(111, "+", "s24");
        addAction(111, "|", "s130");
        addAction(112, ";", "s131");
        addAction(113, "*", "s25");
        addAction(113, "+", "s24");
        addAction(113, "|", "s132");
        addAction(114, ";", "r12");
        addAction(115, "identifier", "s95");
        addAction(116, "identifier", "s134");
        addAction(117, "identifier", "r25");
        addAction(118, "else", "s135");
        addAction(12, "if", "s9");
        addAction(121, "identifier", "s68");
        addAction(122, "|", "s139");
        addAction(123, "identifier", "s141");
        addAction(124, "number_literal", "s142");
        addAction(125, "(", "s143");
        addAction(126, ";", "s144");
        addAction(127, "identifier", "s74");
        addAction(128, "int", "s77");
        addAction(129, "string_literal", "s148");
        addAction(13, "while", "s19");
        addAction(133, ",", "s154");
        addAction(134, "=", "s155");
        addAction(135, "begin", "s44");
        addAction(136, "begin", "s21");
        addAction(137, "*", "s53 / r18");
        addAction(137, "+", "r18");
        addAction(137, "begin", "r18");
        addAction(138, "|", "s159");
        addAction(139, "==", "s160");
        addAction(14, "begin", "s21");
        addAction(140, "*", "r19");
        addAction(140, "+", "r19");
        addAction(140, "|", "r19");
        addAction(141, "|", "s161");
        addAction(142, "|", "s162");
        addAction(144, "end", "r4");
        addAction(145, ";", "s164");
        addAction(146, "|", "s165");
        addAction(147, "identifier", "s95");
        addAction(148, ")", "s167");
        addAction(149, "*", "s25");
        addAction(149, "+", "s24");
        addAction(149, "<", "s169");
        addAction(15, "*", "s25");
        addAction(15, "+", "s24");
        addAction(15, "|", "s22");
        addAction(150, ")", "s170");
        addAction(151, "*", "s25");
        addAction(151, "+", "s24");
        addAction(151, "|", "s171");
        addAction(152, "*", "s25");
        addAction(152, "+", "s24");
        addAction(152, "<", "s173");
        addAction(153, "|", "s174");
        addAction(154, "identifier", "s95");
        addAction(156, "|", "r5");
        addAction(157, "else_if", "s32");
        addAction(158, "identifier", "s68");
        addAction(159, "identifier", "s181");
        addAction(16, "if", "s9");
        addAction(160, "begin", "r21");
        addAction(161, "number_literal", "s182");
        addAction(162, "(", "s183");
        addAction(163, ")", "s184");
        addAction(164, "|", "r4");
        addAction(165, "break", "s186");
        addAction(166, ",", "s154");
        addAction(167, "|", "s188");
        addAction(169, "|", "s190");
        addAction(17, "|", "s28");
        addAction(170, "begin", "s44");
        addAction(173, "|", "s194");
        addAction(175, ",", "s154");
        addAction(176, ",", "r14");
        addAction(177, "*", "s25");
        addAction(177, "+", "s24");
        addAction(177, "|", "s196");
        addAction(178, "|", "s197");
        addAction(179, "|", "s198");
        addAction(18, "|", "s29");
        addAction(180, "*", "r19");
        addAction(180, "+", "r19");
        addAction(180, "<", "r19");
        addAction(181, "|", "s199");
        addAction(182, "|", "s200");
        addAction(184, "|", "s202");
        addAction(185, "|", "s203");
        addAction(186, "|", "r15");
        addAction(187, "|", "r12");
        addAction(188, "print_line", "s204");
        addAction(189, "*", "s25");
        addAction(189, "+", "s24");
        addAction(189, "|", "r17");
        addAction(190, ">", "s205");
        addAction(191, "|", "r8");
        addAction(192, "*", "s25");
        addAction(192, "+", "s24");
        addAction(192, "<", "s207");
        addAction(193, "*", "s25");
        addAction(193, "+", "s24");
        addAction(193, ";", "r17");
        addAction(194, ">", "s209");
        addAction(195, "|", "s210");
        addAction(198, "identifier", "s213");
        addAction(199, "number_literal", "s214");
        addAction(2, "identifier", "s3");
        addAction(20, "else_if", "s32");
        addAction(200, "(", "s215");
        addAction(201, ")", "s216");
        addAction(202, "identifier", "s217");
        addAction(203, "display", "s219");
        addAction(204, "(", "s220");
        addAction(205, "|", "s221");
        addAction(207, "|", "s223");
        addAction(209, "|", "s225");
        addAction(21, "if", "s9");
        addAction(211, "*", "s25");
        addAction(211, "+", "s24");
        addAction(211, "<", "s227");
        addAction(212, "*", "r19");
        addAction(212, "+", "r19");
        addAction(212, "begin", "r19");
        addAction(213, "|", "s228");
        addAction(214, "|", "s229");
        addAction(216, "|", "s231");
        addAction(217, "++", "s232");
        addAction(218, ";", "r9");
        addAction(219, "(", "s233");
        addAction(220, "identifier", "s234");
        addAction(221, "=", "s235");
        addAction(222, ")", "r17");
        addAction(222, "*", "s25");
        addAction(222, "+", "s24");
        addAction(223, ">", "s237");
        addAction(224, "*", "s53");
        addAction(224, "|", "s238");
        addAction(225, "=", "s239");
        addAction(227, "|", "s241");
        addAction(228, "number_literal", "s242");
        addAction(229, "(", "s243");
        addAction(230, ")", "s244");
        addAction(231, "identifier", "s245");
        addAction(232, "|", "r20");
        addAction(233, "string_literal", "s246");
        addAction(234, ")", "s247");
        addAction(235, "|", "s248");
        addAction(237, "|", "s250");
        addAction(239, "|", "s252");
        addAction(24, "|", "s36");
        addAction(240, "*", "s25");
        addAction(240, "+", "s24");
        addAction(240, ",", "r17");
        addAction(241, ">", "s254");
        addAction(242, "|", "s255");
        addAction(244, "|", "s257");
        addAction(245, "++", "s258");
        addAction(246, ")", "s259");
        addAction(247, "|", "r11");
        addAction(248, "==", "s260");
        addAction(249, "*", "s53");
        addAction(249, "|", "s261");
        addAction(25, "|", "s37");
        addAction(250, "=", "s262");
        addAction(251, "*", "s53 / r18");
        addAction(251, "+", "r18");
        addAction(251, ";", "r18");
        addAction(252, "==", "s264");
        addAction(254, "|", "s266");
        addAction(255, "(", "s267");
        addAction(256, ")", "s268");
        addAction(257, "identifier", "s269");
        addAction(258, "*", "r20");
        addAction(258, "|", "r20");
        addAction(259, ";", "r16");
        addAction(26, "end", "r3");
        addAction(260, "|", "r21");
        addAction(262, "|", "s271");
        addAction(263, "identifier", "s68");
        addAction(264, ";", "r21");
        addAction(265, "*", "s53");
        addAction(265, "|", "s273");
        addAction(266, "=", "s274");
        addAction(268, "|", "s276");
        addAction(269, "++", "s277");
        addAction(27, "|", "s38");
        addAction(270, ")", "r18");
        addAction(270, "*", "s53 / r18");
        addAction(270, "+", "r18");
        addAction(271, "==", "s279");
        addAction(272, "|", "s280");
        addAction(274, "|", "s282");
        addAction(275, ")", "s283");
        addAction(276, "identifier", "s284");
        addAction(277, "*", "r20");
        addAction(277, "+", "r20");
        addAction(277, "|", "r20");
        addAction(278, "identifier", "s68");
        addAction(279, ")", "r21");
        addAction(28, "if", "s9");
        addAction(280, "identifier", "s287");
        addAction(281, "*", "s53 / r18");
        addAction(281, "+", "r18");
        addAction(281, ",", "r18");
        addAction(282, "==", "s289");
        addAction(283, "|", "s290");
        addAction(284, "++", "s291");
        addAction(285, "|", "s292");
        addAction(286, "*", "r19");
        addAction(286, "+", "r19");
        addAction(286, ";", "r19");
        addAction(287, "|", "s293");
        addAction(288, "identifier", "s68");
        addAction(289, ",", "r21");
        addAction(29, "for", "s42");
        addAction(290, "identifier", "s295");
        addAction(291, "*", "r20");
        addAction(291, "+", "r20");
        addAction(291, "<", "r20");
        addAction(292, "identifier", "s297");
        addAction(293, "number_literal", "s298");
        addAction(294, "|", "s299");
        addAction(295, "++", "s300");
        addAction(296, ")", "r19");
        addAction(296, "*", "r19");
        addAction(296, "+", "r19");
        addAction(297, "|", "s301");
        addAction(298, "|", "s302");
        addAction(299, "identifier", "s304");
        addAction(3, "begin", "s5");
        addAction(30, "begin", "s44");
        addAction(300, "*", "r20");
        addAction(300, "+", "r20");
        addAction(300, "begin", "r20");
        addAction(301, "number_literal", "s305");
        addAction(302, "(", "s306");
        addAction(303, "*", "r19");
        addAction(303, "+", "r19");
        addAction(303, ",", "r19");
        addAction(304, "|", "s307");
        addAction(305, "|", "s308");
        addAction(307, "number_literal", "s310");
        addAction(308, "(", "s311");
        addAction(309, ")", "s312");
        addAction(31, "|", "s45");
        addAction(310, "|", "s313");
        addAction(312, "|", "s315");
        addAction(313, "(", "s316");
        addAction(314, ")", "s317");
        addAction(315, "identifier", "s318");
        addAction(317, "|", "s320");
        addAction(318, "++", "s321");
        addAction(319, ")", "s322");
        addAction(320, "identifier", "s323");
        addAction(321, "*", "r20");
        addAction(321, "+", "r20");
        addAction(321, ";", "r20");
        addAction(322, "|", "s324");
        addAction(323, "++", "s325");
        addAction(324, "identifier", "s326");
        addAction(325, ")", "r20");
        addAction(325, "*", "r20");
        addAction(325, "+", "r20");
        addAction(326, "++", "s327");
        addAction(327, "*", "r20");
        addAction(327, "+", "r20");
        addAction(327, ",", "r20");
        addAction(33, "end", "s47");
        addAction(34, "*", "s25");
        addAction(34, "+", "s24");
        addAction(34, "<", "s50");
        addAction(35, "*", "s53");
        addAction(35, "|", "s51");
        addAction(36, "-", "s54");
        addAction(37, "/", "s55");
        addAction(38, "while", "s19");
        addAction(39, "|", "r3");
        addAction(4, "$", "r1");
        addAction(40, "|", "s57");
        addAction(41, "|", "s58");
        addAction(42, "(", "s59");
        addAction(43, "|", "r7");
        addAction(44, "if", "s9");
        addAction(45, "if", "s61");
        addAction(46, "begin", "s21");
        addAction(47, "else_if", "r2");
        addAction(5, "if", "s9");
        addAction(50, "|", "s65");
        addAction(52, "identifier", "s68");
        addAction(53, "|", "s69");
        addAction(54, "|", "r22");
        addAction(55, "|", "r23");
        addAction(56, "|", "s70");
        addAction(57, "while", "s19");
        addAction(58, "identifier", "s74");
        addAction(59, "int", "s77");
        addAction(6, "end", "s10");
        addAction(60, "end", "s78");
        addAction(62, "else_if", "s32");
        addAction(63, "*", "s25");
        addAction(63, "+", "s24");
        addAction(63, "begin", "r17");
        addAction(64, "*", "s53");
        addAction(64, "|", "s82");
        addAction(65, ">", "s83");
        addAction(66, "*", "s53 / r18");
        addAction(66, "+", "r18");
        addAction(66, "|", "r18");
        addAction(67, "|", "s85");
        addAction(68, "|", "s86");
        addAction(69, "/", "s87");
        addAction(7, "if", "s9");
        addAction(70, "for", "s42");
        addAction(71, "|", "s89");
        addAction(72, ";", "s90");
        addAction(73, "|", "s91");
        addAction(74, "=", "s92");
        addAction(75, ";", "s93");
        addAction(76, "identifier", "s95");
        addAction(77, "|", "s96");
        addAction(78, "|", "r2");
        addAction(79, "begin", "s21");
        addAction(8, "|", "s13");
        addAction(80, "|", "s98");
        addAction(83, "|", "s101");
        addAction(84, "identifier", "s68");
        addAction(85, "identifier", "s104");
        addAction(86, "number_literal", "s105");
        addAction(87, "identifier", "r24");
        addAction(88, "|", "s106");
        addAction(89, "for", "s42");
        addAction(90, "if", "r4");
        addAction(91, "print_line", "s109");
        addAction(94, ",", "s115");
        addAction(95, "|", "s116");
        addAction(96, "integer", "s117");
        addAction(97, "else_if", "s119");
        addAction(99, "*", "s53");
        addAction(99, "|", "s120");

        // GOTO
        addGoto(0, "PROGRAM", "1");
        addGoto(100, "MULTIPLYING_OPERATOR", "121");
        addGoto(106, "ASSIGNMENT_STMT", "73");
        addGoto(106, "SIMPLE_STMT", "126");
        addGoto(111, "ADDING_OPERATOR", "23");
        addGoto(113, "ADDING_OPERATOR", "23");
        addGoto(115, "VARIABLE_DECLARATION", "133");
        addGoto(119, "EXPRESSION", "136");
        addGoto(119, "SIMPLE_EXPRESSION", "15");
        addGoto(12, "CONDITIONAL_STMT", "8");
        addGoto(12, "STMT", "12");
        addGoto(12, "STMTS", "17");
        addGoto(120, "TERM", "137");
        addGoto(121, "FACTOR", "138");
        addGoto(123, "FACTOR", "140");
        addGoto(127, "ASSIGNMENT_STMT", "73");
        addGoto(127, "SIMPLE_STMT", "145");
        addGoto(128, "DECLARATION_STMT", "146");
        addGoto(128, "TYPE", "147");
        addGoto(13, "WHILE_STMT", "18");
        addGoto(130, "SIMPLE_EXPRESSION", "149");
        addGoto(131, "EXPRESSION", "150");
        addGoto(131, "SIMPLE_EXPRESSION", "151");
        addGoto(132, "SIMPLE_EXPRESSION", "152");
        addGoto(133, "VARIABLE_DECLARATIONS", "153");
        addGoto(135, "COMPOUND_STMT", "156");
        addGoto(136, "COMPOUND_STMT", "157");
        addGoto(137, "MULTIPLYING_OPERATOR", "158");
        addGoto(14, "COMPOUND_STMT", "20");
        addGoto(143, "EXPRESSION", "163");
        addGoto(143, "SIMPLE_EXPRESSION", "151");
        addGoto(147, "VARIABLE_DECLARATION", "166");
        addGoto(149, "ADDING_OPERATOR", "49");
        addGoto(149, "RELATIONAL_OPERATOR", "168");
        addGoto(15, "ADDING_OPERATOR", "23");
        addGoto(151, "ADDING_OPERATOR", "23");
        addGoto(152, "ADDING_OPERATOR", "49");
        addGoto(152, "RELATIONAL_OPERATOR", "172");
        addGoto(154, "VARIABLE_DECLARATION", "175");
        addGoto(155, "EXPRESSION", "176");
        addGoto(155, "SIMPLE_EXPRESSION", "177");
        addGoto(157, "ELSE_IF_STMT", "178");
        addGoto(158, "FACTOR", "179");
        addGoto(159, "FACTOR", "180");
        addGoto(16, "CONDITIONAL_STMT", "27");
        addGoto(16, "STMT", "26");
        addGoto(165, "BREAK_STMT", "185");
        addGoto(166, "VARIABLE_DECLARATIONS", "187");
        addGoto(168, "SIMPLE_EXPRESSION", "189");
        addGoto(170, "COMPOUND_STMT", "191");
        addGoto(171, "SIMPLE_EXPRESSION", "192");
        addGoto(172, "SIMPLE_EXPRESSION", "193");
        addGoto(175, "VARIABLE_DECLARATIONS", "195");
        addGoto(177, "ADDING_OPERATOR", "23");
        addGoto(183, "EXPRESSION", "201");
        addGoto(183, "SIMPLE_EXPRESSION", "151");
        addGoto(189, "ADDING_OPERATOR", "23");
        addGoto(19, "EXPRESSION", "30");
        addGoto(19, "SIMPLE_EXPRESSION", "15");
        addGoto(192, "ADDING_OPERATOR", "49");
        addGoto(192, "RELATIONAL_OPERATOR", "206");
        addGoto(193, "ADDING_OPERATOR", "208");
        addGoto(196, "SIMPLE_EXPRESSION", "211");
        addGoto(198, "FACTOR", "212");
        addGoto(20, "ELSE_IF_STMT", "31");
        addGoto(203, "DISPLAY_STMT", "218");
        addGoto(206, "SIMPLE_EXPRESSION", "222");
        addGoto(208, "TERM", "224");
        addGoto(21, "CONDITIONAL_STMT", "8");
        addGoto(21, "STMT", "7");
        addGoto(21, "STMTS", "33");
        addGoto(211, "ADDING_OPERATOR", "49");
        addGoto(211, "RELATIONAL_OPERATOR", "226");
        addGoto(215, "EXPRESSION", "230");
        addGoto(215, "SIMPLE_EXPRESSION", "151");
        addGoto(22, "SIMPLE_EXPRESSION", "34");
        addGoto(222, "ADDING_OPERATOR", "236");
        addGoto(224, "MULTIPLYING_OPERATOR", "52");
        addGoto(226, "SIMPLE_EXPRESSION", "240");
        addGoto(23, "TERM", "35");
        addGoto(236, "TERM", "249");
        addGoto(238, "TERM", "251");
        addGoto(240, "ADDING_OPERATOR", "253");
        addGoto(243, "EXPRESSION", "256");
        addGoto(243, "SIMPLE_EXPRESSION", "151");
        addGoto(249, "MULTIPLYING_OPERATOR", "52");
        addGoto(251, "MULTIPLYING_OPERATOR", "263");
        addGoto(253, "TERM", "265");
        addGoto(261, "TERM", "270");
        addGoto(263, "FACTOR", "272");
        addGoto(265, "MULTIPLYING_OPERATOR", "52");
        addGoto(267, "EXPRESSION", "275");
        addGoto(267, "SIMPLE_EXPRESSION", "151");
        addGoto(270, "MULTIPLYING_OPERATOR", "278");
        addGoto(273, "TERM", "281");
        addGoto(278, "FACTOR", "285");
        addGoto(28, "CONDITIONAL_STMT", "40");
        addGoto(28, "STMT", "39");
        addGoto(280, "FACTOR", "286");
        addGoto(281, "MULTIPLYING_OPERATOR", "288");
        addGoto(288, "FACTOR", "294");
        addGoto(29, "FOR_STMT", "41");
        addGoto(292, "FACTOR", "296");
        addGoto(299, "FACTOR", "303");
        addGoto(3, "COMPOUND_STMT", "4");
        addGoto(30, "COMPOUND_STMT", "43");
        addGoto(306, "EXPRESSION", "309");
        addGoto(306, "SIMPLE_EXPRESSION", "151");
        addGoto(311, "EXPRESSION", "314");
        addGoto(311, "SIMPLE_EXPRESSION", "151");
        addGoto(316, "EXPRESSION", "319");
        addGoto(316, "SIMPLE_EXPRESSION", "151");
        addGoto(32, "EXPRESSION", "46");
        addGoto(32, "SIMPLE_EXPRESSION", "15");
        addGoto(34, "ADDING_OPERATOR", "49");
        addGoto(34, "RELATIONAL_OPERATOR", "48");
        addGoto(35, "MULTIPLYING_OPERATOR", "52");
        addGoto(38, "WHILE_STMT", "56");
        addGoto(44, "CONDITIONAL_STMT", "8");
        addGoto(44, "STMT", "7");
        addGoto(44, "STMTS", "60");
        addGoto(46, "COMPOUND_STMT", "62");
        addGoto(48, "SIMPLE_EXPRESSION", "63");
        addGoto(49, "TERM", "64");
        addGoto(5, "CONDITIONAL_STMT", "8");
        addGoto(5, "STMT", "7");
        addGoto(5, "STMTS", "6");
        addGoto(51, "TERM", "66");
        addGoto(52, "FACTOR", "67");
        addGoto(57, "WHILE_STMT", "71");
        addGoto(58, "ASSIGNMENT_STMT", "73");
        addGoto(58, "SIMPLE_STMT", "72");
        addGoto(59, "DECLARATION_STMT", "75");
        addGoto(59, "TYPE", "76");
        addGoto(61, "EXPRESSION", "79");
        addGoto(61, "SIMPLE_EXPRESSION", "15");
        addGoto(62, "ELSE_IF_STMT", "80");
        addGoto(63, "ADDING_OPERATOR", "81");
        addGoto(64, "MULTIPLYING_OPERATOR", "52");
        addGoto(66, "MULTIPLYING_OPERATOR", "84");
        addGoto(7, "CONDITIONAL_STMT", "8");
        addGoto(7, "STMT", "12");
        addGoto(7, "STMTS", "11");
        addGoto(70, "FOR_STMT", "88");
        addGoto(76, "VARIABLE_DECLARATION", "94");
        addGoto(79, "COMPOUND_STMT", "97");
        addGoto(81, "TERM", "99");
        addGoto(82, "TERM", "100");
        addGoto(84, "FACTOR", "102");
        addGoto(85, "FACTOR", "103");
        addGoto(89, "FOR_STMT", "107");
        addGoto(9, "EXPRESSION", "14");
        addGoto(9, "SIMPLE_EXPRESSION", "15");
        addGoto(91, "PRINT_STMT", "108");
        addGoto(92, "EXPRESSION", "110");
        addGoto(92, "SIMPLE_EXPRESSION", "111");
        addGoto(93, "EXPRESSION", "112");
        addGoto(93, "SIMPLE_EXPRESSION", "113");
        addGoto(94, "VARIABLE_DECLARATIONS", "114");
        addGoto(97, "ELSE_IF_STMT", "118");
        addGoto(99, "MULTIPLYING_OPERATOR", "52");
    }

    private void addAction(int state, String symbol, String action) {
        actionTable.putIfAbsent(state, new HashMap<>());
        actionTable.get(state).put(symbol, action);
    }

    private void addGoto(int state, String symbol, String nextState) {
        gotoTable.putIfAbsent(state, new HashMap<>());
        gotoTable.get(state).put(symbol, Integer.parseInt(nextState));
    }

    private String getLHS(int productionNumber) {
        // Define LHS for each production
        switch (productionNumber) {
            case 1: return "PROGRAM";
            // Add more cases for each production rule
            default: return "";
        }
    }

    private String[] getRHS(int productionNumber) {
        // Define RHS for each production
        switch (productionNumber) {
            case 1: return new String[] {"program", "IDENTIFIER", "COMPOUND_STMT"};
            // Add more cases for each production rule
            default: return new String[] {};
        }
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
        stateStack.push(0); // initial state, 0

        while (true) {
            int currentState = stateStack.peek();
            Token currentToken = tokenList.get(tokenIdx);
            // get action based on [state, token]
            String action = actionTable.get(currentState).get(currentToken.getTokenName());

            if (action == null) {
                parsingErrorMsg = "Syntax error at token index " + tokenIdx + ": " + currentToken.getTokenName();
                return 1;
            }

            if (action.equals("ACCEPT")) {
                // Accept
                return 0;
            } else if (action.charAt(0) == 's') { // ex. s5, 's'hift to 5
                // Shift action
                int nextState = Integer.parseInt(action.substring(1));
                stateStack.push(nextState);
                symbolStack.push(currentToken.getTokenName());
                tokenIdx++;

                System.out.println("[S] " + currentToken.getTokenName() + " " + action);
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

                System.out.println("[R] " + currentToken.getTokenName() + " " + action);
            }
        }
    }

}
