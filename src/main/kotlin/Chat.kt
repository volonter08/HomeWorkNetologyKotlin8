import java.lang.RuntimeException

class ReceiverNotFoundExceptiom : RuntimeException()
class Chat(val id: Int, val companionId: Int) {
    var countMessage = 0
    private var idMessage = 0
    val listMessage = ArrayList<Message>()
    val listReadMessage = ArrayList<Message>()
    val listNotReadMessage = ArrayList<Message>()
    val listOutMessage = ArrayList<Message>()
    val listInMessage = ArrayList<Message>()
    fun createMessage(messageText: String, senderId: Int, receiverId: Int): Message {
        when (receiverId) {
            companionId -> return Message(idMessage++, messageText, MY_PERSON_ACCOUNT.id, companionId).also {
                countMessage++
                listMessage.add(it)
                listOutMessage.add(it)
                listNotReadMessage.add(it)
            }

            MY_PERSON_ACCOUNT.id -> return Message(idMessage++, messageText, companionId,  MY_PERSON_ACCOUNT.id).also {
                countMessage++
                listMessage.add(it)
                listInMessage.add(it)
                listNotReadMessage.add(it)
            }

            else -> throw ReceiverNotFoundExceptiom()
        }
    }

    fun removeMessage(message: Message): Boolean {
        var isRemoved = false
        listMessage.apply {
            isRemoved = remove(message)
        }
        listReadMessage.apply {
            remove(message)
        }
        listNotReadMessage.apply {
            remove(message)
        }
        listOutMessage.apply {
            remove(message)
        }
        listInMessage.apply {
            remove(message)
        }
        countMessage--
        return isRemoved
    }

    fun readMessages(message: Message) {
        if (listNotReadMessage.remove(message))
            listReadMessage.add(message)
    }

}