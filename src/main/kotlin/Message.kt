data class Message(val id:Int,val text:String,val senderId:Int ,val receiverId:Int){
    override fun equals(other: Any?): Boolean =
        other is Message && ((text == other.text &&
                (senderId == other.senderId )&& receiverId==other.receiverId))
}