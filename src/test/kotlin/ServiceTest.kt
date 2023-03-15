import org.junit.Assert.assertEquals
import org.junit.Test

class ServiceTest {
    private val ACCOUNT_STEPAN = PersonAccount(1, "stepan", "123456")
    private val ACCOUNT_NIKITA = PersonAccount(2, "NIKITA", "123698")
    private val ACCOUNT_FEDYA = PersonAccount(3, "fdEya", "000000")
    private val ACCOUNT_ALEXEY = PersonAccount(4, "LEXA", "111111")
    private val chat1: Chat = Chat(1, ACCOUNT_STEPAN.id);
    private val chat2: Chat = Chat(2, ACCOUNT_NIKITA.id);
    private val chat3: Chat = Chat(3, ACCOUNT_FEDYA.id);
    private val chat4: Chat = Chat(4, ACCOUNT_ALEXEY.id);

    private val service = Service()


    @Test
    fun addChat() {
        val result = service.addChat(chat1)
        assertEquals(chat1, result)
    }

    @Test
    fun createMessage() {
        val result = service.createMessage("Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id)
        assertEquals(Message(0, "Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id), result)
    }
    @Test(expected = ReceiverNotFoundExceptiom::class)
    fun chatCreateMessageReceiverNotFoundException() {
         chat1.createMessage("Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_ALEXEY.id)
    }
    @Test(expected = InvalidArgumentMessageException::class)
    fun createMessageInvalidArgumentException() {
        val result = service.createMessage("Hello", ACCOUNT_FEDYA.id, ACCOUNT_STEPAN.id)
        assertEquals(Message(0, "Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id), result)
    }

    @Test
    fun removeMessage() {
        service.createMessage("Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id)
        val result = service.removeMessage(Message(0, "Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id))
        assertEquals(true, result)
    }


    @Test(expected = InvalidArgumentMessageException::class)
    fun removeMessageInvalidArgumentException() {
        service.createMessage("Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id)
        service.removeMessage(Message(0, "Hello", ACCOUNT_ALEXEY.id, ACCOUNT_STEPAN.id))
    }
    @Test(expected = ChatNotFoundException::class)
    fun removeMessageChatNotFoundException() {
        service.createMessage("Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id)
        service.removeMessage(Message(0, "Hello", ACCOUNT_ALEXEY.id, MY_PERSON_ACCOUNT.id))
    }
    @Test
    fun getUnreadChatsCount() {
        service.createMessage("Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id)
        val result = service.getUnreadChatsCount()
        assertEquals(1, result)
    }

    @Test
    fun getListMessage() {
        service.createMessage("Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id)
        service.apply {
            addChat(chat2)
            addChat(chat3)
            addChat(chat4)
        }
        service.createMessage("How are you?", MY_PERSON_ACCOUNT.id, ACCOUNT_ALEXEY.id)
        service.createMessage("I am fine", ACCOUNT_ALEXEY.id, MY_PERSON_ACCOUNT.id)
        val result = service.getListMessage(4, 1, 1)
        assertEquals(listOf(Message(2, "I am fine", ACCOUNT_ALEXEY.id, MY_PERSON_ACCOUNT.id)), result)
    }
    @Test(expected = ChatNotFoundException::class)
    fun getListMessageChatNotFoundException() {
        service.createMessage("Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id)
        service.apply {
            addChat(chat2)
            addChat(chat3)
            addChat(chat4)
        }
        service.createMessage("How are you?", MY_PERSON_ACCOUNT.id, ACCOUNT_ALEXEY.id)
        service.createMessage("I am fine", ACCOUNT_ALEXEY.id, MY_PERSON_ACCOUNT.id)
        service.getListMessage(5, 1, 1)
    }
    @Test(expected = MessageNotFountException::class)
    fun getListMessageMessageNotFoundException() {
        service.createMessage("Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id)
        service.apply {
            addChat(chat2)
            addChat(chat3)
            addChat(chat4)
        }
        service.createMessage("How are you?", MY_PERSON_ACCOUNT.id, ACCOUNT_ALEXEY.id)
        service.createMessage("I am fine", ACCOUNT_ALEXEY.id, MY_PERSON_ACCOUNT.id)
        service.getListMessage(4, 2, 1)
    }
    @Test
    fun getLastMessageChats() {
        service.createMessage("Hello", MY_PERSON_ACCOUNT.id, ACCOUNT_STEPAN.id)
        service.apply {
            addChat(chat2)
            addChat(chat3)
            addChat(chat4)
        }
        service.createMessage("How are you?", MY_PERSON_ACCOUNT.id, ACCOUNT_ALEXEY.id)
        service.createMessage("I am fine", ACCOUNT_ALEXEY.id, MY_PERSON_ACCOUNT.id)
        val result = service.getLastMessageChats()
        assertEquals(listOf("Hello", "Нет сообщений", "Нет сообщений", "I am fine"), result)
    }

}