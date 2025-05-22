# CafeteriaManagerSystem-Backend Test Suite

This directory contains unit tests for the main business logic and data models of the CafeteriaManagerSystem-Backend project. The tests are written using JUnit 5 (Jupiter) and are designed to ensure the correctness of core calculations and behaviors in the system.

## How to Run the Tests

You can run all tests using Maven:

```
mvn test
```

Or, you can run them from your IDE (such as IntelliJ IDEA or Eclipse) by right-clicking the `src/test/java` directory or individual test classes and selecting **Run Tests**.

> **Note:** Ensure your `pom.xml` is configured for JUnit 5 (see project root README or ask the maintainer if unsure).

## Sum Table Summary

| Test Class | Test Method | Input Values | Expected Sum | Notes |
|------------|-------------|--------------|--------------|-------|
| ItemTest | testSumImportExportQuantity | [10.0, -3.0, 5.0] | 12.0 | Mixed import/export |
| ItemTest | testSumImportExportQuantityForMultipleStacks | Stack1: [10.0, -2.0]<br>Stack2: [7.0, -1.0] | Stack1: 8.0<br>Stack2: 6.0 | Multiple stacks |
| ItemTest | testSumOnlyExports | [-5.0, -2.0] | -7.0 | Export only |
| ItemTest | testSumOnlyImports | [4.0, 6.0] | 10.0 | Import only |
| ItemTest | testSumZeroQuantities | [0.0, 0.0] | 0.0 | Zero values |
| ItemTest | testSumBySupplier | SupplierA: [5.0, -2.0]<br>SupplierB: [3.0] | SupplierA: 3.0<br>SupplierB: 3.0 | By supplier |
| OrderTest | testSumOfItemsInOrder | [2, 3] | 5 | Single order |
| OrderTest | testSumOfItemsInAllOrders | Order1: [2, 3]<br>Order2: [4] | 9 | Multiple orders |
| OrderTest | testSumTotalPriceInOrder | [(2, 5.0), (3, 7.5)] | 30.0 | Price calculation |
| OrderTest | testSumTotalPriceAcrossAllOrders | Order1: [(2, 5.0), (3, 7.5)]<br>Order2: [(4, 8.0)] | 62.0 | Cross-order price |
| OrderTest | testSumWithEmptyOrder | [] | 0 | Empty order |
| OrderTest | testSumWithEmptyOrdersList | [] | 0 | Empty list |
| OrderTest | testSumWithNegativeAndZeroQuantitiesAndPrices | [(-2, 5.0), (0, 7.5), (3, -8.0)] | Quantity: 1<br>Price: -34.0 | Mixed values |

---

## Detailed Test Case Documentation (with Inputs & Outputs)

### `ItemTest.java`
**Location:** `src/test/java/com/cs3332/data/object/storage/ItemTest.java`

- **testSumImportExportQuantity**
  - **Input:**
    - Items: [10.0 (import), -3.0 (export), 5.0 (import)]
  - **Output:**
    - Sum: 12.0
  - **Purpose:** Verifies correct sum of mixed import/export quantities for a single stack.

- **testSumImportExportQuantityForMultipleStacks**
  - **Input:**
    - Stack1: [10.0, -2.0], Stack2: [7.0, -1.0]
  - **Output:**
    - Stack1 sum: 8.0, Stack2 sum: 6.0
  - **Purpose:** Checks grouping and summing by stack.

- **testSumOnlyExports**
  - **Input:**
    - Items: [-5.0, -2.0]
  - **Output:**
    - Sum: -7.0
  - **Purpose:** Ensures negative-only (export) sums are correct.

- **testSumOnlyImports**
  - **Input:**
    - Items: [4.0, 6.0]
  - **Output:**
    - Sum: 10.0
  - **Purpose:** Ensures positive-only (import) sums are correct.

- **testSumZeroQuantities**
  - **Input:**
    - Items: [0.0, 0.0]
  - **Output:**
    - Sum: 0.0
  - **Purpose:** Ensures zero values are handled.

- **testSumBySupplier**
  - **Input:**
    - SupplierA: [5.0, -2.0], SupplierB: [3.0]
  - **Output:**
    - SupplierA sum: 3.0, SupplierB sum: 3.0
  - **Purpose:** Checks grouping and summing by supplier.

---

### `OrderTest.java`
**Location:** `src/test/java/com/cs3332/data/object/order/OrderTest.java`

- **testOrderConstructorAndGetters**
  - **Input:**
    - Order: tableID="T1", orderID=random, items=[OrderItem(productID=random, quantity=2, price=5.0)], ...
  - **Output:**
    - All fields match input values.
  - **Purpose:** Verifies constructor and getter correctness.

