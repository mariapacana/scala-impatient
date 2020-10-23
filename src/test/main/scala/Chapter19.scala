import java.util.{Calendar, Date}
import scala.collection.mutable
import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.combinator.lexical.StdLexical
import scala.util.parsing.combinator.syntactical.StandardTokenParsers
import scala.util.parsing.input.CharArrayReader.EofCh
import scala.util.parsing.input.Positional
import scala.xml._

object Chapter19 {

  /**
   * Task 1:
   *
   * Add `/` and `%` operations to the arithmetic expression evaluator.
   */
  class ExprEvaluator extends RegexParsers {

    val number = "[0-9]+".r

    def eval(e: String): Int = parseAll(expr, e).get

    def expr: Parser[Int] = term ~ rep(("+" | "-") ~ term ^^ {
      case "+" ~ t => t
      case "-" ~ t => -t
    }) ^^ {
      case t ~ r => t + r.sum
    }

    def term: Parser[Int] = factor ~ rep(("*" | "/" | "%") ~ factor) ^^ {
      case f ~ r => r.foldLeft(f)((b, a) => a._1 match {
        case "*" => b * a._2
        case "/" => b / a._2
        case "%" => b % a._2
      })
    }

    def factor: Parser[Int] = number ^^ { _.toInt } | "(" ~ expr ~ ")" ^^ {
      case _ ~ e ~ _ => e
    }
  }

  /**
   * Task 2:
   *
   * Add a `^` operator to the arithmetic expression evaluator. As in mathematics, `^` should have
   * a higher precedence than multiplication, and it should be right associative.
   * That is, `4^2^3` should be `4^(2^3)`, or `65536`.
   */
  class ExprEvaluator2 extends RegexParsers {

    val number = "[0-9]+".r

    def eval(e: String): Int = parseAll(expr, e).get

    def expr: Parser[Int] = term ~ rep(("+" | "-") ~ term ^^ {
      case "+" ~ t => t
      case "-" ~ t => -t
    }) ^^ {
      case t ~ r => t + r.sum
    }

    def term: Parser[Int] = power ~ rep(("*" | "/" | "%") ~ power) ^^ {
      case p ~ r => r.foldLeft(p)((b, a) => a._1 match {
        case "*" => b * a._2
        case "/" => b / a._2
        case "%" => b % a._2
      })
    }

    def power: Parser[Int] = rep(factor ~ "^") ~ factor ^^ {
      case r ~ f => r.foldRight(f)((a, b) => a._2 match {
        case "^" => math.pow(a._1, b).toInt
      })
    }

    def factor: Parser[Int] = number ^^ { _.toInt } | "(" ~ expr ~ ")" ^^ {
      case _ ~ e ~ _ => e
    }
  }

  /**
   * Task 3:
   *
   * Write a parser that parses a list of integers (such as `(1, 23, -79)`) into a `List[Int]`.
   */
  class IntListParser extends RegexParsers {

    val number = "-?[0-9]+".r

    def parse(e: String): List[Int] = {
      val result = parseAll(list, e)
      if (!result.successful) {
        throw new IllegalArgumentException("Parsing failed: " + result)
      }

      result.get
    }

    def list: Parser[List[Int]] = "(" ~> repsep(number ^^ { _.toInt }, ",") <~ ")"
  }

  /**
   * Task 4:
   *
   * Write a parser that can parse date and time expressions in ISO 8601.
   * Your parser should return a `java.util.Date` object.
   */
  class DateTimeParser extends RegexParsers {

    val numRegex = """\d{2}""".r
    val millisRegex = """\d{3}""".r
    val yearRegex = """\d{4}""".r

    def parse(e: String): Date = {
      val result = parseAll(dateTime, e)
      if (!result.successful) {
        throw new IllegalArgumentException("Parsing failed: " + result)
      }

      result.get
    }

    def dateTime: Parser[Date] = date ~ opt(time) ^^ {
      case (y, m, d) ~ None => makeDate(y, m, d, 0, 0, 0, 0)
      case (y, m, d) ~ Some((h, mm, s, ss)) => makeDate(y, m, d, h, mm, s, ss)
    }

    def date: Parser[(Int, Int, Int)] = year ~ month ~ day ^^ {
      case y ~ m ~ d => (y, m, d)
    }

    def time: Parser[(Int, Int, Int, Int)] = "T" ~> hour ~ minute ~ second ~ opt(millis) ^^ {
      case h ~ m ~ s ~ None => (h, m, s, 0)
      case h ~ m ~ s ~ Some(ss) => (h, m, s, ss)
    }

