/*
 Chapter0701a can't access the `greeting` object. It can access objects defined in
 com.horstmann.impatient, but not objects in intermediate packages com or com.horstmann.
 */
package com.horstmann {
  object greeting {
    def sayHello = println("Hello!")
  }
}

package com.horstmann.impatient {
  object innerGreeting {
    def sayHello = println("Hello! (inner)")
  }

  object Chapter0701a {
//    def sayHello = greeting.sayHello()
    def sayHello = innerGreeting.sayHello
  }
}