- **testSumOfItemsInOrder**
  - **Input:**
    - Items: [2, 3]
  - **Output:**
    - Sum: 5
  - **Purpose:** Sums item quantities in a single order.

- **testSumOfItemsInAllOrders**
  - **Input:**
    - Order1: [2, 3], Order2: [4]
  - **Output:**
    - Total sum: 9
  - **Purpose:** Sums item quantities across multiple orders.

- **testSumTotalPriceInOrder**
  - **Input:**
    - Items: [(2, 5.0), (3, 7.5)]
  - **Output:**
    - Total: 2*5.0 + 3*7.5 = 30.0
  - **Purpose:** Sums total price in a single order.

- **testSumTotalPriceAcrossAllOrders**
  - **Input:**
    - Order1: [(2, 5.0), (3, 7.5)], Order2: [(4, 8.0)]
  - **Output:**
    - Total: 2*5.0 + 3*7.5 + 4*8.0 = 62.0
  - **Purpose:** Sums total price across all orders.

- **testSumWithEmptyOrder**
  - **Input:**
    - Items: []
  - **Output:**
    - Sum: 0
  - **Purpose:** Handles empty order.

- **testSumWithEmptyOrdersList**
  - **Input:**
    - Orders: []
  - **Output:**
    - Sum: 0
  - **Purpose:** Handles empty order list.

- **testSumWithNegativeAndZeroQuantitiesAndPrices**
  - **Input:**
    - Items: [(-2, 5.0), (0, 7.5), (3, -8.0)]
  - **Output:**
    - Quantity sum: 1, Price sum: -10.0 + 0.0 + -24.0 = -34.0
  - **Purpose:** Handles negative and zero values.

---

### `OrderItemTest.java`
**Location:** `src/test/java/com/cs3332/data/object/order/OrderItemTest.java`

- **testOrderItemConstructorAndGetters**
  - **Input:**
    - productID=random, quantity=3, priceAtOrder=9.99
  - **Output:**
    - All fields match input values.
  - **Purpose:** Verifies constructor and getter correctness.

---

### `ProductTest.java`
**Location:** `src/test/java/com/cs3332/data/object/storage/ProductTest.java`

- **testProductConstructorAndGetters**
  - **Input:**
    - id=random, name="Coffee", unit="cup", recipe=[Ingredient(random, 2.0)], price=3.5
  - **Output:**
    - All fields match input values.
  - **Purpose:** Verifies constructor and getter correctness.

---

### `UserInformationTest.java`
**Location:** `src/test/java/com/cs3332/data/object/auth/UserInformationTest.java`

- **testUserInformationConstructorAndGetters**
  - **Input:**
    - username="user1", name="John Doe", email="john@example.com", phone="1234567890", dateOfBirth=123456789L, gender="Male", roles=["ADMIN", "MANAGER"]
  - **Output:**
    - All fields match input values.
  - **Purpose:** Verifies constructor and getter correctness.

---

### `UserAuthInformationTest.java`
**Location:** `src/test/java/com/cs3332/data/object/auth/UserAuthInformationTest.java`

- **testUserAuthInformationConstructorAndGetters**
  - **Input:**
    - username="user1", password="password123"
  - **Output:**
    - All fields match input values.
  - **Purpose:** Verifies constructor and getter correctness.

---

### `DataManagerTest.java`
**Location:** `src/test/java/com/cs3332/data/DataManagerTest.java`

- **testCreateAndValidateToken**
  - **Input:**
    - userID="user1" (token created for this ID)
    - token checked for "user1" (should be valid) and "otherUser" (should be invalid)
  - **Output:**
    - Token is not null, valid for correct user, invalid for others.
  - **Purpose:** Verifies token creation and validation logic.

- **testFullAccessTokenAlwaysValid**
  - **Input:**
    - token="FULL_ACCESS_TOKEN", any user ID
  - **Output:**
    - Always valid.
  - **Purpose:** Ensures special token is always valid.

- **testGetRoleWithFullAccessToken**
  - **Input:**
    - token="FULL_ACCESS_TOKEN"
  - **Output:**
    - Roles: [MANAGER, ADMIN]
  - **Purpose:** Checks correct roles for special token.

- **testGetAccountInformationWithFullAccessToken**
  - **Input:**
    - token="FULL_ACCESS_TOKEN"
  - **Output:**
    - UserInformation: username="Console", roles contains "MANAGER" and "ADMIN"
  - **Purpose:** Checks correct account info for special token.

---

## Adding More Tests

To add more tests, create new test classes in the appropriate package under `src/test/java`. Follow the structure of the existing tests and use JUnit 5 annotations.

If you have questions or need help writing more tests, please contact the project maintainer. 