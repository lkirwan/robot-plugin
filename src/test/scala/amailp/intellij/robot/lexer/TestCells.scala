package amailp.intellij.robot.lexer

import java.util.Scanner

import amailp.intellij.robot.elements.RobotTokenTypes



class TestCells extends BaseLexerTest {
  test("Variable") {
    scanString("${a_Variable}")
    nextTokenIsType(RobotTokenTypes.Variable)
  }

  test("ListVariable") {
    scanString("@{aListVariable}")
    nextTokenIsType(RobotTokenTypes.ListVariable)
  }

  test("Word") {
    scanString("ThisIsAWord")
    nextTokenIsType(RobotTokenTypes.Word)
  }

  test("WordWithSymbols") {
    scanString("!IsThisAWord??.")
    nextTokenIsType(RobotTokenTypes.Word)
  }

  test("testCell") {
    scanString("    A cell")
    nextTokenIsType(RobotTokenTypes.Separator)
    nextTokenIs("A", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.Space)
    nextTokenIs("cell", RobotTokenTypes.Word)
  }

  test("VariableCell") {
    scanString("  This ${is} cell  \n")
    nextTokenIsType(RobotTokenTypes.Separator)
    nextTokenIs("This", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.Space)
    nextTokenIs("${is}", RobotTokenTypes.Variable)
    nextTokenIsType(RobotTokenTypes.Space)
    nextTokenIs("cell", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.IrrelevantSpaces)
    nextTokenIsType(RobotTokenTypes.LineTerminator)
  }

  test("VariableQuotedCell") {
    scanString("  This '${is}' cell  \n")
    nextTokenIsType(RobotTokenTypes.Separator)
    nextTokenIs("This", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.Space)
    nextTokenIs("'", RobotTokenTypes.Word)
    nextTokenIs("${is}", RobotTokenTypes.Variable)
    nextTokenIs("'", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.Space)
    nextTokenIs("cell", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.IrrelevantSpaces)
    nextTokenIsType(RobotTokenTypes.LineTerminator)
  }

  test("NonVariableCell") {
    scanString("  This $ {is} notCell  \n")
    nextTokenIsType(RobotTokenTypes.Separator)
    nextTokenIs("This", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.Space)
    nextTokenIs("$", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.Space)
    nextTokenIs("{is}", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.Space)
    nextTokenIs("notCell", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.IrrelevantSpaces)
    nextTokenIsType(RobotTokenTypes.LineTerminator)
  }

  test("NotEndedVariableCell") {
    scanString("This ${is notCell\n")
    nextTokenIs("This", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.Space)
    nextTokenIs("$", RobotTokenTypes.Word)
    nextTokenIs("{is", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.Space)
    nextTokenIs("notCell", RobotTokenTypes.Word)
    nextTokenIsType(RobotTokenTypes.LineTerminator)
  }

  test("Comment") {
    scanString("  #This is comment  ")
    nextTokenIsType(RobotTokenTypes.IrrelevantSpaces)
    nextTokenIs("#This is comment  ", RobotTokenTypes.Comment)
  }

  test("BlankLine") {
    scanString("   \n")
    nextTokenIsType(RobotTokenTypes.BlankLine)
  }

  test("Ellipsis") {
    scanString(" ...\n")
    nextTokenIsType(RobotTokenTypes.Space)
    nextTokenIsType(RobotTokenTypes.Ellipsis)
    nextTokenIsType(RobotTokenTypes.LineTerminator)
  }

  test("All") {
    val s: String = new Scanner(this.getClass.getClassLoader.getResourceAsStream("complete.robot")).useDelimiter("\\A").next
    scanString(s)
    while (robotLexer.getTokenType != null) {
      System.out.println(robotLexer.getTokenType + "\t\t" + robotLexer.getTokenText)
      robotLexer.advance()
    }
  }
}
