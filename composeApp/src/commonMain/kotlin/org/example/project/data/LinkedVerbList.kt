package org.example.project.data

class LinkedList<T> {
    private var head: Node<T>? = null
    private var current: Node<T>? = null


    fun actual(): T? = current?.item
    fun add(verb: T) {
        val newNode = Node<T>(verb)
        if (head == null) {
            head = newNode
            current = head
        } else {
            var temp = head
            while (temp?.next != null) {
                temp = temp.next
            }
            temp?.next = newNode
        }
    }


    fun next(): T? {
        return if (current?.next != null) {
            current = current?.next
            current?.item
        } else {
            null
        }
    }
}

data class Node<T>(val item: T) {
    var next: Node<T>? = null
}


fun <T> List<T>.toLinkedList(): LinkedList<T> {
    return LinkedList<T>().apply {
        this@toLinkedList.map {
            this.add(it)
        }

    }
}