    def year: Parser[Int] = yearRegex ^^ { _.toInt }
    def month: Parser[Int] = opt("-") ~> numRegex ^^ { _.toInt }
    def day: Parser[Int] = opt("-") ~> numRegex ^^ { _.toInt }
    def hour: Parser[Int] = numRegex ^^ { _.toInt }
    def minute: Parser[Int] = opt(":") ~> numRegex ^^ { _.toInt }
    def second: Parser[Int] = opt(":") ~> numRegex ^^ { _.toInt }
    def millis: Parser[Int] = opt(".") ~> millisRegex ^^ { _.toInt }

    private def makeDate(y: Int, m: Int, d: Int, hh: Int, mm: Int, ss: Int, sss: Int): Date = {
      val cal = Calendar.getInstance()
      cal.set(y, m - 1, d, hh, mm, ss)
      cal.set(Calendar.MILLISECOND, sss)
      cal.getTime
    }
  }

  /**
   * Task 5:
   *
   * Write a parser that parses a subset of XML. Handle tags of the form `<ident>...</ident>` or
   * `<ident/>`. Tags can be nested. Handle attributes inside tags. Attribute values can be
   * delimited by single or double quotes. You don't need to deal with character data
   * (that is, text inside tags or CDATA sections).
   * Your parser should return a Scala XML `Elem` value.
   * The challenge is to reject mismatched tags. Hint: `into`, `accept`.
   */
  class IdentXMLParser extends RegexParsers {

    val attrKeyRegex = """[^</>='"]+""".r
    val singleQuotesRegex = """[^']+""".r
    val doubleQuotesRegex = """[^"]+""".r
    val textRegex = """[^<>]+""".r
    val cdataRegex = """(?s)<!\[CDATA\[.*?\]\]>""".r

    def parse(e: String): xml.Elem = {
      val result = parseAll(openCloseTag, e)
      if (!result.successful) {
        throw new IllegalArgumentException("Parsing failed: " + result)
      }

      result.get
    }

    def openCloseTag: Parser[xml.Elem] = tagOpen into { elem =>
      opt(textRegex) ~> rep(singleTag <~ opt(textRegex) | openCloseTag <~ opt(textRegex)) <~
        tagClose ^^ {
        case Nil => elem
        case r => elem.copy(child = r.filter(_ != null))
      }
    }

    def tagOpen: Parser[xml.Elem] = ("<" ~ tagName) ~> attrs <~ ">"
    def tagClose: Parser[String] = "</" ~> tagName <~ ">"

    def singleTag: Parser[xml.Elem] = ("<" ~ tagName) ~> attrs <~ "/>" | cdataRegex ^^ { _ => null }

    def tagName: Parser[String] = "ident" | failure("ident tag expected")

    def attrs: Parser[xml.Elem] = rep(attrPair) ^^ {
      case Nil => makeElem(Nil)
      case attrPairs => makeElem(attrPairs)
    }

    def makeElem(attrPairs: List[(String, String)]): xml.Elem = {
      val attributes = if (attrPairs.nonEmpty) {
        val attrs = for ((key, value) <- attrPairs) yield Attribute(null, key, value, Null)
        attrs.reduceRight((attr, next) => attr.copy(next = next))
      }
      else Null

      xml.Elem(null, "ident", attributes, TopScope, minimizeEmpty = false)
    }

    def attrPair: Parser[(String, String)] = attrKeyRegex ~ "=" ~ attrValue ^^ {
      case key ~ "=" ~ value => (key, value)
    }

    def attrValue: Parser[String] = "'" ~> singleQuotesRegex <~ "'" |
      "\"" ~> doubleQuotesRegex <~ "\""
  }

  /**
   * Task 6:
   *
   * Assume that the parser in Section 19.5, "Generating Parse Trees", on page 275 is completed with
   * {{{
   *  class ExprParser extends RegexParsers {
   *    def expr: Parser[Expr] = (term ~ opt(("+" | "-") ~ expr)) ^^ {
   *      case a ~ None => a
   *      case a ~ Some(op ~ b) => Operator(op, a, b)
   *    }
   *  }
   * }}}
   * Unfortunately, this parser computes an incorrect expression tree - operators with the same
   * precedence are evaluated right-to-left. Modify the parser so that the expression tree
   * is correct. For example, `3-4-5` should yield an
   * {{{
   *  Operator("-", Operator("-", 3, 4), 5)
   * }}}
   */
  class ExprParser extends StandardTokenParsers {

    lexical.delimiters += ("+", "-", "*", "(", ")")

    def parse(in: String): Expr = {
      parseExpr(in, expr)
    }

