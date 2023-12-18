import java.util.*

class Item(
    val name: String,
    var quantity: Int,
    val storagePeriod: Int,
    val carrierType: String
) {
    // Methods to interact with the item
    fun orderItem(quantityToOrder: Int) {
        println("Ordered $quantityToOrder units of '$name'")
        quantity += quantityToOrder
    }

    fun checkQuantity() {
        println("Quantity of '$name': $quantity")
    }

    fun removeExpiredItem() {
        if (storagePeriod <= 0) {
            println("Item '$name' has been removed due to expiration.")
            quantity = 0
        } else {
            println("Item '$name' is not expired yet.")
        }
    }
}

class InventoryManager {
    private val inventory = mutableMapOf<String, Item>()
    private val items: MutableList<Item> = mutableListOf()

    fun removeItemByName(name: String, quantityToRemove: Int) {
        val itemToRemove = items.find { it.name == name }
        if (itemToRemove != null) {
            if (itemToRemove.quantity >= quantityToRemove) {
                itemToRemove.quantity -= quantityToRemove
                println("$quantityToRemove units of '${itemToRemove.name}' removed from inventory.")
            } else {
                println("Not enough quantity of '${itemToRemove.name}' in inventory to remove.")
            }
        } else {
            println("Item '$name' not found in inventory.")
        }
    }

    fun addItem(item: Item) {
        inventory[item.name] = item
        println("Item '${item.name}' added to inventory.")
    }

    fun findItemByName(name: String): Item? {
        return inventory[name]
    }

    fun removeItemByName(name: String) {
        val removedItem = inventory.remove(name)
        if (removedItem != null) {
            println("Item '$name' has been removed from inventory.")
        } else {
            println("Item with name '$name' not found.")
        }
    }
}
fun main() {
    val scanner = Scanner(System.`in`)
    val manager = InventoryManager()

    var continueAdding = true
    while (continueAdding) {
        println("Enter item details:")
        print("Item name: ")
        val name = scanner.nextLine()

        print("Item quantity: ")
        val quantity = readInt(scanner)

        print("Storage period (in days): ")
        val storagePeriod = readInt(scanner)

        scanner.nextLine() // Очистить буфер

        print("Carrier type for the item: ")
        val carrierType = scanner.nextLine()

        val newItem = Item(name, quantity, storagePeriod, carrierType)
        manager.addItem(newItem)

        println("Do you want to add another item? (yes/no): ")
        var choice = scanner.nextLine()
        continueAdding = choice.equals("yes", ignoreCase = true)
    }

    var continueOrdering = true
    while (continueOrdering) {
        do {
            println("Enter the item name to order:")
            val itemName = scanner.nextLine()

            val itemToOrder = manager.findItemByName(itemName)
            if (itemToOrder != null) {
                print("Enter quantity to order for '$itemName': ")
                val quantityToOrder = readInt(scanner)

                if (quantityToOrder > itemToOrder.quantity) {
                    println("Not enough quantity of '$itemName' in inventory.")
                } else {
                    itemToOrder.orderItem(quantityToOrder)
                }
            } else {
                println("Item '$itemName' not found in inventory.")
            }

            println("Do you want to order another item? (yes/no): ")
            val choice = scanner.nextLine()
            continueOrdering = choice.equals("yes", ignoreCase = true)
        } while (continueOrdering)

        var option = 0
        while (option != 4) {
            println("\nChoose an option:")
            println("1. Check item quantity")
            println("2. Remove item from inventory")
            println("3. Add another item")
            println("4. Exit")

            val input = scanner.nextLine()
            if (input.isNotEmpty() && input.all { it.isDigit() }) {
                option = input.toInt()
                when (option) {
                    1 -> {
                        println("Enter the item name to check quantity:")
                        val itemName = scanner.nextLine()
                        val item = manager.findItemByName(itemName)
                        if (item != null) {
                            item.checkQuantity()
                        } else {
                            println("Item '$itemName' not found in inventory.")
                        }
                    }
                    2 -> {
                        println("Enter the item name to remove from inventory:")
                        val itemName = scanner.nextLine()
                        manager.removeItemByName(itemName)
                    }
                    3 -> {
                        continueOrdering = true
                        break // Выйти из текущего блока и вернуться к заказу товара
                    }
                    4 -> println("Program exited.")
                    else -> println("Invalid option.")
                }
            } else {
                println("Please enter a valid option (1, 2, 3, or 4).")
            }
        }
    }
}

fun readInt(scanner: Scanner): Int {
    while (true) {
        try {
            return scanner.nextInt()
        } catch (e: InputMismatchException) {
            println("Please enter a valid integer.")
            scanner.next() // Очистить некорректный ввод
        }
    }
}