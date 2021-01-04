import java.util.{Calendar, Date}
import scala.collection.mutable

object Chapter20 {

  /**
   * Task 1:
   *
   * Add `/` and `%` operations to the arithmetic expression evaluator.
   */


  /**
   * Task 2:
   *
   * Add a `^` operator to the arithmetic expression evaluator. As in mathematics, `^` should have
   * a higher precedence than multiplication, and it should be right associative.
   * That is, `4^2^3` should be `4^(2^3)`, or `65536`.
   */

  /**
   * Task 3:
   *
   * Write a parser that parses a list of integers (such as `(1, 23, -79)`) into a `List[Int]`.
   */


  /**
   * Task 4:
   *
   * Write a parser that can parse date and time expressions in ISO 8601.
   * Your parser should return a `java.util.Date` object.
   */

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

  /**
   * Task 8:
   *
   * Add variables and assignment to the calculator program. Variables are created when they
   * are first used. Uninitialized variables are zero. To print a value, assign it to the special
   * variable `out`.
   */

  /**
   * Task 9:
   *
   * Extend the preceding exercise into a parser for a programming language that has variable
   * assignments, `Boolean` expressions, and `if`/`else` and `while` statements.
   */

  /**
   * Task 10:
   *
   * Add function definitions to the programming language of the preceding exercise.
   */
}