    protected def parseExpr[T <: Expr](in: String, expr: => Parser[T]): T = {
      val result = phrase(expr)(new lexical.Scanner(in))
      if (!result.successful) {
        throw new IllegalArgumentException("Parsing failed: " + result)
      }

      result.get
    }

    def expr: Parser[Expr] = term into { a =>
      rep1(("+" | "-") ~ term ^^ {
        case op ~ b => (op, b)
      }) ^^ {
        case pairs => pairs.foldLeft(a) { (a, pair) =>
          Operator(pair._1, a, pair._2)
        }
      }
    }

    def term: Parser[Expr] = factor ~ opt("*" ~> term) ^^ {
      case a ~ None => a
      case a ~ Some(b) => Operator("*", a, b)
    }

    def factor: Parser[Expr] = numericLit ^^ (n => Number(n.toInt)) |
      "(" ~> expr <~ ")"
  }

  class Expr
  case class Number(value: Int) extends Expr
  case class Operator(op: String, left: Expr, right: Expr) extends Expr

  /**
   * Task 7:
   *
   * Suppose in Section 19.6, "Avoiding Left Recursion", on page 276, we first parse an expr
   * into a list of `~` with operations and values:
   * {{{
   *  def expr: Parser[Int] = term ~ rep(("+" | "-") ~ term) ^^ {...}
   * }}}
   * To evaluate the result, we need to compute `((t0 +- t1) +- t2) +- ...`
   * Implement this computation as a `fold` (see Chapter 13).
   */
  class FoldExprEvaluator extends RegexParsers {

    val number = "[0-9]+".r

    def parse(e: String): Int = {
      val result = parseAll(expr, e)
      if (!result.successful) {
        throw new IllegalArgumentException("Parsing failed: " + result)
      }

      result.get
    }

    def expr: Parser[Int] = term into { a =>
      rep(("+" | "-") ~ term ^^ {
        case "+" ~ b => b
        case "-" ~ b => -b
      }) ^^ {
        case pairs => pairs.fold(a)(_ + _)
      }
    }

    def term: Parser[Int] = factor ~ opt("*" ~> term) ^^ {
      case a ~ None => a
      case a ~ Some(b) => a * b
    }

    def factor: Parser[Int] = number ^^ (_.toInt) | "(" ~> expr <~ ")"
  }

  /**
   * Task 8:
   *
   * Add variables and assignment to the calculator program. Variables are created when they
   * are first used. Uninitialized variables are zero. To print a value, assign it to the special
   * variable `out`.
   */
  class Calculator {

    protected val parser = new CalculatorParser

    def parseAndEval(input: String): Int = {
      eval(new mutable.HashMap[String, Int], parser.parse(input))
    }

    protected def eval(vars: mutable.HashMap[String, Int], expr: Expr): Int = expr match {
      case Block(list) => list.map(eval(vars, _)).lastOption.getOrElse(0)
      case Number(n) => n
      case Operator(op, left, right) => op match {
        case "+" => eval(vars, left) + eval(vars, right)
        case "-" => eval(vars, left) - eval(vars, right)
        case "*" => eval(vars, left) * eval(vars, right)
      }
      case Variable(name) => vars.getOrElse(name, 0)
      case Assignment(v, e) =>
        val res = eval(vars, e)
        if (v.name == "out") print(res)
        else vars(v.name) = res
        res
    }
  }

  class CalculatorParser extends {
    override val lexical = new StdLexical {
      // don't treat new line characters as whitespaces
      override def whitespaceChar = elem("space char", ch => ch <= ' ' && ch != EofCh && ch != '\n')
    }
  } with ExprParser {

    lexical.delimiters += ("=", "\n")

    override def parse(in: String): Block = {
      parseExpr(in, block)
    }

    def block: Parser[Block] = repsep(assign | expr, "\n") ^^ Block

    def assign: Parser[Assignment] = variable ~ "=" ~ (expr | factor) ^^ {
      case v ~ "=" ~ e => Assignment(v, e)
    }

    def variable: Parser[Variable] = ident ^^ Variable

    override def factor: Parser[Expr] = variable | super.factor
  }

  case class Block(list: List[Expr]) extends Expr
  case class Variable(name: String) extends Expr
  case class Assignment(variable: Variable, right: Expr) extends Expr

  /**
   * Task 9:
   *
   * Extend the preceding exercise into a parser for a programming language that has variable
   * assignments, `Boolean` expressions, and `if`/`else` and `while` statements.
   */
  class Program extends Calculator {

    override protected val parser = new ProgramParser

