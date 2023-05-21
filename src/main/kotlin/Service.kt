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
        return chats.fold(0){
            acc, chat ->
            acc + if (chat.listNotReadMessage.size!=0) 1 else 0
        }
        /*var unreadChatsCount = 0
        chats.forEach {
            if (it.listNotReadMessage.size != 0)
                unreadChatsCount++
        }
        return unreadChatsCount

         */
    }

    fun getListMessage(idChat: Int, idMessage: Int, countMessage: Int): List<Message> {
        return chats.singleOrNull{
            it.id==idChat
        }.let {
            it?.listMessage?:throw ChatNotFoundException()
        }.asSequence().run {
            drop(if(indexOfFirst { it.id==idMessage }==-1) throw MessageNotFountException() else indexOfFirst { it.id==idMessage })
        }.take(countMessage).toList()
        /*var count = 1
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

         */
    }

    fun getLastMessageChats(): List<String> {
        return chats.asSequence().map {
            chat ->
            try {
                chat.listMessage.last().text
            } catch (e: NoSuchElementException) {
                "Нет сообщений"
            }
        }.toList()
        /*val returnedList = LinkedList<String>()
        return returnedList.apply {
            chats.forEach {
                try {
                    add(it.listMessage.last().text)
                } catch (e: NoSuchElementException) {
                    add("Нет сообщений")
                }
            }
        }

         */
    }
}