package utest

import utest.framework.{TestSuite, Test}



object Core extends TestSuite{

  def tests = TestSuite{
    "assert"-{
      "success"-{
        assert(true)
        "success!"
      }
      "failure"-{
        try {
          val x = 1
          val y = "2"
          assert(
            x > 0,
            x == y
          )
          Predef.assert(false)
        } catch { case e @ AssertionError(_, logged, cause) =>

          val expected = Seq(LoggedValue("x", "Int", 1), LoggedValue("y", "String", "2"))

          Predef.assert(
            cause == null,
            "cause should be null for boolean failure"
          )

          Predef.assert(
            logged == expected,
            "Logging didn't capture the locals properly " + logged
          )

          Predef.assert(
            e.toString.contains("y: String = 2") && e.toString.contains("x: Int = 1"),
            "Logging doesn't display local values properly " + e.toString
          )

          Predef.assert(
            e.toString.contains("x == y"),
            "Message didnt contain source text " + e.toString
          )



          "caught it! " + logged
        }
      }
      "failureWithException"-{
        val x = 1L
        val y = 0l
        try {
          assert(x / y == 10)
          Predef.assert(false)
        } catch {case e @ AssertionError(src, logged, cause) =>
          Predef.assert(cause.isInstanceOf[ArithmeticException])
          Predef.assert(cause.getMessage == "/ by zero")
          e.getMessage
        }
      }

      "tracingOnFailure"-{
        try {
          val a = "i am cow"
          val b = 31337
          val c = 98
          assert(a + b == c.toString)
        } catch { case e: AssertionError =>
          e.getMessage.contains("i am cow")
          e.getMessage.contains("31337")
          e.getMessage.contains("98")
        }
      }
    }

    "intercept"-{
      "success"-{
        intercept[MatchError]{
          (0: Any) match { case _: String => }
        }
      }
      "failure"-{
        try {
          intercept[NumberFormatException]{
            (0: Any) match { case _: String => }
          }
          Predef.assert(false) // error wasn't thrown???
        } catch { case e: MatchError =>
          "raised"
        }
      }
    }
    "assertMatch"-{
      "success"-{
        val thing = Seq(1, 2, 3)
        assertMatch(thing){case Seq(1, _, 3) =>}
        ()
      }
      "failure"-{
        try {
          assertMatch(Seq(1, 2, 3)){case Seq(1, 2) =>}
          Predef.assert(false)
        } catch{ case e: java.lang.AssertionError =>
          e.getMessage
        }
      }
    }
  }
}
