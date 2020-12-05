package Chapter1007

abstract class SavingsAccount extends Account with Logger {
  def withdraw(amount: Double) {
    if (amount > balance) severe("Insufficient funds")
    else balance -= amount
  }

  // More methods ...
}