    override protected def eval(vars: mutable.HashMap[String, Int], expr: Expr): Int = expr match {
      case Condition(op, left, right) =>
        if (op match {
          case "<" => eval(vars, left) < eval(vars, right)
          case "<=" => eval(vars, left) <= eval(vars, right)
          case ">" => eval(vars, left) > eval(vars, right)
          case ">=" => eval(vars, left) >= eval(vars, right)
          case "==" => eval(vars, left) == eval(vars, right)
          case "!=" => eval(vars, left) != eval(vars, right)
        }) 1
        else 0
      case If(cond, block) =>
        if (eval(vars, cond) != 0) eval(vars, block)
        else 0
      case IfElse(cond, ifBlock, elseBlock) =>
        if (eval(vars, cond) != 0) eval(vars, ifBlock)
        else eval(vars, elseBlock)
      case While(cond, block) =>
        while (eval(vars, cond) != 0) eval(vars, block)
        0
      case _ =>
        super.eval(vars, expr)
    }
  }

  class ProgramParser extends CalculatorParser {

    lexical.reserved += ("if", "else", "while")
    lexical.delimiters += ("<", "<=", ">", ">=", "==", "!=", "{", "}")

    override def block: Parser[Block] = rep("\n") ~> repsep(assign | expr | factor |
      whileExpr | ifElse, rep("\n")) <~ rep("\n") ^^ Block

    def ifElse: Parser[Expr] = ("if" ~> "(" ~> cond <~ ")") ~ (delim("{") ~> block <~ delim("}")) ~
      opt("else" ~> delim("{") ~> block <~ delim("}")) ^^ {
      case cond ~ ifBlock ~ None => If(cond, ifBlock)
      case cond ~ ifBlock ~ Some(elseBlock) => IfElse(cond, ifBlock, elseBlock)
    }

    def whileExpr: Parser[Expr] = ("while" ~> "(" ~> cond <~ ")") ~
      (delim("{") ~> block <~ delim("}")) ^^ {
      case cond ~ block => While(cond, block)
    }

    def delim(d: String): Parser[String] = rep("\n") ~> d <~ rep("\n")

    def cond: Parser[Condition] = factor ~ ("<" | "<=" | ">" | ">=" | "==" | "!=") ~ factor ^^ {
      case left ~ op ~ right => Condition(op, left, right)
    }
  }

  case class Condition(op: String, left: Expr, right: Expr) extends Expr
  case class If(cond: Condition, block: Block) extends Expr
  case class IfElse(cond: Condition, ifBlock: Block, elseBlock: Block) extends Expr
  case class While(cond: Condition, block: Block) extends Expr

  /**
   * Task 10:
   *
   * Add function definitions to the programming language of the preceding exercise.
   */
  class FuncProgram extends Program {

    override protected val parser = new FuncProgramParser
    protected val fx = new mutable.HashMap[String, FuncDef]

    override protected def eval(vars: mutable.HashMap[String, Int], expr: Expr): Int = expr match {
      case fd: FuncDef =>
        // store function definition
        fx(fd.name) = fd
        0
      case fc: FuncCall =>
        val funcDefOpt = fx.get(fc.name)
        if (funcDefOpt.isEmpty) {
          throw new IllegalArgumentException("function definition not found at " +
            fc.pos + "\n" + fc.pos.longString)
        }
        val fd = funcDefOpt.get
        if (fd.args.length != fc.params.length) {
          throw new IllegalArgumentException("function call with wrong arguments number at " +
            fc.pos + "\n" + fc.pos.longString)
        }
        // pass arguments to function
        val funcArgs = new mutable.HashMap[String, Int]
        for ((arg, param) <- fd.args.zip(fc.params)) {
          funcArgs(arg) = eval(vars, param)
        }
        eval(funcArgs, fd.block)
      case _ =>
        super.eval(vars, expr)
    }
  }

  class FuncProgramParser extends ProgramParser {

    lexical.reserved += "def"
    lexical.delimiters += ","

    override def block: Parser[Block] = rep("\n") ~> repsep(assign | expr | factor |
      whileExpr | ifElse | funcDef, rep("\n")) <~ rep("\n") ^^ Block

    def funcDef: Parser[FuncDef] = "def" ~> ident ~ ("(" ~> repsep(ident, ",") <~ ")") ~
      (delim("{") ~> block <~ delim("}")) ^^ {
      case name ~ args ~ block => FuncDef(name, args, block)
    }

    def funcCall: Parser[FuncCall] = positioned(ident ~ ("(" ~>
      repsep(expr | factor, ",") <~ ")") ^^ {
      case name ~ params => FuncCall(name, params)
    })

    override def factor: Parser[Expr] = funcCall | super.factor
  }

  case class FuncDef(name: String, args: List[String], block: Block) extends Expr
  case class FuncCall(name: String, params: List[Expr]) extends Expr with Positional
}
