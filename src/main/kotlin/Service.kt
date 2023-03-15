import java.util.*
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList

class ChatNotFoundException : RuntimeException()
class MessageNotFountException : RuntimeException()
class InvalidArgumentMessageException : RuntimeException()

var chatId = Int.MIN_VALUE

class Service {
    val chats = ArrayList<Chat>()
    fun addChat(chat: Chat): Chat {
        return chat.also {
            chats.add(it)
        }
    }

    fun createMessage(messageText: String, senderId: Int, receiverId: Int): Message {
        if (senderId != MY_PERSON_ACCOUNT.id && receiverId != MY_PERSON_ACCOUNT.id) {
            throw InvalidArgumentMessageException()
        }
        chats.forEach {
            if (it.companionId == senderId || it.companionId == receiverId) {
                return it.createMessage(messageText, senderId, receiverId)
            }
        }
        val companion = if (senderId == MY_PERSON_ACCOUNT.id) receiverId else senderId
        addChat(Chat(chatId++, companion)).apply {
            return createMessage(messageText, senderId, receiverId)
        }
    }

    fun removeMessage(message: Message): Boolean {
        if (message.senderId != MY_PERSON_ACCOUNT.id && message.receiverId != MY_PERSON_ACCOUNT.id) {
            throw InvalidArgumentMessageException()
        }
        chats.forEach {
            if (it.companionId == message.senderId || it.companionId == message.receiverId) {
                return it.removeMessage(message)
            }
        }
        throw ChatNotFoundException()
    }

    fun getUnreadChatsCount(): Int {
        var unreadChatsCount = 0
        chats.forEach {
            if (it.listNotReadMessage.size != 0)
                unreadChatsCount++
        }
        return unreadChatsCount
    }

    fun getListMessage(idChat: Int, idMessage: Int, countMessage: Int): List<Message> {
        var count = 1
        val returnedListMessage = LinkedList<Message>()
        val targeChat = chats.find {
            it.id == idChat
        } ?: throw ChatNotFoundException()
        val indexPoint = targeChat.listMessage.indexOfFirst {
            it.id == idMessage
        }
        if (indexPoint == -1)
            throw MessageNotFountException()
        val iterator = targeChat.listMessage.listIterator(indexPoint)
        return returnedListMessage.apply {
            while (iterator.hasNext() && count <= countMessage) {
                iterator.next().also {
                    add(it)
                    targeChat.readMessages(it)
                }
                count++
            }
        }
    }

    fun getLastMessageChats(): List<String> {
        val returnedList = LinkedList<String>()
        return returnedList.apply {
            chats.forEach {
                try {
                    add(it.listMessage.last().text)
                } catch (e: NoSuchElementException) {
                    add("Нет сообщений")
                }
            }
        }
    }
}