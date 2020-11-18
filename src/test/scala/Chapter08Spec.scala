import org.scalatest.{FlatSpec, Matchers}
//import task0804.{Bundle, Item, SimpleItem}
//import task0806.{Circle, Rectangle, Shape}
//import task0807.Square
//import task0808.{Person, SecretAgent}

class Chapter08Spec extends FlatSpec with Matchers {

  "CheckingAccount" should "charge $1 for every deposit and withdrawal" in {
    val account = new CheckingAccount(100)
    account.deposit(5) shouldBe 104
    account.withdraw(5) shouldBe 98
  }

  "SavingsAccount" should "earn interest every month" in {
    val account = new SavingsAccount(100.0)
    account.deposit(5) shouldBe 105.0
    account.withdraw(5) shouldBe 100.0
    account.deposit(3) shouldBe 103.0
    account.withdraw(3) shouldBe 99.0
    account.deposit(2) shouldBe 100.0
    account.earnMonthlyInterest()
    account.getBalance shouldBe 101.0
    account.withdraw(5) shouldBe 96.0
  }

  "Car" should "print brand and model name" in {
    val myCar = Car("Camry", "Toyota")
    myCar.honk()
    myCar.modelName shouldBe("Camry")
    myCar.getBrand shouldBe("Toyota")
  }

  "Item" should "have SimpleItem and Bundle implementations" in {
    val item: Item = new SimpleItem(500, "iPhone 5s")
    item.price shouldBe 500
    item.description shouldBe "iPhone 5s"

    val bundle: Bundle = new Bundle
    bundle.isInstanceOf[Item] shouldBe true
    bundle.price shouldBe 0
    bundle.description shouldBe ""

    bundle.addItem(item).addItem(new SimpleItem(700, "iPhone 6"))
    bundle.price shouldBe 1200
    bundle.description shouldBe "iPhone 5s, iPhone 6"
  }

  "Point" should "have LabeledPoint subclass" in {
    import task0805.{LabeledPoint, Point}
    val point: LabeledPoint = new LabeledPoint("Black Thursday", 1929, 230.07)

    point.isInstanceOf[Point] shouldBe true
    point.label shouldBe "Black Thursday"
    point.x shouldBe 1929
    point.y shouldBe 230.07
  }

  "Shape06" should "have Rectangle and Circle subclasses" in {
    import task0805.Point
    import task0806._

    val circle: Shape = new Circle(3, new Point(1.0, 2.0))
    val circleCenter: Point = circle.centerPoint
    circleCenter.x shouldBe 1
    circleCenter.y shouldBe 2

    val rectangle: Rectangle = new Rectangle(2.0, 2.0, Point(1.0, 1.0))

    rectangle.topLeft.x shouldBe 0.0
    rectangle.topLeft.y shouldBe 2.0

    rectangle.bottomLeft.x shouldBe 0.0
    rectangle.bottomLeft.y shouldBe 0.0

    rectangle.topRight.x shouldBe 2.0
    rectangle.topRight.y shouldBe 2.0

    rectangle.bottomRight.x shouldBe 2.0
    rectangle.bottomRight.y shouldBe 0.0
  }

  "Square" should "extend java.awt.Rectangle and has three constructors" in {
    import java.awt.Rectangle

    val square1: Rectangle = new Square(1, 2, 3)
    square1.x shouldBe 1
    square1.y shouldBe 2
    square1.width shouldBe 3
    square1.height shouldBe 3

    val square2: Rectangle = new Square(4)
    square2.x shouldBe 0
    square2.y shouldBe 0
    square2.width shouldBe 4
    square2.height shouldBe 4

    val square3: Rectangle = new Square()
    square3.x shouldBe 0
    square3.y shouldBe 0
    square3.width shouldBe 0
    square3.height shouldBe 0
  }

  "SecretAgent" should "extend Person and hide name" in {
    val person: Person = new SecretAgent("Bond")
    person.name shouldBe "secret"
    person.toString shouldBe "secret"
  }
}
